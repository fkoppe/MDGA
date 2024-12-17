package pp.mdga.message.server;

import com.jme3.network.serializing.Serializable;

/**
 * A message sent by the server to the clients to indicate that the ranking shall be rolled again. (only in DetermineStartPlayer)
 */
@Serializable
public class RankingRollAgainMessage extends ServerMessage {
    /**
     * Constructs a new RankingRollAgain instance.
     */
    public RankingRollAgainMessage() {
        super();
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
}
