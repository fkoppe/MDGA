package pp.mdga.server.automaton.game.turn.powercard;

import pp.mdga.game.Piece;
import pp.mdga.game.ShieldState;
import pp.mdga.message.client.SelectedPiecesMessage;
import pp.mdga.message.server.IncorrectRequestMessage;
import pp.mdga.message.server.PlayCardMessage;
import pp.mdga.message.server.PossiblePieceMessage;
import pp.mdga.server.ServerGameLogic;
import pp.mdga.server.automaton.game.turn.PowerCardState;

import java.util.ArrayList;

public class ShieldCardState extends PowerCardAutomatonState {
    /**
     * Constructs a server state of the specified game logic.
     *
     * @param powerCardAutomaton as the automaton of the turn state as a PowerCardState object.
     * @param logic              the game logic
     */
    public ShieldCardState(PowerCardState powerCardAutomaton, ServerGameLogic logic) {
        super(powerCardAutomaton, logic);
    }

    /**
     * This method will be used whenever this state will be entered.
     */
    @Override
    public void enter() {
        logic.getServerSender().send(logic.getGame().getActivePlayerId(), PossiblePieceMessage.shieldPossiblePieces(this.powerCardAutomaton.getVisitor().getShieldPieces()));
    }

    /**
     * This method will be used whenever this state will be exited.
     */
    @Override
    public void exit() {

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
        if (msg.getPieces().size() == 1 && this.powerCardAutomaton.getVisitor().getShieldPieces().contains(msg.getPieces().get(0))) {
            this.powerCardAutomaton.addSelectedPiece(msg.getPieces().get(0));
            for (Piece piece : this.logic.getGame().getPlayerByColor(this.logic.getGame().getActiveColor()).getPieces()) {
                if (piece.getUuid().equals(msg.getPieces().get(0).getUuid())) {
                    piece.setShield(ShieldState.ACTIVE);
                }
            }
            ArrayList<Piece> temp = new ArrayList<>();
            temp.add(msg.getPieces().get(0));
            this.logic.getServerSender().broadcast(new PlayCardMessage(this.powerCardAutomaton.getSelectedCard(), temp, 1));
            this.logic.getGame().getPlayerByColor(this.logic.getGame().getActiveColor()).removeHandCard(this.powerCardAutomaton.getSelectedCard());
            this.logic.getGame().getDiscardPile().add(this.powerCardAutomaton.getSelectedCard());
            this.powerCardAutomaton.getTurnAutomaton().setCurrentState(this.powerCardAutomaton.getTurnAutomaton().getPlayPowerCardState());
        } else {
            this.logic.getServerSender().send(from, new IncorrectRequestMessage(3));
        }
    }
}
