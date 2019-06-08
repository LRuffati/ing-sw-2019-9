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
            return new ControllerActionResultServer(ActionResultType.TERMINATED, "", newsandbox);
                };
        mockInfo = mock(ActionInfo.class);
    }

    @Test
    void iterateAutomaticTarget(){
        assert false;
    }

    @Test
    void iterateManualTarget() {
        assert false;
    }

    @Test
    void iterateManualTargetMandatory() {
        assert false;
    }

    @Test
    void iterateManualTargetOptional() {
        assert false;
    }
}