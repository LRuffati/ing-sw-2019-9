package player;

import board.GameMap;
import uid.DamageableUID;
import viewclasses.GameMapView;

public class ControlPointActor extends Actor{
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

    @Override
    public void damage(Actor shooter, int numOfDmg) {
        super.damage(shooter, numOfDmg);
    }

    @Override
    public void damageRaw(Actor shooter, int numOfDmg) {
        super.damageRaw(shooter, numOfDmg);
    }



}
