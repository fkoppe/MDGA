package pp.mdga.client.gamestate.turnstate;

import pp.mdga.client.ClientGameLogic;
import pp.mdga.client.ClientState;
import pp.mdga.client.gamestate.TurnState;
import pp.mdga.client.gamestate.turnstate.choosepiecestate.*;
import pp.mdga.game.Piece;
import pp.mdga.message.server.*;

public class ChoosePieceState extends TurnStates {

    private TurnState parent;
    private ChoosePieceStates currentState;

    private final NoPieceState noPieceState = new NoPieceState(this, logic);
    private final SelectPieceState selectPieceState = new SelectPieceState(this, logic);
    private final StartPieceState startPieceState = new StartPieceState(this, logic);
    private final WaitingPieceState waitingPieceState = new WaitingPieceState(this, logic);

    /**
     * Constructs a client state of the specified game logic.
     *
     * @param parent the parent state
     * @param logic the client game logic
     */
    public ChoosePieceState(ClientState parent, ClientGameLogic logic) {
        super(parent, logic);
        this.parent = (TurnState) parent;
    }

    /**
     * Enters the state.
     */
    @Override
    public void enter() {
        this.setState(this.noPieceState);
    }

    /**
     * Exits the state.
     */
    @Override
    public void exit() {
        currentState.exit();
        currentState = null;
    }

    /**
     * Sets the state.
     *
     * @param state the state
     */
    public void setState(ChoosePieceStates state) {
        System.out.println("CLIENT STATE old: " + this.currentState + " new: " + state);
        if (currentState != null) {
            currentState.exit();
        }
        state.enter();
        currentState = state;
    }

    /**
     * Selects the piece.
     *
     * @param piece the piece
     */
    @Override
    public void selectPiece(Piece piece) {
        currentState.selectPiece(piece);
    }

    /**
     * Receives the select piece message.
     *
     * @param msg the select piece message
     */
    @Override
    public void received(SelectPieceMessage msg) {
        currentState.received(msg);
    }

    /**
     * Receives the wait piece message.
     *
     * @param msg the wait piece message
     */
    @Override
    public void received(WaitPieceMessage msg) {
        currentState.received(msg);
    }

    /**
     * Receives the start piece message.
     *
     * @param msg the start piece message
     */
    @Override
    public void received(StartPieceMessage msg) {
        currentState.received(msg);
    }

    /**
     * Receives the end of turn message.
     *
     * @param msg the end of turn message
     */
    @Override
    public void received(EndOfTurnMessage msg) {
        currentState.received(msg);
    }

    /**
     * Receives the dice now message.
     *
     * @param msg the dice now message
     */
    @Override
    public void received(DiceNowMessage msg) {
        currentState.received(msg);
    }

    /**
     * Receives the move message.
     *
     * @param msg the move message
     */
    @Override
    public void received(MoveMessage msg) {
        currentState.received(msg);
    }

    /**
     * Returns the no piece state.
     */
    public NoPieceState getNoPiece() {
        return noPieceState;
    }

    /**
     * Returns the select piece state.
     */
    public SelectPieceState getSelectPiece() {
        return selectPieceState;
    }

    /**
     * Returns the start piece state.
     */
    public StartPieceState getStartPiece() {
        return startPieceState;
    }

    /**
     * Returns the waiting piece state.
     */
    public WaitingPieceState getWaitingPiece() {
        return waitingPieceState;
    }

    /**
     * Returns the current state.
     */
    public ChoosePieceStates getState() {
        return currentState;
    }

    /**
     * Returns the parent state.
     */
    public TurnState getParent() {
        return parent;
    }
}
