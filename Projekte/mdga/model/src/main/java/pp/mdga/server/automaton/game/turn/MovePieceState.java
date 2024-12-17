package pp.mdga.server.automaton.game.turn;

import pp.mdga.game.Color;
import pp.mdga.game.Player;
import pp.mdga.message.client.AnimationEndMessage;
import pp.mdga.message.server.ActivePlayerMessage;
import pp.mdga.message.server.DiceNowMessage;
import pp.mdga.message.server.EndOfTurnMessage;
import pp.mdga.message.server.SpectatorMessage;
import pp.mdga.server.ServerGameLogic;
import pp.mdga.server.automaton.game.TurnState;

import java.util.HashSet;
import java.util.Set;

public class MovePieceState extends TurnAutomatonState {
    /**
     * Create LobbyState constants.
     */
    private static final System.Logger LOGGER = System.getLogger(MovePieceState.class.getName());
    private Set<Player> finishedAnimations = new HashSet<>();

    /**
     * Constructs a server state of the specified game logic.
     *
     * @param turnAutomaton as the automaton of the turn state as a GameState object.
     * @param logic         the game logic
     */
    public MovePieceState(TurnState turnAutomaton, ServerGameLogic logic) {
        super(turnAutomaton, logic);
    }

    /**
     * This method will be used whenever this state will be entered.
     */
    @Override
    public void enter() {
        LOGGER.log(System.Logger.Level.DEBUG, "Entered MovePieceState state.");
    }

    /**
     * Sets the active player to the next player in the game.
     *
     * @param activePlayer the current active player color
     */
    private void setActivePlayer(Color activePlayer) {
        Color newColor = activePlayer.next(logic.getGame());
        logic.getGame().setActiveColor(newColor);
        logic.getServerSender().broadcast(new ActivePlayerMessage(newColor));
    }

    /**
     * Handles the received AnimationEndMessage.
     *
     * @param msg  the received AnimationEndMessage
     * @param from the ID of the player who sent the message
     */
    @Override
    public void received(AnimationEndMessage msg, int from) {
        finishedAnimations.add(logic.getGame().getPlayerById(from));
        if (finishedAnimations.size() == logic.getGame().getPlayers().size()) {
            if (logic.getGame().getPlayerByColor(logic.getGame().getActiveColor()).isFinished()) {
                logic.getGame().getPlayerRanking().put(logic.getGame().getPlayerRanking().size(), logic.getGame().getActivePlayer());
                if (logic.getGame().getPlayerRanking().size() == logic.getGame().getPlayers().size() - 1) {
                    logic.getGame().getPlayerRanking().put(logic.getGame().getPlayerRanking().size(), logic.getGame().getPlayerByColor(logic.getGame().getActiveColor().next(logic.getGame())));
                    logic.setCurrentState(logic.getCeremonyState());
                    return;
                }
                logic.getServerSender().send(logic.getGame().getPlayerIdByColor(logic.getGame().getActiveColor()), new SpectatorMessage());
                setActivePlayer(logic.getGame().getActiveColor());
                this.turnAutomaton.getGameAutomaton().setCurrentState(this.turnAutomaton.getGameAutomaton().getTurnState());
            }
            else if (logic.getGame().getDiceEyes() == 6) {
                logic.getServerSender().send(logic.getGame().getPlayerIdByColor(logic.getGame().getActiveColor()), new DiceNowMessage());
                this.turnAutomaton.setCurrentState(this.turnAutomaton.getRollDiceState());
            }
            else {
                logic.getServerSender().send(logic.getGame().getPlayerIdByColor(logic.getGame().getActiveColor()), new EndOfTurnMessage());
                setActivePlayer(logic.getGame().getActiveColor());
                this.turnAutomaton.getGameAutomaton().setCurrentState(this.turnAutomaton.getGameAutomaton().getTurnState());
            }
        }
    }

    /**
     * This method will be used whenever this state will be exited.
     */
    @Override
    public void exit() {
        finishedAnimations.clear();
        LOGGER.log(System.Logger.Level.DEBUG, "Exited MovePieceState state.");
    }
}
