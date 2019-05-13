package controllerresults;

import actions.utils.ActionPicker;
import actions.utils.WeaponChooser;
import actions.utils.ChoiceMaker;

public class ControllerActionResult {
    public final ActionResultType type;
    public final ChoiceMaker choiceMaker;
    public final WeaponChooser weaponChooser;
    public final ActionPicker actionPicker;

    public ControllerActionResult(ChoiceMaker choiceMaker){
        this.choiceMaker = choiceMaker;
        this.type = ActionResultType.PICKTARGET;
        this.weaponChooser = null;
        this.actionPicker = null;
    }
    public ControllerActionResult(WeaponChooser weaponChooser){
        this.choiceMaker = null;
        this.type = ActionResultType.PICKWEAPON;
        this.weaponChooser = weaponChooser;
        this.actionPicker = null;
    }
    public ControllerActionResult(ActionPicker actionPicker){
        this.choiceMaker = null;
        this.type = ActionResultType.PICKACTION;
        this.weaponChooser = null;
        this.actionPicker = actionPicker;
    }
    public ControllerActionResult(ActionResultType type){
        this.choiceMaker = null;
        this.weaponChooser = null;
        this.actionPicker = null;

        if ((type == ActionResultType.ROLLBACK) || (type == ActionResultType.TERMINATED) || (type == ActionResultType.ALREADYTERMINATED)){
            this.type = type;
        } else this.type = null;

    }
}
