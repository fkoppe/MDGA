package pp.mdga.client.dialogstate;

import pp.mdga.Resources;
import pp.mdga.client.ClientGameLogic;
import pp.mdga.client.ClientState;
import pp.mdga.client.DialogsState;
import pp.mdga.message.server.LobbyAcceptMessage;
import pp.mdga.message.server.LobbyDenyMessage;
import pp.mdga.notification.InfoNotification;
import pp.mdga.notification.LobbyDialogNotification;

public class NetworkDialogState extends DialogStates {

    private final DialogsState parent;

    /**
     * Constructor for the NetworkDialogState
     *
     * @param parent the parent state
     * @param logic  the logic
     */
    public NetworkDialogState(ClientState parent, ClientGameLogic logic) {
        super(parent, logic);
        this.parent = (DialogsState) parent;
    }

    /**
     * Enter the state
     */
    @Override
    public void enter() {
        LOGGER.log(System.Logger.Level.INFO, "Entered {0}", this);
    }

    /**
     * Exit the state
     */
    @Override
    public void exit() {
    }

    /**
     * Select the leave option
     */
    @Override
    public void selectLeave() {
        parent.setState(parent.getStartDialog());
    }

    /**
     * This method is called when the server accepts the client into the lobby
     *
     * @param msg the LobbyAcceptMessage
     */
    @Override
    public void received(LobbyAcceptMessage msg) {
        logic.getGame().setHost(msg.getHost());
        logic.setHost(logic.getGame().isHost());
        parent.setState(parent.getLobby());
        logic.addNotification(new LobbyDialogNotification());
    }

    /**
     * This method is called when the server denies the client into the lobby
     *
     * @param msg the LobbyDenyMessage
     */
    @Override
    public void received(LobbyDenyMessage msg) {
        logic.addNotification(new InfoNotification(Resources.stringLookup("lobby.deny.join")));
        parent.setState(parent.getStartDialog());
    }
}
