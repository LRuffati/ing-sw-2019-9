package actions.effects;

import actions.targeters.targets.Targetable;
import board.Sandbox;
import controllerresults.ControllerActionResult;
import uid.TileUID;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class GrabTemplate {
    ControllerActionResult spawn(Map<String, Targetable> targets, Sandbox sandbox, Function<Sandbox,
            ControllerActionResult> consumer){
        TileUID cell = targets.get("self").getSelectedTiles(sandbox).iterator().next();
        return consumer.apply(new Sandbox(sandbox, List.of(new GrabEffect(cell))));
    }
}
