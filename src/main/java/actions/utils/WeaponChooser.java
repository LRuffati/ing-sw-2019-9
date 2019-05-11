package actions.utils;

import controllerresults.ControllerActionResult;
import grabbables.Weapon;

import java.util.List;

public interface WeaponChooser {
    public List<Weapon> showOptions();
    public ControllerActionResult pick(int choice);
}
