package network.exception;

//TODO: why can't do that?
import java.rmi.RemoteException;
//public class InvalidLoginException extends Exception {

public class InvalidLoginException extends Exception {
    public final boolean wrongUsername;
    public final boolean wrongColor;

    public InvalidLoginException(String errorMessage, boolean wrongUsername, boolean wrongColor) {
        super(errorMessage);
        this.wrongUsername = wrongUsername;
        this.wrongColor = wrongColor;
    }
}
