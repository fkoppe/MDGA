package pp.mdga.server.automaton.game.turn.choosepiece;

import pp.mdga.game.*;
import pp.mdga.game.card.HiddenCard;
import pp.mdga.game.card.PowerCard;
import pp.mdga.message.client.RequestMoveMessage;
import pp.mdga.message.server.DrawCardMessage;
import pp.mdga.message.server.IncorrectRequestMessage;
import pp.mdga.message.server.MoveMessage;
import pp.mdga.message.server.SelectPieceMessage;
import pp.mdga.server.ServerGameLogic;
import pp.mdga.server.automaton.game.turn.ChoosePieceState;

import java.util.ArrayList;
import java.util.List;

public class SelectPieceState extends ChoosePieceAutomatonState {
    /**
     * Create FirstRollState constants.
     */
    private static final System.Logger LOGGER = System.getLogger(SelectPieceState.class.getName());

    private ArrayList<Piece> moveablePieces = new ArrayList<>();
    private ArrayList<Boolean> isHomeMove = new ArrayList<>();
    private ArrayList<Integer> targetIndex = new ArrayList<>();

    /**
     * Constructs a server state of the specified game logic.
     *
     * @param choosePieceAutomaton as the automaton of the choose piece state as a ChoosePieceState object.
     * @param logic                the game logic
     */
    public SelectPieceState(ChoosePieceState choosePieceAutomaton, ServerGameLogic logic) {
        super(choosePieceAutomaton, logic);
    }

    public void setMoveablePieces(ArrayList<Piece> moveablePieces) {
        this.moveablePieces = moveablePieces;
    }

    /**
     * This method will be used whenever this state will be entered.
     */
    @Override
    public void enter() {
        int steps = logic.getGame().getDiceModifier() * logic.getGame().getDiceEyes();
        for (Piece piece : moveablePieces) {

            if (canPieceMoveInHome(piece, steps)) {
                int target = getHomeTargetIdx(piece, steps);
                targetIndex.add(target);
                isHomeMove.add(true);
            } else {
                int target = getInfieldTarget(piece, steps);
                targetIndex.add(target);
                isHomeMove.add(false);
            }
        }
        logic.getServerSender().send(logic.getGame().getActivePlayerId(), new SelectPieceMessage(moveablePieces, isHomeMove, targetIndex));
    }

    @Override
    public void received(RequestMoveMessage msg, int from) {
        if (!moveablePieces.contains(msg.getPiece())) {
            throw new RuntimeException("invalid Piece");
        }
        int indexOfPiece = moveablePieces.indexOf(msg.getPiece());
        int steps = logic.getGame().getDiceModifier() * logic.getGame().getDiceEyes();
        Piece piece = moveablePieces.get(indexOfPiece);
        logic.getGame().getActivePlayer().getPlayerStatistic().increaseTraveledNodes(logic.getGame().getDiceEyes() * logic.getGame().getDiceModifier());
        logic.getGame().getGameStatistics().increaseTraveledNodes(logic.getGame().getDiceEyes() * logic.getGame().getDiceModifier());
        if (canPieceMoveInHome(piece, steps)) {
            if (piece.getState().equals(PieceState.HOME)) {
                //gets the oldNode
                int homeIndex = logic.getGame().getActivePlayer().getHomeIndexOfPiece(piece);
                Node oldNode = logic.getGame().getActivePlayer().getHomeNodes()[homeIndex];
                //gets the targetNode
                int targetHomeIdx = getHomeTargetIdx(piece, steps);
                Node targetNode = logic.getGame().getActivePlayer().getHomeNodes()[targetHomeIdx];
                if (targetHomeIdx == logic.getGame().getActivePlayer().getHighestHomeIdx()) {
                    piece.setState(PieceState.HOMEFINISHED);
                } else {
                    piece.setState(PieceState.HOME);
                }

                oldNode.clearOccupant();
                targetNode.setOccupant(piece);
            } else {
                int oldNoteIdx = logic.getGame().getBoard().getInfieldIndexOfPiece(piece);
                Node oldNode = logic.getGame().getBoard().getInfield()[oldNoteIdx];

                //gets the targetNode
                int targetHomeIdx = getHomeTargetIdx(piece, steps);
                Node targetNode = logic.getGame().getActivePlayer().getHomeNodes()[targetHomeIdx];

                if (targetHomeIdx == logic.getGame().getActivePlayer().getHighestHomeIdx()) {
                    piece.setState(PieceState.HOMEFINISHED);
                } else {
                    piece.setState(PieceState.HOME);
                }

                oldNode.clearOccupant();
                targetNode.setOccupant(piece);
            }
            LOGGER.log(System.Logger.Level.INFO, "Server : SelectPieceState: PieceState:" + piece.getState());
        } else {
            LOGGER.log(System.Logger.Level.INFO, "Server : SelectPieceState: PieceState:" + piece.getState());
            int oldNoteIdx = logic.getGame().getBoard().getInfieldIndexOfPiece(piece);
            Node oldNode = logic.getGame().getBoard().getInfield()[oldNoteIdx];

            int targetIndex = (oldNoteIdx + steps) % 40;
            Node targetNode = logic.getGame().getBoard().getInfield()[targetIndex];

            Piece occ = targetNode.getOccupant();
            if (occ != null) {
                logic.getGame().getActivePlayer().getPlayerStatistic().increasePiecesThrown();
                logic.getGame().getGameStatistics().increasePiecesThrown();
                logic.getGame().getPlayerByColor(occ.getColor()).getPlayerStatistic().increasePiecesBeingThrown();
                logic.getGame().getGameStatistics().increasePiecesBeingThrown();
                logic.getGame().getPlayerByColor(occ.getColor()).addWaitingPiece(occ);
            }

            if (targetNode.isStart()) {
                if (piece.getShield() == ShieldState.ACTIVE) piece.setShield(ShieldState.SUPPRESSED);
            }

            oldNode.clearOccupant();
            targetNode.setOccupant(piece);
        }

        boolean homeMove = isHomeMove.get(indexOfPiece);
        int targIdx = targetIndex.get(indexOfPiece);

        Node targetNode = logic.getGame().getBoard().getInfield()[targIdx];

        LOGGER.log(System.Logger.Level.INFO, "Server : SelectPieceState: PieceState: end:" + piece.getState());
        logic.getServerSender().broadcast(new MoveMessage(piece, homeMove, targIdx));

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

    /**
     * This method will be used whenever this state will be exited.
     */
    @Override
    public void exit() {
        LOGGER.log(System.Logger.Level.DEBUG, "Exited SelectPieceState state.");
        moveablePieces.clear();
        isHomeMove.clear();
        targetIndex.clear();
    }

    /**
     * getter for movables pieces
     *
     * @return ArrayList of movables Pieces
     */
    public ArrayList<Piece> getMoveablePieces() {
        return moveablePieces;
    }

    /**
     * this method is used to set the isHomeMoveIndex
     *
     * @param list to be set
     */
    public void setIsHomeMove(ArrayList<Boolean> list){
        this.isHomeMove=list;
    }

    /**
     * this method is used to set the targetIndex
     *
     * @param list to be set
     */
    public void setTargetIndex(ArrayList<Integer> list){
        this.targetIndex=list;
    }
}
