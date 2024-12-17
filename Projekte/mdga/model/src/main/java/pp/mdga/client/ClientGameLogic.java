package pp.mdga.client;

import pp.mdga.Resources;
import pp.mdga.game.BonusCard;
import pp.mdga.game.Color;
import pp.mdga.game.Game;
import pp.mdga.game.Piece;
import pp.mdga.message.client.ClientMessage;
import pp.mdga.message.server.*;
import pp.mdga.notification.InfoNotification;
import pp.mdga.notification.Notification;
import pp.mdga.notification.StartDialogNotification;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.ArrayList;
import java.util.UUID;

/**
 * The ClientGameLogic class is the main class for the client side of the game.
 * It is responsible for handling the game logic on the client side.
 */
public class ClientGameLogic implements ServerInterpreter {
    static final Logger LOGGER = System.getLogger(ClientGameLogic.class.getName());

    private Game game;
    private final ClientSender clientSender;
    private ClientState state;
    private final ArrayList<Notification> notifications = new ArrayList<>();
    private boolean isHost;
    private int ownPlayerID;
    private String ownPlayerName;

    private final DialogsState dialogsState = new DialogsState(null, this);
    private final GameState gameState = new GameState(null, this);
    private final CeremonyState ceremonyState = new CeremonyState(null, this);
    private final InterruptState interruptState = new InterruptState(null, this);
    private final SettingsState settingsState = new SettingsState(null, this);

    /**
     * Creates a new ClientGameLogic
     *
     * @param clientSender the client sender
     */
    public ClientGameLogic(ClientSender clientSender) {
        this.game = new Game();
        this.clientSender = clientSender;
        dialogsState.enter();
        state = dialogsState;
    }

    /**
     * This method is used to send a message to the server
     *
     * @param msg the message to be sent
     */
    public void send(ClientMessage msg) {
        LOGGER.log(Level.INFO, "send {0}", msg);
        clientSender.send(msg);
    }

    /**
     * This method is used to get a piece by its id
     *
     * @param pieceId the UUID of the piece
     * @return the piece
     */
    private Piece getPiece(UUID pieceId) {
        for (var player : this.game.getPlayers().values()) {
            for (Piece piece : player.getPieces()) {
                if (piece.getUuid().equals(pieceId)) {
                    return piece;
                }
            }
        }
        return null;
    }

    public void clear(){
        game = new Game();
        notifications.clear();
        setState(dialogsState);
        isHost = false;
        ownPlayerID = 0;
        ownPlayerName = null;
    }

    /**
     * This method returns the clientSender
     *
     * @return the clientSender
     */
    public ClientSender getClientSender() {
        return clientSender;
    }

    /**
     * This method is used to get the ownPlayerId
     *
     * @return the ownPlayerId
     */
    public int getOwnPlayerId() {
        return ownPlayerID;
    }

    /**
     * This method is used to get the ownPlayerName
     *
     * @return the ownPlayerName
     */
    public String getOwnPlayerName() {
        return ownPlayerName;
    }

    /**
     * This method is used to set the ownPlayerName
     *
     * @param ownPlayerName the ownPlayerName to be set
     */
    public void setOwnPlayerName(String ownPlayerName) {
        this.ownPlayerName = ownPlayerName;
    }

    /**
     * This method is used to set the ownPlayerId
     *
     * @param ownPlayerId the ownPlayerId to be set
     */
    public void setOwnPlayerId(int ownPlayerId) {
        this.ownPlayerID = ownPlayerId;
    }

    /**
     * This method returns the game
     *
     * @return the game
     */
    public Game getGame() {
        return game;
    }

    /**
     * This method returns the current State
     *
     * @return the current State
     */
    public ClientState getState() {
        return state;
    }

    /**
     * This method returns if the client is a host
     *
     * @return if the client is a host
     */
    public boolean isHost() {
        return isHost;
    }

    /**
     * This method returns the steps you can calculate steps
     *
     * @return the calculated moves as int
     */
    public int getCalculatedMoves() {
        return 0;
    }

    /**
     * This method sets if the player is a host
     *
     * @param isHost the boolean value
     */
    public void setHost(boolean isHost) {
        this.isHost = isHost;
    }

    /**
     * This method calls the method received of the state
     *
     * @param msg the ActivePlayer message received
     */
    @Override
    public void received(ActivePlayerMessage msg) {
        state.received(msg);
    }

    /**
     * This method calls the method received of the state
     *
     * @param msg the AnyPiece message received
     */
    @Override
    public void received(AnyPieceMessage msg) {
        state.received(msg);
    }

    /**
     * This method calls the method received of the state
     *
     * @param msg the Briefing message received
     */
    @Override
    public void received(BriefingMessage msg) {
        state.received(msg);
    }

    /**
     * This method calls the method received of the state
     *
     * @param msg the Ceremony message received
     */
    @Override
    public void received(CeremonyMessage msg) {
        state.received(msg);
    }

    /**
     * This method calls the method received of the state
     *
     * @param msg the Dice message received
     */
    @Override
    public void received(DieMessage msg) {
        state.received(msg);
    }

    /**
     * This method calls the method received of the state
     *
     * @param msg the DiceAgain message received
     */
    @Override
    public void received(DiceAgainMessage msg) {
        state.received(msg);
    }

    /**
     * This method calls the method received of the state
     *
     * @param msg the DiceNow message received
     */
    @Override
    public void received(DiceNowMessage msg) {
        state.received(msg);
    }

    /**
     * This method calls the method received of the state
     *
     * @param msg the EndOfGame message received
     */
    @Override
    public void received(EndOfTurnMessage msg) {
        state.received(msg);
    }

    /**
     * This method calls the method received of the state
     *
     * @param msg the GameOver message received
     */
    @Override
    public void received(LobbyAcceptMessage msg) {
        state.received(msg);
    }

    /**
     * This method calls the method received of the state
     *
     * @param msg the LobbyDeny message received
     */
    @Override
    public void received(LobbyDenyMessage msg) {
        state.received(msg);
    }

    /**
     * This method calls the method received of the state
     *
     * @param msg the LobbyPlayerJoin message received
     */
    @Override
    public void received(LobbyPlayerJoinedMessage msg) {
        state.received(msg);
    }

    /**
     * This method calls the method received of the state
     *
     * @param msg the LobbyPlayerLeave message received
     */
    @Override
    public void received(LobbyPlayerLeaveMessage msg) {
        state.received(msg);
    }

    /**
     * This method calls the method received of the state
     *
     * @param msg the MoveMessage message received
     */
    @Override
    public void received(MoveMessage msg) {
        state.received(msg);
    }

    /**
     * This method calls the method received of the state
     *
     * @param msg the NoTurn message received
     */
    @Override
    public void received(NoTurnMessage msg) {
        state.received(msg);
    }

    /**
     * This method calls the method received of the state
     *
     * @param msg the PauseGame message received
     */
    @Override
    public void received(PauseGameMessage msg) {
        state.received(msg);
    }

    /**
     * This method calls the method received of the state
     *
     * @param msg the PlayCard message received
     */
    @Override
    public void received(PlayCardMessage msg) {
        state.received(msg);
    }

    /**
     * This method calls the method received of the state
     *
     * @param msg the PossibleCard message received
     */
    @Override
    public void received(PossibleCardsMessage msg) {
        state.received(msg);
    }

    /**
     * This method calls the method received of the state
     *
     * @param msg the PossiblePiece message received
     */
    @Override
    public void received(PossiblePieceMessage msg) {
        state.received(msg);
    }

    /**
     * This method calls the method received of the state
     *
     * @param msg the RankingResponse message received
     */
    @Override
    public void received(RankingResponseMessage msg) {
        state.received(msg);
    }

    /**
     * This method calls the method received of the state
     *
     * @param msg the RankingRollAgain message received
     */
    @Override
    public void received(RankingRollAgainMessage msg) {
        state.received(msg);
    }

    /**
     * This method calls the method received of the state
     *
     * @param msg the ReconnectBriefing message received
     */
    @Override
    public void received(ReconnectBriefingMessage msg) {
        state.received(msg);
    }

    /**
     * This method calls the method received of the state
     *
     * @param msg the ResumeGame message received
     */
    @Override
    public void received(ResumeGameMessage msg) {
        state.received(msg);
    }

    /**
     * This method calls the method received of the state
     *
     * @param msg the ServerStartGame message received
     */
    @Override
    public void received(ServerStartGameMessage msg) {
        state.received(msg);
    }

    /**
     * This method calls the method received of the state
     *
     * @param msg the SelectTSK message received.
     */
    @Override
    public void received(ShutdownMessage msg) {
        state.received(msg);
    }

    /**
     * Handles a IncorrectRequest message received from the server.
     *
     * @param msg the IncorrectRequest message received.
     */
    @Override
    public void received(IncorrectRequestMessage msg) {
        addNotification(new InfoNotification(Resources.stringLookup("incorrect.request." + msg.getId())));
    }

    /**
     * @param choosePieceStateMessage
     */
    @Override
    public void received(ChoosePieceStateMessage choosePieceStateMessage) {
        state.received(choosePieceStateMessage);
    }

    /**
     * @param drawCardMessage
     */
    @Override
    public void received(DrawCardMessage drawCardMessage) {
        state.received(drawCardMessage);
    }

    /**
     * This method calls the method received of the state
     *
     * @param msg the StartPiece message received
     */
    @Override
    public void received(StartPieceMessage msg) {
        state.received(msg);
    }

    /**
     * This method calls the method received of the state
     *
     * @param msg the UpdateReady message received
     */
    @Override
    public void received(UpdateReadyMessage msg) {
        state.received(msg);
    }

    /**
     * This method calls the method received of the state
     *
     * @param msg the UpdateTSK message received
     */
    @Override
    public void received(UpdateTSKMessage msg) {
        state.received(msg);
    }

    /**
     * This method calls the method received of the state
     *
     * @param msg the WaitPiece message received
     */
    @Override
    public void received(WaitPieceMessage msg) {
        state.received(msg);
    }

    /**
     * This method calls the method received of the state
     *
     * @param msg the Spectator message received.
     */
    @Override
    public void received(SpectatorMessage msg) {
        state.received(msg);
    }

    /**
     * This method calls the method received of the state
     *
     * @param msg the SelectPiece message received.
     */
    @Override
    public void received(SelectPieceMessage msg) {
        state.received(msg);
    }

    /**
     * This method calls the method selectPiece
     *
     * @param pieceId the pieceID
     */
    public void selectPiece(UUID pieceId) {
        state.selectPiece(getPiece(pieceId));
    }

    /**
     * This method call the method selectNExt of the state
     */
    public void selectNext() {
        state.selectNext();
    }

    /**
     * This method calls the method selectCard of the state
     *
     * @param card the BonusCard to selected
     */
    public void selectCard(BonusCard card) {
        state.selectCard(card);
    }

    /**
     * This method call the method selectTsk of the state
     *
     * @param color the Color to be selected
     */
    public void selectTsk(Color color) {
        state.selectTSK(color);
    }

    /**
     * The method calls the method deselectTsk of the state
     *
     * @param color the color to be deselcted
     */
    public void deselectTSK(Color color) {
        state.deselectTSK(color);
    }

    /**
     * This method calls the selectDice method of the state
     */
    public void selectDice() {
        state.selectDice();
    }

    /**
     * This method calls the selectName method of the state
     *
     * @param name the name to be set
     */
    public void selectName(String name) {
        state.setName(name);
    }

    /**
     * This method calls a method of the state base on the parameter value
     *
     * @param ready the value if this method should ready or unready
     */
    public void selectReady(boolean ready) {
        if (ready) {
            state.selectReady();
        } else {
            state.selectUnready();
        }
    }

    /**
     * This method calls the selectHost method of the state
     *
     * @param name the name of the player hosting
     */
    public void selectHost(String name) {
        state.selectHost(name);
    }

    /**
     * This method calls the selectLeave method of the state
     */
    public void selectLeave() {
        state.selectLeave();
    }

    /**
     * This method calls the selectJoin method of the state
     *
     * @param ip the ip to connect to
     */
    public void selectJoin(String ip) {
        state.selectJoin(ip);
    }

    /**
     * This method calls the selectAnimationEnd method of the state
     */
    public void selectAnimationEnd() {
        state.selectAnimationEnd();
    }

    /**
     * This method calls the selectStart method of the state
     */
    public void selectStart() {
        state.selectStart();
    }

    /**
     * This method calls the selectResume method of the state
     */
    public void selectResume() {
        state.selectResume();
    }

    /**
     * This method is used to transition between states
     *
     * @param state the new state
     */
    public void setState(ClientState state) {
        this.state.exit();
        state.enter();
        this.state = state;
    }

    /**
     * This method is used to enter the interrupt state and save the previous state
     */
    public void enterInterrupt() {
        interruptState.enter();
        interruptState.setPreviousState(state);
        this.state = interruptState;
    }

    /**
     * This method is used to get the GameState
     *
     * @return the GameState
     */
    public GameState getGameState() {
        return gameState;
    }

    /**
     * This method is used to get the CeremonyState
     *
     * @return the CeremonyState
     */
    public CeremonyState getCeremony() {
        return ceremonyState;
    }

    /**
     * This method is used to get the InterruptState
     *
     * @return the InterruptState
     */
    public InterruptState getInterrupt() {
        return interruptState;
    }

    /**
     * This method is used to get the DialogsState
     *
     * @return the DialogsState
     */
    public DialogsState getDialogs() {
        return dialogsState;
    }

    /**
     * This method is used to get the SettingsState
     *
     * @return the SettingsState
     */
    public SettingsState getSettings() {
        return settingsState;
    }

    /**
     * This method is used to get the next notification
     *
     * @return the next notification
     */
    public Notification getNotification() {
        if (!notifications.isEmpty()) {
            return notifications.remove(0);
        } else {
            return null;
        }
    }

    /**
     * This method is used to add a notification
     *
     * @param notification the notification to be added
     */
    public void addNotification(Notification notification) {
        notifications.add(notification);
    }

}
