package actions;

import static org.mockito.Mockito.*;


import actions.effects.Effect;
import actions.effects.EffectTemplate;
import actions.effects.Fire;
import actions.targeters.targets.BasicTarget;
import actions.targeters.targets.Targetable;
import actions.utils.ActionPicker;
import actions.utils.AmmoAmount;
import board.GameMap;
import board.Sandbox;
import controllerresults.ActionResultType;
import controllerresults.ControllerActionResult;
import genericitems.Tuple;
import grabbables.Weapon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import uid.DamageableUID;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

class ActionBundleTest{

    GameMap map;
    DamageableUID pov;
    Sandbox sandboxOriginal;
    Sandbox newSandbox;
    BasicTarget selfTarget;
    ActionTemplate temp;
    Weapon weapon1;
    Weapon weapon2;
    Effect mockeff;
    @BeforeEach
    void setup(){
        map = mock(GameMap.class);
        pov = new DamageableUID();
        sandboxOriginal = mock(Sandbox.class);
        newSandbox = mock(Sandbox.class);
        ActionInfo info = mock(ActionInfo.class);
        BasicTarget selfTarget = mock(BasicTarget.class);

        weapon1 = mock(Weapon.class);
        weapon2 = mock(Weapon.class);

        when(sandboxOriginal.getBasic(pov)).thenReturn(selfTarget);
        when(map.createSandbox(pov)).thenReturn(sandboxOriginal);
        when(sandboxOriginal.getArsenal()).thenReturn(List.of(new Tuple<>(false, weapon1),
                new Tuple<>(true, weapon2)));

        mockeff = mock(Effect.class);
        when(sandboxOriginal.getEffectsHistory()).thenReturn(List.of(mockeff));
        when(newSandbox.getEffectsHistory()).thenReturn(List.of(mockeff,mockeff));

        EffectTemplate fireEff = new Fire();
        temp = new ActionTemplate(info, List.of(), List.of(fireEff));

    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 1})
    void testOutOfBound(int choice){
        ActionBundle testTarget = new ActionBundle(map, List.of(temp), pov);
        ControllerActionResult res = testTarget.pickAction(choice);

        assertEquals(res.actionPicker, testTarget);
    }

    @Test
    void testFireType(){
        ActionBundle testTarget = new ActionBundle(map, List.of(temp), pov);
        ControllerActionResult res = testTarget.pickAction(0);
        assertEquals(res.type, ActionResultType.PICKWEAPON);
    }

    @Test
    void testFireOptions(){
        ActionBundle testTarget = new ActionBundle(map, List.of(temp), pov);
        ControllerActionResult res = testTarget.pickAction(0);
        assertEquals(res.weaponChooser.showOptions(), List.of(weapon2));
    }

    @Test
    void testFinalizeType(){
        ActionInfo info = mock(ActionInfo.class);
        temp = new ActionTemplate(info, List.of(), List.of());
        ActionBundle testTarget = new ActionBundle(map, List.of(temp), pov);

        assertEquals(testTarget.pickAction(0).type,
                ActionResultType.TERMINATED);
    }

    @Test
    void testdoubleFinalize(){
        ActionInfo info = mock(ActionInfo.class);
        temp = new ActionTemplate(info, List.of(), List.of());
        ActionBundle testTarget = new ActionBundle(map, List.of(temp), pov);
        testTarget.pickAction(0);
        assertEquals(testTarget.pickAction(0).type, ActionResultType.ALREADYTERMINATED);
    }

    @Test
    void testFinalizeEffects(){
        ActionInfo info = mock(ActionInfo.class);
        temp = new ActionTemplate(info, List.of(), List.of());
        ActionBundle testTarget = new ActionBundle(map, List.of(temp), pov);
        testTarget.pickAction(0);
        assertEquals(testTarget.getEffects().get(0), mockeff);
    }

    @Test
    void testApplyEffectFinalize(){ // Test that the sandbox in the finalizer is the newly
        // created one
        ActionInfo info = mock(ActionInfo.class);
        EffectTemplate eff = new EffectTemplate() {
            @Override
            public ControllerActionResult spawn(Map<String, Targetable> targets, Sandbox sandbox, Function<Sandbox, ControllerActionResult> consumer) {
                return consumer.apply(newSandbox);
            }
        };
        ActionTemplate template = new ActionTemplate(info, List.of(), List.of(eff));
        ActionBundle test = new ActionBundle(map, List.of(template), pov);
        test.pickAction(0);
        assertEquals(test.getEffects(), List.of(mockeff,mockeff)); // The sandbox has been
                                                                    // substituted correctly

    }
}