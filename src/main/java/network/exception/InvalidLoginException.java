package network.exception;

public class InvalidLoginException extends Exception {
    public final boolean wrongUsername;
    public final boolean wrongColor;

    public InvalidLoginException(String errorMessage, boolean wrongUsername, boolean wrongColor) {
        super(errorMessage);
        this.wrongUsername = wrongUsername;
        this.wrongColor = wrongColor;
    }
}
