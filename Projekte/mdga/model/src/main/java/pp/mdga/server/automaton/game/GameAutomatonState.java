package pp.mdga.server.automaton.game;

import pp.mdga.server.ServerGameLogic;
import pp.mdga.server.automaton.GameState;
import pp.mdga.server.automaton.ServerState;

/**
 * This abstract class represents the game automaton state of the game state automaton.
 */
public abstract class GameAutomatonState extends ServerState {
    /**
     * Create GameAutomatonState attributes.
     */
    protected final GameState gameAutomaton;

    /**
     * Constructs a server state of the specified game logic.
     *
     * @param gameAutomaton as the automaton of the game state as a GameState object.
     * @param logic         the game logic
     */
    public GameAutomatonState(GameState gameAutomaton, ServerGameLogic logic) {
        super(logic);
        this.gameAutomaton = gameAutomaton;
    }

    /**
     * This method will be used to return gameAutomaton attribute of GameAutomatonState object.
     *
     * @return gameAutomaton as a GameState object.
     */
    public GameState getGameAutomaton() {
        return this.gameAutomaton;
    }
}
