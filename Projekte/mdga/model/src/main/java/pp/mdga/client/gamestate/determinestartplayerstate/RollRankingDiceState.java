package pp.mdga.client.gamestate.determinestartplayerstate;

import pp.mdga.client.ClientGameLogic;
import pp.mdga.client.ClientState;
import pp.mdga.client.gamestate.DetermineStartPlayerState;
import pp.mdga.message.client.RequestDieMessage;
import pp.mdga.message.server.DieMessage;
import pp.mdga.notification.DiceNowNotification;
import pp.mdga.notification.RollDiceNotification;

public class RollRankingDiceState extends DetermineStartPlayerStates {

    private final System.Logger LOGGER = System.getLogger(this.getClass().getName());

    private final DetermineStartPlayerState parent;
    private boolean isRolled = false;

    /**
     * Constructs a client state of the specified game logic.
     *
     * @param parent the parent state
     * @param logic the client game logic
     */
    public RollRankingDiceState(ClientState parent, ClientGameLogic logic) {
        super(parent, logic);
        this.parent = (DetermineStartPlayerState) parent;
    }

    /**
     * Enters the state.
     */
    @Override
    public void enter() {
        LOGGER.log(System.Logger.Level.INFO, "Entering RollRankingDiceState");
        logic.addNotification(new DiceNowNotification());
    }

    /**
     * Exits the state.
     */
    @Override
    public void exit() {
        LOGGER.log(System.Logger.Level.INFO, "Exiting RollRankingDiceState");
        isRolled = false;
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
     * This method is called when the server sends a DieMessage.
     *
     * @param msg the DieMessage
     */
    @Override
    public void received(DieMessage msg) {
        parent.setState(parent.getWaitRanking());
        logic.addNotification(new RollDiceNotification(logic.getGame().getPlayerById(logic.getOwnPlayerId()).getColor(), msg.getDiceEye(), true));
    }
}
