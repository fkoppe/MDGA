package pp.mdga.message.client;

import com.jme3.network.serializing.Serializable;

/**
 * A message sent by a client to request a briefing from the server. (after a reconnect)
 */
@Serializable
public class RequestBriefingMessage extends ClientMessage {
    /**
     * Constructs a new RequestBriefing instance.
     */
    public RequestBriefingMessage() {
        super();
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
