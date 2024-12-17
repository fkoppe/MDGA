package pp.mdga.game;

import com.jme3.network.serializing.Serializable;

/**
 * Enum representing the different types of bonus cards.
 */
@Serializable
public enum BonusCard {
    /**
     * The hidden bonus card.
     */
    HIDDEN,
    /**
     * The shield bonus card.
     */
    SHIELD,
    /**
     * The turbo bonus card.
     */
    TURBO,
    /**
     * The swap bonus card.
     */
    SWAP
}
