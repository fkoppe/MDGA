package pp.mdga.client.gamestate;

import pp.mdga.client.ClientGameLogic;
import pp.mdga.client.ClientState;
import pp.mdga.game.*;
import pp.mdga.message.server.PlayCardMessage;
import pp.mdga.notification.ShieldActiveNotification;
import pp.mdga.notification.ShieldSuppressedNotification;
import pp.mdga.notification.SwapPieceNotification;

import java.util.UUID;

public abstract class GameStates extends ClientState {

    private final System.Logger LOGGER = System.getLogger(this.getClass().getName());

    /**
     * Constructs a client state of the specified game logic.
     *
     * @param parent the parent state
     * @param logic the client game logic
     */
    public GameStates(ClientState parent, ClientGameLogic logic) {
        super(parent, logic);
    }

    /**
     * Handles the power card.
     *
     * @param msg the play card message
     */
    protected void handlePowerCard(PlayCardMessage msg) {
        if (msg.getCard().getCard().equals(BonusCard.TURBO)) {
            logic.getGame().setTurboFlag(true);
            logic.getGame().setDiceModifier(msg.getDiceModifier());
        } else if (msg.getCard().getCard().equals(BonusCard.SHIELD)) {
            handleShield(msg.getPieces().get(0).getUuid());
        } else {
            swapPieces(msg.getPieces().get(0), msg.getPieces().get(1));
        }
        logic.getGame().getDiscardPile().add(msg.getCard());
    }

    /**
     * Handles the shield.
     *
     * @param uuid the uuid
     */
    private void handleShield(UUID uuid) {
        Board board = logic.getGame().getBoard();
        Piece piece = logic.getGame().getPieceThroughUUID(uuid);
        Node node = board.getInfield()[board.getInfieldIndexOfPiece(piece)];
        if (node.isStart()) {
            logic.getGame().getPieceThroughUUID(uuid).setShield(ShieldState.SUPPRESSED);
            logic.addNotification(new ShieldActiveNotification(uuid));
            logic.addNotification(new ShieldSuppressedNotification(uuid));
        } else {
            logic.getGame().getPieceThroughUUID(uuid).setShield(ShieldState.ACTIVE);
            logic.addNotification(new ShieldActiveNotification(uuid));
        }
    }

    /**
     * Swaps the pieces.
     *
     * @param messageOwn the own piece
     * @param messageEnemy the enemy piece
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

        logic.addNotification(new SwapPieceNotification(modelOwn.getUuid(), modelEnemy.getUuid()));
        checkShieldAfterSwap(enemyNode, modelOwn);
        checkShieldAfterSwap(ownNode, modelEnemy);

    }

    /**
     * Checks the shield after the swap.
     *
     * @param node the node
     * @param piece the piece
     */
    private void checkShieldAfterSwap(Node node, Piece piece) {
        if (node.isStart()) {
            if (piece.isShielded()) {
                piece.setShield(ShieldState.SUPPRESSED);
                logic.addNotification(new ShieldSuppressedNotification(piece.getUuid()));
            }
        } else if (piece.isSuppressed()) {
            piece.setShield(ShieldState.ACTIVE);
            logic.addNotification(new ShieldActiveNotification(piece.getUuid()));
        }
    }
}
