package pp.mdga.notification;

import pp.mdga.game.Color;

/**
 * Notification that a player is in the game.
 */
public class ResumeNotification extends Notification {
    /**
     * The color of the player that is in the game.
     */
    private final Color color;

    /**
     * Constructor.
     *
     * @param color the color of the player that is in the game.
     */
    public ResumeNotification(Color color) {
        this.color = color;
    }

    /**
     * Get the color of the player that is in the game.
     *
     * @return the color of the player that is in the game.
     */
    public Color getColor() {
        return color;
    }
}
