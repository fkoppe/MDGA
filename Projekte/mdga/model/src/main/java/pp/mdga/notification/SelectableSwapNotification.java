package pp.mdga.notification;

import java.util.List;
import java.util.UUID;

/**
 * Notification for selecting pieces for swapcard.
 */
public class SelectableSwapNotification extends Notification {
    /**
     * List of UUIDs representing the player's own pieces available for selection.
     */
    private final List<UUID> ownPieces;

    /**
     * List of UUIDs representing the opponent's pieces available for selection.
     */
    private final List<UUID> enemyPieces;

    /**
     * Constructs a notification for selecting pieces for swapcard.
     *
     * @param ownPieces   the list of the player's own pieces
     * @param enemyPieces the list of the opponent's pieces
     */
    public SelectableSwapNotification(List<UUID> ownPieces, List<UUID> enemyPieces) {
        this.ownPieces = ownPieces;
        this.enemyPieces = enemyPieces;
    }

    /**
     * Gets the list of the player's own pieces available for selection.
     *
     * @return a list of UUIDs representing the player's own pieces
     */
    public List<UUID> getOwnPieces() {
        return ownPieces;
    }

    /**
     * Gets the list of the opponent's pieces available for selection.
     *
     * @return a list of UUIDs representing the opponent's pieces
     */
    public List<UUID> getEnemyPieces() {
        return enemyPieces;
    }
}
