package hr.fer.oprpp1.hw05.shell.commands;

import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellCommand;
import hr.fer.oprpp1.hw05.shell.ShellStatus;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Objects;

public class CharsetsCommand implements ShellCommand {
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

        if(!arguments.isBlank()){
            env.writeln("charset command takes no arguments!");
            return ShellStatus.CONTINUE;
        }

        Charset.availableCharsets().keySet().forEach(env::writeln);
        return ShellStatus.CONTINUE;
    }

    /**
     * Method is used to fetch the name of the command.
     * @return {@code String} command name.
     */
    @Override
    public String getCommandName() {
        return "charset";
    }

    /**
     * Method fetches the instructions for usage of commands.
     * @return {@code List} of command descriptions.
     */
    @Override
    public List<String> getCommandDescription() {
        return List.of("Takes no arguments.",
            "Lists all available charsets for your Java platform.");
    }
}
