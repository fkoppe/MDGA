package pp.mdga.notification;

import pp.mdga.game.BonusCard;

import java.util.List;

/**
 * Notification that contains a list of cards that the player can choose from.
 */
public class SelectableCardsNotification extends Notification {
    /**
     * The list of cards that the player can choose from.
     */
    private final List<BonusCard> cards;

    /**
     * Constructor.
     *
     * @param cards The list of cards that the player can choose from.
     */
    public SelectableCardsNotification(List<BonusCard> cards) {
        this.cards = cards;
    }

    /**
     * Get the list of cards that the player can choose from.
     *
     * @return The list of cards that the player can choose from.
     */
    public List<BonusCard> getCards() {
        return cards;
    }
}
