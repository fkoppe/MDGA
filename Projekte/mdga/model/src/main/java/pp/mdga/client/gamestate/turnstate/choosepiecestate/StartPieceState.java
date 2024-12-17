package pp.mdga.client.gamestate.turnstate.choosepiecestate;

import pp.mdga.client.ClientGameLogic;
import pp.mdga.client.ClientState;
import pp.mdga.client.gamestate.turnstate.ChoosePieceState;
import pp.mdga.game.Node;
import pp.mdga.game.Piece;
import pp.mdga.game.ShieldState;
import pp.mdga.message.client.RequestMoveMessage;
import pp.mdga.message.server.MoveMessage;
import pp.mdga.notification.*;

public class StartPieceState extends ChoosePieceStates {

    private final System.Logger LOGGER = System.getLogger(this.getClass().getName());

    private final ChoosePieceState parent;
    private Piece moveablePiece;

    /**
     * Constructs a client state of the specified game logic.
     *
     * @param parent the parent state
     * @param logic the client game logic
     */
    public StartPieceState(ClientState parent, ClientGameLogic logic) {
        super(parent, logic);
        this.parent = (ChoosePieceState) parent;
    }

    /**
     * Enters the state.
     */
    @Override
    public void enter() {

    }

    /**
     * Exits the state.
     */
    @Override
    public void exit() {
        moveablePiece = null;
    }

    /**
     * Sets the moveable piece.
     *
     * @param moveablePiece the moveable piece
     */
    public void setMoveablePiece(Piece moveablePiece) {
        this.moveablePiece = moveablePiece;
    }

    /**
     * Selects the piece.
     *
     * @param piece the piece
     */
    @Override
    public void selectPiece(Piece piece) {
        if (moveablePiece.equals(piece)) {
            logic.send(new RequestMoveMessage(piece));
        }
    }

    /**
     * Receives the move message.
     *
     * @param msg the move message
     */
    @Override
    public void received(MoveMessage msg) {
        Piece piece = logic.getGame().getPieceThroughUUID(msg.getPiece().getUuid());
        int oldIndex = logic.getGame().getBoard().getInfieldIndexOfPiece(piece);
        int targetIndex = msg.getTargetIndex();
        Node targetNode = logic.getGame().getBoard().getInfield()[targetIndex];
        //clear old Node
        logic.getGame().getBoard().getInfield()[oldIndex].clearOccupant();

        //get Occupant
        Piece occ = targetNode.getOccupant();
        if (occ != null) {
            logic.getGame().getPlayerByColor(occ.getColor()).addWaitingPiece(occ);
            if (occ.isSuppressed()) {
                logic.addNotification(new RemoveShieldNotification(occ.getUuid()));
                occ.setShield(ShieldState.NONE);
            }
            logic.addNotification(new ThrowPieceNotification(occ.getUuid(), piece.getColor()));
        }

        if (targetNode.isStart()) {
            if (piece.isShielded()) {
                piece.setShield(ShieldState.SUPPRESSED);
                logic.addNotification(new ShieldSuppressedNotification(piece.getUuid()));
            }
        } else if (piece.isSuppressed()) {
            piece.setShield(ShieldState.ACTIVE);
            logic.addNotification(new RemoveShieldNotification(piece.getUuid()));
            logic.addNotification(new ShieldActiveNotification(piece.getUuid()));
        }

        targetNode.setOccupant(msg.getPiece());
        logic.addNotification(new MovePieceNotification(msg.getPiece().getUuid(), oldIndex, targetIndex));
        logic.getGame().setTurboFlag(false);
        parent.getParent().setState(parent.getParent().getMovePiece());
    }
}
