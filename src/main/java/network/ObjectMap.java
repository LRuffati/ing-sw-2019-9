package network;

import actions.ActionTemplate;
import actions.targeters.targets.Targetable;
import actions.utils.ActionPicker;
import actions.utils.ChoiceMaker;
import actions.utils.WeaponChooser;
import controllerresults.ActionResultType;
import controllerresults.ControllerActionResult;
import genericitems.Tuple;
import grabbables.Weapon;
import viewclasses.ActionView;
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

    private ObjectMap() {
        choiceMakerMap = new HashMap<>();
        weaponChooserMap = new HashMap<>();
        actionPickerMap = new HashMap<>();
    }

    private Map<String, ChoiceMaker> choiceMakerMap;
    private Map<String, WeaponChooser> weaponChooserMap;
    private Map<String, ActionPicker> actionPickerMap;

    private String newID(){
        return new UID().toString();
    }

    private Tuple<ActionResultType, String> handle(ControllerActionResult res){
        String id = newID();
        Tuple<ActionResultType, String> ret;
        ret = new Tuple<>(res.type, id);
        if(res.type == ActionResultType.PICKTARGET)
            choiceMakerMap.put(id, res.choiceMaker);
        if(res.type == ActionResultType.PICKWEAPON)
            weaponChooserMap.put(id, res.weaponChooser);
        if(res.type == ActionResultType.PICKACTION)
            actionPickerMap.put(id, res.actionPicker);

        if(res.type == ActionResultType.TERMINATED) {
            ret = new Tuple<>(res.type, "");
        }
        if(res.type == ActionResultType.ROLLBACK){
            ret = new Tuple<>(res.type, "");
        }

        return ret;
    }

    public Tuple<ActionResultType, String> pickTarg(String choiceMakerId, int choice) {
        if(!choiceMakerMap.containsKey(choiceMakerId)) {
            //todo: return ??
        }
        return handle(choiceMakerMap.get(choiceMakerId).pick(choice));
    }
    public Tuple<ActionResultType, String> pickWeapon(String weaponChooserId, int[] choice) {
        if(!weaponChooserMap.containsKey(weaponChooserId)){
            //todo: return ?
        }
        return handle(weaponChooserMap.get(weaponChooserId).pick(choice));
    }
    public Tuple<ActionResultType, String> pickWeapon(String weaponChooserId, List<Integer> choice) {
        return pickWeapon(weaponChooserId, choice.stream().mapToInt(Integer::intValue).toArray());
    }
    public Tuple<ActionResultType, String> pickAction(String actionPickerId, int choice) {
        if(!actionPickerMap.containsKey(actionPickerId)){
            //todo: return ?
        }
        return handle(actionPickerMap.get(actionPickerId).pickAction(choice));
    }


    public Tuple<Boolean, List<TargetView>> showOptionsTarget(String choiceMakerId) {
        return choiceMakerMap.get(choiceMakerId).showOptions();
    }
    public List<WeaponView> showOptionsWeapon(String weaponChooserId) {
        return weaponChooserMap.get(weaponChooserId).showOptions().stream().map(Weapon::generateView).collect(Collectors.toList());
    }
    //TODO: List<ActionView>?
    public Tuple<Boolean, List<ActionView>> showOptionsAction(String actionPickerId) {
        return actionPickerMap.get(actionPickerId).showActionsAvailable();
    }
}
