package actions.effects;


import actions.utils.AmmoAmount;
import board.Tile;
import grabbables.AmmoCard;
import grabbables.Grabbable;
import grabbables.Weapon;
import player.Actor;
import testcontroller.SlaveController;
import uid.TileUID;


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

        AmmoAmount amountsToGrab = tile.getGrabbable().stream()
                .flatMap(i-> Stream.of(i.getAmmoAmount()))
                .reduce(new AmmoAmount(), (tot, amm)->new AmmoAmount(tot.add(amm)));

        int powerups = tile.getGrabbable().stream()
                .flatMap(i->Stream.of(i.getNumOfPowerUp()))
                .reduce(0, Integer::sum);

        Set<Weapon> weapons = tile.getGrabbable().stream()
                .map(Grabbable::getWeapon)
                .flatMap(Set::stream)
                .collect(Collectors.toSet());

        BiConsumer<Weapon, Optional<Weapon>> onChoice = (toGrab, toDiscard) -> {
            String broadcast = String.format("%s", pov.getSelf().pawn().getUsername());

            //TODO: move weapon toGrab from the tile to the actor
            broadcast = broadcast.concat(String.format(" ha raccolto %s", toGrab.getName()));

            if (toDiscard.isPresent()) {
                Weapon toDisc = toDiscard.get();
                //TODO: move weapon toDiscard from the Actor to the tile
                broadcast = broadcast.concat(String.format(" e lasciato %s", toDisc.getName()));
            }
            broadcaster.accept(broadcast);
            finalize.run();
        };

        //TODO: discard the AmmoCard if present and change actor loadout, use gamemap methods to
        // ensure proper deck usage. Then broadcast the powerup and ammo changes. If weapons is
        // not empty start the grabWeapon in slaveController, else run finalize
        if (new AmmoAmount().compareTo(amountsToGrab)<0){ //If I have to grab some amount of cubes
            broadcaster.accept(String.format("%s ha raccolto %s",
                    pov.getSelf().pawn().getUsername(), amountsToGrab.toString()));
            // TODO: add to actor
        }

        powerups = Math.min(powerups, 3-pov.getSelf().getPowerUp().size())
        if (powerups>0){
            broadcaster.accept(String.format("%s ha raccolto %d powerup",
                    pov.getSelf().pawn().getUsername(), powerups));
            // TODO: Add powerups powerup to actor
        }

        if (!weapons.isEmpty()){
            pov.makeGrabChoice(weapons, onChoice);
        } else {
            finalize.run();
        }
    }
}
