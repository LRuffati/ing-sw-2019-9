package actions.effects;

import actions.targeters.targets.Targetable;
import board.Sandbox;
import testcontroller.controllermessage.ControllerMessage;
import testcontroller.controllermessage.RollbackMessage;
import uid.TileUID;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class GrabTemplate implements EffectTemplate{

    public ControllerMessage spawn(
            Map<String, Targetable> targets,
            Sandbox sandbox,
            Function<Sandbox,ControllerMessage> consumer
            ){
        TileUID cell = targets.get("self").getSelectedTiles(sandbox).iterator().next();
        /* TODO:
            1. Check if the tile is a spawn point or tile
            2. If spawn point return a weapon chooser (filtered by ammoamount available)
                2a. If not enough free slots return a weapon chooser on old weapons
            3. Create the appropriate effect
         */
        if (!sandbox.canGrab())
            return new RollbackMessage("You can't grab anything in this location");

        return consumer.apply(new Sandbox(sandbox, List.of(new GrabEffect())));

    }
}
