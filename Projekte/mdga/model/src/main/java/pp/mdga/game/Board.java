package pp.mdga.game;

import com.jme3.network.serializing.Serializable;

/**
 * This class will be used to hold all Board relevant data.
 */
@Serializable
public class Board {
    /**
     * The size of the board.
     */
    public static final int BOARD_SIZE = 40;

    /**
     * Create Board attributes.
     */
    private final Node[] infield;

    /**
     * This constructor is used to create a new board
     */
    public Board() {
        infield = new Node[BOARD_SIZE];
        initializeBoard();
    }

    /**
     * Initializes the board by setting up the nodes.
     * Start nodes, bonus nodes, and regular nodes are created based on their positions.
     */
    private void initializeBoard() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (isStartNode(i)) {
                infield[i] = createStartNode(i);
            } else if (isBonusNode(i)) {
                infield[i] = new BonusNode();
            } else {
                infield[i] = new Node(null);
            }
        }
    }

    /**
     * Checks if the given index is a start node.
     *
     * @param i the index to check
     * @return true if the index is a start node, false otherwise
     */
    private boolean isStartNode(int i) {
        return i % 10 == 0;
    }

    /**
     * Checks if the given index is a bonus node.
     *
     * @param i the index to check
     * @return true if the index is a bonus node, false otherwise
     */
    private boolean isBonusNode(int i) {
        return i % 10 == 4;
    }

    /**
     * Creates a start node with the appropriate color based on the index.
     *
     * @param i the index of the start node
     * @return a new StartNode with the corresponding color
     */
    private StartNode createStartNode(int i) {
        return new StartNode(Color.getColor(i));
    }

    /**
     * This method returns the index of a specific piece on the board
     *
     * @param piece the piece to be searched for
     * @return the index of the piece
     */
    public int getInfieldIndexOfPiece(Piece piece) {
        for (int i = 0; i < infield.length; i++) {
            if (infield[i].isOccupied()) {
                if (infield[i].getOccupant().equals(piece)) {
                    return i;
                }
            }
        }

        return -1;
    }

    /**
     * This method returns the infield
     *
     * @return the infield
     */
    public Node[] getInfield() {
        return infield;
    }

    /**
     * This method sets a piece on a specific Node in the infield
     *
     * @param index the index of the node
     * @param piece the piece to be set
     */
    public void setPieceOnBoard(int index, Piece piece) {
        infield[index].setOccupant(piece);
    }

    @Override
    public String toString() {
        return "Default Board";
    }
}
