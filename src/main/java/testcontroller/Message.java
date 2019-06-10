package testcontroller;

/**
 * This message represents a status update from the server to the client
 */
public interface Message {



    /**
     * I communicate the changes that happened
     * @return
     */
    List<EffectView> getChanges();
}
