package pp.mdga.game;

import com.jme3.network.serializing.Serializable;

/**
 * Represents a node on the board.
 */
@Serializable
public class Node {
    /**
     * The occupant of the node.
     */
    protected Piece occupant;

    /**
     * This constructor is used to create a new node object with a given occupant.
     *
     * @param piece as the occupant of the node.
     */
    public Node(Piece piece) {
        occupant = piece;
    }

    /**
     * This constructor is used to create a new node object with a default occupant.
     */
    private Node() {
        occupant = new Piece(Color.AIRFORCE, PieceState.WAITING);
    }

    /**
     * This method is used to get an occupant of the node.
     *
     * @return the current occupant of the node
     */
    public Piece getOccupant() {
        return occupant;
    }

    /**
     * This method is used to set a new occupant
     *
     * @param occupant the new occupant of the node
     */
    public void setOccupant(Piece occupant) {
        this.occupant = occupant;
    }

    public boolean isBonus() {
        return false;
    }

    /**
     * This method is used to clear the node of its occupant
     */
    public void clearOccupant() {
        this.occupant = null;
    }

    /**
     * This method is used to return a boolean value based on the occupation status
     *
     * @return the boolean value corresponding to the occupation status
     */
    public boolean isOccupied() {
        return occupant != null;
    }

    /**
     * This method will be used to check if the node is occupied by a piece of the given color.
     *
     * @param color as the color of the piece as a Color object.
     * @return true or false.
     */
    public boolean isOccupied(Color color) {
        return isOccupied() && this.occupant.getColor() == color;
    }

    public boolean isStart() {
        return false;
    }
}
