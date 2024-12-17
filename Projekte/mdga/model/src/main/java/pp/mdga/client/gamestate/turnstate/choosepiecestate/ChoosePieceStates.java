package pp.mdga.client.gamestate.turnstate.choosepiecestate;

import pp.mdga.client.ClientGameLogic;
import pp.mdga.client.ClientState;
import pp.mdga.client.gamestate.turnstate.TurnStates;

public abstract class ChoosePieceStates extends TurnStates {

    /**
     * Constructs a client state of the specified game logic.
     *
     * @param parent the parent state
     * @param logic the client game logic
     */
    public ChoosePieceStates(ClientState parent, ClientGameLogic logic) {
        super(parent, logic);
    }
}
