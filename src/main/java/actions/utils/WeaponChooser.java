package actions.utils;

import controllerresults.ControllerActionResultServer;
import grabbables.Weapon;

import java.util.List;

public interface WeaponChooser {
    public List<Weapon> showOptions();
    public ControllerActionResultServer pick(int[] choice);
}
