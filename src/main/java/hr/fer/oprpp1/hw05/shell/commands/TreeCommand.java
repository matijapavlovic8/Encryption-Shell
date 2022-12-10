package hr.fer.oprpp1.hw05.shell.commands;

import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellCommand;
import hr.fer.oprpp1.hw05.shell.ShellStatus;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.Objects;

public class TreeCommand implements ShellCommand {
    /**
     * Method tries to execute the current command in given {@code Environment env} with provided {@code arguments}
     * @param env {@code Environment} in which the command is run.
     * @param arguments Arguments with which the command is run.
     * @return {@code ShellStatus.CONTINUE} if shell should be kept running after the execution of the command,
     * {@code ShellStatus.TERMINATE} otherwise.
     */
    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        Objects.requireNonNull(env, "Environment can't be null");

        List<String> args = ShellUtil.parsePath(arguments.split("\\s+"));

        if(args.size() != 1){
            env.writeln("tree takes exactly one argument!");
            return ShellStatus.CONTINUE;
        }

        Path path = null;
        try{
            path = Paths.get(args.get(0));
        } catch (InvalidPathException e){
            env.writeln(e.getMessage());
            return ShellStatus.CONTINUE;
        }

        if(Files.notExists(path) || !Files.isDirectory(path)){
            env.writeln("Invalid path given!");
            return ShellStatus.CONTINUE;
        }

        try{
            Files.walkFileTree(path, new FileVisitor<Path>() {

                int depth = 1;
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    env.writeln(" ".repeat(depth) + dir.getFileName());
                    depth += 2;
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    env.writeln(" ".repeat(depth) + file.getFileName());
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    depth -= 2;
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e){
            env.writeln(e.getMessage());
            return ShellStatus.CONTINUE;
        }
        return ShellStatus.CONTINUE;
    }

    /**
     * Method is used to fetch the name of the command.
     * @return {@code String} command name.
     */
    @Override
    public String getCommandName() {
        return "tree";
    }

    /**
     * Method fetches the instructions for usage of commands.
     * @return {@code List} of command descriptions.
     */
    @Override
    public List<String> getCommandDescription() {
        return List.of("Takes one argument - a directory.",
            "Prints directories inner structure.");
    }
}
