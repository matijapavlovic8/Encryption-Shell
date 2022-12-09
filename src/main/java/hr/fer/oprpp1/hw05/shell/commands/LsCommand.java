package hr.fer.oprpp1.hw05.shell.commands;

import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellCommand;
import hr.fer.oprpp1.hw05.shell.ShellStatus;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class LsCommand implements ShellCommand {
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
            env.writeln("ls command takes exactly one argument.");
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
            env.writeln("The given path doesn't exist or isn't a directory!");
            return ShellStatus.CONTINUE;
        }

        try(DirectoryStream<Path> stream = Files.newDirectoryStream(path)){
            for(Path file : stream){
                try {
                    env.writeln(lsOutput(file));
                } catch (IOException e){
                    env.writeln(e.getMessage());
                    return ShellStatus.CONTINUE;
                }
            }
        } catch(IOException e){
            env.writeln(e.getMessage());
            return ShellStatus.CONTINUE;
        }

        return ShellStatus.CONTINUE;

    }

    /**
     * Formats output string for files.
     * @param file file whose data is being formatted.
     * @return {@code String} output for a single file.
     * @throws IOException if IO exception occurs.
     */
    private String lsOutput(Path file) throws IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        BasicFileAttributeView faView;
        faView = Files.getFileAttributeView(
            file, BasicFileAttributeView.class, LinkOption.NOFOLLOW_LINKS
        );
        BasicFileAttributes attributes = faView.readAttributes();
        FileTime fileTime = attributes.creationTime();
        String formattedDateTime = sdf.format(new Date(fileTime.toMillis()));

        StringBuilder sb = new StringBuilder();

        sb.append(Files.isDirectory(file) ? "d" : "-")
            .append(Files.isReadable(file) ? "r" : "-")
            .append(Files.isWritable(file) ? "w" : "-")
            .append(Files.isExecutable(file) ? "x " : "- ")
            .append(String.format("%10d ", Files.size(file)))
            .append(formattedDateTime)
            .append(file.getFileName().toString());

        return sb.toString();
    }

    /**
     * Method is used to fetch the name of the command.
     * @return {@code String} command name.
     */
    @Override
    public String getCommandName() {
        return "ls";
    }

    /**
     * Method fetches the instructions for usage of commands.
     * @return {@code List} of command descriptions.
     */
    @Override
    public List<String> getCommandDescription() {
        return List.of("Takes one arument - directory.",
            "Writes directory listing.");
    }
}
