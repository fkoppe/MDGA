package pp.mdga.message.client;

import com.jme3.network.serializing.Serializable;
import pp.mdga.game.BonusCard;

import java.util.UUID;

/**
 * A message sent by a client to request playing a bonus card.
 */
@Serializable
public class RequestPlayCardMessage extends ClientMessage {
    /**
     * The bonus card to be played.
     */
    private final BonusCard card;

    /**
     * The identifier of the piece.
     */
    private final UUID ownPieceIdentifier;

    /**
     * The identifier of the enemy piece.
     */
    private final UUID enemyPieceIdentifier;

    /**
     * Constructs a new RequestPlayCard instance.
     *
     * @param card               the bonus card to be played
     * @param ownPieceIdentifier the identifier of the piece
     */
    public RequestPlayCardMessage(BonusCard card, UUID ownPieceIdentifier, UUID enemyPieceIdentifier) {
        this.ownPieceIdentifier = ownPieceIdentifier;
        this.card = card;
        this.enemyPieceIdentifier = enemyPieceIdentifier;
    }

    /**
     * Default constructor for serialization purposes.
     */
    private RequestPlayCardMessage() {
        card = BonusCard.HIDDEN;
        ownPieceIdentifier = UUID.randomUUID();
        enemyPieceIdentifier = UUID.randomUUID();
    }

    /**
     * Creates a new RequestPlayCard instance for a given bonus card.
     *
     * @param ownPieceIdentifier   the identifier of the piece
     * @param enemyPieceIdentifier the identifier of the enemy piece
     * @return a new RequestPlayCard instance
     */
    public static RequestPlayCardMessage requestPlaySwap(UUID ownPieceIdentifier, UUID enemyPieceIdentifier) {
        return new RequestPlayCardMessage(BonusCard.SWAP, ownPieceIdentifier, enemyPieceIdentifier);
    }

    /**
     * Creates a new RequestPlayCard instance for a shield bonus card.
     *
     * @param ownPieceIdentifier the identifier of the piece
     * @return a new RequestPlayCard instance
     */
    public static RequestPlayCardMessage requestPlayShield(UUID ownPieceIdentifier) {
        return new RequestPlayCardMessage(BonusCard.SHIELD, ownPieceIdentifier, null);
    }

    /**
     * Gets the bonus card associated with this request.
     *
     * @return the bonus card
     */
    public BonusCard getCard() {
        return card;
    }

    /**
     * Gets the piece identifier associated with this request.
     *
     * @return the piece identifier
     */
    public UUID getOwnPieceIdentifier() {
        return ownPieceIdentifier;
    }

    /**
     * Gets the enemy piece identifier associated with this request.
     *
     * @return the enemy piece identifier
     */
    public UUID getEnemyPieceIdentifier() {
        return enemyPieceIdentifier;
    }

    /**
     * Returns a string representation of this message.
     *
     * @return a string representation of this message
     */
    @Override
    public String toString() {
        assert card != null;
        return "RequestPlayCard={card=" + card + '}';
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
