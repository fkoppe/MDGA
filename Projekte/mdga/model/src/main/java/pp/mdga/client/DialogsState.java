package pp.mdga.client;

import pp.mdga.client.dialogstate.DialogStates;
import pp.mdga.client.dialogstate.LobbyState;
import pp.mdga.client.dialogstate.NetworkDialogState;
import pp.mdga.client.dialogstate.StartDialogState;
import pp.mdga.game.Color;
import pp.mdga.message.server.*;

public class DialogsState extends ClientState {

    private DialogStates currentState;

    private final LobbyState lobbyState = new LobbyState(this, logic);
    private final NetworkDialogState networkDialogState = new NetworkDialogState(this, logic);
    private final StartDialogState startDialogState = new StartDialogState(this, logic);

    /**
     * Creates a new DialogsState
     *
     * @param parent the parent state
     * @param logic  the game logic
     */
    public DialogsState(ClientState parent, ClientGameLogic logic) {
        super(parent, logic);
    }

    /**
     * exits this state
     */
    @Override
    public void exit() {
        currentState.exit();
    }

    /**
     * Enters the new state machine
     */
    @Override
    public void enter() {
        setState(startDialogState);
    }

    /**
     * This method is used to set a new SubState
     *
     * @param newState the state to be set
     */
    public void setState(DialogStates newState) {
        if (currentState != null) {
            currentState.exit();
        }
        newState.enter();
        currentState = newState;
    }

    /**
     * This method is used to get the lobbyState
     *
     * @return the lobbyState
     */
    public LobbyState getLobby() {
        return lobbyState;
    }

    /**
     * This method is used to get the networkDialogState
     *
     * @return the networkDialogState
     */
    public NetworkDialogState getNetworkDialog() {
        return networkDialogState;
    }

    /**
     * This method is used to get the startDialogState
     *
     * @return the startDialogState
     */
    public StartDialogState getStartDialog() {
        return startDialogState;
    }

    /**
     * This method is used to call the selectLeave method of the current state
     */
    @Override
    public void selectLeave() {
        currentState.selectLeave();
    }

    /**
     * This method is used to call the selectName method of the current state
     *
     * @param name the name to be set
     */
    @Override
    public void setName(String name) {
        currentState.setName(name);
    }

    /**
     * This method is used to call the selectTSK method of the current state
     *
     * @param color the color to be set
     */
    @Override
    public void selectTSK(Color color) {
        currentState.selectTSK(color);
    }

    /**
     * This method is used to call the deselectTSK method of the current state
     *
     * @param color the color to be deselected
     */
    @Override
    public void deselectTSK(Color color) {
        currentState.deselectTSK(color);
    }

    /**
     * This method is used to call the selectReady method of the current state
     */
    @Override
    public void selectReady() {
        currentState.selectReady();
    }

    /**
     * This method is used to call the selectUnready method of the current state
     */
    @Override
    public void selectUnready() {
        currentState.selectUnready();
    }

    /**
     * This method is used to call the selectStart method of the current state
     */
    @Override
    public void selectStart() {
        currentState.selectStart();
    }

    /**
     * This method is used to call the selectJoin method of the current state
     *
     * @param string the string to be set
     */
    @Override
    public void selectJoin(String string) {
        currentState.selectJoin(string);
    }

    /**
     * This method is used to call the selectHost method of the current state
     *
     * @param name the name to be set
     */
    @Override
    public void selectHost(String name) {
        currentState.selectHost(name);
    }

    /**
     * This method is used to call the received method of the current state
     *
     * @param msg the LobbyPlayerJoin message received
     */
    @Override
    public void received(LobbyPlayerJoinedMessage msg) {
        currentState.received(msg);
    }

    /**
     * This method is used to call the received method of the current state
     *
     * @param msg the LobbyPlayerLeave message received
     */
    @Override
    public void received(LobbyPlayerLeaveMessage msg) {
        currentState.received(msg);
    }

    /**
     * This method is used to call the received method of the current state
     *
     * @param msg the UpdateTSKMessage message received
     */
    @Override
    public void received(UpdateTSKMessage msg) {
        currentState.received(msg);
    }

    /**
     * This method is used to call the received method of the current state
     *
     * @param msg the UpdateReady message received
     */
    @Override
    public void received(UpdateReadyMessage msg) {
        currentState.received(msg);
    }

    /**
     * This method is used to call the received method of the current state
     *
     * @param msg the ServerStartGame message received
     */
    @Override
    public void received(ServerStartGameMessage msg) {
        currentState.received(msg);
    }

    /**
     * This method is used to call the received method of the current state
     *
     * @param msg the LobbyAccept message received
     */
    @Override
    public void received(LobbyAcceptMessage msg) {
        currentState.received(msg);
    }

    /**
     * This method is used to call the received method of the current state
     *
     * @param msg the LobbyDeny message received
     */
    @Override
    public void received(LobbyDenyMessage msg) {
        currentState.received(msg);
    }

    /**
     * This method is used to get the current state
     */
    public DialogStates getState() {
        return currentState;
    }
}
