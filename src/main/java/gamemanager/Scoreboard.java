package gamemanager;

import player.Actor;

import java.util.*;

/**
 * This class implements the Scoreboard for the Deathmatch games. It checks if the Final Frenzy is starting and add
 * every kills to the Scoreboard with the player who committed the frag and how many tokens he got from it.
 */
//TODO: see what is part of the model and what part of the controller
public class Scoreboard {
    private final List<Actor> actorsList;
    private int numOfDeaths;
    private final int maxDeaths;
    private ArrayList<Map<Actor, Integer>> skullBox;

    private final List<Integer> pointForDeath = List.of(8,6,4,2,1,1,1,1); //TODO: caricare da file settings

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

    Scoreboard(){
        this.actorsList = new ArrayList<>();
        this.maxDeaths = 0;
    }

    public int getMaxDeaths() {
        return maxDeaths;
    }

    /**
     *
     * @return true if all the skulls are removed and the final phase of the game needs to start.
     */
    public boolean finalFrenzy(){
        return numOfDeaths >= maxDeaths;
    }

    /**
     * Add to the @points attribute of every player (in the class actor) the points gain from a kill.
     */
    public void score(Actor dead){
        TreeSet<Actor> scoreSet = new TreeSet<>
                (Comparator.comparing(
                        x -> Collections.frequency(dead.getDamageTaken(), x)
                ));

        if(dead.isDead()){
            dead.getDamageTaken().get(0).addPoints(1);

            scoreSet.addAll(dead.getDamageTaken());

            int num = dead.getNumOfDeaths();
            for(Actor actor : scoreSet.descendingSet()){
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
    public void addKill(Actor killer, Actor victim){
        int numPoints = 1;
        if(victim.getDamageTaken().get(11)!= null) {
            numPoints = 2;
            //TODO: probably should't be done here
            victim.addMark(killer.getPawn().getDamageableUID(), 1);
        }
        skullBox.add(Map.of(killer, numPoints));
        numOfDeaths++;
    }


    /**
     * Parse the players list and return the player with more points.
     * @return the actor controller by the winner player.
     */
    public Actor claimWinner(){
        Actor maxA = null;
        for(Actor a:actorsList){
            if(maxA == null || a.getPoints()>maxA.getPoints()) maxA = a;
        }
        return maxA;
    }

    /**
     * Needed for tests.
     * @return list of actors.
     */
    public List<Actor> getActorsList() {
        return actorsList;
    }

    /**
     * Needed for tests.
     * @return the object skullbox.
     */
    public List<Map<Actor, Integer>> getSkullBox() {
        return new ArrayList<>(skullBox);
    }
}
