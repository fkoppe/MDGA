package pp.mdga.message.client;

import com.jme3.network.serializing.Serializable;

/**
 * A message indicating an animation event is finished in the game.
 */
@Serializable
public class AnimationEndMessage extends ClientMessage {
    /**
     * Constructs an AnimationEnd message.
     */
    public AnimationEndMessage() {
        super();
    }

    /**
     * Accepts a visitor for processing this message.
     *
     * @param interpreter the visitor to be used for processing
     * @param from        the connection ID of the sender
     */
    @Override
    public void accept(ClientInterpreter interpreter, int from) {
        interpreter.received(this, from);
    }
}
