package grabbables;

import actions.effects.Effect;
import actions.effects.MoveEffect;
import actions.selectors.ExistSelector;
import actions.selectors.Selector;
import actions.targeters.targets.BasicTarget;
import actions.targeters.targets.Targetable;
import actions.utils.AmmoColor;
import actions.utils.ChoiceMaker;
import actions.utils.PowerUpType;
import board.Sandbox;
import genericitems.Tuple;
import player.Actor;
import controller.SlaveController;
import controller.controllermessage.ControllerMessage;
import controller.controllermessage.PickTargetMessage;
import controller.controllermessage.WaitMessage;
import uid.TileUID;
import viewclasses.TargetView;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class implements the Teleporter powerUp.
 */
public class Teleporter extends PowerUp {
    Teleporter(AmmoColor color) {
        super(PowerUpType.TELEPORTER, color);
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
        return true;
    }

    /**
     * @param povC               the controller using the powerup (if this not in pov.powerups the
     *                           onPowerupFinalized)
     * @param lastEffects        effects prior to this powerup (used for targeting scope)
     * @param onPowerupFinalized the function to call once the effects have been merged in Main
     * @return the controller message to show the user
     */
    @Override
    public ControllerMessage usePowup(SlaveController povC, List<Effect> lastEffects,
                                      Runnable onPowerupFinalized) {
        Actor pov = povC.getSelf();
        Sandbox sandbox = pov.getGm().createSandbox(pov.pawnID());
        BasicTarget povT = sandbox.getBasic(pov.pawnID());
        Selector alls = new ExistSelector();

        if (!pov.getPowerUp().contains(this)) {
            onPowerupFinalized.run();
            return new WaitMessage(List.of());
        }

        List<Targetable> targs = new ArrayList<>(alls.select(sandbox, povT,
                tileUID -> Stream.of(sandbox.getTile(tileUID))));

        ChoiceMaker choiceMaker = new ChoiceMaker() {
            @Override
            public void giveTargets(String targetId, List<TargetView> possibilities, Function<Integer, Targetable> action) {
                return;
            }

            @Override
            public Tuple<Boolean, List<TargetView>> showOptions() {
                return new Tuple<>(false, targs.stream().map(i-> i.generateView(sandbox)).collect(Collectors.toList()));
            }

            @Override
            public ControllerMessage pick(int choice) {
                povC.setCurrentMessage(new WaitMessage(List.of()));
                TileUID destination = targs.get(choice).getSelectedTiles(sandbox).iterator().next();
                Effect effect = new MoveEffect(pov.pawnID(), destination);
                new Thread(() -> {
                    pov.discardPowerUp(Teleporter.this);
                    povC.main.resolveEffect(povC, List.of(effect), onPowerupFinalized);
                }).start();
                return new WaitMessage(List.of());
            }
        };
        return new PickTargetMessage(choiceMaker, "Scegli dove spostarti", sandbox);
    }
}
