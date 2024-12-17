package pp.mdga.message.client;

import com.jme3.network.AbstractMessage;

/**
 * An abstract base class for client messages used in network transfer.
 * It extends the AbstractMessage class provided by the jme3-network library.
 */
public abstract class ClientMessage extends AbstractMessage {
    /**
     * Constructs a new ClientMessage instance.
     */
    protected ClientMessage() {
        super(true);
    }

    /**
     * Accepts a visitor for processing this message.
     *
     * @param interpreter the visitor to be used for processing
     * @param from        the connection ID of the sender
     */
    public abstract void accept(ClientInterpreter interpreter, int from);

    /**
     * Returns a string representation of this object.
     *
     * @return a string representation of this object
     */
    public String toString() {
        return getClass().getSimpleName() + "{}";
    }
}
