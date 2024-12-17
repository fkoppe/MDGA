package pp.mdga.message.client;

import com.jme3.network.serializing.Serializable;

/**
 * A message sent by a client to indicate that the player is not using a power card.
 */
@Serializable
public class NoPowerCardMessage extends ClientMessage {
    /**
     * Constructs a new NoPowerCard instance.
     */
    public NoPowerCardMessage() {
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
