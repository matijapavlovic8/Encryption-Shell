package hr.fer.oprpp1.hw05.shell.commands;

import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellCommand;
import hr.fer.oprpp1.hw05.shell.ShellStatus;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

public class HexdumpCommand implements ShellCommand {
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

        List<String> args = ShellUtil.parsePath(arguments.split("\\s+"));

        if(args.size() != 1){
            env.writeln("Hexdump takes exactly one argument!");
            return ShellStatus.CONTINUE;
        }

        Path path = null;
        try{
            path = Paths.get(args.get(0));
        } catch (InvalidPathException e){
            env.writeln(e.getMessage());
            return ShellStatus.CONTINUE;
        }

        if(Files.notExists(path) || !Files.isRegularFile(path)){
            env.writeln("The given path doesn't exist!");
            return ShellStatus.CONTINUE;
        }

        try(InputStream is = new BufferedInputStream(Files.newInputStream(path))){
            byte[] bytearr = new byte[16];
            int curr = 0;

            while(true){
                StringBuilder sb = new StringBuilder();
                int i = is.read(bytearr);
                if(i <= 0) break;
                sb.append(String.format("%08X: ", curr));
                for(int j = 0; j < 16; j++){
                    if(j < i) sb.append(String.format("%02X", bytearr[j]));
                    else sb.append(" ");

                    if(i != 7) sb.append(" ");
                    else sb.append("|");

                }
                sb.append(" | ");

                for(int j = 0; j < i; j++){
                    sb.append(String.format("%c", bytearr[j] < 32 ? '.' : bytearr[j]));
                }

                env.writeln(sb.toString());
                curr += 16;
            }
        } catch (IOException e){
            env.writeln(e.getMessage());
        }

        return ShellStatus.CONTINUE;

    }

    /**
     * Method is used to fetch the name of the command.
     * @return {@code String} command name.
     */
    @Override
    public String getCommandName() {
        return "hexdump";
    }

    /**
     * Method fetches the instructions for usage of commands.
     * @return {@code List} of command descriptions.
     */
    @Override
    public List<String> getCommandDescription() {
        return List.of("Takes one argument - file name.",
            "Produces hex output.");
    }
}
