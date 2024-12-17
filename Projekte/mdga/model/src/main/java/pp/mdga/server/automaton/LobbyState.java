package pp.mdga.server.automaton;

import pp.mdga.game.Color;
import pp.mdga.game.Piece;
import pp.mdga.game.PieceState;
import pp.mdga.game.Player;
import pp.mdga.game.card.PowerCard;
import pp.mdga.message.client.*;
import pp.mdga.message.server.*;
import pp.mdga.server.ServerGameLogic;

import java.util.Map;

/**
 * This class represents the lobby state of the server.
 * It will handle all join and disconnect messages, as well the selection of the color of the player.
 */
public class LobbyState extends ServerState {
    /**
     * Create LobbyState constants.
     */
    private static final System.Logger LOGGER = System.getLogger(ServerState.class.getName());

    /**
     * Constructs a server state of the specified game logic.
     *
     * @param logic the game logic
     */
    public LobbyState(ServerGameLogic logic) {
        super(logic);
    }

    /**
     * This method will be used whenever this state will be entered.
     */
    @Override
    public void enter() {
        LOGGER.log(System.Logger.Level.DEBUG, "Entered LobbyState state.");
    }

    /**
     * This method will be used whenever this state will be exited.
     */
    @Override
    public void exit() {
        LOGGER.log(System.Logger.Level.DEBUG, "Exited LobbyState state.");
    }

    /**
     * This method will be used to initialize the game and all necessary objects.
     */
    public void initializeGame() {
        for (var player : this.logic.getGame().getPlayers().values()) {
            player.initialize();
            PowerCard card = this.logic.getGame().draw();
            if (card == null) {
                this.logic.getServerSender().broadcast(new IncorrectRequestMessage(7));
            } else {
                player.addHandCard(card);
            }

            Piece piece = player.getPieces()[0];
            player.getWaitingArea()[0] = null;
            piece.setState(PieceState.ACTIVE);
            this.logic.getGame().getBoard().getInfield()[player.getStartNodeIndex()].setOccupant(piece);
        }
    }

    /**
     * This method will be called whenever the server received a JoinedLobbyMessage message.
     * It will also get the client id of the player who send this message
     *
     * @param msg  as the message which was sent by the player as a JoinedLobbyMessage object.
     * @param from as the client id of the player as an Integer.
     */
    @Override
    public void received(JoinedLobbyMessage msg, int from) {
        Player player = new Player(msg.getName());
        player.setColor(Color.NONE);
        this.logic.getGame().addPlayer(from, player);
        for (Map.Entry<Integer, Player> entry : this.logic.getGame().getPlayers().entrySet()) {
            this.logic.getServerSender().broadcast(new LobbyPlayerJoinedMessage(entry.getKey(), entry.getValue(), entry.getKey() == this.logic.getGame().getHost()));

            if (entry.getKey() != from && entry.getValue().isReady()) {
                this.logic.getServerSender().send(from, new UpdateReadyMessage(entry.getKey(), entry.getValue().isReady()));
            }
        }
    }

    /**
     * This method will be called whenever the server received a SelectTSKMessage message.
     * It will also get the client id of the player who send this message.
     *
     * @param msg  as the message which was sent by the player as a SelectTSK object.
     * @param from as the client id of the player as an Integer.
     */
    @Override
    public void received(SelectTSKMessage msg, int from) {
        if (msg.getColor() != Color.NONE && !this.logic.getGame().isColorTaken(msg.getColor())) {
            this.logic.getServerSender().broadcast(new UpdateTSKMessage(from, Color.NONE, false));
            this.logic.getGame().getPlayerById(from).setColor(msg.getColor());
            this.logic.getServerSender().broadcast(new UpdateTSKMessage(from, msg.getColor(), true));
        } else {
            this.logic.getServerSender().send(from, new IncorrectRequestMessage(0));
        }
    }

    /**
     * This method will be called whenever the server received a DeselectTSKMessage message.
     * It will also get the client id of the player who send this message.
     *
     * @param msg  as the message which was sent by the player as a DeselectTSK object.
     * @param from as the client id of the player as an Integer.
     */
    @Override
    public void received(DeselectTSKMessage msg, int from) {
        this.logic.getGame().getPlayerById(from).setColor(Color.NONE);
        this.logic.getServerSender().broadcast(new UpdateTSKMessage(from, Color.NONE, false));
    }

    /**
     * This method will be called whenever the server received a LobbyReadyMessage message.
     * It will also get the client id of the player who send this message.
     *
     * @param msg  as the message which was sent by the player as a LobbyReady object.
     * @param from as the client id of the player as an Integer.
     */
    @Override
    public void received(LobbyReadyMessage msg, int from) {
        if (this.logic.getGame().getPlayerById(from).getColor() == Color.NONE) {
            Color color = this.logic.getGame().getFirstUnusedColor();

            if (color != Color.NONE) {
                this.logic.getGame().getPlayerById(from).setColor(color);
                this.logic.getServerSender().broadcast(new UpdateTSKMessage(from, color, true));
            } else {
                this.logic.getServerSender().send(from, new IncorrectRequestMessage(1));
            }
        }
        this.logic.getGame().getPlayerById(from).setReady(true);
        this.logic.getServerSender().broadcast(new UpdateReadyMessage(from, true));
    }

    /**
     * This method will be called whenever the server received a LobbyNotReadyMessage message.
     * It will also get the client id of the player who send this message.
     *
     * @param msg  as the message which was sent by the player as a LobbyNotReady object.
     * @param from as the client id of the player as an Integer.
     */
    @Override
    public void received(LobbyNotReadyMessage msg, int from) {
        this.logic.getGame().getPlayerById(from).setReady(false);
        this.logic.getServerSender().broadcast(new UpdateReadyMessage(from, false));
    }

    /**
     * This method will be called whenever the server received a StartGame message.
     * It will also get the client id of the player who send this message.
     *
     * @param msg  as the message which was sent by the player as a ForceStartGame object.
     * @param from as the client id of the player as an Integer.
     */
    @Override
    public void received(StartGameMessage msg, int from) {
        if (this.logic.getGame().areAllReady()) {
            if (this.logic.getGame().getPlayers().size() >= 2) {
                this.initializeGame();
                this.logic.getServerSender().broadcast(new ServerStartGameMessage(this.logic.getGame().getPlayersAsList(), this.logic.getGame().getBoard()));
                this.logic.setCurrentState(this.logic.getGameState());
            } else {
                this.logic.getServerSender().send(from, new IncorrectRequestMessage(6));
            }
        } else {
            this.logic.getServerSender().send(from, new IncorrectRequestMessage(5));
        }
    }

    /**
     * Removed the player after receiving a LeaveGameMessage message.
     *
     * @param msg  as the message which was sent by the player as a LeaveGameMessage object.
     * @param from as the client id of the player as an Integer.
     */
    @Override
    public void received(LeaveGameMessage msg, int from) {
        this.logic.getGame().removePlayer(from);
        this.logic.getServerSender().broadcast(new LobbyPlayerLeaveMessage(from));
    }
}
