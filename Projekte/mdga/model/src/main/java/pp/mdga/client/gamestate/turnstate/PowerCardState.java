package pp.mdga.client.gamestate.turnstate;

import pp.mdga.client.ClientGameLogic;
import pp.mdga.client.ClientState;
import pp.mdga.client.gamestate.TurnState;
import pp.mdga.client.gamestate.turnstate.powercardstate.ChoosePowerCardState;
import pp.mdga.client.gamestate.turnstate.powercardstate.PowerCardStates;
import pp.mdga.client.gamestate.turnstate.powercardstate.ShieldState;
import pp.mdga.client.gamestate.turnstate.powercardstate.SwapState;
import pp.mdga.game.BonusCard;
import pp.mdga.game.Piece;
import pp.mdga.message.server.DiceNowMessage;
import pp.mdga.message.server.PlayCardMessage;
import pp.mdga.message.server.PossibleCardsMessage;
import pp.mdga.message.server.PossiblePieceMessage;

public class PowerCardState extends TurnStates {

    private final TurnState parent;
    private PowerCardStates state;

    private final ChoosePowerCardState choosePowerCardState = new ChoosePowerCardState(this, logic);
    private final ShieldState shieldState = new ShieldState(this, logic);
    private final SwapState swapState = new SwapState(this, logic);

    /**
     * Constructs a client state of the specified game logic.
     *
     * @param parent the parent state
     * @param logic the client game logic
     */
    public PowerCardState(ClientState parent, ClientGameLogic logic) {
        super(parent, logic);
        this.parent = (TurnState) parent;
    }

    /**
     * Enters the state.
     */
    @Override
    public void enter() {
        this.setState(this.choosePowerCardState);
    }

    /**
     * Exits the state.
     */
    public void exit() {
        state.exit();
        state = null;
    }

    /**
     * Sets the state.
     *
     * @param state the state
     */
    public void setState(PowerCardStates state) {
        System.out.println("CLIENT STATE old: " + this.state + " new: " + state);

        if (this.state != null) {
            this.state.exit();
        }
        state.enter();
        this.state = state;
    }

    /**
     * Receives the possible cards message.
     *
     * @param msg the possible cards message
     */
    @Override
    public void received(PossibleCardsMessage msg) {
        state.received(msg);
    }

    /**
     * Receives the play card message.
     *
     * @param msg the play card message
     */
    @Override
    public void received(PlayCardMessage msg) {
        state.received(msg);
    }

    /**
     * Receives the dice now message.
     *
     * @param msg the dice now message
     */
    @Override
    public void received(DiceNowMessage msg) {
        state.received(msg);
    }

    /**
     * Receives the possible piece message.
     *
     * @param msg the possible piece message
     */
    @Override
    public void received(PossiblePieceMessage msg) {
        state.received(msg);
    }

    /**
     * Selects the card.
     *
     * @param card the card
     */
    @Override
    public void selectCard(BonusCard card) {
        state.selectCard(card);
    }

    /**
     * Selects the piece.
     *
     * @param piece the piece
     */
    @Override
    public void selectPiece(Piece piece) {
        state.selectPiece(piece);
    }

    /**
     * Returns the choose power card state.
     */
    public ChoosePowerCardState getChoosePowerCard() {
        return choosePowerCardState;
    }

    /**
     * Returns the shield state.
     */
    public ShieldState getShield() {
        return shieldState;
    }

    /**
     * Returns the swap state.
     */
    public SwapState getSwap() {
        return swapState;
    }

    /**
     * Returns the parent state.
     */
    public TurnState getParent() {
        return parent;
    }

    /**
     * Returns the state.
     */
    public PowerCardStates getState() {
        return state;
    }
}
