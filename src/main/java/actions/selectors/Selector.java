package actions.selectors;

import actions.targeters.targets.Targetable;
import board.Sandbox;

import java.security.InvalidParameterException;
import java.util.Collection;
import java.util.Map;

interface Selector {
    default Collection<Targetable> select(Targetable sourceTarget, Sandbox sandbox) {
        throw new InvalidParameterException("Targeter not compatible with action");
    }
    default Collection<Targetable> select(Map<String, Targetable> validatedTargets, Sandbox sandbox) {
        throw new InvalidParameterException("Targeter not compatible with action");
    }
}