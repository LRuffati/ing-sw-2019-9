package actions.effects;

import actions.targeters.targets.Targetable;
import actions.utils.AmmoAmount;
import actions.utils.AmmoAmountUncapped;
import board.Sandbox;
import grabbables.PowerUp;
import testcontroller.ChoiceBoard;
import testcontroller.Message;
import testcontroller.controllermessage.ControllerMessage;
import testcontroller.controllermessage.PickPowerupMessage;
import testcontroller.controllermessage.RollbackMessage;
import testcontroller.controllerstates.SlaveControllerState;
import viewclasses.GameMapView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PayTemplate implements EffectTemplate{

    private final AmmoAmountUncapped amount;

    public PayTemplate(AmmoAmountUncapped amount){
        this.amount = new AmmoAmountUncapped(amount.getAmounts());
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

        if (totAv.compareTo(amount)<0)
            return new RollbackMessage("Not enough ammo or powerups");

        List<PowerUp> useful = sandbox.powerUpsAvailable().stream()
                .filter(p -> amount.compareTo(new AmmoAmountUncapped(p.getAmmo().getAmounts()))>0) // this checks that the powerup has any use
                .collect(Collectors.toList());

        boolean optional = new AmmoAmountUncapped(sandbox.getUpdatedAmmoAvailable().getAmounts())
                .compareTo(amount) > 0; // If > 0 I can pay with just cubes

        ChoiceBoard options = ChoiceBoard.powupChoiceFactory(useful,
                "Pick powerups to pay your debt", //TODO: add the amount to pay and the cubes
                false, optional);

        Function<List<PowerUp>, ControllerMessage> onChoice =
                list -> {
                    AmmoAmountUncapped tempSum = new AmmoAmountUncapped();
                    List<PowerUp> powUpsUsed= new ArrayList<>();
                    for (PowerUp p: list){
                        AmmoAmountUncapped intermediate =
                                tempSum.add(new AmmoAmountUncapped(p.getAmmo().getAmounts()));
                        if (amount.compareTo(intermediate)>0){ // If the total doesn't exceed the amount
                            tempSum = intermediate; // Add to the temp
                            powUpsUsed.add(p);
                        }
                    } // At the end of the cycle I have an amount compatible with the sum to pay

                    List<Effect> payEffect = powUpsUsed.stream().map(PayWithPowUpEffect::new)
                            .collect(Collectors.toList());

                    Sandbox newSandbox = new Sandbox(sandbox, payEffect);
                    AmmoAmountUncapped amountLeft = amount.subtract(tempSum);

                    boolean canPayWithCubes =
                            new AmmoAmountUncapped(newSandbox.getUpdatedAmmoAvailable().getAmounts())
                            .compareTo(amountLeft) > 0;

                    if (tempSum.compareTo(amount)>0){ // If it's enough to pay just with powerups
                        return consumer.apply(newSandbox);
                    } else if (canPayWithCubes) {
                        Sandbox completedSandbox = new Sandbox(newSandbox,
                                List.of(new PayWithAmmoEffect(amountLeft)));
                        return consumer.apply(completedSandbox);
                    } else {
                        return new PayTemplate(amount.subtract(tempSum)).spawn(targets,
                                newSandbox,consumer);
                    }

                    // Check if these are enough to pay (either alone or topped up by the cubes)
                    // If they are just add the effects and call consumer with the sandbox
                    // else: subtract the powerups from the Amount, update the sandbox, create a
                    // new PayTemplate with the new amount and call .spawn on it
                };

        return new PickPowerupMessage(useful, onChoice, optional, options);
    }
}
