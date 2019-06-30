package controller.controllerstates;

/**
 * These are the states of the SlaveController, thus by extension of the Client controller
 */
public enum SlaveControllerState {
    /**
     * Denotes the execution of the player's own turn
     */
    MAIN, // I'm running the main line of execution

    /**
     * The player is respawning
     */
    RESPAWN, // I'm respawning

    /**
     * The player is using the takeback grenade on some other player in the middle of said
     * player's turn
     * While a player is in this state the player whose turn it is receives a WAIT instruction
     * momentarily
     */
    USETAGBACK, // I'm using the takeback grenade

    /**
     * The player decided to use the terminator action
     */
    TERMINATOR, // I'm using the terminator

    /**
     * The slave is waiting for an update by the server.
     * In the SlaveController this update is directly made by the main controller class, for the
     * client this can be achieved either by polling or by a remote wake up
     */
    WAIT, // I'm waiting for instructions

    /**
     * Used to notify that, while in a choice path the user should roll back to a previous state.
     * This can happen when the user gets in a position from which it can't choose any valid
     * option but terminating the operation is likewise not acceptable.
     * A Message of this type should never appear as the result from SlaveController.giveMessage
     */
    ROLLBACK,

    /**
     * The client should discard this and repeat the last action
     */
    REPEAT
}

