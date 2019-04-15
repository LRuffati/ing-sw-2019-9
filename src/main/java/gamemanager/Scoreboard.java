package gamemanager;

import board.GameMap;
import player.Actor;

import java.util.*;

/**
 * This class implements the Scoreboard for the Deathmatch games. It checks if the Final Frenzy is starting and add
 * every kills to the Scoreboard with the player who committed the frag and how many tokens he got from it.
 */
public class Scoreboard {
    private Collection<Actor> actorsList;
    private int numOfDeaths;
    private int maxDeaths;
    private ArrayList<Map<Actor, Integer>> skullBox;

    /**
     * Constructor for a standard game (8 skulls).
     */
    public Scoreboard(){
        this.actorsList = new ArrayList<>();
        this.maxDeaths = 8;
        this.numOfDeaths = 0;
        this.skullBox = new ArrayList<>();
    }

    /**
     * Constructor for a custom game.
     * @param skulls will be the lenght of the scoreboard.
     */
    public Scoreboard(int skulls){
        this.maxDeaths = skulls;
        this.numOfDeaths = 0;
        this.skullBox = new ArrayList<>();
    }

    /**
     *
     * @return true if all the skulls are removed and the final phase of the game needs to start.
     */
    public boolean finalFrenzy(){
        return numOfDeaths == maxDeaths;
    }

    /**
     * Remove a skull and add the killer marker(s).
     * @param killer is the player who got the kill.
     * @param overkill if has been done more damage than what was necessary to kill the target.
     */
    //TODO Understand how to manage all the kill cases.
    public void addKill(Actor killer, Boolean overkill){
        Map<Actor, Integer> m = new HashMap<>();
        if(overkill){
            m.put(killer,2);
        } else {
            m.put(killer,1);
        }
        this.skullBox.add(m);
        numOfDeaths++;
    }

    /**
     * This method add a player to the game.
     * @param a is the actor controlled by the player.
     */
    public void addPlayer(Actor a){
        if(this.actorsList == null){
            a.setStartingPlayerMarker();
        }
        actorsList.add(a);
    }

    /**
     * Parse the players list and return the player with more points.
     * @param actorsList are the players playing this game.
     * @return the actor controller by the winner player.
     */
    public Actor claimWinner(Collection<Actor> actorsList){
        Actor maxA = null;
        for(Actor a:actorsList){
            if(maxA == null || a.getPoints()>maxA.getPoints()) maxA = a;
        }
        return maxA;
    }

    /**
     * End the player turn and start the next player's turn.
     */
    public void nextTurn(){
        boolean flag = false;
        for(Actor a:actorsList){
            if(flag){
                a.setTurn(true);
                break;
            }
            if(a.isTurn()){
                a.setTurn(false);
                flag = true;
            }

        }
    }
}
