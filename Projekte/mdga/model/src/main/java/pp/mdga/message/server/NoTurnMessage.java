package pp.mdga.message.server;

import com.jme3.network.serializing.Serializable;

/**
 * A message sent by the server to the active player to indicate that he has no valid moves.
 */
@Serializable
public class NoTurnMessage extends ServerMessage {
    /**
     * Constructs a new NoTurn instance.
     */
    public NoTurnMessage() {
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
