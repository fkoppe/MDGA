package pp.mdga.client.dialogstate;

import pp.mdga.client.ClientGameLogic;
import pp.mdga.client.ClientState;
import pp.mdga.client.DialogsState;
import pp.mdga.game.Color;
import pp.mdga.game.Piece;
import pp.mdga.game.Player;
import pp.mdga.message.client.*;
import pp.mdga.message.server.*;
import pp.mdga.notification.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class LobbyState extends DialogStates {

    private final DialogsState parent;

    /**
     * The constructor of the LobbyState.
     *
     * @param parent the parent state
     * @param logic the client game logic
     */
    public LobbyState(ClientState parent, ClientGameLogic logic) {
        super(parent, logic);
        this.parent = (DialogsState) parent;
    }

    /**
     * This method will be called whenever the client enters the LobbyState.
     */
    @Override
    public void enter() {
        logic.send(new JoinedLobbyMessage(logic.getOwnPlayerName()));
    }

    /**
     * This method will be called whenever the client exits the LobbyState.
     */
    @Override
    public void exit() {
    }

    /**
     * This method will be called whenever the client selects the leave button.
     */
    @Override
    public void selectLeave() {
        logic.send(new LeaveGameMessage());
        logic.addNotification(new StartDialogNotification());
        logic.setState(logic.getDialogs());
    }

    /**
     * This method will be called whenever the client selects the color of the player.
     *
     * @param color the color of the player
     */
    @Override
    public void selectTSK(Color color) {
        logic.send(new SelectTSKMessage(color));
    }

    /**
     * This method will be called whenever the client deselects the color of the player.
     *
     * @param color the color of the player
     */
    @Override
    public void deselectTSK(Color color) {
        logic.send(new DeselectTSKMessage(color));
    }

    /**
     * This method will be called whenever the client selects the ready button.
     */
    @Override
    public void selectReady() {
        logic.send(new LobbyReadyMessage());
    }

    /**
     * This method will be called whenever the client selects the unready button.
     */
    @Override
    public void selectUnready() {
        logic.send(new LobbyNotReadyMessage());
    }

    /**
     * This method will be called whenever the client selects the start button.
     */
    @Override
    public void selectStart() {
        if (logic.isHost() && logic.getGame().areAllReady()) {
            logic.send(new StartGameMessage());
        } else {
            logic.send(new StartGameMessage());
            LOGGER.log(System.Logger.Level.ERROR, "You are not the host");
        }
    }

    /**
     * This method will be called whenever the client receives a message.
     *
     * @param msg the message which was received
     */
    @Override
    public void received(ServerStartGameMessage msg) {
        for (Player player : msg.getPlayers()) {
            for (Map.Entry<Integer, Player> entry : this.logic.getGame().getPlayers().entrySet()) {
                if (entry.getValue().getColor() == player.getColor()) {
                    this.logic.getGame().getPlayers().put(entry.getKey(), player);
                }
            }
        }
        logic.getGame().setBoard(msg.getBoard());
        logic.addNotification(new GameNotification(logic.getGame().getPlayerById(logic.getOwnPlayerId()).getColor()));
        for (var player : logic.getGame().getPlayers().values()) {
            List<UUID> pieces = new ArrayList<>();
            for (Piece piece : player.getPieces()) {
                pieces.add(piece.getUuid());
            }
            logic.addNotification(new PlayerInGameNotification(player.getColor(), pieces, player.getName()));
        }
        logic.setState(logic.getGameState());
    }

    /**
     * This method will be called whenever the client receives a message.
     *
     * @param msg the message which was received
     */
    @Override
    public void received(LobbyPlayerJoinedMessage msg) {
        if (msg.getPlayer().getName().equals(logic.getOwnPlayerName())) {
            System.out.println(msg.getId());
            logic.setOwnPlayerId(msg.getId());
        }
        if (msg.getPlayer().getColor() != Color.NONE){
            logic.addNotification(new TskSelectNotification(msg.getPlayer().getColor(), msg.getPlayer().getName(), msg.getPlayer().getName().equals(logic.getOwnPlayerName())));
        }
        logic.getGame().getPlayers().put(msg.getId(), msg.getPlayer());
    }

    /**
     * This method will be called whenever the client receives a message.
     *
     * @param msg the message which was received
     */
    @Override
    public void received(UpdateTSKMessage msg) {
        if (msg.isTaken()) {
            logic.addNotification(new TskSelectNotification(msg.getColor(), logic.getGame().getPlayers().get(msg.getId()).getName(), logic.getOwnPlayerId() == msg.getId()));
        } else {
            logic.addNotification(new TskUnselectNotification(logic.getGame().getPlayers().get(msg.getId()).getColor()));
        }

        logic.getGame().getPlayers().get(msg.getId()).setColor(msg.getColor());
    }

    /**
     * This method will be called whenever the client receives a message.
     *
     * @param msg the message which was received
     */
    @Override
    public void received(LobbyPlayerLeaveMessage msg) {
        logic.addNotification(new TskUnselectNotification(logic.getGame().getPlayers().get(msg.getId()).getColor()));
        logic.getGame().getPlayers().remove(msg.getId());
    }

    /**
     * This method will be called whenever the client receives a message.
     *
     * @param msg the message which was received
     */
    @Override
    public void received(UpdateReadyMessage msg) {
        logic.addNotification(new LobbyReadyNotification(logic.getGame().getPlayers().get(msg.getPlayerId()).getColor(), msg.isReady()));
        logic.getGame().getPlayers().get(msg.getPlayerId()).setReady(msg.isReady());
    }
}
