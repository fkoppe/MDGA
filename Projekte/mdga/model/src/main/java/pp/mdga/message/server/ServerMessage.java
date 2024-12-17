package pp.mdga.message.server;

import com.jme3.network.AbstractMessage;

/**
 * An abstract base class for server messages used in network transfer.
 * It extends the AbstractMessage class provided by the jme3-network library.
 */
public abstract class ServerMessage extends AbstractMessage {
    /**
     * Constructs a new ServerMessage instance.
     */
    protected ServerMessage() {
        super(true);
    }

    /**
     * Accepts a visitor to process this message.
     *
     * @param interpreter the visitor to process this message
     */
    public abstract void accept(ServerInterpreter interpreter);

    /**
     * Returns a string representation of this message.
     *
     * @return a string representation of this message
     */
    @Override
    public String toString() {
        return getClass().getSimpleName() + "{}";
    }
}
