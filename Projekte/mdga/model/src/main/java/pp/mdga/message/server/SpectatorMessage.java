package pp.mdga.message.server;

import com.jme3.network.serializing.Serializable;

/**
 * A message sent by the server to indicate that the client is now a spectator.
 */
@Serializable
public class SpectatorMessage extends ServerMessage {
    /**
     * Constructor
     */
    public SpectatorMessage() {
        super();
    }

    /**
     * @param interpreter the visitor to process this message
     */
    @Override
    public void accept(ServerInterpreter interpreter) {
        interpreter.received(this);
    }
}
