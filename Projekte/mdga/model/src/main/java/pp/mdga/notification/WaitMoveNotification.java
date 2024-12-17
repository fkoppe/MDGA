package pp.mdga.notification;

import java.util.UUID;

/**
 * Notification to inform the player that he has to wait for the other player to move.
 */
public class WaitMoveNotification extends Notification {
    /**
     * The id of the piece that has to move.
     */
    private final UUID pieceId;

    /**
     * Constructor.
     *
     * @param pieceId the id of the piece that has to move.
     */
    public WaitMoveNotification(UUID pieceId) {
        this.pieceId = pieceId;
    }

    /**
     * Get the id of the piece that has to move.
     *
     * @return the id of the piece that has to move.
     */
    public UUID getPieceId() {
        return pieceId;
    }
}
