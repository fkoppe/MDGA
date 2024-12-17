package pp.mdga.notification;

import java.util.UUID;

/**
 * Notification that two pieces have been swapped.
 */
public class SwapPieceNotification extends Notification {
    /**
     * The UUID of the first piece that has been swapped.
     */
    private final UUID firstPiece;

    /**
     * The UUID of the second piece that has been swapped.
     */
    private final UUID secondPiece;

    /**
     * Constructor.
     *
     * @param firstPiece  the UUID of the first piece that has been swapped.
     * @param secondPiece the UUID of the second piece that has been swapped.
     */
    public SwapPieceNotification(UUID firstPiece, UUID secondPiece) {
        assert (!firstPiece.equals(secondPiece));
        this.firstPiece = firstPiece;
        this.secondPiece = secondPiece;
    }

    /**
     * Get the UUID of the first piece that has been swapped.
     *
     * @return the UUID of the first piece that has been swapped.
     */
    public UUID getFirstPiece() {
        return firstPiece;
    }

    /**
     * Get the UUID of the second piece that has been swapped.
     *
     * @return the UUID of the second piece that has been swapped.
     */
    public UUID getSecondPiece() {
        return secondPiece;
    }
}
