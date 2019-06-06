package testcontroller.controllerstates;

/**
 * These are the types of an update received by the ClientController
 */
public enum UpdateTypes {
    /**
     * Some property of the Actor linked to the SlaveController has been modified
     */
    SELFCHANGE,

    /**
     * Another change has occoured on the map
     */
    OTHERCHANGE,

    /**
     * I'm receiving a complete GameMap view
     */
    MAP,

    /**
     * I'm receiving a complete Sandbox view
     */
    SANDBOX
}
