package board;

import actions.effects.Effect;
import actions.targeters.targets.BasicTarget;
import actions.targeters.targets.RoomTarget;
import actions.targeters.targets.TileTarget;
import actions.utils.AmmoAmountUncapped;
import genericitems.Tuple;
import grabbables.Weapon;
import uid.DamageableUID;
import uid.RoomUID;
import uid.TileUID;
import uid.WeaponUID;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Sandbox {
    /*
    Todo:
    Make the sandbox more functional.
    Changes:
        + At creation nothing is a target, the sandbox is just a proxy for the GameMap
        + Method
     */
    private final Sandbox father;

    private final Map<RoomUID, RoomTarget> roomsTargeted;
    private final Map<TileUID, TileTarget> tilesTargeted;
    private final Map<DamageableUID, BasicTarget> pawnsTargeted;

    /*
        Manage changes, relevant effects:
         + move: DamageableUID, TileUID
         + reload and fire: Weapon, boolean
         +
         */
    private final List<Effect> effectsHistory;
    private final Map<DamageableUID, TileUID> updatedLocations;
    private final Map<Weapon, Boolean> updatedWeapons;

    public AmmoAmountUncapped getUpdatedAmmoAvailable() {
        return updatedAmmoAvailable;
    }

    public final AmmoAmountUncapped updatedAmmoAvailable;

    public final DamageableUID pov;
    private final GameMap map;

    public Sandbox(GameMap map, DamageableUID pov){

        this.roomsTargeted = new HashMap<>();
        this.tilesTargeted = new HashMap<>();
        this.pawnsTargeted = new HashMap<>();

        this.effectsHistory = new ArrayList<>();
        this.updatedLocations = new HashMap<>();
        this.updatedWeapons = new HashMap<>();

        this.map = map;
        this.pov = pov;
        this.updatedAmmoAvailable = map.getPawn(pov).getActor().getTotalAmmo();

        this.father = null;
    }

    public Sandbox(Sandbox parent, List<Effect> effects){
        this.roomsTargeted = new HashMap<>(parent.roomsTargeted);
        this.tilesTargeted = new HashMap<>(parent.tilesTargeted);
        this.pawnsTargeted = new HashMap<>(parent.pawnsTargeted);

        this.pov = parent.pov;
        this.map = parent.map;

        Map<Weapon, Boolean> weaponsTemp = new HashMap<>(parent.updatedWeapons);
        Map<DamageableUID, TileUID> tempLocs = new HashMap<>(parent.updatedLocations);
        AmmoAmountUncapped ammoTemp = parent.updatedAmmoAvailable;
        for (Effect i: effects){
            weaponsTemp = i.newWeapons(weaponsTemp);
            tempLocs = i.newLocations(tempLocs);
            ammoTemp = i.newAmmoAvailable(ammoTemp);
        }
        this.updatedWeapons = weaponsTemp;
        this.updatedLocations = tempLocs;
        this.updatedAmmoAvailable = ammoTemp;

        this.father = parent;
        this.effectsHistory = new ArrayList<>(father.effectsHistory);
        effectsHistory.addAll(effects);
    }


    public List<Effect> getEffectsHistory() {
        return new ArrayList<>(effectsHistory);
    }

    /**
     * Returns the neighbors of the cell
     * @param tile the source tile
     * @param logical if true don't go through walls, if false do
     * @return A map of neighboring TileUIDs paired with the direction
     */
    public Map<Direction, TileUID> neighbors(TileUID tile, boolean logical){
        return map.neighbors(tile, logical);
    }

    /**
     * Returns the tiles in the manhattan circle of radius given centered on a tile
     * If radius is 0 return a set with only the tile given
     * If less than 0 return an empty set
     * @param centre
     * @param radius
     * @param logical go through walls or not
     * @return
     */
    public Set<TileUID> circle(TileUID centre, int radius, boolean logical){
        HashSet<TileUID> retVal = new HashSet<>();
        HashSet<TileUID> interior = new HashSet<>();
        HashSet<TileUID> border;

        if (radius<0) return retVal;
        retVal.add(centre);
        int rad = 0;

        while (rad<radius){
            border = new HashSet<>();
            //1. Find the border
            border.addAll(retVal);
            border.removeAll(interior);
            //2. Find neighbors of the border
            for (TileUID i: border){
                retVal.addAll(neighbors(i, logical).values());
            }
            interior.addAll(border);
            rad++;
        }
        return retVal;

    }

    /**
     * Returns all the pawns (player or domination points) contained in the tile
     * @param tile
     * @return
     */
    public Collection<DamageableUID> containedPawns(TileUID tile){

        return map.getDamageable().stream().filter(i -> tile(i).equals(tile)).collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Returns the room containing the tile
     * @param tile
     * @return
     */
    public RoomUID room(TileUID tile){
        return map.room(tile);
    }

    /**
     * Returns the room containing the pawn
     * @param pawn
     * @return
     */
    public RoomUID room(DamageableUID pawn){
        return map.room(tile(pawn));
    }

    /**
     * @param pawn
     * @return the room containing the pawn
     */
    public TileUID tile(DamageableUID pawn){
        //TODO: make effects dependent
        return pawnsTargeted.get(pawn).location(this);
    }

    /**
     * @return All the tiles in the room
     */
    public Collection<TileUID> tilesInRoom(RoomUID room){
        return map.tilesInRoom(room);
    }

    public Set<TileUID> tilesSeen(TileUID source){
        Collection<TileUID> tilesNear = neighbors(source, true).values();

        //Todo: convert to functional
        HashSet<RoomUID> roomsNeigh = new HashSet<>();
        for (TileUID i: tilesNear){
            roomsNeigh.add(room(i));
        }

        HashSet<TileUID> visible = new HashSet<>();
        for (RoomUID i: roomsNeigh){
            visible.addAll(tilesInRoom(i));
        }

        return visible;
    }

    public Set<TileUID> allTiles(){
        return map.allTiles();
    }

    public RoomTarget getRoom(RoomUID roomUID) {
        if (roomsTargeted.containsKey(roomUID)) return roomsTargeted.get(roomUID);
        else {
            RoomTarget targ =  new RoomTarget(roomUID);
            roomsTargeted.put(roomUID, targ);
            return roomsTargeted.get(roomUID);
        }
    }

    public TileTarget getTile(TileUID tileUID) {
        if (tilesTargeted.containsKey(tileUID)) return  tilesTargeted.get(tileUID);
        else {
            TileTarget targ = new TileTarget(tileUID);
            tilesTargeted.put(tileUID, targ);
            return tilesTargeted.get(tileUID);
        }
    }

    public BasicTarget getBasic(DamageableUID targetUID){
        if (pawnsTargeted.containsKey(targetUID)) return pawnsTargeted.get(targetUID);
        else {
            BasicTarget targ = BasicTarget.basicFactory(map.getPawn(targetUID));
            pawnsTargeted.put(targetUID, targ);
            return pawnsTargeted.get(targetUID);
        }
    }

    public List<Tuple<Boolean,Weapon>> getArsenal(){
        List<Tuple<Boolean, Weapon>> ret = new ArrayList<>();
        for (Weapon i: map.getPawn(pov).getActor().getLoadedWeapon()){
            Boolean status = updatedWeapons.getOrDefault(i, Boolean.TRUE);
            ret.add(new Tuple<>(status, i));
        }
        for (Weapon i: map.getPawn(pov).getActor().getUnloadedWeapon()){
            Boolean status = updatedWeapons.getOrDefault(i, Boolean.FALSE);
            ret.add(new Tuple<>(status, i));
        }
        return ret;
    }
}
