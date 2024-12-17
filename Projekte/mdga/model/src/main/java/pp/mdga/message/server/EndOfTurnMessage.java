package pp.mdga.message.server;

import com.jme3.network.serializing.Serializable;

/**
 * A message sent by the server to indicate the end of the turn of the active player.
 */
@Serializable
public class EndOfTurnMessage extends ServerMessage {
    /**
     * Constructs a new EndOfTurn instance.
     */
    public EndOfTurnMessage() {
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
