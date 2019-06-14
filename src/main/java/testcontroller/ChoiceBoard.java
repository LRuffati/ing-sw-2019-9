package testcontroller;

import actions.utils.ActionPicker;
import actions.utils.ChoiceMaker;
import actions.utils.WeaponChooser;
import genericitems.Tuple;
import grabbables.Weapon;
import testcontroller.controllerstates.PickTypes;
import viewclasses.ActionView;
import viewclasses.PowerUpView;
import viewclasses.TargetView;
import viewclasses.WeaponView;

import java.util.List;
import java.util.stream.Collectors;

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

        actionViews = null;
        weaponViews = null;
        targetViews = tup.y;
        powerUpViews = null;
        stringViews = null;

    }

    public ChoiceBoard(WeaponChooser weaponChooser, String s) {
        Tuple<Boolean, Boolean> opts = weaponChooser.params();
        single = opts.y;
        optional = opts.x;
        description = s;
        type = PickTypes.WEAPON;

        actionViews = null;
        weaponViews = weaponChooser.showOptions().stream().map(Weapon::generateView).collect(Collectors.toList());
        targetViews = null;
        powerUpViews = null;
        stringViews = null;
    }

    public ChoiceBoard(ActionPicker actionPicker, String s) {
        type = PickTypes.ACTION;
        description = s;
        single = true;
        optional = actionPicker.showActionsAvailable().x;

        actionViews = actionPicker.showActionsAvailable().y;
        weaponViews = null;
        targetViews = null;
        powerUpViews = null;
        stringViews = null;
    }

    public ChoiceBoard(List<String> options, String s){
        type = PickTypes.STRING;
        description = s;
        single = true;
        optional = false;

        actionViews = null;
        weaponViews = null;
        targetViews = null;
        powerUpViews = null;
        stringViews = options;
    }

    /**
     * The lists contain view elements or are null
      */
    public final List<ActionView> actionViews;
    public final List<WeaponView> weaponViews;
    public final List<TargetView> targetViews;
    public final List<PowerUpView> powerUpViews;
    public final List<String> stringViews;

}
