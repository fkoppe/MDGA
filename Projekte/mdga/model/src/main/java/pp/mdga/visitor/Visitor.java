package pp.mdga.visitor;

import pp.mdga.game.card.HiddenCard;
import pp.mdga.game.card.ShieldCard;
import pp.mdga.game.card.SwapCard;
import pp.mdga.game.card.TurboCard;

/**
 * This interface will be used to realize the visitor pattern inside the application.
 */
public interface Visitor {
    /**
     * This method will be used to visit the given card parameter.
     *
     * @param card as a TurboCard object.
     */
    void visit(TurboCard card);

    /**
     * This method will be used to visit the given card parameter.
     *
     * @param card as a SwapCard object.
     */
    void visit(SwapCard card);

    /**
     * This method will be used to visit the given card parameter.
     *
     * @param card as a ShieldCard oblect
     */
    void visit(ShieldCard card);

    /**
     * This method will be used to visit the given card parameter.
     *
     * @param card as a HiddenCard object.
     */
    void visit(HiddenCard card);
}
