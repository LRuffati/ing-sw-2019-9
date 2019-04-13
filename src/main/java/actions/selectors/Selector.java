package actions.selectors;

import actions.targeters.targets.Targetable;
import board.Sandbox;

import java.security.InvalidParameterException;
import java.util.Collection;

interface Selector {
    default Collection<Targetable> select(Targetable sourceTarget) {
        throw new InvalidParameterException("Targeter not compatible with action");
    }
}