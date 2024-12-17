package pp.mdga.message.server;

import com.jme3.network.serializing.Serializable;
import pp.mdga.game.card.PowerCard;

@Serializable
public class DrawCardMessage extends ServerMessage {

    private final PowerCard card;

    public DrawCardMessage(PowerCard card) {
        super();
        this.card = card;
    }

    private DrawCardMessage() {
        super();
        card = null;
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

    public PowerCard getCard() {
        return card;
    }

    @Override
    public String toString() {
        return "DrawCardMessage{" + "PowerCard=" + card + '}';
    }
}
