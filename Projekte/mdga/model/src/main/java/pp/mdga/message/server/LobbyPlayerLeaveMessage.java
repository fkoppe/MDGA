package pp.mdga.message.server;

import com.jme3.network.serializing.Serializable;

/**
 * A message sent by the server to indicate that a player has left the lobby.
 */
@Serializable
public class LobbyPlayerLeaveMessage extends ServerMessage {
    /**
     * The name of the player leaving the lobby.
     */
    private final int id;

    /**
     * Constructs a new LobbyPlayerLeave instance with the specified player name and color.
     *
     * @param id the id of the player leaving the lobby.
     */
    public LobbyPlayerLeaveMessage(int id) {
        super();
        this.id = id;
    }

    /**
     * Default constructor for serialization purposes.
     */
    private LobbyPlayerLeaveMessage() {
        super();
        id = 0;
    }

    /**
     * Returns the name of the player leaving the lobby.
     *
     * @return the name of the player leaving the lobby
     */
    public int getId() {
        return id;
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

    /**
     * Returns a string representation of this message.
     *
     * @return a string representation of this message
     */
    @Override
    public String toString() {
        return "LobbyPlayerLeaveMessage{" + "id=" + id + '}';
    }
}
