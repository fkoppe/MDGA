package pp.mdga.client.dialogstate;

import pp.mdga.client.ClientGameLogic;
import pp.mdga.client.ClientState;

public abstract class DialogStates extends ClientState {

    /**
     * Constructs a client state of the specified game logic.
     *
     * @param parent the parent state
     * @param logic the client game logic
     */
    public DialogStates(ClientState parent, ClientGameLogic logic) {
        super(parent, logic);
    }
}
