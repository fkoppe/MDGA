package pp.mdga.client.gamestate.turnstate;

import pp.mdga.client.ClientGameLogic;
import pp.mdga.client.ClientState;
import pp.mdga.client.gamestate.TurnState;
import pp.mdga.message.client.AnimationEndMessage;
import pp.mdga.message.client.RequestDieMessage;
import pp.mdga.message.server.ChoosePieceStateMessage;
import pp.mdga.message.server.DiceNowMessage;
import pp.mdga.message.server.DieMessage;
import pp.mdga.message.server.NoTurnMessage;
import pp.mdga.notification.DiceNowNotification;
import pp.mdga.notification.RollDiceNotification;

public class RollDiceState extends TurnStates {

    private final TurnState parent;
    private boolean isRolled = false;

    /**
     * Constructs a client state of the specified game logic.
     *
     * @param parent the parent state
     * @param logic the client game logic
     */
    public RollDiceState(ClientState parent, ClientGameLogic logic) {
        super(parent, logic);
        this.parent = (TurnState) parent;
    }

    /**
     * Enters the state.
     */
    @Override
    public void enter() {
        isRolled = false;
        logic.addNotification(new DiceNowNotification());
    }

    /**
     * Exits the state.
     */
    @Override
    public void exit() {
        logic.getGame().setDiceModifier(1);
        isRolled = false;
    }

    /**
     * Gets the parent state.
     *
     * @return the parent state
     */
    public TurnState getParent() {
        return parent;
    }

    /**
     * Selects the dice.
     */
    @Override
    public void selectDice() {
        if (!isRolled) {
            isRolled = true;
            logic.send(new RequestDieMessage());
        }
    }

    /**
     * Receives the die message.
     *
     * @param msg the die message
     */
    @Override
    public void received(DieMessage msg) {
        logic.getGame().setDiceEyes(msg.getDiceEye());

        if (logic.getGame().getTurboFlag()) {
            logic.addNotification(new RollDiceNotification(logic.getGame().getPlayerById(logic.getOwnPlayerId()).getColor(), msg.getDiceEye(), logic.getGame().getDiceModifier()));
        } else {
            logic.addNotification(new RollDiceNotification(logic.getGame().getPlayerById(logic.getOwnPlayerId()).getColor(), msg.getDiceEye()));

        }
    }

    /**
     * Selects the animation end.
     */
    @Override
    public void selectAnimationEnd() {
        logic.send(new AnimationEndMessage());
    }

    /**
     * Receives the choose piece state message.
     *
     * @param msg the choose piece state message
     */
    @Override
    public void received(ChoosePieceStateMessage msg) {
        parent.setState(parent.getChoosePiece());
    }

    /**
     * Receives the no turn message.
     *
     * @param msg the no turn message
     */
    @Override
    public void received(NoTurnMessage msg) {
        parent.getParent().setState(parent.getParent().getWaiting());
    }

    /**
     * Receives the dice now message.
     *
     * @param msg the dice now message
     */
    @Override
    public void received(DiceNowMessage msg) {
        isRolled = false;
        logic.addNotification(new DiceNowNotification());
    }
}
