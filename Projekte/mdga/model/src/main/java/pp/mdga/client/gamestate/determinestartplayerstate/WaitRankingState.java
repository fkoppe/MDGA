package pp.mdga.client.gamestate.determinestartplayerstate;

import pp.mdga.client.ClientGameLogic;
import pp.mdga.client.ClientState;
import pp.mdga.client.gamestate.DetermineStartPlayerState;
import pp.mdga.game.Color;
import pp.mdga.message.client.AnimationEndMessage;
import pp.mdga.message.server.ActivePlayerMessage;
import pp.mdga.message.server.DiceNowMessage;
import pp.mdga.message.server.RankingResponseMessage;
import pp.mdga.notification.RankingResponceNotification;

import java.util.HashMap;
import java.util.Map;

public class WaitRankingState extends DetermineStartPlayerStates {

    private final System.Logger LOGGER = System.getLogger(this.getClass().getName());

    private final DetermineStartPlayerState parent;
    private boolean canChange = false;

    /**
     * Constructs a client state of the specified game logic.
     *
     * @param parent the parent state
     * @param logic the client game logic
     */
    public WaitRankingState(ClientState parent, ClientGameLogic logic) {
        super(parent, logic);
        this.parent = (DetermineStartPlayerState) parent;
    }

    /**
     * Changes the state to the intro state.
     */
    private void changeToIntro() {
        if (!canChange) {
            canChange = true;
            return;
        }
        parent.setState(parent.getIntro());
    }

    /**
     * Enters the state.
     */
    @Override
    public void enter() {
        LOGGER.log(System.Logger.Level.INFO, "Entering WaitRankingState");
    }

    /**
     * Exits the state.
     */
    @Override
    public void exit() {
        canChange = false;
        LOGGER.log(System.Logger.Level.INFO, "Exiting WaitRankingState");
    }

    /**
     * This method is called when the server sends a DiceNowMessage.
     *
     * @param msg the DiceNowMessage
     */
    @Override
    public void received(DiceNowMessage msg) {
        parent.setState(parent.getRollRankingDice());
    }

    /**
     * This method is called when the server sends a RankingResponseMessage.
     *
     * @param msg the RankingResponseMessage
     */
    @Override
    public void received(RankingResponseMessage msg) {
        Map<Color, Integer> rankingResults = new HashMap<>();
        for (var entry : msg.getRankingResults().entrySet()) {
            rankingResults.put(logic.getGame().getPlayerById(entry.getKey()).getColor(), entry.getValue());
        }
        logic.addNotification(new RankingResponceNotification(rankingResults));
    }

    /**
     * This method is called when the view has completed the animation.
     */
    @Override
    public void selectAnimationEnd() {
        changeToIntro();
        logic.send(new AnimationEndMessage());
    }

    /**
     * This method is called when the server sends an ActivePlayerMessage.
     *
     * @param msg the ActivePlayerMessage
     */
    @Override
    public void received(ActivePlayerMessage msg) {
        logic.getGame().setActiveColor(msg.getColor());
        changeToIntro();
    }
}
