package pp.mdga.message.server;

import com.jme3.network.serializing.Serializable;
import pp.mdga.game.Piece;
import pp.mdga.game.card.PowerCard;

import java.util.ArrayList;
import java.util.List;

/**
 * A message sent by the server to the active player to play a card.
 */
@Serializable
public class PlayCardMessage extends ServerMessage {
    /**
     * The card that should be played.
     */
    private final PowerCard card;

    /**
     * The list of pieces which will be used.
     */
    private final List<Piece> pieces;

    /**
     * The modifier of the dice.
     */
    private final int diceModifier;

    /**
     * Constructs a new PlayCard message.
     *
     * @param card         the card that should be played
     * @param pieces       as the pieces which should be played as a List of Piece objects.
     * @param diceModifier the new modifier of the dice
     */
    public PlayCardMessage(PowerCard card, List<Piece> pieces, int diceModifier) {
        super();
        this.card = card;
        this.pieces = pieces;
        this.diceModifier = diceModifier;
    }

    /**
     * Default constructor for serialization purposes.
     */
    private PlayCardMessage() {
        super();
        this.card = null;
        this.pieces = new ArrayList<Piece>();
        this.diceModifier = 1;
    }

    /**
     * This method will be used to return card attribute of PlayCardMessage class.
     *
     * @return card as a PowerCard object.
     */
    public PowerCard getCard() {
        return this.card;
    }

    /**
     * This mehtod will be usd to return pieces attribute of PlayCardMessage class.
     *
     * @return pieces as a List of Piece objects.
     */
    public List<Piece> getPieces() {
        return this.pieces;
    }

    /**
     * this method returns the dice modifier
     *
     * @return the dice modifier as int
     */
    public int getDiceModifier() {
        return diceModifier;
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
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("PlayCardMessage{card: ").append(this.card).append(" with selected pieces: {");
        for (Piece piece : this.pieces) {
            stringBuilder.append(piece).append(", ");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        stringBuilder.append("} and dice modifier: ").append(this.diceModifier);

        return stringBuilder.toString();
    }
}
