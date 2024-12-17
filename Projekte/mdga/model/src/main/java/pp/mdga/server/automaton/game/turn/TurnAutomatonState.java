package pp.mdga.server.automaton.game.turn;

import pp.mdga.server.ServerGameLogic;
import pp.mdga.server.automaton.ServerState;
import pp.mdga.server.automaton.game.TurnState;

public abstract class TurnAutomatonState extends ServerState {
    /**
     * Create TurnAutomatonState attributes.
     */
    protected final TurnState turnAutomaton;

    /**
     * Constructs a server state of the specified game logic.
     *
     * @param turnAutomaton as the automaton of the turn state as a TurnState object.
     * @param logic         the game logic
     */
    public TurnAutomatonState(TurnState turnAutomaton, ServerGameLogic logic) {
        super(logic);
        this.turnAutomaton = turnAutomaton;
    }

    /**
     * This method will be used to return turnAutomaton attribute of TurnAutomatonState object.
     *
     * @return turnAutomaton as a TurnState object
     */
    public TurnState getTurnAutomaton() {
        return this.turnAutomaton;
    }
}
