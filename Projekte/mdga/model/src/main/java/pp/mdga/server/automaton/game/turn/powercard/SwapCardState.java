package pp.mdga.server.automaton.game.turn.powercard;

import pp.mdga.game.Board;
import pp.mdga.game.Node;
import pp.mdga.game.Piece;
import pp.mdga.game.card.SwapCard;
import pp.mdga.message.client.SelectedPiecesMessage;
import pp.mdga.message.server.IncorrectRequestMessage;
import pp.mdga.message.server.PlayCardMessage;
import pp.mdga.message.server.PossiblePieceMessage;
import pp.mdga.server.ServerGameLogic;
import pp.mdga.server.automaton.game.turn.PowerCardState;

import java.util.List;

public class SwapCardState extends PowerCardAutomatonState {

    private final System.Logger LOGGER = System.getLogger(this.getClass().getName());

    /**
     * Constructs a server state of the specified game logic.
     *
     * @param powerCardAutomaton as the automaton of the turn state as a PowerCardState object.
     * @param logic              the game logic
     */
    public SwapCardState(PowerCardState powerCardAutomaton, ServerGameLogic logic) {
        super(powerCardAutomaton, logic);
    }

    /**
     * This method will be used whenever this state will be entered.
     */
    @Override
    public void enter() {
        LOGGER.log(System.Logger.Level.INFO, "Entered SwapCardState");
        logic.getServerSender().send(logic.getGame().getActivePlayerId(), PossiblePieceMessage.swapPossiblePieces(this.powerCardAutomaton.getVisitor().getSwapOwnPieces(), this.powerCardAutomaton.getVisitor().getSwapOtherPieces()));
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
        if (msg.getPieces().size() == 2) {
            Piece selOwn = msg.getPieces().get(0);
            Piece selEnemy = msg.getPieces().get(1);
            System.out.println("Server: Swapcard: ownPiece: " + selOwn + " enemyPiece: " + selEnemy);
            List<Piece> ownPieces = this.powerCardAutomaton.getVisitor().getSwapOwnPieces();
            List<Piece> enemyPieces = this.powerCardAutomaton.getVisitor().getSwapOtherPieces();

            //if selOwn and selEnemy is in wrong order
            if (ownPieces.contains(selEnemy) && enemyPieces.contains(selOwn)) {
                Piece temp = selEnemy;
                selEnemy = selOwn;
                selOwn = temp;
            }

            if (ownPieces.contains(selOwn) && enemyPieces.contains(selEnemy)) {
                this.powerCardAutomaton.addSelectedPiece(selOwn);
                this.powerCardAutomaton.addSelectedPiece(selEnemy);

                if (!(powerCardAutomaton.getSelectedCard() instanceof SwapCard))
                    throw new RuntimeException("getSelectedCard is not swapCard");

                swapPieces(selOwn, selEnemy);

                logic.getServerSender().broadcast(new PlayCardMessage(this.powerCardAutomaton.getSelectedCard(), msg.getPieces(), 1));

                this.logic.getGame().getPlayerByColor(this.logic.getGame().getActiveColor()).removeHandCard(this.powerCardAutomaton.getSelectedCard());
                this.logic.getGame().getDiscardPile().add(this.powerCardAutomaton.getSelectedCard());

                this.powerCardAutomaton.getTurnAutomaton().setCurrentState(this.powerCardAutomaton.getTurnAutomaton().getPlayPowerCardState());
            }
        } else {
            this.logic.getServerSender().send(from, new IncorrectRequestMessage(4));
        }
    }

    /**
     * This method will swap the pieces of the given messageOwn and messageEnemy.
     *
     * @param messageOwn   as the own piece as a Piece object.
     * @param messageEnemy as the enemy piece as a Piece object.
     */
    private void swapPieces(Piece messageOwn, Piece messageEnemy) {
        //swap Pieces in Model
        Board board = logic.getGame().getBoard();
        Piece modelOwn = logic.getGame().getPieceThroughUUID(messageOwn.getUuid());
        Piece modelEnemy = logic.getGame().getPieceThroughUUID(messageEnemy.getUuid());
        Node ownNode = board.getInfield()[board.getInfieldIndexOfPiece(modelOwn)];
        Node enemyNode = board.getInfield()[board.getInfieldIndexOfPiece(modelEnemy)];

        ownNode.setOccupant(modelEnemy);
        enemyNode.setOccupant(modelOwn);
    }
}
