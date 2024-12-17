package pp.mdga.game;

import com.jme3.network.serializing.Serializable;

/**
 * This class represents a BonusNode
 */
@Serializable
public class BonusNode extends Node {
    /**
     * Constructor.
     */
    BonusNode() {
        super(null);
    }

    /**
     * This method will return true if the node is a bonus node.
     *
     * @return true if the node is a bonus node.
     */
    @Override
    public boolean isBonus() {
        return true;
    }
}
