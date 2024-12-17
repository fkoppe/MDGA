package pp.mdga.notification;

import pp.mdga.game.BonusCard;
import pp.mdga.game.Color;

/**
 * Notification that a card has been played.
 */
public class PlayCardNotification extends Notification {
    /**
     * The color of the player that played the card.
     */
    private final Color color;

    /**
     * The card that was played.
     */
    private final BonusCard card;

    /**
     * Constructor.
     *
     * @param color the color of the player that played the card.
     * @param card  the card that was played.
     */
    public PlayCardNotification(Color color, BonusCard card) {
        this.color = color;
        this.card = card;
    }

    /**
     * Get the color of the player that played the card.
     *
     * @return the color of the player that played the card.
     */
    public Color getColor() {
        return color;
    }

    /**
     * Get the card that was played.
     *
     * @return the card that was played.
     */
    public BonusCard getCard() {
        return card;
    }
}
