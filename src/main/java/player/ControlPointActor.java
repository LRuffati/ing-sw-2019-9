package player;

import board.GameMap;
import gamemanager.DominationMode;
import gamemanager.Scoreboard;
import uid.DamageableUID;

import java.awt.*;

/**
 * This class extends the {@link Actor Actor} class and is used to contain a ControlPointActor
 */
public class ControlPointActor extends Actor{

    private boolean damaged;
    private Color spawnColor;
    private boolean stepped;
    /**
     * This constructor gets the GameMap and the Pawn, and build the Actor
     *
     * @param map         GameMap
     * @param pawnId      the Pawn identifier that has to be associated with this Actor
     * @param firstPlayer True if the Actor is the first player in the game
     */
    public ControlPointActor(GameMap map, DamageableUID pawnId, boolean firstPlayer, Color spawnColor) {
        super(map, pawnId, firstPlayer);
        this.spawnColor = spawnColor;
        damaged = false;
        stepped = false;
    }

    /**
     * Sets the Damage flag as true. The actor will gain a point
     * @param shooter the attacker
     * @param numOfDmg number of damage points
     */
    @Override
    public void damage(Actor shooter, int numOfDmg) {
        damaged = true;
    }

    /**
     * Sets the Damage flag as true. The actor will gain a point
     * @param shooter the attacker
     * @param numOfDmg number of damage points
     */
    @Override
    public void damageRaw(Actor shooter, int numOfDmg) {
        damaged = true;
    }

    /**
     * Sets the Stepped flag as true. The actor will gain a point
     */
    public void steppedOn(Actor boldOne){
        stepped = true;
    }

    /**
     * Domination point can not take marks, so nothing happens here
     * @param attackerActor The attacker
     * @param numOfMarks the number of marks to apply
     */
    @Override
    public int addMark(Actor attackerActor, int numOfMarks) {
        return 0;
    }

    /**
     * At the end of the turn if the actor is in the {@link board.DominationPointTile tile} or the actor damaged the Domaination Point it will gain a point
     * @param player The player that finished the turn
     * @param scoreboard Scoreboard
     * @return always false, since the DominationPoint cannot die
     */
    @Override
    public boolean endTurn(Actor player, Scoreboard scoreboard) {
        if (damaged){
            ((DominationMode)scoreboard).addSpawnTrackerPoint(spawnColor, player);
        }
        if (stepped){
            ((DominationMode) scoreboard).addSpawnTrackerPoint(spawnColor, player);
        }
        damaged=false;
        stepped=false;
        return false;
    }
}
