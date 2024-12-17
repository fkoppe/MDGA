package pp.mdga.notification;

import pp.mdga.game.Color;

public class LobbyReadyNotification extends Notification {

    /**
     * The color of the player.
     */
    private Color color;

    /**
     * Indicates if the player is ready.
     */
    private boolean ready;

    /**
     * Constructor
     */
    public LobbyReadyNotification(Color color, boolean ready) {
        this.color = color;
        this.ready = ready;
    }

    /**
     * This method is used to get the color of the player
     *
     * @return the color of the player
     */
    public Color getColor() {
        return color;
    }

    /**
     * This method is used to get the ready state of the player
     *
     * @return the ready state of the player
     */
    public boolean isReady() {
        return ready;
    }

}
