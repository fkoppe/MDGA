package pp.mdga.client;

import com.jme3.network.*;
import pp.mdga.message.client.ClientMessage;
import pp.mdga.message.server.ServerMessage;

import java.io.IOException;

/**
 * The NetworkSupport class provides support for network communication between the client and server.
 * It implements the MessageListener and ClientStateListener interfaces to handle incoming messages
 * and client state changes, respectively.
 */
public class NetworkSupport implements MessageListener<Client>, ClientStateListener, ServerConnection {

    private static final System.Logger LOGGER = System.getLogger(NetworkSupport.class.getName());
    private final MdgaApp app;
    private Client client;

    /**
     * Constructor for NetworkSupport.
     *
     * @param app the MdgaApp instance
     */
    public NetworkSupport(MdgaApp app) {
        this.app = app;
    }

    /**
     * Returns the MdgaApp instance.
     *
     * @return the MdgaApp instance
     */
    public MdgaApp getApp() {
        return this.app;
    }

    /**
     * Returns whether the client is connected to the server.
     *
     * @return true if the client is connected, false otherwise
     */
    public boolean isConnected() {
        return this.client != null && this.client.isConnected();
    }

    /**
     * Connects the client to the server.
     */
    public void connect() {
        if (this.client != null) {
            throw new IllegalStateException("trying to join a game again");
        } else {
            try {
                this.initNetwork("localhost", 2345);
            } catch (IOException e) {
                LOGGER.log(System.Logger.Level.ERROR, "could not connect to server", e);
            }
        }

    }

    /**
     * Disconnects the client from the server.
     */
    public void disconnect() {
        if (this.client != null) {
            this.client.close();
            this.client = null;
            LOGGER.log(System.Logger.Level.INFO, "client closed");
        }
    }

    /**
     * Initializes the network connection to the server.
     *
     * @param host the server host
     * @param port the server port
     * @throws IOException if an I/O error occurs
     */
    public void initNetwork(String host, int port) throws IOException {
        if (this.client != null) {
            throw new IllegalStateException("trying to join a game again");
        } else {
            this.client = Network.connectToServer(host, port);
            this.client.start();
            this.client.addMessageListener(this);
            this.client.addClientStateListener(this);
        }
    }

    /**
     * Handles incoming messages from the server.
     *
     * @param client the client
     * @param message the message
     */
    public void messageReceived(Client client, Message message) {
        LOGGER.log(System.Logger.Level.INFO, "message received from server: {0}", new Object[]{message});
        if (message instanceof ServerMessage serverMessage) {
            this.app.enqueue(() -> serverMessage.accept(this.app.getGameLogic()));
        }

    }

    /**
     * Handles client connection to the server.
     *
     * @param client the client
     */
    public void clientConnected(Client client) {
        LOGGER.log(System.Logger.Level.INFO, "Client connected: {0}", new Object[]{client});
    }

    /**
     * Handles client disconnection from the server.
     *
     * @param client the client
     * @param disconnectInfo the disconnect information
     */
    public void clientDisconnected(Client client, ClientStateListener.DisconnectInfo disconnectInfo) {
        LOGGER.log(System.Logger.Level.INFO, "Client {0} disconnected: {1}", new Object[]{client, disconnectInfo});
        if (this.client != client) {
            throw new IllegalArgumentException("parameter value must be client");
        } else {
            LOGGER.log(System.Logger.Level.INFO, "client still connected: {0}", new Object[]{client.isConnected()});
            this.client = null;
            this.disconnect();
        }
    }

    /**
     * Sends a message to the server.
     *
     * @param message the message
     */
    @Override
    public void send(ClientMessage message) {
        LOGGER.log(System.Logger.Level.INFO, "sending {0}", new Object[]{message});
        if (this.client == null) {
            LOGGER.log(System.Logger.Level.WARNING, "client not connected");
        } else {
            this.client.send(message);
        }

    }
}
