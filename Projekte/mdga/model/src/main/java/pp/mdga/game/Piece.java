package pp.mdga.game;

import com.jme3.network.serializing.Serializable;

import java.util.UUID;

/**
 * This class will be used to hold all Piece relevant data.
 */
@Serializable
public class Piece {
    /**
     * The shield state of the piece.
     */
    private ShieldState shield;

    /**
     * The current state of the piece.
     */
    private PieceState state;

    /**
     * The color of the piece.
     */
    private final Color color;

    /**
     * The unique identifier of the piece.
     */
    private final UUID uuid = UUID.randomUUID();

    /**
     * This constructor is used to create a new Piece
     *
     * @param color the color of the piece
     * @param state the state of the piece
     */
    public Piece(Color color, PieceState state) {
        this.color = color;
        this.state = state;
        shield = ShieldState.NONE;
    }

    /**
     * Constructor.
     */
    private Piece() {
        color = Color.NONE;
        state = PieceState.WAITING;
        shield = ShieldState.NONE;
    }

    /**
     * Sets the shield state of the piece.
     *
     * @param shield the new shield state
     */
    public void setShield(ShieldState shield) {
        this.shield = shield;
    }

    /**
     * Gets the shield state of the piece.
     *
     * @return the shield state
     */
    public ShieldState getShield() {
        return shield;
    }

    /**
     * Sets the state of the piece.
     *
     * @param state the new state
     */
    public void setState(PieceState state) {
        this.state = state;
    }

    /**
     * Gets the state of the piece.
     *
     * @return the state
     */
    public PieceState getState() {
        return state;
    }

    /**
     * Checks if the piece is shielded.
     *
     * @return true if the piece is shielded, false otherwise
     */
    public boolean isShielded() {
        return shield == ShieldState.ACTIVE;
    }

    /**
     * Checks if the shield of a piece is suppressed.
     *
     * @return true if the shield is suppressed, false otherwise
     */
    public boolean isSuppressed() {
        return shield == ShieldState.SUPPRESSED;
    }

    /**
     * Gets the color of the piece.
     *
     * @return the color
     */
    public Color getColor() {
        return color;
    }

    /**
     * Gets the unique identifier of the piece.
     *
     * @return the UUID
     */
    public UUID getUuid() {
        return uuid;
    }

    /**
     * This method will return all necessary information of Piece class in a more readable way.
     *
     * @return information as a String.
     */
    @Override
    public String toString() {
        return "Piece with UUID: %s and color: %s with state: %s".formatted(this.uuid, color, state);
    }

    /**
     * This method will be used to create the hash code of this Piece class.
     *
     * @return hashCode as an Integer.
     */
    @Override
    public int hashCode() {
        return this.uuid.hashCode();
    }

    /**
     * This method will be used to check if the given obj parameter is equal to this object.
     * It will return false if the obj parameter is null or if the hash codes of both objects are not equal. Otherwise
     * it will return true.
     *
     * @param obj as the object which will be compared as an Object.
     * @return true or false.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj instanceof Piece) {
            Piece piece = (Piece) obj;

            return uuid.toString().equals(piece.uuid.toString());
        }

        return false;
    }
}
