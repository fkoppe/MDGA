package pp.mdga.message.server;

import com.jme3.network.serializing.Serializable;

/**
 * A message sent by the server to inform the clients that the server is shutting down.
 */
@Serializable
public class ShutdownMessage extends ServerMessage {
    /**
     * Constructs a new Shutdown message.
     */
    public ShutdownMessage() {
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
