package pp.mdga.server.automaton.game.turn;

import pp.mdga.message.client.RequestMoveMessage;
import pp.mdga.server.ServerGameLogic;
import pp.mdga.server.automaton.game.TurnState;
import pp.mdga.server.automaton.game.turn.choosepiece.*;

public class ChoosePieceState extends TurnAutomatonState {
    /**
     * Create LobbyState constants.
     */
    private static final System.Logger LOGGER = System.getLogger(ChoosePieceState.class.getName());

    /**
     * Create ChoosePieceState attributes.
     */
    private ChoosePieceAutomatonState currentState;
    private final NoPieceState noPieceState;
    private final NoTurnState noTurnState;
    private final WaitingPieceState waitingPieceState;
    private final StartPieceState startPieceState;
    private final SelectPieceState selectPieceState;

    /**
     * Constructs a server state of the specified game logic.
     *
     * @param turnAutomaton as the automaton of the turn state as a GameState object.
     * @param logic         the game logic
     */
    public ChoosePieceState(TurnState turnAutomaton, ServerGameLogic logic) {
        super(turnAutomaton, logic);
        this.noPieceState = new NoPieceState(this, logic);
        this.noTurnState = new NoTurnState(this, logic);
        this.waitingPieceState = new WaitingPieceState(this, logic);
        this.startPieceState = new StartPieceState(this, logic);
        this.selectPieceState = new SelectPieceState(this, logic);
    }

    @Override
    public void enter() {
        LOGGER.log(System.Logger.Level.DEBUG, "Entered ChoosePieceState state.");
        this.setCurrentState(this.noPieceState);
    }

    @Override
    public void exit() {
        LOGGER.log(System.Logger.Level.DEBUG, "Exited ChoosePieceState state.");
    }

    /**
     * This method will be called whenever the server received a RequestMoveMessage message.
     * It will also get the client id of the player who send this message.
     *
     * @param msg  as the message which was sent by the player as a RequestMoveMessage object.
     * @param from as the client id of the player as an Integer.
     */
    @Override
    public void received(RequestMoveMessage msg, int from) {
        this.currentState.received(msg, from);
    }

    /**
     * This method will be used to return currentState attribute of ChoosePieceState class.
     *
     * @return currentState as a ChoosePieceAutomatonState object.
     */
    public ChoosePieceAutomatonState getCurrentState() {
        return this.currentState;
    }

    /**
     * This method will be used to return noPieceState attribute of ChoosePieceState class.
     *
     * @return noPieceState as a NoOPieceState object.
     */
    public NoPieceState getNoPieceState() {
        return this.noPieceState;
    }

    /**
     * This method will be used to return noTurnState attribute of ChoosePieceState class.
     *
     * @return noTurnState as a NoTurnState object.
     */
    public NoTurnState getNoTurnState() {
        return this.noTurnState;
    }

    /**
     * This method will be used to return waitingPieceState attribute of ChoosePieceState class.
     *
     * @return waitingPieceState as a WaitingPieceState object.
     */
    public WaitingPieceState getWaitingPieceState() {
        return this.waitingPieceState;
    }

    /**
     * This method will be used to return startPieceState attribute of ChoosePieceState class.
     *
     * @return startPieceState as a StartPieceState object.
     */
    public StartPieceState getStartPieceState() {
        return this.startPieceState;
    }

    /**
     * This method will be used to return selectPieceState attribute of ChoosePieceState class.
     *
     * @return selectPieceState as a SelectPieceState object.
     */
    public SelectPieceState getSelectPieceState() {
        return this.selectPieceState;
    }

    /**
     * This method will be used to set currentState attribute of ChoosePieceState class to the given state parameter.
     * In Addition, the currentState will be exited, changed and entered.
     *
     * @param state as the new currentState attribute as a ChoosePieceAutomatonState object.
     */
    public void setCurrentState(ChoosePieceAutomatonState state) {
        if (this.currentState != null) {
            this.currentState.exit();
        }
        this.currentState = state;
        this.currentState.enter();
    }
}
