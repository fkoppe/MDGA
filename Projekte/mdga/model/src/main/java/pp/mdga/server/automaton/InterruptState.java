package pp.mdga.server.automaton;

import com.jme3.system.Timer;
import pp.mdga.message.client.ForceContinueGameMessage;
import pp.mdga.message.server.ResumeGameMessage;
import pp.mdga.server.ServerGameLogic;

/**
 *
 */
public class InterruptState extends ServerState {
    /**
     * Create LobbyState constants.
     */
    private static final System.Logger LOGGER = System.getLogger(InterruptState.class.getName());

    /**
     * Attributes.
     */
    private Timer timer;

    /**
     * Constructor.
     *
     * @param logic as the server game logic which is the automaton as a ServerGameLogic object.
     */
    public InterruptState(ServerGameLogic logic) {
        super(logic);
    }

    /**
     * This method will be used whenever this state will be entered.
     */
    @Override
    public void enter() {
        LOGGER.log(System.Logger.Level.DEBUG, "Entered InterruptState state.");
    }

    /**
     * This method will be used whenever this state will be exited.
     */
    @Override
    public void exit() {
        LOGGER.log(System.Logger.Level.DEBUG, "Exited InterruptState state.");
    }

    /**
     * This method will be called whenever the server received a ForceContinueGameMessage message.
     * It will also get the client id of the player who send this message.
     *
     * @param msg  as the message which was sent by the player as a ForceContinueGameMessage object.
     * @param from as the client id of the player as an Integer.
     */
    @Override
    public void received(ForceContinueGameMessage msg, int from) {
        this.logic.getServerSender().broadcast(new ResumeGameMessage());
        this.logic.setCurrentState(this.logic.getGameState());
    }
}
