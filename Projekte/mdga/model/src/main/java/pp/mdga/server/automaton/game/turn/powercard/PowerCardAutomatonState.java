package pp.mdga.server.automaton.game.turn.powercard;

import pp.mdga.server.ServerGameLogic;
import pp.mdga.server.automaton.ServerState;
import pp.mdga.server.automaton.game.turn.PowerCardState;

public abstract class PowerCardAutomatonState extends ServerState {
    /**
     * Create PowerCardAutomatonState attributes.
     */
    protected final PowerCardState powerCardAutomaton;

    /**
     * Constructs a server state of the specified game logic.
     *
     * @param powerCardAutomaton as the automaton of the turn state as a PowerCardState object.
     * @param logic              the game logic
     */
    public PowerCardAutomatonState(PowerCardState powerCardAutomaton, ServerGameLogic logic) {
        super(logic);
        this.powerCardAutomaton = powerCardAutomaton;
    }
}
