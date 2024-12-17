package pp.mdga.message.client;

import com.jme3.network.serializing.Serializable;

/**
 * A message sent by the host to force the continuation of the game, when the game was interrupted.
 */
@Serializable
public class ForceContinueGameMessage extends ClientMessage {
    /**
     * Constructs a new ForceContinueGame message.
     */
    public ForceContinueGameMessage() {
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
