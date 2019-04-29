package actions.conditions;

import actions.targeters.interfaces.PointLike;
import actions.targeters.interfaces.SuperTile;
import actions.targeters.targets.*;
import genericitems.Tuple;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import uid.TileUID;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HasConditionTest {

    @ParameterizedTest
    @ValueSource(strings = { "true", "false" })
    void checkTarget(boolean negated) {
        Targetable checker = mock(PointLike.class);
        Targetable target = mock(SuperTile.class);
        TileUID tile1 = new TileUID();
        TileUID tile2 = new TileUID();

        when(((SuperTile)target).containedTiles()).thenReturn(Set.of(tile1,tile2));
        when(((PointLike)checker).location()).thenReturn(tile1);

        Condition cond = new HasCondition(negated);
        //The condition is made so it's always true, I'm testing the negation and the type checking
        assertEquals(!negated, cond.checkTarget(target,checker));
    }

    static Stream<Tuple<Targetable,Targetable>> generator(){
        List<Targetable> targets = List.of(mock(BasicTarget.class),
                mock(DominationPointTarget.class),
                mock(GroupTarget.class));
        List<Targetable> checkers = List.of(mock(DirectionTarget.class), mock(RoomTarget.class),
                mock(GroupTarget.class));

        List<Tuple<Targetable, Targetable>> ret = new ArrayList<>();
        for (Targetable t: targets){
            for (Targetable c: checkers){
                ret.add(new Tuple<>(t,c));
            }
        }
        return ret.stream();
    }

    @ParameterizedTest
    @MethodSource("generator")
    void checkTargetInvalid(Tuple<Targetable, Targetable> tup){
        Targetable checker = tup.y;
        Targetable target = tup.x;
        Condition cond = new HasCondition(true);
        assertThrows(IllegalArgumentException.class, ()->cond.checkTarget(target,checker));
    }
}