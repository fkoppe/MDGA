package pp.mdga.game;

import org.junit.Before;
import org.junit.Test;
import pp.mdga.game.card.PowerCard;
import pp.mdga.game.card.ShieldCard;
import pp.mdga.game.card.SwapCard;
import pp.mdga.game.card.TurboCard;
import pp.mdga.message.client.AnimationEndMessage;
import pp.mdga.message.client.LobbyReadyMessage;
import pp.mdga.message.client.NoPowerCardMessage;
import pp.mdga.message.client.RequestDieMessage;
import pp.mdga.message.client.RequestMoveMessage;
import pp.mdga.message.client.SelectTSKMessage;
import pp.mdga.message.client.StartGameMessage;
import pp.mdga.message.server.ServerMessage;
import pp.mdga.server.ServerGameLogic;
import pp.mdga.server.ServerSender;
import pp.mdga.server.automaton.game.TurnState;
import pp.mdga.server.automaton.game.turn.RollDiceState;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * this test-class tests the testcases T001-T016
 */
public class GameTest {

    private ServerGameLogic logic;
    private Game game;

    private Player playerHost;
    private int IDHost;
    private Player playerClient;
    private int IDClient;

    /**
     * this method is used to set the variables for this test-class
     */
    @Before
    public void setup() {
        game = new Game();
        logic = new ServerGameLogic(new ServerSender() {
            @Override
            public void send(int id, ServerMessage message) {

            }

            @Override
            public void broadcast(ServerMessage message) {

            }

            @Override
            public void disconnectClient(int id) {

            }

            @Override
            public void shutdown() {

            }
        }, game);

        playerHost = new Player("Host");
        IDHost = 1;
        playerClient = new Player("Client");
        IDClient = 2;

        game.addPlayer(IDHost, playerHost);
        game.setHost(IDHost);
        game.addPlayer(IDClient, playerClient);
    }

    /**
     * this method tests, that, at the beginning of the game, all players have one piece on the start-field and one powerCard
     * <p>
     * T001
     */
    @Test
    public void testStartLineUp() {
        //tests if both player have no color and are not ready
        assertFalse(playerClient.isReady());
        assertFalse(playerHost.isReady());
        assertEquals(playerClient.getColor(), Color.NONE);
        assertEquals(playerHost.getColor(), Color.NONE);

        //send the selectTSK-message
        logic.received(new SelectTSKMessage(Color.CYBER), IDClient);
        logic.received(new SelectTSKMessage(Color.ARMY), IDHost);

        //tests if the Tsk's are set correctly
        assertFalse(playerClient.isReady());
        assertFalse(playerHost.isReady());
        assertEquals(playerClient.getColor(), Color.CYBER);
        assertEquals(playerHost.getColor(), Color.ARMY);

        //sends and tests the readyMessage for the client
        logic.received(new LobbyReadyMessage(), IDClient);
        assertTrue(playerClient.isReady());
        assertFalse(playerHost.isReady());

        //try to start the game, which should fail
        logic.received(new StartGameMessage(), IDHost);
        assertEquals(logic.getCurrentState(), logic.getLobbyState());

        //sends and tests the readyMessage for the host
        logic.received(new LobbyReadyMessage(), IDHost);
        assertTrue(playerClient.isReady());
        assertTrue(playerHost.isReady());

        //tests if the game has started after the gameStartMessage
        logic.received(new StartGameMessage(), IDHost);
        assertEquals(logic.getCurrentState(), logic.getGameState());

        //tests the start positions for the pieces
        assertTrue(game.getBoard().getInfield()[playerHost.getStartNodeIndex()].isOccupied());
        assertTrue(game.getBoard().getInfield()[playerHost.getStartNodeIndex()].isOccupied(playerHost.getColor()));
        assertTrue(game.getBoard().getInfield()[playerClient.getStartNodeIndex()].isOccupied());
        assertTrue(game.getBoard().getInfield()[playerClient.getStartNodeIndex()].isOccupied(playerClient.getColor()));

        //tests if the players have no handCards
        assertFalse(playerHost.getHandCards().isEmpty());
        assertFalse(playerClient.getHandCards().isEmpty());
        assertEquals(1, playerHost.getHandCards().size());
        assertEquals(1, playerClient.getHandCards().size());
    }

    /**
     * this method tests the drawPile
     * <p>
     * T002
     */
    @Test
    public void testCreatePowerCardDeck() {
        //tests if both player have no color and are not ready
        assertFalse(playerClient.isReady());
        assertFalse(playerHost.isReady());
        assertEquals(playerClient.getColor(), Color.NONE);
        assertEquals(playerHost.getColor(), Color.NONE);

        //send the selectTSK-message
        logic.received(new SelectTSKMessage(Color.CYBER), IDClient);
        logic.received(new SelectTSKMessage(Color.ARMY), IDHost);

        //sends and tests the readyMessage for the client
        logic.received(new LobbyReadyMessage(), IDClient);
        logic.received(new LobbyReadyMessage(), IDHost);
        assertTrue(playerClient.isReady());
        assertTrue(playerHost.isReady());

        //tests if the game has started after the gameStartMessage
        logic.received(new StartGameMessage(), IDHost);
        assertEquals(logic.getCurrentState(), logic.getGameState());

        //tests if the players have no handCards
        assertFalse(playerHost.getHandCards().isEmpty());
        assertFalse(playerClient.getHandCards().isEmpty());
        assertEquals(1, playerHost.getHandCards().size());
        assertEquals(1, playerClient.getHandCards().size());

        assertEquals(38, game.getDrawPile().size());
        ArrayList<PowerCard> cards = new ArrayList<>(game.getDrawPile());
        cards.add(playerClient.getHandCards().get(0));
        cards.add(playerHost.getHandCards().get(0));

        //tests if the right amount of PowerCards are in the DrawPile
        assertEquals(12, cards.stream().filter((powerCard -> powerCard instanceof ShieldCard)).toArray().length);
        assertEquals(12, cards.stream().filter((powerCard -> powerCard instanceof SwapCard)).toArray().length);
        assertEquals(16, cards.stream().filter((powerCard -> powerCard instanceof TurboCard)).toArray().length);
    }

    /**
     * this test-method tests the case, if a player finishes
     * <p>
     * T003
     */
    @Test
    public void testGameFinishes() {
        //tests if both player have no color and are not ready
        assertFalse(playerClient.isReady());
        assertFalse(playerHost.isReady());
        assertEquals(playerClient.getColor(), Color.NONE);
        assertEquals(playerHost.getColor(), Color.NONE);

        //send the selectTSK-message
        logic.received(new SelectTSKMessage(Color.CYBER), IDClient);
        logic.received(new SelectTSKMessage(Color.ARMY), IDHost);

        //sends and tests the readyMessage for the client
        logic.received(new LobbyReadyMessage(), IDClient);
        logic.received(new LobbyReadyMessage(), IDHost);
        assertTrue(playerClient.isReady());
        assertTrue(playerHost.isReady());

        //tests if the game has started after the gameStartMessage
        logic.received(new StartGameMessage(), IDHost);
        assertEquals(logic.getCurrentState(), logic.getGameState());

        //roll the order
        game.setDie(new Die(4));
        logic.received(new RequestDieMessage(), IDHost);
        game.setDie(new Die(3));
        logic.received(new RequestDieMessage(), IDClient);
        logic.received(new AnimationEndMessage(), IDClient);
        logic.received(new AnimationEndMessage(), IDHost);

        //tests that the server is in the animation-state
        assertEquals(logic.getCurrentState(), logic.getGameState());
        assertEquals(logic.getGameState().getCurrentState(), logic.getGameState().getAnimationState());

        logic.received(new AnimationEndMessage(), IDHost);
        logic.received(new AnimationEndMessage(), IDClient);

        //set all pieces except one in the home, the other direct in front of the home
        Piece piece1 = game.getBoard().getInfield()[playerHost.getStartNodeIndex()].getOccupant();
        Piece piece2 = playerHost.getWaitingArea()[1];
        Piece piece3 = playerHost.getWaitingArea()[2];
        Piece piece4 = playerHost.getWaitingArea()[3];
        piece1.setState(PieceState.HOMEFINISHED);
        piece2.setState(PieceState.HOMEFINISHED);
        piece3.setState(PieceState.HOMEFINISHED);
        piece4.setState(PieceState.ACTIVE);
        playerHost.removeWaitingPiece(piece1);
        playerHost.removeWaitingPiece(piece2);
        playerHost.removeWaitingPiece(piece3);
        playerHost.removeWaitingPiece(piece4);
        playerHost.setPieceInHome(3,piece1);
        playerHost.setPieceInHome(2,piece2);
        playerHost.setPieceInHome(1,piece3);

        game.getBoard().getInfield()[playerHost.getStartNodeIndex()-1].setOccupant(piece4);

        logic.received(new NoPowerCardMessage(), IDHost);

        //tests if the server is in first-roll
        assertEquals(logic.getCurrentState(), logic.getGameState());
        assertEquals(logic.getGameState().getCurrentState(), logic.getGameState().getTurnState());
        TurnState turn = logic.getGameState().getTurnState();
        assertEquals(turn.getCurrentState(), turn.getRollDiceState());
        RollDiceState rollDiceState = turn.getRollDiceState();
        assertEquals(rollDiceState.getCurrentState(), rollDiceState.getFirstRollState());

        game.setDie(new Die(1));
        logic.received(new RequestDieMessage(), IDHost);
        logic.received(new AnimationEndMessage(), IDHost);

        //tests if the server is in choose-piece-state
        assertEquals(logic.getCurrentState(), logic.getGameState());
        assertEquals(logic.getGameState().getCurrentState(), logic.getGameState().getTurnState());
        assertEquals(turn.getCurrentState(), turn.getChoosePieceState());
        assertEquals(turn.getChoosePieceState().getSelectPieceState(), turn.getChoosePieceState().getCurrentState());

        logic.received(new RequestMoveMessage(piece4),IDHost);
        //tests if the server is in choose-piece-state
        assertEquals(logic.getCurrentState(), logic.getGameState());
        assertEquals(logic.getGameState().getCurrentState(), logic.getGameState().getTurnState());
        assertEquals(turn.getCurrentState(), turn.getMovePieceState());


        logic.received(new AnimationEndMessage(),IDHost);
        logic.received(new AnimationEndMessage(),IDClient);

        assertTrue(playerHost.isFinished());

        //tests if the server is in Ceremony
        assertEquals(logic.getCurrentState(), logic.getCeremonyState());
    }

    /**
     * this test-method is used to test, when a player finishes
     * <p>
     * T003
     */
    @Test
    public void testPlayerFinishes() {
        //tests if both player have no color and are not ready
        assertFalse(playerClient.isReady());
        assertFalse(playerHost.isReady());
        assertEquals(playerClient.getColor(), Color.NONE);
        assertEquals(playerHost.getColor(), Color.NONE);

        //send the selectTSK-message
        logic.received(new SelectTSKMessage(Color.CYBER), IDClient);
        logic.received(new SelectTSKMessage(Color.ARMY), IDHost);

        //sends and tests the readyMessage for the client
        logic.received(new LobbyReadyMessage(), IDClient);
        logic.received(new LobbyReadyMessage(), IDHost);
        assertTrue(playerClient.isReady());
        assertTrue(playerHost.isReady());

        //tests if the game has started after the gameStartMessage
        logic.received(new StartGameMessage(), IDHost);
        assertEquals(logic.getCurrentState(), logic.getGameState());

        //roll the order
        game.setDie(new Die(4));
        logic.received(new RequestDieMessage(), IDHost);
        game.setDie(new Die(3));
        logic.received(new RequestDieMessage(), IDClient);
        logic.received(new AnimationEndMessage(), IDClient);
        logic.received(new AnimationEndMessage(), IDHost);

        //tests that the server is in the animation-state
        assertEquals(logic.getCurrentState(), logic.getGameState());
        assertEquals(logic.getGameState().getCurrentState(), logic.getGameState().getAnimationState());

        logic.received(new AnimationEndMessage(), IDHost);
        logic.received(new AnimationEndMessage(), IDClient);

        //set all pieces except one in the home, the other direct in front of the home
        Piece piece1 = game.getBoard().getInfield()[playerHost.getStartNodeIndex()].getOccupant();
        Piece piece2 = playerHost.getWaitingArea()[1];
        Piece piece3 = playerHost.getWaitingArea()[2];
        Piece piece4 = playerHost.getWaitingArea()[3];
        piece1.setState(PieceState.HOMEFINISHED);
        piece2.setState(PieceState.HOMEFINISHED);
        piece3.setState(PieceState.HOMEFINISHED);
        piece4.setState(PieceState.ACTIVE);
        playerHost.removeWaitingPiece(piece1);
        playerHost.removeWaitingPiece(piece2);
        playerHost.removeWaitingPiece(piece3);
        playerHost.removeWaitingPiece(piece4);
        playerHost.setPieceInHome(3,piece1);
        playerHost.setPieceInHome(2,piece2);
        playerHost.setPieceInHome(1,piece3);

        game.getBoard().getInfield()[playerHost.getStartNodeIndex()-1].setOccupant(piece4);

        logic.received(new NoPowerCardMessage(), IDHost);

        //tests if the server is in first-roll
        assertEquals(logic.getCurrentState(), logic.getGameState());
        assertEquals(logic.getGameState().getCurrentState(), logic.getGameState().getTurnState());
        TurnState turn = logic.getGameState().getTurnState();
        assertEquals(turn.getCurrentState(), turn.getRollDiceState());
        RollDiceState rollDiceState = turn.getRollDiceState();
        assertEquals(rollDiceState.getCurrentState(), rollDiceState.getFirstRollState());

        game.setDie(new Die(1));
        logic.received(new RequestDieMessage(), IDHost);
        logic.received(new AnimationEndMessage(), IDHost);

        //tests if the server is in choose-piece-state
        assertEquals(logic.getCurrentState(), logic.getGameState());
        assertEquals(logic.getGameState().getCurrentState(), logic.getGameState().getTurnState());
        assertEquals(turn.getCurrentState(), turn.getChoosePieceState());
        assertEquals(turn.getChoosePieceState().getSelectPieceState(), turn.getChoosePieceState().getCurrentState());

        logic.received(new RequestMoveMessage(piece4),IDHost);
        //tests if the server is in choose-piece-state
        assertEquals(logic.getCurrentState(), logic.getGameState());
        assertEquals(logic.getGameState().getCurrentState(), logic.getGameState().getTurnState());
        assertEquals(turn.getCurrentState(), turn.getMovePieceState());


        logic.received(new AnimationEndMessage(),IDHost);
        logic.received(new AnimationEndMessage(),IDClient);

        assertTrue(playerHost.isFinished());
    }

    /**
     * * this test-method tests that the player has 3 chances to roll a 6, if all his pieces are in the waiting-area
     * <p>
     * T005
     */
    @Test
    public void testAllPiecesInWaitingArea() {
        //tests if both player have no color and are not ready
        assertFalse(playerClient.isReady());
        assertFalse(playerHost.isReady());
        assertEquals(playerClient.getColor(), Color.NONE);
        assertEquals(playerHost.getColor(), Color.NONE);

        //send the selectTSK-message
        logic.received(new SelectTSKMessage(Color.CYBER), IDClient);
        logic.received(new SelectTSKMessage(Color.ARMY), IDHost);

        //sends and tests the readyMessage for the client
        logic.received(new LobbyReadyMessage(), IDClient);
        logic.received(new LobbyReadyMessage(), IDHost);
        assertTrue(playerClient.isReady());
        assertTrue(playerHost.isReady());

        //tests if the game has started after the gameStartMessage
        logic.received(new StartGameMessage(), IDHost);
        assertEquals(logic.getCurrentState(), logic.getGameState());

        //roll the order
        game.setDie(new Die(4));
        logic.received(new RequestDieMessage(), IDHost);
        game.setDie(new Die(3));
        logic.received(new RequestDieMessage(), IDClient);
        logic.received(new AnimationEndMessage(), IDClient);
        logic.received(new AnimationEndMessage(), IDHost);

        //tests that the server is in the animation-state
        assertEquals(logic.getCurrentState(), logic.getGameState());
        assertEquals(logic.getGameState().getCurrentState(), logic.getGameState().getAnimationState());

        //set all pieces of host in waiting-field
        game.getBoard().getInfield()[playerHost.getStartNodeIndex()].getOccupant().setState(PieceState.WAITING);
        playerHost.addWaitingPiece(game.getBoard().getInfield()[playerHost.getStartNodeIndex()].getOccupant());
        game.getBoard().getInfield()[playerHost.getStartNodeIndex()].clearOccupant();

        logic.received(new AnimationEndMessage(), IDHost);
        logic.received(new AnimationEndMessage(), IDClient);

        logic.received(new NoPowerCardMessage(), IDHost);

        //tests if the server is in first-roll
        assertEquals(logic.getCurrentState(), logic.getGameState());
        assertEquals(logic.getGameState().getCurrentState(), logic.getGameState().getTurnState());
        TurnState turn = logic.getGameState().getTurnState();
        assertEquals(turn.getCurrentState(), turn.getRollDiceState());
        RollDiceState rollDiceState = turn.getRollDiceState();
        assertEquals(rollDiceState.getCurrentState(), rollDiceState.getFirstRollState());

        game.setDie(new Die(5));
        logic.received(new RequestDieMessage(), IDHost);
        logic.received(new AnimationEndMessage(), IDHost);

        //tests if the server is in second-roll-state
        assertEquals(logic.getCurrentState(), logic.getGameState());
        assertEquals(logic.getGameState().getCurrentState(), logic.getGameState().getTurnState());
        assertEquals(turn.getCurrentState(), turn.getRollDiceState());
        assertEquals(rollDiceState.getCurrentState(), rollDiceState.getSecondRollState());

        game.setDie(new Die(5));
        logic.received(new RequestDieMessage(), IDHost);
        logic.received(new AnimationEndMessage(), IDHost);

        //tests if the server is in second-roll-state
        assertEquals(logic.getCurrentState(), logic.getGameState());
        assertEquals(logic.getGameState().getCurrentState(), logic.getGameState().getTurnState());
        assertEquals(turn.getCurrentState(), turn.getRollDiceState());
        assertEquals(rollDiceState.getCurrentState(), rollDiceState.getThirdRollState());
    }

    /**
     * this test-method tests that the player has 3 chances to roll a 6, if all his pieces are in the waiting-area
     * and if he rolls a 6, he can choose a piece out of the waiting-area
     * T004
     */
    @Test
    public void test3TriesFor6() {
        //tests if both player have no color and are not ready
        assertFalse(playerClient.isReady());
        assertFalse(playerHost.isReady());
        assertEquals(playerClient.getColor(), Color.NONE);
        assertEquals(playerHost.getColor(), Color.NONE);

        //send the selectTSK-message
        logic.received(new SelectTSKMessage(Color.CYBER), IDClient);
        logic.received(new SelectTSKMessage(Color.ARMY), IDHost);

        //sends and tests the readyMessage for the client
        logic.received(new LobbyReadyMessage(), IDClient);
        logic.received(new LobbyReadyMessage(), IDHost);
        assertTrue(playerClient.isReady());
        assertTrue(playerHost.isReady());

        //tests if the game has started after the gameStartMessage
        logic.received(new StartGameMessage(), IDHost);
        assertEquals(logic.getCurrentState(), logic.getGameState());

        //roll the order
        game.setDie(new Die(4));
        logic.received(new RequestDieMessage(), IDHost);
        game.setDie(new Die(3));
        logic.received(new RequestDieMessage(), IDClient);
        logic.received(new AnimationEndMessage(), IDClient);
        logic.received(new AnimationEndMessage(), IDHost);

        //tests that the server is in the animation-state
        assertEquals(logic.getCurrentState(), logic.getGameState());
        assertEquals(logic.getGameState().getCurrentState(), logic.getGameState().getAnimationState());

        //set all pieces of host in waiting-field
        game.getBoard().getInfield()[playerHost.getStartNodeIndex()].getOccupant().setState(PieceState.WAITING);
        playerHost.addWaitingPiece(game.getBoard().getInfield()[playerHost.getStartNodeIndex()].getOccupant());
        game.getBoard().getInfield()[playerHost.getStartNodeIndex()].clearOccupant();

        logic.received(new AnimationEndMessage(), IDHost);
        logic.received(new AnimationEndMessage(), IDClient);

        logic.received(new NoPowerCardMessage(), IDHost);

        //tests if the server is in first-roll
        assertEquals(logic.getCurrentState(), logic.getGameState());
        assertEquals(logic.getGameState().getCurrentState(), logic.getGameState().getTurnState());
        TurnState turn = logic.getGameState().getTurnState();
        assertEquals(turn.getCurrentState(), turn.getRollDiceState());
        RollDiceState rollDiceState = turn.getRollDiceState();
        assertEquals(rollDiceState.getCurrentState(), rollDiceState.getFirstRollState());

        game.setDie(new Die(5));
        logic.received(new RequestDieMessage(), IDHost);
        logic.received(new AnimationEndMessage(), IDHost);

        //tests if the server is in second-roll-state
        assertEquals(logic.getCurrentState(), logic.getGameState());
        assertEquals(logic.getGameState().getCurrentState(), logic.getGameState().getTurnState());
        assertEquals(turn.getCurrentState(), turn.getRollDiceState());
        assertEquals(rollDiceState.getCurrentState(), rollDiceState.getSecondRollState());

        game.setDie(new Die(6));
        logic.received(new RequestDieMessage(), IDHost);
        logic.received(new AnimationEndMessage(), IDHost);

        //tests if the server is in second-roll-state
        assertEquals(logic.getCurrentState(), logic.getGameState());
        assertEquals(logic.getGameState().getCurrentState(), logic.getGameState().getTurnState());
        assertEquals(turn.getCurrentState(), turn.getChoosePieceState());
        assertEquals(turn.getChoosePieceState().getWaitingPieceState(), turn.getChoosePieceState().getCurrentState());
    }

    // UC-Game-05
    @Test
    public void testGameTerminates() {
        // TODO: Implement test logic for game termination
    }

    /**
     * this method is used to test, that the start player is determined correctly
     * <p>
     * T006
     */
    @Test
    public void testStartingOrder() {
        //tests if both player have no color and are not ready
        assertFalse(playerClient.isReady());
        assertFalse(playerHost.isReady());
        assertEquals(playerClient.getColor(), Color.NONE);
        assertEquals(playerHost.getColor(), Color.NONE);

        //send the selectTSK-message
        logic.received(new SelectTSKMessage(Color.CYBER), IDClient);
        logic.received(new SelectTSKMessage(Color.ARMY), IDHost);

        //sends and tests the readyMessage for the client
        logic.received(new LobbyReadyMessage(), IDClient);
        logic.received(new LobbyReadyMessage(), IDHost);
        assertTrue(playerClient.isReady());
        assertTrue(playerHost.isReady());

        //tests if the game has started after the determineStartPlayerState
        logic.received(new StartGameMessage(), IDHost);
        assertEquals(logic.getCurrentState(), logic.getGameState());
        assertEquals(logic.getGameState().getCurrentState(), logic.getGameState().getDetermineStartPlayerState());

        //roll the order
        game.setDie(new Die(4));
        logic.received(new RequestDieMessage(), IDHost);
        game.setDie(new Die(3));
        logic.received(new RequestDieMessage(), IDClient);
        logic.received(new AnimationEndMessage(), IDClient);
        logic.received(new AnimationEndMessage(), IDHost);

        //tests that the server is in the animation-state
        assertEquals(logic.getCurrentState(), logic.getGameState());
        assertEquals(logic.getGameState().getCurrentState(), logic.getGameState().getAnimationState());

        logic.received(new AnimationEndMessage(), IDClient);
        logic.received(new AnimationEndMessage(), IDHost);

        assertEquals(logic.getCurrentState(), logic.getGameState());
        assertEquals(logic.getGameState().getCurrentState(), logic.getGameState().getTurnState());

        //tests if the host is the active player
        assertEquals(game.getActiveColor(), playerHost.getColor());

        assertEquals(playerHost.getColor().next(game), playerClient.getColor());
    }

    /**
     * this method test the determination of the start player, if two players roll even
     * <p>
     * T007
     */
    @Test
    public void testDouble() {
        //tests if both player have no color and are not ready
        assertFalse(playerClient.isReady());
        assertFalse(playerHost.isReady());
        assertEquals(playerClient.getColor(), Color.NONE);
        assertEquals(playerHost.getColor(), Color.NONE);

        //send the selectTSK-message
        logic.received(new SelectTSKMessage(Color.CYBER), IDClient);
        logic.received(new SelectTSKMessage(Color.ARMY), IDHost);

        //sends and tests the readyMessage for the client
        logic.received(new LobbyReadyMessage(), IDClient);
        logic.received(new LobbyReadyMessage(), IDHost);
        assertTrue(playerClient.isReady());
        assertTrue(playerHost.isReady());

        //tests if the game has started after the determineStartPlayerState
        logic.received(new StartGameMessage(), IDHost);
        assertEquals(logic.getCurrentState(), logic.getGameState());
        assertEquals(logic.getGameState().getCurrentState(), logic.getGameState().getDetermineStartPlayerState());

        //roll the order
        game.setDie(new Die(4));
        logic.received(new RequestDieMessage(), IDHost);
        logic.received(new RequestDieMessage(), IDClient);
        logic.received(new AnimationEndMessage(), IDClient);
        logic.received(new AnimationEndMessage(), IDHost);

        //tests that the server is in the determineStartPlayer-state
        assertEquals(logic.getCurrentState(), logic.getGameState());
        assertEquals(logic.getGameState().getCurrentState(), logic.getGameState().getDetermineStartPlayerState());

        //roll the order the second time
        game.setDie(new Die(4));
        logic.received(new RequestDieMessage(), IDHost);
        game.setDie(new Die(3));
        logic.received(new RequestDieMessage(), IDClient);
        logic.received(new AnimationEndMessage(), IDClient);
        logic.received(new AnimationEndMessage(), IDHost);

        //tests that the server is in the animation-state
        assertEquals(logic.getCurrentState(), logic.getGameState());
        assertEquals(logic.getGameState().getCurrentState(), logic.getGameState().getAnimationState());

        logic.received(new AnimationEndMessage(), IDClient);
        logic.received(new AnimationEndMessage(), IDHost);

        assertEquals(logic.getCurrentState(), logic.getGameState());
        assertEquals(logic.getGameState().getCurrentState(), logic.getGameState().getTurnState());

        //tests if the host is the active player
        assertEquals(game.getActiveColor(), playerHost.getColor());
    }

    /**
     *
     * this test-method tests that the next Player is set, after the current player has finished his turn
     * <p>
     * T010
     */
    @Test
    public void testChangeActivePlayer() {
        //tests if both player have no color and are not ready
        assertFalse(playerClient.isReady());
        assertFalse(playerHost.isReady());
        assertEquals(playerClient.getColor(), Color.NONE);
        assertEquals(playerHost.getColor(), Color.NONE);

        //send the selectTSK-message
        logic.received(new SelectTSKMessage(Color.CYBER), IDClient);
        logic.received(new SelectTSKMessage(Color.ARMY), IDHost);

        //sends and tests the readyMessage for the client
        logic.received(new LobbyReadyMessage(), IDClient);
        logic.received(new LobbyReadyMessage(), IDHost);
        assertTrue(playerClient.isReady());
        assertTrue(playerHost.isReady());

        //tests if the game has started after the determineStartPlayerState
        logic.received(new StartGameMessage(), IDHost);
        assertEquals(logic.getCurrentState(), logic.getGameState());
        assertEquals(logic.getGameState().getCurrentState(), logic.getGameState().getDetermineStartPlayerState());

        //roll the order
        game.setDie(new Die(4));
        logic.received(new RequestDieMessage(), IDHost);
        game.setDie(new Die(3));
        logic.received(new RequestDieMessage(), IDClient);
        logic.received(new AnimationEndMessage(), IDClient);
        logic.received(new AnimationEndMessage(), IDHost);

        //tests that the server is in the animation-state
        assertEquals(logic.getCurrentState(), logic.getGameState());
        assertEquals(logic.getGameState().getCurrentState(), logic.getGameState().getAnimationState());

        logic.received(new AnimationEndMessage(), IDClient);
        logic.received(new AnimationEndMessage(), IDHost);

        assertEquals(logic.getCurrentState(), logic.getGameState());
        assertEquals(logic.getGameState().getCurrentState(), logic.getGameState().getTurnState());

        //tests if the host is the active player
        assertEquals(game.getActiveColor(), playerHost.getColor());

        //hosts plays no power-card
        logic.received(new NoPowerCardMessage(), IDHost);

        //simulates a roll and the move of a piece for the host
        logic.received(new RequestDieMessage(), IDHost);
        logic.received(new AnimationEndMessage(), IDHost);
        logic.received(new RequestMoveMessage(game.getBoard().getInfield()[playerHost.getStartNodeIndex()].getOccupant()), IDHost);

        //tests if the server is in movePiece
        assertEquals(logic.getGameState().getCurrentState(), logic.getGameState().getTurnState());
        assertEquals(logic.getGameState().getTurnState().getCurrentState(), logic.getGameState().getTurnState().getMovePieceState());

        //sends the animationEndMessages
        logic.received(new AnimationEndMessage(), IDClient);
        logic.received(new AnimationEndMessage(), IDHost);

        //tests if there is a new active Player
        assertEquals(game.getActiveColor(), playerClient.getColor());
    }

    @Test
    public void testUseTurbo() {
        //TODO
    }

    /**
     * this test-method tests the chances fo the turbo-card
     * <p>
     * T012
     */
    @Test
    public void testMuliplicationChance() {
        Die die = new Die();

        ArrayList<Integer> modifier0 = new ArrayList<>();
        ArrayList<Integer> modifier1 = new ArrayList<>();
        ArrayList<Integer> modifier2 = new ArrayList<>();
        for (int i = 0; i < 10000000; i++) {
            die.modify();
            switch (die.getDieModifier()) {
                case 0 -> modifier0.add(0);
                case 1 -> modifier1.add(1);
                default -> modifier2.add(2);
            }
        }
        //System.out.println("modifier2.size(): " + modifier2.size() + " modifier1.size(): " + modifier1.size()+" modifier0.size(): " + modifier0.size());

        //test with 5% range for the correct changes
        assertTrue(5700000 < modifier2.size() && modifier2.size() < 6300000);
        assertTrue(1950000 < modifier1.size() && modifier1.size() < 2050000);
        assertTrue(1950000 < modifier0.size() && modifier0.size() < 2050000);
    }

    // UC-Game-10
    @Test
    public void testTurboOn6() {
        // TODO: Implement test logic for turbo activation on rolling a 6
    }

    // UC-Game-11
    @Test
    public void testAwardCeremony() {
        // TODO: Implement test logic for award ceremony
    }

    @Test
    public void testStatistics() {
        // TODO: Implement test logic for gathering or displaying game statistics
    }

    // UC-Game-12
    @Test
    public void testRefillPowerCardDeck() {
        //tests if both player have no color and are not ready
        assertFalse(playerClient.isReady());
        assertFalse(playerHost.isReady());
        assertEquals(playerClient.getColor(), Color.NONE);
        assertEquals(playerHost.getColor(), Color.NONE);

        //send the selectTSK-message
        logic.received(new SelectTSKMessage(Color.CYBER), IDClient);
        logic.received(new SelectTSKMessage(Color.ARMY), IDHost);

        //sends and tests the readyMessage for the client
        logic.received(new LobbyReadyMessage(), IDClient);
        logic.received(new LobbyReadyMessage(), IDHost);
        assertTrue(playerClient.isReady());
        assertTrue(playerHost.isReady());

        //tests if the game has started after the determineStartPlayerState
        logic.received(new StartGameMessage(), IDHost);
        assertEquals(logic.getCurrentState(), logic.getGameState());
        assertEquals(logic.getGameState().getCurrentState(), logic.getGameState().getDetermineStartPlayerState());

        //roll the order
        game.setDie(new Die(4));
        logic.received(new RequestDieMessage(), IDHost);
        game.setDie(new Die(3));
        logic.received(new RequestDieMessage(), IDClient);
        logic.received(new AnimationEndMessage(), IDClient);
        logic.received(new AnimationEndMessage(), IDHost);

        //tests that the server is in the animation-state
        assertEquals(logic.getCurrentState(), logic.getGameState());
        assertEquals(logic.getGameState().getCurrentState(), logic.getGameState().getAnimationState());

        logic.received(new AnimationEndMessage(), IDClient);
        logic.received(new AnimationEndMessage(), IDHost);

        assertEquals(logic.getCurrentState(), logic.getGameState());
        assertEquals(logic.getGameState().getCurrentState(), logic.getGameState().getTurnState());

        //tests if the host is the active player
        assertEquals(game.getActiveColor(), playerHost.getColor());

        //hosts plays no power-card
        logic.received(new NoPowerCardMessage(), IDHost);

        game.setDie(new Die(4));
        game.setDiscardPile(game.getDrawPile());
        game.setDrawPile(new ArrayList<>());

        //tests if the draw-pile is empty
        assertTrue(game.getDrawPile().isEmpty());

        //simulates a roll and the move of a piece for the host
        logic.received(new RequestDieMessage(), IDHost);
        logic.received(new AnimationEndMessage(), IDHost);
        logic.received(new RequestMoveMessage(game.getBoard().getInfield()[playerHost.getStartNodeIndex()].getOccupant()), IDHost);

        //tests if the server is in movePiece
        assertEquals(logic.getGameState().getCurrentState(), logic.getGameState().getTurnState());
        assertEquals(logic.getGameState().getTurnState().getCurrentState(), logic.getGameState().getTurnState().getMovePieceState());

        //tests if the draw-pile is not empty, the discard pile is empty and the host-player has drawn a card
        assertFalse(game.getDrawPile().isEmpty());
        assertTrue(game.getDiscardPile().isEmpty());
        assertEquals(2, playerHost.getHandCards().size());
    }
}
