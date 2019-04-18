package exception;

public class AlreadyBoundedActorException extends Exception{
    public AlreadyBoundedActorException(String errorMessage) {
        super(errorMessage);
    }
}
