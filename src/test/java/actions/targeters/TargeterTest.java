package actions.targeters;

import actions.conditions.HasCondition;
import actions.selectors.DistanceSelector;
import actions.selectors.HasSelector;
import actions.selectors.VisibleSelector;
import actions.targeters.targets.BasicTarget;
import actions.targeters.targets.Targetable;
import actions.utils.ChoiceMaker;
import board.Coord;
import board.GameMap;
import board.Sandbox;
import controller.GameMode;
import controller.controllermessage.ControllerMessage;
import gamemanager.GameBuilder;
import genericitems.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import player.Actor;
import uid.TileUID;
import viewclasses.TargetView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class TargeterTest {

    Sandbox sandbox;
    GameMap map;
    Map<Integer, Targetable> targetMap;
    List<Actor> actorList;
    @BeforeEach
    void setup(){
        targetMap = new HashMap<>();
        GameBuilder gameBuilder = new GameBuilder(GameMode.NORMAL, 4);
        map = gameBuilder.getMap();
        actorList = gameBuilder.getActorList();
        actorList.get(0).move(map.getPosition(new Coord(1,2)));
        actorList.get(1).move(map.getPosition(new Coord(1,1)));
        actorList.get(2).move(map.getPosition(new Coord(0,2)));
        actorList.get(3).move(map.getPosition(new Coord(1,0)));
        sandbox = map.createSandbox(actorList.get(0).pawnID());
        targetMap.put(0, sandbox.getBasic(actorList.get(0).pawnID()));
        targetMap.put(1, sandbox.getBasic(actorList.get(1).pawnID()));
        targetMap.put(2, sandbox.getBasic(actorList.get(2).pawnID()));
        targetMap.put(3, sandbox.getBasic(actorList.get(3).pawnID()));
    }

    @Test
    void distanceLogic() {
        TargeterTemplate dist_log = new TargeterTemplate(
                new Tuple<>("self", new DistanceSelector(0,1,true)),
                List.of(),
                "pawn",
                true, false,false, ""
        );
        ChoiceMaker choiceMaker = new ChoiceMaker() {
            @Override
            public void giveTargets(String targetId, List<TargetView> possibilities, Function<Integer, Targetable> action) {
                assertEquals(1, possibilities.size());
                List<Targetable> targets = new ArrayList<>();
                for (int i=0; i<possibilities.size(); i++){
                    targets.add(action.apply(i));
                }
                assertEquals(targetMap.get(2), targets.get(0));
            }

            @Override
            public Tuple<Boolean, List<TargetView>> showOptions() {
                return null;
            }

            @Override
            public ControllerMessage pick(int choice) {
                return null;
            }
        };
        new Targeter(sandbox, choiceMaker,Map.of("self", targetMap.get(0)), dist_log, "log").giveChoices();
    }

    @Test
    void distancePh() {
        TargeterTemplate dist_log = new TargeterTemplate(
                new Tuple<>("self", new DistanceSelector(0,1,false)),
                List.of(),
                "pawn",
                true, false,false, ""
        );
        ChoiceMaker choiceMaker = new ChoiceMaker() {
            @Override
            public void giveTargets(String targetId, List<TargetView> possibilities, Function<Integer, Targetable> action) {
                assertEquals(2, possibilities.size());
                List<Targetable> targets = new ArrayList<>();
                for (int i=0; i<possibilities.size(); i++){
                    targets.add(action.apply(i));
                }
                assertTrue(targets.containsAll(List.of(targetMap.get(1), targetMap.get(2))));
            }

            @Override
            public Tuple<Boolean, List<TargetView>> showOptions() {
                return null;
            }

            @Override
            public ControllerMessage pick(int choice) {
                return null;
            }
        };
        new Targeter(sandbox, choiceMaker,Map.of("self", targetMap.get(0)), dist_log, "log").giveChoices();
    }

    @Test
    void directionLog() {
        TargeterTemplate dist_log = new TargeterTemplate(
                new Tuple<>("targ4", new HasSelector()),
                List.of(new Tuple<>("targ2", new HasCondition(false))),
                "direction",
                true, false,false, ""
        );
        ChoiceMaker choiceMaker = new ChoiceMaker() {
            @Override
            public void giveTargets(String targetId, List<TargetView> possibilities, Function<Integer, Targetable> action) {
                assertEquals(1, possibilities.size());
                Targetable direction = action.apply(0);;
                assertTrue(direction.getSelectedPawns(sandbox).containsAll(List.of(actorList.get(1).pawnID(),
                        actorList.get(3).pawnID())));
                assertEquals(2, direction.getSelectedTiles(sandbox).size());
            }

            @Override
            public Tuple<Boolean, List<TargetView>> showOptions() {
                return null;
            }

            @Override
            public ControllerMessage pick(int choice) {
                return null;
            }
        };
        new Targeter(sandbox, choiceMaker,Map.of("targ2", targetMap.get(1), "targ4",
                targetMap.get(3), "self", targetMap.get(0)),
                dist_log,
                "log").giveChoices();
    }

    @Test
    void directionPh() {
        TargeterTemplate dist_log = new TargeterTemplate(
                new Tuple<>("targ4", new HasSelector()),
                List.of(new Tuple<>("targ2", new HasCondition(false))),
                "directionph",
                true, false,false, ""
        );
        ChoiceMaker choiceMaker = new ChoiceMaker() {
            @Override
            public void giveTargets(String targetId, List<TargetView> possibilities, Function<Integer, Targetable> action) {
                assertEquals(1, possibilities.size());
                Targetable direction = action.apply(0);;
                assertTrue(direction.getSelectedPawns(sandbox).containsAll(List.of(actorList.get(1).pawnID(),
                        actorList.get(3).pawnID())));
                assertEquals(4, direction.getSelectedTiles(sandbox).size());
            }

            @Override
            public Tuple<Boolean, List<TargetView>> showOptions() {
                return null;
            }

            @Override
            public ControllerMessage pick(int choice) {
                return null;
            }
        };
        new Targeter(sandbox, choiceMaker,Map.of("targ2", targetMap.get(1), "targ4",
                targetMap.get(3), "self", targetMap.get(0)),
                dist_log,
                "log").giveChoices();
    }
}