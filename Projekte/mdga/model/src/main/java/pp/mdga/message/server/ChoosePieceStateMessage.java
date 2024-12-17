package pp.mdga.message.server;

import com.jme3.network.serializing.Serializable;

@Serializable
public class ChoosePieceStateMessage extends ServerMessage {

    public ChoosePieceStateMessage() {
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
