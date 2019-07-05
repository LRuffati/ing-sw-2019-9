package actions.utils;

import genericitems.Tuple;
import controller.controllermessage.ControllerMessage;
import viewclasses.ActionView;

import java.util.List;

public interface ActionPicker {
    /**
     *
     * @return a flag indicating whether the action is optional or not and a list of actions
     */
    Tuple<Boolean, List<ActionView>> showActionsAvailable();

    ControllerMessage pickAction(int choice);
}
