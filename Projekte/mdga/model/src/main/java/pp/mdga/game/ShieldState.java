package pp.mdga.game;

/**
 * Represents the state of a piece's shield.
 */
public enum ShieldState {
    /**
     * The shield is not active.
     */
    NONE,
    /**
     * The shield is active.
     */
    ACTIVE,
    /**
     * The shield is suppressed, when the piece is on a start node.
     */
    SUPPRESSED
}
