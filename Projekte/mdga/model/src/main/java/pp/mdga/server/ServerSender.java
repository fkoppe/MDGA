package pp.mdga.server;

import pp.mdga.message.server.ServerMessage;

/**
 * Interface for sending messages to a client.
 */
public interface ServerSender {
    /**
     * Send the specified message to the client.
     *
     * @param id      the id of the client that shall receive the message
     * @param message the message
     */
    void send(int id, ServerMessage message);

    /**
     * This method will be used to send the given message parameter to all connected players which are saved inside the
     * players attribute of Game class.
     *
     * @param message as the message which will be sent to all players as a ServerMessage.
     */
    void broadcast(ServerMessage message);

    /**
     * This method will be used to disconnect the client depending on the given id parameter.
     *
     * @param id as the connection id of the client as an Integer.
     */
    void disconnectClient(int id);

    /**
     * This method will be used to shut down the server.
     */
    void shutdown();
}
