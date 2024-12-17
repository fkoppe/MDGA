package pp.mdga.message.server;

import com.jme3.network.serializing.Serializable;
import pp.mdga.game.Piece;

/**
 * A message sent by the server to the client to move a piece on the board.
 */
@Serializable
public class MoveMessage extends ServerMessage {
    /**
     * The identifier of the piece that should be moved.
     */
    private final Piece piece;

    /**
     * The index of the target node;
     */
    private final int targetIndex;

    /**
     * True if the pieces move into the home.
     */
    private final boolean isHomeMove;

    /**
     * Constructs a new MoveMessage instance.
     *
     * @param piece       the identifier of the piece that should be moved
     * @param isHomeMove  boolean flag declaring home move or not
     * @param targetIndex the targetIndex
     */
    public MoveMessage(Piece piece, boolean isHomeMove, int targetIndex) {
        super();
        this.piece = piece;
        this.isHomeMove = isHomeMove;
        this.targetIndex = targetIndex;
    }

    /**
     * Default constructor for serialization purposes.
     */
    private MoveMessage() {
        super();
        piece = null;
        targetIndex = 0;
        isHomeMove = false;
    }

    /**
     * Returns the identifier of the piece that should be moved.
     *
     * @return the identifier of the piece that should be moved
     */
    public Piece getPiece() {
        return piece;
    }

    /**
     * Returns the index of the target Node.
     *
     * @return the target index
     */
    public int getTargetIndex() {
        return targetIndex;
    }

    /**
     * Returns a boolean based on if it is a home move or not.
     *
     * @return the boolean isHomeMove.
     */
    public boolean isHomeMove() {
        return isHomeMove;
    }

    /**
     * Accepts a visitor to process this message.
     *
     * @param interpreter the visitor to process this message
     */
    @Override
    public void accept(ServerInterpreter interpreter) {
        interpreter.received(this);
    }

    /**
     * Returns a string representation of this message.
     *
     * @return a string representation of this message
     */
    @Override
    public String toString() {
        return "MoveMessage{" + "pieceUUID=" + piece.getUuid() + ", targetIndex=" + targetIndex + ", isHomeMove=" + isHomeMove + '}';
    }
}
