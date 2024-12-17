package pp.mdga.message.server;

import com.jme3.network.serializing.Serializable;

import java.util.HashMap;
import java.util.Map;

/**
 * A message sent by the server to all client to inform them about the current ranking. (only in DetermineStartPlayer)
 */
@Serializable
public class RankingResponseMessage extends ServerMessage {
    /**
     * Create RankingResponseMessage attributes.
     */
    private final Map<Integer, Integer> rankingResults;

    /**
     * Constructor.
     */
    public RankingResponseMessage() {
        super();
        this.rankingResults = new HashMap<Integer, Integer>();
    }

    /**
     * Constructor.
     *
     * @param rankingResults as the results of all players after the start player was determined as a Map combining
     *                       Integers and Integers.
     */
    public RankingResponseMessage(Map<Integer, Integer> rankingResults) {
        super();
        this.rankingResults = rankingResults;
    }

    /**
     * This method will be used to return rankingResults attribute of RankingResponseMessage class.
     *
     * @return rankingResults as a Map combining Integers and Integers.
     */
    public Map<Integer, Integer> getRankingResults() {
        return this.rankingResults;
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
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("RankingResponseMessage{");
        for (Map.Entry<Integer, Integer> entry : this.rankingResults.entrySet()) {
            stringBuilder.append("Player with ID: ").append(entry.getKey()).append(" rolled: ").append(entry.getValue()).append(", ");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
