package gamemanager;

import player.Actor;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This class implements the Domination Mode for the game.
 */

public class DominationMode extends Scoreboard{
    private ArrayList<ArrayList<Actor>> spawnTracker;
    private ArrayList<Map<Actor, Integer>> skullBox;
    private int maxDeaths;
    private int numOfDeaths;
    private final List<Actor> actorList;

    private final List<Integer> pointForDeath
            = Arrays.stream(ParserConfiguration.parse("scoreBeforeFrenzy").split(","))
            .map(Integer::parseInt).collect(Collectors.toCollection(ArrayList::new));

    private final List<Integer> pointForDeathFinal
            = Arrays.stream(ParserConfiguration.parse("scoreAfterFrenzy").split(","))
            .map(Integer::parseInt).collect(Collectors.toCollection(ArrayList::new));

    private final List<Integer> pointForSpawnTracker = new ArrayList<>(List.of(8,6,4,2,1));



    public DominationMode(List<Actor> actorList){
        this(actorList, ParserConfiguration.parseInt("numOfDeaths"));
    }

    public DominationMode(List<Actor> actorList, int numOfdeaths) {
        this.actorList = actorList;
        this.maxDeaths = numOfdeaths;
        this.numOfDeaths = 0;
        this.skullBox = new ArrayList<>();
        this.spawnTracker = new ArrayList<>();
        spawnTracker.add(new ArrayList<>());
        spawnTracker.add(new ArrayList<>());
        spawnTracker.add(new ArrayList<>());
    }

    public boolean finalFrenzy() {
        return numOfDeaths >= maxDeaths || spawnTracker.stream().filter(x -> x.size() == 8).count() >= 2;
    }


    public void addSpawnTrackerPoint(int index, Actor actor) {
        spawnTracker.get(index).add(actor);
    }

    public void addPointsAtEndGame() {
        for(ArrayList list : spawnTracker) {
            TreeSet<Actor> scoreSet = new TreeSet<>
                    (Comparator.comparing(
                            x -> Collections.frequency(list, x)
                    ));
            int i = 0;
            for(Actor actor : scoreSet.descendingSet()){
                scoreSet.clear();
                scoreSet.addAll(spawnTracker.get(i));
                actor.addPoints(pointForSpawnTracker.get(i));
                i++;
            }
        }
    }

    /**
     * Remove a skull and add the killer marker(s).
     * @param killer is the player who got the kill.
     */
    public void addKill(Actor killer, Actor victim){
        int numPoints = 1;
        if(victim.getDamageTaken().size()>10 && victim.getDamageTaken().get(11)!= null) {
            //todo: aggiungere punto al track giusto
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
    public Actor claimWinner() {
        Actor maxA = null;
        for(Actor a : actorList){
            if(maxA == null || a.getPoints()>maxA.getPoints())   maxA = a;
        }
        return maxA;
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
                if(!finalFrenzy()) {
                    if (num < pointForDeath.size()) {
                        actor.addPoints(pointForDeath.get(num));
                        num++;
                    }
                }
                else {
                    if (num < pointForDeathFinal.size()) {
                        actor.addPoints(pointForDeathFinal.get(num));
                        num++;
                    }
                }
            }
        }
    }
}
