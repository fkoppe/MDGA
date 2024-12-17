package pp.mdga.message.client;

import com.jme3.network.serializing.Serializable;

/**
 * A message sent by the host to start the game.
 */
@Serializable
public class ClientStartGameMessage extends ClientMessage {
    /**
     * Constructor.
     */
    public ClientStartGameMessage() {
        super();
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
