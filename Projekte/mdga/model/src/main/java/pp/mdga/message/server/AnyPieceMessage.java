package pp.mdga.message.server;

import com.jme3.network.serializing.Serializable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * A message sent by the server to the active player containing a list of pieces that the player can move any piece of the list on the board.
 */
@Serializable
public class AnyPieceMessage extends ServerMessage {
    /**
     * The list of pieces
     */
    private final ArrayList<UUID> piece;

    /**
     * Constructor for AnyPiece
     */
    public AnyPieceMessage() {
        super();
        piece = new ArrayList<>();
    }

    /**
     * Add a piece to the list of pieces
     *
     * @param piece the piece to add
     */
    public void addPiece(UUID piece) {
        this.piece.add(piece);
    }

    /**
     * Getter for the list of pieces
     *
     * @return the list of pieces
     */
    public List<UUID> getPiece() {
        return piece;
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
        return "AnyPiece{piece=" + piece + '}';
    }
}
