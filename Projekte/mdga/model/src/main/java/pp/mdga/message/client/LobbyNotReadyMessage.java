package pp.mdga.message.client;

import com.jme3.network.serializing.Serializable;

/**
 * A message sent by a client to unready in the lobby.
 */
@Serializable
public class LobbyNotReadyMessage extends ClientMessage {
    /**
     * Constructs a new LobbyNotReady instance.
     */
    public LobbyNotReadyMessage() {
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
