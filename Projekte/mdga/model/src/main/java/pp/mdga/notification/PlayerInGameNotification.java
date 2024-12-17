package pp.mdga.notification;

import pp.mdga.game.Color;

import java.util.List;
import java.util.UUID;

/**
 * Notification that a player is in the game.
 */
public class PlayerInGameNotification extends Notification {
    /**
     * The color of the player that is in the game.
     */
    private final Color color;

    /**
     * The name of the player that is in the game.
     */
    private final String name;

    /**
     * The list of piece IDs associated with the player.
     */
    private final List<UUID> piecesList;

    /**
     * Constructor.
     *
     * @param color the color of the player that is in the game.
     * @param name  the name of the player that is in the game.
     */
    public PlayerInGameNotification(Color color, List<UUID> piecesList, String name) {
        this.color = color;
        this.name = name;
        this.piecesList = piecesList;
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
     * Get the list of piece IDs associated with the player.
     *
     * @return the list of piece IDs associated with the player.
     */
    public List<UUID> getPiecesList() {
        return piecesList;
    }
}
