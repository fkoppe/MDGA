package pp.mdga.server.automaton.game.turn.rolldice;

import pp.mdga.server.ServerGameLogic;
import pp.mdga.server.automaton.ServerState;
import pp.mdga.server.automaton.game.turn.RollDiceState;

public abstract class RollDiceAutomatonState extends ServerState {
    /**
     * Create RollDiceAutomatonState attributes.
     */
    protected final RollDiceState rollDiceAutomaton;

    /**
     * Constructs a server state of the specified game logic.
     *
     * @param rollDiceAutomaton as the automaton of the roll dice state as a RollDiceState object.
     * @param logic             the game logic
     */
    public RollDiceAutomatonState(RollDiceState rollDiceAutomaton, ServerGameLogic logic) {
        super(logic);
        this.rollDiceAutomaton = rollDiceAutomaton;
    }
}
