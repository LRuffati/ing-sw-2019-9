package actions;

import actions.conditions.Condition;
import actions.effects.EffectTemplate;
import actions.selectors.HasSelector;
import actions.selectors.Selector;
import actions.targeters.TargeterTemplate;
import actions.targeters.targets.DirectionTarget;
import actions.targeters.targets.Targetable;
import board.Coord;
import board.Direction;
import board.GameMap;
import board.Sandbox;
import controllerresults.ActionResultType;
import controllerresults.ControllerActionResultServer;
import gamemanager.GameBuilder;
import genericitems.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import player.Actor;

import java.io.FileNotFoundException;
import java.util.*;
import java.util.function.Function;

import static org.mockito.Mockito.*;

class ActionTest {
    Action testedAction;
    Sandbox oldsandbox;
    Sandbox newsandbox;
    Function<Tuple<Sandbox, Map<String, Targetable>>, ControllerActionResultServer> finalizer;
    ActionInfo mockInfo;
    private GameMap map;
    private List<Actor> actorList;

    @BeforeEach
    void setup(){
        GameBuilder builder = null;
        String tilePath = "src/resources/ammoTile.txt";
        String mapPath = "src/resources/map1.txt";
        try {
            builder = new GameBuilder(
                    mapPath, null, null, tilePath, 3);
        }
        catch (FileNotFoundException e){
        }
        map = builder.getMap();
        actorList = builder.getActorList();
        newsandbox = mock(Sandbox.class);
        finalizer = sandboxMapTuple -> {
            return new ControllerActionResultServer(ActionResultType.TERMINATED);
                };
        mockInfo = mock(ActionInfo.class);
    }

    @Test
    void iterateAutomaticTarget(){
        List<Tuple<String, TargeterTemplate>> targeters = new ArrayList<>();
        Selector selector1 = new HasSelector();
        List<Tuple<String, Condition>> filters = new ArrayList<>();
        TargeterTemplate tt1 = new TargeterTemplate(new Tuple<>("Pietro", selector1), filters,
                "tile", false, true,true);
        Tuple<String,TargeterTemplate> t1 = new Tuple<>("Pietro", tt1);
        targeters.add(t1);
        List<EffectTemplate> effects = new ArrayList<>();
        ActionTemplate aT = new ActionTemplate(mockInfo, targeters, effects);
        Map<String, Targetable> previousTargets = new HashMap<>();
        Targetable targetable1 = new DirectionTarget(newsandbox,map.getPosition(new Coord(2,2)),
                Direction.DOWN,true);
        previousTargets.put("Pietro",targetable1);
        testedAction = new Action(newsandbox, aT, previousTargets,finalizer);
        testedAction.iterate();
    }

    @Test
    void iterateManualTarget() {
        List<Tuple<String, TargeterTemplate>> targeters = new ArrayList<>();
        Selector selector1 = new HasSelector();
        List<Tuple<String, Condition>> filters = new ArrayList<>();
        TargeterTemplate tt1 = new TargeterTemplate(new Tuple<>("Pietro", selector1), filters,
                "tile", false, true,false);
        Tuple<String,TargeterTemplate> t1 = new Tuple<>("Pietro", tt1);
        targeters.add(t1);
        List<EffectTemplate> effects = new ArrayList<>();
        ActionTemplate aT = new ActionTemplate(mockInfo, targeters, effects);
        Map<String, Targetable> previousTargets = new HashMap<>();
        Targetable targetable1 = new DirectionTarget(newsandbox,map.getPosition(new Coord(2,2)),
                Direction.DOWN,true);
        previousTargets.put("Pietro",targetable1);
        testedAction = new Action(newsandbox, aT, previousTargets,finalizer);
        testedAction.iterate();
    }

    @Test
    void iterateManualTargetMandatory() {
    }

    @Test
    void iterateManualTargetOptional() {
    }
}