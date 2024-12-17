package pp.mdga.notification;

import pp.mdga.game.Color;

import java.util.UUID;

/**
 * Notification that is sent when a piece is thrown.
 */
public class ThrowPieceNotification extends Notification {
    /**
     * The id of the piece that was thrown.
     */
    private final UUID pieceId;
    private final Color throwColor;

    /**
     * This constructor is used to create a new ThrowPieceNotification
     */
    public ThrowPieceNotification(UUID pieceId, Color throwColor) {
        this.pieceId = pieceId;
        this.throwColor = throwColor;
    }

    /**
     * Get the id of the piece that was thrown.
     *
     * @return The id of the piece that was thrown.
     */
    public UUID getPieceId() {
        return pieceId;
    }

    public Color getThrowColor() {
        return throwColor;
    }
}
