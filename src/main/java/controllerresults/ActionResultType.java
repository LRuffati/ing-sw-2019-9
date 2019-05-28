package controllerresults;

import java.io.Serializable;

public enum ActionResultType implements Serializable {
    PICKTARGET,
    PICKACTION,
    PICKWEAPON,
    ROLLBACK,
    TERMINATED,
    ALREADYTERMINATED,
    REDO
}
