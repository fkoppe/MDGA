package pp.mdga.message.server;

import com.jme3.network.serializing.Serializable;

/**
 * A message sent by the server to the active player to indicate that they can roll the dice again.
 */
@Serializable
public class DiceAgainMessage extends ServerMessage {
    /**
     * Constructs a new DiceAgain instance.
     */
    public DiceAgainMessage() {
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
