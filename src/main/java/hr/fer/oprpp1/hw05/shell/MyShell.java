package hr.fer.oprpp1.hw05.shell;

import hr.fer.oprpp1.hw05.shell.commands.CatCommand;
import hr.fer.oprpp1.hw05.shell.commands.CharsetsCommand;
import hr.fer.oprpp1.hw05.shell.commands.CopyCommand;
import hr.fer.oprpp1.hw05.shell.commands.ExitCommand;
import hr.fer.oprpp1.hw05.shell.commands.HelpCommand;
import hr.fer.oprpp1.hw05.shell.commands.HexdumpCommand;
import hr.fer.oprpp1.hw05.shell.commands.LsCommand;
import hr.fer.oprpp1.hw05.shell.commands.MkdirCommand;
import hr.fer.oprpp1.hw05.shell.commands.SymbolCommand;
import hr.fer.oprpp1.hw05.shell.commands.TreeCommand;

import java.util.Collections;
import java.util.Objects;
import java.util.Scanner;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * {@code MyShell} represents an implementation of a shell.
 * @author MatijaPav
 */

public class MyShell implements Environment{

    /**
     * {@code Character} that represents the beginning of a prompt in the shell window.
     */
    private Character promptSymbol;

    /**
     * {@code Character} that signalizes that there are more lines for the user input following the current line.
     */
    private Character morelinesSymbol;

    /**
     * {@code Character} signalizes that multiple lines are taken into account for the user input.
     */
    private Character multilineSymbol;

    /**
     * {@code SortedMap} of all available commands.
     */
    private SortedMap<String, ShellCommand> commands;

    /**
     * Status of the shell.
     */
    private ShellStatus status;

    private Scanner sc;

    public MyShell(){
        this.promptSymbol = '>';
        this.morelinesSymbol = '\\';
        this.multilineSymbol = '|';
        this.sc = new Scanner(System.in);
        this.status = ShellStatus.CONTINUE;
        this.commands = new TreeMap<>();
        getCommands();
    }


    /**
     * Reads the next line.
     * @return next line from the input.
     * @throws ShellIOException if it isn't possible to read the next line.
     */
    @Override
    public String readLine() throws ShellIOException {
        StringBuilder sb = new StringBuilder();
        String line = sc.nextLine();
        while(line.endsWith(getMorelinesSymbol().toString())){
            sb.append(line, 0, line.length() - 1);
            write(getMultilineSymbol().toString() + " ");
            line = sc.nextLine();
        }
        return sb.append(line).toString();
    }

    /**
     * Method {@code write} writes to console.
     * @param text string written to console.
     * @throws ShellIOException if writing to console fails.
     */
    @Override
    public void write(String text) throws ShellIOException {
        System.out.print(text);
    }

    /**
     * Writes to console and automatically goes to next line at the end of the input.
     * @param text string written to console.
     * @throws ShellIOException if writing to console fails.
     */
    @Override
    public void writeln(String text) throws ShellIOException {
        System.out.println(text);
    }

    /**
     * Unmodifiable map of the commands.
     * @return Unmodifiable {@code SortedMap}.
     */
    @Override
    public SortedMap<String, ShellCommand> commands() {
        return Collections.unmodifiableSortedMap(commands);
    }

    /**
     * Getter for the Multiline symbol.
     * @return {@code Character} that represents multiline symbol.
     */
    @Override
    public Character getMultilineSymbol() {
        return this.multilineSymbol;
    }

    /**
     * Sets the multiline symbol to the desired character.
     * @param symbol Desired character to represent Multiline symbol.
     */
    @Override
    public void setMultilineSymbol(Character symbol) {
        this.promptSymbol = Objects.requireNonNull(symbol, "Symbol cant be null!");
    }

    /**
     * Getter for the Prompt symbol.
     * @return {@code Character} that represents prompt symbol.
     */
    @Override
    public Character getPromptSymbol() {
        return this.promptSymbol;
    }

    /**
     * Sets the prompt symbol to the desired character.
     * @param symbol Desired character to represent prompt symbol.
     */
    @Override
    public void setPromptSymbol(Character symbol) {
        this.promptSymbol = Objects.requireNonNull(symbol, "Symbol cant be null!");
    }

    /**
     * Getter for the Morelines symbol.
     * @return {@code Character} that represents morelines symbol.
     */
    @Override
    public Character getMorelinesSymbol() {
        return this.morelinesSymbol;
    }

    /**
     * Sets the Morelines symbol to the desired character.
     * @param symbol Desired character to represent Morelines symbol.
     */
    @Override
    public void setMorelinesSymbol(Character symbol) {
        this.morelinesSymbol = Objects.requireNonNull(symbol, "Symbol cant be null!");
    }

    private void getCommands(){
        this.commands.put("help", new HelpCommand());
        this.commands.put("symbol", new SymbolCommand());
        this.commands.put("mkdir", new MkdirCommand());
        this.commands.put("exit", new ExitCommand());
        this.commands.put("ls", new LsCommand());
        this.commands.put("tree", new TreeCommand());
        this.commands.put("hexdump", new HexdumpCommand());
        this.commands.put("copy", new CopyCommand());
        this.commands.put("cat", new CatCommand());
        this.commands.put("charsets", new CharsetsCommand());
    }

    public static void main(String[] args) {
        MyShell shell = new MyShell();

        try {
            shell.writeln("Welcome to MyShell v 1.0");
            do {
                shell.write(shell.getPromptSymbol().toString() + " ");
                String[] input = shell.readLine().trim().split("\\s+", 2);
                ShellCommand cmd = shell.commands.get(input[0]);
                String arguments = input.length == 1 ? "" : input[1];
                if(cmd == null) {
                    shell.writeln("Invalid command!");
                    continue;
                }
                shell.status = cmd.executeCommand(shell, arguments);
            } while (shell.status != ShellStatus.TERMINATE);
        } catch (RuntimeException e){
            if(e.getClass() == ShellIOException.class)
                return;
            shell.writeln(e.getMessage());
        }


    }
}
