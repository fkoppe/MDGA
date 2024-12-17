package pp.mdga.game;

import org.junit.Before;
import org.junit.Test;
import pp.mdga.game.card.PowerCard;
import pp.mdga.game.card.ShieldCard;
import pp.mdga.game.card.SwapCard;
import pp.mdga.game.card.TurboCard;
import pp.mdga.message.client.AnimationEndMessage;
import pp.mdga.message.client.RequestDieMessage;
import pp.mdga.message.client.RequestMoveMessage;
import pp.mdga.message.client.RequestPlayCardMessage;
import pp.mdga.message.client.SelectCardMessage;
import pp.mdga.message.client.SelectedPiecesMessage;
import pp.mdga.message.server.ServerMessage;
import pp.mdga.server.ServerGameLogic;
import pp.mdga.server.ServerSender;
import pp.mdga.server.automaton.GameState;
import pp.mdga.server.automaton.game.TurnState;
import pp.mdga.server.automaton.game.turn.ChoosePieceState;
import pp.mdga.server.automaton.game.turn.PlayPowerCardState;
import pp.mdga.server.automaton.game.turn.PowerCardState;
import pp.mdga.server.automaton.game.turn.RollDiceState;
import pp.mdga.server.automaton.game.turn.choosepiece.SelectPieceState;
import pp.mdga.server.automaton.game.turn.choosepiece.StartPieceState;
import pp.mdga.server.automaton.game.turn.choosepiece.WaitingPieceState;
import pp.mdga.server.automaton.game.turn.rolldice.FirstRollState;
import pp.mdga.visitor.Visitor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * this test-class tests the testcases T035-T058
 */
public class PieceTest {

    //declare game here
    private Game game;

    //declare server here
    private ServerGameLogic serverGameLogic;

    //declare player-Client here
    private Player playerClient;
    private String nameClient = "Client";
    private Color clientColor;

    //declare pieces of client here
    private Piece pieceClient0;
    private Piece pieceClient1;
    private Piece pieceClient2;
    private Piece pieceClient3;

    //declare player-host here
    private Player playerHost;
    private String nameHost = "Host";
    private Color hostColor;

    //declare pieces of host here
    private Piece pieceHost0;
    private Piece pieceHost1;
    private Piece pieceHost2;
    private Piece pieceHost3;

    //declare cyberPlayer here
    private String nameCyber;
    private Player playerCyber;
    private Color cyberColor;

    //declare cyberPieces
    private Piece pieceCyber0;
    private Piece pieceCyber1;
    private Piece pieceCyber2;
    private Piece pieceCyber3;

    //ID's for player
    private int IDHost;
    private int IDClient;
    private int IDCyber;

    //declare states
    GameState gameState;
    TurnState turnState;
    RollDiceState rollDiceState;
    FirstRollState firstRollState;
    ChoosePieceState choosePieceState;
    SelectPieceState selectPieceState;
    PowerCardState powerCardState;
    PlayPowerCardState playPowerCardState;
    WaitingPieceState waitingPieceState;
    StartPieceState startPieceState;

    //declare dies
    private Die die1;
    private Die die2;
    private Die die3;
    private Die die4;
    private Die die5;
    private Die die6;

    //declare PowerCards here
    PowerCard swapCard = new PowerCard() {
        @Override
        public void accept(Visitor visitor) {

        }
    };

    PowerCard shieldCard = new PowerCard() {
        @Override
        public void accept(Visitor visitor) {

        }
    };


    //declare messages here

    @Before
    public void Setup() {
        //initialize game here
        game = new Game();

        //initialize server here
        serverGameLogic = new ServerGameLogic(new ServerSender() {
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

        IDHost = 1;
        IDClient = 2;
        IDCyber = 3;


        //declare player-Client here
        playerClient = new Player(nameClient);
        clientColor = Color.ARMY;
        playerClient.setColor(clientColor);
        playerClient.initialize();
        game.addPlayer(IDClient, playerClient);

        //declare player-host here
        playerHost = new Player(nameHost);
        hostColor = Color.NAVY;
        playerHost.setColor(hostColor);
        playerHost.initialize();
        game.addPlayer(IDHost, playerHost);

        //declare playerCyber here
        nameCyber = "Cyber";
        playerCyber = new Player(nameCyber);
        cyberColor = Color.CYBER;
        playerCyber.setColor(cyberColor);
        playerCyber.initialize();
        game.addPlayer(IDCyber, playerCyber);

        //initialize the playerData
        for(Map.Entry<Integer, Player> entry : game.getPlayers().entrySet()){
            game.addPlayer(entry.getKey(), entry.getValue());
        }


        //declare pieces of client here
        pieceClient0 = game.getPlayerByColor(clientColor).getWaitingArea()[0];
        pieceClient1 = game.getPlayerByColor(clientColor).getWaitingArea()[1];
        pieceClient2 = game.getPlayerByColor(clientColor).getWaitingArea()[2];
        pieceClient3 = game.getPlayerByColor(clientColor).getWaitingArea()[3];

        //clear waiting-area of Client
        for (int i = 0; i < 4; i++) {
            game.getPlayerByColor(clientColor).getWaitingArea()[i] = null;
        }


        //declare pieces of host here
        pieceHost0 = game.getPlayerByColor(hostColor).getWaitingArea()[0];
        pieceHost1 = game.getPlayerByColor(hostColor).getWaitingArea()[1];
        pieceHost2 = game.getPlayerByColor(hostColor).getWaitingArea()[2];
        pieceHost3 = game.getPlayerByColor(hostColor).getWaitingArea()[3];

        //clear waiting-area of Client
        for (int i = 0; i < 4; i++) {
            game.getPlayerByColor(hostColor).getWaitingArea()[i] = null;
        }

        //declare pieces of cyber here
        pieceCyber0 = game.getPlayerByColor(cyberColor).getWaitingArea()[0];
        pieceCyber1 = game.getPlayerByColor(cyberColor).getWaitingArea()[1];
        pieceCyber2 = game.getPlayerByColor(cyberColor).getWaitingArea()[2];
        pieceCyber3 = game.getPlayerByColor(cyberColor).getWaitingArea()[3];

        //clear waiting-area of cyber
        for (int i = 0; i < 4; i++) {
            game.getPlayerByColor(cyberColor).getWaitingArea()[i] = null;
        }
        PieceState active = PieceState.ACTIVE;
        PieceState home = PieceState.HOME;
        //set the Client-pieces here
        game.getBoard().setPieceOnBoard(25, pieceClient0); //for UC 02, 03.01, 14,4
        pieceClient0.setState(active);
        game.getPlayerByColor(clientColor).setPieceInHome(1, pieceClient1);//set piece in Home at 2 slot for UC 18,12,13,11
        pieceClient1.setState(home);
        game.getBoard().setPieceOnBoard(19, pieceClient2); //for UC 13, 15
        pieceClient2.setState(active);

        //set the host-pieces here
        game.getBoard().setPieceOnBoard(28, pieceHost0); //for UC 02,14 ,15, 03.01,4
        pieceHost0.setState(active);
        game.getBoard().setPieceOnBoard(18, pieceHost1); //for UC 1, 10, 17, 16
        pieceHost1.setState(active);
        game.getPlayerByColor(hostColor).addWaitingPiece(pieceClient2);//set in waitingArea fur uc 5
        game.getBoard().setPieceOnBoard(0, pieceHost3); //for uc 9
        pieceHost3.setState(active);

        //set the pieces of cyber

        game.getBoard().setPieceOnBoard(10,pieceCyber0); // for UC 6,7,8
        pieceCyber0.setState(active);
        game.getBoard().setPieceOnBoard(12,pieceCyber1); //
        pieceCyber1.setState(active);
        game.getPlayerByColor(cyberColor).addWaitingPiece(pieceClient3); //for uc 7

        //initializes the states
        gameState= serverGameLogic.getGameState();
        turnState= gameState.getTurnState();
        rollDiceState= turnState.getRollDiceState();
        firstRollState= rollDiceState.getFirstRollState();
        choosePieceState= turnState.getChoosePieceState();
        selectPieceState= choosePieceState.getSelectPieceState();
        powerCardState = turnState.getPowerCardState();
        playPowerCardState = turnState.getPlayPowerCardState();
        waitingPieceState =serverGameLogic.getGameState().getTurnState().getChoosePieceState().getWaitingPieceState();
        startPieceState = serverGameLogic.getGameState().getTurnState().getChoosePieceState().getStartPieceState();

        //initialize dies
        die1 = new Die(1);
        die2= new Die(2);
        die3 = new Die(3);
        die4 = new Die(4);
        die5 = new Die(5);
        die6 = new Die(6);

    }

    /**
     * Tests the logic for when a piece moves.
     * <p>
     * Use Case UC-Piece-01: Ensure that a piece moves in the right direction
     * </p>
     */
    @Test (expected = RuntimeException.class)
    public void testMove() {
        //sets the active Player to host
        game.setActiveColor(hostColor);

        //sends the server in firstRoll
        serverGameLogic.setCurrentState(gameState);
        gameState.setCurrentState(turnState);
        turnState.setCurrentState(rollDiceState);
        rollDiceState.setCurrentState(firstRollState);

        //tests if the server is in firstRoll
        assertEquals(serverGameLogic.getCurrentState(),gameState);
        assertEquals(gameState.getCurrentState(), turnState);
        assertEquals(turnState.getCurrentState(),rollDiceState);
        assertEquals(rollDiceState.getCurrentState(),firstRollState);

        //sets the die-class, to roll 4's
        game.setDie(die4);

        //sends the request-die-message
        serverGameLogic.received(new RequestDieMessage(),IDHost);
        serverGameLogic.received(new AnimationEndMessage(),IDHost);

        //tests if the server is in selectPieceState
        assertTrue(game.getBoard().getInfield()[12].isOccupied());
        assertEquals(serverGameLogic.getCurrentState(),gameState);
        assertEquals(gameState.getCurrentState(), turnState);
        assertEquals(turnState.getCurrentState(),choosePieceState);
        assertEquals(choosePieceState.getCurrentState(),selectPieceState);

        //send wrong message
        serverGameLogic.received(new RequestMoveMessage(pieceCyber1),IDHost);

        //tests if there is no change
        assertTrue(game.getBoard().getInfield()[12].isOccupied());
        assertEquals(serverGameLogic.getCurrentState(),gameState);
        assertEquals(gameState.getCurrentState(), turnState);
        assertEquals(turnState.getCurrentState(),choosePieceState);
        assertEquals(choosePieceState.getCurrentState(),selectPieceState);

        //sends the move-message
        serverGameLogic.received(new RequestMoveMessage(pieceHost2),IDHost);

        //tests if the piece has moved in the right direction
        assertTrue(game.getBoard().getInfield()[4].isOccupied());
        assertEquals(game.getBoard().getInfield()[4].getOccupant(),pieceHost3);
    }

    /**
     * Tests the logic for when a piece can't move.
     * <p>
     * Use Case UC-Piece-02: Ensure that a piece cannot move if blocked by others.
     * </p>
     */
    @Test (expected = RuntimeException.class)
    public void testCantMove() {
        //set active player to army
        game.setActiveColor(clientColor);

        //sends the server in firstRoll
        serverGameLogic.setCurrentState(gameState);
        gameState.setCurrentState(turnState);
        turnState.setCurrentState(rollDiceState);
        rollDiceState.setCurrentState(firstRollState);

        //tests if the server is in firstRoll
        assertEquals(serverGameLogic.getCurrentState(),gameState);
        assertEquals(gameState.getCurrentState(), turnState);
        assertEquals(turnState.getCurrentState(),rollDiceState);
        assertEquals(rollDiceState.getCurrentState(),firstRollState);

        //set die-class
        game.setDie(die2);

        //send request Die-message
        serverGameLogic.received(new RequestDieMessage(),IDClient);
        serverGameLogic.received(new AnimationEndMessage(),IDClient);

        //send requestMove-Message
        serverGameLogic.received(new RequestMoveMessage(pieceClient2),IDClient);

        //tests if the hostPiece2 is still at idx 19 and the server is still in selectable pieces
        assertTrue(game.getBoard().getInfield()[19].isOccupied());
        assertEquals(game.getBoard().getInfield()[19].getOccupant(),pieceClient2);
        assertEquals(serverGameLogic.getCurrentState(),gameState);
        assertEquals(gameState.getCurrentState(), turnState);
        assertEquals(turnState.getCurrentState(),choosePieceState);
        assertEquals(choosePieceState.getCurrentState(),selectPieceState);
    }

    /**
     * Tests the logic for when no possible moves are available.
     * <p>
     * Use Case UC-Piece-03: Check game behavior when a player has no valid moves.
     * </p>
     */
    @Test
    public void testNoPossibleMove() {
        // TODO: Implement test logic for when no possible moves are available
    }

    /**
     * Tests the logic for throwing a piece off the board.
     * <p>
     * Use Case UC-Piece-03: Verify conditions under which a piece can be thrown off.
     * </p>
     */
    @Test
    public void testThrow() {

        //set active player to host
        game.setActiveColor(hostColor);
        playerHost.setStartNodeIndex(10);

        //sends the server in firstRoll
        serverGameLogic.setCurrentState(gameState);
        gameState.setCurrentState(turnState);
        turnState.setCurrentState(rollDiceState);
        rollDiceState.setCurrentState(firstRollState);

        //tests if the server is in firstRoll
        assertEquals(serverGameLogic.getCurrentState(),gameState);
        assertEquals(gameState.getCurrentState(), turnState);
        assertEquals(turnState.getCurrentState(),rollDiceState);
        assertEquals(rollDiceState.getCurrentState(),firstRollState);

        //set Dice to 1
        game.setDie(die1);

        //send requestDice
        serverGameLogic.received(new RequestDieMessage(),IDHost);
        serverGameLogic.received(new AnimationEndMessage(),IDHost);

        //send requestMove-message
        serverGameLogic.received(new RequestMoveMessage(pieceHost1),IDHost);

        //tests if the idx is unoccupied
        assertFalse(game.getBoard().getInfield()[18].isOccupied());

        //tests if the idx 20 is occupied
        assertTrue(game.getBoard().getInfield()[19].isOccupied());

        //tests if the piece on idx 20 is pieceHost1
        assertEquals(game.getBoard().getInfield()[19].getOccupant(),pieceHost1);
    }

    /**
     * Tests the logic for when a piece gets thrown.
     * <p>
     * Use Case UC-Piece-04: Confirm that a thrown piece is removed from play.
     * </p>
     */
    @Test
    public void testGetThrown() {
        //set active player to host
        game.setActiveColor(hostColor);

        //sends the server in firstRoll
        serverGameLogic.setCurrentState(gameState);
        gameState.setCurrentState(turnState);
        turnState.setCurrentState(rollDiceState);
        rollDiceState.setCurrentState(firstRollState);

        //tests if the server is in firstRoll
        assertEquals(serverGameLogic.getCurrentState(),gameState);
        assertEquals(gameState.getCurrentState(), turnState);
        assertEquals(turnState.getCurrentState(),rollDiceState);
        assertEquals(rollDiceState.getCurrentState(),firstRollState);

        //set Dice to 1
        game.setDie(die1);

        //send requestDice
        serverGameLogic.received(new RequestDieMessage(),IDHost);
        serverGameLogic.received(new AnimationEndMessage(),IDHost);

        //send requestMove-message
        serverGameLogic.received(new RequestMoveMessage(pieceHost1),IDHost);

        //tests if pieceClient2 is waitingArea
        Piece[] wait = game.getPlayerByColor(clientColor).getWaitingArea();
        assertTrue(wait[0]==pieceClient2 ||wait[1]==pieceClient2 ||wait[2]==pieceClient2 ||wait[3]==pieceClient2 );
    }

    /**
     * Tests the logic for a piece leaving the waiting area.
     * <p>
     * Use Case UC-Piece-05: Ensure a piece can transition from waiting to active play.
     * </p>
     */
    @Test
    public void testLeaveWaitingArea() {

        //set active player to host
        game.setActiveColor(hostColor);
        playerHost.setStartNodeIndex(20);

        //sends the server in firstRoll
        serverGameLogic.setCurrentState(gameState);
        gameState.setCurrentState(turnState);
        turnState.setCurrentState(rollDiceState);
        rollDiceState.setCurrentState(firstRollState);

        //tests if the server is in firstRoll
        assertEquals(serverGameLogic.getCurrentState(),gameState);
        assertEquals(gameState.getCurrentState(), turnState);
        assertEquals(turnState.getCurrentState(),rollDiceState);
        assertEquals(rollDiceState.getCurrentState(),firstRollState);

        //set Dice to 6
        game.setDie(die6);

        //send requestDice
        serverGameLogic.received(new RequestDieMessage(),IDHost);
        serverGameLogic.received(new AnimationEndMessage(),IDHost);

        //tests if the sever is in selectPiece
        assertEquals(serverGameLogic.getCurrentState(),gameState);
        assertEquals(gameState.getCurrentState(), turnState);
        assertEquals(turnState.getCurrentState(),choosePieceState);
        waitingPieceState.setPiece(pieceHost2);
        assertEquals(choosePieceState.getCurrentState(),waitingPieceState);

        //send requestMove-message
        serverGameLogic.received(new RequestMoveMessage(pieceHost2),IDHost);

        //tests if the waitingArea does not include the piece anymore
        assertFalse(Arrays.stream(game.getPlayerByColor(hostColor).getWaitingArea()).toList().contains(pieceHost2));
        //tests if the pieceHost3 is at idx 30
        assertTrue(game.getBoard().getInfield()[20].isOccupied());
        assertEquals(game.getBoard().getInfield()[20].getOccupant(),pieceHost2);
    }

    /**
     * Tests the logic for a piece that must leave the starting field.
     * <p>
     * Use Case UC-Piece-06: Verify conditions requiring a piece to leave its starting position.
     * </p>
     */
    @Test
    public void testMustLeaveStartingField() {
        //sets the activePlayer to cyber
        game.setActiveColor(cyberColor);

        //sends the server in firstRoll
        serverGameLogic.setCurrentState(gameState);
        gameState.setCurrentState(turnState);
        turnState.setCurrentState(rollDiceState);
        rollDiceState.setCurrentState(firstRollState);

        //tests if the server is in firstRoll
        assertEquals(serverGameLogic.getCurrentState(),gameState);
        assertEquals(gameState.getCurrentState(), turnState);
        assertEquals(turnState.getCurrentState(),rollDiceState);
        assertEquals(rollDiceState.getCurrentState(),firstRollState);

        //set Dice to 4
        game.setDie(die4);

        //send requestDiceMessage
        serverGameLogic.received(new RequestDieMessage(),IDCyber);
        serverGameLogic.received(new AnimationEndMessage(),IDCyber);
        System.out.println(game.getBoard().getInfieldIndexOfPiece(pieceCyber0));

        //tests if the sever is in startPiece
        assertEquals(serverGameLogic.getCurrentState(),gameState);
        assertEquals(gameState.getCurrentState(), turnState);
        assertEquals(turnState.getCurrentState(),choosePieceState);
        assertEquals(choosePieceState.getCurrentState(),startPieceState);

        //send requestMoveMessage
        serverGameLogic.received(new RequestMoveMessage(pieceCyber0),IDCyber);

        //tests if the cyberPiece2 has moved
        assertTrue(game.getBoard().getInfield()[14].isOccupied());
        assertEquals(game.getBoard().getInfield()[14].getOccupant(),pieceCyber0);
        System.out.println(game.getBoard().getInfieldIndexOfPiece(pieceCyber0));
    }

    /**
     * Tests the logic for when a piece doesn't have to leave the starting field.
     * <p>
     * Use Case UC-Piece-07: Check scenarios where a piece can stay on its starting field.
     * </p>
     */
    @Test
    public void testDontHaveToLeaveStartingField() {
        //set the active player to cyberPlayer
        game.setActiveColor(cyberColor);

        //sends the server in firstRoll
        serverGameLogic.setCurrentState(gameState);
        gameState.setCurrentState(turnState);
        turnState.setCurrentState(rollDiceState);
        rollDiceState.setCurrentState(firstRollState);

        //tests if the server is in firstRoll
        assertEquals(serverGameLogic.getCurrentState(),gameState);
        assertEquals(gameState.getCurrentState(), turnState);
        assertEquals(turnState.getCurrentState(),rollDiceState);
        assertEquals(rollDiceState.getCurrentState(),firstRollState);

        //remove piece from cyberPlayer
        game.getPlayerByColor(cyberColor).removeWaitingPiece(game.getPlayerByColor(cyberColor).getWaitingPiece());

        //set dice to 3
        game.setDie(die3);

        //tests if the server is in selectPieceState
        assertEquals(serverGameLogic.getCurrentState(),gameState);
        assertEquals(gameState.getCurrentState(), turnState);
        assertEquals(turnState.getCurrentState(),rollDiceState);
        assertEquals(rollDiceState.getCurrentState(),firstRollState);

        //send requestDice-Message
        serverGameLogic.received(new RequestDieMessage(),IDCyber);
        serverGameLogic.received(new AnimationEndMessage(),IDCyber);

        //send requestMoveMessage for CyberPiece1
        serverGameLogic.received(new RequestMoveMessage(pieceCyber1),IDCyber);

        //tests if the cyberPiece1 is moved
        assertTrue(game.getBoard().getInfield()[15].isOccupied());
        assertEquals(game.getBoard().getInfield()[15].getOccupant(), pieceCyber1);

    }

    /**
     * Tests the logic for when a piece can't leave the starting field.
     * <p>
     * Use Case UC-Piece-08: Verify conditions preventing a piece from leaving its starting field.
     * </p>
     */
    @Test (expected = RuntimeException.class)
    public void testCantLeaveStartingField() {
        //sets the activePlayer to cyber
        game.setActiveColor(cyberColor);

        //sends the server in firstRoll
        serverGameLogic.setCurrentState(gameState);
        gameState.setCurrentState(turnState);
        turnState.setCurrentState(rollDiceState);
        rollDiceState.setCurrentState(firstRollState);

        //tests if the server is in firstRoll
        assertEquals(serverGameLogic.getCurrentState(),gameState);
        assertEquals(gameState.getCurrentState(), turnState);
        assertEquals(turnState.getCurrentState(),rollDiceState);
        assertEquals(rollDiceState.getCurrentState(),firstRollState);

        //set Dice to two
        game.setDie(die2);

        //send requestDiceMessage
        serverGameLogic.received(new RequestDieMessage(),IDCyber);
        serverGameLogic.received(new AnimationEndMessage(),IDCyber);

        //tests if the sever is in selectPiece
        assertEquals(serverGameLogic.getCurrentState(),gameState);
        assertEquals(gameState.getCurrentState(), turnState);
        assertEquals(turnState.getCurrentState(),choosePieceState);
        assertEquals(choosePieceState.getCurrentState(),selectPieceState);
        //selectPieceState.setMoveablePieces(new ArrayList<>(List.of(pieceCyber0)));

        //send requestMoveMessage
        serverGameLogic.received(new RequestMoveMessage(pieceCyber0),IDCyber);

        //tests if the sever is in selectPiece
        assertEquals(serverGameLogic.getCurrentState(),gameState);
        assertEquals(gameState.getCurrentState(), turnState);
        assertEquals(turnState.getCurrentState(),choosePieceState);
        assertEquals(choosePieceState.getCurrentState(),selectPieceState);

        //tests if the cyberPiece0 is still at its idx
        assertTrue(game.getBoard().getInfield()[10].isOccupied());
        assertEquals(game.getBoard().getInfield()[10].getOccupant(),pieceHost0);

        //sets the dice to 2
        game.setDie(die2);

        //send the requestMove-Message
        serverGameLogic.received(new RequestMoveMessage(pieceCyber1),IDCyber);

        //tests if the pieceCyber
        assertTrue(game.getBoard().getInfield()[14].isOccupied());
        assertEquals(game.getBoard().getInfield()[14].getOccupant(),pieceHost1);
    }

    /**
     * Tests the logic for when a piece reaches a bonus field.
     * <p>
     * Use Case UC-Piece-09: Verify the benefits applied when landing on a bonus field.
     * </p>
     */
    @Test
    public void testReachBonusField() {
        //sets the active Player to Host
        game.setActiveColor(hostColor);

        //sends the server in firstRoll
        serverGameLogic.setCurrentState(gameState);
        gameState.setCurrentState(turnState);
        turnState.setCurrentState(rollDiceState);
        rollDiceState.setCurrentState(firstRollState);

        //tests if the server is in firstRoll
        assertEquals(serverGameLogic.getCurrentState(),gameState);
        assertEquals(gameState.getCurrentState(), turnState);
        assertEquals(turnState.getCurrentState(),rollDiceState);
        assertEquals(rollDiceState.getCurrentState(),firstRollState);

        //gets the top bonusCard
        PowerCard card = game.getDrawPile().get(0);
        System.out.println(card);
        System.out.println(game.getDrawPile().get(1));

        //sets the dice-seed to 4
        game.setDie(die4);

        //sends the requestDice-Message
        serverGameLogic.received(new RequestDieMessage(),IDHost);
        serverGameLogic.received(new AnimationEndMessage(),IDHost);

        //requestMove of hostPiece3
        serverGameLogic.received(new RequestMoveMessage(pieceHost3),IDHost);

        //tests the position of hostPiece3
        assertTrue(game.getBoard().getInfield()[4].isOccupied());
        assertEquals(game.getBoard().getInfield()[4].getOccupant(),pieceHost3);

        //tests if the player has received a bonusCard
        assertTrue(game.getPlayers().get(IDHost).getHandCards().stream().toList().contains(card));
        assertNotEquals(card, game.getDrawPile().get(0));
    }

    /**
     * Tests the logic for when there are no power cards available.
     * <p>
     * Use Case UC-Piece-09: Ensure the game handles the situation with no available power cards.
     * </p>
     */
    @Test
    public void testNoPowerCards() {
        //sets the active Player to Host
        game.setActiveColor(hostColor);

        //sends the server in firstRoll
        serverGameLogic.setCurrentState(gameState);
        gameState.setCurrentState(turnState);
        turnState.setCurrentState(rollDiceState);
        rollDiceState.setCurrentState(firstRollState);

        //tests if the server is in firstRoll
        assertEquals(serverGameLogic.getCurrentState(),gameState);
        assertEquals(gameState.getCurrentState(), turnState);
        assertEquals(turnState.getCurrentState(),rollDiceState);
        assertEquals(rollDiceState.getCurrentState(),firstRollState);

        //sets the dice-seed to 4
        game.setDie(die4);

        //sets the pile empty
        game.setDrawPile(new ArrayList<>());
        assertTrue(game.getDrawPile().isEmpty());

        //sets the discard pile empty
        game.setDiscardPile(new ArrayList<>());
        assertTrue(game.getDiscardPile().isEmpty());

        //sends the requestDice-Message
        serverGameLogic.received(new RequestDieMessage(),IDHost);
        serverGameLogic.received(new AnimationEndMessage(),IDHost);

        // requestMOve of hostPiece3
        serverGameLogic.received(new RequestMoveMessage(pieceHost3),IDHost);

        assertTrue(game.getBoard().getInfield()[4].isOccupied());
        assertEquals(game.getBoard().getInfield()[4].getOccupant(),pieceHost3);

        //tests if the player has received a bonusCard
        assertTrue(game.getDrawPile().isEmpty());
        assertTrue(game.getPlayers().get(IDHost).getHandCards().stream().toList().isEmpty());
    }

    /**
     * Tests the logic for shuffling the pile of power cards.
     * <p>
     * Use Case UC-Piece-09: Confirm that the power card deck can be shuffled correctly.
     * </p>
     */
    @Test
    public void testShufflePile() {

        //sets the active Player to Host
        game.setActiveColor(hostColor);

        //sends the server in firstRoll
        serverGameLogic.setCurrentState(gameState);
        gameState.setCurrentState(turnState);
        turnState.setCurrentState(rollDiceState);
        rollDiceState.setCurrentState(firstRollState);

        //tests if the server is in firstRoll
        assertEquals(serverGameLogic.getCurrentState(),gameState);
        assertEquals(gameState.getCurrentState(), turnState);
        assertEquals(turnState.getCurrentState(),rollDiceState);
        assertEquals(rollDiceState.getCurrentState(),firstRollState);

        //sets the dice-seed to 4
        game.setDie(die4);

        //sets the discard pile empty
        game.setDiscardPile(new ArrayList<>(List.of(new TurboCard(),new TurboCard())));
        assertFalse(game.getDiscardPile().isEmpty());

        //sets the pile empty
        game.setDrawPile(new ArrayList<>());
        assertTrue(game.getDrawPile().isEmpty());

        //sends the requestDice-Message
        serverGameLogic.received(new RequestDieMessage(),IDHost);
        serverGameLogic.received(new AnimationEndMessage(),IDHost);

        // requestMOve of hostPiece3
        serverGameLogic.received(new RequestMoveMessage(pieceHost3),IDHost);

        assertTrue(game.getBoard().getInfield()[4].isOccupied());
        assertEquals(game.getBoard().getInfield()[4].getOccupant(),pieceHost3);
        System.out.println(game.getBoard().getInfield()[4].isBonus());

        //tests if the player has received a bonusCard
        assertFalse(game.getDrawPile().isEmpty());
        assertTrue(game.getDiscardPile().isEmpty());

        //tests if the drawPile is not Empty
        assertFalse(game.getDrawPile().isEmpty());
        assertTrue(game.getDiscardPile().isEmpty());

    }

    /**
     * Tests the logic for entering the house area.
     * <p>
     * Use Case UC-Piece-10: Verify that a piece can enter the house area according to the rules.
     * </p>
     */
    @Test
    public void testEnterHouse() {
        pieceClient2.setState(PieceState.ACTIVE);
        playerClient.setStartNodeIndex(20);
        //set activePlayer to client
        game.setActiveColor(clientColor);

        //sends the server in firstRoll
        serverGameLogic.setCurrentState(gameState);
        gameState.setCurrentState(turnState);
        turnState.setCurrentState(rollDiceState);
        rollDiceState.setCurrentState(firstRollState);

        //tests if the server is in firstRoll
        assertEquals(serverGameLogic.getCurrentState(),gameState);
        assertEquals(gameState.getCurrentState(), turnState);
        assertEquals(turnState.getCurrentState(),rollDiceState);
        assertEquals(rollDiceState.getCurrentState(),firstRollState);

        //set the die in Game to 1
        game.setDie(die1);

        //sends the request-Dice-message
        serverGameLogic.received(new RequestDieMessage(),IDClient);
        serverGameLogic.received(new AnimationEndMessage(),IDClient);

        //tests if the server is in firstRoll
        assertEquals(serverGameLogic.getCurrentState(),gameState);
        assertEquals(gameState.getCurrentState(), turnState);
        assertEquals(turnState.getCurrentState(),choosePieceState);
        assertEquals(choosePieceState.getCurrentState(),selectPieceState);

        //sends the requestMove-message for pieceClient1
        serverGameLogic.received(new RequestMoveMessage(pieceClient2),IDClient);

        //tests, if the piece is in the first slot of the home
        assertFalse(game.getBoard().getInfield()[19].isOccupied());
        assertTrue(game.getPlayerByColor(clientColor).getHomeNodes()[0].isOccupied());
        assertEquals(game.getPlayerByColor(clientColor).getHomeNodes()[0].getOccupant(), pieceClient2);
    }

    /**
     * Tests the logic to ensure a piece can only enter its own house.
     * <p>
     * Use Case UC-Piece-10: Check that a piece can only enter its designated house.
     * </p>
     */
    @Test
    public void testOnlyEnterOwnHouse() {
        //set activePlayer to host
        game.setActiveColor(hostColor);
        System.out.println(playerHost.getStartNodeIndex());
        game.getBoard().setPieceOnBoard(28,pieceHost1);
        game.getBoard().getInfield()[18].clearOccupant();

        //sends the server in firstRoll
        serverGameLogic.setCurrentState(gameState);
        gameState.setCurrentState(turnState);
        turnState.setCurrentState(rollDiceState);
        rollDiceState.setCurrentState(firstRollState);
        System.out.println(game.getBoard().getInfieldIndexOfPiece(pieceHost1));

        //tests if the server is in firstRoll
        assertEquals(serverGameLogic.getCurrentState(),gameState);
        assertEquals(gameState.getCurrentState(), turnState);
        assertEquals(turnState.getCurrentState(),rollDiceState);
        assertEquals(rollDiceState.getCurrentState(),firstRollState);

        //set the die in Game to 3
        game.setDie(die3);
        System.out.println(game.getBoard().getInfieldIndexOfPiece(pieceHost1));
        //sends the request-Dice-message
        serverGameLogic.received(new RequestDieMessage(),IDHost);
        serverGameLogic.received(new AnimationEndMessage(),IDHost);

        //sends the requestMove-message for pieceClient1
        System.out.println(game.getBoard().getInfieldIndexOfPiece(pieceHost1));
        serverGameLogic.received(new RequestMoveMessage(pieceHost1),IDHost);
        System.out.println(game.getBoard().getInfieldIndexOfPiece(pieceHost1));

        //tests if hostPiece1 is at idx 20
        assertTrue(game.getBoard().getInfield()[31].isOccupied());
        assertEquals(game.getBoard().getInfield()[31].getOccupant(),pieceHost1);
    }

    /**
     * Tests the logic for activating a piece in the home area.
     * <p>
     * Use Case UC-Piece-11: Confirm that a piece can be activated correctly in the home area.
     * </p>
     */
    @Test
    public void testActiveHomePiece() {
        //set activePlayer to client
        game.setActiveColor(clientColor);

        //sends the server in firstRoll
        serverGameLogic.setCurrentState(gameState);
        gameState.setCurrentState(turnState);
        turnState.setCurrentState(rollDiceState);
        rollDiceState.setCurrentState(firstRollState);

        //tests if the server is in firstRoll
        assertEquals(serverGameLogic.getCurrentState(),gameState);
        assertEquals(gameState.getCurrentState(), turnState);
        assertEquals(turnState.getCurrentState(),rollDiceState);
        assertEquals(rollDiceState.getCurrentState(),firstRollState);

        //set the die in Game to 2
        game.setDie(die2);

        //sends the request-Dice-message
        serverGameLogic.received(new RequestDieMessage(),IDClient);
        serverGameLogic.received(new AnimationEndMessage(),IDClient);

        //sends the requestMove-message for pieceClient1
        serverGameLogic.received(new RequestMoveMessage(pieceClient1),IDClient);


        //tests if clientPiece1 is in the home at idx 3
        assertTrue(game.getPlayerByColor(clientColor).getHomeNodes()[3].isOccupied());
        assertEquals(game.getPlayerByColor(clientColor).getHomeNodes()[3].getOccupant(), pieceClient1);
    }

    /**
     * Tests the logic to prevent jumping over another piece in the house.
     * <p>
     * Use Case UC-Piece-11: Ensure that a piece cannot jump over another piece in the house area.
     * </p>
     */
    @Test (expected = RuntimeException.class)
    public void testCantJumpOverFigureInHouse() {
        //set activePlayer to client
        game.setActiveColor(clientColor);

        pieceClient2.setState(PieceState.ACTIVE);
        pieceClient1.setState(PieceState.HOME);

        //sends the server in firstRoll
        serverGameLogic.setCurrentState(gameState);
        gameState.setCurrentState(turnState);
        turnState.setCurrentState(rollDiceState);
        rollDiceState.setCurrentState(firstRollState);

        //tests if the server is in firstRoll
        assertEquals(serverGameLogic.getCurrentState(),gameState);
        assertEquals(gameState.getCurrentState(), turnState);
        assertEquals(turnState.getCurrentState(),rollDiceState);
        assertEquals(rollDiceState.getCurrentState(),firstRollState);

        //set the die in Game to 4
        game.setDie(die4);

        //send requestDice-Message
        serverGameLogic.received(new RequestDieMessage(),IDClient);
        serverGameLogic.received(new AnimationEndMessage(),IDClient);

        assertEquals(serverGameLogic.getCurrentState(),gameState);
        assertEquals(gameState.getCurrentState(), turnState);
        assertEquals(turnState.getCurrentState(),choosePieceState);
        assertEquals(choosePieceState.getCurrentState(),selectPieceState);

        selectPieceState.setMoveablePieces(new ArrayList<>(List.of()));
        System.out.println(selectPieceState);

        System.out.println(game.getBoard().getInfieldIndexOfPiece(pieceClient2));
        //sends requestMoveMessage with clientPiece 02
        serverGameLogic.received(new RequestMoveMessage(pieceClient2),IDClient);
        System.out.println(game.getBoard().getInfieldIndexOfPiece(pieceClient2));

        //tests, if the clientPiece02 is at idx 19
        assertTrue(game.getBoard().getInfield()[19].isOccupied());
        assertEquals(game.getBoard().getInfield()[19].getOccupant(),pieceClient2);
    }

    /**
     * Tests the logic for when an active home piece is blocked.
     * <p>
     * Use Case UC-Piece-12: Verify that an active piece cannot move if blocked by another piece.
     * </p>
     */
    @Test
    public void testActiveHomePieceBlocked() {

        game.setActiveColor(clientColor);
        turnState.setPlayer(playerClient);
        //sends the server in firstRoll
        serverGameLogic.setCurrentState(gameState);
        gameState.setCurrentState(turnState);
        turnState.setCurrentState(rollDiceState);
        rollDiceState.setCurrentState(firstRollState);

        //tests if the server is in firstRoll
        assertEquals(serverGameLogic.getCurrentState(),gameState);
        assertEquals(gameState.getCurrentState(), turnState);
        assertEquals(turnState.getCurrentState(),rollDiceState);
        assertEquals(rollDiceState.getCurrentState(),firstRollState);

        //set active player to client
        game.setActiveColor(clientColor);

        //set Dice to 3
        game.setDie(die3);

        //sends the requestDice-message
        serverGameLogic.received(new RequestDieMessage(),IDClient);

        //sends the request MoveMessage
        serverGameLogic.received(new RequestMoveMessage(pieceClient1),IDClient);

        //tests if the piece is still at idx 2 in home
        assertTrue(game.getPlayerByColor(clientColor).getHomeNodes()[1].isOccupied());
        assertEquals(game.getPlayerByColor(clientColor).getHomeNodes()[1].getOccupant(), pieceClient1);
    }

    /**
     * Tests the logic for a piece on the starting field with a shield.
     * <p>
     * Use Case UC-Piece-13: Ensure that a piece with a shield behaves correctly on the starting field.
     * </p>
     */
    @Test
    public void testOnStartingFieldWithShield() {
        //sets the color to host
        game.setActiveColor(hostColor);
        game.getBoard().setPieceOnBoard(28,pieceHost1);
        game.getBoard().setPieceOnBoard(18,null);

        //sets the dice to 2
        game.setDie(die2);

        //sets the shield of hostPiece1 true
        pieceHost1.setShield(ShieldState.ACTIVE);

        //sends the server in firstRoll
        serverGameLogic.setCurrentState(gameState);
        gameState.setCurrentState(turnState);
        turnState.setCurrentState(rollDiceState);
        rollDiceState.setCurrentState(firstRollState);

        //tests if the server is in firstRoll
        assertEquals(serverGameLogic.getCurrentState(),gameState);
        assertEquals(gameState.getCurrentState(), turnState);
        assertEquals(turnState.getCurrentState(),rollDiceState);
        assertEquals(rollDiceState.getCurrentState(),firstRollState);

        //sends the requestDice-message
        serverGameLogic.received(new RequestDieMessage(),IDHost);
        serverGameLogic.received(new AnimationEndMessage(),IDHost);

        pieceHost1.setShield(ShieldState.ACTIVE);
        //sends the moveMessage
        serverGameLogic.received(new RequestMoveMessage(pieceHost1),IDHost);

        //tests the position of the hostPiece1 and that shield is suppressed
        assertTrue(game.getBoard().getInfield()[30].isOccupied());
        assertEquals(game.getBoard().getInfield()[30].getOccupant(),pieceHost1);

        assertEquals(pieceHost1.getShield(),ShieldState.SUPPRESSED);
    }

    /**
     * Tests the logic for attempting to throw a figure with a shield.
     * <p>
     * Use Case UC-Piece-14: Verify that a piece with a shield cannot be thrown by opponents.
     * </p>
     */
    @Test
    public void testThrowFigureWithShield() {
        //set clientPiece2 the shield active
        pieceClient2.setShield(ShieldState.ACTIVE);

        //set host as active player
        game.setActiveColor(hostColor);

        //set die to 1
        game.setDie(die1);

        //sends the server in firstRoll
        serverGameLogic.setCurrentState(gameState);
        gameState.setCurrentState(turnState);
        turnState.setCurrentState(rollDiceState);
        rollDiceState.setCurrentState(firstRollState);

        //tests if the server is in firstRoll
        assertEquals(serverGameLogic.getCurrentState(),gameState);
        assertEquals(gameState.getCurrentState(), turnState);
        assertEquals(turnState.getCurrentState(),rollDiceState);
        assertEquals(rollDiceState.getCurrentState(),firstRollState);

        //send requestDice
        serverGameLogic.received(new RequestDieMessage(),IDHost);

        //send requestMove of hostPiece1
        serverGameLogic.received(new RequestMoveMessage(pieceHost1),IDHost);

        //tests if the clientPiece2 is still at idx 19 and hostPiece1 at idx 18 and clientPiece2 shield is still active
        assertTrue(game.getBoard().getInfield()[19].isOccupied());
        assertTrue(game.getBoard().getInfield()[18].isOccupied());

        assertEquals(game.getBoard().getInfield()[18].getOccupant(),pieceHost1);
        assertEquals(game.getBoard().getInfield()[19].getOccupant(),pieceClient2);

        assertEquals(pieceClient2.getShield(),ShieldState.ACTIVE);
    }

    /**
     * Tests the logic for using a swap power-up.
     * <p>
     * Use Case UC-Piece-15: Confirm that a player can use a swap power-up to exchange positions.
     * </p>
     */
    @Test
    public void testUseSwap() {
        //set activePlayer to Host
        game.setActiveColor(hostColor);
        PowerCard swap = new SwapCard();
        playerHost.addHandCard(swap);
        pieceClient0.setState(PieceState.ACTIVE);
        pieceHost0.setShield(ShieldState.ACTIVE);

        //send the server in choosePowerCard
        serverGameLogic.setCurrentState(gameState);
        serverGameLogic.getGameState().setCurrentState(turnState);
        serverGameLogic.getGameState().getTurnState().setCurrentState(powerCardState);
        powerCardState.setSelectedCard(swap);
        powerCardState.setCurrentState(powerCardState.getSwapCardState());

        //tests if the server is in selectPieces
        assertEquals(serverGameLogic.getCurrentState(),gameState);
        assertEquals(gameState.getCurrentState(), turnState);
        assertEquals(turnState.getCurrentState(),powerCardState);



        assertTrue(game.getBoard().getInfield()[25].isOccupied());
        assertEquals(game.getBoard().getInfield()[25].getOccupant(),pieceClient0);

        assertTrue(game.getBoard().getInfield()[28].isOccupied());
        assertEquals(game.getBoard().getInfield()[28].getOccupant(),pieceHost0);

        //sends the requestPlayCard
        serverGameLogic.received(new SelectCardMessage(swapCard),IDHost);


        //sends the selectedPiece-message
        serverGameLogic.received(new SelectedPiecesMessage(List.of(pieceHost0,pieceClient0)),IDHost);
        System.out.println(game.getBoard().getInfieldIndexOfPiece(pieceClient0));

        //tests if the piece at idx 25 is pieceHost0 and at idx 28 is pieceClient0
        assertTrue(game.getBoard().getInfield()[25].isOccupied());
        assertEquals(game.getBoard().getInfield()[25].getOccupant(),pieceHost0);

        assertTrue(game.getBoard().getInfield()[28].isOccupied());
        assertEquals(game.getBoard().getInfield()[28].getOccupant(),pieceClient0);


        //tests if the server is in playPowerCard
        assertEquals(serverGameLogic.getCurrentState(),gameState);
        assertEquals(gameState.getCurrentState(), turnState);
        assertEquals(turnState.getCurrentState(),playPowerCardState);
    }

    /**
     * Tests the logic for using a shield power-up.
     * <p>
     * Use Case UC-Piece-16: Ensure that a player can activate a shield power-up correctly.
     * </p>
     */
    @Test
    public void testUseShield() {
        //set activePlayer to Host
        PowerCard shield = new ShieldCard();
        game.setActiveColor(hostColor);
        playerHost.addHandCard(shield);

        //send the server in choosePowerCard
        serverGameLogic.setCurrentState(gameState);
        serverGameLogic.getGameState().setCurrentState(turnState);
        serverGameLogic.getGameState().getTurnState().setCurrentState(powerCardState);
        powerCardState.setCurrentState(powerCardState.getChoosePowerCardState());

        //tests if the server is in selectPieces
        assertEquals(serverGameLogic.getCurrentState(),gameState);
        assertEquals(gameState.getCurrentState(), turnState);
        assertEquals(turnState.getCurrentState(),powerCardState);
        assertEquals(powerCardState.getCurrentState(),powerCardState.getChoosePowerCardState());


        //sends the requestPlayCard
        serverGameLogic.received(new SelectCardMessage(shield),IDHost);
        powerCardState.setSelectedCard(shield);

        //tests if the server is in selectPieces
        assertEquals(serverGameLogic.getCurrentState(),gameState);
        assertEquals(gameState.getCurrentState(), turnState);
        assertEquals(turnState.getCurrentState(),powerCardState);
        assertEquals(powerCardState.getCurrentState(),powerCardState.getShieldCardState());

        //sends the selectedPiece-message
        serverGameLogic.received(new SelectedPiecesMessage(new ArrayList<>(List.of(pieceHost0))),IDHost);

        //tests if the piece at idx 28 has a shield
        assertEquals(pieceHost0.getShield(),ShieldState.ACTIVE);

        //tests if the server is in playPowerCard
        assertEquals(serverGameLogic.getCurrentState(),gameState);
        assertEquals(gameState.getCurrentState(), turnState);
        assertEquals(turnState.getCurrentState(),playPowerCardState);
    }

    /**
     * Tests the logic for when a piece loses its shield.
     * <p>
     * Use Case UC-Piece-17: Verify that the game correctly updates the status when a piece loses its shield.
     * </p>
     */
    @Test
    public void testLoseShield() {
        //sets activePlayer to client
        game.setActiveColor(clientColor);

        game.removePlayer(IDCyber);
        //set shieldState.ACTIVE im pieceHost1
        pieceHost1.setShield(ShieldState.ACTIVE);

        //set die to 5
        game.setDie(die5);

        //sends the server in selectPiece
        serverGameLogic.setCurrentState(gameState);
        serverGameLogic.getGameState().setCurrentState(turnState);
        serverGameLogic.getGameState().getTurnState().setCurrentState(rollDiceState);
        rollDiceState.setCurrentState(firstRollState);

        //tests if the server is in selectPieces
        assertEquals(serverGameLogic.getCurrentState(),gameState);
        assertEquals(gameState.getCurrentState(), turnState);
        assertEquals(turnState.getCurrentState(),rollDiceState);
        assertEquals(rollDiceState.getCurrentState(),firstRollState);

        //send requestDice-message
        serverGameLogic.received(new RequestDieMessage(),IDClient);
        serverGameLogic.received(new AnimationEndMessage(),IDClient);

        //send requestMove-message for clientPiece0
        serverGameLogic.received(new RequestMoveMessage(pieceClient0),IDClient);
        serverGameLogic.received(new AnimationEndMessage(),IDClient);
        serverGameLogic.received(new AnimationEndMessage(),IDHost);
        serverGameLogic.received(new AnimationEndMessage(),IDCyber);

        //tests if the server is in turn-state
        assertEquals(serverGameLogic.getCurrentState(),gameState);
        assertEquals(gameState.getCurrentState(), gameState.getTurnState());

        //send 3 animationEndMessage
        serverGameLogic.received(new AnimationEndMessage(),IDClient);
        serverGameLogic.received(new AnimationEndMessage(),IDHost);
        serverGameLogic.received(new AnimationEndMessage(),IDCyber);

        //test for new Player
        assertEquals(serverGameLogic.getGame().getActiveColor(),hostColor);

        //tests, if the shieldState is NONE
        assertEquals(pieceHost1.getShield(),ShieldState.NONE);
    }

    /**
     * Tests the logic for a piece that has finished the game.
     * <p>
     * Use Case UC-Piece-18: Confirm that the game recognizes a piece that has completed its journey.
     * </p>
     */
    @Test
    public void testFinishedPiece() {
        //sets the active color to client
        game.setActiveColor(clientColor);
        playerClient.setHandCards(new ArrayList<>());


        //sends the server in RollDicePiece
        serverGameLogic.setCurrentState(gameState);
        serverGameLogic.getGameState().setCurrentState(turnState);

        //tests if the server is in firstRoll-state
        assertEquals(serverGameLogic.getCurrentState(),gameState);
        assertEquals(gameState.getCurrentState(), turnState);
        assertEquals(turnState.getCurrentState(),rollDiceState);
        assertEquals(rollDiceState.getCurrentState(),firstRollState);

        //set the dice to 2
        game.setDie(die2);

        //sends the requestDie-Message
        serverGameLogic.received(new RequestDieMessage(),IDClient);
        serverGameLogic.received(new AnimationEndMessage(),IDClient);

        //tests if the server is in selectPieces
        assertEquals(serverGameLogic.getCurrentState(),gameState);
        assertEquals(gameState.getCurrentState(), turnState);
        assertEquals(turnState.getCurrentState(),choosePieceState);
        assertEquals(choosePieceState.getCurrentState(),selectPieceState);

        //sends the requestMove-Message
        serverGameLogic.received(new RequestMoveMessage(pieceClient1),IDClient);

        //tests if the Piece is in the final position and is marked as home-finished
        assertTrue(game.getPlayerByColor(clientColor).getHomeNodes()[3].isOccupied());
        assertEquals(game.getPlayerByColor(clientColor).getHomeNodes()[3].getOccupant(), pieceClient1);
        assertEquals(pieceClient1.getState(),PieceState.HOMEFINISHED);
    }
}
