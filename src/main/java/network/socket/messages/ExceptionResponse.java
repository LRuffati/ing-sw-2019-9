package network.socket.messages;

public class ExceptionResponse implements Response {
    public final Exception exception;

    public ExceptionResponse(Exception exception){
        this.exception = exception;
    }

    @Override
    public void handle(ResponseHandler handler) {
        handler.handle(this);
    }
}
