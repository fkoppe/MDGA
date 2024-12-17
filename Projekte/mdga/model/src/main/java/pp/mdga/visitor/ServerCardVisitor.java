package pp.mdga.visitor;

import pp.mdga.game.Piece;
import pp.mdga.game.PieceState;
import pp.mdga.game.card.*;
import pp.mdga.server.ServerGameLogic;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the visitor for all types of power cards.
 */
public class ServerCardVisitor implements Visitor {
    /**
     * Create ServerCardVisitor attributes
     */
    private final List<PowerCard> cards = new ArrayList<>();
    private final List<Piece> shieldPieces = new ArrayList<>();
    private final List<Piece> swapOwnPieces = new ArrayList<>();
    private final List<Piece> swapOtherPieces = new ArrayList<>();
    private final ServerGameLogic logic;

    /**
     * Constructor.
     *
     * @param logic as the logic of this server which holds the model as a ServerGameLogic object.
     */
    public ServerCardVisitor(ServerGameLogic logic) {
        this.logic = logic;
    }

    /**
     * This method will be used to visit the given card parameter.
     *
     * @param card as a TurboCard object.
     */
    @Override
    public void visit(TurboCard card) {
        for (Piece piece : this.logic.getGame().getActivePlayer().getPieces()) {
            if (piece.getState() == PieceState.ACTIVE) {
                if (!this.cards.stream().map(PowerCard::getCard).toList().contains(card.getCard())) {
                    this.cards.add(card);
                }
            }
        }
    }

    /**
     * This method will be used to visit the given card parameter.
     *
     * @param card as a SwapCard object.
     */
    @Override
    public void visit(SwapCard card) {
        List<Piece> possibleOwnPieces = new ArrayList<>();
        for (Piece piece : this.logic.getGame().getActivePlayer().getPieces()) {
            if (piece.getState() == PieceState.ACTIVE) {
                if (!possibleOwnPieces.contains(piece)) {
                    possibleOwnPieces.add(piece);
                }
            }
        }

        List<Piece> possibleOtherPieces = new ArrayList<>();
        for (var player : this.logic.getGame().getPlayers().values()) {
            if (player != this.logic.getGame().getActivePlayer()) {
                for (Piece piece : player.getPieces()) {
                    if (piece.getState() == PieceState.ACTIVE) {
                        if (!possibleOtherPieces.contains(piece)) {
                            possibleOtherPieces.add(piece);
                        }
                    }
                }
            }
        }

        if (!possibleOtherPieces.isEmpty() && !possibleOwnPieces.isEmpty()) {
            this.swapOwnPieces.addAll(possibleOwnPieces);
            this.swapOtherPieces.addAll(possibleOtherPieces);
            if (!this.cards.stream().map(PowerCard::getCard).toList().contains(card.getCard())) {
                this.cards.add(card);
            }
        }
    }

    /**
     * This method will be used to visit the given card parameter.
     *
     * @param card as a ShieldCard object.
     */
    @Override
    public void visit(ShieldCard card) {
        for (Piece piece : this.logic.getGame().getPlayerByColor(this.logic.getGame().getActiveColor()).getPieces()) {
            if (piece.getState() == PieceState.ACTIVE) {
                if (!this.shieldPieces.contains(piece)) {
                    this.shieldPieces.add(piece);
                }
                if (!this.cards.stream().map(PowerCard::getCard).toList().contains(card.getCard())) {
                    this.cards.add(card);
                }
            }
        }
    }

    /**
     * This method will be used to visit the given card parameter.
     *
     * @param card as a HiddenCard object.
     */
    @Override
    public void visit(HiddenCard card) {

    }

    /**
     * This method will be used to return cards attribute of ServerCardVisitor class.
     *
     * @return cards as a List of PowerCard objects.
     */
    public List<PowerCard> getCards() {
        return this.cards;
    }

    /**
     * This method will be used to return shieldPieces attribute of ServerCardVisitor class.
     *
     * @return shieldPieces as a List of Piece objects.
     */
    public List<Piece> getShieldPieces() {
        return this.shieldPieces;
    }

    /**
     * This method will be used to return swapOwnPieces attribute of ServerCardVisitor class.
     *
     * @return swapOwnPieces as a List of Piece objects.
     */
    public List<Piece> getSwapOwnPieces() {
        return this.swapOwnPieces;
    }

    /**
     * This method will be used to return swapOtherPieces attribute of ServerCardVisitor class.
     *
     * @return swapOtherPieces as a List of Piece objects.
     */
    public List<Piece> getSwapOtherPieces() {
        return this.swapOtherPieces;
    }
}
