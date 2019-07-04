package gamemanager;

import player.Actor;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This class implements the Scoreboard for Normal mode. It checks if the Final Frenzy is starting and add
 * every kills to the Scoreboard with the player who committed the frag and how many tokens he got from it.
 */
public class Scoreboard {
    List<Actor> actorsList;
    int numOfDeaths;
    int maxDeaths;
    ArrayList<Map<Actor, Integer>> skullBox;

    final List<Integer> pointForDeath
            = Arrays.stream(ParserConfiguration.parse("scoreBeforeFrenzy").split(","))
            .map(Integer::parseInt).collect(Collectors.toCollection(ArrayList::new));

    final List<Integer> pointForDeathFinal
            = Arrays.stream(ParserConfiguration.parse("scoreAfterFrenzy").split(","))
            .map(Integer::parseInt).collect(Collectors.toCollection(ArrayList::new));

    /**
     * Constructor for a standard game (8 skulls).
     */
    public Scoreboard(){
        this(ParserConfiguration.parseInt("numOfDeaths"));
    }

    /**
     * Constructor for a custom game.
     * @param skulls will be the lenght of the scoreboard.
     */
    public Scoreboard(int skulls){
        this.actorsList = new ArrayList<>();
        this.maxDeaths = skulls;
        this.numOfDeaths = 0;
        this.skullBox = new ArrayList<>();
    }

    public Scoreboard(List<Actor> actorList, int skulls) {
        this.actorsList = actorList;
        this.maxDeaths = skulls;
        this.numOfDeaths = 0;
        this.skullBox = new ArrayList<>();
    }

    /**
     * Method that binds the scoreboard to the actors
     * @param actorList a List containing all the actors
     */
    void setActor(List<Actor> actorList) {
        this.actorsList = actorList;
    }

    /**
     *
     * @return The number of death needed to start the Final Frenzy
     */
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
     * Add to the points attribute of every player (in the class actor) the points gain from a kill.
     */
    public void score(Actor dead) {

        TreeSet<Actor> scoreSet = new TreeSet<>
                (Comparator.comparing(
                        x -> Collections.frequency(dead.getDamageTaken(), x)
                ));

        if (!dead.getDamageTaken().get(0).equals(dead))
            dead.getDamageTaken().get(0).addPoints(1);

        List<Actor> damages = dead.getDamageTaken().stream()
                .filter(a -> a != dead).collect(Collectors.toList());

        scoreSet.addAll(damages);

        int num = dead.getNumOfDeaths();
        for (Actor actor : scoreSet.descendingSet()) {
            // TODO: OK O <= O ALTRO?
            if (!finalFrenzy()) {
                if (num < pointForDeath.size()) {
                    actor.addPoints(pointForDeath.get(num));
                    num++;
                }
            } else {
                if (num < pointForDeathFinal.size()) {
                    actor.addPoints(pointForDeathFinal.get(num));
                    num++;
                }
            }
        }
    }

    /**
     * Remove a skull and add the killer marker(s).
     * @param killer is the player who got the kill.
     */
    public void addKill(Actor killer, Actor victim) {
        if(victim.getDamageTaken().size()>10) {
            //TODO: probably should't be done here
            victim.addMark(killer, 1);
            skullBox.add(Map.of(killer, 1));
        }
        skullBox.add(Map.of(killer, 1));
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
     * @return list of actors.
     */
    public List<Actor> getActorsList() {
        return actorsList;
    }

    /**
     * @return the object skullbox.
     */
    public List<Map<Actor, Integer>> getSkullBox() {
        return new ArrayList<>(skullBox);
    }

    /**
     * @return the number of current deaths in the game.
     */
    public int getNumOfDeaths() {
        return numOfDeaths;
    }
}
