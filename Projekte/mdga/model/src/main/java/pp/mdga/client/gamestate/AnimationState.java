package pp.mdga.client.gamestate;

import pp.mdga.client.ClientGameLogic;
import pp.mdga.client.ClientState;
import pp.mdga.client.GameState;
import pp.mdga.message.client.AnimationEndMessage;

public class AnimationState extends GameStates {

    private final System.Logger LOGGER = System.getLogger(this.getClass().getName());

    private final GameState parent;

    private boolean spectator = false;

    /**
     * Constructs a client state of the specified game logic.
     *
     * @param parent the parent state
     * @param logic the client game logic
     */
    public AnimationState(ClientState parent, ClientGameLogic logic) {
        super(parent, logic);
        this.parent = (GameState) parent;
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
        spectator = false;
    }

    /**
     * Sets the spectator.
     *
     * @param spectator the spectator
     */
    public void setSpectator(boolean spectator) {
        this.spectator = spectator;
    }

    /**
     * Selects the animation end.
     */
    @Override
    public void selectAnimationEnd() {
        logic.send(new AnimationEndMessage());
        if (spectator){
            parent.setState(parent.getSpectator());
        } else {
            parent.setState(parent.getWaiting());
        }
    }
}
