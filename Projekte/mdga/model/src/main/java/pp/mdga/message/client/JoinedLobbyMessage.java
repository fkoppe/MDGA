package pp.mdga.message.client;

import com.jme3.network.serializing.Serializable;

/**
 * A message sent by a client when joining the server.
 */
@Serializable
public class JoinedLobbyMessage extends ClientMessage {
    /**
     * The name of the player that is joining the server.
     */
    private final String name;

    /**
     * Constructs a new JoinServer instance.
     */
    public JoinedLobbyMessage(String name) {
        super();
        this.name = name;
    }

    /**
     * Constructs a new JoinServer instance.
     */
    public JoinedLobbyMessage() {
        super();
        name = null;
    }

    /**
     * Returns the name of the player that is joining the server.
     *
     * @return the name of the player that is joining the server
     */
    public String getName() {
        return name;
    }

    /**
     * Returns a string representation of this message.
     *
     * @return a string representation of this message
     */
    @Override
    public String toString() {
        return "JoinServer {" + "name=" + name + '}';
    }

    /**
     * Accepts a visitor to process this message.
     *
     * @param interpreter the visitor to process this message
     * @param from        the connection ID from which the message was received
     */
    @Override
    public void accept(ClientInterpreter interpreter, int from) {
        interpreter.received(this, from);
    }
}
