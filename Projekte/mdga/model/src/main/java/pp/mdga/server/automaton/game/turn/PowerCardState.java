package pp.mdga.server.automaton.game.turn;

import pp.mdga.game.BonusCard;
import pp.mdga.game.Piece;
import pp.mdga.game.card.PowerCard;
import pp.mdga.message.client.NoPowerCardMessage;
import pp.mdga.message.client.SelectCardMessage;
import pp.mdga.message.client.SelectedPiecesMessage;
import pp.mdga.message.server.DiceNowMessage;
import pp.mdga.message.server.PossibleCardsMessage;
import pp.mdga.server.ServerGameLogic;
import pp.mdga.server.automaton.game.TurnState;
import pp.mdga.server.automaton.game.turn.powercard.*;
import pp.mdga.visitor.ServerCardVisitor;

import java.util.HashSet;
import java.util.Set;

public class PowerCardState extends TurnAutomatonState {
    /**
     * Create FirstRollState constants.
     */
    private static final System.Logger LOGGER = System.getLogger(PowerCardState.class.getName());

    /**
     * Create PowerCardState states.
     */
    private PowerCardAutomatonState currentState;
    private final ChoosePowerCardState choosePowerCardState;
    private final ShieldCardState shieldCardState;
    private final SwapCardState swapCardState;
    private final TurboCardState turboCardState;

    /**
     * Create PowerCardState attributes.
     */
    private ServerCardVisitor visitor;
    private PowerCard selectedCard;
    private final Set<Piece> selectedPieces = new HashSet<>();
    private final Set<BonusCard> chekedCards = new HashSet<>();

    /**
     * Constructs a server state of the specified game logic.
     *
     * @param turnAutomaton as the automaton of the turn state as a GameState object.
     * @param logic         the game logic
     */
    public PowerCardState(TurnState turnAutomaton, ServerGameLogic logic) {
        super(turnAutomaton, logic);
        this.choosePowerCardState = new ChoosePowerCardState(this, logic);
        this.shieldCardState = new ShieldCardState(this, logic);
        this.swapCardState = new SwapCardState(this, logic);
        this.turboCardState = new TurboCardState(this, logic);

    }

    /**
     * Enters the PowerCardState state.
     * Clears the checked cards, logs the entry, sets the current state to choosePowerCardState,
     * initializes the visitor, and processes the player's hand cards.
     * If no cards are available, sends a DiceNowMessage and transitions to RollDiceState.
     * Otherwise, sends a PossibleCardsMessage with the available cards.
     */
    @Override
    public void enter() {
        chekedCards.clear();
        LOGGER.log(System.Logger.Level.INFO, "Enter PowerCardState state.");
        this.setCurrentState(this.choosePowerCardState);
        this.visitor = new ServerCardVisitor(this.logic);
        for (PowerCard card : this.turnAutomaton.getPlayer().getHandCards()) {
            if (!chekedCards.contains(card.getCard())) {
                chekedCards.add(card.getCard());
                card.accept(this.visitor);
            }
        }

        if (this.visitor.getCards().isEmpty()) {
            this.logic.getServerSender().send(this.logic.getGame().getActivePlayerId(), new DiceNowMessage());
            this.turnAutomaton.setCurrentState(this.turnAutomaton.getRollDiceState());
        } else {
            this.logic.getServerSender().send(this.logic.getGame().getActivePlayerId(), new PossibleCardsMessage(this.visitor.getCards()));
        }
    }

    /**
     * Exits the PowerCardState state.
     * Logs the exit.
     */
    @Override
    public void exit() {
        LOGGER.log(System.Logger.Level.DEBUG, "Exit PowerCardState state.");
    }

    /**
     * Handles the received NoPowerCardMessage.
     * Delegates the message handling to the current state's received method.
     *
     * @param msg  the NoPowerCardMessage received
     * @param form the client id of the player who sent the message
     */
    @Override
    public void received(NoPowerCardMessage msg, int form) {
        currentState.received(msg, form);
    }

    /**
     * This method will be used to add the given piece parameter to the selectedPieces attribute of PowerCardState
     * class.
     *
     * @param piece as the new selected piece as a Piece object.
     */
    public void addSelectedPiece(Piece piece) {
        this.selectedPieces.add(piece);
    }

    public void received(SelectCardMessage msg, int from) {
        currentState.received(msg, from);
    }

    /**
     * This method will be called whenever the server received an SelectedPiecesMessage message.
     * It will also get the client id of the player who send this message.
     *
     * @param msg  as the message which was sent by the player as a SelectedPiecesMessage object.
     * @param from as the client id of the player as an Integer.
     */
    @Override
    public void received(SelectedPiecesMessage msg, int from) {
        this.currentState.received(msg, from);
    }

    /**
     * This method will be used to return currentState attribute of PowerCardState class.
     *
     * @return currentState as a PowerCardAutomatonState object.
     */
    public PowerCardAutomatonState getCurrentState() {
        return this.currentState;
    }

    /**
     * This method will be used to return choosePowerCardState attribute of PowerCardState class.
     *
     * @return choosePowerCardState as a ChoosePowerCardState object.
     */
    public ChoosePowerCardState getChoosePowerCardState() {
        return this.choosePowerCardState;
    }

    /**
     * This method will be used to return shieldCardState attribute of PowerCardState class.
     *
     * @return shieldCardState as a ShieldCardState object.
     */
    public ShieldCardState getShieldCardState() {
        return this.shieldCardState;
    }

    /**
     * This method will be used to return swapCardState attribute of PowerCardState class.
     *
     * @return swapCardState as a SwapCardState object.
     */
    public SwapCardState getSwapCardState() {
        return this.swapCardState;
    }

    /**
     * This method will be used to return turboCardState attribute of PowerCardState class.
     *
     * @return turboCardState as a TurboCardState object.
     */
    public TurboCardState getTurboCardState() {
        return this.turboCardState;
    }

    /**
     * This method will be used to return visitor attribute of PowerCardState class.
     *
     * @return visitor as a ServerCardVisitor object.
     */
    public ServerCardVisitor getVisitor() {
        return this.visitor;
    }

    /**
     * This method will be used to return selectedPieces attribute of PowerCardState class.
     *
     * @return selectedCard as a PowerCard object.
     */
    public PowerCard getSelectedCard() {
        return this.selectedCard;
    }

    /**
     * This method will be used to return selectedPieces attribute of PowerCardState class.
     *
     * @return selectedPieces as a Set of Piece objects.
     */
    public Set<Piece> getSelectedPieces() {
        return this.selectedPieces;
    }

    /**
     * This method will be used to set currentState attribute of PowerCardState class to the given state parameter.
     * In Addition, the currentState will be exited, changed and entered.
     *
     * @param state as the new currentState attribute as a PowerCardAutomatonState object.
     */
    public void setCurrentState(PowerCardAutomatonState state) {
        if (this.currentState != null) {
            this.currentState.exit();
        }
        this.currentState = state;
        this.currentState.enter();
    }

    /**
     * This method will be used to set selectedCard attribute of PowerCardState class to the given selectedCard
     * parameter.
     *
     * @param selectedCard as the new selectedCard attribute as a PowerCard object.
     */
    public void setSelectedCard(PowerCard selectedCard) {
        this.selectedCard = selectedCard;
    }
}
