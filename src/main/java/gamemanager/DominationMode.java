package gamemanager;

import player.Actor;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class implements the scoreboard for the domination mode
 */
public class DominationMode extends Scoreboard{
    private Map<Color, List<Actor>> spawnTracker;

    public void addTrack(Color color){
        spawnTracker.put(color, new ArrayList<>());
    }

    private final List<Integer> pointForSpawnTracker
            = Arrays.stream(ParserConfiguration.parse("scoreSpawnTracker").split(","))
            .map(Integer::parseInt).collect(Collectors.toCollection(ArrayList::new));


    /**
     * Basic constructor
     */
    DominationMode(){
        this(ParserConfiguration.parseInt("numOfDeaths"));
    }

    /**
     * Constructor that allows to choose the number of death needed to complete the game
     */
    DominationMode(int numOfdeaths) {
        super(numOfdeaths);
        this.spawnTracker = new HashMap<>();
    }

    /**
     *
     * @return A Map containing all the spawnTrackers
     */
    public Map<Color, List<Actor>> getSpawnTracker() {
        return spawnTracker;
    }

    /**
     *
     * @return true iif FinalFrenzy phase has started
     */
    @Override
    public boolean finalFrenzy() {
        return numOfDeaths >= maxDeaths
                || spawnTracker.values().stream()
                .filter(x -> x.size() == 8).count() >= 2;
    }

    /**
     * Adds a point to the correct ColorTrack
     * @param color the ColorTrack that will receive the point
     * @param actor the actor that scores the point
     */
    public void addSpawnTrackerPoint(Color color, Actor actor) {
        spawnTracker.get(color).add(actor);
    }

    /**
     * Used at the end of the game, it counts the points on the colorTracks and assign the bonus to the players that awarded more points
     */
    private void addPointsAtEndGame() {
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
            for(Actor actor : scoreSet.descendingSet()) {
                actor.addPoints(pointForSpawnTracker.get(i));
                i++;
            }
        }
    }

    /**
     * Parse the players list and return the player with more points.
     * @return the actor controller by the winner player.
     */
    @Override
    public Actor claimWinner() {
        addPointsAtEndGame();
        Actor maxA = null;
        for (Actor a : actorsList) {
            if (maxA == null || a.getPoints() > maxA.getPoints()) maxA = a;
        }
        return maxA;
    }
}
