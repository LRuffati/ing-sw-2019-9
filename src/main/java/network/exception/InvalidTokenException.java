package network.exception;

/**
 * Exception thrown when the username of the sender is not allowed to make the request/response he did
 */
public class InvalidTokenException extends Exception {
    public InvalidTokenException() {
        super();
    }
}
