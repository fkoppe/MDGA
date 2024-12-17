package pp.mdga.client;

import pp.mdga.message.client.ForceContinueGameMessage;
import pp.mdga.message.client.LeaveGameMessage;
import pp.mdga.message.server.ResumeGameMessage;
import pp.mdga.notification.StartDialogNotification;

public class InterruptState extends ClientState {

    private ClientState previousState;

    /**
     * Creates a new InterruptState
     *
     * @param parent the parent state
     * @param logic  the game logic
     */
    public InterruptState(ClientState parent, ClientGameLogic logic) {
        super(parent, logic);
    }

    /**
     * Enters the new state machine
     */
    @Override
    public void enter() {
        previousState = null;
    }

    /**
     * exits this state
     */
    @Override
    public void exit() {
        previousState = null;
    }

    /**
     * This method sets the stores the gamestate as previous state
     *
     * @param previousState
     */
    public void setPreviousState(ClientState previousState) {
        this.previousState = previousState;
    }

    /**
     * returns teh previous gamestate
     *
     * @return the previous gamestate
     */
    public ClientState getPreviousState() {
        return previousState;
    }

    /**
     * The host resumes the game
     */
    @Override
    public void selectResume() {
        if (logic.isHost()) {
            logic.send(new ForceContinueGameMessage());
        }
    }

    @Override
    public void selectLeave() {
        logic.send(new LeaveGameMessage());
        logic.addNotification(new StartDialogNotification());
        logic.setState(logic.getDialogs());
    }

    /**
     * The server resumes the game
     *
     * @param msg the ResumeGame message received
     */
    public void received(ResumeGameMessage msg) {
        //TODO: logic.addNotification(new ResumeNotification());
        logic.setState(previousState);
    }
}
