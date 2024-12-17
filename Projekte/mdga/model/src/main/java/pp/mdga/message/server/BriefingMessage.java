package pp.mdga.message.server;

import com.jme3.network.serializing.Serializable;

/**
 * A message sent by the server to the reconnected player to provide a briefing about the current game state.
 */
@Serializable
public class BriefingMessage extends ServerMessage {
    /**
     * Constructs a new Briefing instance.
     */
    public BriefingMessage() {
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
