package pp.mdga.message.server;

import com.jme3.network.serializing.Serializable;
import pp.mdga.game.Piece;

import java.util.ArrayList;
import java.util.List;

/**
 * A message sent by the server to the active player to give all possible pieces to choose from.
 */
@Serializable
public class PossiblePieceMessage extends ServerMessage {
    /**
     * The list of possible own pieces
     */
    private final List<Piece> possibleOwnPieces;

    /**
     * The list of possible enemy pieces
     */
    private final List<Piece> possibleEnemyPieces;

    /**
     * Constructor for PossiblePiece
     */
    public PossiblePieceMessage() {
        super();
        possibleOwnPieces = new ArrayList<>();
        possibleEnemyPieces = new ArrayList<>();
    }

    /**
     * Swap the possible pieces
     *
     * @param possibleOwnPieces   the list of possible own pieces
     * @param possibleEnemyPieces the list of possible enemy pieces
     * @return the swapped possible pieces
     */
    public static PossiblePieceMessage swapPossiblePieces(List<Piece> possibleOwnPieces, List<Piece> possibleEnemyPieces) {
        PossiblePieceMessage possiblePieceMessage = new PossiblePieceMessage();
        possiblePieceMessage.possibleOwnPieces.addAll(possibleOwnPieces);
        possiblePieceMessage.possibleEnemyPieces.addAll(possibleEnemyPieces);
        return possiblePieceMessage;
    }

    /**
     * Get the possible pieces for the shield
     *
     * @param possibleOwnPieces the list of possible own pieces
     * @return the possible pieces for the shield
     */
    public static PossiblePieceMessage shieldPossiblePieces(List<Piece> possibleOwnPieces) {
        PossiblePieceMessage possiblePieceMessage = new PossiblePieceMessage();
        possiblePieceMessage.possibleOwnPieces.addAll(possibleOwnPieces);
        return possiblePieceMessage;
    }

    /**
     * Add a piece to the list of possible pieces
     *
     * @param piece the piece to add
     */
    public void addOwnPossiblePiece(Piece piece) {
        this.possibleOwnPieces.add(piece);
    }

    /**
     * Add a piece to the list of possible enemy pieces
     *
     * @param piece the piece to add
     */
    public void addEnemyPossiblePiece(Piece piece) {
        this.possibleEnemyPieces.add(piece);
    }

    /**
     * Getter for the list of possible pieces
     *
     * @return the list of possible pieces
     */
    public List<Piece> getOwnPossiblePieces() {
        return possibleOwnPieces;
    }

    /**
     * Getter for the list of possible enemy pieces
     *
     * @return the list of possible enemy pieces
     */
    public List<Piece> getEnemyPossiblePieces() {
        return possibleEnemyPieces;
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
        return "PossiblePieceMessage{" + "possibleOwnPieces=" + possibleOwnPieces + ", possibleEnemyPieces=" + possibleEnemyPieces + '}';
    }
}
