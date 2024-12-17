package pp.mdga.server.automaton;

import pp.mdga.message.client.*;
import pp.mdga.message.server.CeremonyMessage;
import pp.mdga.message.server.PauseGameMessage;
import pp.mdga.server.ServerGameLogic;
import pp.mdga.server.automaton.game.AnimationState;
import pp.mdga.server.automaton.game.DetermineStartPlayerState;
import pp.mdga.server.automaton.game.GameAutomatonState;
import pp.mdga.server.automaton.game.TurnState;

/**
 * This class represents the game state of this application.
 * In Addition, it will be used as a state machine for the game process.
 */
public class GameState extends ServerState {
    /**
     * Create LobbyState constants.
     */
    private static final System.Logger LOGGER = System.getLogger(GameState.class.getName());

    /**
     * Create GameState states.
     */
    private GameAutomatonState currentState;
    private final DetermineStartPlayerState determineStartPlayerState;
    private final AnimationState animationState;
    private final TurnState turnState;

    /**
     * Constructor.
     *
     * @param logic as the server game logic which is the automaton as a ServerGameLogic object.
     */
    public GameState(ServerGameLogic logic) {
        super(logic);
        this.determineStartPlayerState = new DetermineStartPlayerState(this, logic);
        this.animationState = new AnimationState(this, logic);
        this.turnState = new TurnState(this, logic);
    }

    /**
     * This method will be used whenever this state will be entered.
     */
    @Override
    public void enter() {
        LOGGER.log(System.Logger.Level.DEBUG, "Entered GameState state.");
        this.setCurrentState(this.determineStartPlayerState);
    }

    /**
     * This method will be used whenever this state will be exited.
     */
    @Override
    public void exit() {
        LOGGER.log(System.Logger.Level.DEBUG, "Exited GameState state.");
    }

    /**
     * This method will be called whenever the server received a DisconnectedMessage message.
     * It will also get the client id of the player who send this message.
     *
     * @param msg  as the message which was sent by the player as a Disconnected object.
     * @param from as the client id of the player as an Integer.
     */
    @Override
    public void received(DisconnectedMessage msg, int from) {
        this.logic.getServerSender().broadcast(new PauseGameMessage());
        this.logic.setCurrentState(this.logic.getInterruptState());
    }

    /**
     * This method will be called whenever the server received an LeaveGameMessage message.
     * It will also get the client id of the player who send this message.
     *
     * @param msg  as the message which was sent by the player as a LeaveGame object.
     * @param from as the client id of the player as an Integer.
     */
    @Override
    public void received(LeaveGameMessage msg, int from) {
        this.logic.getGame().updatePlayerActiveState(from, false);
        if (this.logic.getGame().getNumberOfActivePlayers() == 1) {
            this.logic.getServerSender().broadcast(new CeremonyMessage());
            this.logic.setCurrentState(this.logic.getCeremonyState());
        }
    }

    /**
     * This method will be called whenever the server received an NoPowerCardMessage message.
     * It will also get the client id of the player who send this message.
     *
     * @param msg  as the message which was sent by the player as a NoPowerCardMessage object.
     * @param from as the client id of the player as an Integer.
     */
    @Override
    public void received(NoPowerCardMessage msg, int from) {
        this.currentState.received(msg, from);
    }

    /**
     * This method will be called whenever the server received a SelectCardMessage message.
     * It will also get the client id of the player who send this message.
     *
     * @param msg  as the message which was sent by the player as a SelectCardMessage object.
     * @param from as the client id of the player as an Integer.
     */
    public void received(SelectCardMessage msg, int from) {
        currentState.received(msg, from);
    }
    /**
     * This method will be called whenever the server received a RequestMoveMessage message.
     * It will also get the client id of the player who send this message.
     *
     * @param msg  as the message which was sent by the player as a RequestMoveMessage object.
     * @param from as the client id of the player as an Integer.
     */
    public void received(RequestMoveMessage msg, int from) {
        this.currentState.received(msg, from);
    }

    /**
     * This method will be called whenever the server received a SelectedPiecesMessage message.
     * It will also get the client id of the player who send this message.
     *
     * @param msg  as the message which was sent by the player as a SelectedPiecesMessage object.
     * @param from as the client id of the player as an Integer.
     */
    @Override
    public void received(SelectedPiecesMessage msg, int from) {
        this.currentState.received(msg, from);
    }

    /**
     * This method will be called whenever the server received a RequestDieMessage message.
     * It will also get the client id of the player who send this message.
     *
     * @param msg  as the message which was sent by the player as a RequestDieMessage object.
     * @param from as the client id of the player as an Integer.
     */
    @Override
    public void received(RequestDieMessage msg, int from) {
        this.currentState.received(msg, from);
    }

    /**
     * This method will be called whenever the server received an AnimationEndMessage message.
     * It will also get the client id of the player who send this message.
     *
     * @param msg  as the message which was sent by the player as a AnimationEndMessage object.
     * @param from as the client id of the player as an Integer.
     */
    @Override
    public void received(AnimationEndMessage msg, int from) {
        this.currentState.received(msg, from);
    }

    /**
     * This method will be used to return currentState attribute of GameState class.
     *
     * @return currentState as a GameAutomatonState object.
     */
    public GameAutomatonState getCurrentState() {
        return this.currentState;
    }

    /**
     * This method will be used to return determineStartPlayerState attribute of GameState class.
     *
     * @return determineStartPlayerState as a DetermineStartPlayerState object.
     */
    public DetermineStartPlayerState getDetermineStartPlayerState() {
        return this.determineStartPlayerState;
    }

    /**
     * This method will be used to return animationState attribute of GameState class.
     *
     * @return animationState as a AnimationState object.
     */
    public AnimationState getAnimationState() {
        return this.animationState;
    }

    /**
     * This method will be used to return turnState attribute of GameState class.
     *
     * @return turnState as a TurnState object.
     */
    public TurnState getTurnState() {
        return this.turnState;
    }

    /**
     * This method will be used to set currentState attribute of GameState class to the given state parameter.
     * In Addition, the currentState will be exited, changed and entered.
     *
     * @param state as the new currentState attribute as a GameAutomatonState object.
     */
    public void setCurrentState(GameAutomatonState state) {
        if (this.currentState != null) {
            this.currentState.exit();
        }
        this.currentState = state;
        this.currentState.enter();
    }
}
