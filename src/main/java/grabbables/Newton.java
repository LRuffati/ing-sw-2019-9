package grabbables;

import actions.Action;
import actions.ActionInfo;
import actions.ActionTemplate;
import actions.conditions.HasCondition;
import actions.effects.Effect;
import actions.effects.MoveTemplate;
import actions.selectors.ExistSelector;
import actions.selectors.HasSelector;
import actions.selectors.ReachableSelector;
import actions.targeters.TargeterTemplate;
import actions.targeters.targets.BasicTarget;
import actions.utils.AmmoAmount;
import actions.utils.AmmoAmountUncapped;
import actions.utils.AmmoColor;
import actions.utils.PowerUpType;
import board.Sandbox;
import controller.SetMessageProxy;
import controller.SlaveController;
import controller.controllermessage.ControllerMessage;
import controller.controllermessage.WaitMessage;
import genericitems.Tuple;
import player.Actor;
import uid.DamageableUID;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * This class implements the Newton powerUp.
 */
public class Newton extends PowerUp {
    Newton(AmmoColor color){
        super(PowerUpType.NEWTON, color);
    }

    @Override
    public boolean canUse(List<Effect> lastEffects) {
        return true;
    }

    @Override
    public ControllerMessage usePowup(SetMessageProxy proxy, List<Effect> lastEffects,
                                      Runnable onPowerupFinalized) {
        SlaveController povC = proxy.slave;
        Actor pov = povC.getSelf();
        Sandbox sandbox = pov.getGm().createSandbox(pov.pawnID());
        DamageableUID selfU = pov.pawnID();
        BasicTarget selfT = sandbox.getBasic(selfU);

        ActionTemplate newton = new ActionTemplate(
                new ActionInfo(
                        "Newton action",
                        "newton",
                        "",
                        new AmmoAmount(AmmoAmountUncapped.zeroAmmo),
                        List.of(),
                        List.of(),
                        Optional.empty(),
                        true
                ),
                List.of(
                        new Tuple<>(
                                "targ",
                                new TargeterTemplate(
                                        new Tuple<>("self", new ExistSelector()),
                                        List.of(),
                                        "pawn",
                                        false,
                                        true,
                                        false,
                                        "Scegli il bersaglio da spostare"
                                )
                        ),
                        new Tuple<>(
                                "dest",
                                new TargeterTemplate(
                                        new Tuple<>("targ", new ReachableSelector(1,2)),
                                        List.of(),
                                        "tile",
                                        false,
                                        false,
                                        false,
                                        "Scegli la cella in cui spostare il bersaglio (direzioni " +
                                                "cardinali)"
                                )
                        ),
                        new Tuple<>(
                                "check",
                                new TargeterTemplate(
                                        new Tuple<>("targ", new HasSelector()),
                                        List.of(new Tuple<>("dest", new HasCondition(false))),
                                        "direction",
                                        false,
                                        false,
                                        true,
                                        "Controllo automatico"
                                )
                        )
                ),
                List.of(
                        new MoveTemplate("targ", "dest")
                )
        );

        Action newtonAction = newton.generate(sandbox, Map.of("self", selfT), sandboxMapTuple -> {
            List<Effect> effects = sandboxMapTuple.x.getEffectsHistory();
            if (proxy.setControllerMessage(new WaitMessage(List.of()))){
                    new Thread(() -> {
                        pov.discardPowerUp(Newton.this);
                        povC.main.resolveEffect(povC, effects, onPowerupFinalized);
                    }).start();
                }

                return new WaitMessage(List.of());
        });
        return newtonAction.iterate();
    }
}
