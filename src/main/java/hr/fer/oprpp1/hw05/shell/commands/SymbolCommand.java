package hr.fer.oprpp1.hw05.shell.commands;

import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellCommand;
import hr.fer.oprpp1.hw05.shell.ShellStatus;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class SymbolCommand implements ShellCommand {

    /**
     * Method tries to execute the current command in given {@code Environment env} with provided {@code arguments}
     * @param env {@code Environment} in which the command is run.
     * @param arguments Arguments with which the command is run.
     * @return {@code ShellStatus.CONTINUE} if shell should be kept running after the execution of the command,
     * {@code ShellStatus.TERMINATE} otherwise.
     */
    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        Objects.requireNonNull(env, "Environment can't be null!");
        Objects.requireNonNull(arguments, "Arguments can't be null!");

        String[] args = arguments.split("\\s+");
        int len = args.length;

        if(len < 1 || len > 2){
            env.writeln("Symbol takes one or two arguments!");
            return ShellStatus.CONTINUE;
        }
        if(len == 1){
            switch (args[0].toLowerCase()){
                case "prompt":
                    env.writeln("Symbol for prompt is " + env.getPromptSymbol());
                case "morelines":
                    env.writeln("Symbol for morelines is " + env.getMorelinesSymbol());
                case "multiline":
                    env.writeln("Symbol for multiline is " + env.getMultilineSymbol());
                default:
                    env.writeln("Invalid symbol name!");
            }

        } else {
            if(args[1].length() != 1) {
                env.writeln("Only a single character can become a symbol!");
                return ShellStatus.CONTINUE;
            }
            if(args[0].toLowerCase().equals("prompt")){
                char old = env.getPromptSymbol();
                env.setPromptSymbol(args[1].charAt(0));
                env.writeln("Symbol for prompt changed from " + old + " to " + env.getPromptSymbol());
            } else if(args[0].toLowerCase().equals("multiline")){
                char old = env.getMultilineSymbol();
                env.setMultilineSymbol(args[1].charAt(0));
                env.writeln("Symbol for multiline changed from " + old + " to " + env.getMultilineSymbol());
            } else if(args[0].toLowerCase().equals("morelines")){
                char old = env.getMorelinesSymbol();
                env.setMorelinesSymbol(args[1].charAt(0));
                env.writeln("Symbol for morelines changed from " + old + " to " + env.getMorelinesSymbol());
            } else {
                env.writeln("Invalid symbol name!");
            }
        }
        return ShellStatus.CONTINUE;
    }

    /**
     * Method is used to fetch the name of the command.
     * @return {@code String} command name.
     */
    @Override
    public String getCommandName() {
        return "symbol";
    }

    /**
     * Method fetches the instructions for usage of commands.
     * @return {@code List} of command descriptions.
     */
    @Override
    public List<String> getCommandDescription() {
        return List.of("Takes one or two arguments.",
            "If given one argument, the name of symbol, returns the symbol.",
            "If given two arguments, the name of symbol and a char, changes the current symbol to a new one!");
    }
}
