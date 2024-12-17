package pp.mdga.message.server;

import com.jme3.network.serializing.Serializable;
import pp.mdga.game.Piece;

import java.util.List;

/**
 * A message sent by the server to the active player to select a piece to move.
 */
@Serializable
public class SelectPieceMessage extends ServerMessage {
    /**
     * The list of pieces
     */
    private final List<Piece> pieces;

    /**
     * The list of booleans of isHomeMove of the pieces
     */
    private final List<Boolean> isHomeMove;

    /**
     * The list of indexes of target nodes of the pieces
     */
    private final List<Integer> targetIndex;

    /**
     * Constructs a new SelectPiece instance.
     *
     * @param pieces      the pieces to be selected
     * @param isHomeMove  the List of booleans of isHomeMove of the pieces
     * @param targetIndex the List of indexes of target nodes of the pieces
     */
    public SelectPieceMessage(List<Piece> pieces, List<Boolean> isHomeMove, List<Integer> targetIndex) {
        super();
        this.pieces = pieces;
        this.isHomeMove = isHomeMove;
        this.targetIndex = targetIndex;
    }

    /**
     * Default constructor for serialization purposes.
     */
    public SelectPieceMessage() {
        super();
        pieces = null;
        isHomeMove = null;
        targetIndex = null;
    }

    /**
     * Getter for the pieces
     *
     * @return the pieces
     */
    public List<Piece> getPieces() {
        return pieces;
    }

    /**
     * returns if a move is a home move for an index
     *
     * @return List of boolean values
     */
    public List<Boolean> getIsHomeMove() {
        return isHomeMove;
    }

    /**
     * returns the target index of the pieces
     *
     * @return List of integers
     */
    public List<Integer> getTargetIndex() {
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
        return "SelectPieceMessage{" + "pieces=" + pieces + ", isHomeMove=" + isHomeMove + ", targetIndex=" + targetIndex + '}';
    }
}
