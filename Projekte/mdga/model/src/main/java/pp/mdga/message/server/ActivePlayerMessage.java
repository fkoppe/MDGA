package pp.mdga.message.server;

import com.jme3.network.serializing.Serializable;
import pp.mdga.game.Color;

/**
 * A message sent by the server to inform the clients about the active player.
 */
@Serializable
public class ActivePlayerMessage extends ServerMessage {
    /**
     * The color of the active player.
     */
    private final Color color;

    /**
     * Constructor for ActivePlayer
     *
     * @param color the color of the active player
     */
    public ActivePlayerMessage(Color color) {
        super();
        this.color = color;
    }

    /**
     * Default constructor for serialization purposes.
     */
    private ActivePlayerMessage() {
        color = null;
    }

    /**
     * Getter for the color of the active player
     *
     * @return the color of the active player
     */
    public Color getColor() {
        return color;
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
        return "ActivePlayer{color=" + color + '}';
    }
}
