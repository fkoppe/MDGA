package pp.mdga.server.automaton.game.turn.powercard;

import pp.mdga.game.BonusCard;
import pp.mdga.game.card.*;
import pp.mdga.message.client.NoPowerCardMessage;
import pp.mdga.message.client.SelectCardMessage;
import pp.mdga.message.server.DiceNowMessage;
import pp.mdga.message.server.IncorrectRequestMessage;
import pp.mdga.server.ServerGameLogic;
import pp.mdga.server.automaton.game.turn.PowerCardState;
import pp.mdga.visitor.Visitor;

import java.util.List;

public class ChoosePowerCardState extends PowerCardAutomatonState implements Visitor {

    private static final System.Logger LOGGER = System.getLogger(ChoosePowerCardState.class.getName());

    /**
     * Constructs a server state of the specified game logic.
     *
     * @param powerCardAutomaton as the automaton of the turn state as a PowerCardState object.
     * @param logic              the game logic
     */
    public ChoosePowerCardState(PowerCardState powerCardAutomaton, ServerGameLogic logic) {
        super(powerCardAutomaton, logic);
    }

    /**
     * This method will be used whenever this state will be entered.
     */
    @Override
    public void enter() {
        LOGGER.log(System.Logger.Level.INFO, "Entered ChoosePowerCard state.");
    }

    /**
     * This method will be used whenever this state will be exited.
     */
    @Override
    public void exit() {

    }

    @Override
    public void received(SelectCardMessage msg, int from) {
        BonusCard receivedCard = msg.getCard().getCard();
        List<BonusCard> acceptedCards = powerCardAutomaton.getVisitor().getCards().stream().map(PowerCard::getCard).toList();

        if (acceptedCards.contains(receivedCard)) {
            this.powerCardAutomaton.setSelectedCard(msg.getCard());
            msg.getCard().accept(this);
        } else {
            this.logic.getServerSender().send(from, new IncorrectRequestMessage(2));
        }
    }

    @Override
    public void received(NoPowerCardMessage msg, int from) {
        this.powerCardAutomaton.getTurnAutomaton().setCurrentState(this.powerCardAutomaton.getTurnAutomaton().getRollDiceState());
        logic.getServerSender().send(from, new DiceNowMessage());
    }

    /**
     * This method will be used to change the state of the power card automaton depending on the given card parameter.
     *
     * @param card as a ShieldCard object.
     */
    private void changeState(ShieldCard card) {
        this.powerCardAutomaton.setCurrentState(this.powerCardAutomaton.getShieldCardState());
    }

    /**
     * This method will be used to change the state of the power card automaton depending on the given card parameter.
     *
     * @param card as a SwapCard object.
     */
    private void changeState(SwapCard card) {
        this.powerCardAutomaton.setCurrentState(this.powerCardAutomaton.getSwapCardState());
    }

    /**
     * This method will be used to change the state of the power card automaton depending on the given card parameter.
     *
     * @param card as a TurboCard object.
     */
    private void changeState(TurboCard card) {
        this.powerCardAutomaton.setCurrentState(this.powerCardAutomaton.getTurboCardState());
    }

    /**
     * This method will be used to change the state of the power card automaton depending on the given card parameter.
     *
     * @param card as a TurboCard object.
     */
    private void changeState(HiddenCard card) {
        this.logic.getServerSender().send(this.logic.getGame().getActivePlayerId(), new IncorrectRequestMessage(5));
    }

    /**
     * This method will be used to visit the given card parameter.
     *
     * @param card as a TurboCard object.
     */
    @Override
    public void visit(TurboCard card) {
        this.changeState(card);
    }

    /**
     * This method will be used to visit the given card parameter.
     *
     * @param card as a SwapCard object.
     */
    @Override
    public void visit(SwapCard card) {
        this.changeState(card);
    }

    /**
     * This method will be used to visit the given card parameter.
     *
     * @param card as a ShieldCard oblect
     */
    @Override
    public void visit(ShieldCard card) {
        this.changeState(card);
    }

    /**
     * This method will be used to visit the given card parameter.
     *
     * @param card as a HiddenCard object.
     */
    @Override
    public void visit(HiddenCard card) {
        this.changeState(card);
    }
}
