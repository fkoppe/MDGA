package pp.mdga.message.client;

import com.jme3.network.serializing.Serializable;
import pp.mdga.game.Piece;

import java.util.UUID;

/**
 * A message sent by a client to request a move for a piece.
 */
@Serializable
public class RequestMoveMessage extends ClientMessage {
    /**
     * The identifier for the piece.
     */
    private final Piece piece;

    /**
     * Constructor for RequestMove
     *
     * @param piece the piece
     */
    public RequestMoveMessage(Piece piece) {
        this.piece = piece;
    }

    /**
     * Default constructor for serialization purposes.
     */
    private RequestMoveMessage() {
        piece = null;
    }

    /**
     * Getter for the piece identifier
     *
     * @return the piece identifier
     */
    public Piece getPiece() {
        return piece;
    }

    /**
     * Returns a string representation of this message.
     *
     * @return a string representation of this message
     */
    @Override
    public String toString() {
        assert piece != null;
        return "RequestMove{pieceIdentifier = " + piece.toString() + '}';
    }

    /**
     * Accepts a visitor to process this message.
     *
     * @param interpreter the visitor to process this message
     * @param from        the connection ID from which the message was received
     */
    @Override
    public void accept(ClientInterpreter interpreter, int from) {
        interpreter.received(this, from);
    }
}
