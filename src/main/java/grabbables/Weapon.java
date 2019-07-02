package grabbables;

import actions.ActionTemplate;
import actions.utils.AmmoAmount;
import actions.utils.AmmoAmountUncapped;
import viewclasses.WeaponView;

import java.util.*;

/**
 * This class contains all the information needed for a weapon.
 */
public class Weapon extends Grabbable{
    private AmmoAmount buyCost;
    private AmmoAmount reloadCost;
    private String name;
    private String weaponID;
    private String description;

    private Map<String, ActionTemplate> actions;

    /**
     * Empty constructor, should not be used
     */
    public Weapon(){}

    /**
     * Default constructor
     * @param name name of the weapon
     * @param buyCost {@link actions.utils.AmmoAmount Cost} that must be paid to pick up the weapon
     * @param reloadCost {@link actions.utils.AmmoAmount Cost} that must be paid to reload the weapon
     * @param actions A collection that contains all the {@link actions.ActionTemplate actions} that can be performed with the weapon
     * @param description a description of the weapon
     */
    public Weapon(String name,
                  AmmoAmount buyCost,
                  AmmoAmount reloadCost,
                  Collection<ActionTemplate> actions,
                  String description){
        this.name = name;
        this.buyCost = buyCost;
        this.reloadCost = reloadCost;
        this.actions = new HashMap<>();
        for (ActionTemplate i: actions){
            this.actions.put(i.getInfo().getActionId(), i);
        }

        this.weaponID = name;
        this.description = description;
    }

    @Override
    public Set<Weapon> getWeapon(){
        return Set.of(this);
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


    /**
     * This generates a {@link viewclasses.WeaponView weaponView} that contains all the information needed by the client
     */
    public WeaponView generateView() {
        WeaponView weaponView = new WeaponView();

        weaponView.setName(name);
        weaponView.setBuyCost(buyCost);
        weaponView.setReloadCost(reloadCost);

        weaponView.setUid(super.getId());

        return weaponView;
    }

    public WeaponView generateView2() {
        WeaponView toRet = generateView();
        Map<String, String> map = new HashMap<>();
        for(ActionTemplate actionTemplate : actions.values()) {
            map.put(actionTemplate.getInfo().getName(), actionTemplate.getInfo().getDescription());
        }
        toRet.setActionDescriptions(map);
        return toRet;
    }
}
