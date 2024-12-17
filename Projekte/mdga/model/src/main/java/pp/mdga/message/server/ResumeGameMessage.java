package pp.mdga.message.server;

import com.jme3.network.serializing.Serializable;

/**
 * A message sent by the server to resume the game.
 */
@Serializable
public class ResumeGameMessage extends ServerMessage {
    /**
     * Constructs a new ResumeGame instance.
     */
    public ResumeGameMessage() {
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
