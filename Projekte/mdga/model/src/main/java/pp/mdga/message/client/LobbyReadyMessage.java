package pp.mdga.message.client;

import com.jme3.network.serializing.Serializable;

/**
 * A message sent by the client to ready-up in the lobby.
 */
@Serializable
public class LobbyReadyMessage extends ClientMessage {
    /**
     * Constructs a new LobbyReady instance.
     */
    public LobbyReadyMessage() {
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
