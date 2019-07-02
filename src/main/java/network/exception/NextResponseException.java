package network.exception;

/**
 * Exception thrown when an error in deserialization occurs.
 */
public class NextResponseException extends RuntimeException{
    public NextResponseException(String errorMessage) {
        super(errorMessage);
    }
}

