package board;

import actions.effects.Effect;
import actions.targeters.targets.BasicTarget;
import actions.targeters.targets.RoomTarget;
import actions.targeters.targets.TileTarget;
import actions.utils.AmmoAmount;
import actions.utils.AmmoAmountUncapped;
import genericitems.Tuple;
import grabbables.Weapon;
import uid.DamageableUID;
import uid.RoomUID;
import uid.TileUID;
import viewclasses.*;

import java.rmi.server.UID;
import java.util.*;
import java.util.stream.Collectors;

public class Sandbox {
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

    private final List<PowerUp> usedPowerups;
    private final AmmoAmount updatedAmmoAvailable; //Excludes powerups

    public final DamageableUID pov;
    private final GameMap map;

    public final String uid;

    public Sandbox(GameMap map, DamageableUID pov){

        this.roomsTargeted = new HashMap<>();
        this.tilesTargeted = new HashMap<>();
        this.pawnsTargeted = new HashMap<>();

        this.effectsHistory = new ArrayList<>();
        this.updatedLocations = new HashMap<>();
        this.updatedWeapons = new HashMap<>();
        this.updatedAmmoAvailable = map.getPawn(pov).getActor().getAmmo();
        this.usedPowerups = new ArrayList<>();

        this.map = map;
        this.pov = pov;

        this.father = null;
        this.uid = new UID().toString();
    }

    public Sandbox(Sandbox parent, List<Effect> effects){
        this.roomsTargeted = new HashMap<>(parent.roomsTargeted);
        this.tilesTargeted = new HashMap<>(parent.tilesTargeted);
        this.pawnsTargeted = new HashMap<>(parent.pawnsTargeted);

        this.pov = parent.pov;
        this.map = parent.map;

        Map<Weapon, Boolean> weaponsTemp = new HashMap<>(parent.updatedWeapons);
        Map<DamageableUID, TileUID> tempLocs = new HashMap<>(parent.updatedLocations);
        AmmoAmount ammoTemp = parent.getUpdatedAmmoAvailable();
        List<PowerUp> powupTemp = parent.usedPowerups;
        for (Effect i: effects){
            weaponsTemp = i.newWeapons(weaponsTemp);
            tempLocs = i.newLocations(tempLocs);
            ammoTemp = i.newAmmoAvailable(ammoTemp);
            powupTemp = i.newUsedPowUp(powupTemp);
        }

        this.updatedWeapons = weaponsTemp;
        this.updatedLocations = tempLocs;
        this.updatedAmmoAvailable = ammoTemp;
        this.usedPowerups = powupTemp;

        this.father = parent;
        this.effectsHistory = new ArrayList<>(father.effectsHistory);
        effectsHistory.addAll(effects);

        this.uid = new UID().toString();
    }

    /**
     *
     * @return the ammo available exclusively in the form of ammo cubes
     */
    public AmmoAmount getUpdatedAmmoAvailable() {
        return updatedAmmoAvailable;
    }

    /**
     *
     * @return the list of effects that cause this sandbox to differ from GameMap
     */
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
     * @param centre the tileUID from which to compute the circle
     * @param radius the radius
     * @param logical go through walls or not
     * @return the set of tileUID withing radius steps from centre
     */
    public Set<TileUID> circle(TileUID centre, int radius, boolean logical){
        HashSet<TileUID> retVal = new HashSet<>();
        HashSet<TileUID> interior = new HashSet<>();
        HashSet<TileUID> border;

        if (radius<0) return retVal;

        retVal.add(centre);
        int rad = 0;

        while (rad<radius){
            //1. Find the border
            border = new HashSet<>(retVal);
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
     * @param tile the tile I want to check
     * @return all players or control points in the tile
     */
    public Collection<DamageableUID> containedPawns(TileUID tile){
        return map.getDamageable().stream()
                .filter(i -> tile(i).equals(tile))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Returns the room containing the tile
     * @param tile the tile in question
     * @return the RoomUID of the containing room
     */
    public RoomUID room(TileUID tile){
        return map.room(tile);
    }

    /**
     * Returns the room containing the pawn
     * @param pawn the pawn in question
     * @return the room containing the pawn
     */
    public RoomUID room(DamageableUID pawn){
        // Use sandbox method because player might have moved
        return map.room(tile(pawn));
    }

    /**
     *
     * @param pawn the pawn I want to know the location of
     * @return the TileUID of the pawn's location
     */
    public TileUID tile(DamageableUID pawn){
        if (updatedLocations.containsKey(pawn))
            return updatedLocations.get(pawn);
        else {
            return map.tile(pawn);
        }
    }

    /**
     * @return All the tiles in the room
     */
    public Collection<TileUID> tilesInRoom(RoomUID room){
        return map.tilesInRoom(room);
    }

    /**
     *
     * @param source the tile from which the observation is made
     * @return the tileUid of all tiles visible from the source
     */
    public Set<TileUID> tilesSeen(TileUID source){
        Collection<TileUID> tilesNear = neighbors(source, true).values();

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

    /**
     *
     * @return all the tiles in the sandbox
     */
    public Set<TileUID> allTiles(){
        return map.allTiles();
    }

    /**
     *
     * @param roomUID the room uid
     * @return the target representing the room
     */
    public RoomTarget getRoom(RoomUID roomUID) {
        if (!roomsTargeted.containsKey(roomUID))
            roomsTargeted.put(roomUID, new RoomTarget(roomUID));

        return roomsTargeted.get(roomUID);
    }

    /**
     *
     * @param tileUID the Tile UID
     * @return the target representing the Tile
     */
    public TileTarget getTile(TileUID tileUID) {
        if (!tilesTargeted.containsKey(tileUID))
            tilesTargeted.put(tileUID, new TileTarget(tileUID));

        return  tilesTargeted.get(tileUID);
    }

    public BasicTarget getBasic(DamageableUID targetUID){
        if (pawnsTargeted.containsKey(targetUID)) return pawnsTargeted.get(targetUID);
        else {
            BasicTarget targ = BasicTarget.basicFactory(map.getPawn(targetUID));
            pawnsTargeted.put(targetUID, targ);
            return pawnsTargeted.get(targetUID);
        }
    }

    /**
     *
     * @return all weapons owned by the player and their availability
     */
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

    /**
     *
     * @return a list of PowerUps available to the user to pay for actions
     */
    public List<PowerUp> powerUpsAvailable(){
        List<PowerUp> ret = new ArrayList<>();
        for (PowerUp p: map.getPawn(pov).getActor().getPowerUp()){
            if (!usedPowerups.contains(p)) ret.add(p);
        }
        return ret;
    }

    /**
     * This method generates a TargetView (TileView) given a TileUID
     */
    public TargetView generateTargetView(TileUID tileUID) {
        return new TargetView(uid, null, List.of(tileUID));
    }

    /**
     * This method generates a TargetView (TileListView) given a Collection of TileUid
     */
    public TargetView generateTargetView(Collection<TileUID> tiles) {
        return new TargetView(uid, null, tiles);
    }

    /**
     * This method generates a TargetView (ActorView) given a DamageableUID
     */
    public TargetView generateTargetView(DamageableUID damageableUID) {
        return new TargetView(uid, List.of(damageableUID), null);
    }

    /**
     * This method generates a TargetView (ActorListView) given a Collection of DamageableUID
     */
    public TargetView generateTargetView(Set<DamageableUID> targets) {
        return new TargetView(uid, targets, null);
    }

    /**
     * This method generates a GameMapView of the current Sandbox.
     * Starting from the GameMapView, all the players position, tha ammoAmount and the weapons are updated considering the Action already performed
     */
    public GameMapView generateView() {
        GameMapView gameMapView = map.generateView(pov);

        for(Map.Entry entry : updatedLocations.entrySet()) {

            TileView tileView = gameMapView.getPosition(map.getCoord((TileUID)entry.getValue()));
            List<ActorView> players = tileView.players();

            for(ActorView actor: gameMapView.players()) {
                if(actor.uid().equals(entry.getKey())) {
                    actor.position().setPlayers(tileView.players().stream().filter(x -> !x.equals(actor)).collect(Collectors.toList()));
                    actor.setPosition(tileView);

                    players.add(actor);
                }
            }

            tileView.setPlayers(players);
        }

        gameMapView.you().setAmmo(new AmmoAmount(getUpdatedTotalAmmoAvailable()));

        gameMapView.you().setLoadedWeapon(getArsenal().stream()
                .filter(i -> i.x == Boolean.TRUE)
                .map(i -> i.y.generateView())
                .collect(Collectors.toList()));
        gameMapView.you().setUnloadedWeapon(getArsenal().stream()
                .filter(i -> i.x == Boolean.FALSE)
                .map(i -> i.y.generateView())
                .collect(Collectors.toList()));

        return gameMapView;
    }

    /**
     * @return get the total ammo, obtained by summing powerups to cubes
     */
    public AmmoAmountUncapped getUpdatedTotalAmmoAvailable() {
        AmmoAmountUncapped cubes = new AmmoAmountUncapped(updatedAmmoAvailable.getAmounts());
        AmmoAmountUncapped pows = new AmmoAmountUncapped();
        for (PowerUp p: powerUpsAvailable()){
            pows.add(new AmmoAmountUncapped(p.getAmmo().getAmounts()));
        }
        return cubes.add(pows);
    }
}
