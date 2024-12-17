package pp.mdga.message.server;

import com.jme3.network.serializing.Serializable;

/**
 * A message sent by the server to the active player to enable the dice now button.
 */
@Serializable
public class DiceNowMessage extends ServerMessage {
    /**
     * Constructs a new DiceNow instance.
     */
    public DiceNowMessage() {
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
