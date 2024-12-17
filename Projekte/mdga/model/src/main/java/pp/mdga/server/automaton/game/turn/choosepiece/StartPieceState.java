package pp.mdga.server.automaton.game.turn.choosepiece;

import pp.mdga.game.Node;
import pp.mdga.game.Piece;
import pp.mdga.game.Player;
import pp.mdga.game.card.HiddenCard;
import pp.mdga.game.card.PowerCard;
import pp.mdga.message.client.RequestMoveMessage;
import pp.mdga.message.server.DrawCardMessage;
import pp.mdga.message.server.IncorrectRequestMessage;
import pp.mdga.message.server.MoveMessage;
import pp.mdga.message.server.StartPieceMessage;
import pp.mdga.server.ServerGameLogic;
import pp.mdga.server.automaton.game.turn.ChoosePieceState;

public class StartPieceState extends ChoosePieceAutomatonState {
    /**
     * Create FirstRollState constants.
     */
    private static final System.Logger LOGGER = System.getLogger(StartPieceState.class.getName());

    private Piece piece;

    /**
     * Constructs a server state of the specified game logic.
     *
     * @param choosePieceAutomaton as the automaton of the choose piece state as a ChoosePieceState object.
     * @param logic                the game logic
     */
    public StartPieceState(ChoosePieceState choosePieceAutomaton, ServerGameLogic logic) {
        super(choosePieceAutomaton, logic);
    }

    /**
     * This method will be used whenever this state will be entered.
     */
    @Override
    public void enter() {
        LOGGER.log(System.Logger.Level.INFO, "Enter StartPieceState state.");
        piece = logic.getGame().getBoard().getInfield()[logic.getGame().getPlayerByColor(logic.getGame().getActiveColor()).getStartNodeIndex()].getOccupant();
        logic.getServerSender().send(logic.getGame().getActivePlayerId(), new StartPieceMessage(piece.getUuid(), calculateTargetIndex(piece)));
    }

    @Override
    public void received(RequestMoveMessage msg, int from) {
        LOGGER.log(System.Logger.Level.INFO, "Received RequestMoveMessage message. is piece equals: " + piece.equals(msg.getPiece()));
        if (piece.equals(msg.getPiece())) {
            int targetIndex = calculateTargetIndex(piece);
            Node targetNode = logic.getGame().getBoard().getInfield()[targetIndex];
            logic.getGame().getActivePlayer().getPlayerStatistic().increaseTraveledNodes(logic.getGame().getDiceEyes() * logic.getGame().getDiceModifier());
            logic.getGame().getGameStatistics().increaseTraveledNodes(logic.getGame().getDiceEyes() * logic.getGame().getDiceModifier());

            //send MoveMessage
            logic.getServerSender().broadcast(new MoveMessage(piece, false, targetIndex));

            //clear old piece from node
            logic.getGame().getBoard().getInfield()[logic.getGame().getBoard().getInfieldIndexOfPiece(piece)].clearOccupant();

            Piece occ = targetNode.getOccupant();
            if (occ != null) {
                logic.getGame().getActivePlayer().getPlayerStatistic().increasePiecesThrown();
                logic.getGame().getGameStatistics().increasePiecesThrown();
                logic.getGame().getPlayerByColor(occ.getColor()).getPlayerStatistic().increasePiecesBeingThrown();
                logic.getGame().getGameStatistics().increasePiecesBeingThrown();
                //move occ to waiting
                logic.getGame().getPlayerByColor(occ.getColor()).addWaitingPiece(occ);
            }
            //move piece to targetNode
            targetNode.setOccupant(piece);

            if (targetNode.isBonus()) {
                logic.getGame().getActivePlayer().getPlayerStatistic().increaseActivatedBonusNodes();
                logic.getGame().getGameStatistics().increaseActivatedBonusNodes();
                PowerCard cardToDraw = logic.getGame().draw();
                for (Player p : logic.getGame().getPlayersAsList()) {
                    if (p.getColor() == logic.getGame().getActiveColor()) {
                        if (cardToDraw == null) {
                            this.logic.getServerSender().broadcast(new IncorrectRequestMessage(7));
                        } else {
                            p.addHandCard(cardToDraw);
                            logic.getServerSender().send(logic.getGame().getPlayerIdByColor(p.getColor()), new DrawCardMessage(cardToDraw));
                        }
                    } else {
                        if (cardToDraw == null) {
                            this.logic.getServerSender().broadcast(new IncorrectRequestMessage(7));
                        } else {
                            logic.getServerSender().send(logic.getGame().getPlayerIdByColor(p.getColor()), new DrawCardMessage(new HiddenCard()));
                        }
                    }
                }
            }

            this.choosePieceAutomaton.getTurnAutomaton().setCurrentState(this.choosePieceAutomaton.getTurnAutomaton().getMovePieceState());
        }
    }

    /**
     * This method will be used whenever this state will be exited.
     */
    @Override
    public void exit() {
        LOGGER.log(System.Logger.Level.DEBUG, "Entered StartPieceState state.");
        piece = null;
    }

    /**
     * this is a setter for the piece
     *
     * @param piece to be set
     */
    public void setPiece(Piece piece){
        this.piece = piece;
    }
}
