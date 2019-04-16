package genericitems;

import actions.ActionInfo;

/**
 *
 * Tuple should be immutable or a deep copy should be implemented in
 * {@link ActionInfo#getActionRequirements()} and others
 *
 * @param <X>
 * @param <Y>
 */
public class Tuple<X, Y> {

    public final X x;
    public final Y y;

    public Tuple(X x, Y y){
        this.x = x;
        this.y = y;
    }
}
