package pp.mdga.client;

import pp.mdga.Resources;
import pp.mdga.client.ceremonystate.CeremonyStates;
import pp.mdga.client.ceremonystate.PodiumState;
import pp.mdga.client.ceremonystate.StatisticsState;
import pp.mdga.message.server.ShutdownMessage;
import pp.mdga.notification.InfoNotification;
import pp.mdga.notification.StartDialogNotification;

public class CeremonyState extends ClientState {

    private CeremonyStates currentState;

    private final PodiumState podiumState = new PodiumState(this, logic);
    private final StatisticsState statisticsState = new StatisticsState(this, logic);

    /**
     * Creates a new CeremonyState
     *
     * @param parent the parent state
     * @param logic  the game logic
     */
    public CeremonyState(ClientState parent, ClientGameLogic logic) {
        super(parent, logic);
    }

    /**
     * Enters the new state machine
     */
    @Override
    public void enter() {
        setState(podiumState);
    }

    /**
     * exits this state
     */
    @Override
    public void exit() {
        currentState.exit();
    }

    /**
     * This method is used to set a new SubState
     *
     * @param state the state to be set
     */
    public void setState(CeremonyStates state) {
        if (this.currentState != null) {
            this.currentState.exit();
        }
        state.enter();
        currentState = state;
    }

    /**
     * This method get the PodiumState
     *
     * @return the PodiumState
     */
    public PodiumState getPodiumState() {
        return podiumState;
    }

    /**
     * This method get the StatisticsState
     *
     * @return the StatisticsState
     */
    public StatisticsState getStatisticsState() {
        return statisticsState;
    }

    /**
     * This method is used to get the current State
     *
     * @return the current State
     */
    public CeremonyStates getState() {
        return currentState;
    }

    @Override
    public void received(ShutdownMessage msg){
        logic.addNotification(new InfoNotification(Resources.stringLookup("server.shutdown")));
    }

    /**
     * this method is used to parse the selectNext from the clientGameLogic
     */
    @Override
    public void selectNext() {
        currentState.selectNext();
    }
}
