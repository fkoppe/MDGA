package pp.mdga.client.dialogstate;

import pp.mdga.client.ClientGameLogic;
import pp.mdga.client.ClientState;
import pp.mdga.client.DialogsState;

public class StartDialogState extends DialogStates {

    private final DialogsState parent;

    /**
     * Constructor for the StartDialogState
     *
     * @param parent the parent state
     * @param logic  the logic
     */
    public StartDialogState(ClientState parent, ClientGameLogic logic) {
        super(parent, logic);
        this.parent = (DialogsState) parent;
    }

    /**
     * Enter the state
     */
    @Override
    public void enter() {
    }

    /**
     * Exit the state
     */
    @Override
    public void exit() {
    }

    /**
     * Set the name
     *
     * @param name the name
     */
    @Override
    public void setName(String name) {
        logic.setOwnPlayerName(name);
        parent.setState(parent.getNetworkDialog());
    }

    /**
     * Select the leave option
     */
    @Override
    public void selectLeave() {
        parent.exit();
    }
}
