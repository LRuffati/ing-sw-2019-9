package exception;

//TODO: accettabile??
public class FileNotFoundExceptionBis extends RuntimeException{
    public FileNotFoundExceptionBis(String errorMessage) {
        super(errorMessage);
    }
}
