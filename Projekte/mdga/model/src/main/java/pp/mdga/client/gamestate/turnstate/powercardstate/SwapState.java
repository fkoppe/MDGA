package pp.mdga.client.gamestate.turnstate.powercardstate;

import pp.mdga.client.ClientGameLogic;
import pp.mdga.client.ClientState;
import pp.mdga.client.gamestate.turnstate.PowerCardState;
import pp.mdga.game.Piece;
import pp.mdga.message.client.SelectedPiecesMessage;
import pp.mdga.message.server.PlayCardMessage;
import pp.mdga.notification.SelectableSwapNotification;

import java.util.ArrayList;
import java.util.UUID;

public class SwapState extends PowerCardStates {

    private final System.Logger LOGGER = System.getLogger(this.getClass().getName());

    private final PowerCardState parent;

    private ArrayList<Piece> possibleOwnPieces;
    private ArrayList<Piece> possibleEnemyPieces;
    private Piece selectedOwnPiece;
    private Piece selectedEnemyPiece;

    /**
     * Constructs a client state of the specified game logic.
     *
     * @param parent the parent state
     * @param logic the client game logic
     */
    public SwapState(ClientState parent, ClientGameLogic logic) {
        super(parent, logic);
        this.parent = (PowerCardState) parent;
        possibleOwnPieces = new ArrayList<>();
        possibleEnemyPieces = new ArrayList<>();
        selectedOwnPiece = null;
        selectedEnemyPiece = null;
    }

    /**
     * Enters the state.
     */
    @Override
    public void enter() {
        LOGGER.log(System.Logger.Level.INFO, "Entering SwapState");
        ArrayList<UUID> ownPieces = new ArrayList<>(possibleOwnPieces.stream().map(Piece::getUuid).toList());
        ArrayList<UUID> enemyPieces = new ArrayList<>(possibleEnemyPieces.stream().map(Piece::getUuid).toList());
        logic.addNotification(new SelectableSwapNotification(ownPieces, enemyPieces));
        selectedOwnPiece = null;
        selectedEnemyPiece = null;
    }

    /**
     * Exits the state.
     */
    @Override
    public void exit() {
        LOGGER.log(System.Logger.Level.INFO, "Exiting SwapState");
        possibleOwnPieces.clear();
        possibleEnemyPieces.clear();
    }

    /**
     * Sets the possible own pieces.
     *
     * @param possibleOwnPieces the possible own pieces
     */
    public void setPossibleOwnPieces(ArrayList<Piece> possibleOwnPieces) {
        this.possibleOwnPieces = possibleOwnPieces;
    }

    /**
     * Sets the possible enemy pieces.
     *
     * @param possibleEnemyPieces the possible enemy pieces
     */
    public void setPossibleEnemyPieces(ArrayList<Piece> possibleEnemyPieces) {
        this.possibleEnemyPieces = possibleEnemyPieces;
    }

    /**
     * Selects the piece.
     *
     * @param piece the piece
     */
    @Override
    public void selectPiece(Piece piece) {
        if (possibleOwnPieces.contains(piece)) {
            selectedOwnPiece = piece;
        } else if (possibleEnemyPieces.contains(piece)) {
            selectedEnemyPiece = piece;
        }
        if (selectedOwnPiece != null && selectedEnemyPiece != null) {
            ArrayList<Piece> temp = new ArrayList<>();
            temp.add(selectedOwnPiece);
            temp.add(selectedEnemyPiece);
            System.out.println("Client : Swap: temp: " + temp.get(0) + temp.get(1));
            logic.send(new SelectedPiecesMessage(temp));
            selectedEnemyPiece = null;
            selectedOwnPiece = null;
        }
    }

    /**
     * Receive a card
     *
     * @param msg card message
     */
    @Override
    public void received(PlayCardMessage msg) {
        parent.getParent().getPlayPowerCard().setPlayCard(msg);
        parent.getParent().setState(parent.getParent().getPlayPowerCard());
    }
}
