package pp.mdga.notification;

import java.util.List;
import java.util.UUID;

public class SelectableShieldNotification extends Notification {

    /**
     * The list of piece IDs associated with the player.
     */
    private final List<UUID> pieces;

    /**
     * Constructor.
     *
     * @param pieces the list of piece IDs associated with the player.
     */
    public SelectableShieldNotification(List<UUID> pieces) {
        this.pieces = pieces;
    }

    /**
     * Get the list of piece IDs associated with the player.
     *
     * @return the list of piece IDs associated with the player.
     */
    public List<UUID> getPieces() {
        return pieces;
    }
}
