package pp.mdga.client.gamestate;

import pp.mdga.client.ClientGameLogic;
import pp.mdga.client.ClientState;
import pp.mdga.client.GameState;
import pp.mdga.client.gamestate.turnstate.ChoosePieceState;
import pp.mdga.client.gamestate.turnstate.MovePieceState;
import pp.mdga.client.gamestate.turnstate.PlayPowerCardState;
import pp.mdga.client.gamestate.turnstate.PowerCardState;
import pp.mdga.client.gamestate.turnstate.RollDiceState;
import pp.mdga.client.gamestate.turnstate.TurnStates;
import pp.mdga.game.BonusCard;
import pp.mdga.game.Piece;
import pp.mdga.game.ShieldState;
import pp.mdga.message.server.CeremonyMessage;
import pp.mdga.message.server.ChoosePieceStateMessage;
import pp.mdga.message.server.DiceAgainMessage;
import pp.mdga.message.server.DiceNowMessage;
import pp.mdga.message.server.DieMessage;
import pp.mdga.message.server.EndOfTurnMessage;
import pp.mdga.message.server.MoveMessage;
import pp.mdga.message.server.NoTurnMessage;
import pp.mdga.message.server.PlayCardMessage;
import pp.mdga.message.server.PossibleCardsMessage;
import pp.mdga.message.server.PossiblePieceMessage;
import pp.mdga.message.server.SelectPieceMessage;
import pp.mdga.message.server.SpectatorMessage;
import pp.mdga.message.server.StartPieceMessage;
import pp.mdga.message.server.WaitPieceMessage;
import pp.mdga.notification.RemoveShieldNotification;

public class TurnState extends GameStates {

    private GameState parent;
    private TurnStates state;

    private final ChoosePieceState choosePieceState = new ChoosePieceState(this, logic);
    private final MovePieceState movePieceState = new MovePieceState(this, logic);
    private final PlayPowerCardState playPowerCardState = new PlayPowerCardState(this, logic);
    private final PowerCardState powerCardState = new PowerCardState(this, logic);
    private final RollDiceState rollDiceState = new RollDiceState(this, logic);
    private boolean canChangeTurbo = false;

    /**
     * Constructs a client state of the specified game logic.
     *
     * @param parent the parent state
     * @param logic  the client game logic
     */
    public TurnState(ClientState parent, ClientGameLogic logic) {
        super(parent, logic);
        this.parent = (GameState) parent;
    }

    /**
     * Enters the state.
     */
    @Override
    public void enter() {
        logic = logic;
        for (Piece piece : logic.getGame().getPlayerByColor(logic.getGame().getActiveColor()).getPieces()) {
            if (piece.isShielded() || piece.isSuppressed()) {
                piece.setShield(ShieldState.NONE);
                logic.addNotification(new RemoveShieldNotification(piece.getUuid()));
            }
        }
        logic.getGame().setTurboFlag(false);
        this.setState(this.powerCardState);
    }

    /**
     * Exits the state.
     */
    @Override
    public void exit() {
        state = null;
    }

    /**
     * Sets the state.
     *
     * @param state the state
     */
    public void setState(TurnStates state) {
        System.out.println("CLIENT STATE old: " + this.state + " new: " + state);
        if (this.state != null) {
            this.state.exit();
        }
        state.enter();
        this.state = state;
    }

    /**
     * Selects the dice.
     */
    @Override
    public void selectDice() {
        state.selectDice();
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
     * Selects the card.
     *
     * @param card the card
     */
    @Override
    public void selectCard(BonusCard card) {
        state.selectCard(card);
    }

    /**
     * Selects the animation end.
     */
    @Override
    public void selectAnimationEnd() {
        state.selectAnimationEnd();
    }

    /**
     * Receives the select piece message.
     *
     * @param msg the select piece message
     */
    @Override
    public void received(SelectPieceMessage msg) {
        state.received(msg);
    }

    /**
     * Receives the wait piece message.
     *
     * @param msg the wait piece message
     */
    @Override
    public void received(WaitPieceMessage msg) {
        state.received(msg);
    }

    /**
     * Receives the start piece message.
     *
     * @param msg the start piece message
     */
    @Override
    public void received(StartPieceMessage msg) {
        state.received(msg);
    }

    /**
     * Receives the NoTurnMessage message.
     *
     * @param msg the NoTurnMessage message
     */
    @Override
    public void received(NoTurnMessage msg) {
        state.received(msg);
    }

    /**
     * Receives the MoveMessage message.
     *
     * @param msg the MoveMessage message
     */
    @Override
    public void received(MoveMessage msg) {
        state.received(msg);
    }

    /**
     * Receives the CeremonyMessage message.
     *
     * @param msg the CeremonyMessage message
     */
    @Override
    public void received(CeremonyMessage msg) {
        state.received(msg);
    }

    /**
     * Receives the EndOfTurnMessage message.
     *
     * @param msg the EndOfTurnMessage message
     */
    @Override
    public void received(EndOfTurnMessage msg) {
        state.received(msg);
    }

    /**
     * Receives the SpectatorMessage message.
     *
     * @param msg the SpectatorMessage message
     */
    @Override
    public void received(SpectatorMessage msg) {
        state.received(msg);
    }

    /**
     * Receives the DiceAgainMessage message.
     *
     * @param msg the DiceAgainMessage message
     */
    @Override
    public void received(DiceAgainMessage msg) {
        state.received(msg);
    }

    /**
     * Receives the PossibleCardsMessage message.
     *
     * @param msg the PossibleCardsMessage message
     */
    @Override
    public void received(PossibleCardsMessage msg) {
        state.received(msg);
    }

    /**
     * Receives the PlayCardMessage message.
     *
     * @param msg the PlayCardMessage message
     */
    @Override
    public void received(PlayCardMessage msg) {
        state.received(msg);
    }

    /**
     * Receives the DiceNow message.
     *
     * @param msg the DiceNow message
     */
    @Override
    public void received(DiceNowMessage msg) {
        state.received(msg);
    }

    /**
     * Receives the DieMessage message.
     *
     * @param msg the DieMessage message
     */
    @Override
    public void received(DieMessage msg) {
        state.received(msg);
    }

    /**
     * Receives the PossiblePieceMessage message.
     *
     * @param msg the PossiblePieceMessage message
     */
    @Override
    public void received(PossiblePieceMessage msg) {
        state.received(msg);
    }

    /**
     * Receives the ChoosePieceStateMessage message.
     *
     * @param msg the ChoosePieceStateMessage message
     */
    @Override
    public void received(ChoosePieceStateMessage msg) {
        state.received(msg);
    }

    /**
     * Gets the ChoosePieceState.
     *
     * @return the ChoosePieceState
     */
    public ChoosePieceState getChoosePiece() {
        return choosePieceState;
    }

    /**
     * Gets the MovePieceState.
     *
     * @return the MovePieceState
     */
    public MovePieceState getMovePiece() {
        return movePieceState;
    }

    /**
     * Gets the PlayPowerCardState.
     *
     * @return the PlayPowerCardState
     */
    public PlayPowerCardState getPlayPowerCard() {
        return playPowerCardState;
    }

    /**
     * Gets the PowerCardState.
     *
     * @return the PowerCardState
     */
    public PowerCardState getPowerCard() {
        return powerCardState;
    }

    /**
     * Gets the RollDiceState.
     *
     * @return the RollDiceState
     */
    public RollDiceState getRollDice() {
        return rollDiceState;
    }

    /**
     * Gets the parent GameState.
     *
     * @return the parent GameState
     */
    public GameState getParent() {
        return parent;
    }

    /**
     * Gets the current TurnStates.
     *
     * @return the current TurnStates
     */
    public TurnStates getState() {
        return state;
    }

    /**
     * Checks if turbo can be changed.
     *
     * @return true if turbo can be changed, false otherwise
     */
    public boolean isCanChangeTurbo() {
        return canChangeTurbo;
    }

    /**
     * Sets the turbo change flag.
     *
     * @param canChangeTurbo the new value for the turbo change flag
     */
    public void setCanChangeTurbo(boolean canChangeTurbo) {
        this.canChangeTurbo = canChangeTurbo;
    }
}
