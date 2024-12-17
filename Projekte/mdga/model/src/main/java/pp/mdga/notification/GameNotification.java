package pp.mdga.notification;

import pp.mdga.game.Color;

/**
 * GameNotification class
 */
public class GameNotification extends Notification {

    private final Color ownColor;

    /**
     * Constructor
     */
    public GameNotification(Color ownColor) {
        this.ownColor = ownColor;
    }

    /**
     * This method will return the ownColor attribute of GameNotification class.
     *
     * @return ownColor as a Color object.
     */
    public Color getOwnColor() {
        return ownColor;
    }
}
