package pp.mdga.notification;

import pp.mdga.game.BonusCard;
import pp.mdga.game.Color;

/**
 * Notification that a card has been drawn
 */
public class DrawCardNotification extends Notification {
    /**
     * The color of the player who drew the card
     */
    private final Color color;

    /**
     * The card that was drawn
     */
    private final BonusCard card;

    /**
     * @param color the color of the player who drew the card
     * @param card  the card that was drawn
     */
    public DrawCardNotification(Color color, BonusCard card) {
        this.color = color;
        this.card = card;
    }

    /**
     * @return the color of the player who drew the card
     */
    public Color getColor() {
        return color;
    }

    /**
     * @return the card that was drawn
     */
    public BonusCard getCard() {
        return card;
    }
}
