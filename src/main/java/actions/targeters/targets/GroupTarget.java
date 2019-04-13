package actions.targeters.targets;

import board.Sandbox;
import uid.DamageableUID;
import uid.TileUID;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

public class GroupTarget implements Targetable {
    private HashSet<DamageableUID> targets;
    private final Sandbox sandbox;

    public GroupTarget(Sandbox sandbox, Collection<DamageableUID> players){
        targets = new HashSet<>(players);
        this.sandbox = sandbox;
    }

    @Override
    public Set<DamageableUID> getSelectedPawns() {
        return new HashSet<>(targets);
    }

    @Override
    public Set<TileUID> getSelectedTiles() {
        Set<TileUID> ret = new HashSet<>();
        for (DamageableUID i: targets){
            ret.add(sandbox.tile(i));
        }
        return ret;
    }

    /**
     * @return the sandbox containing the target
     */
    @Override
    public Sandbox getSandbox() {
        return sandbox;
    }

}
