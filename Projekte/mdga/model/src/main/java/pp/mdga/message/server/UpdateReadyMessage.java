package pp.mdga.message.server;

import com.jme3.network.serializing.Serializable;

/**
 * A message sent by the server to every client to update the readiness status of a player.
 */
@Serializable
public class UpdateReadyMessage extends ServerMessage {
    /**
     * The color associated with the update.
     */
    private final int playerId;

    /**
     * Indicates whether the player is ready.
     */
    private final boolean ready;

    /**
     * Constructs a new UpdateReady instance with the specified color and readiness status.
     *
     * @param playerId the playerId associated with the update
     * @param ready    the readiness status
     */
    public UpdateReadyMessage(int playerId, boolean ready) {
        super();
        this.playerId = playerId;
        this.ready = ready;
    }

    /**
     * Default constructor for serialization purposes.
     */
    private UpdateReadyMessage() {
        super();
        this.playerId = 0;
        this.ready = false;
    }

    /**
     * Gets the playerId associated with the update.
     *
     * @return the playerId
     */
    public int getPlayerId() {
        return playerId;
    }

    /**
     * Checks if the player is ready.
     *
     * @return true if the player is ready, false otherwise
     */
    public boolean isReady() {
        return ready;
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
        return "UpdateReadyMessage{" + "playerId=" + playerId + ", ready=" + ready + '}';
    }
}
