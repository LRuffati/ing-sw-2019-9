package network.socket.client;

/**
 * SINGLETON (CLIENT SIDE)
 *
 * Context used by clients to record data
 */
public class ClientContext {
    private static ClientContext instance;

    private ClientContext() {}

    public static synchronized ClientContext get() {
        if (instance == null) {
            instance = new ClientContext();
        }
        return instance;
    }


    private String token;
    public synchronized void setToken(String token) {
        this.token = token;
    }
    public synchronized String getToken() {
        return token;
    }

    private int mirror;
    public synchronized void setMirror(int mirror) {
        this.mirror = mirror;
    }
    public synchronized int getMirror() {
        return mirror;
    }

    private String message;
    public void setTextMessage(String message) {
        this.message = message;
    }
    public String getTextMessage() {
        return message;
    }
}
