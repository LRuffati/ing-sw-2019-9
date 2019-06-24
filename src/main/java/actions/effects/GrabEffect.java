package actions.effects;


import actions.utils.AmmoAmount;
import board.Tile;
import grabbables.AmmoCard;
import grabbables.Grabbable;
import grabbables.Weapon;
import controller.SlaveController;


import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class GrabEffect implements Effect {
    public final EffectType type;

    public GrabEffect() {
        this.type = EffectType.GRAB;
    }

    @Override
    public EffectType type() {
        return EffectType.GRAB;
    }

    @Override
    public void mergeInGameMap(SlaveController pov, Runnable finalize,
                               Consumer<String> broadcaster) {


        // Choose actual operations
        Tile tile = pov.getSelf().getGm().getTile(pov.getSelf().pawn().getTile());

        Set<Grabbable> grabbables = tile.getGrabbable();

        AmmoAmount amountsToGrab = grabbables.stream()
                .flatMap(i-> Stream.of(i.getAmmoAmount()))
                .reduce(new AmmoAmount(), (tot, amm)->new AmmoAmount(tot.add(amm)));

        int powerups = grabbables.stream()
                .flatMap(i->Stream.of(i.getNumOfPowerUp()))
                .reduce(0, Integer::sum);

        Set<Weapon> weapons = grabbables.stream()
                .map(Grabbable::getWeapon)
                .flatMap(Set::stream)
                .collect(Collectors.toSet());

        BiConsumer<Weapon, Optional<Weapon>> onChoice = (toGrab, toDiscard) -> {
            String broadcast = String.format("%s", pov.getSelf().pawn().getUsername());
            broadcast = broadcast.concat(String.format(" ha raccolto %s", toGrab.getName()));

            if (toDiscard.isPresent()) {
                broadcast = broadcast.concat(String.format(" e lasciato %s", toDiscard.get().getName()));
            }
            pov.getSelf().pickUp(toGrab, toDiscard);
            broadcaster.accept(broadcast);
            finalize.run();
        };

        if (new AmmoAmount().compareTo(amountsToGrab)<0){ //If I have to grab some amount of cubes
            broadcaster.accept(String.format("%s ha raccolto %s",
                    pov.getSelf().pawn().getUsername(), amountsToGrab.toString()));
        }

        powerups = Math.min(powerups, 3-pov.getSelf().getPowerUp().size());
        if (powerups>0){
            broadcaster.accept(String.format("%s ha raccolto %d powerup",
                    pov.getSelf().pawn().getUsername(), powerups));
        }

        if (!weapons.isEmpty()){
            pov.makeGrabChoice(weapons, onChoice);
        } else {
            Set<AmmoCard> cardsToDiscard = grabbables.stream().flatMap(i->i.getCard().stream())
                    .collect(Collectors.toSet());
            for (AmmoCard i: cardsToDiscard){
                pov.getSelf().pickUp(i);
            }
            finalize.run();
        }
    }
}
