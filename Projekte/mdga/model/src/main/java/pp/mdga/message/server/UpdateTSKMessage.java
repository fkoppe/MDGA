package pp.mdga.message.server;

import com.jme3.network.serializing.Serializable;
import pp.mdga.game.Color;

/**
 * A message sent by the server to every client to update the TSK.
 */
@Serializable
public class UpdateTSKMessage extends ServerMessage {
    /**
     * The name associated with the update.
     */
    private final int id;

    /**
     * The color associated with the update.
     */
    private final Color color;

    /**
     * The flag if the TSK is taken.
     */
    private final boolean isTaken;

    /**
     * Constructs a new UpdateTSK instance with the specified id and color.
     *
     * @param id    the name associated with the update
     * @param color the color associated with the update
     */
    public UpdateTSKMessage(int id, Color color, boolean isTaken) {
        super();
        this.id = id;
        this.color = color;
        this.isTaken = isTaken;
    }

    /**
     * Default constructor for serialization purposes.
     */
    private UpdateTSKMessage() {
        this(0, null, false);
    }

    /**
     * Gets the name associated with the update.
     *
     * @return the name
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the color associated with the update.
     *
     * @return the color
     */
    public Color getColor() {
        return color;
    }

    /**
     * Checks if the TSK is taken.
     *
     * @return true if the TSK is taken, false otherwise
     */
    public boolean isTaken() {
        return isTaken;
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
        return "UpdateTSKMessage{" + "id=" + id + ", color=" + color + '}';
    }
}
