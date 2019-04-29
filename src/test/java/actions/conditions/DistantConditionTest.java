package actions.conditions;

import static org.mockito.Mockito.*;

import actions.targeters.interfaces.PointLike;
import actions.targeters.targets.RoomTarget;
import actions.targeters.targets.Targetable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import uid.RoomUID;
import uid.TileUID;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class DistantConditionTest {
    @ParameterizedTest
    @ValueSource(strings = { "true", "false" })
    void checkTargetValid(boolean positive) {
        Targetable checker = mock(PointLike.class);
        Targetable target = mock(Targetable.class);
        TileUID tile1 = new TileUID();
        TileUID tile2 = new TileUID();

        when(((PointLike)checker).distanceSelector(0,0,true)).thenReturn(Set.of(tile1));
        when(target.getSelectedTiles()).thenReturn(Set.of(tile1,tile2));

        Condition cond = new DistantCondition(0,0,true, !positive);
        assertEquals(positive, cond.checkTarget(target,checker));
    }

    @Test
    void checkTargetInvalid(){
        Targetable checker = mock(Targetable.class);
        Targetable target = mock(Targetable.class);
        Condition cond = new DistantCondition(0,0,true, true);
        assertThrows(IllegalArgumentException.class, ()->cond.checkTarget(target,checker));
    }

}