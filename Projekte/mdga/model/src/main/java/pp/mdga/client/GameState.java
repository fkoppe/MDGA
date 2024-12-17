package pp.mdga.client;

import pp.mdga.client.gamestate.*;
import pp.mdga.game.BonusCard;
import pp.mdga.game.Piece;
import pp.mdga.game.card.HiddenCard;
import pp.mdga.message.client.LeaveGameMessage;
import pp.mdga.message.server.*;
import pp.mdga.notification.*;

public class GameState extends ClientState {

    /**
     * the current substate
     */
    private GameStates state;

    private final AnimationState animationState = new AnimationState(this, logic);
    private final DetermineStartPlayerState determineStartPlayerState = new DetermineStartPlayerState(this, logic);
    private final SpectatorState spectatorState = new SpectatorState(this, logic);
    private final TurnState turnState = new TurnState(this, logic);
    private final WaitingState waitingState = new WaitingState(this, logic);

    /**
     * Constructor for GameState
     *
     * @param parent the parent of this state
     * @param logic  the ClientGameLogic
     */
    public GameState(ClientState parent, ClientGameLogic logic) {
        super(parent, logic);
    }

    /**
     * The method to enter the state
     */
    @Override
    public void enter() {
        this.setState(this.determineStartPlayerState);
    }

    /**
     * the method to exit this state
     */
    @Override
    public void exit() {
        state.exit();
    }

    /**
     * This method is used to set a new SubState
     *
     * @param newState the state to be set
     */
    public void setState(GameStates newState) {
        if (this.state != null) {
            this.state.exit();
        }
        System.out.println("CLIENT STATE old: " + this.state + " new: " + newState);
        newState.enter();
        state = newState;
    }

    /**
     * This method is used to call the selectAnimationEnd method of the current state
     */
    @Override
    public void selectAnimationEnd() {
        state.selectAnimationEnd();
    }

    /**
     * This method is used to call the selectDice method of the current state
     */
    @Override
    public void selectDice() {
        state.selectDice();
    }

    /**
     * This method is used to call the selectPiece method of the current state
     *
     * @param piece the piece to be selected
     */
    @Override
    public void selectPiece(Piece piece) {
        state.selectPiece(piece);
    }

    /**
     * This method is used to call the selectCard method of the current state
     *
     * @param card the card to be selected
     */
    @Override
    public void selectCard(BonusCard card) {
        state.selectCard(card);
    }

    /**
     * This method is used to call the received method of the current state
     *
     * @param msg the message to be received
     */
    @Override
    public void received(PauseGameMessage msg) {
        logic.enterInterrupt();
        logic.addNotification(new InterruptNotification(logic.getGame().getPlayers().get(msg.getPlayerId()).getColor()));
    }

    @Override
    public void selectLeave() {
        logic.send(new LeaveGameMessage());
        logic.addNotification(new StartDialogNotification());
        logic.setState(logic.getDialogs());
    }

    /**
     * This method is used to call the received method of the current state
     *
     * @param msg the message to be received
     */
    @Override
    public void received(DieMessage msg) {
        state.received(msg);
    }

    /**
     * This method is used to call the received method of the current state
     *
     * @param msg the message to be received
     */
    @Override
    public void received(RankingRollAgainMessage msg) {
        state.received(msg);
    }

    /**
     * This method is used to call the received method of the current state
     *
     * @param msg the message to be received
     */
    @Override
    public void received(RankingResponseMessage msg) {
        state.received(msg);
    }

    /**
     * This method is used to call the received method of the current state
     *
     * @param msg the message to be received
     */
    @Override
    public void received(SelectPieceMessage msg) {
        state.received(msg);
    }

    /**
     * This method is used to call the received method of the current state
     *
     * @param msg the message to be received
     */
    @Override
    public void received(WaitPieceMessage msg) {
        state.received(msg);
    }

    /**
     * This method is used to call the received method of the current state
     *
     * @param msg the message to be received
     */
    @Override
    public void received(StartPieceMessage msg) {
        state.received(msg);
    }

    /**
     * This method is used to call the received method of the current state
     *
     * @param msg the message to be received
     */
    @Override
    public void received(NoTurnMessage msg) {
        state.received(msg);
    }

    /**
     * This method is used to call the received method of the current state
     *
     * @param msg the message to be received
     */
    @Override
    public void received(MoveMessage msg) {
        state.received(msg);
    }

    /**
     * This method is used to call the received method of the current state
     *
     * @param msg the message to be received
     */
    @Override
    public void received(CeremonyMessage msg) {
        CeremonyNotification notification = new CeremonyNotification();
        notification.setColors(msg.getColors());
        notification.setNames(msg.getNames());
        notification.setBonusNodes(msg.getBonusNodes());
        notification.setSixes(msg.getSixes());
        notification.setBonusCardsPlayed(msg.getBonusCardsPlayed());
        notification.setPiecesLost(msg.getPiecesLost());
        notification.setPiecesThrown(msg.getPiecesThrown());
        notification.setNodesMoved(msg.getNodesMoved());
        logic.addNotification(notification);
        logic.setState(logic.getCeremony());
    }

    /**
     * This method is used to call the received method of the current state
     *
     * @param msg the message to be received
     */
    @Override
    public void received(EndOfTurnMessage msg) {
        state.received(msg);
    }

    /**
     * This method is used to call the received method of the current state
     *
     * @param msg the message to be received
     */
    @Override
    public void received(SpectatorMessage msg) {
        state.received(msg);
    }

    /**
     * This method is used to call the received method of the current state
     *
     * @param msg the message to be received
     */
    @Override
    public void received(DiceAgainMessage msg) {
        state.received(msg);
    }

    /**
     * This method is used to call the received method of the current state
     *
     * @param msg the message to be received
     */
    @Override
    public void received(PossibleCardsMessage msg) {
        state.received(msg);
    }

    /**
     * This method is used to call the received method of the current state
     *
     * @param msg the message to be received
     */
    @Override
    public void received(PlayCardMessage msg) {
        state.received(msg);
    }

    /**
     * This method is used to call the received method of the current state
     *
     * @param msg the message to be received
     */
    @Override
    public void received(DiceNowMessage msg) {
        state.received(msg);
    }

    /**
     * This method is used to call the received method of the current state
     *
     * @param msg the message to be received
     */
    @Override
    public void received(ActivePlayerMessage msg) {
        state.received(msg);
    }

    @Override
    public void received(PossiblePieceMessage msg) {
        state.received(msg);
    }

    @Override
    public void received(ChoosePieceStateMessage msg) {
        state.received(msg);
    }

    @Override
    public void received(DrawCardMessage msg) {
        logic.getGame().getActivePlayer().addHandCard(msg.getCard());
        if (msg.getCard() instanceof HiddenCard) {
            logic.addNotification(new DrawCardNotification(logic.getGame().getActiveColor(), BonusCard.HIDDEN));
        } else {
            logic.addNotification(new AcquireCardNotification(msg.getCard().getCard()));
        }

    }

    /**
     * This method returns the current state
     *
     * @return the current state
     */
    public GameStates getState() {
        return state;
    }

    /**
     * This method returns the AnimationState
     *
     * @return the AnimationState
     */
    public AnimationState getAnimation() {
        return animationState;
    }

    /**
     * This method returns the DetermineStartPlayerState
     *
     * @return the DetermineStartPlayerState
     */
    public DetermineStartPlayerState getDetermineStartPlayer() {
        return determineStartPlayerState;
    }

    /**
     * This method returns the SpectatorState
     *
     * @return the SpectatorState
     */
    public SpectatorState getSpectator() {
        return spectatorState;
    }

    /**
     * This method returns the TurnState
     *
     * @return the TurnState
     */
    public TurnState getTurn() {
        return turnState;
    }

    /**
     * This method returns the WaitingState
     *
     * @return the WaitingState
     */
    public WaitingState getWaiting() {
        return waitingState;
    }
}
