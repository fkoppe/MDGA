package pp.mdga.notification;

import pp.mdga.game.Color;

import java.util.ArrayList;

/**
 * Class CeremonyNotification
 * Represents a notification for a ceremony in the game.
 * <p>
 * Index mapping:
 * index = 0 ==> winner
 * index = 1 ==> 2nd
 * index = 2 ==> third place
 * index = 3 ==> loser
 * index = 4 ==> total
 */
public class CeremonyNotification extends Notification {
    /**
     * Attributes.
     */
    private ArrayList<Color> colors;
    private ArrayList<String> names;
    private ArrayList<Integer> piecesThrown;
    private ArrayList<Integer> piecesLost;
    private ArrayList<Integer> bonusCardsPlayed;
    private ArrayList<Integer> sixes;
    private ArrayList<Integer> nodesMoved;
    private ArrayList<Integer> bonusNodes;

    /**
     * Constructor
     * Initializes all lists.
     */
    public CeremonyNotification() {
        this.colors = new ArrayList<>();
        this.names = new ArrayList<>();
        this.piecesThrown = new ArrayList<>();
        this.piecesLost = new ArrayList<>();
        this.bonusCardsPlayed = new ArrayList<>();
        this.sixes = new ArrayList<>();
        this.nodesMoved = new ArrayList<>();
        this.bonusNodes = new ArrayList<>();
    }

    /**
     * @return the list of colors
     */
    public ArrayList<Color> getColors() {
        return colors;
    }

    /**
     * @param colors the list of colors to set
     */
    public void setColors(ArrayList<Color> colors) {
        this.colors = colors;
    }

    /**
     * @return the list of player names
     */
    public ArrayList<String> getNames() {
        return names;
    }

    /**
     * @param names the list of player names to set
     */
    public void setNames(ArrayList<String> names) {
        this.names = names;
    }

    /**
     * @return the list of pieces thrown
     */
    public ArrayList<Integer> getPiecesThrown() {
        return piecesThrown;
    }

    /**
     * @param piecesThrown the list of pieces thrown to set
     */
    public void setPiecesThrown(ArrayList<Integer> piecesThrown) {
        this.piecesThrown = piecesThrown;
    }

    /**
     * @return the list of pieces lost
     */
    public ArrayList<Integer> getPiecesLost() {
        return piecesLost;
    }

    /**
     * @param piecesLost the list of pieces lost to set
     */
    public void setPiecesLost(ArrayList<Integer> piecesLost) {
        this.piecesLost = piecesLost;
    }

    /**
     * @return the list of bonus cards played
     */
    public ArrayList<Integer> getBonusCardsPlayed() {
        return bonusCardsPlayed;
    }

    /**
     * @param bonusCardsPlayed the list of bonus cards played to set
     */
    public void setBonusCardsPlayed(ArrayList<Integer> bonusCardsPlayed) {
        this.bonusCardsPlayed = bonusCardsPlayed;
    }

    /**
     * @return the list of sixes rolled
     */
    public ArrayList<Integer> getSixes() {
        return sixes;
    }

    /**
     * @param sixes the list of sixes rolled to set
     */
    public void setSixes(ArrayList<Integer> sixes) {
        this.sixes = sixes;
    }

    /**
     * @return the list of nodes moved
     */
    public ArrayList<Integer> getNodesMoved() {
        return nodesMoved;
    }

    /**
     * @param nodesMoved the list of nodes moved to set
     */
    public void setNodesMoved(ArrayList<Integer> nodesMoved) {
        this.nodesMoved = nodesMoved;
    }

    /**
     * @return the list of bonus nodes
     */
    public ArrayList<Integer> getBonusNodes() {
        return bonusNodes;
    }

    /**
     * @param bonusNodes the list of bonus nodes to set
     */
    public void setBonusNodes(ArrayList<Integer> bonusNodes) {
        this.bonusNodes = bonusNodes;
    }
}
