package pp.mdga.message.server;

import com.jme3.network.serializing.Serializable;
import pp.mdga.game.Color;

import java.util.ArrayList;

/**
 * A message sent by the server to indicate the beginning of the ceremony.
 */
@Serializable
public class CeremonyMessage extends ServerMessage {

    private ArrayList<Color> colors;
    private ArrayList<String> names;
    private ArrayList<Integer> piecesThrown;
    private ArrayList<Integer> piecesLost;
    private ArrayList<Integer> bonusCardsPlayed;
    private ArrayList<Integer> sixes;
    private ArrayList<Integer> nodesMoved;
    private ArrayList<Integer> bonusNodes;

    /**
     * Constructs a new Ceremony instance.
     */
    public CeremonyMessage() {
        super();
        colors = new ArrayList<>();
        names = new ArrayList<>();
        piecesThrown = new ArrayList<>();
        piecesLost = new ArrayList<>();
        bonusCardsPlayed = new ArrayList<>();
        sixes = new ArrayList<>();
        nodesMoved = new ArrayList<>();
        bonusNodes = new ArrayList<>();
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
