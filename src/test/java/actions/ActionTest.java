package actions;

import actions.selectors.VisibleSelector;
import actions.targeters.TargeterTemplate;
import actions.targeters.targets.Targetable;
import actions.utils.AmmoAmount;
import actions.utils.AmmoAmountUncapped;
import board.GameMap;
import board.Sandbox;
import controllerresults.ActionResultType;
import controllerresults.ControllerActionResult;
import genericitems.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import player.Actor;
import player.Pawn;
import uid.DamageableUID;
import uid.TileUID;

import java.util.Map;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ActionTest {
    Action testedAction;
    Sandbox oldsandbox;
    Sandbox newsandbox;
    Function<Tuple<Sandbox, Map<String, Targetable>>, ControllerActionResult> finalizer;
    ActionInfo mockInfo;
    @BeforeEach
    void setup(){
        newsandbox = mock(Sandbox.class);
        finalizer = sandboxMapTuple -> {
            return new ControllerActionResult(ActionResultType.TERMINATED);
                };
        mockInfo = mock(ActionInfo.class);
    }

    @Test
    void iterateAutomaticTarget() { //Write a manual

    }

    @Test
    void iterateManualTarget() {
    }

    @Test
    void iterateManualTargetMandatory() {
    }

    @Test
    void iterateManualTargetOptional() {
    }
}