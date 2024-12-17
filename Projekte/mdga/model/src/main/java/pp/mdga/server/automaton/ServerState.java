package pp.mdga.server.automaton;

import pp.mdga.message.client.*;
import pp.mdga.message.server.LobbyPlayerLeaveMessage;
import pp.mdga.message.server.ShutdownMessage;
import pp.mdga.server.ServerGameLogic;

/**
 * Defines the behavior and state transitions for the server-side game logic.
 * Different states of the game logic implement this interface to handle various game events and actions.
 */
public abstract class ServerState {
    /**
     * The server logic object.
     */
    protected final ServerGameLogic logic;

    /**
     * Constructs a server state of the specified game logic.
     *
     * @param logic the game logic
     */
    public ServerState(ServerGameLogic logic) {
        this.logic = logic;
    }

    /**
     * This method will be used whenever this state will be entered.
     */
    public abstract void enter();

    /**
     * This method will be used whenever this state will be exited.
     */
    public abstract void exit();

    /**
     * This method will be called whenever the server received an AnimationEndMessage message.
     * It will also get the client id of the player who send this message.
     *
     * @param msg  as the message which was sent by the player as a AnimationEndMessage object.
     * @param from as the client id of the player as an Integer.
     */
    public void received(AnimationEndMessage msg, int from) {
    }

    /**
     * This method will be called whenever the server received a DeselectTSKMessage message.
     * It will also get the client id of the player who send this message.
     *
     * @param msg  as the message which was sent by the player as a DeselectTSKMessage object.
     * @param from as the client id of the player as an Integer.
     */
    public void received(DeselectTSKMessage msg, int from) {
    }

    /**
     * This method will be called whenever the server received a StartGame message.
     * It will also get the client id of the player who send this message.
     *
     * @param msg  as the message which was sent by the player as a StartGame object.
     * @param from as the client id of the player as an Integer.
     */
    public void received(StartGameMessage msg, int from) {
    }

    /**
     * This method will be called whenever the server received a JoinedLobbyMessage message.
     * It will also get the client id of the player who send this message.
     *
     * @param msg  as the message which was sent by the player as a JoinedLobbyMessage object.
     * @param from as the client id of the player as an Integer.
     */
    public void received(JoinedLobbyMessage msg, int from) {
    }

    /**
     * This method will be called whenever the server received an LeaveGameMessage message.
     * It will also get the client id of the player who send this message.
     *
     * @param msg  as the message which was sent by the player as a LeaveGameMessage object.
     * @param from as the client id of the player as an Integer.
     */
    public void received(LeaveGameMessage msg, int from) {
        if (from == this.logic.getGame().getHost()) {
            this.logic.getServerSender().broadcast(new ShutdownMessage());
            this.logic.getServerSender().shutdown();
        }
        this.logic.getServerSender().disconnectClient(from);
        this.logic.getGame().removePlayer(from);
        this.logic.getServerSender().broadcast(new LobbyPlayerLeaveMessage(from));
    }

    /**
     * This method will be called whenever the server received a LobbyReadyMessage message.
     * It will also get the client id of the player who send this message.
     *
     * @param msg  as the message which was sent by the player as a LobbyReadyMessage object.
     * @param from as the client id of the player as an Integer.
     */
    public void received(LobbyReadyMessage msg, int from) {
    }

    /**
     * This method will be called whenever the server received a LobbyNotReadyMessage message.
     * It will also get the client id of the player who send this message.
     *
     * @param msg  as the message which was sent by the player as a LobbyNotReadyMessage object.
     * @param from as the client id of the player as an Integer.
     */
    public void received(LobbyNotReadyMessage msg, int from) {
    }

    /**
     * This method will be called whenever the server received a DisconnectedMessage message.
     * It will also get the client id of the player who send this message.
     *
     * @param msg  as the message which was sent by the player as a DisconnectedMessage object.
     * @param from as the client id of the player as an Integer.
     */
    public void received(DisconnectedMessage msg, int from) {
    }

    /**
     * This method will be called whenever the server received a RequestBriefingMessage message.
     * It will also get the client id of the player who send this message.
     *
     * @param msg  as the message which was sent by the player as a RequestBriefingMessage object.
     * @param from as the client id of the player as an Integer.
     */
    public void received(RequestBriefingMessage msg, int from) {
    }

    /**
     * This method will be called whenever the server received a RequestDieMessage message.
     * It will also get the client id of the player who send this message.
     *
     * @param msg  as the message which was sent by the player as a RequestDieMessage object.
     * @param from as the client id of the player as an Integer.
     */
    public void received(RequestDieMessage msg, int from) {
    }

    /**
     * This method will be called whenever the server received a RequestMoveMessage message.
     * It will also get the client id of the player who send this message.
     *
     * @param msg  as the message which was sent by the player as a RequestMoveMessage object.
     * @param from as the client id of the player as an Integer.
     */
    public void received(RequestMoveMessage msg, int from) {
    }

    /**
     * This method will be called whenever the server received a RequestPlayCardMessage message.
     * It will also get the client id of the player who send this message.
     *
     * @param msg  as the message which was sent by the player as a RequestPlayCardMessage object.
     * @param from as the client id of the player as an Integer.
     */
    public void received(RequestPlayCardMessage msg, int from) {
    }

    /**
     * This method will be called whenever the server received a SelectCardMessage message.
     * It will also get the client id of the player who send this message.
     *
     * @param msg  as the message which was sent by the player as a SelectCardMessage object.
     * @param from as the client id of the player as an Integer.
     */
    public void received(SelectCardMessage msg, int from) {
    }

    /**
     * This method will be called whenever the server received a SelectTSKMessage message.
     * It will also get the client id of the player who send this message.
     *
     * @param msg  as the message which was sent by the player as a SelectTSKMessage object.
     * @param from as the client id of the player as an Integer.
     */
    public void received(SelectTSKMessage msg, int from) {
    }

    /**
     * This method will be called whenever the server received a ForceContinueGameMessage message.
     * It will also get the client id of the player who send this message.
     *
     * @param msg  as the message which was sent by the player as a ForceContinueGameMessage object.
     * @param from as the client id of the player as an Integer.
     */
    public void received(ForceContinueGameMessage msg, int from) {
    }

    /**
     * This method will be called whenever the server received a ClientStartGameMessage message.
     * It will also get the client id of the player who send this message.
     *
     * @param msg  as the message which was sent by the player as a ClientStartGameMessage object.
     * @param from as the client id of the player as an Integer.
     */
    public void received(ClientStartGameMessage msg, int from) {
    }

    /**
     * This method will be called whenever the server received a NoPowerCardMessage message.
     * It will also get the client id of the player who send this message.
     *
     * @param msg  as the message which was sent by the player as a NoPowerCardMessage object.
     * @param from as the client id of the player as an Integer.
     */
    public void received(NoPowerCardMessage msg, int from) {
    }

    /**
     * This method will be called whenever the server received a SelectedPiecesMessage message.
     * It will also get the client id of the player who send this message.
     *
     * @param msg  as the message which was sent by the player as a SelectedPiecesMessage object.
     * @param from as the client id of the player as an Integer.
     */
    public void received(SelectedPiecesMessage msg, int from) {
    }
}
