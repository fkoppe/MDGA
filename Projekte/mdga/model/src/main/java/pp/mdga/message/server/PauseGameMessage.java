package pp.mdga.message.server;

import com.jme3.network.serializing.Serializable;

/**
 * A message sent by the server to indicate that the game is paused.
 */
@Serializable
public class PauseGameMessage extends ServerMessage {
    /**
     * the id of the player who has disconnected
     */
    private int playerId;

    /**
     * Constructs a new PauseGame instance.
     */
    public PauseGameMessage() {
        super();
    }

    /**
     * Constructs a new PauseGame instance.
     */
    public PauseGameMessage(int playerId) {
        super();
        this.playerId = playerId;
    }

    /**
     * Returns the player id of the disconnected player
     *
     * @return the id of the disconnected player as an int
     */
    public int getPlayerId() {
        return playerId;
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
        return "PauseGameMessage{" + "playerId=" + playerId + '}';
    }
}
