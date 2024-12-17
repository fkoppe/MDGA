package pp.mdga.server.automaton.game;

import pp.mdga.message.client.AnimationEndMessage;
import pp.mdga.server.ServerGameLogic;
import pp.mdga.server.automaton.GameState;

import java.util.HashSet;
import java.util.Set;

/**
 * This class represents the animation state of the game state.
 */
public class AnimationState extends GameAutomatonState {
    /**
     * Create AnimationState constants.
     */
    private static final System.Logger LOGGER = System.getLogger(AnimationState.class.getName());

    /**
     * Create AnimationState attributes.
     */
    private final Set<Integer> messageReceived = new HashSet<>();

    /**
     * Constructs a server state of the specified game logic.
     *
     * @param gameAutomaton as the automaton of the game state as a GameState object.
     * @param logic         the game logic
     */
    public AnimationState(GameState gameAutomaton, ServerGameLogic logic) {
        super(gameAutomaton, logic);
    }

    /**
     * This method will be used whenever this state will be entered.
     */
    @Override
    public void enter() {
        LOGGER.log(System.Logger.Level.DEBUG, "Entered AnimationState state.");
    }

    /**
     * This method will be used whenever this state will be exited.
     */
    @Override
    public void exit() {
        LOGGER.log(System.Logger.Level.DEBUG, "Exited AnimationState state.");
        this.messageReceived.clear();
    }

    /**
     * This method will be called whenever the server received an AnimationEndMessage message.
     * It will also get the client id of the player who send this message.
     *
     * @param msg  as the message which was sent by the player as a AnimationEndMessage object.
     * @param from as the client id of the player as an Integer.
     */
    @Override
    public void received(AnimationEndMessage msg, int from) {
        this.messageReceived.add(from);
        if (this.messageReceived.size() == this.logic.getGame().getPlayers().size()) {
            this.gameAutomaton.setCurrentState(this.gameAutomaton.getTurnState());
        }
    }
}
