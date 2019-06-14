package actions.utils;

import genericitems.Tuple;
import grabbables.Weapon;
import testcontroller.controllermessage.ControllerMessage;

import java.util.List;

public interface WeaponChooser {

    /**
     *
     * @return x=True if choice is optional
     *          y=True if I have to choose just one
     *
     */
    Tuple<Boolean, Boolean> params();
    public List<Weapon> showOptions();
    public ControllerMessage pick(int[] choice);
}
