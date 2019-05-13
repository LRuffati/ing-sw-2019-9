package grabbables;

import actions.ActionTemplate;
import actions.WeaponUse;
import actions.utils.AmmoAmount;
import board.GameMap;
import uid.DamageableUID;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Weapon extends Grabbable{
    private AmmoAmount buyCost;
    private AmmoAmount reloadCost;
    private boolean isLoaded;
    private String name;
    private String weaponID;
    private String description;

    private Map<String, ActionTemplate> actions;

    /**
     * Constructor used only for tests
     */
    public Weapon(){}


    public Weapon(String name,
                  String description,
                  AmmoAmount buyCost,
                  AmmoAmount reloadCost,
                  Collection<ActionTemplate> actions){
        this.name = name;
        this.description = description;
        this.buyCost = buyCost;
        this.reloadCost = reloadCost;

        this.actions = new HashMap<>();
        for (ActionTemplate i: actions){
            this.actions.put(i.getInfo().getActionId(), i);
        }

        this.weaponID = name;

        this.isLoaded = true;
    }

    public String getDescription() {
        return description;
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
    public Optional<AmmoAmount> canReload(AmmoAmount ammoAvailable){
        Optional<AmmoAmount> ret;
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
    public Optional<AmmoAmount> canPickUp(AmmoAmount ammoAvailable){
        Optional<AmmoAmount> ret;
        try {
            ret = Optional.of(ammoAvailable.subtract(buyCost));
        }
        catch (IllegalArgumentException e){
            ret = Optional.empty();
        }
        return ret;
    }

    /**
     * Silly method to set the weapon from the Actor class.
     */
    public void setLoaded(){
        this.isLoaded = true;
    }

    /**
     *
     * @return true if the weapon is loaded and ready to be used.
     */
    public boolean isLoaded() {
        return isLoaded;
    }



    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}

