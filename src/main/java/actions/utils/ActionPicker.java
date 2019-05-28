package actions.utils;

import controllerresults.ControllerActionResultServer;
import genericitems.Tuple;
import viewclasses.ActionView;

import java.util.List;

public interface ActionPicker {
    Tuple<Boolean, List<ActionView>> showActionsAvailable();

    ControllerActionResultServer pickAction(int choice);
}
