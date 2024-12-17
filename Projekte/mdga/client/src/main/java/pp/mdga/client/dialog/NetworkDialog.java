package pp.mdga.client.dialog;

import com.jme3.scene.Node;
import pp.mdga.client.MdgaApp;
import pp.mdga.client.NetworkSupport;
import pp.mdga.client.server.MdgaServer;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * The {@code NetworkDialog} class serves as an abstract base class for dialogs
 * that involve network-related functionalities, such as connecting to a server or hosting a game.
 * It provides methods for initializing, connecting to, and managing a network server.
 */
public abstract class NetworkDialog extends Dialog {

    private NetworkSupport network;
    private String hostname;
    private int portNumber;
    private Future<Object> connectionFuture;
    private MdgaServer serverInstance;
    private Thread serverThread;

    /**
     * Constructs a {@code NetworkDialog}.
     *
     * @param app     The main application managing the dialog.
     * @param node    The root node for attaching UI elements.
     * @param network The network support instance for managing network interactions.
     */
    public NetworkDialog(MdgaApp app, Node node, NetworkSupport network) {
        super(app, node);
        this.network = network;
    }

    /**
     * Sets the hostname for the network connection.
     *
     * @param hostname The hostname or IP address of the server.
     */
    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    /**
     * Sets the port number for the network connection.
     *
     * @param portNumber The port number to use for the connection.
     */
    public void setPortNumber(int portNumber) {
        this.portNumber = portNumber;
    }

    /**
     * Initializes the network connection using the current hostname and port number.
     *
     * @return {@code null} if successful, otherwise throws an exception.
     */
    protected Object initNetwork() {
        try {
            this.network.initNetwork(this.hostname, this.portNumber);
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Starts the process of connecting to a server asynchronously.
     */
    protected void connectServer() {
        try {
            connectionFuture = this.network.getApp().getExecutor().submit(this::initNetwork);
        } catch (NumberFormatException var2) {
            throw new NumberFormatException("Port must be a number");
        }
    }

    /**
     * Starts hosting a server in a separate thread.
     */
    protected void startServer() {
        serverThread = new Thread(() -> {
            try {
                serverInstance = new MdgaServer(portNumber);
                serverInstance.run();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        serverThread.start();
    }

    /**
     * Shuts down the hosted server and cleans up resources.
     */
    public void shutdownServer() {

        serverInstance.shutdown();

        // Wait for the server to shut down
        try {
            serverThread.join(); // Wait for the server thread to finish
            System.out.println("Server shutdown successfully.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Updates the state of the connection process.
     *
     * @param delta The time elapsed since the last update call.
     */
    public void update(float delta) {
        if (this.connectionFuture != null && this.connectionFuture.isDone()) {
            try {
                this.connectionFuture.get();
            } catch (ExecutionException ignored) {
                // TODO: implement
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Retrieves the {@code NetworkSupport} instance associated with this dialog.
     *
     * @return The {@code NetworkSupport} instance.
     */
    public NetworkSupport getNetwork() {
        return network;
    }
}
