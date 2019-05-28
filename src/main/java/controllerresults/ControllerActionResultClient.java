package controllerresults;

import java.io.Serializable;

public class ControllerActionResultClient implements Serializable {
    public final ActionResultType type;
    public final String actionId;
    public final String sandboxUID;
    public final String message;

    public ControllerActionResultClient(ControllerActionResultServer controllerActionResultServer, String actionId){
        this.type = controllerActionResultServer.type;
        this.message = controllerActionResultServer.message;
        this.actionId = actionId;
        this.sandboxUID = controllerActionResultServer.sandbox.uid;
    }

}
