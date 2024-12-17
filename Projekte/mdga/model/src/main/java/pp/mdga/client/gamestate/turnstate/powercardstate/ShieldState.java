package pp.mdga.client.gamestate.turnstate.powercardstate;

import pp.mdga.client.ClientGameLogic;
import pp.mdga.client.ClientState;
import pp.mdga.client.gamestate.turnstate.PowerCardState;
import pp.mdga.game.Piece;
import pp.mdga.message.client.SelectedPiecesMessage;
import pp.mdga.message.server.PlayCardMessage;
import pp.mdga.notification.SelectableShieldNotification;

import java.lang.System.Logger.Level;
import java.util.ArrayList;

public class ShieldState extends PowerCardStates {

    private final PowerCardState parent;
    private final System.Logger LOGGER = System.getLogger(this.getClass().getName());

    private ArrayList<Piece> possiblePieces;

    /**
     * Constructs a client state of the specified game logic.
     *
     * @param parent the parent state
     * @param logic the client game logic
     */
    public ShieldState(ClientState parent, ClientGameLogic logic) {
        super(parent, logic);
        this.parent = (PowerCardState) parent;
        possiblePieces = new ArrayList<>();
    }

    /**
     * Enters the state.
     */
    @Override
    public void enter() {
        logic.addNotification(new SelectableShieldNotification(possiblePieces.stream().map(Piece::getUuid).toList()));
    }

    /**
     * Exits the state.
     */
    @Override
    public void exit() {
        possiblePieces = null;
    }

    /**
     * Sets the possible pieces.
     *
     * @param possiblePieces the possible pieces
     */
    public void setPossiblePieces(ArrayList<Piece> possiblePieces) {
        this.possiblePieces = possiblePieces;
    }

    /**
     * Selects the piece.
     *
     * @param piece the piece
     */
    public void selectPiece(Piece piece) {
        if (possiblePieces.contains(piece)) {
//            logic.send(RequestPlayCardMessage.requestPlayShield(piece.getUuid()));
            ArrayList<Piece> temp = new ArrayList<>();
            temp.add(piece);
            logic.send(new SelectedPiecesMessage(temp));
        } else {
            LOGGER.log(Level.DEBUG, "Invalid piece selected");
        }
    }

    /**
     * This method is called when the server sends a PlayCardMessage.
     *
     * @param msg the PlayCardMessage
     */
    @Override
    public void received(PlayCardMessage msg) {
        parent.getParent().getPlayPowerCard().setPlayCard(msg);
        parent.getParent().setState(parent.getParent().getPlayPowerCard());
    }
}
