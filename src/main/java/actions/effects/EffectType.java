package actions.effects;

/**
 * This enum contains all the Effects that can be achieved during the game
 */
public enum EffectType {
    MOVE,
    MARK,
    DAMAGE,
    DAMAGERAW,
    FIRE, // Makes weapon unavailable
    RELOAD, // Makes weapon available
    DISCARD,
    PAY,
    GRAB
}
