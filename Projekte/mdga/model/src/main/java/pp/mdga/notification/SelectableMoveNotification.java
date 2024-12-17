package pp.mdga.notification;

import java.util.List;
import java.util.UUID;

/**
 * Notification for selecting pieces and their possible moves.
 */
public class SelectableMoveNotification extends Notification {
    /**
     * List of UUIDs representing the pieces that can be moved based on the dice roll.
     */
    private final List<UUID> pieces;

    /**
     * List of integers representing the target nodes the pieces can move to.
     */
    private final List<Integer> moveIndices;

    /**
     * List of booleans indicating whether the corresponding target nodes are in the home area.
     * {@code true} if the target node is in the home area, {@code false} if in the infield.
     */
    private final List<Boolean> homeMoves;

    /**
     * Constructs a notification for selectable piece moves.
     *
     * @param pieces      the list of pieces that can be moved
     * @param moveIndices the list of target nodes for the moves
     * @param homeMoves   the list indicating if the target nodes are in the home area
     */
    public SelectableMoveNotification(List<UUID> pieces, List<Integer> moveIndices, List<Boolean> homeMoves) {
        this.pieces = pieces;
        this.moveIndices = moveIndices;
        this.homeMoves = homeMoves;
    }

    /**
     * Gets the list of pieces that can be moved.
     *
     * @return a list of UUIDs representing the movable pieces
     */
    public List<UUID> getPieces() {
        return pieces;
    }

    /**
     * Gets the list of target nodes for the moves.
     *
     * @return a list of integers representing the target nodes
     */
    public List<Integer> getMoveIndices() {
        return moveIndices;
    }

    /**
     * Gets the list indicating whether the target nodes are in the home area.
     *
     * @return a list of booleans, {@code true} if the target node is in the home area, {@code false} otherwise
     */
    public List<Boolean> getHomeMoves() {
        return homeMoves;
    }
}
