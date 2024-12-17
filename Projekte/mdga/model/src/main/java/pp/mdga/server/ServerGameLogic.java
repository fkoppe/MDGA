package pp.mdga.server;

import pp.mdga.game.Game;
import pp.mdga.message.client.*;
import pp.mdga.server.automaton.*;

import java.lang.System.Logger;

/**
 *
 */
public class ServerGameLogic implements ClientInterpreter {
    /**
     * Constants.
     */
    private static final Logger LOGGER = System.getLogger(ServerGameLogic.class.getName());

    /**
     * Attributes.
     */
    private final ServerSender serverSender;
    private final Game game;

    /**
     * States
     */
    private ServerState currentState;
    private final LobbyState lobbyState;
    private final GameState gameState;
    private final InterruptState interruptState;
    private final CeremonyState ceremonyState;

    /**
     * Constructor.
     *
     * @param serverSender
     * @param game
     */
    public ServerGameLogic(ServerSender serverSender, Game game) {
        this.serverSender = serverSender;
        this.game = game;
        this.lobbyState = new LobbyState(this);
        this.gameState = new GameState(this);
        this.interruptState = new InterruptState(this);
        this.ceremonyState = new CeremonyState(this);
        this.currentState = this.lobbyState;
    }


    /**
     * Handles the reception of an AnimationEndMessage.
     *
     * @param msg  the received AnimationEndMessage
     * @param from the sender of the message
     */
    @Override
    public void received(AnimationEndMessage msg, int from) {
        this.currentState.received(msg, from);
    }

    /**
     * Handles the reception of a DeselectTSKMessage.
     *
     * @param msg  the received DeselectTSKMessage
     * @param from the sender of the message
     */
    @Override
    public void received(DeselectTSKMessage msg, int from) {
        this.currentState.received(msg, from);
    }

    /**
     * Handles the reception of a StartGameMessage.
     *
     * @param msg  the received StartGameMessage
     * @param from the sender of the message
     */
    @Override
    public void received(StartGameMessage msg, int from) {
        this.currentState.received(msg, from);
    }

    /**
     * Handles the reception of a JoinedLobbyMessage.
     *
     * @param msg  the received JoinedLobbyMessage
     * @param from the sender of the message
     */
    @Override
    public void received(JoinedLobbyMessage msg, int from) {
        this.currentState.received(msg, from);
    }

    /**
     * Handles the reception of a LeaveGameMessage.
     *
     * @param msg  the received LeaveGameMessage
     * @param from the sender of the message
     */
    @Override
    public void received(LeaveGameMessage msg, int from) {
        this.currentState.received(msg, from);
    }

    /**
     * Handles the reception of a LobbyReadyMessage.
     *
     * @param msg  the received LobbyReadyMessage
     * @param from the sender of the message
     */
    @Override
    public void received(LobbyReadyMessage msg, int from) {
        this.currentState.received(msg, from);
    }

    /**
     * Handles the reception of a LobbyNotReadyMessage.
     *
     * @param msg  the received LobbyNotReadyMessage
     * @param from the sender of the message
     */
    @Override
    public void received(LobbyNotReadyMessage msg, int from) {
        this.currentState.received(msg, from);
    }

    /**
     * Handles the reception of a DisconnectedMessage.
     *
     * @param msg  the received DisconnectedMessage
     * @param from the sender of the message
     */
    @Override
    public void received(DisconnectedMessage msg, int from) {
        this.currentState.received(msg, from);
    }

    /**
     * Handles the reception of a RequestBriefingMessage.
     *
     * @param msg  the received RequestBriefingMessage
     * @param from the sender of the message
     */
    @Override
    public void received(RequestBriefingMessage msg, int from) {
        this.currentState.received(msg, from);
    }

    /**
     * Handles the reception of a RequestDieMessage.
     *
     * @param msg  the received RequestDieMessage
     * @param from the sender of the message
     */
    @Override
    public void received(RequestDieMessage msg, int from) {
        this.currentState.received(msg, from);
    }

    /**
     * Handles the reception of a RequestMoveMessage.
     *
     * @param msg  the received RequestMoveMessage
     * @param from the sender of the message
     */
    @Override
    public void received(RequestMoveMessage msg, int from) {
        this.currentState.received(msg, from);
    }

    /**
     * Handles the reception of a RequestPlayCardMessage.
     *
     * @param msg  the received RequestPlayCardMessage
     * @param from the sender of the message
     */
    @Override
    public void received(RequestPlayCardMessage msg, int from) {
        this.currentState.received(msg, from);
    }

    /**
     * Handles the reception of a SelectCardMessage.
     *
     * @param msg  the received SelectCardMessage
     * @param from the sender of the message
     */
    @Override
    public void received(SelectCardMessage msg, int from) {
        this.currentState.received(msg, from);
    }

    /**
     * Handles the reception of a SelectTSKMessage.
     *
     * @param msg  the received SelectTSKMessage
     * @param from the sender of the message
     */
    @Override
    public void received(SelectTSKMessage msg, int from) {
        this.currentState.received(msg, from);
    }

    /**
     * Handles the reception of a ForceContinueGameMessage.
     *
     * @param msg  the received ForceContinueGameMessage
     * @param from the sender of the message
     */
    @Override
    public void received(ForceContinueGameMessage msg, int from) {
        this.currentState.received(msg, from);
    }

    /**
     * Handles the reception of a ClientStartGameMessage.
     *
     * @param msg  the received ClientStartGameMessage
     * @param from the sender of the message
     */
    @Override
    public void received(ClientStartGameMessage msg, int from) {
        this.currentState.received(msg, from);
    }

    /**
     * Handles the reception of a NoPowerCardMessage.
     *
     * @param msg  the received NoPowerCardMessage
     * @param from the sender of the message
     */
    @Override
    public void received(NoPowerCardMessage msg, int from) {
        this.currentState.received(msg, from);
    }

    /**
     * Handles the reception of a SelectedPiecesMessage.
     *
     * @param msg  the received SelectedPiecesMessage
     * @param from the sender of the message
     */
    @Override
    public void received(SelectedPiecesMessage msg, int from) {
        this.currentState.received(msg, from);
    }

    /**
     * This method will be used to return serverSender attribute of ServerGameLogic class.
     *
     * @return serverSender as a ServerSender object.
     */
    public ServerSender getServerSender() {
        return this.serverSender;
    }

    /**
     * This method will be used to return game attribute of ServerGameLogic class.
     *
     * @return game as a Game object.
     */
    public Game getGame() {
        return this.game;
    }

    /**
     * This method will be used to return currentState attribute of ServerGameLogic class.
     *
     * @return currentState as a ServerState object.
     */
    public ServerState getCurrentState() {
        return this.currentState;
    }

    /**
     * This method will be used to return lobbyState attribute of ServerGameLogic class.
     *
     * @return lobbyState as a LobbyState object.
     */
    public LobbyState getLobbyState() {
        return this.lobbyState;
    }

    /**
     * This method will be used to return gamestate attribute of ServerGameLogic class.
     *
     * @return gamestate as a GameState object.
     */
    public GameState getGameState() {
        return this.gameState;
    }

    /**
     * This method will be used to return interruptState attribute of ServerGameLogic class.
     *
     * @return interruptState as a InterruptState object.
     */
    public InterruptState getInterruptState() {
        return this.interruptState;
    }

    /**
     * This method will be used to return ceremonystate attribute of ServerGameLogic class.
     *
     * @return ceremonystate as a CeremonyState object.
     */
    public CeremonyState getCeremonyState() {
        return this.ceremonyState;
    }

    /**
     * This method will be used to set currentState attribute of ServerGameLogic class to the given state parameter.
     * In Addition, the currentState will be exited, changed and entered.
     *
     * @param state as the new currentState attribute as a ServerState object.
     */
    public void setCurrentState(ServerState state) {
        System.out.println("SERVER STATE old: " + this.currentState + " new: " + state);

        if (this.currentState != null) {
            this.currentState.exit();
        }
        this.currentState = state;
        this.currentState.enter();
    }
}
