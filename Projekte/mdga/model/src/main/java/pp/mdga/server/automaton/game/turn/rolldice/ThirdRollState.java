package pp.mdga.server.automaton.game.turn.rolldice;

import pp.mdga.Resources;
import pp.mdga.message.client.AnimationEndMessage;
import pp.mdga.message.client.RequestDieMessage;
import pp.mdga.message.server.*;
import pp.mdga.server.ServerGameLogic;
import pp.mdga.server.automaton.game.turn.RollDiceState;

public class ThirdRollState extends RollDiceAutomatonState {
    /**
     * Create FirstRollState constants.
     */
    private static final System.Logger LOGGER = System.getLogger(ThirdRollState.class.getName());

    /**
     * Constructs a server state of the specified game logic.
     *
     * @param rollDiceAutomaton as the automaton of the roll dice state as a RollDiceState object.
     * @param logic             the game logic
     */
    public ThirdRollState(RollDiceState rollDiceAutomaton, ServerGameLogic logic) {
        super(rollDiceAutomaton, logic);
    }

    /**
     * This method will be used whenever this state will be entered.
     */
    @Override
    public void enter() {
        this.logic.getServerSender().send(this.logic.getGame().getActivePlayerId(), new DiceNowMessage());
        LOGGER.log(System.Logger.Level.DEBUG, "Exited ThirdRollState state.");
    }

    /**
     * This method will be used whenever this state will be exited.
     */
    @Override
    public void exit() {
        LOGGER.log(System.Logger.Level.DEBUG, "Entered ThirdRollState state.");
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
        this.logic.getGame().setDiceEyes(roll);
        if (roll == Resources.MAX_EYES) {
            logic.getGame().getActivePlayer().getPlayerStatistic().increaseDiced6();
            logic.getGame().getGameStatistics().increaseDiced6();
        }
        this.logic.getServerSender().broadcast(new DieMessage(roll));
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
        if (from != this.logic.getGame().getActivePlayerId()) {
            return;
        }
        if (this.logic.getGame().getDiceEyes() == 6) {
            this.logic.getServerSender().send(this.logic.getGame().getActivePlayerId(), new ChoosePieceStateMessage());
            this.rollDiceAutomaton.getTurnAutomaton().setCurrentState(this.rollDiceAutomaton.getTurnAutomaton().getChoosePieceState());
        } else {
            this.logic.getServerSender().send(this.logic.getGame().getActivePlayerId(), new NoTurnMessage());
            logic.getGame().setActiveColor(logic.getGame().getActiveColor().next(logic.getGame()));
            logic.getServerSender().broadcast(new ActivePlayerMessage(logic.getGame().getActiveColor()));
            this.rollDiceAutomaton.getTurnAutomaton().getGameAutomaton().setCurrentState(this.rollDiceAutomaton.getTurnAutomaton().getGameAutomaton().getTurnState());
        }
    }
}
