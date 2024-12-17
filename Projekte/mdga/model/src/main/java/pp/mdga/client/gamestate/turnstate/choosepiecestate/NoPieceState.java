package pp.mdga.client.gamestate.turnstate.choosepiecestate;

import pp.mdga.client.ClientGameLogic;
import pp.mdga.client.ClientState;
import pp.mdga.client.gamestate.turnstate.ChoosePieceState;
import pp.mdga.game.Piece;
import pp.mdga.message.server.DiceNowMessage;
import pp.mdga.message.server.EndOfTurnMessage;
import pp.mdga.message.server.SelectPieceMessage;
import pp.mdga.message.server.StartPieceMessage;
import pp.mdga.message.server.WaitPieceMessage;
import pp.mdga.notification.DiceNowNotification;
import pp.mdga.notification.SelectableMoveNotification;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class NoPieceState extends ChoosePieceStates {

    private final System.Logger LOGGER = System.getLogger(this.getClass().getName());

    private final ChoosePieceState parent;

    /**
     * Constructs a client state of the specified game logic.
     *
     * @param parent the parent state
     * @param logic the client game logic
     */
    public NoPieceState(ClientState parent, ClientGameLogic logic) {
        super(parent, logic);
        this.parent = (ChoosePieceState) parent;
    }

    /**
     * Enters the state.
     */
    @Override
    public void enter() {
        LOGGER.log(System.Logger.Level.INFO, "Entering NoPieceState");
    }

    /**
     * Exits the state.
     */
    @Override
    public void exit() {

    }

    /**
     * Selects the piece.
     */
    @Override
    public void received(SelectPieceMessage msg) {
        ArrayList<Piece> pieces = msg.getPieces().stream().map(piece -> logic.getGame().getPieceThroughUUID(piece.getUuid())).collect(Collectors.toCollection(ArrayList::new));
        parent.getSelectPiece().setPossiblePieces(pieces);
        ArrayList<UUID> listPiece = pieces.stream().map(Piece::getUuid).collect(Collectors.toCollection(ArrayList::new));
        LOGGER.log(System.Logger.Level.INFO, "Received " + msg.getPieces().size() + " pieces");
        logic.addNotification(new SelectableMoveNotification(listPiece, msg.getTargetIndex(), msg.getIsHomeMove()));
        parent.setState(parent.getSelectPiece());
    }

    /**
     * Selects the dice.
     */
    @Override
    public void received(WaitPieceMessage msg) {
        LOGGER.log(System.Logger.Level.INFO, "Received WaitPieceMessage");
        Piece piece = logic.getGame().getPieceThroughUUID(msg.getPieceID());
        logic.addNotification(new SelectableMoveNotification(new ArrayList<>(List.of(msg.getPieceID())), new ArrayList<>(List.of(logic.getGame().getPlayerByColor(piece.getColor()).getStartNodeIndex())), new ArrayList<>(List.of(false))));
        parent.setState(parent.getWaitingPiece());
    }

    /**
     * This method is called when the server sends a DiceNowMessage.
     *
     * @param msg the DiceNowMessage
     */
    @Override
    public void received(StartPieceMessage msg) {
        Piece piece = logic.getGame().getPieceThroughUUID(msg.getPieceIdentifier());
        List<UUID> listPiece = new ArrayList<>();
        List<Integer> listIndex = new ArrayList<>();
        List<Boolean> homeMove = new ArrayList<>();
        listPiece.add(piece.getUuid());
        listIndex.add(msg.getTargetIndex());
        homeMove.add(false);
        parent.getStartPiece().setMoveablePiece(piece);
        LOGGER.log(System.Logger.Level.INFO, "Received start piece " + listPiece.get(0));
        logic.addNotification(new SelectableMoveNotification(listPiece, listIndex, homeMove));
        parent.setState(parent.getStartPiece());
    }

    /**
     * This method is called when the server sends a DiceNowMessage.
     *
     * @param msg the DiceNowMessage
     */
    @Override
    public void received(EndOfTurnMessage msg) {
        logic.getGame().setTurboFlag(false);
        parent.getParent().getParent().setState(parent.getParent().getParent().getWaiting());
    }

    /**
     * This method is called when the server sends a DiceNowMessage.
     *
     * @param msg the DiceNowMessage
     */
    @Override
    public void received(DiceNowMessage msg){
        parent.getParent().setState(parent.getParent().getRollDice());
    }
}
