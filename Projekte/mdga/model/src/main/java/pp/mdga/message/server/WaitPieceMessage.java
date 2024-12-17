package pp.mdga.message.server;

import com.jme3.network.serializing.Serializable;

import java.util.UUID;

/**
 * A message sent by the server to the active player to choose a piece from the waiting area.
 */
@Serializable
public class WaitPieceMessage extends ServerMessage {
    /**
     * The pieceID of the piece to choose.
     */
    private final UUID pieceID;

    /**
     * Constructs a new WaitPiece instance.
     */
    public WaitPieceMessage(UUID pieceID) {
        super();
        this.pieceID = pieceID;
    }

    /**
     * Constructs a new WaitPiece instance.
     */
    public WaitPieceMessage() {
        super();
        this.pieceID = null;
    }

    /**
     * Getter for the pieceID
     *
     * @return the pieceID
     */
    public UUID getPieceID() {
        return pieceID;
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
        return "WaitPieceMessage{" + "pieceID=" + pieceID + '}';
    }
}
