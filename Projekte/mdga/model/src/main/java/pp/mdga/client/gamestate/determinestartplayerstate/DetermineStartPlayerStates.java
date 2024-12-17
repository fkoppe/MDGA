package pp.mdga.client.gamestate.determinestartplayerstate;

import pp.mdga.client.ClientGameLogic;
import pp.mdga.client.ClientState;
import pp.mdga.client.gamestate.GameStates;

public abstract class DetermineStartPlayerStates extends GameStates {

    /**
     * Constructs a client state of the specified game logic.
     *
     * @param parent the parent state
     * @param logic the client game logic
     */
    public DetermineStartPlayerStates(ClientState parent, ClientGameLogic logic) {
        super(parent, logic);
    }

}
