package testcontroller;

import actions.utils.ChoiceMaker;
import testcontroller.controllerstates.PickTypes;
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

    /**
     * The lists contain view elements or are null
      */
    List<...> ...
}
