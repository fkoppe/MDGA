package pp.mdga.game;

import pp.mdga.game.card.PowerCard;
import pp.mdga.game.card.ShieldCard;
import pp.mdga.game.card.SwapCard;
import pp.mdga.game.card.TurboCard;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * The Game class represents the game state of the game.
 * It contains all the information needed to play the game.
 * The game state is updated by the game logic.
 */
public class Game {
    /**
     * The number of turbo cards available in the game.
     */
    public static final int AMOUNT_OF_TURBO_CARDS = 16;

    /**
     * The number of shield cards available in the game.
     */
    public static final int AMOUNT_OF_SHIELD_CARDS = 12;

    /**
     * The number of swap cards available in the game.
     */
    public static final int AMOUNT_OF_SWAP_CARDS = 12;

    /**
     * A map of player IDs to Player objects.
     */
    private Map<Integer, Player> players = new HashMap<>();

    /**
     * The statistics of the game.
     */
    private Statistic gameStatistics;

    /**
     * The pile of bonus cards available for drawing.
     */
    private List<PowerCard> drawPile = new ArrayList<>();

    /**
     * The pile of bonus cards that have been discarded.
     */
    private List<PowerCard> discardPile = new ArrayList<>();

    /**
     * The game board.
     */
    private Board board;

    /**
     * The die used in the game.
     */
    private Die die;

    /**
     * The host of this game
     */
    private int host = -1;

    /**
     * The color of the active player.
     */
    private Color activeColor;

    /**
     * The dice modifier.
     */
    private int diceModifier = 1;

    /**
     * The number of eyes on the dice.
     */
    private int diceEyes;

    private boolean turboFlag = false;

    private Map<Integer, Player> playerRanking = new HashMap<>();

    /**
     * This constructor creates a new Game object.
     */
    public Game() {
        gameStatistics = new Statistic();
        initializeDrawPile();
        board = new Board();
        die = new Die();
    }

    /**
     * This method initializes the draw pile with the predefined number of bonus cards.
     */
    private void initializeDrawPile() {
        this.addBonusCards(new TurboCard(), AMOUNT_OF_TURBO_CARDS);
        this.addBonusCards(new SwapCard(), AMOUNT_OF_SWAP_CARDS);
        this.addBonusCards(new ShieldCard(), AMOUNT_OF_SHIELD_CARDS);
        Collections.shuffle(this.drawPile);
    }

    /**
     * This method will be used to remove the first card of the drawPile attribute of Game class.
     *
     * @return first card as a PowerCard enumeration.
     */
    public PowerCard draw() {
        if (!this.drawPile.isEmpty()) {
            if (drawPile.size() == 1) {
                Collections.shuffle(this.discardPile);
                this.drawPile.addAll(this.discardPile);
                discardPile.clear();
            }
            return this.drawPile.remove(0);
        }
        else{
            Collections.shuffle(this.discardPile);
            this.drawPile.addAll(this.discardPile);
            discardPile.clear();
            if(!this.drawPile.isEmpty()){
                return this.drawPile.remove(0);
            }
        }
        return null;
    }

    /**
     * This method adds a number of bonus cards to the draw pile.
     *
     * @param card  the card to add
     * @param count the number of cards to add
     */
    private void addBonusCards(PowerCard card, int count) {
        Class<? extends PowerCard> cardClass = card.getClass();
        try {
            // Get the constructor for the PowerCard class
            Constructor<? extends PowerCard> constructor = cardClass.getDeclaredConstructor();
            constructor.setAccessible(true); // Necessary if the constructor is not public

            for (int i = 0; i < count; i++) {
                // Create a new instance of the PowerCard
                PowerCard newCard = constructor.newInstance();
                this.drawPile.add(newCard);
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace(); // Log any issues with reflection
        }
    }

    /**
     * This method adds a player to the game.
     *
     * @param id     the id of the player
     * @param player the player to be added
     */
    public void addPlayer(int id, Player player) {
        players.put(id, player);
    }

    /**
     * This method removes a player from the game.
     *
     * @param id the color of the player
     */
    public void removePlayer(int id) {
        players.remove(id);
    }

    /**
     * This method updates the active state of a player.
     *
     * @param id     the id of the player
     * @param active the new active state
     */
    public void updatePlayerActiveState(int id, boolean active) {
        this.players.get(id).setActive(active);
    }

    /**
     * This method will be used to check if the given color parameter is already taken.
     * If yes it will return true, otherwise false.
     *
     * @param color as the color which should be checked if taken as a Color enumeration.
     * @return true or false.
     */
    public boolean isColorTaken(Color color) {
        for (Map.Entry<Integer, Player> entry : this.players.entrySet()) {
            if (entry.getValue().getColor() == color) {
                return true;
            }
        }

        return false;
    }

    /**
     * This method will be used to return the first unused color if possible.
     *
     * @return color as a Color enumeration.
     */
    public Color getFirstUnusedColor() {
        for (Color color : Color.values()) {
            if (!isColorTaken(color)) {
                return color;
            }
        }

        return null;
    }

    /**
     * This method will be used to return the player which has the given id parameter.
     *
     * @param id as the unique id of a player as an Integer.
     * @return the player with the given id as a Player object.
     */
    public Player getPlayerById(int id) {
        return this.players.get(id);
    }

    /**
     * This method will be used to the get the player depending on the given color parameter.
     *
     * @param color as the color of the player as a Color enumeration.
     * @return the player with the given color as a Player object.
     */
    public Player getPlayerByColor(Color color) {
        for (Map.Entry<Integer, Player> entry : this.players.entrySet()) {
            if (entry.getValue().getColor() == color) {
                return entry.getValue();
            }
        }

        return null;
    }

    /**
     * This method will be used to the get the active player
     *
     * @return the active player
     */
    public Player getActivePlayer() {
        return getPlayerByColor(activeColor);
    }

    /**
     * This method will be used to return all connected players as a list.
     *
     * @return players as a List of Player objects.
     */
    public List<Player> getPlayersAsList() {
        return new ArrayList<>(this.players.values());
    }

    /**
     * This method will be used to return the id of the active player depending on the activeColor attribute of Game
     * class.
     *
     * @return the id of the active player as an Integer.
     */
    public int getActivePlayerId() {
        return this.getPlayerIdByColor(this.activeColor);
    }

    /**
     * This method will be used to return the id of the Player defined by the given color parameter.
     *
     * @param color as the color of the player as a Color enumeration.
     * @return the id of the player as an Integer.
     */
    public int getPlayerIdByColor(Color color) {
        for (Map.Entry<Integer, Player> entry : this.players.entrySet()) {
            if (entry.getValue().getColor() == color) {
                return entry.getKey();
            }
        }

        return -1;
    }

    /**
     * This method will be used to return the number of active players of this game.
     *
     * @return activePlayers as an Integer.
     */
    public int getNumberOfActivePlayers() {
        int activePlayers = 0;
        for (Map.Entry<Integer, Player> entry : this.players.entrySet()) {
            if (entry.getValue().isActive()) {
                activePlayers++;
            }
        }

        return activePlayers;
    }

    /**
     * This method will be used to check if all players are ready.
     * If yes it will return true, otherwise false.
     *
     * @return true or false.
     */
    public boolean areAllReady() {
        for (Map.Entry<Integer, Player> entry : this.players.entrySet()) {
            if (!entry.getValue().isReady()) {
                return false;
            }
        }

        return true;
    }

    /**
     * This method will be used to return a piece based on the UUID.
     *
     * @return the piece specified by the UUID
     */
    public Piece getPieceThroughUUID(UUID pieceId) {
        for (var player : this.getPlayers().values()) {
            for (var piece : player.getPieces()) {
                if (piece.getUuid().equals(pieceId)) {
                    return piece;
                }
            }
        }
        return null;
    }

    /**
     * This method will be used to check if this client is the host for the game.
     *
     * @return true or false.
     */
    public boolean isHost() {
        return this.host != -1;
    }

    public void setTurboFlag(boolean flag) {
        this.turboFlag = flag;
    }

    public boolean getTurboFlag() {
        return this.turboFlag;
    }

    /**
     * This method returns the players.
     *
     * @return the players
     */
    public Map<Integer, Player> getPlayers() {
        return players;
    }

    /**
     * This method returns the game statistics.
     *
     * @return the game statistics
     */
    public Statistic getGameStatistics() {
        return gameStatistics;
    }

    /**
     * This method will be used to return drawPile attribute of Game class.
     *
     * @return drawPile as a List of PowerCard objects.
     */
    public List<PowerCard> getDrawPile() {
        return this.drawPile;
    }

    /**
     * This method will be used to return discardPile attribute of Game class.
     *
     * @return discardPile as a List of PowerCard objects.
     */
    public List<PowerCard> getDiscardPile() {
        return this.discardPile;
    }

    /**
     * This method returns the board.
     *
     * @return the board
     */
    public Board getBoard() {
        return board;
    }

    /**
     * This method will be used to return die attribute of Game class.
     *
     * @return die as a Die object.
     */
    public Die getDie() {
        return this.die;
    }

    /**
     * This method returns the active color.
     *
     * @return the active color
     */
    public Color getActiveColor() {
        return activeColor;
    }

    /**
     * This method will be used to return host attribute of Game class.
     *
     * @return host as an Integer.
     */
    public int getHost() {
        return this.host;
    }

    /**
     * This method sets the players.
     *
     * @param players the new players
     */
    public void setPlayers(Map<Integer, Player> players) {
        this.players = players;
    }

    /**
     * This method sets the game statistics.
     *
     * @param gameStatistics the new game statistics
     */
    public void setGameStatistics(Statistic gameStatistics) {
        this.gameStatistics = gameStatistics;
    }

    /**
     * This method will be used to set drawPile attribute of Game class to the given discardPile parameter.
     * It will be used to test cases.
     *
     * @param drawPile the new value of drawPile attribute as a List of PowerCards.
     */
    public void setDrawPile(List<PowerCard> drawPile) {
        this.drawPile = drawPile;
    }

    /**
     * This method will be used to set discardPile attribute of Game class to the given discardPile parameter.
     * It will be used to test cases.
     *
     * @param discardPile the new value of discardPile attribute as a List of PowerCards.
     */
    public void setDiscardPile(List<PowerCard> discardPile) {
        this.discardPile = discardPile;
    }

    /**
     * This method sets the board.
     *
     * @param board the new board
     */
    public void setBoard(Board board) {
        this.board = board;
    }

    /**
     * This method sets the active color.
     *
     * @param activeColor the new active color
     */
    public void setActiveColor(Color activeColor) {
        this.activeColor = activeColor;
    }

    /**
     * This method will be used to set die attribute of Game class to the given die parameter.
     *
     * @param die as the new value of die as a Die object.
     */
    public void setDie(Die die) {
        this.die = die;
    }

    /**
     * This method will be used to set host attribute of Game class to the given host parameter.
     *
     * @param host as the new value of host as an Integer.
     */
    public void setHost(int host) {
        this.host = host;
    }

    /**
     * This method will be used to get the dice eyes.
     *
     * @return the dice eyes
     */
    public int getDiceEyes() {
        return diceEyes;
    }

    /**
     * This method is used to get the dice modifier.
     *
     * @return the dice modifier
     */
    public int getDiceModifier() {
        return diceModifier;
    }

    /**
     * This method will be used to set the dice eyes.
     *
     * @param diceEyes the new dice eyes
     */
    public void setDiceEyes(int diceEyes) {
        this.diceEyes = diceEyes;
    }

    /**
     * This method is used to set the dice modifier.
     *
     * @param diceModifier the new dice modifier
     */
    public void setDiceModifier(int diceModifier) {
        this.diceModifier = diceModifier;
    }

    public Map<Integer, Player> getPlayerRanking() {
        return playerRanking;
    }

    public void setPlayerRanking(Map<Integer, Player> playerRanking) {
        this.playerRanking = playerRanking;
    }
}
