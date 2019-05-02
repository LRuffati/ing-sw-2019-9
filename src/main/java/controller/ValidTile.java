package controller;

import board.GameMap;
import player.Actor;
import uid.TileUID;

import java.util.List;
import java.util.Set;

public class ValidTile {
    private ValidTile(){}

    /**
     * This method returns all the Tiles that can be reached with a Move action
     * @param map The Map of the game
     * @param actor The actor that makes the check
     * @param actorList List containing all the Actors in the game. This list must have the same order of the game sequence
     * @return A Set containing all the Tiles that can be reached with a Move action in the current phase of the game
     */
    public static Set<TileUID> getMove(GameMap map, Actor actor, List<Actor> actorList){
        return allOperations(map,actor,actorList,
                new Values(-1,0,3,0,4));
    }

    /**
     * This method returns all the Tiles that can be reached with a Grab action
     * @param map The Map of the game
     * @param actor The actor that makes the check
     * @param actorList List containing all the Actors in the game. This list must have the same order of the game sequence
     * @return A Set containing all the Tiles that can be reached with a Grab action in the current phase of the game
     */
    public static Set<TileUID> getGrab(GameMap map, Actor actor, List<Actor> actorList){
        return allOperations(map,actor,actorList,
                new Values(3,1,2,3,2));
    }

    /**
     * This method returns all the Tiles that can be reached with a Fire action
     * @param map The Map of the game
     * @param actor The actor that makes the check
     * @param actorList List containing all the Actors in the game. This list must have the same order of the game sequence
     * @return A Set containing all the Tiles that can be reached with a Fire action in the current phase of the game
     */
    public static Set<TileUID> getFire(GameMap map, Actor actor, List<Actor> actorList){
        return allOperations(map,actor,actorList,
                new Values(6,0,1,2,1));
    }

    private static Set<TileUID> allOperations(GameMap map, Actor actor, List<Actor> actorList, Values v){
        TileUID tile = actor.pawn().getTile();
        List<Actor> dmg = actor.getDamageTaken();
        if(!actor.getFrenzy()){
            if (dmg.size() < v.limitInDmg)
                return map.getSurroundings(false, v.distBeforeLim, tile);
            else
                return map.getSurroundings(false, v.distAfterLim, tile);
        }
        else {
            if(beforeFirstFrenzy(actor, actorList))
                return map.getSurroundings(false, v.distBeforeFirstFrenzy, tile);
            else
                return map.getSurroundings(false, v.distAfterFirstFrenzy, tile);
        }
    }

    private static boolean beforeFirstFrenzy(Actor actor, List<Actor> actorList){
        //TODO check if the first element is always the player that started the Frenzy
        Actor firstFrenzy = (Actor)actorList.stream().filter(Actor::getFrenzy).toArray()[0];
        return actorList.indexOf(actor) < actorList.indexOf(firstFrenzy);
    }


    private static class Values{
        int limitInDmg;
        int distBeforeLim;
        int distAfterLim;
        int distBeforeFirstFrenzy;
        int distAfterFirstFrenzy;
        Values(int limitInDmg, int distBeforeLim, int distAfterLim,
               int distBeforeFirstFrenzy, int distAfterFirstFrenzy){
            this.limitInDmg = limitInDmg;
            this.distBeforeLim = distBeforeLim;
            this.distAfterLim = distAfterLim;
            this.distBeforeFirstFrenzy = distBeforeFirstFrenzy;
            this.distAfterFirstFrenzy = distAfterFirstFrenzy;
        }
    }
}
