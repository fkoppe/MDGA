package pp.mdga.notification;

import java.util.UUID;

/**
 * Notification that a piece has been moved.
 */
public class MovePieceNotification extends Notification {
    /**
     * The unique identifier of the piece being moved.
     */
    private final UUID piece;

    /**
     * The index of the node from which the piece started moving.
     * Ignored if {@code moveStart} is {@code true}.
     */
    private final int startIndex;

    /**
     * The index of the node to which the piece is moving.
     * If {@code moveStart} is {@code true}, this index represents the start node.
     */
    private final int moveIndex;

    /**
     * Flag indicating that the piece ({@code uuid}) is moving from waiting area to the startNode.
     */
    private final boolean moveStart;

    /**
     * Constructs a notification for a piece start movement.
     *
     * @param piece     the unique identifier of the piece
     * @param moveIndex the destination node index
     * @param moveStart whether to ignore {@code startIndex} and use {@code moveIndex} as the start node
     */
    public MovePieceNotification(UUID piece, int moveIndex, boolean moveStart) {
        this.piece = piece;
        this.startIndex = 0;
        this.moveIndex = moveIndex;
        this.moveStart = moveStart;
    }

    /**
     * Constructs a notification for a piece infield move with {@code moveStart} set to {@code false}.
     *
     * @param piece      the unique identifier of the piece
     * @param startIndex the starting node index
     * @param moveIndex  the destination node index
     */
    public MovePieceNotification(UUID piece, int startIndex, int moveIndex) {
        this.piece = piece;
        this.startIndex = startIndex;
        this.moveIndex = moveIndex;
        this.moveStart = false;
    }

    /**
     * @return the destination node index
     */
    public int getMoveIndex() {
        return moveIndex;
    }

    /**
     * @return the starting node index
     */
    public int getStartIndex() {
        return startIndex;
    }

    /**
     * @return the unique identifier of the piece
     */
    public UUID getPiece() {
        return piece;
    }

    /**
     * @return {@code true} if the piece is moving from the waiting area to the start node
     */
    public boolean isMoveStart() {
        return moveStart;
    }
}
