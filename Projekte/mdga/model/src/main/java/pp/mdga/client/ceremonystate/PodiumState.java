package pp.mdga.client.ceremonystate;

import pp.mdga.client.CeremonyState;
import pp.mdga.client.ClientGameLogic;
import pp.mdga.client.ClientState;

public class PodiumState extends CeremonyStates {

    private final CeremonyState parent;

    /**
     * This constructs a new PodiumState.
     *
     * @param parent the parent state
     * @param logic the client game logic
     */
    public PodiumState(ClientState parent, ClientGameLogic logic) {
        super(parent, logic);
        this.parent = (CeremonyState) parent;
    }

    /**
     * This method will be called whenever the client enters the PodiumState.
     */
    @Override
    public void enter() {

    }

    /**
     * This method will be called whenever the client exits the PodiumState.
     */
    @Override
    public void exit() {

    }

    /**
     * This method will be called whenever the client selects the next button.
     */
    @Override
    public void selectNext() {
        parent.setState(parent.getStatisticsState());
    }
}
