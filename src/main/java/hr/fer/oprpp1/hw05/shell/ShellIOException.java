package hr.fer.oprpp1.hw05.shell;

/**
 * Exception thrown when read or write operations fail in Shell environment.
 * @author MatijaPav
 */
public class ShellIOException extends RuntimeException {

    /**
     * Creates an exception without specified message.
     */
    public ShellIOException(){
        super();
    }

    /**
     * Creates an exception with specified message.
     * @param message Detailed message with cause of exception.
     */
    public ShellIOException(String message){
        super(message);
    }
}
