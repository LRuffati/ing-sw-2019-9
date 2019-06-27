package gamemanager;

import player.Actor;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class implements the Domination Mode for the game.
 */

public class DominationMode extends Scoreboard{
    private Map<Color, List<Actor>> spawnTracker;

    public void addTrack(Color color){
        spawnTracker.put(color, new ArrayList<>());
    }

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

    private final List<Integer> pointForSpawnTracker
            = Arrays.stream(ParserConfiguration.parse("scoreSpawnTracker").split(","))
            .map(Integer::parseInt).collect(Collectors.toCollection(ArrayList::new));



    public DominationMode(){
        this(ParserConfiguration.parseInt("numOfDeaths"));
    }

    public DominationMode(int numOfdeaths) {
        super(numOfdeaths);
        this.actorList = new ArrayList<>();
        this.maxDeaths = numOfdeaths;
        this.numOfDeaths = 0;
        this.skullBox = new ArrayList<>();
        this.spawnTracker = new HashMap<>();
    }

    public Map<Color, List<Actor>> getSpawnTracker() {
        return spawnTracker;
    }

    public boolean finalFrenzy() {
        return numOfDeaths >= maxDeaths || spawnTracker.values().stream().filter(x -> x.size() == 8).count() >= 2;
    }


    public void addSpawnTrackerPoint(Color color, Actor actor) {
        spawnTracker.get(color).add(actor);
    }

    public void addPointsAtEndGame() {
        TreeSet<Actor> scoreSet;
        for(List<Actor> list : spawnTracker.values()) {
            scoreSet = new TreeSet<>
                    (Comparator.comparing(
                            x -> Collections.frequency(list, x)
                    ));
            scoreSet.addAll(list);
            int i = 0;
            //FIXME: possible bug in this, seems to take only one
            //todo: should be solved, da testare per sicurezza
            for(Actor actor : scoreSet.descendingSet()){
                actor.addPoints(pointForSpawnTracker.get(i));
                i++;
            }
        }
    }

    /**
     * Remove a skull and add the killer marker(s).
     * @param killer is the player who got the kill.
     */
    @Override
    public void addKill(Actor killer, Actor victim){
        int numPoints = 1;
        if(victim.getDamageTaken().size()>10 && victim.getDamageTaken().get(11)!= null) {
            //todo: aggiungere punto al track giusto
            //TODO: probably should't be done here
            victim.addMark(killer, 1);
        }
        skullBox.add(Map.of(killer, numPoints));
        numOfDeaths++;
    }

    /**
     * Parse the players list and return the player with more points.
     * @return the actor controller by the winner player.
     */
    @Override
    public Actor claimWinner() {
        Actor maxA = null;
        for (Actor a : actorList) {
            if (maxA == null || a.getPoints() > maxA.getPoints()) maxA = a;
        }
        return maxA;
    }
}
