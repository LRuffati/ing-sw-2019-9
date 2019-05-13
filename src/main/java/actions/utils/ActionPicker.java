package actions.utils;

import actions.ActionTemplate;
import controllerresults.ControllerActionResult;
import genericitems.Tuple;

import java.util.List;

public interface ActionPicker {
    Tuple<Boolean, List<ActionTemplate>> showActionsAvailable();

    ControllerActionResult pickAction(int choice);
}
