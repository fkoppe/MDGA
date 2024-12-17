package pp.mdga.client.gamestate.turnstate.choosepiecestate;

import pp.mdga.client.ClientGameLogic;
import pp.mdga.client.ClientState;
import pp.mdga.client.gamestate.turnstate.ChoosePieceState;
import pp.mdga.game.Piece;
import pp.mdga.game.PieceState;
import pp.mdga.game.ShieldState;
import pp.mdga.message.client.RequestMoveMessage;
import pp.mdga.message.server.MoveMessage;
import pp.mdga.notification.MovePieceNotification;
import pp.mdga.notification.RemoveShieldNotification;
import pp.mdga.notification.ThrowPieceNotification;

public class WaitingPieceState extends ChoosePieceStates {

    private final ChoosePieceState parent;
    private Piece moveablePiece;

    /**
     * Constructs a client state of the specified game logic.
     *
     * @param parent the parent state
     * @param logic the client game logic
     */
    public WaitingPieceState(ClientState parent, ClientGameLogic logic) {
        super(parent, logic);
        this.parent = (ChoosePieceState) parent;
    }

    /**
     * Enters the state.
     */
    @Override
    public void enter() {
        moveablePiece = null;
    }

    /**
     * Exits the state.
     */
    @Override
    public void exit() {
    }

    /**
     * Sets the moveable piece.
     *
     * @param piece the moveable piece
     */
    @Override
    public void selectPiece(Piece piece) {
        logic.send(new RequestMoveMessage(piece));
    }

    /**
     * Receives the move message.
     *
     * @param msg the move message
     */
    @Override
    public void received(MoveMessage msg) {
        Piece pieceToMove = logic.getGame().getPieceThroughUUID(msg.getPiece().getUuid());
        Piece occ = logic.getGame().getBoard().getInfield()[msg.getTargetIndex()].getOccupant();
        if (occ != null) {
            logic.getGame().getPlayerByColor(occ.getColor()).addWaitingPiece(occ);
            if (occ.isSuppressed()) {
                logic.addNotification(new RemoveShieldNotification(occ.getUuid()));
                occ.setShield(ShieldState.NONE);
            }
            logic.addNotification(new ThrowPieceNotification(occ.getUuid(), msg.getPiece().getColor()));
        }

        logic.addNotification(new MovePieceNotification(pieceToMove.getUuid(), logic.getGame().getPlayerByColor(logic.getGame().getActiveColor()).getStartNodeIndex(), true));
        logic.getGame().getBoard().getInfield()[msg.getTargetIndex()].setOccupant(msg.getPiece());
        logic.getGame().setTurboFlag(false);
        pieceToMove.setState(PieceState.ACTIVE);
        parent.getParent().setState(parent.getParent().getMovePiece());
    }
}
