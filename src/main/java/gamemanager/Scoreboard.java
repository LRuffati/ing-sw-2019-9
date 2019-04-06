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

    public Scoreboard(){
        this.maxDeaths = 8;
        this.numOfDeaths = 0;
        this.skullBox = new ArrayList<>();
    }

    public Scoreboard(int skulls){
        this.maxDeaths = skulls;
        this.numOfDeaths = 0;
        this.skullBox = new ArrayList<>();
    }

    public boolean finalFrenzy(){
        return numOfDeaths == maxDeaths;
    }

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