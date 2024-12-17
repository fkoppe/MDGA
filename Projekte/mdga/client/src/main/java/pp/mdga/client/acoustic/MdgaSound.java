package pp.mdga.client.acoustic;

/**
 * Enum representing the various sound effects used in the game.
 * Each sound corresponds to an event or action in the game and may consist of one or more
 * audio files, potentially with time delays between them.
 * <p>
 * These sounds are used to play specific audio cues, such as when a dice is rolled,
 * a turn starts or ends, a piece is moved or lost, and various other events in the game.
 */
public enum MdgaSound {
    DICE_ROLL,
    TURN_START,
    TURN_END,
    PIECE_END,
    PIECE_MOVE,
    PIECE_LOST,
    SELECT,
    DESELECT,
    HURRY,
    VICTORY,
    LOST,
    BUTTON_PRESSED,
    WRONG_INPUT,
    UI_CLICK,
    START,
    THROW,
    POWERUP,
    SELF_READY,
    OTHER_READY,
    OTHER_CONNECTED,
    NOT_READY,
    LEAVE,
    JET,
    EXPLOSION,
    LOSE,
    BONUS,
    UI90,
    MISSILE,
    MATRIX,
    TURRET_ROTATE,
    TANK_SHOOT,
    TANK_EXPLOSION,
    SHIELD,
    TURBO,
    SWAP,
}
