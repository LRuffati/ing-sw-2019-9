package network.exception;

public class NextResponseException extends RuntimeException{
    public NextResponseException(String errorMessage) {
        super(errorMessage);
    }
}

