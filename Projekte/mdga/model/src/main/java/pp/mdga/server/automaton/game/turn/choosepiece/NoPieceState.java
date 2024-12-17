package pp.mdga.server.automaton.game.turn.choosepiece;

import pp.mdga.game.Piece;
import pp.mdga.game.PieceState;
import pp.mdga.game.Player;
import pp.mdga.server.ServerGameLogic;
import pp.mdga.server.automaton.game.turn.ChoosePieceState;

import java.util.ArrayList;

public class NoPieceState extends ChoosePieceAutomatonState {
    /**
     * Create FirstRollState constants.
     */
    private static final System.Logger LOGGER = System.getLogger(NoPieceState.class.getName());

    /**
     * Constructs a server state of the specified game logic.
     *
     * @param choosePieceAutomaton as the automaton of the choose piece state as a ChoosePieceState object.
     * @param logic                the game logic
     */
    public NoPieceState(ChoosePieceState choosePieceAutomaton, ServerGameLogic logic) {
        super(choosePieceAutomaton, logic);
    }

    /**
     * Initializes the state based on the current game logic and active player.
     * Determines the next state of the automaton based on the game conditions.
     */
    private void initialize() {
        Player activePlayer = logic.getGame().getPlayerByColor(logic.getGame().getActiveColor());
        if (logic.getGame().getDiceModifier() == 0) {
            if (logic.getGame().getDiceEyes() == 6) {
                if (activePlayer.hasPieceInWaitingArea()) {
                    if (!logic.getGame().getBoard().getInfield()[activePlayer.getStartNodeIndex()].isOccupied(activePlayer.getColor())) {
                        this.choosePieceAutomaton.setCurrentState(this.choosePieceAutomaton.getWaitingPieceState());
                        return;
                    }
                }
            }
            this.choosePieceAutomaton.setCurrentState(this.choosePieceAutomaton.getNoTurnState());
        } else if (activePlayer.hasPieceInWaitingArea()) {
            if (!logic.getGame().getBoard().getInfield()[activePlayer.getStartNodeIndex()].isOccupied(activePlayer.getColor())) {
                if (logic.getGame().getDiceEyes() == 6) {
                    this.choosePieceAutomaton.setCurrentState(this.choosePieceAutomaton.getWaitingPieceState());
                } else {
                    checkSelectPiece(activePlayer);
                }
            } else if (canMove(logic.getGame().getBoard().getInfield()[activePlayer.getStartNodeIndex()].getOccupant())) {
                this.choosePieceAutomaton.setCurrentState(this.choosePieceAutomaton.getStartPieceState());
            } else {
                checkSelectPiece(activePlayer);
            }
        } else {
            checkSelectPiece(activePlayer);
        }
    }

    /**
     * Checks and selects the pieces that can be moved by the active player.
     * Updates the state of the automaton based on the movable pieces.
     *
     * @param activePlayer the player whose pieces are being checked
     */
    private void checkSelectPiece(Player activePlayer) {
        ArrayList<Piece> moveablePieces = new ArrayList<>();
        for (Piece piece : activePlayer.getPieces()) {
            if (piece.getState().equals(PieceState.ACTIVE) || piece.getState().equals(PieceState.HOME)) {
                moveablePieces.add(piece);
            }
        }
        moveablePieces.removeIf(piece -> !canMove(piece));
        if (moveablePieces.isEmpty()) {
            this.choosePieceAutomaton.setCurrentState(this.choosePieceAutomaton.getNoTurnState());
        } else {
            this.choosePieceAutomaton.getSelectPieceState().setMoveablePieces(moveablePieces);
            this.choosePieceAutomaton.setCurrentState(this.choosePieceAutomaton.getSelectPieceState());
        }
    }

    /**
     * This method will be used whenever this state will be entered.
     */
    @Override
    public void enter() {
        LOGGER.log(System.Logger.Level.INFO, "Entered NoPieceState state.");
        initialize();
    }

    /**
     * This method will be used whenever this state will be exited.
     */
    @Override
    public void exit() {
        LOGGER.log(System.Logger.Level.DEBUG, "Exited NoPieceState state.");
    }
}
