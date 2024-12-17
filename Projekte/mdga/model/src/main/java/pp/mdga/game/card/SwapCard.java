package pp.mdga.game.card;

import com.jme3.network.serializing.Serializable;
import pp.mdga.game.BonusCard;
import pp.mdga.visitor.Visitor;

/**
 * This class represents the swap power card of this application.
 */
@Serializable
public class SwapCard extends PowerCard {
    /**
     * Constructor.
     */
    public SwapCard() {
        this.card = BonusCard.SWAP;
    }

    /**
     * This method will be used to call the visit method of the given visitor parameter and pass a PowerCard object.
     *
     * @param visitor as the visitor which will differentiates between all types of power card types as a Visitor
     *                interface.
     */
    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
