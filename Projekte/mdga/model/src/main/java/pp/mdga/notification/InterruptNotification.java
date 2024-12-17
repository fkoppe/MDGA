package pp.mdga.notification;

import pp.mdga.game.Color;

/**
 * Notification that a card has been drawn
 */
public class InterruptNotification extends Notification {
    /**
     * The color of the player who disconnected
     */
    private final Color color;

    /**
     * @param color the color of the player who disconnected
     */
    public InterruptNotification(Color color) {
        this.color = color;
    }

    /**
     * @return the color of the player who disconnected
     */
    public Color getColor() {
        return color;
    }
}
