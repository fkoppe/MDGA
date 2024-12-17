package pp.mdga.message.server;

import com.jme3.network.serializing.Serializable;
import pp.mdga.game.Game;

/**
 * A message sent by the server to a client that has reconnected to the game. (give the last saved model)
 */
@Serializable
public class ReconnectBriefingMessage extends ServerMessage {
    /**
     * The game.
     */
    private final Game game;

    /**
     * Constructs a new ReconnectBriefing message.
     */
    public ReconnectBriefingMessage(Game game) {
        super();
        this.game = game;
    }

    /**
     * Default constructor for serialization purposes.
     */
    private ReconnectBriefingMessage() {
        this(null);
    }

    /**
     * Returns the game.
     *
     * @return the game
     */
    public Game getGame() {
        return game;
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
        return "ReconnectBriefingMessage{" + "game=" + game + '}';
    }
}
