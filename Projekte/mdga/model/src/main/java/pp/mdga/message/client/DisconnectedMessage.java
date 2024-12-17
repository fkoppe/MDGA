package pp.mdga.message.client;

import com.jme3.network.serializing.Serializable;

/**
 * A message sent by a client to indicate that it has disconnected.
 */
@Serializable
public class DisconnectedMessage extends ClientMessage {
    /**
     * Constructs a new Disconnected message.
     */
    public DisconnectedMessage() {
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
