package testcontroller.controllerstates;

/**
 * These are the states of the main controller
 */
public enum MainControllerState {
    /**
     * This is the phase where the game is still being set up
     */
    GAMESETUP, // Before the game begins

    /**
     * This means that the Main controller handed over control to a slave controller and is now
     * waiting for an update
     */
    WAITING, // Waiting for interaction by the SlaveController(s)

    /**
     * In this step the main controller received the effects to apply to the GameMap and does so,
     */
    RESOLVING, // Resolving the effects

    /**
     * This is when the round of a player ended and the controller is running checks as well as
     * dealing damage (for instance when the player ends the turn on a control point) and
     * respawning/assigning points
     */
    INTERPLAYER, // Between player rounds, for instance will count points

    /**
     * This is when the last player turn has been played and a ranking needs to be calculated
     */
    ENDGAME
}
