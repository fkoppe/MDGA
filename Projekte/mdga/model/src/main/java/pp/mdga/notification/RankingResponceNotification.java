package pp.mdga.notification;

import pp.mdga.game.Color;

import java.util.Map;

public class RankingResponceNotification extends Notification {
    private final Map<Color, Integer> rankingResults;

    /**
     * Constructor.
     *
     * @param rankingResults as the results of all players after the start player was determined as a Map combining
     *                       Integers and Integers.
     */
    public RankingResponceNotification(Map<Color, Integer> rankingResults) {
        super();
        this.rankingResults = rankingResults;
    }

    /**
     * This method will be used to return rankingResults attribute of RankingResponseMessage class.
     *
     * @return rankingResults as a Map combining Integers and Integers.
     */
    public Map<Color, Integer> getRankingResults() {
        return this.rankingResults;
    }
}
