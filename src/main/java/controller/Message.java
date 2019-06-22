package controller;

import java.io.Serializable;
import java.util.List;

/**
 * This message represents a status update from the server to the client
 */
public interface Message extends Serializable {
    /**
     * I communicate the changes that happened
     * @return
     */
    List<String> getChanges();
}
