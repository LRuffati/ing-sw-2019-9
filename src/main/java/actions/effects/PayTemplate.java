package actions.effects;

import actions.targeters.targets.Targetable;
import actions.utils.AmmoAmountUncapped;
import board.Sandbox;
import grabbables.PowerUp;
import controller.ChoiceBoard;
import controller.controllermessage.ControllerMessage;
import controller.controllermessage.PickPowerupMessage;
import controller.controllermessage.RollbackMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PayTemplate implements EffectTemplate{

    private final AmmoAmountUncapped amount;

    public PayTemplate(AmmoAmountUncapped amount){
        this.amount = amount;
    }

    /**
     *
     * @param targets all targets previously acquired
     * @param sandbox the latest sandbox
     * @param consumer this action provides the information on what to do with the modified sandbox
     * @return a ControllerMessage letting you choose (if possible) which and how many powerups
     * to use to pay the debt
     */
    @Override
    public ControllerMessage spawn(Map<String, Targetable> targets, Sandbox sandbox, Function<Sandbox,
            ControllerMessage> consumer) {
        AmmoAmountUncapped totAv = sandbox.getUpdatedTotalAmmoAvailable();

        if (!totAv.canBuy(amount))
            return new RollbackMessage("Not enough ammo or powerups");

        List<PowerUp> useful = sandbox.powerUpsAvailable().stream()
                .filter(p -> amount.canBuy(p.getAmmo())) // this checks that the powerup has any use
                .collect(Collectors.toList());

        boolean optional = sandbox.getUpdatedAmmoAvailable().canBuy(amount); // If > 0 I can pay
        // with just cubes

        ChoiceBoard options = ChoiceBoard.powupChoiceFactory(useful,
                "Pick powerups to pay your debt of: "+amount.toString(),
                false, optional);

        Function<List<PowerUp>, ControllerMessage> onChoice =
                list -> {
                    AmmoAmountUncapped tempSum = AmmoAmountUncapped.zeroAmmo;
                    List<PowerUp> powUpsUsed= new ArrayList<>();
                    for (PowerUp p: list){
                        AmmoAmountUncapped intermediate =
                                tempSum.add(new AmmoAmountUncapped(p.getAmmo().getAmounts()));
                        if (amount.canBuy(intermediate)){ // If the total doesn't exceed the amount
                            tempSum = intermediate; // Add to the temp
                            powUpsUsed.add(p);
                        }
                    } // At the end of the cycle I have an amount compatible with the sum to pay

                    List<Effect> payEffect = powUpsUsed.stream().map(PayWithPowUpEffect::new)
                            .collect(Collectors.toList());

                    Sandbox newSandbox = new Sandbox(sandbox, payEffect);
                    AmmoAmountUncapped amountLeft = amount.subtract(tempSum);

                    boolean canPayWithCubes =
                            newSandbox.getUpdatedAmmoAvailable().canBuy(amountLeft);

                    if (tempSum.canBuy(amount)){ // If it's enough to pay just with powerups
                        return consumer.apply(newSandbox);
                    } else if (canPayWithCubes) {
                        Sandbox completedSandbox = new Sandbox(newSandbox,
                                List.of(new PayWithAmmoEffect(amountLeft)));
                        return consumer.apply(completedSandbox);
                    } else {
                        return new PayTemplate(amount.subtract(tempSum)).spawn(targets,
                                newSandbox,consumer);
                    }
                };

        if (useful.isEmpty())
            return onChoice.apply(List.of());

        return new PickPowerupMessage(useful, onChoice, optional, options);
    }
}
