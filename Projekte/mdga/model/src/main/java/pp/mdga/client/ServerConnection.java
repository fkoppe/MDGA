package pp.mdga.client;

public interface ServerConnection extends ClientSender {
    boolean isConnected();

    void connect();

    void disconnect();
}
