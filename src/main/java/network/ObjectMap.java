package network;

import actions.utils.ActionPicker;
import actions.utils.ChoiceMaker;
import actions.utils.WeaponChooser;
import board.Sandbox;
import controllerresults.ActionResultType;
import controllerresults.ControllerActionResultClient;
import controllerresults.ControllerActionResultServer;
import genericitems.Tuple;
import grabbables.Weapon;
import testcontroller.ChoiceBoard;
import testcontroller.ControllerMessage;
import viewclasses.ActionView;
import viewclasses.GameMapView;
import viewclasses.TargetView;
import viewclasses.WeaponView;

import java.rmi.server.UID;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ObjectMap {

    private static ObjectMap ourInstance = new ObjectMap();
    public static ObjectMap get() {
        return ourInstance;
    }

    private Map<String, ControllerMessage> choiceMap;

    private Map<String, ChoiceMaker> choiceMakerMap = new HashMap<>();
    private Map<String, WeaponChooser> weaponChooserMap = new HashMap<>();
    private Map<String, ActionPicker> actionPickerMap = new HashMap<>();

    private Map<String, Sandbox> sandboxMap = new HashMap<>();

    private ObjectMap() {
    }

    public void clearChache(){
        choiceMakerMap.clear();
        actionPickerMap.clear();
        weaponChooserMap.clear();
        sandboxMap.clear();
    }

    private String newID(){
        return new UID().toString();
    }

    private ChoiceBoard handlePick(ControllerMessage controllerMessage) {
        String id = newID();
        choiceMap.put(id, controllerMessage);
        sandboxMap.put(newID(), controllerMessage.sandbox());
        return new ChoiceBoard();
    }

    public ChoiceBoard pick(String choiceId, List<Integer> choices) {
        if(!choiceMap.containsKey(choiceId))
            //todo return ??
        return handlePick(choiceMap.get(choiceId).pick(choices));
    }


    private ControllerActionResultClient handlePick(ControllerActionResultServer controllerActionResultServer){

        String id = newID();

        ControllerActionResultClient ret = new ControllerActionResultClient(controllerActionResultServer, id);

        sandboxMap.put(newID(), controllerActionResultServer.sandbox);

        if(controllerActionResultServer.type == ActionResultType.PICKTARGET)
            choiceMakerMap.put(id, controllerActionResultServer.choiceMaker);
        if(controllerActionResultServer.type == ActionResultType.PICKWEAPON)
            weaponChooserMap.put(id, controllerActionResultServer.weaponChooser);
        if(controllerActionResultServer.type == ActionResultType.PICKACTION)
            actionPickerMap.put(id, controllerActionResultServer.actionPicker);

        //TODO: TERMINATED/ROLLBACK ??

        return ret;
    }

    public ControllerActionResultClient pickTarg(String choiceMakerId, int choice) {
        if(!choiceMakerMap.containsKey(choiceMakerId)) {
            //todo: return ??
        }
        return handlePick(choiceMakerMap.get(choiceMakerId).pick(choice));
    }
    public ControllerActionResultClient pickWeapon(String weaponChooserId, int[] choice) {
        if(!weaponChooserMap.containsKey(weaponChooserId)){
            //todo: return ?
        }
        return handlePick(weaponChooserMap.get(weaponChooserId).pick(choice));
    }
    public ControllerActionResultClient pickWeapon(String weaponChooserId, List<Integer> choice) {
        return pickWeapon(weaponChooserId, choice.stream().mapToInt(Integer::intValue).toArray());
    }
    public ControllerActionResultClient pickAction(String actionPickerId, int choice) {
        if(!actionPickerMap.containsKey(actionPickerId)){
            //todo: return ?
        }
        return handlePick(actionPickerMap.get(actionPickerId).pickAction(choice));
    }


    public Tuple<Boolean, List<TargetView>> showOptionsTarget(String choiceMakerId) {
        return choiceMakerMap.get(choiceMakerId).showOptions();
    }
    public List<WeaponView> showOptionsWeapon(String weaponChooserId) {
        return weaponChooserMap.get(weaponChooserId).showOptions().stream().map(Weapon::generateView).collect(Collectors.toList());
    }
    public Tuple<Boolean, List<ActionView>> showOptionsAction(String actionPickerId) {
        return actionPickerMap.get(actionPickerId).showActionsAvailable();
    }

    public GameMapView showGameMap(String gameMapId) {
        return sandboxMap.get(gameMapId).generateView();
    }

}
