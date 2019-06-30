package player;

import board.GameMap;
import gamemanager.DominationMode;
import gamemanager.Scoreboard;
import genericitems.Tuple;
import uid.DamageableUID;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ControlPointActor extends Actor{

    private List<Tuple<Actor, Color>> damageList = new ArrayList<>();
    /**
     * This constructor gets the GameMap and the Pawn, and build the Actor
     *
     * @param map         GameMap
     * @param pawnId      the Pawn identifier that has to be associated with this Actor
     * @param firstPlayer True if the Actor is the first player in the game
     */
    public ControlPointActor(GameMap map, DamageableUID pawnId, boolean firstPlayer) {
        super(map, pawnId, firstPlayer);
    }

    public void addDamageList(Actor actor, Color color) {
        damageList.add(new Tuple<>(actor, color));
    }

    @Override
    public void damage(Actor shooter, int numOfDmg) {
        damageRaw(shooter, numOfDmg);
    }

    @Override
    public void damageRaw(Actor shooter, int numOfDmg) {
        super.damageRaw(shooter, numOfDmg);
    }


    @Override
    public boolean endTurn(Actor player, Scoreboard scoreboard) {
        for (Tuple<Actor, Color> t : damageList) {
            ((DominationMode)scoreboard).addSpawnTrackerPoint(t.y, t.x);
        }
        return false;
    }

    @Override
    public int addMark(Actor attackerActor, int numOfMarks) {
        //does nothing
        return 0;
    }
}
