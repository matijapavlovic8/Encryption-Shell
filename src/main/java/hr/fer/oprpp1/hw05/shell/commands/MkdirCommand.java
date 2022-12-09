package hr.fer.oprpp1.hw05.shell.commands;

import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellCommand;
import hr.fer.oprpp1.hw05.shell.ShellIOException;
import hr.fer.oprpp1.hw05.shell.ShellStatus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MkdirCommand implements ShellCommand {

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
        Objects.requireNonNull(arguments,"mkdir must get one argument!");

        List<String> args = ShellUtil.parsePath(arguments.split("\\s+"));

        if(args.size() != 1 || args.get(0).length() == 0) {
            env.writeln("mkdir takes exactly one argument!");
            return ShellStatus.CONTINUE;
        }

        String p = args.get(0);
        Path path = null;

        try {
            path = Paths.get(p);
        } catch (InvalidPathException e) {
            env.writeln("Invalid path given!");
            return ShellStatus.CONTINUE;
        }

        try{
            Files.createDirectories(path);
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
        return "mkdir";
    }

    /**
     * Method fetches the instructions for usage of commands.
     * @return {@code List} of command descriptions.
     */
    @Override
    public List<String> getCommandDescription() {
        return Collections.unmodifiableList(List.of("Creates a new directory within current directory.",
            "Takes a single argument - new directory name."));
    }
}
