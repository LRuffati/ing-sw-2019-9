package controllerresults;

import uid.SandboxUID;

import java.io.Serializable;

public class ControllerActionResultClient implements Serializable {
    public final ActionResultType type;
    public final String nextId;
    public final SandboxUID sandboxUID;
    public final String message;

    public ControllerActionResultClient(ControllerActionResultServer controllerActionResultServer, String nextId){
        this.type = controllerActionResultServer.type;
        this.message = controllerActionResultServer.message;
        this.nextId = nextId;
        this.sandboxUID = controllerActionResultServer.sandbox.uid;
    }

}
