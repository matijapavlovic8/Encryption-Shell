package hr.fer.oprpp1.hw05.shell;

import java.util.SortedMap;

/**
 * Interface Environment represents an implementation of a Shell.
 *
 * @author MatijaPav
 */

public interface Environment {
    /**
     * Reads the next line.
     * @return next line from the input.
     * @throws ShellIOException if it isn't possible to read the next line.
     */
    String readLine() throws ShellIOException;

    /**
     * Method {@code write} writes to console.
     * @param text string written to console.
     * @throws ShellIOException if writing to console fails.
     */
    void write(String text) throws ShellIOException;

    /**
     * Writes to console and automatically goes to next line at the end of the input.
     * @param text string written to console.
     * @throws ShellIOException if writing to console fails.
     */
    void writeln(String text) throws ShellIOException;

    /**
     * Unmodifiable map of the commands.
     * @return Unmodifiable {@code SortedMap}.
     */
    SortedMap<String, ShellCommand> commands();

    /**
     * Getter for the Multiline symbol.
     * @return {@code Character} that represents multiline symbol.
     */
    Character getMultilineSymbol();

    /**
     * Sets the multiline symbol to the desired character.
     * @param symbol Desired character to represent Multiline symbol.
     */
    void setMultilineSymbol(Character symbol);

    /**
     * Getter for the Prompt symbol.
     * @return {@code Character} that represents prompt symbol.
     */
    Character getPromptSymbol();

    /**
     * Sets the prompt symbol to the desired character.
     * @param symbol Desired character to represent prompt symbol.
     */
    void setPromptSymbol(Character symbol);

    /**
     * Getter for the Morelines symbol.
     * @return {@code Character} that represents morelines symbol.
     */
    Character getMorelinesSymbol();

    /**
     * Sets the Morelines symbol to the desired character.
     * @param symbol Desired character to represent Morelines symbol.
     */
    void setMorelinesSymbol(Character symbol);

}
