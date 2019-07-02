package grabbables;

import actions.effects.*;
import actions.targeters.targets.Targetable;
import actions.utils.*;
import board.Sandbox;
import controller.SetMessageProxy;
import controller.SlaveController;
import controller.controllermessage.ControllerMessage;
import controller.controllermessage.PickTargetMessage;
import controller.controllermessage.WaitMessage;
import genericitems.Tuple;
import uid.DamageableUID;
import viewclasses.TargetView;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * This class implements the Targeting Scope powerUp.
 */
public class TargScope extends PowerUp {
    TargScope(AmmoColor color) {
        super(PowerUpType.TARGETINGSCOPE, color);
    }

    /**
     * Gets the list of effects which were played prior to this call and tells if I can use the
     * powerup
     *
     * @param lastEffects
     * @return true if the effect can be used, false otherwise
     */
    @Override
    public boolean canUse(List<Effect> lastEffects) {
        return lastEffects.stream()
                .anyMatch(effect -> effect.type().equals(EffectType.DAMAGE));
    }

    /**
     * Uses and discards the powerup
     *
     * @param proxy
     * @param lastEffects        effects prior to this powerup (used for targeting scope   )
     * @param onPowerupFinalized the function to call once the effects have been merged in Main
     * @return the controller message to show the use
     */
    @Override
    public ControllerMessage usePowup(SetMessageProxy proxy, List<Effect> lastEffects,
                                      Runnable onPowerupFinalized) {
        SlaveController pov = proxy.slave;
        List<DamageableUID> possibleTargets = lastEffects.stream()
                .filter(e->e.type().equals(EffectType.DAMAGE))
                .flatMap(effect -> effect.targetedPlayers().stream())
                .collect(Collectors.toList());

        Sandbox sandbox = pov.getSelf().getGm().createSandbox(pov.getSelf().pawnID());
        ChoiceMaker targPick = new ChoiceMaker() {
            @Override
            public void giveTargets(String targetId, List<TargetView> possibilities, Function<Integer, Targetable> action) {
                return;
            }

            @Override
            public Tuple<Boolean, List<TargetView>> showOptions() {
                return new Tuple<>(
                        true,
                        possibleTargets.stream()
                            .map(uid -> sandbox.getBasic(uid).generateView(sandbox))
                            .collect(Collectors.toList())
                );
            }

            @Override
            public ControllerMessage pick(int choice) {
                if (choice==-1){
                    if (proxy.setControllerMessage(new WaitMessage(List.of()))){
                        onPowerupFinalized.run();
                    }
                    return new WaitMessage(List.of());
                } else {
                    DamageableUID target = possibleTargets.get(choice);
                    Effect addDamage = new DamageEffect(target,1,true);
                    EffectTemplate payEffect = new PayTemplate(new WildCardAmmo());
                    return payEffect.spawn(
                            Map.of(),
                            new Sandbox(sandbox,List.of(addDamage)),
                            sandbox1 -> {
                                //Apply effects to gamemap and run onPowerupFinalized
                                if (proxy.setControllerMessage(new WaitMessage(List.of()))){
                                    pov.getSelf().discardPowerUp(TargScope.this);
                                    new Thread(()->pov.main.resolveEffect(pov,
                                        sandbox1.getEffectsHistory(), onPowerupFinalized)).start();
                                }

                                return new WaitMessage(List.of());
                            });
                }
            }
        };

        return new PickTargetMessage(targPick,"Scegli su chi fare un danno aggiuntivo", sandbox);
    }
}
