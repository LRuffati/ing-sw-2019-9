package controller;

import actions.utils.ActionPicker;
import actions.utils.ChoiceMaker;
import actions.utils.WeaponChooser;
import genericitems.Tuple;
import grabbables.PowerUp;
import grabbables.Weapon;
import controller.controllerstates.PickTypes;
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
     */
    public final String description;
    private int numOfElems;

    public ChoiceBoard(ChoiceMaker choiceMaker, String message) {
        Tuple<Boolean, List<TargetView>> tup = choiceMaker.showOptions();
        single = true;
        optional = tup.x;
        description = message;
        type = PickTypes.TARGET;
        setNumOfElems(tup.y.size());

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
        setNumOfElems(weaponChooser.showOptions().size());

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
        setNumOfElems(actionPicker.showActionsAvailable().y.size());

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
        setNumOfElems(options.size());

        actionViews = null;
        weaponViews = null;
        targetViews = null;
        powerUpViews = null;
        stringViews = options;
    }

    private ChoiceBoard(PickTypes type, String description, boolean single, boolean optional, List<PowerUp> options){
        this.type = type;
        this.description = description;
        this.single = single;
        this.optional = optional;

        actionViews = null;
        weaponViews = null;
        targetViews = null;
        powerUpViews = options.stream().map(PowerUp::generateView).collect(Collectors.toList());
        stringViews = null;
    }

    public static ChoiceBoard powupChoiceFactory(List<PowerUp> options, String description,
                                                 boolean single, boolean optional){
        ChoiceBoard ret = new ChoiceBoard(PickTypes.POWERUP, description, single, optional, options);
        ret.setNumOfElems(options.size());
        return ret;
    }

    /**
     * The lists contain view elements or are null
      */
    List<ActionView> actionViews;
    List<WeaponView> weaponViews;
    List<TargetView> targetViews;
    List<PowerUpView> powerUpViews;
    List<String> stringViews;

    public int getNumOfElems() {
        return numOfElems;
    }

    private void setNumOfElems(int numOfElems) {
        this.numOfElems = numOfElems;
    }

    public List<PowerUpView> getPowerUps() {
        if (powerUpViews==null){
            return List.of();
        } else return List.copyOf(powerUpViews);
    }
}
