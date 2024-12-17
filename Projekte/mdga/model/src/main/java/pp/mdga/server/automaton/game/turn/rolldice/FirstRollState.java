package pp.mdga.server.automaton.game.turn.rolldice;

import pp.mdga.Resources;
import pp.mdga.game.Piece;
import pp.mdga.game.PieceState;
import pp.mdga.message.client.AnimationEndMessage;
import pp.mdga.message.client.RequestDieMessage;
import pp.mdga.message.server.ChoosePieceStateMessage;
import pp.mdga.message.server.DieMessage;
import pp.mdga.server.ServerGameLogic;
import pp.mdga.server.automaton.game.turn.RollDiceState;

import java.util.ArrayList;
import java.util.List;

public class FirstRollState extends RollDiceAutomatonState {
    /**
     * Create FirstRollState constants.
     */
    private static final System.Logger LOGGER = System.getLogger(FirstRollState.class.getName());
    private List<Piece> moveablePieces;
    private int roll;
    private boolean isDied = false;

    /**
     * Constructs a server state of the specified game logic.
     *
     * @param rollDiceAutomaton as the automaton of the roll dice state as a RollDiceState object.
     * @param logic             the game logic
     */
    public FirstRollState(RollDiceState rollDiceAutomaton, ServerGameLogic logic) {
        super(rollDiceAutomaton, logic);
    }

    @Override
    public void enter() {
        LOGGER.log(System.Logger.Level.INFO, "Entered FirstRollState state.");
        roll = 0;
        isDied = false;
        moveablePieces = new ArrayList<>();
        for (Piece piece : this.logic.getGame().getPlayerByColor(this.logic.getGame().getActiveColor()).getPieces()) {
            if (piece.getState() == PieceState.HOME || piece.getState() == PieceState.ACTIVE) {
                moveablePieces.add(piece);
            }
        }
    }

    /**
     * This method will be used whenever this state will be exited.
     */
    @Override
    public void exit() {
        LOGGER.log(System.Logger.Level.INFO, "Exited FirstRollState state.");
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
        roll = this.logic.getGame().getDie().shuffle();
        this.logic.getGame().setDiceEyes(roll);
        if (roll == Resources.MAX_EYES) {
            logic.getGame().getActivePlayer().getPlayerStatistic().increaseDiced6();
            logic.getGame().getGameStatistics().increaseDiced6();
        }
        this.logic.getServerSender().broadcast(new DieMessage(roll));
        isDied = true;
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
        if (from != this.logic.getGame().getActivePlayerId()) {
            LOGGER.log(System.Logger.Level.INFO, "Received AnimationEndMessage from wrong player.");
        } else if (!isDied){
            LOGGER.log(System.Logger.Level.INFO, "Received AnimationEndMessage without the active player rolling a die.");
        } else if (!moveablePieces.isEmpty()) {
            System.out.println(!moveablePieces.isEmpty());
            this.logic.getServerSender().send(this.logic.getGame().getActivePlayerId(), new ChoosePieceStateMessage());
            this.rollDiceAutomaton.getTurnAutomaton().setCurrentState(this.rollDiceAutomaton.getTurnAutomaton().getChoosePieceState());
        } else {
            if (roll == Resources.MAX_EYES) {
                this.logic.getServerSender().send(this.logic.getGame().getActivePlayerId(), new ChoosePieceStateMessage());
                this.rollDiceAutomaton.getTurnAutomaton().setCurrentState(this.rollDiceAutomaton.getTurnAutomaton().getChoosePieceState());
            } else {
                this.rollDiceAutomaton.setCurrentState(this.rollDiceAutomaton.getSecondRollState());
            }
        }
    }

    /**
     * This method is used to get the isDied boolean.
     *
     * @return the isDied boolean.
     */
    public boolean isDied() {
        return isDied;
    }

    /**
     * This method is used to set the isDied boolean.
     *
     * @param isDied as a boolean.
     */
    public void setIsDied(boolean isDied) {
        this.isDied = isDied;
    }

    /**
     * sets the moveablePieces
     *
     * @param moveablePieces List that should be set
     */
    public void setMoveablePieces(List<Piece> moveablePieces) {
        this.moveablePieces = moveablePieces;
    }

    /**
     * adds a piece to the movablePieces
     *
     * @param piece to be added
     */
    public void addMovablePieces(Piece piece){
        moveablePieces.add(piece);
    }
}
