package pp.mdga.message.server;

import com.jme3.network.serializing.Serializable;

import java.util.UUID;

/**
 * A message sent by the server to the active player that he has to move a start piece.
 */
@Serializable
public class StartPieceMessage extends ServerMessage {
    /**
     * The identifier for the piece.
     */
    private final UUID pieceIdentifier;

    /**
     * The target index of the move;
     */
    private final int targetIndex;

    /**
     * Constructs a new StartPiece instance with the specified piece identifier.
     *
     * @param pieceIdentifier the identifier for the piece
     * @param targetIndex     the index of the targetNode
     */
    public StartPieceMessage(UUID pieceIdentifier, int targetIndex) {
        super();
        this.pieceIdentifier = pieceIdentifier;
        this.targetIndex = targetIndex;
    }

    /**
     * Default constructor for serialization purposes.
     */
    private StartPieceMessage() {
        super();
        this.pieceIdentifier = null;
        this.targetIndex = 0;
    }

    /**
     * Gets the identifier for the piece.
     *
     * @return the piece identifier
     */
    public UUID getPieceIdentifier() {
        return pieceIdentifier;
    }

    /**
     * Gets the index of the target node
     *
     * @return the index of the target node as int.
     */
    public int getTargetIndex() {
        return targetIndex;
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
        return "StartPieceMessage{" + "pieceIdentifier=" + pieceIdentifier + ", targetIndex=" + targetIndex + '}';
    }
}
