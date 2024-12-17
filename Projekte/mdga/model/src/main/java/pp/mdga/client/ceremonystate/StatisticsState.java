package pp.mdga.client.ceremonystate;

import pp.mdga.client.ClientGameLogic;
import pp.mdga.client.ClientState;
import pp.mdga.notification.StartDialogNotification;

public class StatisticsState extends CeremonyStates {

    /**
     * This constructs a new StatisticsState.
     *
     * @param parent the parent state
     * @param logic the client game logic
     */
    public StatisticsState(ClientState parent, ClientGameLogic logic) {
        super(parent, logic);
    }

    /**
     * This method will be called whenever the client enters the StatisticsState.
     */
    @Override
    public void enter() {

    }

    /**
     * This method will be called whenever the client exits the StatisticsState.
     */
    @Override
    public void exit() {

    }

    /**
     * This method will be called whenever the client selects the next button.
     */
    @Override
    public void selectNext() {
        logic.addNotification(new StartDialogNotification());
        logic.setState(logic.getDialogs());
    }
}
