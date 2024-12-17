package pp.mdga.server.automaton.game.turn;

import pp.mdga.message.client.AnimationEndMessage;
import pp.mdga.message.client.RequestDieMessage;
import pp.mdga.server.ServerGameLogic;
import pp.mdga.server.automaton.game.TurnState;
import pp.mdga.server.automaton.game.turn.rolldice.FirstRollState;
import pp.mdga.server.automaton.game.turn.rolldice.RollDiceAutomatonState;
import pp.mdga.server.automaton.game.turn.rolldice.SecondRollState;
import pp.mdga.server.automaton.game.turn.rolldice.ThirdRollState;

public class RollDiceState extends TurnAutomatonState {
    /**
     * Create FirstRollState constants.
     */
    private static final System.Logger LOGGER = System.getLogger(RollDiceState.class.getName());

    /**
     * Create RollDiceState attributes.
     */
    private RollDiceAutomatonState currentState;
    private final FirstRollState firstRollState;
    private final SecondRollState secondRollState;
    private final ThirdRollState thirdRollState;
    private boolean resetModifier = true;

    /**
     * Constructs a server state of the specified game logic.
     *
     * @param turnAutomaton as the automaton of the turn state as a GameState object.
     * @param logic         the game logic
     */
    public RollDiceState(TurnState turnAutomaton, ServerGameLogic logic) {
        super(turnAutomaton, logic);
        this.firstRollState = new FirstRollState(this, logic);
        this.secondRollState = new SecondRollState(this, logic);
        this.thirdRollState = new ThirdRollState(this, logic);
    }

    @Override
    public void enter() {
        LOGGER.log(System.Logger.Level.DEBUG, "Entered RollDiceState state.");
        if (resetModifier) {
            logic.getGame().setDiceModifier(1);
        }
        this.setCurrentState(this.firstRollState);
    }

    @Override
    public void exit() {
        LOGGER.log(System.Logger.Level.DEBUG, "Exited RollDiceState state.");
        resetModifier = true;
    }

    /**
     * This method will be called whenever the server received a RequestDieMessage message.
     * It will also get the client id of the player who send this message.
     *
     * @param msg  as the message which was sent by the player as a RequestDieMessage object.
     * @param from as the client id of the player as an Integer.
     */
    @Override
    public void received(RequestDieMessage msg, int from) {
        this.currentState.received(msg, from);
    }

    /**
     * This method will be called whenever the server received a AnimationEndMessage message.
     * It will also get the client id of the player who send this message.
     *
     * @param msg  as the message which was sent by the player as a AnimationEndMessage object.
     * @param from as the client id of the player as an Integer.
     */
    @Override
    public void received(AnimationEndMessage msg, int from) {
        this.currentState.received(msg, from);
    }

    /**
     * This method will be used to return currentState attribute of RollDiceState class.
     *
     * @return currentState as a RollDiceAutomatonState object.
     */
    public RollDiceAutomatonState getCurrentState() {
        return this.currentState;
    }

    public void setResetModifier(boolean resetModifier) {
        this.resetModifier = resetModifier;
    }

    /**
     * This method will be used to return firstRollState attribute of RollDiceState class.
     *
     * @return firstRollState as a FirstRollState object.
     */
    public FirstRollState getFirstRollState() {
        return this.firstRollState;
    }

    /**
     * This method will be used to return secondRollState attribute of RollDiceState class.
     *
     * @return secondRollState as a SecondRollState object.
     */
    public SecondRollState getSecondRollState() {
        return this.secondRollState;
    }

    /**
     * This method will be used to return currentState attribute of RollDiceState class.
     *
     * @return thirdRollState as a ThirdRollState object.
     */
    public ThirdRollState getThirdRollState() {
        return this.thirdRollState;
    }

    /**
     * This method will be used to set currentState attribute of RollDiceState class to the given state parameter.
     * In Addition, the currentState will be exited, changed and entered.
     *
     * @param state as the new currentState attribute as a RollDiceAutomatonState object.
     */
    public void setCurrentState(RollDiceAutomatonState state) {
        if (this.currentState != null) {
            this.currentState.exit();
        }
        this.currentState = state;
        this.currentState.enter();
    }
}
