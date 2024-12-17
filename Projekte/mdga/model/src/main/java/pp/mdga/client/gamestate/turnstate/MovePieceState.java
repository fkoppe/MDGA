package pp.mdga.client.gamestate.turnstate;

import pp.mdga.client.ClientGameLogic;
import pp.mdga.client.ClientState;
import pp.mdga.client.gamestate.TurnState;
import pp.mdga.message.client.AnimationEndMessage;
import pp.mdga.message.server.CeremonyMessage;
import pp.mdga.message.server.DiceNowMessage;
import pp.mdga.message.server.EndOfTurnMessage;
import pp.mdga.message.server.SpectatorMessage;

public class MovePieceState extends TurnStates {

    private final TurnState parent;

    /**
     * Constructs a client state of the specified game logic.
     *
     * @param parent the parent state
     * @param logic the client game logic
     */
    public MovePieceState(ClientState parent, ClientGameLogic logic) {
        super(parent, logic);
        this.parent = (TurnState) parent;
    }

    /**
     * Enters the state.
     */
    @Override
    public void enter() {

    }

    /**
     * Exits the state.
     */
    @Override
    public void exit() {

    }

    /**
     * Selects the animation end.
     */
    @Override
    public void selectAnimationEnd() {
        logic.send(new AnimationEndMessage());
    }

    /**
     * Receives the ceremony message.
     *
     * @param msg the ceremony message
     */
    @Override
    public void received(CeremonyMessage msg) {
        logic.setState(logic.getCeremony());
    }

    /**
     * Receives the end of turn message.
     *
     * @param msg the end of turn message
     */
    @Override
    public void received(EndOfTurnMessage msg) {
        parent.getParent().setState(parent.getParent().getWaiting());
    }

    /**
     * Receives the spectator message.
     *
     * @param msg the spectator message
     */
    @Override
    public void received(SpectatorMessage msg) {
        parent.getParent().setState(parent.getParent().getSpectator());
    }

    /**
     * Receives the dice now message.
     *
     * @param msg the dice now message
     */
    @Override
    public void received(DiceNowMessage msg) {
        parent.setState(parent.getRollDice());
    }

    /**
     * Gets the parent state.
     *
     * @return the parent state
     */
    public TurnState getParent() {
        return parent;
    }
}
