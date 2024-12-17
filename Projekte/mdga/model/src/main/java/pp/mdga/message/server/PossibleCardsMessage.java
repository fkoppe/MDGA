package pp.mdga.message.server;

import com.jme3.network.serializing.Serializable;
import pp.mdga.game.card.PowerCard;

import java.util.ArrayList;
import java.util.List;

/**
 * A message sent by the server to the client to indicate the possible cards that can be chosen.
 */
@Serializable
public class PossibleCardsMessage extends ServerMessage {
    /**
     * The list of possible cards.
     */
    private final List<PowerCard> possibleCards;

    /**
     * Constructor for a PossibleCard instance.
     */
    public PossibleCardsMessage() {
        super();
        possibleCards = new ArrayList<>();
    }

    /**
     * Constructor.
     *
     * @param possibleCards as the list of possible power cards as a List of PowerCard objects.
     */
    public PossibleCardsMessage(List<PowerCard> possibleCards) {
        this.possibleCards = possibleCards;
    }

    /**
     * This method will be used to return possibleCards attribute of PossibleCardMessage class.
     *
     * @return possibleCards as a List of PowerCard objects.
     */
    public List<PowerCard> getPossibleCards() {
        return this.possibleCards;
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

    /**
     * Returns a string representation of this message.
     *
     * @return a string representation of this message
     */
    @Override
    public String toString() {
        return "PossibleCardMessage{" + "possibleCards=" + possibleCards + '}';
    }
}
