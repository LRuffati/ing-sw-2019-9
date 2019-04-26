package gamemanager;

import player.Actor;

import java.util.*;

/**
 * This class implements the Scoreboard for the Deathmatch games. It checks if the Final Frenzy is starting and add
 * every kills to the Scoreboard with the player who committed the frag and how many tokens he got from it.
 */
public class Scoreboard {
    private final List<Actor> actorsList;
    private int numOfDeaths;
    private final int maxDeaths;
    private ArrayList<Map<Actor, Integer>> skullBox;

    private final List<Integer> pointForDeath = List.of(8,6,4,2,1,1,1,1);

    /**
     * Constructor for a standard game (8 skulls).
     */
    public Scoreboard(List<Actor> actorList){
        this(actorList, 8);
    }

    /**
     * Constructor for a custom game.
     * @param skulls will be the lenght of the scoreboard.
     */
    public Scoreboard(List<Actor> actorList, int skulls){
        this.actorsList = actorList;
        this.maxDeaths = skulls;
        this.numOfDeaths = 0;
        this.skullBox = new ArrayList<>();
    }

    public Scoreboard(){
        this.actorsList = null;
        this.maxDeaths = 0;
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
