package pp.mdga.notification;

import pp.mdga.game.Color;

/**
 * Notification that the active player has changed
 */
public class ActivePlayerNotification extends Notification {
    /**
     * The color of the active player
     */
    private final Color color;

    /**
     * Constructor.
     *
     * @param color the color of the active player
     */
    public ActivePlayerNotification(Color color) {
        this.color = color;
    }

    /**
     * @return the color of the active player
     */
    public Color getColor() {
        return color;
    }
}
