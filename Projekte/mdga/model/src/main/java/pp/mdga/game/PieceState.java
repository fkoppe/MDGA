package pp.mdga.game;

import com.jme3.network.serializing.Serializable;

/**
 * Represents the state of a piece.
 */
@Serializable
public enum PieceState {
    /**
     * The piece is active.
     */
    ACTIVE,
    /**
     * The piece is waiting.
     */
    WAITING,
    /**
     * The piece is in the home.
     */
    HOME,
    /**
     * The piece is finished.
     */
    HOMEFINISHED
}
