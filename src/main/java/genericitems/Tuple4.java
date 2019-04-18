package genericitems;

import actions.ActionInfo;

/**
 *
 * Tuple should be immutable or a deep copy should be implemented in
 * {@link ActionInfo#getActionRequirements()} and others
 *
 * @param <X>
 * @param <Y>
 * @param <Z>
 * @param <T>
 */
public class Tuple4<X, Y, Z, T> {

    public final X x;
    public final Y y;
    public final Z z;
    public final T t;

    public Tuple4(X x, Y y, Z z, T t){
        this.x = x;
        this.y = y;
        this.z = z;
        this.t = t;
    }
}
