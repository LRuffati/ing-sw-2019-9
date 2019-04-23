package actions;

import actions.targeters.targets.Targetable;
import actions.utils.AmmoAmount;
import board.GameMap;
import board.Sandbox;
import grabbables.Weapon;
import uid.DamageableUID;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WeaponUse {
    private Weapon weapon;
    private Sandbox sandbox;
    Map<String, Targetable> existingTargets;
    List<ActionTemplate> previousActions;
    AmmoAmount ammoAvailable;


    public WeaponUse(Weapon weapon, GameMap map, DamageableUID pawnId){
        this.weapon = weapon;
        sandbox = map.createSandbox();
        existingTargets = Map.of("self", sandbox.getBasic(pawnId));
        previousActions = new ArrayList<>();
        ammoAvailable = map.getPawn(pawnId).getActor().getAmmo();
    }

    public void commit(){
        //TODO: copy the changes to the GameMap and to the Actors.
    }

}
