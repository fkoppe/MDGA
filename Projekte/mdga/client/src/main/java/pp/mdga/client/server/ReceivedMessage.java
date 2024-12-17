package pp.mdga.client.server;

import pp.mdga.message.client.ClientInterpreter;
import pp.mdga.message.client.ClientMessage;

/**
 * Represents a message received from the server.
 */
public record ReceivedMessage(ClientMessage msg, int from) {

    /**
     * Processes the received message using the specified client interpreter.
     *
     * @param interpreter the client interpreter to use for processing the message
     */
    void process(ClientInterpreter interpreter) {
        msg.accept(interpreter, from);
    }
}
