package pp.mdga.message.client;

import com.jme3.network.serializing.Serializable;
import pp.mdga.game.Color;

/**
 * A message sent by a client to select a TSK.
 */
@Serializable
public class SelectTSKMessage extends ClientMessage {
    /**
     * The color associated with the TSK to be selected.
     */
    private final Color color;

    /**
     * Constructs a new SelectTSK message with the specified color.
     *
     * @param color the color associated with the TSK to be selected
     */
    public SelectTSKMessage(Color color) {
        this.color = color;
    }

    /**
     * Default constructor for serialization purposes.
     */
    private SelectTSKMessage() {
        color = null;
    }

    /**
     * Gets the color associated with the TSK to be selected.
     *
     * @return the color associated with the TSK to be selected
     */
    public Color getColor() {
        return color;
    }

    /**
     * Returns a string representation of this message.
     *
     * @return a string representation of this message
     */
    @Override
    public String toString() {
        return "SelectTSK{color=" + color + '}';
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
