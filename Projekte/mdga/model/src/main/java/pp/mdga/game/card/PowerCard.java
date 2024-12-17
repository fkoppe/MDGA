package pp.mdga.game.card;

import com.jme3.network.serializing.Serializable;
import pp.mdga.game.BonusCard;
import pp.mdga.visitor.Visitor;

import java.util.UUID;

/**
 * This abstract class represents the abstract power class which will be used to realize a visitor pattern for all
 * types of different cards inside the game.
 */
@Serializable
public abstract class PowerCard {
    /**
     * Create PowerCard attributes.
     */
    private final UUID uuid = UUID.randomUUID();
    protected BonusCard card;

    /**
     * Constructor.
     */
    public PowerCard() {
        this.card = BonusCard.HIDDEN;
    }

    /**
     * This method will be used to call the visit method of the given visitor parameter and pass a PowerCard object.
     *
     * @param visitor as the visitor which will differentiates between all types of power card types as a Visitor
     *                interface.
     */
    public abstract void accept(Visitor visitor);

    /**
     * This method will be used to return uuid attribute of abstract PowerCard class.
     *
     * @return uuid as a UUID object.
     */
    public UUID getUuid() {
        return this.uuid;
    }

    /**
     * This method will be used to return card attribute of abstract PowerCard class.
     *
     * @return card as a BonusCard enumeration.
     */
    public BonusCard getCard() {
        return this.card;
    }

    /**
     * This method will be used to create the hash code of this abstract PowerCard class.
     *
     * @return hashCode as an Integer.
     */
    @Override
    public int hashCode() {
        return this.uuid.hashCode();
    }

    /**
     * This method will be used to check if the given obj parameter is equal to this object.
     * It will return false if the obj parameter is null or if the hash codes of both objects are not equal. Otherwise
     * it will return true.
     *
     * @param obj as the object which will be compared as an Object.
     * @return true or false.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj instanceof PowerCard) {
            PowerCard card = (PowerCard) obj;

            return this.hashCode() == card.hashCode();
        }

        return false;
    }
}
