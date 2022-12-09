package hr.fer.oprpp1.hw05.shell.commands;

import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellCommand;
import hr.fer.oprpp1.hw05.shell.ShellIOException;
import hr.fer.oprpp1.hw05.shell.ShellStatus;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

public class CatCommand implements ShellCommand {
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

        if(args.size() < 1 || args.size() > 2 || args.get(0).isBlank()){
            env.writeln("cat takes one or two arguments!");
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

        Charset cs = Charset.defaultCharset();

        if(args.size() == 2) {
            try{
                cs = Charset.forName(args.get(1));
            } catch (IllegalArgumentException e){
                env.writeln(e.getMessage());
                return ShellStatus.CONTINUE;
            }
        }

        try(BufferedReader br = new BufferedReader(new InputStreamReader(new BufferedInputStream(Files.newInputStream(path)), cs))){
            br.lines().forEach(env::writeln);
        } catch (IOException e){
            throw new ShellIOException(e.getMessage());
        }
        return ShellStatus.CONTINUE;

    }

    /**
     * Method is used to fetch the name of the command.
     * @return {@code String} command name.
     */
    @Override
    public String getCommandName() {
        return "cat";
    }

    /**
     * Method fetches the instructions for usage of commands.
     * @return {@code List} of command descriptions.
     */
    @Override
    public List<String> getCommandDescription() {
        return List.of("cat takes one or two arguments.",
            "First one is path to file, while second one is optional and represents charset",
            "which should be used to interpret chars from bytes.",
            "If charset is not provided, default charset will be used!");
    }
}
