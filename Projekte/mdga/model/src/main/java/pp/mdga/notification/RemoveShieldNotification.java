package pp.mdga.notification;

import java.util.UUID;

public class RemoveShieldNotification extends Notification {

    /**
     * The UUID of the piece that has had its shield removed
     */
    private final UUID pieceUuid;

    /**
     * Constructor.
     *
     * @param pieceUuid the UUID of the piece that has had its shield removed
     */
    public RemoveShieldNotification(UUID pieceUuid) {
        this.pieceUuid = pieceUuid;
    }

    /**
     * @return the UUID of the piece that has had its shield removed
     */
    public UUID getPieceUuid() {
        return pieceUuid;
    }
}
