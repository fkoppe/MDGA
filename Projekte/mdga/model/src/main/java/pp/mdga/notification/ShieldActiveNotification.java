package pp.mdga.notification;

import java.util.UUID;

/**
 * This class will be used to hold all ShieldActiveNotification relevant data.
 */
public class ShieldActiveNotification extends Notification {
    /**
     * The pieceId
     */
    private final UUID pieceId;

    /**
     * This constructor is used to create a new ShieldActiveNotification
     *
     * @param pieceId the pieceId
     */
    public ShieldActiveNotification(UUID pieceId) {
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
