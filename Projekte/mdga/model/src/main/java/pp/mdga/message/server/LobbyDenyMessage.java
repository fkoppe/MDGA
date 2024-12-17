package pp.mdga.message.server;

import com.jme3.network.serializing.Serializable;

/**
 * A message sent by the server to deny a client's request to join the lobby.
 */
@Serializable
public class LobbyDenyMessage extends ServerMessage {
    /**
     * Constructs a new LobbyDeny instance.
     */
    public LobbyDenyMessage() {
        super();
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
}
