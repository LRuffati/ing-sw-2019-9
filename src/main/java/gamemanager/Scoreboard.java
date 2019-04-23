package gamemanager;

import board.GameMap;
import player.Actor;

import java.util.*;

/**
 * This class implements the Scoreboard for the Deathmatch games. It checks if the Final Frenzy is starting and add
 * every kills to the Scoreboard with the player who committed the frag and how many tokens he got from it.
 */
public class Scoreboard {
    private ArrayList<Actor> actorsList;
    private int numOfDeaths;
    private final int maxDeaths;
    private ArrayList<Map<Actor, Integer>> skullBox;

    private final List<Integer> pointForDeath = List.of(8,6,4,2,1,1,1,1);

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
     * Add to the @points attribute of every player (in the class actor) the points gain from a kill.
     */
    public void score(Actor dead){
        TreeSet<Actor> set = new TreeSet<>
                (Comparator.comparing(
                        x -> Collections.frequency(dead.getDamageTaken(), x)
                ));

        if(dead.getPawn().getTile()==null){
            dead.getDamageTaken().get(0).addPoints(1);

            set.addAll(dead.getDamageTaken());

            int num = dead.getNumOfDeaths();
            for(Actor actor : set.descendingSet()){
                // TODO: OK O <= O ALTRO?
                if(num < pointForDeath.size()) {
                    actor.addPoints(pointForDeath.get(num));
                    num++;
                }
            }

            /*ArrayList<Integer> max = new ArrayList<>();
            max.add(0);
            for(Actor i:actorsList){
                int count = Collections.frequency(dead.getDamageTaken(), i);
                if(count > max.get(0)) max.add(count);
            }

            //TODO check if it parse the players in the same order twice (probably it doesn't).
            for(Actor i:actorsList){
                if(max.get(0)==0) break;
                i.addPoints(max.get(0));
                max.remove(0);
            }
            */

        }
    }

    /**
     * Remove a skull and add the killer marker(s).
     * @param killer is the player who got the kill.
     */
    //TODO Understand how to manage all the kill cases.
    public void addKill(Actor killer, Actor victim){
        Map<Actor, Integer> m = new HashMap<>();
        //if(victim.getDamageTaken().get(11)!= null)                    gestire overkill con marchi
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
