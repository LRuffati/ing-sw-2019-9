package actions.effects;

import actions.targeters.targets.Targetable;
import actions.utils.AmmoAmount;
import actions.utils.AmmoAmountUncapped;
import board.Sandbox;
import controller.ChoiceBoard;
import controller.controllermessage.ControllerMessage;
import controller.controllermessage.PickPowerupMessage;
import controller.controllermessage.RollbackMessage;
import controller.controllermessage.StringChoiceMessage;
import genericitems.Tuple;
import grabbables.PowerUp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * This template allows to pay the "cube of any color" required by the targeting scope
 */
public class PayWildCardTemp {
    public PayWildCardTemp(){
    }

    public ControllerMessage spawn(Map<String, Targetable> targets, Sandbox sandbox, Function<Sandbox,
                ControllerMessage> consumer) {
        AmmoAmountUncapped totAv = sandbox.getUpdatedTotalAmmoAvailable();
        if (AmmoAmountUncapped.zeroAmmo.canBuy(totAv))
            return new RollbackMessage("You don't have enough Ammo");

        boolean optional =
                !AmmoAmountUncapped.zeroAmmo.canBuy(sandbox.getUpdatedAmmoAvailable());

        List<PowerUp> powerUps = sandbox.powerUpsAvailable();

        ChoiceBoard options = ChoiceBoard.powupChoiceFactory(powerUps,
                "Pick powerup to pay",
                false, optional);

        Function<List<PowerUp>, ControllerMessage> onChoice =
                list -> {
                    List<PowerUp> powUpsUsed = new ArrayList<>();
                    if (!list.isEmpty())
                        powUpsUsed.add(list.get(0));
                    if (list.isEmpty() && AmmoAmountUncapped.zeroAmmo.canBuy(sandbox.getUpdatedAmmoAvailable()))
                        return new RollbackMessage("Non hai munizioni, devi usare i powerup");

                    Effect effect;
                    Sandbox newSandbox;
                    if (!powUpsUsed.isEmpty()){
                        effect= new PayWithPowUpEffect(list.get(0));
                        newSandbox = new Sandbox(sandbox, List.of(effect));
                        return consumer.apply(newSandbox);
                    } else {
                        AmmoAmount avail = sandbox.getUpdatedAmmoAvailable();
                        List<Tuple<String, AmmoAmount>> optionsStrings =
                                List.of(
                                        new Tuple<>("Paga un rosso", AmmoAmount.redSingleton),
                                        new Tuple<>("Paga un blu", AmmoAmount.blueSingleton),
                                        new Tuple<>("Paga un giallo", AmmoAmount.yellowSingleton)
                                );
                        final List<Tuple<String, AmmoAmount>> optionsStringsFin =
                                optionsStrings.stream()
                                .filter(tup -> avail.canBuy(tup.y))
                                .collect(Collectors.toList());

                        return new StringChoiceMessage(
                                optionsStringsFin.stream().map(i->i.x).collect(Collectors.toList()),
                                "Scegli che colore pagare",
                                integer -> {
                                    Effect payeffect =
                                            new PayWithAmmoEffect(new ArrayList<>(optionsStringsFin)
                                                    .get(integer).y);
                                    Sandbox newSand = new Sandbox(sandbox, List.of(payeffect));
                                    return consumer.apply(newSand);
                                },
                                sandbox
                        );
                    }
                };

        if (powerUps.isEmpty()){
            return onChoice.apply(List.of());
        } else {
            return new PickPowerupMessage(powerUps, onChoice, true, options);
        }
    }
}
