package pp.mdga.server.automaton.game.turn;

import pp.mdga.message.client.AnimationEndMessage;
import pp.mdga.message.server.DiceNowMessage;
import pp.mdga.server.ServerGameLogic;
import pp.mdga.server.automaton.game.TurnState;

import java.util.HashSet;
import java.util.Set;

public class PlayPowerCardState extends TurnAutomatonState {
    /**
     * Create FirstRollState constants.
     */
    private static final System.Logger LOGGER = System.getLogger(PlayPowerCardState.class.getName());

    /**
     * Create PlayPowerCardState attributes.
     */
    private final Set<Integer> messageReceived = new HashSet<Integer>();

    /**
     * Constructs a server state of the specified game logic.
     *
     * @param turnAutomaton as the automaton of the turn state as a GameState object.
     * @param logic         the game logic
     */
    public PlayPowerCardState(TurnState turnAutomaton, ServerGameLogic logic) {
        super(turnAutomaton, logic);
    }

    /**
     * This method will be used whenever this state will be entered.
     */
    @Override
    public void enter() {
        LOGGER.log(System.Logger.Level.DEBUG, "Entered PlayPowerCardState state.");
    }

    /**
     * This method will be used whenever this state will be exited.
     */
    @Override
    public void exit() {
        messageReceived.clear();
        LOGGER.log(System.Logger.Level.DEBUG, "Exited PlayPowerCardState state.");
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
            this.logic.getServerSender().send(this.logic.getGame().getActivePlayerId(), new DiceNowMessage());
            logic.getGame().getActivePlayer().getPlayerStatistic().increaseCardsPlayed();
            logic.getGame().getGameStatistics().increaseCardsPlayed();
            this.turnAutomaton.setCurrentState(this.turnAutomaton.getRollDiceState());
        }
    }
}
