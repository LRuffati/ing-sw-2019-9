package testcontroller;

import actions.effects.Effect;
import board.GameMap;
import board.Sandbox;
import testcontroller.controllerstates.UpdateTypes;
import viewclasses.GameMapView;

import java.util.List;

/**
 * This message represents a status update from the server to the client
 */
public interface Message {

    /**
     * This is the type of the change, it'll change the way I interpret the data
     * @return
     */
    UpdateTypes type();

    /**
     * A human readable description of the changes
     * @return
     * TODO: Change to some other format if necessary
     */
    String message();

    /**
     * I communicate the changes that happened
     * @return
     */
    List<EffectView> getChanges();

    /**
     *
     * @return
     */
    Sandbox sandbox();

    /**
     *
     * @return
     */
    Integer gameMapIteration();
}
