package pp.mdga.message.server;

import com.jme3.network.serializing.Serializable;
import pp.mdga.game.Player;

/**
 * A message sent from the server to the client indicating that a player has joined the lobby.
 */
@Serializable
public class LobbyPlayerJoinedMessage extends ServerMessage {

    /**
     * The player joining the lobby.
     */
    private final Player player;

    /**
     * The ID of the new Player
     */
    private final int id;

    /**
     * The flag is the new player is the host
     */
    private final boolean host;

    /**
     * Constructs a new LobbyPlayerJoin instance with the specified player name.
     *
     * @param player the player joining the lobby
     * @param id     the id of the player
     * @param host   as the flag if the player is the host as a Boolean.
     */
    public LobbyPlayerJoinedMessage(int id, Player player, boolean host) {
        super();
        this.player = player;
        this.id = id;
        this.host = host;
    }

    /**
     * Default constructor for serialization purposes.
     */
    private LobbyPlayerJoinedMessage() {
        super();
        player = null;
        id = 0;
        host = false;
    }

    /**
     * Returns the player joining the lobby.
     *
     * @return the player joining the lobby
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Returns the id of the new Player
     *
     * @return the id of the player
     */
    public int getId() {
        return id;
    }

    public boolean isHost() {
        return this.host;
    }

    /**
     * Accepts a visitor to process this message.
     *
     * @param interpreter the visitor to process this message
     */
    @Override
    public void accept(ServerInterpreter interpreter) {
        interpreter.received(this);
    }

    @Override
    public String toString() {
        return "LobbyPlayerJoinedMessage{" + "player=" + player + ", id=" + id + ", host=" + host + '}';
    }
}
