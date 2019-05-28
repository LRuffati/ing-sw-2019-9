package grabbables;

import actions.ActionTemplate;
import actions.WeaponUse;
import actions.utils.AmmoAmount;
import actions.utils.AmmoAmountUncapped;
import board.GameMap;
import uid.DamageableUID;
import viewclasses.WeaponView;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Weapon extends Grabbable{
    private AmmoAmount buyCost;
    private AmmoAmount reloadCost;
    private String name;
    private String weaponID;
    private String description;

    private Map<String, ActionTemplate> actions;

    /**
     * Constructor used only for tests
     */
    public Weapon(){}


    public Weapon(String name,
                  AmmoAmount buyCost,
                  AmmoAmount reloadCost,
                  Collection<ActionTemplate> actions){
        this.name = name;
        this.buyCost = buyCost;
        this.reloadCost = reloadCost;

        this.actions = new HashMap<>();
        for (ActionTemplate i: actions){
            this.actions.put(i.getInfo().getActionId(), i);
        }

        this.weaponID = name;
    }


    public String getName() {
        return name;
    }

    public AmmoAmount getBuyCost() {
        return buyCost;
    }

    public AmmoAmount getReloadCost() {
        return reloadCost;
    }

    public Map<String, ActionTemplate> getActions(){
        return Map.copyOf(actions);
    }

    /**
     * This method checks if this weapon can be reloaded, given the amount of ammo currently available
     * @param ammoAvailable The amount of ammo currently available
     * @return An empty Optional if the weapon can't be reloaded, otherwise the amount of ammo left
     */
    public Optional<AmmoAmountUncapped> canReload(AmmoAmountUncapped ammoAvailable){
        Optional<AmmoAmountUncapped> ret;
        try {
            ret = Optional.of(ammoAvailable.subtract(reloadCost));
        }
        catch (IllegalArgumentException e){
            ret = Optional.empty();
        }
        return ret;
    }

    /**
     * This method checks if this weapon can be picked, given the amount of ammo currently available
     * @param ammoAvailable The amount of ammo currently available
     * @return An empty Optional if the weapon can't be picked up, otherwise the amount of ammo left
     */
    public Optional<AmmoAmountUncapped> canPickUp(AmmoAmountUncapped ammoAvailable){
        Optional<AmmoAmountUncapped> ret;
        try {
            ret = Optional.of(ammoAvailable.subtract(buyCost));
        }
        catch (IllegalArgumentException e){
            ret = Optional.empty();
        }
        return ret;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }


    public WeaponView generateView() {
        WeaponView weaponView = new WeaponView();

        weaponView.setName(name);
        weaponView.setBuyCost(buyCost);
        weaponView.setReloadCost(reloadCost);

        weaponView.setUid(super.getId());

        return weaponView;
    }
}
