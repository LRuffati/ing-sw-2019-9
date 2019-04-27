package gamemanager;

import actions.utils.AmmoColor;
import player.Actor;

import java.util.Collection;
import java.util.Map;

/**
 * This class implements the Domination Mode for the game.
 */

//TODO Basically everything, I need more time to think about a pattern and a way to define the domination point as some sort of player
public class DominationMode extends Scoreboard{
    private Collection<Collection<Map<AmmoColor, Actor>>> spawnTracker;

    public DominationMode(){

    }

    public void scoreControlPoint(Actor a){

    }
}
