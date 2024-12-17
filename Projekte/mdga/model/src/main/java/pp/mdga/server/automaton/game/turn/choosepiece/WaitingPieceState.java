package pp.mdga.server.automaton.game.turn.choosepiece;

import pp.mdga.game.Piece;
import pp.mdga.game.PieceState;
import pp.mdga.message.client.RequestMoveMessage;
import pp.mdga.message.server.MoveMessage;
import pp.mdga.message.server.WaitPieceMessage;
import pp.mdga.server.ServerGameLogic;
import pp.mdga.server.automaton.game.turn.ChoosePieceState;

public class WaitingPieceState extends ChoosePieceAutomatonState {
    /**
     * Create FirstRollState constants.
     */
    private static final System.Logger LOGGER = System.getLogger(WaitingPieceState.class.getName());
    private Piece piece;

    /**
     * Constructs a server state of the specified game logic.
     *
     * @param choosePieceAutomaton as the automaton of the choose piece state as a ChoosePieceState object.
     * @param logic                the game logic
     */
    public WaitingPieceState(ChoosePieceState choosePieceAutomaton, ServerGameLogic logic) {
        super(choosePieceAutomaton, logic);
    }

    /**
     * This method will be used whenever this state will be entered.
     */
    @Override
    public void enter() {
        LOGGER.log(System.Logger.Level.INFO, "Entered WaitingPieceState state.");
        this.piece = logic.getGame().getPlayerByColor(logic.getGame().getActiveColor()).getWaitingPiece();
        logic.getServerSender().send(logic.getGame().getActivePlayerId(), new WaitPieceMessage(this.piece.getUuid()));
    }

    @Override
    public void received(RequestMoveMessage msg, int from) {
        if (msg.getPiece().equals(this.piece)) {
            logic.getGame().getPlayerByColor(logic.getGame().getActiveColor()).removeWaitingPiece(this.piece);
            piece.setState(PieceState.ACTIVE);
            Piece thrownOcc = logic.getGame().getBoard().getInfield()[logic.getGame().getPlayerByColor(logic.getGame().getActiveColor()).getStartNodeIndex()].getOccupant();
            if (thrownOcc != null) {
                logic.getGame().getActivePlayer().getPlayerStatistic().increasePiecesThrown();
                logic.getGame().getGameStatistics().increasePiecesThrown();
                logic.getGame().getPlayerByColor(thrownOcc.getColor()).getPlayerStatistic().increasePiecesBeingThrown();
                logic.getGame().getGameStatistics().increasePiecesBeingThrown();
                logic.getGame().getPlayerByColor(thrownOcc.getColor()).addWaitingPiece(thrownOcc);
            }
            logic.getGame().getBoard().getInfield()[logic.getGame().getPlayerByColor(logic.getGame().getActiveColor()).getStartNodeIndex()].setOccupant(this.piece);
            logic.getServerSender().broadcast(new MoveMessage(this.piece, false, logic.getGame().getPlayerById(logic.getGame().getActivePlayerId()).getStartNodeIndex()));
            this.choosePieceAutomaton.getTurnAutomaton().setCurrentState(this.choosePieceAutomaton.getTurnAutomaton().getMovePieceState());
        }
    }

    /**
     * This method will be used whenever this state will be exited.
     */
    @Override
    public void exit() {
        LOGGER.log(System.Logger.Level.DEBUG, "Exited WaitingPieceState state.");
        piece = null;
    }

    /**
     * this is a setter for the piece
     *
     * @param piece to be set
     */
    public void setPiece(Piece piece){
        this.piece = piece;
    }
}
