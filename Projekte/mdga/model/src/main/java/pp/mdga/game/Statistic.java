package pp.mdga.game;

import com.jme3.network.serializing.Serializable;

/**
 * This class will be used to store Statistics during the Game;
 */
@Serializable
public class Statistic {
    /**
     * The number of cards played.
     */
    private int cardsPlayed;

    /**
     * The number of pieces thrown. (enemy)
     */
    private int piecesThrown;

    /**
     * The number of pieces being thrown. (own pieces)
     */
    private int piecesBeingThrown;

    /**
     * The number of times a 6 was diced.
     */
    private int diced6;

    /**
     * The number of nodes traveled.
     */
    private int traveledNodes;

    /**
     * The number of bonus nodes activated.
     */
    private int activatedBonusNodes;

    /**
     * Constructs a new Statistic object with all values initialized to 0.
     */
    public Statistic() {
        cardsPlayed = 0;
        piecesThrown = 0;
        piecesBeingThrown = 0;
        diced6 = 0;
        traveledNodes = 0;
        activatedBonusNodes = 0;
    }

    /**
     * This method returns the amount of CardsPlayed;
     *
     * @return the amount of cardsPlayed as an int
     */
    public int getCardsPlayed() {
        return cardsPlayed;
    }

    /**
     * This method sets the amounts of cardsPlayed
     *
     * @param cardsPlayed the new amount of cardsPlayed.
     */
    public void setCardsPlayed(int cardsPlayed) {
        this.cardsPlayed = cardsPlayed;
    }

    /**
     * This method returns the count of enemyPieces thrown during the game.
     *
     * @return the count of enemyPieces thrown
     */
    public int getPiecesThrown() {
        return piecesThrown;
    }

    /**
     * This method sets the new count of enemyPieces thrown
     *
     * @param piecesThrown the new count of pieces thrown.
     */
    public void setPiecesThrown(int piecesThrown) {
        this.piecesThrown = piecesThrown;
    }

    /**
     * This method returns the count of ownPieces Being thrown.
     *
     * @return the count of thrown own pieces
     */
    public int getPiecesBeingThrown() {
        return piecesBeingThrown;
    }

    /**
     * This method sets the new count of own pieces thrown.
     *
     * @param piecesBeingThrown the new count of thrown own pieces.
     */
    public void setPiecesBeingThrown(int piecesBeingThrown) {
        this.piecesBeingThrown = piecesBeingThrown;
    }

    /**
     * This methode returns the amount of 6s diced during the game.
     *
     * @return the amount of 6s diced
     */
    public int getDiced6() {
        return diced6;
    }

    /**
     * This method sets the new amount of 6s diced
     *
     * @param diced6 the new amount of 6s diced
     */
    public void setDiced6(int diced6) {
        this.diced6 = diced6;
    }

    /**
     * This method returns the amount of traveled Nodes
     *
     * @return the amount of traveled Nodes as int
     */
    public int getTraveledNodes() {
        return traveledNodes;
    }

    /**
     * This method sets the new amount of traveled Nodes.
     *
     * @param traveledNodes the new amount of traveled Nodes
     */
    public void setTraveledNodes(int traveledNodes) {
        this.traveledNodes = traveledNodes;
    }

    /**
     * This method returns the amount of activated BonusNodes.
     *
     * @return the amount of activated BonusNodes as int
     */
    public int getActivatedBonusNodes() {
        return activatedBonusNodes;
    }

    /**
     * This method sets the new amount of activated BonusNodes
     *
     * @param activatedBonusNodes the new amount of activated BonusNodes
     */
    public void setActivatedBonusNodes(int activatedBonusNodes) {
        this.activatedBonusNodes = activatedBonusNodes;
    }

    /**
     * This method increases the value of cardsPlayed by 1.
     */
    public void increaseCardsPlayed() {
        cardsPlayed++;
    }

    /**
     * This method increases the value of piecesThrown by 1.
     */
    public void increasePiecesThrown() {
        piecesThrown++;
    }

    /**
     * This method increases the value of piecesBeingThrown by 1.
     */
    public void increasePiecesBeingThrown() {
        piecesBeingThrown++;
    }

    /**
     * This method increases the value of diced6 by 1.
     */
    public void increaseDiced6() {
        diced6++;
    }

    /**
     * This method increases the value of traveledNodes by 1.
     */
    public void increaseTraveledNodes() {
        traveledNodes++;
    }

    /**
     * This method increases the value of traveledNodes by the given amount.
     *
     * @param nodes the amount of nodes to increase the traveledNodes by.
     */
    public void increaseTraveledNodes(int nodes) {
        traveledNodes += nodes;
    }

    /**
     * This method increases the value of activatedBonusNodes by 1.
     */
    public void increaseActivatedBonusNodes() {
        activatedBonusNodes++;
    }
}
