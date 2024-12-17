package pp.mdga.notification;

import pp.mdga.game.Color;

/**
 * Class TskSelectNotification
 */
public class TskSelectNotification extends Notification {
    /**
     * The color of the player that is in the game.
     */
    private final Color color;

    /**
     * The name of the player that is in the game.
     */
    private final String name;

    /**
     * Indicates if the select notification affects the own user.
     */
    private final boolean isSelf;

    /**
     * Constructor.
     *
     * @param color the color of the player that is in the game.
     * @param name  the name of the player that is in the game.
     */
    public TskSelectNotification(Color color, String name, boolean isSelf) {
        this.color = color;
        this.name = name;
        this.isSelf = isSelf;
    }

    /**
     * Get the color of the player that is in the game.
     *
     * @return the color of the player that is in the game.
     */
    public Color getColor() {
        return color;
    }

    /**
     * Get the name of the player that is in the game.
     *
     * @return the name of the player that is in the game.
     */
    public String getName() {
        return name;
    }

    /**
     * returns a boolean based of if the select notification affects the own user
     *
     * @return boolean isSelf
     */
    public boolean isSelf() {
        return isSelf;
    }
}
