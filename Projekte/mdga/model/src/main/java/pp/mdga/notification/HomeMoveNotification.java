package pp.mdga.notification;

import java.util.UUID;

/**
 * Notification that a piece has moved to a home.
 */
public class HomeMoveNotification extends Notification {
    /**
     * The unique identifier of the piece.
     */
    private final UUID pieceId;

    /**
     * The index of the home position.
     */
    private final int homeIndex;

    /**
     * Constructor.
     *
     * @param pieceId   the piece id
     * @param homeIndex the home index
     */
    public HomeMoveNotification(UUID pieceId, int homeIndex) {
        this.pieceId = pieceId;
        this.homeIndex = homeIndex;
    }

    /**
     * Get the piece id.
     *
     * @return the piece id
     */
    public UUID getPieceId() {
        return pieceId;
    }

    /**
     * Get the home index.
     *
     * @return the home index
     */
    public int getHomeIndex() {
        return homeIndex;
    }
}
