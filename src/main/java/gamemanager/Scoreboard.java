package gamemanager;

import player.Actor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This class implements the Scoreboard for the Deathmatch games. It checks if the Final Frenzy is starting and add
 * every kills to the Scoreboard with the player who committed the frag and how many tokens he got from it.
 */
public class Scoreboard {
    private int numOfDeaths;
    private int maxDeaths;
    private ArrayList<Map<Actor, Integer>> skullBox;

    /**
     * Constructor for a standard game (8 skulls).
     */
    public Scoreboard(){
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

}
