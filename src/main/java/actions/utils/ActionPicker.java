package actions.utils;

import genericitems.Tuple;
import testcontroller.controllermessage.ControllerMessage;
import viewclasses.ActionView;

import java.util.List;

public interface ActionPicker {
    Tuple<Boolean, List<ActionView>> showActionsAvailable();

    ControllerMessage pickAction(int choice);
}
