package pp.mdga.game;

import com.jme3.network.serializing.Serializable;
import pp.mdga.Resources;
import pp.mdga.game.card.PowerCard;

import java.util.ArrayList;

/**
 * This class represents a player in the game.
 */
@Serializable
public class Player {
    /**
     * The name of the player.
     */
    private String name;

    /**
     * The statistics of the player.
     */
    private Statistic playerStatistic = new Statistic();

    /**
     * The hand cards of the player.
     */
    private ArrayList<PowerCard> handCards = new ArrayList<>();

    /**
     * The color of the player.
     */
    private Color color = Color.NONE;

    /**
     * Indicates if the player is ready.
     */
    private boolean ready = false;

    /**
     * Indicates if the player is active.
     */
    private boolean active = true;

    /**
     * Node and piece attributes
     */
    private int startNodeIndex = -1;

    /**
     * The home nodes of the player.
     */
    private HomeNode[] homeNodes = new HomeNode[Resources.MAX_PIECES];

    /**
     * The waiting area of the player.
     */
    private Piece[] waitingArea = new Piece[Resources.MAX_PIECES];

    /**
     * The pieces of the player.
     */
    private Piece[] pieces = new Piece[Resources.MAX_PIECES];

    /**
     * Constructor.
     */
    public Player() {
        this("");
    }

    /**
     * This constructor constructs a new Player object
     *
     * @param name the name of the player
     */
    public Player(String name) {
        this.name = name;
    }

    /**
     * This method will be used to initialize all nodes and pieces of the Player class.
     */
    public void initialize() {
        for (int index = 0; index < Resources.MAX_PIECES; index++) {
            this.homeNodes[index] = new HomeNode();
            this.pieces[index] = new Piece(this.color, PieceState.WAITING);
            this.waitingArea[index] = this.pieces[index];
        }
        startNodeIndex = color.ordinal() * 10;
    }

    /**
     * This method adds a new handCard to the player
     *
     * @param card the card to be added to the players hand
     */
    public void addHandCard(PowerCard card) {
        this.handCards.add(card);
    }

    /**
     * This method will be used to remove the given card parameter from the handCards attribute of Player card.
     *
     * @param card as the card which should be removed from the handCards attribute as a PowerCard object.
     */
    public void removeHandCard(PowerCard card) {
        this.handCards.remove(card);
    }

    /**
     * This method will be used to add the given piece parameter to the first free slot inside the waitingArea attribute
     * of Player class.
     *
     * @param piece as the piece which should be added to the waitingArea attribute of Player class as a Piece object.
     */
    public void addWaitingPiece(Piece piece) {
        piece.setState(PieceState.WAITING);

        for (int i = 0; i < Resources.MAX_PIECES; i++) {
            if (this.waitingArea[i] == null) {
                this.waitingArea[i] = piece;
                return;
            }
        }
    }

    /**
     * This method will be used to check if the player is finished.
     * ToDo: Currently return always false. Implement logic!
     *
     * @return true or false.
     */
    public boolean isFinished() {
        for (int i = 0; i < Resources.MAX_PIECES; i++) {
            if (this.pieces[i].getState() != PieceState.HOMEFINISHED) {
                return false;
            }
        }
        return true;
    }

    /**
     * This method returns a PowerCard based on its Type.
     *
     * @param bonusCard the card Type to be matched
     * @return the first PowerCard of this type
     */
    public PowerCard getPowerCardByType(BonusCard bonusCard) {
        for (PowerCard card : this.handCards) {
            if (card.getCard().equals(bonusCard)) {
                return card;
            }
        }
        return null;
//        throw new RuntimeException("bonusCard is not in handCards");
    }

    public Piece getWaitingPiece() {
        for (Piece piece : this.waitingArea) {
            if (piece != null) {
                return piece;
            }
        }
        return null;

    }

    /**
     * This method returns a boolean based on if the Player has a piece in its waiting area
     *
     * @return the boolean if the waiting area contains a piece
     */
    public boolean hasPieceInWaitingArea() {
        for (Piece piece : this.waitingArea) {
            if (piece != null) {
                return true;
            }
        }
        return false;
    }

    public int getHomeIndexOfPiece(Piece piece) {
        for (int i = 0; i < Resources.MAX_PIECES; i++) {
            if (piece.equals(this.homeNodes[i].getOccupant())) {
                return i;
            }
        }
        return -1;
    }

    public boolean pieceInsideOfHome(Piece piece) {
        for (Node node : this.homeNodes) {
            if (piece.equals(node.getOccupant())) {
                return true;
            }
        }
        return false;
    }

    public boolean isHomeFinished(Piece piece) {
        for (int i = getHomeIndexOfPiece(piece); i < Resources.MAX_PIECES; i++) {
            if (!this.homeNodes[i].isOccupied()) {
                return false;
            }
        }
        return true;
    }

    /**
     * this method will be used to determine the highest index in the home
     *
     * @return the index
     */
    public int getHighestHomeIdx() {
        for (int i = 3; i >= 0; i--) {
            if (!homeNodes[i].isOccupied()) {
                System.out.println("Player: highestHomeIndex:" + i);
                return i;
            }

        }
        return -1;
    }

    /**
     * This method returns the give name of the Player
     *
     * @return the name of the player as a String
     */
    public String getName() {
        return name;
    }

    /**
     * This methode returns the statistics of the player
     *
     * @return the statistics of the player
     */
    public Statistic getPlayerStatistic() {
        return playerStatistic;
    }

    /**
     * This method will be used to return handCards attribute of Player class.
     *
     * @return handCards as a List of PowerCard objects.
     */
    public ArrayList<PowerCard> getHandCards() {
        return this.handCards;
    }

    /**
     * This method returns the color of the player
     *
     * @return the color of the player
     */
    public Color getColor() {
        return color;
    }

    /**
     * This method returns if the player is ready
     *
     * @return true if the player is ready, false otherwise
     */
    public boolean isReady() {
        return ready;
    }

    /**
     * This method will be used to return active attribute of Player class.
     *
     * @return active as a Boolean.
     */
    public boolean isActive() {
        return this.active;
    }

    /**
     * This method will be used to return startNodeIndex of Player class.
     *
     * @return startNodeIndex as an Integer.
     */
    public int getStartNodeIndex() {
        return this.startNodeIndex;
    }

    /**
     * This method will be used to return homeNodes attribute of Player class.
     *
     * @return homeNodes as an Array of Node objects.
     */
    public Node[] getHomeNodes() {
        return this.homeNodes;
    }

    /**
     * This method will be used to return waitingArea attribute of Player class.
     *
     * @return waitingArea as an Array of Piece objects.
     */
    public Piece[] getWaitingArea() {
        return this.waitingArea;
    }

    /**
     * This method will be used to return pieces attribute of Player class.
     *
     * @return pieces as an Array of Piece objects.
     */
    public Piece[] getPieces() {
        return this.pieces;
    }

    /**
     * This method sets the name of the player
     *
     * @param name the new name of the player
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * This method sets the color of the player
     *
     * @param color the new color of the player
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * This method sets the player to ready
     *
     * @param ready true if the player is ready, false otherwise
     */
    public void setReady(boolean ready) {
        this.ready = ready;
    }

    /**
     * This method will be used to set active attribute of Player class to the given active parameter.
     *
     * @param active as the new active value as a Boolean.
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * This method sets a piece at the given index in the home area
     *
     * @param index the index of the node
     * @param piece the piece to be set at the given index
     */
    public void setPieceInHome(int index, Piece piece) {
        this.homeNodes[index].setOccupant(piece);
    }

    /**
     * this method is used to set the homeIndex
     *
     * @param index to be set
     */
    public void setStartNodeIndex(int index){
        this.startNodeIndex=index;
    }

    /**
     * The string representation of the player
     *
     * @return the string representation of the player
     */
    @Override
    public String toString() {
        return "Player: " + name + " Color: " + color;
    }

    public void removeWaitingPiece(Piece piece) {
        for (int i = 0; i < Resources.MAX_PIECES; i++) {
            if (piece.equals(this.waitingArea[i])) {
                this.waitingArea[i] = null;
                return;
            }
        }
    }

    public void setHandCards(ArrayList<PowerCard> handCards) {
        this.handCards = handCards;
    }
}
