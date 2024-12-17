package pp.mdga.client.gamestate;

import pp.mdga.client.ClientGameLogic;
import pp.mdga.client.ClientState;
import pp.mdga.client.GameState;
import pp.mdga.client.gamestate.determinestartplayerstate.DetermineStartPlayerStates;
import pp.mdga.client.gamestate.determinestartplayerstate.Intro;
import pp.mdga.client.gamestate.determinestartplayerstate.RollRankingDiceState;
import pp.mdga.client.gamestate.determinestartplayerstate.WaitRankingState;
import pp.mdga.message.server.*;

public class DetermineStartPlayerState extends GameStates {

    private final GameState parent;
    private DetermineStartPlayerStates state;

    private final RollRankingDiceState rollRankingDiceState = new RollRankingDiceState(this, logic);
    private final WaitRankingState waitRankingState = new WaitRankingState(this, logic);
    private final Intro intro = new Intro(this, logic);

    /**
     * Constructs a client state of the specified game logic.
     *
     * @param parent the parent state
     * @param logic the client game logic
     */
    public DetermineStartPlayerState(ClientState parent, ClientGameLogic logic) {
        super(parent, logic);
        this.parent = (GameState) parent;
    }

    /**
     * Gets the roll ranking dice.
     *
     * @return the roll ranking dice
     */
    public RollRankingDiceState getRollRankingDice() {
        return rollRankingDiceState;
    }

    /**
     * Gets the wait ranking.
     *
     * @return the wait ranking
     */
    public WaitRankingState getWaitRanking() {
        return waitRankingState;
    }

    /**
     * Gets the state.
     *
     * @return the state
     */
    public DetermineStartPlayerStates getState() {
        return state;
    }

    /**
     * Gets the intro.
     *
     * @return the intro
     */
    public Intro getIntro() {
        return intro;
    }

    /**
     * Gets the parent state.
     *
     * @return the parent state
     */
    public GameState getParent() {
        return parent;
    }

    /**
     * Enters the state.
     */
    @Override
    public void enter() {
        this.setState(this.rollRankingDiceState);
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
    public void setState(DetermineStartPlayerStates state) {
        System.out.println("CLIENT STATE old: " + this.state + " new: " + state);
        if (this.state != null) {
            this.state.exit();
        }
        this.state = state;
        this.state.enter();
    }

    /**
     * Selects the dice.
     */
    @Override
    public void selectDice() {
        state.selectDice();
    }

    /**
     * Selects the animation end.
     */
    @Override
    public void selectAnimationEnd() {
        state.selectAnimationEnd();
    }

    /**
     * Receives the die message.
     *
     * @param msg the die message
     */
    @Override
    public void received(DieMessage msg) {
        state.received(msg);
    }

    /**
     * Receives the ceremony message.
     *
     * @param msg the ceremony message
     */
    @Override
    public void received(DiceNowMessage msg) {
        state.received(msg);
    }

    /**
     * Receives the ranking roll again message.
     *
     * @param msg the ranking roll again message
     */
    @Override
    public void received(RankingRollAgainMessage msg) {
        state.received(msg);
    }

    /**
     * Receives the ranking response message.
     *
     * @param msg the ranking response message
     */
    @Override
    public void received(RankingResponseMessage msg) {
        state.received(msg);
    }

    /**
     * Receives the ranking result message.
     *
     * @param msg the ranking result message
     */
    @Override
    public void received(ActivePlayerMessage msg) {
        state.received(msg);
    }
}
