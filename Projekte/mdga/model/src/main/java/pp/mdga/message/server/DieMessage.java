package pp.mdga.message.server;

import com.jme3.network.serializing.Serializable;

/**
 * A message sent by the server to the client to inform about the dice roll.
 */
@Serializable
public class DieMessage extends ServerMessage {
    /**
     * The eye of the dice
     */
    private final int diceEye;

    /**
     * Constructor for Dice
     *
     * @param diceEye the eye of the dice
     */
    public DieMessage(int diceEye) {
        super();
        this.diceEye = diceEye;
    }

    /**
     * Default constructor for serialization purposes.
     */
    public DieMessage() {
        diceEye = 0;
    }

    /**
     * Getter for the eye of the dice
     *
     * @return the eye of the dice
     */
    public int getDiceEye() {
        return diceEye;
    }

    /**
     * Accepts a visitor to process this message.
     *
     * @param interpreter the visitor to process this message
     */
    @Override
    public void accept(ServerInterpreter interpreter) {
        interpreter.received(this);
    }

    /**
     * Returns a string representation of this message.
     *
     * @return a string representation of this message
     */
    @Override
    public String toString() {
        return "DieMessage{" + "diceEye=" + diceEye + '}';
    }
}
