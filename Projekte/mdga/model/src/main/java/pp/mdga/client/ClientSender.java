package pp.mdga.client;

import pp.mdga.message.client.ClientMessage;

public interface ClientSender {
    void send(ClientMessage msg);
}
