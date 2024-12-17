package pp.mdga.server.automaton.game.turn.choosepiece;

import pp.mdga.Resources;
import pp.mdga.game.Color;
import pp.mdga.message.server.ActivePlayerMessage;
import pp.mdga.message.server.DiceNowMessage;
import pp.mdga.message.server.EndOfTurnMessage;
import pp.mdga.server.ServerGameLogic;
import pp.mdga.server.automaton.game.turn.ChoosePieceState;

public class NoTurnState extends ChoosePieceAutomatonState {
    /**
     * Create FirstRollState constants.
     */
    private static final System.Logger LOGGER = System.getLogger(NoTurnState.class.getName());

    /**
     * Constructs a server state of the specified game logic.
     *
     * @param choosePieceAutomaton as the automaton of the choose piece state as a ChoosePieceState object.
     * @param logic                the game logic
     */
    public NoTurnState(ChoosePieceState choosePieceAutomaton, ServerGameLogic logic) {
        super(choosePieceAutomaton, logic);
    }

    /**
     * This method is used to end the turn of the active player and determine a new active player
     *
     * @param color the color of the current active player
     */
    private void setActivePlayer(Color color) {
        if (!logic.getGame().getPlayerByColor(color.next(logic.getGame())).isFinished()) {
            logic.getGame().setActiveColor(logic.getGame().getActiveColor().next(logic.getGame()));
            logic.getServerSender().broadcast(new ActivePlayerMessage(logic.getGame().getActiveColor()));
        } else {
            setActivePlayer(color.next(logic.getGame()));
        }
        this.choosePieceAutomaton.getTurnAutomaton().getGameAutomaton().setCurrentState(this.choosePieceAutomaton.getTurnAutomaton().getGameAutomaton().getTurnState());
    }

    /**
     * This method will be used whenever this state will be entered.
     */
    @Override
    public void enter() {
        LOGGER.log(System.Logger.Level.DEBUG, "Entered NoTurnState state.");
        if (logic.getGame().getDiceEyes() == Resources.MAX_EYES){
            logic.getServerSender().send(logic.getGame().getActivePlayerId(), new DiceNowMessage());
            this.choosePieceAutomaton.getTurnAutomaton().setCurrentState(this.choosePieceAutomaton.getTurnAutomaton().getRollDiceState());
        } else {
            logic.getServerSender().send(logic.getGame().getActivePlayerId(), new EndOfTurnMessage());
            setActivePlayer(logic.getGame().getActiveColor());
        }
    }

    /**
     * This method will be used whenever this state will be exited.
     */
    @Override
    public void exit() {
        LOGGER.log(System.Logger.Level.DEBUG, "Exited NoTurnState state.");
    }
}
