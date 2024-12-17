package pp.mdga.client.gamestate.turnstate;

import pp.mdga.client.ClientGameLogic;
import pp.mdga.client.ClientState;
import pp.mdga.client.gamestate.GameStates;

public abstract class TurnStates extends GameStates {

    /**
     * Constructs a client state of the specified game logic.
     *
     * @param parent the parent state
     * @param logic the client game logic
     */
    public TurnStates(ClientState parent, ClientGameLogic logic) {
        super(parent, logic);
    }
}
