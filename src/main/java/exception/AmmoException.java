package exception;

/**
 * Exception thrown if the amount of ammo available is less than the amount of ammo needed
 */
public class AmmoException extends Exception{
    public AmmoException(String errorMessage) {
        super(errorMessage);
    }
}
