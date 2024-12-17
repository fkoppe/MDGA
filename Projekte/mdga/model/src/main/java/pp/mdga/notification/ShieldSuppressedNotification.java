package pp.mdga.notification;

import java.util.UUID;

/**
 * This class will be used to hold all ShieldSuppressedNotification relevant data.
 */
public class ShieldSuppressedNotification extends Notification {
    /**
     * The pieceId
     */
    private final UUID pieceId;

    /**
     * This constructor is used to create a new ShieldSuppressedNotification
     *
     * @param pieceId the pieceId
     */
    public ShieldSuppressedNotification(UUID pieceId) {
        this.pieceId = pieceId;
    }

    /**
     * This method returns the pieceId
     *
     * @return the pieceId
     */
    public UUID getPieceId() {
        return pieceId;
    }
}
