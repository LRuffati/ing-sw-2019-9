package genericitems;

import actions.ActionInfo;

import java.io.Serializable;

/**
 *
 * Tuple should be immutable or a deep copy should be implemented in
 * {@link ActionInfo#getActionRequirements()} and others
 *
 * @param <X>
 * @param <Y>
 * @param <Z>
 */
public class Tuple3<X, Y, Z> implements Serializable {

    public final X x;
    public final Y y;
    public final Z z;

    public Tuple3(X x, Y y, Z z){
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
