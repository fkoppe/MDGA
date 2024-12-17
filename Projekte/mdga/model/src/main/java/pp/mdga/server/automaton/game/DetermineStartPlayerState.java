package pp.mdga.server.automaton.game;

import pp.mdga.game.Color;
import pp.mdga.message.client.AnimationEndMessage;
import pp.mdga.message.client.RequestDieMessage;
import pp.mdga.message.server.ActivePlayerMessage;
import pp.mdga.message.server.DiceNowMessage;
import pp.mdga.message.server.DieMessage;
import pp.mdga.message.server.RankingResponseMessage;
import pp.mdga.server.ServerGameLogic;
import pp.mdga.server.automaton.GameState;

import java.lang.System.Logger.Level;
import java.util.*;

public class DetermineStartPlayerState extends GameAutomatonState {
    /**
     * Create FirstRollState constants.
     */
    private static final System.Logger LOGGER = System.getLogger(DetermineStartPlayerState.class.getName());

    /**
     * Create DetermineStartPlayerState attributes.
     */
    private final Map<Integer, Integer> diceResults = new HashMap<>();
    private final Map<Integer, Integer> finalDiceResults = new HashMap<>();
    private final List<Integer> playersHaveToRoll = new ArrayList<>();
    private final Set<Integer> messageReceived = new HashSet<>();
    private int playerToStart;

    /**
     * Constructs a server state of the specified game logic.
     *
     * @param gameAutomaton as the automaton of the game state as a GameState object.
     * @param logic         the game logic
     */
    public DetermineStartPlayerState(GameState gameAutomaton, ServerGameLogic logic) {
        super(gameAutomaton, logic);
    }

    /**
     * This method will be used whenever this state will be entered.
     */
    @Override
    public void enter() {
        LOGGER.log(System.Logger.Level.INFO, "Entered DetermineStartPlayerState state.");
        playerToStart = this.logic.getGame().getPlayers().size();
    }

    /**
     * This method will be used whenever this state will be exited.
     */
    @Override
    public void exit() {
        LOGGER.log(Level.INFO, "Exited DetermineStartPlayerState state.");
        this.diceResults.clear();
        this.messageReceived.clear();
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
        int roll = this.logic.getGame().getDie().shuffle();
        this.logic.getServerSender().send(from, new DieMessage(roll));
        this.diceResults.put(from, roll);
        if (this.diceResults.size() == playerToStart) {
            int maximumRoll = 0;

            for (Map.Entry<Integer, Integer> entry : this.diceResults.entrySet()) {
                if (maximumRoll == entry.getValue()) {
                    this.playersHaveToRoll.add(entry.getKey());
                    LOGGER.log(Level.INFO, "Players have to roll(RD same as maximum): {0}", this.playersHaveToRoll.size());
                } else if (maximumRoll < entry.getValue()) {
                    maximumRoll = entry.getValue();
                    this.playersHaveToRoll.clear();
                    this.playersHaveToRoll.add(entry.getKey());
                    LOGGER.log(Level.INFO, "Players have to roll(RD higher as maximum): {0}", this.playersHaveToRoll.size());
                }
            }
        }
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
        if (this.messageReceived.size() == playerToStart) {
            this.messageReceived.clear();
            LOGGER.log(Level.INFO, "Players have to roll(Animation End): {0}", this.playersHaveToRoll.size());
            if (this.playersHaveToRoll.size() > 1) {
                playerToStart = this.playersHaveToRoll.size();
                for (Integer id : this.playersHaveToRoll) {
                    diceResults.remove(id);
                    this.logic.getServerSender().send(id, new DiceNowMessage());
                }
                finalDiceResults.putAll(this.diceResults);
                diceResults.clear();
            } else {
                finalDiceResults.putAll(this.diceResults);
                LOGGER.log(Level.INFO, "Players have to roll: %s".formatted(this.logic.getGame().getPlayerById(this.playersHaveToRoll.get(0))));
                Color color = this.logic.getGame().getPlayerById(this.playersHaveToRoll.get(0)).getColor();
                this.logic.getGame().setActiveColor(color);
                this.logic.getServerSender().broadcast(new RankingResponseMessage(this.finalDiceResults));
                this.logic.getServerSender().broadcast(new ActivePlayerMessage(color));
                this.gameAutomaton.setCurrentState(this.gameAutomaton.getAnimationState());
            }
        }
    }
}
