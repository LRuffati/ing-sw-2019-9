package testcontroller;

import actions.utils.ActionPicker;
import actions.utils.ChoiceMaker;
import actions.utils.WeaponChooser;
import genericitems.Tuple;
import testcontroller.controllerstates.PickTypes;
import uid.TileUID;
import viewclasses.ActionView;
import viewclasses.PowerUpView;
import viewclasses.TargetView;
import viewclasses.WeaponView;

import java.util.List;

/**
 * This class contains all the possible choises to be made
 */
public class ChoiceBoard {
    /**
     * If optional is true then I can pick no element
     */
    public final boolean optional;

    /**
     * If single is true then I can pick at most one element
     */
    public final boolean single;

    /**
     * This represents the type of choice to be made, only the list corresponding to this type
     * will have any elements inside of it
     */
    public final PickTypes type;

    /**
     * This provides a human readable description of the choice being made
     * TODO: change to something other than string
     */
    public final String description;

    public ChoiceBoard(ChoiceMaker choiceMaker, String message) {
        Tuple<Boolean, List<TargetView>> tup = choiceMaker.showOptions();
        single = true;
        optional = tup.x;
        description = message;
        type = PickTypes.TARGET;
        //TODO: tup.y nella lista giusta, altre a null
    }

    public ChoiceBoard(WeaponChooser weaponChooser, String s) {
        Tuple<Boolean, Boolean> opts = weaponChooser.params();
        single = opts.y;
        optional = opts.x;
        description = s;
        type = PickTypes.WEAPON;
        //TODO: add to the correct list weaponChooser.showOptions()
    }

    public ChoiceBoard(ActionPicker actionPicker, String s) {
        type = PickTypes.ACTION;
        description = s;
        single = true;
        optional = actionPicker.showActionsAvailable().x;
        //TODO: add actionpicker.showactionsavailable().y
    }

    public ChoiceBoard(List<String> options, String s){
        type = PickTypes.STRING;
        description = s;
        single = true;
        optional = false;
        //TODO: add to list
    }

    /**
     * The lists contain view elements or are null
      */
    List<ActionView> actionViews;
    List<WeaponView> weaponViews;
    List<TargetView> targetViews;
    List<PowerUpView> powerUpViews;
    List<String> stringViews;
}
