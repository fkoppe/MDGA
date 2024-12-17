package pp.mdga.message.server;

import com.jme3.network.serializing.Serializable;

@Serializable
public class IncorrectRequestMessage extends ServerMessage {
    /**
     * Create IncorrectRequestMessage attributes.
     */
    private final int id;

    /**
     * Constructor.
     *
     * @param id as the id of the error message as an Integer.
     */
    public IncorrectRequestMessage(int id) {
        super();
        this.id = id;
    }

    /**
     * Constructor.
     */
    public IncorrectRequestMessage() {
        super();
        this.id = 0;
    }

    /**
     * Returns the id of the incorrect request message
     *
     * @return the id of the error message
     */
    public int getId() {
        return id;
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
     * This method will be used to return necessary class information in a more readable format.
     *
     * @return information as a String.
     */
    @Override
    public String toString() {
        return "IncorrectRequestMessage with id: %d".formatted(this.id);
    }
}
