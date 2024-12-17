package pp.mdga.message.client;

import com.jme3.network.serializing.Serializable;
import pp.mdga.game.Color;

/**
 * A message sent by a client to deselect a TSK.
 */
@Serializable
public class DeselectTSKMessage extends ClientMessage {
    /**
     * The color associated with the TSK to be deselected.
     */
    private final Color color;

    /**
     * Constructs a new DeselectTSK message with the specified color.
     *
     * @param color the color associated with the TSK to be deselected
     */
    public DeselectTSKMessage(Color color) {
        super();
        this.color = color;
    }

    /**
     * Default constructor for serialization purposes.
     */
    private DeselectTSKMessage() {
        color = null;
    }

    /**
     * Returns the color associated with the TSK to be deselected.
     *
     * @return the color associated with the TSK to be deselected
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
        return "DeselectTSK{" + "color=" + color + '}';
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
