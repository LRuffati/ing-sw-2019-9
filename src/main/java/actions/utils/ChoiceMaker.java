package actions.utils;

import actions.targeters.targets.Targetable;
import genericitems.Tuple;
import controller.controllermessage.ControllerMessage;
import viewclasses.TargetView;

import java.util.List;
import java.util.function.Function;

/**
 */
public interface ChoiceMaker {
    /**
     * This function will be called by the Targeter
     */
    void giveTargets(String targetId, List<TargetView> possibilities,
                     Function<Integer, Targetable> action);

    default void giveTargetsWithDescr(String targetId, List<TargetView> possibilities,
                                      Function<Integer, Targetable> action, String description){
        return;
    }

    default String getDescription(){
        return "";
    }
    /**
     * This function will be called by the controller
     *
     * true if optional, false if required
     */
    Tuple<Boolean, List<TargetView>> showOptions();

    /**
     *
     */
    ControllerMessage pick(int choice);
}
