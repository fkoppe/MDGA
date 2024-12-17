package pp.mdga.client.gamestate.turnstate.choosepiecestate;

import pp.mdga.client.ClientGameLogic;
import pp.mdga.client.ClientState;
import pp.mdga.client.gamestate.turnstate.ChoosePieceState;
import pp.mdga.game.Piece;
import pp.mdga.game.PieceState;
import pp.mdga.game.ShieldState;
import pp.mdga.message.client.RequestMoveMessage;
import pp.mdga.message.server.MoveMessage;
import pp.mdga.notification.*;

import java.util.ArrayList;

public class SelectPieceState extends ChoosePieceStates {

    private final ChoosePieceState parent;
    private ArrayList<Piece> possiblePieces;
    private final System.Logger LOGGER = System.getLogger(this.getClass().getName());

    /**
     * Constructs a client state of the specified game logic.
     *
     * @param parent the parent state
     * @param logic the client game logic
     */
    public SelectPieceState(ClientState parent, ClientGameLogic logic) {
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
        possiblePieces = new ArrayList<>();
    }

    /**
     * Sets the possible pieces.
     *
     * @param possiblePieces the possible pieces
     */
    public void setPossiblePieces(ArrayList<Piece> possiblePieces) {
        this.possiblePieces = possiblePieces;
    }

    /**
     * Selects the piece.
     *
     * @param piece the piece
     */
    @Override
    public void selectPiece(Piece piece) {
        if (possiblePieces.contains(piece)) {
            logic.send(new RequestMoveMessage(piece));
        }
    }

    /**
     * This method is called when the server sends a MoveMessage.
     *
     * @param msg the MoveMessage
     */
    @Override
    public void received(MoveMessage msg) {
        Piece piece = logic.getGame().getPieceThroughUUID(msg.getPiece().getUuid());
        //logic.getGame().getBoard().getInfield()[logic.getGame().getBoard().getInfieldIndexOfPiece(piece)].clearOccupant();
        System.out.println("Client: selectPieceState: receivedMoveMessage: msg.isHomeMoved():" + msg.isHomeMove());
        if (msg.isHomeMove()) {
            System.out.println("Client: selectPieceState: receivedMoveMessage: msg.getPiece.getState():" + piece.getState());
            if (piece.getState().equals(PieceState.ACTIVE)) {
                logic.addNotification(new HomeMoveNotification(piece.getUuid(), msg.getTargetIndex()));
                int infieldIndex = logic.getGame().getBoard().getInfieldIndexOfPiece(piece);
                System.out.println("Client: SelectState: activePiece in Home: infieldIndex" + infieldIndex);
                if (msg.getTargetIndex() == logic.getGame().getActivePlayer().getHighestHomeIdx()) {
                    piece.setState(PieceState.HOMEFINISHED);
                } else {
                    piece.setState(PieceState.HOME);
                }
                logic.getGame().getBoard().getInfield()[infieldIndex].clearOccupant();
                logic.getGame().getPlayerByColor(piece.getColor()).setPieceInHome(msg.getTargetIndex(), piece);
            } else {
                System.out.println("Client: SelectPieceState: receivedMoveMessage:reached else");
                logic.addNotification(new HomeMoveNotification(piece.getUuid(), msg.getTargetIndex()));
                System.out.println("Client: electPieceState: homeindex" + logic.getGame().getActivePlayer().getHomeIndexOfPiece(piece));
                int pieceHomeIndex = logic.getGame().getActivePlayer().getHomeIndexOfPiece(piece);
                if (msg.getTargetIndex() == logic.getGame().getActivePlayer().getHighestHomeIdx()) {
                    piece.setState(PieceState.HOMEFINISHED);
                } else {
                    piece.setState(PieceState.HOME);
                }
                logic.getGame().getActivePlayer().getHomeNodes()[pieceHomeIndex].clearOccupant();
                logic.getGame().getPlayerByColor(piece.getColor()).setPieceInHome(msg.getTargetIndex(), piece);
            }
        } else {
            int oldIndex = logic.getGame().getBoard().getInfieldIndexOfPiece(piece);

            Piece occ = logic.getGame().getBoard().getInfield()[msg.getTargetIndex()].getOccupant();
            //logic.getGame().getBoard().getInfield()[msg.getTargetIndex()].moveOccupant(piece);
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
            logic.addNotification(new MovePieceNotification(msg.getPiece().getUuid(), oldIndex, msg.getTargetIndex()));

            //clear old node
            logic.getGame().getBoard().getInfield()[logic.getGame().getBoard().getInfieldIndexOfPiece(piece)].clearOccupant();
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
        logic.getGame().setTurboFlag(false);
        parent.getParent().setState(parent.getParent().getMovePiece());
    }
}
