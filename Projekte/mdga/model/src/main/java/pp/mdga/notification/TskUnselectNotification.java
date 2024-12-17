package pp.mdga.notification;

import pp.mdga.game.Color;

/**
 * Notification for unselecting a player
 */
public class TskUnselectNotification extends Notification {
    /**
     * The color of the player
     */
    private final Color color;

    /**
     * Constructor
     *
     * @param color the color of the player
     */
    public TskUnselectNotification(Color color) {
        this.color = color;
    }

    /**
     * Get the color
     *
     * @return color
     */
    public Color getColor() {
        return color;
    }
}
