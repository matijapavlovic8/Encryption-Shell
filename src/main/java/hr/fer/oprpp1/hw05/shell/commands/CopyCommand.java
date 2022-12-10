package hr.fer.oprpp1.hw05.shell.commands;

import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellCommand;
import hr.fer.oprpp1.hw05.shell.ShellStatus;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

public class CopyCommand implements ShellCommand {
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

        if(args.size() != 2){
            env.writeln("copy command takes exactly two arguments!");
            return ShellStatus.CONTINUE;
        }

        Path source = null;
        Path target = null;

        try{
            source = Path.of(args.get(0));
            target = Path.of(args.get(1));
        } catch (InvalidPathException e){
            env.writeln(e.getMessage());
            return ShellStatus.CONTINUE;
        }

        if(Files.notExists(source) || !Files.isRegularFile(source)){
            env.writeln("Invalid source file given!");
            return ShellStatus.CONTINUE;
        }

        if(Files.isDirectory(target)){
            try {
                target = Path.of(target.toString() + "/" + source.toString());
            } catch (InvalidPathException e){
                env.writeln(e.getMessage());
                return ShellStatus.CONTINUE;
            }
        }

        if(Files.exists(target)){
            if(!Files.isRegularFile(target)){
                env.writeln("Invalid path to target file given!");
                return ShellStatus.CONTINUE;
            }
            env.write("Overwrite existing File (y/n): ");
            String input = env.readLine().trim();
            if(input.equalsIgnoreCase("y")){
                copy(env, source, target);
                return ShellStatus.CONTINUE;
            } else if(input.equalsIgnoreCase("n")){
                return ShellStatus.CONTINUE;
            } else {
                env.writeln("Invalid input!");
                return ShellStatus.CONTINUE;
            }
        } else {
            copy(env, source, target);
            return ShellStatus.CONTINUE;
        }

    }

    private void copy(Environment env, Path source, Path target){
        try(InputStream is = new BufferedInputStream(Files.newInputStream(source));
            OutputStream os = new BufferedOutputStream(Files.newOutputStream(target))){
            byte[] bytearr = new byte[4096];
            while(true){
                int i = is.read(bytearr);
                if(i == -1) break;
                os.write(bytearr, 0, i);
            }

        } catch (IOException e){
            env.writeln(e.getMessage());
        }
    }

    /**
     * Method is used to fetch the name of the command.
     * @return {@code String} command name.
     */
    @Override
    public String getCommandName() {
        return "copy";
    }

    /**
     * Method fetches the instructions for usage of commands.
     * @return {@code List} of command descriptions.
     */
    @Override
    public List<String> getCommandDescription() {
        return List.of("Expects two files. Source file and destination file.",
            "Copies content from source file to the destination.");
    }
}
