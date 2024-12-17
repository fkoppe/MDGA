package pp.mdga.notification;

import pp.mdga.game.BonusCard;

/**
 * Notification that is sent when a card is acquired.
 */
public class AcquireCardNotification extends Notification {

    private BonusCard bonusCard;

    /**
     * Constructor.
     */
    public AcquireCardNotification(BonusCard bonusCard) {
        this.bonusCard = bonusCard;
    }

    /**
     * This method will return the bonusCard attribute of AcquireCardNotification class.
     *
     * @return bonusCard as a BonusCard object.
     */
    public BonusCard getBonusCard() {
        return bonusCard;
    }
}
