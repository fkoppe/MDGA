package pp.mdga.message.server;

import com.jme3.network.serializing.Serializable;

/**
 * A message sent by the server to indicate that the client has been accepted into the lobby.
 */
@Serializable
public class LobbyAcceptMessage extends ServerMessage {
    /**
     * Create LobbyAcceptMessage attributes.
     */
    private final int host;

    /**
     * Constructs a new LobbyAccept instance.
     */
    public LobbyAcceptMessage() {
        super();
        this.host = -1;
    }

    /**
     * Constructor.
     *
     * @param host as the id of the host as an Integer.
     */
    public LobbyAcceptMessage(int host) {
        super();
        this.host = host;
    }

    /**
     * This method will be used return host attribute of LobbyAcceptMessage class.
     *
     * @return host as an Integer.
     */
    public int getHost() {
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

    /**
     * This method will be used to return necessary class information in a more readable format.
     *
     * @return information as a String.
     */
    @Override
    public String toString() {
        return "LobbyAcceptMessage with host: %d".formatted(this.host);
    }
}
