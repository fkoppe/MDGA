package pp.mdga.message.server;

import com.jme3.network.serializing.Serializable;
import pp.mdga.game.Board;
import pp.mdga.game.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * A message indicating that the game shall start.
 */
@Serializable
public class ServerStartGameMessage extends ServerMessage {
    /**
     * The list of players.
     */
    private final List<Player> players;

    /**
     * The board of the game.
     */
    private final Board board;

    /**
     * Constructor.
     */
    public ServerStartGameMessage() {
        super();
        this.players = new ArrayList<>();
        this.board = new Board();
    }

    /**
     * Constructor.
     *
     * @param players as the connected players as a List of Player objects.
     * @param board   as the board of the game as a Board object.
     */
    public ServerStartGameMessage(List<Player> players, Board board) {
        super();
        this.players = players;
        this.board = board;
    }

    /**
     * This method will be used to return players attribute of ServerStartGameMessage class.
     *
     * @return players as a List of Player objects.
     */
    public List<Player> getPlayers() {
        return this.players;
    }

    /**
     * This method will return board attribute of ServerStartGameMessage class.
     *
     * @return board as a Board object.
     */
    public Board getBoard() {
        return this.board;
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
        return "ServerStartGameMessage{" + "players=" + players + ", board=" + board + '}';
    }

}
