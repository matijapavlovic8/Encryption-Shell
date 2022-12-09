package hr.fer.oprpp1.hw05.shell;

import java.util.List;

/**
 * Interface ShellCommand represents commands implemented in Shell environment.
 * @author MatijaPav
 */
public interface ShellCommand {

    /**
     * Method tries to execute the current command in given {@code Environment env} with provided {@code arguments}
     * @param env {@code Environment} in which the command is run.
     * @param arguments Arguments with which the command is run.
     * @return {@code ShellStatus.CONTINUE} if shell should be kept running after the execution of the command,
     * {@code ShellStatus.TERMINATE} otherwise.
     */

    ShellStatus executeCommand(Environment env, String arguments);

    /**
     * Method is used to fetch the name of the command.
     * @return {@code String} command name.
     */
    String getCommandName();

    /**
     * Method fetches the instructions for usage of commands.
     * @return {@code List} of command descriptions.
     */
    List<String> getCommandDescription();
}
