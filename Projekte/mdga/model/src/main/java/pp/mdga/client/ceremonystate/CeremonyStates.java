package pp.mdga.client.ceremonystate;

import pp.mdga.client.ClientGameLogic;
import pp.mdga.client.ClientState;

public abstract class CeremonyStates extends ClientState {

    /**
     * Constructs a client state of the specified game logic.
     *
     * @param parent the parent state
     * @param logic the client game logic
     */
    protected CeremonyStates(ClientState parent, ClientGameLogic logic) {
        super(parent, logic);
    }
}
