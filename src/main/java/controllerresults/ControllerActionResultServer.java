package controllerresults;

import actions.utils.ActionPicker;
import actions.utils.WeaponChooser;
import actions.utils.ChoiceMaker;
import board.Sandbox;

public class ControllerActionResultServer {
    public final ActionResultType type;
    public final ChoiceMaker choiceMaker;
    public final WeaponChooser weaponChooser;
    public final ActionPicker actionPicker;

    public final String message;
    public final Sandbox sandbox;

    public ControllerActionResultServer(ChoiceMaker choiceMaker, String message, Sandbox sandbox){
        this.choiceMaker = choiceMaker;
        this.type = ActionResultType.PICKTARGET;
        this.weaponChooser = null;
        this.actionPicker = null;

        this.message = message;
        this.sandbox = sandbox;
    }
    public ControllerActionResultServer(WeaponChooser weaponChooser, String message, Sandbox sandbox){
        this.choiceMaker = null;
        this.type = ActionResultType.PICKWEAPON;
        this.weaponChooser = weaponChooser;
        this.actionPicker = null;

        this.message = message;
        this.sandbox = sandbox;
    }
    public ControllerActionResultServer(ActionPicker actionPicker, String message, Sandbox sandbox){
        this.choiceMaker = null;
        this.type = ActionResultType.PICKACTION;
        this.weaponChooser = null;
        this.actionPicker = actionPicker;

        this.message = message;
        this.sandbox = sandbox;
    }
    public ControllerActionResultServer(ActionResultType type, String message, Sandbox sandbox){
        this.choiceMaker = null;
        this.weaponChooser = null;
        this.actionPicker = null;

        if ((type == ActionResultType.ROLLBACK) || (type == ActionResultType.TERMINATED) || (type == ActionResultType.ALREADYTERMINATED)){
            this.type = type;
        } else this.type = null;


        this.message = message;
        this.sandbox = sandbox;
    }
}
