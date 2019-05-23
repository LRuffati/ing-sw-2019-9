package actions.utils;

import controllerresults.ControllerActionResult;
import genericitems.Tuple;
import viewclasses.ActionView;

import java.util.List;

public interface ActionPicker {
    Tuple<Boolean, List<ActionView>> showActionsAvailable();

    ControllerActionResult pickAction(int choice);
}
