package pp.mdga.client.gamestate;

import pp.mdga.client.ClientGameLogic;
import pp.mdga.client.ClientState;
import pp.mdga.client.GameState;
import pp.mdga.game.BonusCard;
import pp.mdga.game.Node;
import pp.mdga.game.Piece;
import pp.mdga.game.PieceState;
import pp.mdga.game.ShieldState;
import pp.mdga.game.card.HiddenCard;
import pp.mdga.message.server.*;
import pp.mdga.notification.*;

public class SpectatorState extends GameStates {

    private final GameState parent;

    /**
     * Constructs a client state of the specified game logic.
     *
     * @param parent the parent state
     * @param logic the client game logic
     */
    public SpectatorState(ClientState parent, ClientGameLogic logic) {
        super(parent, logic);
        this.parent = (GameState) parent;
    }

    /**
     * Enters the state.
     */
    @Override
    public void enter() {
        parent.getAnimation().setSpectator(true);
    }

    /**
     * Exits the state.
     */
    @Override
    public void exit() {

    }

    /**
     * Handles the reception of a CeremonyMessage.
     *
     * @param msg the ceremony message
     */
    @Override
    public void received(CeremonyMessage msg) {
        logic.setState(logic.getCeremony());
    }

    /**
     * Handles the reception of a DieMessage.
     *
     * @param msg the die message
     */
    @Override
    public void received(DieMessage msg) {
        logic.getGame().setDiceEyes(msg.getDiceEye());
        if (logic.getGame().getTurboFlag()) {
            logic.addNotification(new RollDiceNotification(logic.getGame().getActiveColor(), msg.getDiceEye(), logic.getGame().getDiceModifier()));
        } else {
            logic.addNotification(new RollDiceNotification(logic.getGame().getActiveColor(), msg.getDiceEye()));
        }

        //stats
        if (msg.getDiceEye() == 6) {
            logic.getGame().getPlayerByColor(logic.getGame().getActiveColor()).getPlayerStatistic().increaseDiced6();
            logic.getGame().getGameStatistics().increaseDiced6();
        }
    }

    /**
     * Handles the reception of a PlayCardMessage.
     *
     * @param msg the play card message
     */
    @Override
    public void received(PlayCardMessage msg) {
        logic.addNotification(new PlayCardNotification(logic.getGame().getActiveColor(), msg.getCard().getCard()));
        handlePowerCard(msg);
        logic.getGame().getPlayerByColor(logic.getGame().getActiveColor()).getPlayerStatistic().increaseCardsPlayed();
        logic.getGame().getGameStatistics().increaseCardsPlayed();
        parent.setState(parent.getAnimation());
    }

    /**
     * Handles the reception of an ActivePlayerMessage.
     *
     * @param msg the active player message
     */
    @Override
    public void received(ActivePlayerMessage msg) {
        logic.addNotification(new ActivePlayerNotification(msg.getColor()));
        logic.getGame().setActiveColor(msg.getColor());
        if (msg.getColor() == logic.getGame().getPlayers().get(logic.getOwnPlayerId()).getColor()) {
            parent.setState(parent.getTurn());
        } else {
            for (Piece piece : logic.getGame().getActivePlayer().getPieces()) {
                if (piece.isShielded() || piece.isSuppressed()) {
                    logic.addNotification(new RemoveShieldNotification(piece.getUuid()));
                    piece.setShield(ShieldState.NONE);
                }
            }
        }
    }

    /**
     * Handles the reception of a MoveMessage.
     *
     * @param msg the move message
     */
    @Override
    public void received(MoveMessage msg) {
        Piece piece = logic.getGame().getPieceThroughUUID(msg.getPiece().getUuid());
        if (msg.isHomeMove()) {
            if (piece.getState().equals(PieceState.HOME)) {
                logic.addNotification(new HomeMoveNotification(piece.getUuid(), msg.getTargetIndex()));
                int pieceHomeIndex = logic.getGame().getActivePlayer().getHomeIndexOfPiece(piece);
                Node pieceNode = logic.getGame().getActivePlayer().getHomeNodes()[pieceHomeIndex];

                //gets the oldNode
                int homeIndex = logic.getGame().getActivePlayer().getHomeIndexOfPiece(piece);
                Node oldNode = logic.getGame().getActivePlayer().getHomeNodes()[homeIndex];
                //gets the targetNode
                Node targetNode = logic.getGame().getActivePlayer().getHomeNodes()[msg.getTargetIndex()];
                if (msg.getTargetIndex() == logic.getGame().getActivePlayer().getHighestHomeIdx()) {
                    piece.setState(PieceState.HOMEFINISHED);
                } else {
                    piece.setState(PieceState.HOME);
                }

                oldNode.clearOccupant();
                targetNode.setOccupant(piece);

            } else {
                logic.addNotification(new HomeMoveNotification(piece.getUuid(), msg.getTargetIndex()));
                int oldNoteIdx = logic.getGame().getBoard().getInfieldIndexOfPiece(piece);
                Node oldNode = logic.getGame().getBoard().getInfield()[oldNoteIdx];

                //gets the targetNode
                Node targetNode = logic.getGame().getActivePlayer().getHomeNodes()[msg.getTargetIndex()];

                if (msg.getTargetIndex() == logic.getGame().getActivePlayer().getHighestHomeIdx()) {
                    piece.setState(PieceState.HOMEFINISHED);
                } else {
                    piece.setState(PieceState.HOME);
                }

                oldNode.clearOccupant();
                targetNode.setOccupant(piece);
            }
        } else {
            int oldIndex = logic.getGame().getBoard().getInfieldIndexOfPiece(piece);
            Piece occ = logic.getGame().getBoard().getInfield()[msg.getTargetIndex()].getOccupant();
            if (occ != null) {
                //TODO: MoveThrowNotification
                logic.addNotification(new ThrowPieceNotification(occ.getUuid(), piece.getColor()));
                if (occ.isSuppressed()) {
                    logic.addNotification(new RemoveShieldNotification(occ.getUuid()));
                    occ.setShield(ShieldState.NONE);
                }
                //set occ to waiting
                logic.getGame().getPlayerByColor(occ.getColor()).addWaitingPiece(occ);
            }
            if (oldIndex == -1) {
                logic.addNotification(new MovePieceNotification(piece.getUuid(), msg.getTargetIndex(), true));
                logic.getGame().getPlayerByColor(piece.getColor()).removeWaitingPiece(piece);
                piece.setState(PieceState.ACTIVE);
            } else {
                logic.addNotification(new MovePieceNotification(piece.getUuid(), oldIndex, msg.getTargetIndex()));
                //clear old node
                logic.getGame().getBoard().getInfield()[logic.getGame().getBoard().getInfieldIndexOfPiece(piece)].clearOccupant();
            }
            //set new node
            logic.getGame().getBoard().getInfield()[msg.getTargetIndex()].setOccupant(piece);
            if (logic.getGame().getBoard().getInfield()[msg.getTargetIndex()].isStart()) {
                if (piece.isShielded()) {
                    piece.setShield(ShieldState.SUPPRESSED);
                    logic.addNotification(new ShieldSuppressedNotification(piece.getUuid()));
                }
            } else if (piece.isSuppressed()) {
                piece.setShield(ShieldState.ACTIVE);
                logic.addNotification(new ShieldActiveNotification(piece.getUuid()));
            }
        }
        System.out.println("send AnimationEndMessage");
        logic.getGame().setTurboFlag(false);
        parent.setState(parent.getAnimation());
    }
}
