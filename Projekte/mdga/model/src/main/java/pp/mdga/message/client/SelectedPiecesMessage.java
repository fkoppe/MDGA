package pp.mdga.message.client;

import com.jme3.network.serializing.Serializable;
import pp.mdga.game.Piece;

import java.util.ArrayList;
import java.util.List;

/**
 * A message sent by a client to indicate that a piece has been selected for a bonus cards.
 */
@Serializable
public class SelectedPiecesMessage extends ClientMessage {
    /**
     * Create SelectedPiecesMessage attributes.
     */
    private final List<Piece> pieces;

    /**
     * Constructor.
     */
    private SelectedPiecesMessage() {
        this.pieces = new ArrayList<>();
    }

    /**
     * Constructor for SelectedPieces
     *
     * @param pieces the piece identifier
     */
    public SelectedPiecesMessage(List<Piece> pieces) {
        this.pieces = pieces;
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

    /**
     * This method will be used to return pieces of SelectedPiecesMessage class.
     *
     * @return pieces as a List of Piece objects.
     */
    public List<Piece> getPieces() {
        return pieces;
    }

    /**
     * Returns a string representation of this message.
     *
     * @return a string representation of this message
     */
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SelectedPieces{Number of pieces: ").append(this.pieces.size()).append(" | ");
        for (Piece piece : this.pieces) {
            stringBuilder.append(piece).append(", ");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }
}
