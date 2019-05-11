package controllerresults;

import actions.Action;
import actions.effects.Effect;
import actions.utils.ActionPicker;
import actions.utils.WeaponChooser;
import actions.utils.ChoiceMaker;

import java.util.List;

public class ControllerActionResult {
    public ActionResultType type;
    public ChoiceMaker choiceMaker;
    public WeaponChooser weaponChooser;
    public ActionPicker actionPicker;

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
