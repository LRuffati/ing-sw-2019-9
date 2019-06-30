package player;

import actions.utils.AmmoAmount;
import actions.utils.AmmoColor;
import board.GameMap;
import gamemanager.DominationMode;
import gamemanager.Scoreboard;
import genericitems.Tuple;
import uid.DamageableUID;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @Override
    public void damage(Actor shooter, int numOfDmg) {
        damaged = true;
    }

    @Override
    public void damageRaw(Actor shooter, int numOfDmg) {
        damaged = true;
    }

    public void steppedOn(Actor boldOne){
        stepped = true;
    }

    @Override
    public int addMark(Actor attackerActor, int numOfMarks) {
        return 0;
    }

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
