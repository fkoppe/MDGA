package pp.mdga.server.automaton.game;

import pp.mdga.game.Piece;
import pp.mdga.game.Player;
import pp.mdga.game.ShieldState;
import pp.mdga.message.client.*;
import pp.mdga.server.ServerGameLogic;
import pp.mdga.server.automaton.GameState;
import pp.mdga.server.automaton.game.turn.*;

import java.lang.System.Logger.Level;

/**
 * This class represents the turn state of the server state automaton.
 * It will also be used as the turn automaton.
 */
public class TurnState extends GameAutomatonState {
    /**
     * Create FirstRollState constants.
     */
    private static final System.Logger LOGGER = System.getLogger(TurnState.class.getName());
    /**
     * Create TurnState states.
     */
    private TurnAutomatonState currentState;
    private final PowerCardState powerCardState;
    private final PlayPowerCardState playPowerCardState;
    private final RollDiceState rollDiceState;
    private final ChoosePieceState choosePieceState;
    private final MovePieceState movePieceState;
    private Player player;

    /**
     * Constructs a server state of the specified game logic.
     *
     * @param gameAutomaton as the automaton of the game state as a GameState object.
     * @param logic         the game logic
     */
    public TurnState(GameState gameAutomaton, ServerGameLogic logic) {
        super(gameAutomaton, logic);
        this.powerCardState = new PowerCardState(this, logic);
        this.playPowerCardState = new PlayPowerCardState(this, logic);
        this.rollDiceState = new RollDiceState(this, logic);
        this.choosePieceState = new ChoosePieceState(this, logic);
        this.movePieceState = new MovePieceState(this, logic);
    }

    /**
     * This method will be used whenever this state will be entered.
     */
    @Override
    public void enter() {
        LOGGER.log(Level.INFO, "Entered TurnState state.");
        this.player = this.logic.getGame().getPlayerById(this.logic.getGame().getActivePlayerId());
        for (Piece piece : this.player.getPieces()) {
            piece.setShield(ShieldState.NONE);
        }
        this.setCurrentState(this.powerCardState);
    }

    /**
     * This method will be used whenever this state will be exited.
     */
    @Override
    public void exit() {
        LOGGER.log(Level.DEBUG, "Exited TurnState state.");
    }

    /**
     * This method will be called whenever the server received an SelectedPiecesMessage message.
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
     * This method will be called whenever the server received a RequestMoveMessage message.
     * It will also get the client id of the player who send this message.
     *
     * @param msg  as the message which was sent by the player as a RequestMoveMessage object.
     * @param from as the client id of the player as an Integer.
     */
    @Override
    public void received(RequestMoveMessage msg, int from) {
        this.currentState.received(msg, from);
    }

    /**
     * This method will be called whenever the server received a AnimationEndMessage message.
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
     * This method will be used to return currentState attribute of TurnState class.
     *
     * @return currentState as a TurnAutomatonState object.
     */
    public TurnAutomatonState getCurrentState() {
        return this.currentState;
    }

    /**
     * This method will be used to return powerCardState attribute of TurnState class.
     *
     * @return powerCardState as a PowerCardState object.
     */
    public PowerCardState getPowerCardState() {
        return this.powerCardState;
    }

    /**
     * This method will be used to return playPowerCardState attribute of TurnState class.
     *
     * @return playPowerState as a PlayPowerCardState object.
     */
    public PlayPowerCardState getPlayPowerCardState() {
        return this.playPowerCardState;
    }

    /**
     * This method will be used to return rollDiceState attribute of TurnState class.
     *
     * @return rollDiceState as a RollDiceState object.
     */
    public RollDiceState getRollDiceState() {
        return this.rollDiceState;
    }

    /**
     * This method will be used to return choosePieceState attribute of TurnState class.
     *
     * @return choosePieceState as a ChoosePieceState object.
     */
    public ChoosePieceState getChoosePieceState() {
        return this.choosePieceState;
    }

    /**
     * This method will be used to return movePieceState attribute of TurnState class.
     *
     * @return movePieceState as a MovePieceState object.
     */
    public MovePieceState getMovePieceState() {
        return this.movePieceState;
    }

    /**
     * This method will be used to return player attribute of TurnState class.
     *
     * @return player as a Player object.
     */
    public Player getPlayer() {
        return this.player;
    }

    /**
     * This method will be used to set currentState attribute of TurnState class to the given state parameter.
     * In Addition, the currentState will be exited, changed and entered.
     *
     * @param state as the new currentState attribute as a TurnAutomatonState object.
     */
    public void setCurrentState(TurnAutomatonState state) {
        if (this.currentState != null) {
            this.currentState.exit();
        }
        System.out.println("Server: the server entered:" + state);
        this.currentState = state;
        this.currentState.enter();
    }

    public void setPlayer(Player playe) {
        this.player = player;
    }
}
