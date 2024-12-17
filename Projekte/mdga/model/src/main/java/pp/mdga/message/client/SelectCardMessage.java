package pp.mdga.message.client;

import com.jme3.network.serializing.Serializable;
import pp.mdga.game.BonusCard;
import pp.mdga.game.card.PowerCard;

/**
 * A message sent from the client to the server to select a bonus card.
 */
@Serializable
public class SelectCardMessage extends ClientMessage {
    /**
     * The bonus card to be selected.
     */
    private final PowerCard card;

    /**
     * Constructs a new SelectCard instance.
     *
     * @param card the power card to be selected
     */
    public SelectCardMessage(PowerCard card) {
        this.card = card;
    }

    /**
     * Default constructor for serialization purposes.
     */
    private SelectCardMessage() {
        card = null;
    }

    /**
     * Gets the power card associated with this selection.
     *
     * @return the power card
     */
    public PowerCard getCard() {
        return card;
    }

    /**
     * Returns a string representation of this message.
     *
     * @return a string representation of this message
     */
    @Override
    public String toString() {
        return "SelectCard{card=" + card + '}';
    }

    /**
     * Accepts a visitor to process this message.
     *
     * @param interpreter the visitor to process this message
     * @param from        the connection ID from which the message was received
     */
    @Override
    public void accept(ClientInterpreter interpreter, int from) {
        interpreter.received(this, from);
    }
}
