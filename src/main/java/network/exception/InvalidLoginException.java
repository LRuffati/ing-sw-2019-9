package network.exception;

/**
 * Exception thrown when a Login procedure does not terminate successfully
 */
public class InvalidLoginException extends Exception {
    public final boolean wrongUsername;
    public final boolean wrongColor;

    /**
     * Constructor of the exception
     * @param errorMessage error message
     * @param wrongUsername true if the username cannot be picked up
     * @param wrongColor true if the color cannot be chosen
     */
    public InvalidLoginException(String errorMessage, boolean wrongUsername, boolean wrongColor) {
        super(errorMessage);
        this.wrongUsername = wrongUsername;
        this.wrongColor = wrongColor;
    }
}
