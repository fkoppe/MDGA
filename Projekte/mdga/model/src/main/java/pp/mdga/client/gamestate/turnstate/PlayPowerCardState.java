package pp.mdga.client.gamestate.turnstate;

import pp.mdga.client.ClientGameLogic;
import pp.mdga.client.ClientState;
import pp.mdga.client.gamestate.TurnState;
import pp.mdga.game.BonusCard;
import pp.mdga.message.client.AnimationEndMessage;
import pp.mdga.message.server.PlayCardMessage;
import pp.mdga.notification.PlayCardNotification;

public class PlayPowerCardState extends TurnStates {

    private final TurnState parent;

    private PlayCardMessage playCardMessage;
    private int extraAnimationCounter = 0;

    /**
     * Constructor
     *
     * @param parent parent state
     * @param logic  game logic
     */
    public PlayPowerCardState(ClientState parent, ClientGameLogic logic) {
        super(parent, logic);
        this.parent = (TurnState) parent;
    }

    /**
     * Enter the state
     */
    @Override
    public void enter() {
        if (playCardMessage.getCard().getCard().equals(BonusCard.SWAP)) {
            extraAnimationCounter++;
        }

        logic.addNotification(new PlayCardNotification(logic.getGame().getActiveColor(), playCardMessage.getCard().getCard()));
        logic.getGame().getActivePlayer().removeHandCard(playCardMessage.getCard());
        handlePowerCard(playCardMessage);
    }

    /**
     * Exits the state
     */
    @Override
    public void exit() {
        playCardMessage = null;
    }

    /**
     * Handle the power card
     *
     * @param playCardMessage the play card message
     */
    public void setPlayCard(PlayCardMessage playCardMessage) {
        this.playCardMessage = playCardMessage;
    }

    /**
     * The view has finished its animation
     */
    @Override
    public void selectAnimationEnd() {
        if (extraAnimationCounter > 0) {
            extraAnimationCounter--;
            return;
        }

        logic.send(new AnimationEndMessage());
        parent.setState(parent.getRollDice());
    }

    /**
     * getter for extraAnimationCounter
     *
     * @return int
     */
    public int getExtraAnimationCounter(){
        return extraAnimationCounter;
    }
}
