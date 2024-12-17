package pp.mdga.server.automaton.game.turn.powercard;

import pp.mdga.message.client.SelectedPiecesMessage;
import pp.mdga.message.server.PlayCardMessage;
import pp.mdga.server.ServerGameLogic;
import pp.mdga.server.automaton.game.turn.PowerCardState;

import java.util.ArrayList;

public class TurboCardState extends PowerCardAutomatonState {
    /**
     * Constructs a server state of the specified game logic.
     *
     * @param powerCardAutomaton as the automaton of the turn state as a PowerCardState object.
     * @param logic              the game logic
     */
    public TurboCardState(PowerCardState powerCardAutomaton, ServerGameLogic logic) {
        super(powerCardAutomaton, logic);
    }

    /**
     * This method will be used whenever this state will be entered.
     */
    @Override
    public void enter() {
        this.logic.getGame().getDie().modify();
        this.logic.getGame().setDiceModifier(this.logic.getGame().getDie().getDieModifier());
        this.logic.getServerSender().broadcast(new PlayCardMessage(this.powerCardAutomaton.getSelectedCard(), new ArrayList<>(), this.logic.getGame().getDiceModifier()));
        this.logic.getGame().getPlayerByColor(this.logic.getGame().getActiveColor()).removeHandCard(this.powerCardAutomaton.getSelectedCard());
        this.logic.getGame().getDiscardPile().add(this.powerCardAutomaton.getSelectedCard());
        this.powerCardAutomaton.getTurnAutomaton().getRollDiceState().setResetModifier(false);
        this.powerCardAutomaton.getTurnAutomaton().setCurrentState(this.powerCardAutomaton.getTurnAutomaton().getPlayPowerCardState());
    }

    /**
     * This method will be used whenever this state will be exited.
     */
    @Override
    public void exit() {

    }
}
