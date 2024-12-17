package pp.mdga.server.serverState;

import org.junit.Before;
import org.junit.Test;
import pp.mdga.game.*;
import pp.mdga.game.card.PowerCard;
import pp.mdga.game.card.ShieldCard;
import pp.mdga.game.card.SwapCard;
import pp.mdga.game.card.TurboCard;
import pp.mdga.message.client.*;
import pp.mdga.message.server.ServerMessage;
import pp.mdga.server.ServerGameLogic;
import pp.mdga.server.ServerSender;
import pp.mdga.server.automaton.CeremonyState;
import pp.mdga.server.automaton.GameState;
import pp.mdga.server.automaton.InterruptState;
import pp.mdga.server.automaton.LobbyState;
import pp.mdga.server.automaton.game.AnimationState;
import pp.mdga.server.automaton.game.DetermineStartPlayerState;
import pp.mdga.server.automaton.game.TurnState;
import pp.mdga.server.automaton.game.turn.*;
import pp.mdga.server.automaton.game.turn.choosepiece.*;
import pp.mdga.server.automaton.game.turn.rolldice.*;
import pp.mdga.visitor.Visitor;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * this test-class tests the Testcases T132-T169
 */

public class ServerStateTest {

    //declare game here
    private Game game;

    //declare serverGameLogic here
    private ServerGameLogic serverGameLogic;

    //declare client-messages here
    private AnimationEndMessage animationEnd;
    private DeselectTSKMessage deselectTSK;
    private DisconnectedMessage disconnected;
    private ForceContinueGameMessage forceContinueGame;
    private JoinedLobbyMessage joinServer;
    private LeaveGameMessage leaveGame;
    private LobbyNotReadyMessage lobbyNotReady;
    private LobbyReadyMessage lobbyReady;
    private NoPowerCardMessage noPowerCard;
    private RequestBriefingMessage requestBriefing;
    private RequestDieMessage requestDie;
    private RequestMoveMessage requestMove;
    private RequestPlayCardMessage requestPlayCard;
    private SelectCardMessage selectCard;
    private SelectedPiecesMessage selectedPieces;
    private SelectTSKMessage selectTSK;
    private StartGameMessage startGame;

    //declare two players

    private Player playerHost;
    private int IDPlayerHost;
    private Color playerHostColor;

    private Player playerClient;
    private int IDPlayerClient;
    private Color playerClientColor;

    //declare a movable piece for client

    private Piece piece;
    private PieceState pieceState;

    private Piece hostPiece1;
    private Piece hostPiece2;
    private Piece hostPiece3;
    private Piece hostPiece4;

    //declare 3 pieces in home from client
    private Piece pieceHome1;
    private Piece pieceHome2;
    private Piece pieceHome3;
    private Piece pieceClient4;

    //declare a card for client
    private PowerCard bonusCardClient;

    //declare the states here
    private AnimationState animationState;
    private CeremonyState ceremonyState;
    private ChoosePieceState choosePieceState;
    private DetermineStartPlayerState determineStartPlayerState;
    private FirstRollState firstRollState;
    private GameState gameState;
    private InterruptState interruptState;
    private LobbyState lobbyState;
    private MovePieceState movePieceState;
    private PlayPowerCardState playPowerCardState;
    private PowerCardState powerCardState;
    private RollDiceState rollDiceState;
    private SecondRollState secondRollState;
    private SelectPieceState selectPieceState;
    private StartPieceState startPieceState;
    private ThirdRollState thirdRollState;
    private TurnState turnState;
    private WaitingPieceState waitingPieceState;
    private NoPieceState noPieceState;
    private NoTurnState noTurnState;

    private SelectedPiecesMessage selectedPiecesMessage;

    /**
     * this method is used to initialize the attributes of this test-class
     */
    @Before
    public void setUp() {
        game = new Game();
        //initialize two players
        playerHost = new Player("Host");
        playerHostColor = Color.CYBER;
        playerHost.setColor(playerHostColor);
        PowerCard shield = new ShieldCard();
        selectCard = new SelectCardMessage(shield);

        playerHost.addHandCard(shield);
        playerHost.addHandCard(new SwapCard());
        playerHost.addHandCard(new TurboCard());
        playerHost.initialize();
        IDPlayerHost = 1;
        game.addPlayer(IDPlayerHost, playerHost);
        hostPiece1 = playerHost.getWaitingArea()[0];
        hostPiece2 = playerHost.getWaitingArea()[1];
        hostPiece3 = playerHost.getWaitingArea()[2];
        hostPiece4 = playerHost.getWaitingArea()[3];
        game.getBoard().setPieceOnBoard(15, hostPiece1);
        hostPiece1.setState(PieceState.ACTIVE);
        game.getBoard().setPieceOnBoard(35, hostPiece2);
        hostPiece2.setState(PieceState.ACTIVE);
        selectedPiecesMessage = new SelectedPiecesMessage(List.of(hostPiece2));

        playerClient = new Player("Client");
        playerClient.addHandCard(new ShieldCard());
        playerClient.addHandCard(new SwapCard());
        playerClient.addHandCard(new TurboCard());
        IDPlayerClient = 2;
        playerClientColor = Color.ARMY;
        playerClient.setColor(playerClientColor);
        playerClient.initialize();
        playerClient.addHandCard(bonusCardClient);
        game.addPlayer(IDPlayerClient, playerClient);
        pieceClient4 = playerClient.getWaitingArea()[3];
        game.getBoard().setPieceOnBoard(22, pieceClient4);
        pieceClient4.setState(PieceState.ACTIVE);

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

        //initialize a piece for client
        pieceState = PieceState.ACTIVE;
        piece = new Piece(playerClientColor, pieceState);
        game.getBoard().setPieceOnBoard(15, piece); //sets the piece at inx 12, bc the home begins after inx 19

        //initialize the other 3 House-Pieces
        pieceHome1 = playerHost.getWaitingArea()[0];
        pieceHome2 = playerHost.getWaitingArea()[1];
        pieceHome3 = playerHost.getWaitingArea()[2];
        game.getPlayerByColor(playerClientColor).setPieceInHome(1, pieceHome1);
        game.getPlayerByColor(playerClientColor).setPieceInHome(2, pieceHome2);
        game.getPlayerByColor(playerClientColor).setPieceInHome(3, pieceHome3);

        //initialize the powerCard
        bonusCardClient = new PowerCard() {
            @Override
            public void accept(Visitor visitor) {

            }
        };

        lobbyState = serverGameLogic.getLobbyState();
        gameState = serverGameLogic.getGameState();
        ceremonyState = serverGameLogic.getCeremonyState();
        interruptState = serverGameLogic.getInterruptState();

        determineStartPlayerState = gameState.getDetermineStartPlayerState();
        turnState = gameState.getTurnState();
        animationState = gameState.getAnimationState();

        animationEnd = new AnimationEndMessage();
        deselectTSK = new DeselectTSKMessage(playerClientColor);
        disconnected = new DisconnectedMessage();
        forceContinueGame = new ForceContinueGameMessage();
        joinServer = new JoinedLobbyMessage();
        leaveGame = new LeaveGameMessage();
        lobbyNotReady = new LobbyNotReadyMessage();
        lobbyReady = new LobbyReadyMessage();
        noPowerCard = new NoPowerCardMessage();
        requestBriefing = new RequestBriefingMessage();
        requestDie = new RequestDieMessage();

        //initialize the states here

        powerCardState = turnState.getPowerCardState();
        playPowerCardState = turnState.getPlayPowerCardState();
        rollDiceState = turnState.getRollDiceState();
        choosePieceState = turnState.getChoosePieceState();
        movePieceState = turnState.getMovePieceState();

        thirdRollState = rollDiceState.getThirdRollState();
        secondRollState = rollDiceState.getSecondRollState();
        firstRollState = rollDiceState.getFirstRollState();

        noPieceState = choosePieceState.getNoPieceState();
        noTurnState = choosePieceState.getNoTurnState();
        waitingPieceState = choosePieceState.getWaitingPieceState();
        startPieceState = choosePieceState.getStartPieceState();
        selectPieceState = choosePieceState.getSelectPieceState();

        /*
         * this attribute is used, so the server knows, that always it's the host's turn
         */
        game.setActiveColor(playerHostColor);
    }

    /**
     * this test-method tests, that the server, when initialized, is in the Lobby-state
     * <p>
     * serverStateTest1
     */
    @Test
    public void testInitialStateServerState() {
        //tests if the server is in lobbyState
        assertEquals(serverGameLogic.getCurrentState(), lobbyState);
    }

    /**
     * this method tests that the server, if all players are ready and the startGameMessage is issued,
     * changes into the DetermineStartPlayer-state
     * <p>
     * serverStateTest2
     */
    @Test
    public void testLobbyToDetermineStartPlayer() {
        //sends the server in the lobby-state
        serverGameLogic.setCurrentState(lobbyState);
        assertEquals(serverGameLogic.getCurrentState(), lobbyState);

        //send all other messages, which don't indicate a state change
        serverGameLogic.received(animationEnd, IDPlayerClient);
        serverGameLogic.received(deselectTSK, IDPlayerClient);
        serverGameLogic.received(disconnected, IDPlayerClient);
        serverGameLogic.received(forceContinueGame, IDPlayerClient);
        serverGameLogic.received(joinServer, IDPlayerClient);
        serverGameLogic.received(lobbyNotReady, IDPlayerClient);
        serverGameLogic.received(lobbyReady, IDPlayerClient);
        serverGameLogic.received(noPowerCard, IDPlayerClient);
        serverGameLogic.received(requestBriefing, IDPlayerClient);
        serverGameLogic.received(requestDie, IDPlayerClient);
        serverGameLogic.received(requestMove, IDPlayerClient);
        serverGameLogic.received(requestPlayCard, IDPlayerClient);
        serverGameLogic.received(selectCard, IDPlayerClient);
        serverGameLogic.received(selectedPieces, IDPlayerClient);

        //tests if the server is still in the lobby-state
        assertEquals(serverGameLogic.getLobbyState(), lobbyState);

        serverGameLogic.received(new LobbyReadyMessage(), IDPlayerClient);
        serverGameLogic.received(new LobbyReadyMessage(), IDPlayerHost);

        //tests if the server is still in the lobby-state
        assertEquals(serverGameLogic.getLobbyState(), lobbyState);

        //sends the startGame-message IDPlayerClient the Host to the server
        serverGameLogic.received(startGame, IDPlayerHost);

        //tests if the server is in DSP-state
        assertEquals(serverGameLogic.getCurrentState(), gameState);
        assertEquals(gameState.getCurrentState(), determineStartPlayerState);
    }

    /**
     * this method tests, that the server stays in the Lobby
     * <p>
     * serverStateTest3
     */
    @Test
    public void testStayInLobby() {
        //sends the server in the lobby-state
        serverGameLogic.setCurrentState(lobbyState);
        assertEquals(serverGameLogic.getCurrentState(), lobbyState);

        //serverGameLogic gets all messages, which don't indicate a state change
        serverGameLogic.received(animationEnd, IDPlayerClient);
        serverGameLogic.received(deselectTSK, IDPlayerClient);
        serverGameLogic.received(forceContinueGame, IDPlayerClient);
        serverGameLogic.received(joinServer, IDPlayerClient);
        serverGameLogic.received(lobbyNotReady, IDPlayerClient);
        serverGameLogic.received(lobbyReady, IDPlayerClient);
        serverGameLogic.received(noPowerCard, IDPlayerClient);
        serverGameLogic.received(requestBriefing, IDPlayerClient);
        serverGameLogic.received(requestDie, IDPlayerClient);
        serverGameLogic.received(requestMove, IDPlayerClient);
        serverGameLogic.received(requestPlayCard, IDPlayerClient);
        serverGameLogic.received(selectCard, IDPlayerClient);
        serverGameLogic.received(selectedPieces, IDPlayerClient);

        //tests if Server is still in Lobby
        assertEquals(serverGameLogic.getCurrentState(), lobbyState);
    }

    /**
     * this method tests, that the server can go into the interrupt-state
     * <p>
     * serverStateTest4
     */
    @Test
    public void testServerGameSubStatesToInterrupt() {
        //sends the server in the gameState
        serverGameLogic.setCurrentState(gameState);
        assertEquals(serverGameLogic.getCurrentState(), gameState);

        //sends the interrupt
        serverGameLogic.received(disconnected, IDPlayerClient);

        //tests if the server is in the Interrupt-state
        assertEquals(serverGameLogic.getInterruptState(), interruptState);
    }

    /**
     * tests the state-change IDPlayerClient Game to Ceremony if the Game is finished
     * <p>
     * serverStateTest5
     */
    @Test
    public void testServerGameToCeremony() {
        game.getBoard().setPieceOnBoard(22, null);
        Piece piece1 = playerClient.getWaitingArea()[0];
        piece1.setState(PieceState.HOMEFINISHED);
        Piece piece2 = playerClient.getWaitingArea()[1];
        piece2.setState(PieceState.HOMEFINISHED);
        Piece piece3 = playerClient.getWaitingArea()[2];
        piece3.setState(PieceState.HOMEFINISHED);
        Piece piece4 = playerClient.getWaitingArea()[3];
        playerClient.removeWaitingPiece(piece1);
        playerClient.removeWaitingPiece(piece2);
        playerClient.removeWaitingPiece(piece3);
        playerClient.removeWaitingPiece(piece4);
        playerClient.setPieceInHome(3, piece1);
        playerClient.setPieceInHome(2, piece2);
        playerClient.setPieceInHome(1, piece1);
        game.getBoard().setPieceOnBoard(29, piece4);

        game.setActiveColor(playerClientColor);
        playerClient.setHandCards(new ArrayList<>());
        turnState.setPlayer(playerClient);

        //sends the server in GameState
        serverGameLogic.setCurrentState(gameState);
        assertEquals(serverGameLogic.getCurrentState(), gameState);

        //sends the server in Turn
        gameState.setCurrentState(turnState);
        assertEquals(gameState.getCurrentState(), turnState);

        //sends the server in ChoosePiece
        assertEquals(turnState.getCurrentState(), rollDiceState);

        game.setDie(new Die(1));
        serverGameLogic.received(new RequestDieMessage(), IDPlayerClient);
        serverGameLogic.received(new AnimationEndMessage(), IDPlayerClient);

        //sends the server in StartPiece
        assertEquals(choosePieceState.getCurrentState(), selectPieceState);

        serverGameLogic.received(new RequestMoveMessage(piece4), IDPlayerClient);
        assertEquals(serverGameLogic.getCurrentState(), gameState);
        assertEquals(gameState.getCurrentState(), turnState);
        assertEquals(turnState.getCurrentState(), movePieceState);

        serverGameLogic.received(new AnimationEndMessage(), IDPlayerClient);
        serverGameLogic.received(new AnimationEndMessage(), IDPlayerHost);

        assertTrue(playerClient.isFinished());

        //tests if the server is in the end-state of game, Ceremony
        assertEquals(serverGameLogic.getCurrentState(), ceremonyState);
    }

    /**
     * this method tests that the server goes back to the Game, when the ForceStartGame-message is issued
     * <p>
     * serverStateTest6
     */
    @Test
    public void testInterruptToGameContinue() {
        //sends the server in the Interrupt-State
        serverGameLogic.setCurrentState(interruptState);
        assertEquals(serverGameLogic.getCurrentState(), interruptState);

        //send all other messages except forceContinue
        serverGameLogic.received(animationEnd, IDPlayerClient);
        serverGameLogic.received(deselectTSK, IDPlayerClient);
        serverGameLogic.received(disconnected, IDPlayerClient);
        serverGameLogic.received(joinServer, IDPlayerClient);
        serverGameLogic.received(leaveGame, IDPlayerClient);
        serverGameLogic.received(lobbyNotReady, IDPlayerClient);
        serverGameLogic.received(lobbyReady, IDPlayerClient);
        serverGameLogic.received(noPowerCard, IDPlayerClient);
        serverGameLogic.received(requestBriefing, IDPlayerClient);
        serverGameLogic.received(requestDie, IDPlayerClient);
        serverGameLogic.received(requestMove, IDPlayerClient);
        serverGameLogic.received(requestPlayCard, IDPlayerClient);
        serverGameLogic.received(selectCard, IDPlayerClient);
        serverGameLogic.received(selectedPieces, IDPlayerClient);
        serverGameLogic.received(selectTSK, IDPlayerClient);
        serverGameLogic.received(startGame, IDPlayerClient);

        //tests if the server is still in Interrupt
        assertEquals(serverGameLogic.getCurrentState(), interruptState);

        //sends the continue-message to the server
        serverGameLogic.received(forceContinueGame, IDPlayerClient);

        //tests if new Stet is in GameState
        assertEquals(serverGameLogic.getCurrentState(), gameState);
    }

    /**
     * this method tests, that the server goes back to the game, if the missing client has reconnected
     * <p>
     * serverStateTest7
     */
    @Test
    public void testInterruptToGameReconnect() {
        /*


        //sends the server in the Interrupt-State
        serverGameLogic.setCurrentState(interruptState);
        assertEquals(serverGameLogic.getCurrentState(), interruptState);

        //todo implement the reconnect

        //tests if new Stet is in GameState
        assertEquals(serverGameLogic.getCurrentState(), gameState);
        */
    }

    /**
     * this method tests, that th e server continues with the game, if there is no time left on the timer
     * <p>
     * serverStateTest8
     */
    @Test
    public void testInterruptToGameTimer() {
        /*
        //sends the server in the Interrupt-State
        serverGameLogic.setCurrentState(interruptState);
        assertEquals(serverGameLogic.getCurrentState(), interruptState);

        //Todo implement the timer

        //tests if new Stet is in GameState
        assertEquals(serverGameLogic.getCurrentState(), gameState);

         */
    }

    /**
     * this method tests, that the server closes, if the ceremony has ended
     * <p>
     * serverStateTest9
     */
    @Test
    public void testCeremonyToServerStateEndState() {
    }

    /**
     * this method tests that the server stays in DetermineStartPlayer, when issued messages
     * <p>
     * serverStateTest 10
     */
    @Test
    public void testDetermineStartPlayerToDetermineStartPlayer1() {
        //sends the server in Game-State
        serverGameLogic.setCurrentState(gameState);
        assertEquals(serverGameLogic.getCurrentState(), gameState);

        //sends the gameState in DSP-state
        gameState.setCurrentState(determineStartPlayerState);
        assertEquals(gameState.getCurrentState(), determineStartPlayerState);

        //sends messages to the server
        serverGameLogic.received(animationEnd, IDPlayerClient);
        serverGameLogic.received(deselectTSK, IDPlayerClient);
        //serverGameLogic.received(disconnected, IDPlayerClient);
        serverGameLogic.received(forceContinueGame, IDPlayerClient);
        serverGameLogic.received(joinServer, IDPlayerClient);
        //serverGameLogic.received(leaveGame, IDPlayerClient);
        serverGameLogic.received(lobbyNotReady, IDPlayerClient);
        serverGameLogic.received(lobbyReady, IDPlayerClient);
        serverGameLogic.received(noPowerCard, IDPlayerClient);
        serverGameLogic.received(requestBriefing, IDPlayerClient);
        serverGameLogic.received(requestDie, IDPlayerClient);
        serverGameLogic.received(requestMove, IDPlayerClient);
        serverGameLogic.received(requestPlayCard, IDPlayerClient);
        serverGameLogic.received(selectCard, IDPlayerClient);
        serverGameLogic.received(selectedPieces, IDPlayerClient);
        serverGameLogic.received(selectTSK, IDPlayerClient);
        serverGameLogic.received(startGame, IDPlayerClient);

        //tests if the server is still in DSP-state
        assertEquals(serverGameLogic.getCurrentState(), gameState);
        assertEquals(gameState.getCurrentState(), determineStartPlayerState);
    }

    /**
     * this method tests that the server stays in DetermineStartPlayer, when all players are ranked and two of the highest are even
     * <p>
     * serverStateTest 11
     */
    @Test
    public void testDetermineStartPlayerToDetermineStartPlayer2() {
        //sends the server in Game-State
        serverGameLogic.setCurrentState(gameState);
        assertEquals(serverGameLogic.getCurrentState(), gameState);

        //sends the gameState in DSP-state
        gameState.setCurrentState(determineStartPlayerState);
        assertEquals(gameState.getCurrentState(), determineStartPlayerState);

        //set die to 6
        game.setDie(new Die(6));

        //sends the two requestDiceMessages
        serverGameLogic.received(requestDie, IDPlayerClient);
        serverGameLogic.received(requestDie, IDPlayerHost);

        //sends the requestDieMessage to the server
        gameState.received(requestDie, IDPlayerClient);

        //tests if the server is still in DSP-state
        assertEquals(serverGameLogic.getCurrentState(), gameState);
        assertEquals(gameState.getCurrentState(), determineStartPlayerState);

        //sends the requestDieMessage to the server
        gameState.received(requestMove, IDPlayerClient);

        //tests if the server is still in DSP-state
        assertEquals(serverGameLogic.getCurrentState(), gameState);
        assertEquals(gameState.getCurrentState(), determineStartPlayerState);
    }

    /**
     * this method tests that the server goes into Animation-state, if there is an order
     * <p>
     * serverStateTest12
     */
    @Test
    public void testDetermineStartPlayerToAnimation() {
        //sends the server in Game-State
        serverGameLogic.setCurrentState(gameState);
        assertEquals(serverGameLogic.getCurrentState(), gameState);

        //sends the gameState in DSP-state
        gameState.setCurrentState(determineStartPlayerState);
        assertEquals(gameState.getCurrentState(), determineStartPlayerState);

        //set die to 6
        game.setDie(new Die(6));

        //sends the two requestDiceMessages
        serverGameLogic.received(requestDie, IDPlayerClient);

        //set die to 6
        game.setDie(new Die(5));

        //sends the two requestDiceMessages
        serverGameLogic.received(requestDie, IDPlayerHost);
        serverGameLogic.received(new AnimationEndMessage(), IDPlayerClient);
        serverGameLogic.received(new AnimationEndMessage(), IDPlayerHost);

        //tests if the Server is in animationState
        assertEquals(serverGameLogic.getCurrentState(), gameState);
        assertEquals(gameState.getCurrentState(), animationState);
    }

    /**
     * tests that the server goes IDPlayerClient the animation-state into PowerCardState, if the animation in the client has ended
     * and all players have issued a animationEndMessage
     * <p>
     * serverStateTest13
     */
    @Test
    public void testAnimationToPowerCard() {
        //sends the server in Game-State
        serverGameLogic.setCurrentState(gameState);
        assertEquals(serverGameLogic.getCurrentState(), gameState);

        //sends the gameState in Animation
        gameState.setCurrentState(animationState);
        assertEquals(gameState.getCurrentState(), animationState);

        //receives one animation endMessage and tests if the server is still in the Animation-state
        serverGameLogic.received(animationEnd, IDPlayerClient);
        assertEquals(serverGameLogic.getCurrentState(), gameState);
        assertEquals(gameState.getCurrentState(), animationState);

        //receives another animation endMessage
        serverGameLogic.received(animationEnd, IDPlayerClient);
        serverGameLogic.received(animationEnd, IDPlayerHost);

        //tests if the server is in the PowerCard-state
        assertEquals(serverGameLogic.getCurrentState(), gameState);
        assertEquals(gameState.getCurrentState(), turnState);
        assertEquals(turnState.getCurrentState(), powerCardState);
    }

    /**
     * this method tests that the server changes it's state IDPlayerClient the turn-state to the animation-state, if there are at
     * least two player left
     * <p>
     * serverStateTest14
     */
    @Test
    public void testTurnToAnimation() {
        game.setActiveColor(playerClientColor);
        playerClient.setHandCards(new ArrayList<>());
        //sends the server in Game-State
        serverGameLogic.setCurrentState(gameState);
        assertEquals(serverGameLogic.getCurrentState(), gameState);

        //sends the gameState in Turn
        gameState.setCurrentState(turnState);
        assertEquals(gameState.getCurrentState(), turnState);

        //sends the server in Animation
        turnState.setCurrentState(movePieceState);
        assertEquals(turnState.getCurrentState(), movePieceState);

        //set dices-eyes in game !=6
        game.setDiceEyes(4);

        //serverGameLogic receives animationEndMessages
        serverGameLogic.received(animationEnd, IDPlayerClient);

        //tests if the server is still in movePieceState
        assertEquals(serverGameLogic.getCurrentState(), gameState);
        assertEquals(gameState.getCurrentState(), turnState);
        assertEquals(turnState.getCurrentState(), movePieceState);

        //sends the second animationEndMessage
        serverGameLogic.received(animationEnd, IDPlayerHost);
        serverGameLogic.received(animationEnd, IDPlayerClient);

        //tests if the server is in the AnimationState
        assertEquals(serverGameLogic.getCurrentState(), gameState);
        assertEquals(gameState.getCurrentState(), turnState);
        assertNotSame(game.getActiveColor(), playerClientColor);
    }

    /**
     * this method tests that the server changes it's state IDPlayerClient the turn-state to the ceremony-state,
     * if there is only one player left
     * <p>
     * serverStateTest15
     */
    @Test
    public void testTurnToGameEndState() {
        game.getBoard().setPieceOnBoard(22, null);
        Piece piece1 = playerClient.getWaitingArea()[0];
        piece1.setState(PieceState.HOMEFINISHED);
        Piece piece2 = playerClient.getWaitingArea()[1];
        piece2.setState(PieceState.HOMEFINISHED);
        Piece piece3 = playerClient.getWaitingArea()[2];
        piece3.setState(PieceState.HOMEFINISHED);
        Piece piece4 = playerClient.getWaitingArea()[3];
        playerClient.removeWaitingPiece(piece1);
        playerClient.removeWaitingPiece(piece2);
        playerClient.removeWaitingPiece(piece3);
        playerClient.removeWaitingPiece(piece4);
        playerClient.setPieceInHome(3, piece1);
        playerClient.setPieceInHome(2, piece2);
        playerClient.setPieceInHome(1, piece1);
        game.getBoard().setPieceOnBoard(29, piece4);

        game.setActiveColor(playerClientColor);
        playerClient.setHandCards(new ArrayList<>());
        turnState.setPlayer(playerClient);

        //sends the server in GameState
        serverGameLogic.setCurrentState(gameState);
        assertEquals(serverGameLogic.getCurrentState(), gameState);

        //sends the server in Turn
        gameState.setCurrentState(turnState);
        assertEquals(gameState.getCurrentState(), turnState);

        //sends the server in ChoosePiece
        assertEquals(turnState.getCurrentState(), rollDiceState);

        game.setDie(new Die(1));
        serverGameLogic.received(new RequestDieMessage(), IDPlayerClient);
        serverGameLogic.received(new AnimationEndMessage(), IDPlayerClient);

        //sends the server in StartPiece
        assertEquals(choosePieceState.getCurrentState(), selectPieceState);

        serverGameLogic.received(new RequestMoveMessage(piece4), IDPlayerClient);
        assertEquals(serverGameLogic.getCurrentState(), gameState);
        assertEquals(gameState.getCurrentState(), turnState);
        assertEquals(turnState.getCurrentState(), movePieceState);

        serverGameLogic.received(new AnimationEndMessage(), IDPlayerClient);
        serverGameLogic.received(new AnimationEndMessage(), IDPlayerHost);

        assertTrue(playerClient.isFinished());

        //tests if the server is in the end-state of game, Ceremony
        assertEquals(serverGameLogic.getCurrentState(), ceremonyState);
    }

    /**
     * this method tests that the server don't change it's state whe issued with messages,
     * which don't implicate a statechange
     * <p>
     * serverStateTest16
     */
    @Test
    public void testStayInPowerCard() {
        //sends the server in Game-State
        serverGameLogic.setCurrentState(gameState);
        assertEquals(serverGameLogic.getCurrentState(), gameState);

        //sends the gameState in Turn
        gameState.setCurrentState(turnState);
        assertEquals(gameState.getCurrentState(), turnState);

        //sends the TurnState in PowerCard
        turnState.setCurrentState(powerCardState);
        assertEquals(turnState.getCurrentState(), powerCardState);

        //receive messages which don't lead to a state change
        serverGameLogic.received(deselectTSK, IDPlayerClient);
        serverGameLogic.received(forceContinueGame, IDPlayerClient);
        serverGameLogic.received(joinServer, IDPlayerClient);
        serverGameLogic.received(lobbyNotReady, IDPlayerClient);
        serverGameLogic.received(lobbyReady, IDPlayerClient);
        serverGameLogic.received(requestBriefing, IDPlayerClient);
        serverGameLogic.received(requestDie, IDPlayerClient);
        serverGameLogic.received(requestMove, IDPlayerClient);
        serverGameLogic.received(selectTSK, IDPlayerClient);
        serverGameLogic.received(startGame, IDPlayerClient);

        //tests if the server is in PowerCard
        assertEquals(serverGameLogic.getCurrentState(), gameState);
        assertEquals(gameState.getCurrentState(), turnState);
        assertEquals(turnState.getCurrentState(), powerCardState);
    }

    /**
     * Tests the transition IDPlayerClient PowerCard state to PlayPowerCard state.
     * UC-ServerState-17
     */
    @Test
    public void testPowerCardToPlayPowerCard() {
        //sends the server in Game-State
        serverGameLogic.setCurrentState(gameState);
        assertEquals(serverGameLogic.getCurrentState(), gameState);

        //sends the gameState in Turn
        gameState.setCurrentState(turnState);
        assertEquals(gameState.getCurrentState(), turnState);

        //sends the TurnState in PowerCard
        turnState.setCurrentState(powerCardState);
        assertEquals(turnState.getCurrentState(), powerCardState);

        //sends the server in shieldState
        powerCardState.setCurrentState(powerCardState.getShieldCardState());
        assertEquals(powerCardState.getCurrentState(), powerCardState.getShieldCardState());

        //
        serverGameLogic.received(selectedPiecesMessage, IDPlayerHost);

        //tests if the server is in PlayPowerCard
        assertEquals(serverGameLogic.getCurrentState(), gameState);
        assertEquals(gameState.getCurrentState(), turnState);
        assertEquals(turnState.getCurrentState(), playPowerCardState);
    }

    /**
     * Tests the transition IDPlayerClient PlayPowerCard state to RollDice state.
     * UC-ServerState-18
     */
    @Test
    public void testPlayPowerCardToRollDice() {
        //sends the server in Game-State
        serverGameLogic.setCurrentState(gameState);
        assertEquals(serverGameLogic.getCurrentState(), gameState);

        //sends the gameState in Turn
        gameState.setCurrentState(turnState);
        assertEquals(gameState.getCurrentState(), turnState);

        //sends the TurnState in PowerCard
        turnState.setCurrentState(playPowerCardState);
        assertEquals(turnState.getCurrentState(), playPowerCardState);

        //receive first AnimationEndMessage IDPlayerClient the host
        serverGameLogic.received(animationEnd, IDPlayerClient);
        assertEquals(serverGameLogic.getCurrentState(), gameState);
        assertEquals(gameState.getCurrentState(), turnState);
        assertEquals(turnState.getCurrentState(), playPowerCardState);

        //receive second AnimationEndMessage
        serverGameLogic.received(animationEnd, IDPlayerClient);
        serverGameLogic.received(animationEnd, IDPlayerHost);

        //tests if the server is in RollDice and in FirstRoll
        assertEquals(serverGameLogic.getCurrentState(), gameState);
        assertEquals(gameState.getCurrentState(), turnState);
        assertEquals(turnState.getCurrentState(), rollDiceState);
        assertEquals(rollDiceState.getCurrentState(), firstRollState);
    }

    /**
     * Tests the transition IDPlayerClient ChoosePiece state to MovePiece state.
     * UC-ServerState-19
     */
    @Test
    public void testChoosePieceToMovePiece() {
        playerClient.setHandCards(new ArrayList<>());
        game.setActiveColor(playerClientColor);
        turnState.setPlayer(playerClient);
        playerClient.removeWaitingPiece(pieceClient4);
        game.getBoard().setPieceOnBoard(25, pieceClient4);
        pieceClient4.setState(PieceState.ACTIVE);

        //sends the server in Game-State
        serverGameLogic.setCurrentState(gameState);
        assertEquals(serverGameLogic.getCurrentState(), gameState);
        game.setActiveColor(playerClientColor);
        game.setDiceEyes(1);
        //sends the gameState in Turn
        gameState.setCurrentState(turnState);
        assertEquals(gameState.getCurrentState(), turnState);
        game.setActiveColor(playerClientColor);
        //sends the TurnState in ChoosePiece
        turnState.setCurrentState(choosePieceState);
        assertEquals(turnState.getCurrentState(), choosePieceState);

        choosePieceState.setCurrentState(selectPieceState);
        selectPieceState.setMoveablePieces(new ArrayList<>(List.of(pieceClient4)));
        selectPieceState.setIsHomeMove(new ArrayList<>(List.of(false)));
        selectPieceState.setTargetIndex(new ArrayList<>(List.of(23)));

        serverGameLogic.received(new RequestMoveMessage(pieceClient4), IDPlayerClient);

        assertEquals(serverGameLogic.getCurrentState(), gameState);
        assertEquals(gameState.getCurrentState(), turnState);
        assertEquals(turnState.getCurrentState(), movePieceState);
    }

    /**
     * Tests the transition IDPlayerClient MovePiece state to TurnEndState.
     * UC-ServerState-20
     */
    @Test
    public void testMovePieceToTurnEndState() {
        game.setActiveColor(playerClientColor);
        playerClient.setHandCards(new ArrayList<>());
        turnState.setPlayer(playerClient);

        //sends the server in GameState
        serverGameLogic.setCurrentState(gameState);
        assertEquals(serverGameLogic.getCurrentState(), gameState);

        //sends the server in Turn
        gameState.setCurrentState(turnState);
        assertEquals(gameState.getCurrentState(), turnState);

        //sends the server in ChoosePiece
        assertEquals(turnState.getCurrentState(), rollDiceState);

        game.setDie(new Die(3));
        serverGameLogic.received(new RequestDieMessage(), IDPlayerClient);
        serverGameLogic.received(new AnimationEndMessage(), IDPlayerClient);

        //tests if the server is in SelectPiece
        assertEquals(serverGameLogic.getCurrentState(), gameState);
        assertEquals(gameState.getCurrentState(), turnState);
        assertEquals(turnState.getCurrentState(), choosePieceState);
        assertEquals(choosePieceState.getCurrentState(), selectPieceState);

        serverGameLogic.received(new RequestMoveMessage(pieceClient4), IDPlayerClient);

        //tests if the server is in SelectPiece
        assertEquals(serverGameLogic.getCurrentState(), gameState);
        assertEquals(gameState.getCurrentState(), turnState);
        assertEquals(turnState.getCurrentState(), movePieceState);

        serverGameLogic.received(new AnimationEndMessage(), IDPlayerClient);
        serverGameLogic.received(new AnimationEndMessage(), IDPlayerHost);

        assertEquals(serverGameLogic.getCurrentState(), gameState);
        assertEquals(gameState.getCurrentState(), turnState);
        assertNotEquals(turnState.getCurrentState(), movePieceState);
        assertNotSame(game.getActiveColor(), playerClientColor);
    }

    /**
     * Tests the transition IDPlayerClient MovePiece state to FirstRoll state.
     * UC-ServerState-21
     */
    @Test
    public void testMovePieceToFirstRoll() {
        //sets the die to 6
        game.setDiceEyes(6);

        //sends the server in Game-State
        serverGameLogic.setCurrentState(gameState);
        assertEquals(serverGameLogic.getCurrentState(), gameState);

        //sends the gameState in Turn
        gameState.setCurrentState(turnState);
        assertEquals(gameState.getCurrentState(), turnState);

        //sends the TurnState in MovePiece
        turnState.setCurrentState(movePieceState);
        assertEquals(turnState.getCurrentState(), movePieceState);

        //sends the animationEndMessage
        serverGameLogic.received(animationEnd, IDPlayerClient);
        serverGameLogic.received(animationEnd, IDPlayerHost);

        //tests if the server is in firstRoll
        assertEquals(serverGameLogic.getCurrentState(), gameState);
        assertEquals(gameState.getCurrentState(), turnState);
        assertEquals(turnState.getCurrentState(), rollDiceState);
        assertEquals(rollDiceState.getCurrentState(), firstRollState);

        //tests if the activeColor is still Host
        assertEquals(game.getActiveColor(), playerHostColor);
    }

    /**
     * Tests the transition IDPlayerClient FirstRoll state to RollDiceEndState.
     * UC-ServerState-22
     */
    @Test
    public void testFirstRollToRollDiceEndState() {
        playerClient.setHandCards(new ArrayList<>());

        //sends the server in Game-State
        serverGameLogic.setCurrentState(gameState);
        assertEquals(serverGameLogic.getCurrentState(), gameState);

        //sends the gameState in Turn
        gameState.setCurrentState(turnState);
        assertEquals(gameState.getCurrentState(), turnState);

        game.setActiveColor(playerClientColor);
        //sends the TurnState in RollDice
        turnState.setCurrentState(rollDiceState);
        assertEquals(turnState.getCurrentState(), rollDiceState);

        //sends the RollDiceState in FirstRoll
        rollDiceState.setCurrentState(firstRollState);
        assertEquals(rollDiceState.getCurrentState(), firstRollState);

        firstRollState.setMoveablePieces(new ArrayList<>(List.of(pieceClient4)));

        game.setDie(new Die(1));

        serverGameLogic.received(new RequestDieMessage(), IDPlayerClient);
        serverGameLogic.received(new AnimationEndMessage(), IDPlayerClient);

        //tests if the server is in ChoosePiece
        assertEquals(serverGameLogic.getCurrentState(), gameState);
        assertEquals(gameState.getCurrentState(), turnState);
        assertEquals(turnState.getCurrentState(), choosePieceState);
    }

    /**
     * Tests the transition IDPlayerClient FirstRoll state to SecondRoll state.
     * UC-ServerState-23
     */
    @Test
    public void testFirstRollToSecondRoll() {
        playerClient.addWaitingPiece(pieceClient4);
        playerClient.setHandCards(new ArrayList<>());
        pieceClient4.setState(PieceState.WAITING);

        game.setActiveColor(playerClientColor);
        //sends the server in Game-State
        serverGameLogic.setCurrentState(gameState);
        assertEquals(serverGameLogic.getCurrentState(), gameState);

        //sends the gameState in Turn
        gameState.setCurrentState(turnState);
        assertEquals(gameState.getCurrentState(), turnState);

        //sends the TurnState in RollDice
        turnState.setCurrentState(rollDiceState);
        assertEquals(turnState.getCurrentState(), rollDiceState);

        //sends the RollDiceState in FirstRoll
        rollDiceState.setCurrentState(firstRollState);
        assertEquals(rollDiceState.getCurrentState(), firstRollState);

        game.setDie(new Die(5));
        game.setDiceEyes(5);
        //player has no figures to move and had no 6
        serverGameLogic.received(requestDie, IDPlayerClient);
        serverGameLogic.received(animationEnd, IDPlayerClient);

        //tests if the server is in the SecondRoll
        assertEquals(serverGameLogic.getCurrentState(), gameState);
        assertEquals(gameState.getCurrentState(), turnState);
        assertEquals(turnState.getCurrentState(), rollDiceState);
        assertEquals(rollDiceState.getCurrentState(), secondRollState);
    }

    /**
     * Tests the transition IDPlayerClient SecondRoll state to RollDiceEndState.
     * UC-ServerState-24
     */
    @Test
    public void testSecondRollToRollDiceEndState() {
        playerClient.addWaitingPiece(pieceClient4);
        pieceClient4.setState(PieceState.WAITING);

        game.setActiveColor(playerClientColor);
        serverGameLogic.setCurrentState(gameState);
        assertEquals(serverGameLogic.getCurrentState(), gameState);

        //sends the gameState in Turn
        turnState.setPlayer(playerClient);
        playerClient.setHandCards(new ArrayList<>());
        gameState.setCurrentState(turnState);
        assertEquals(gameState.getCurrentState(), turnState);

        //sends the TurnState in RollDice
        turnState.setCurrentState(rollDiceState);
        assertEquals(turnState.getCurrentState(), rollDiceState);

        //sends the RollDiceState in SecondRoll
        rollDiceState.setCurrentState(secondRollState);
        assertEquals(rollDiceState.getCurrentState(), secondRollState);

        game.setDie(new Die(6));

        //
        serverGameLogic.received(requestDie, IDPlayerClient);
        serverGameLogic.received(animationEnd, IDPlayerClient);

        //tests if the server is in NoPiece of ChoosePiece
        assertEquals(serverGameLogic.getCurrentState(), gameState);
        assertEquals(gameState.getCurrentState(), turnState);
        assertEquals(turnState.getCurrentState(), choosePieceState);
    }

    /**
     * Tests the transition IDPlayerClient SecondRoll state to ThirdRoll state.
     * UC-ServerState-25
     */
    @Test
    public void testSecondRollToThirdRoll() {
        //sends the server in Game-State
        serverGameLogic.setCurrentState(gameState);
        assertEquals(serverGameLogic.getCurrentState(), gameState);

        //sends the gameState in Turn
        gameState.setCurrentState(turnState);
        assertEquals(gameState.getCurrentState(), turnState);

        //sends the TurnState in RollDice
        turnState.setCurrentState(rollDiceState);
        assertEquals(turnState.getCurrentState(), rollDiceState);

        //sends the RollDiceState in SecondRoll
        rollDiceState.setCurrentState(secondRollState);
        assertEquals(rollDiceState.getCurrentState(), secondRollState);

        game.setDie(new Die(5));
        game.setDiceEyes(5);
        //player has no figures to move and had no 6
        serverGameLogic.received(requestDie, IDPlayerHost);
        serverGameLogic.received(animationEnd, IDPlayerHost);

        //tests if the server is in the ThirdRoll
        assertEquals(serverGameLogic.getCurrentState(), gameState);
        assertEquals(gameState.getCurrentState(), turnState);
        assertEquals(turnState.getCurrentState(), rollDiceState);
        assertEquals(rollDiceState.getCurrentState(), thirdRollState);
    }

    /**
     * Tests the transition IDPlayerClient ThirdRoll state to RollDiceEndState.
     * UC-ServerState-26
     */
    @Test
    public void testThirdRollToRollDiceEndState() {
        game.setActiveColor(playerClientColor);
        turnState.setPlayer(playerClient);
        playerClient.setHandCards(new ArrayList<>());
        //sends the server in Game-State
        serverGameLogic.setCurrentState(gameState);
        assertEquals(serverGameLogic.getCurrentState(), gameState);

        //sends the gameState in Turn
        gameState.setCurrentState(turnState);
        assertEquals(gameState.getCurrentState(), turnState);

        //sends the TurnState in RollDice
        turnState.setCurrentState(rollDiceState);
        assertEquals(turnState.getCurrentState(), rollDiceState);

        //sends the RollDiceState in ThirdRoll
        rollDiceState.setCurrentState(thirdRollState);
        assertEquals(rollDiceState.getCurrentState(), thirdRollState);

        //die =6
        game.setDie(new Die(6));
        serverGameLogic.received(requestDie, IDPlayerClient);
        serverGameLogic.received(animationEnd, IDPlayerClient);

        //tests if the server is in NoPiece of ChoosePiece
        assertEquals(serverGameLogic.getCurrentState(), gameState);
        assertEquals(gameState.getCurrentState(), turnState);
        assertEquals(turnState.getCurrentState(), choosePieceState);
    }

    /**
     * Tests the transition IDPlayerClient ThirdRoll state to TurnEndState.
     * UC-ServerState-27
     */
    @Test
    public void testThirdRollToTurnEndState() {
        game.setActiveColor(playerClientColor);
        playerClient.setHandCards(new ArrayList<>());

        //sends the server in Game-State
        serverGameLogic.setCurrentState(gameState);
        assertEquals(serverGameLogic.getCurrentState(), gameState);

        //sends the gameState in Turn
        gameState.setCurrentState(turnState);
        assertEquals(gameState.getCurrentState(), turnState);

        //sends the TurnState in RollDice
        turnState.setCurrentState(rollDiceState);
        assertEquals(turnState.getCurrentState(), rollDiceState);

        //sends the RollDiceState in ThirdRoll
        rollDiceState.setCurrentState(thirdRollState);
        assertEquals(rollDiceState.getCurrentState(), thirdRollState);

        game.setDie(new Die(4));

        serverGameLogic.received(new RequestDieMessage(), IDPlayerClient);
        serverGameLogic.received(new AnimationEndMessage(), IDPlayerClient);

        assertNotSame(game.getActiveColor(), playerClientColor);
    }

    /**
     * Tests the transition IDPlayerClient NoPiece state to WaitingPiece state.
     * UC-ServerState-28
     */
    @Test
    public void testNoPieceToWaitingPiece() {
        game.setActiveColor(playerClientColor);
        playerClient.setHandCards(new ArrayList<>());
        turnState.setPlayer(playerClient);
        game.getBoard().getInfield()[22].clearOccupant();
        game.getBoard().setPieceOnBoard(29, pieceClient4);

        //sends the server in GameState
        serverGameLogic.setCurrentState(gameState);
        assertEquals(serverGameLogic.getCurrentState(), gameState);

        //sends the server in Turn
        gameState.setCurrentState(turnState);
        assertEquals(gameState.getCurrentState(), turnState);

        //sends the server in ChoosePiece
        assertEquals(turnState.getCurrentState(), rollDiceState);

        game.setDie(new Die(6));
        serverGameLogic.received(new RequestDieMessage(), IDPlayerClient);
        serverGameLogic.received(new AnimationEndMessage(), IDPlayerClient);

        //tests if the server is in WaitPiece
        assertEquals(serverGameLogic.getCurrentState(), gameState);
        assertEquals(gameState.getCurrentState(), turnState);
        assertEquals(turnState.getCurrentState(), choosePieceState);
        assertEquals(choosePieceState.getCurrentState(), waitingPieceState);
    }

    /**
     * Tests the transition IDPlayerClient NoPiece state to NoTurn state.
     * UC-ServerState-29
     */
    @Test
    public void testNoPieceToNoTurn() {
        game.setActiveColor(playerClientColor);
        playerClient.setHandCards(new ArrayList<>());
        turnState.setPlayer(playerClient);
        game.getBoard().getInfield()[22].clearOccupant();
        game.getBoard().setPieceOnBoard(29, pieceClient4);

        //sends the server in GameState
        serverGameLogic.setCurrentState(gameState);
        assertEquals(serverGameLogic.getCurrentState(), gameState);

        //sends the server in Turn
        gameState.setCurrentState(turnState);
        assertEquals(gameState.getCurrentState(), turnState);

        //sends the server in ChoosePiece
        assertEquals(turnState.getCurrentState(), rollDiceState);

        game.setDie(new Die(5));
        serverGameLogic.received(new RequestDieMessage(), IDPlayerClient);
        serverGameLogic.received(new AnimationEndMessage(), IDPlayerClient);

        //tests if the server is in WaitPiece
        assertEquals(serverGameLogic.getCurrentState(), gameState);
        assertEquals(gameState.getCurrentState(), turnState);
        assertNotSame(game.getActiveColor(), playerClientColor);
    }

    /**
     * Tests the transition IDPlayerClient NoTurn state to TurnEndState.
     * UC-ServerState-30
     */
    @Test
    public void testNoTurnToTurnEndState() {
        game.setActiveColor(playerClientColor);
        playerClient.setHandCards(new ArrayList<>());
        turnState.setPlayer(playerClient);
        game.getBoard().getInfield()[22].clearOccupant();
        game.getBoard().setPieceOnBoard(29, pieceClient4);

        //sends the server in GameState
        serverGameLogic.setCurrentState(gameState);
        assertEquals(serverGameLogic.getCurrentState(), gameState);

        //sends the server in Turn
        gameState.setCurrentState(turnState);
        assertEquals(gameState.getCurrentState(), turnState);

        //sends the server in ChoosePiece
        assertEquals(turnState.getCurrentState(), rollDiceState);

        game.setDie(new Die(5));
        serverGameLogic.received(new RequestDieMessage(), IDPlayerClient);
        serverGameLogic.received(new AnimationEndMessage(), IDPlayerClient);

        //tests if the server is in WaitPiece
        assertEquals(serverGameLogic.getCurrentState(), gameState);
        assertEquals(gameState.getCurrentState(), turnState);
        assertNotSame(game.getActiveColor(), playerClientColor);
    }

    /**
     * Tests that the system stays in the WaitingPiece state.
     * UC-ServerState-31
     */
    @Test
    public void testStayInWaitingPiece() {
        game.setActiveColor(playerClientColor);
        playerClient.setHandCards(new ArrayList<>());
        turnState.setPlayer(playerClient);
        game.getBoard().getInfield()[22].clearOccupant();
        game.getBoard().setPieceOnBoard(29, pieceClient4);

        //sends the server in GameState
        serverGameLogic.setCurrentState(gameState);
        assertEquals(serverGameLogic.getCurrentState(), gameState);

        //sends the server in Turn
        gameState.setCurrentState(turnState);
        assertEquals(gameState.getCurrentState(), turnState);

        //sends the server in ChoosePiece
        assertEquals(turnState.getCurrentState(), rollDiceState);

        game.setDie(new Die(6));
        serverGameLogic.received(new RequestDieMessage(), IDPlayerClient);
        serverGameLogic.received(new AnimationEndMessage(), IDPlayerClient);

        //tests if the server is in WaitPiece
        assertEquals(serverGameLogic.getCurrentState(), gameState);
        assertEquals(gameState.getCurrentState(), turnState);
        assertEquals(turnState.getCurrentState(), choosePieceState);
        assertEquals(choosePieceState.getCurrentState(), waitingPieceState);

        serverGameLogic.received(deselectTSK, IDPlayerClient);
        serverGameLogic.received(forceContinueGame, IDPlayerClient);
        serverGameLogic.received(joinServer, IDPlayerClient);
        serverGameLogic.received(lobbyNotReady, IDPlayerClient);
        serverGameLogic.received(lobbyReady, IDPlayerClient);
        serverGameLogic.received(requestBriefing, IDPlayerClient);
        serverGameLogic.received(requestDie, IDPlayerClient);
        serverGameLogic.received(selectTSK, IDPlayerClient);
        serverGameLogic.received(startGame, IDPlayerClient);

        //tests if the server is in WaitingPiece
        assertEquals(serverGameLogic.getCurrentState(), gameState);
        assertEquals(gameState.getCurrentState(), turnState);
        assertEquals(turnState.getCurrentState(), choosePieceState);
        assertEquals(choosePieceState.getCurrentState(), waitingPieceState);
    }

    /**
     * Tests the transition IDPlayerClient WaitingPiece state to MovePiece state.
     * UC-ServerState-32
     */
    @Test
    public void testWaitingPieceToMovePiece() {
        game.setActiveColor(playerClientColor);
        playerClient.setHandCards(new ArrayList<>());
        turnState.setPlayer(playerClient);
        game.getBoard().getInfield()[22].clearOccupant();
        game.getBoard().setPieceOnBoard(29, pieceClient4);

        //sends the server in GameState
        serverGameLogic.setCurrentState(gameState);
        assertEquals(serverGameLogic.getCurrentState(), gameState);

        //sends the server in Turn
        gameState.setCurrentState(turnState);
        assertEquals(gameState.getCurrentState(), turnState);

        //sends the server in ChoosePiece
        assertEquals(turnState.getCurrentState(), rollDiceState);

        game.setDie(new Die(6));
        serverGameLogic.received(new RequestDieMessage(), IDPlayerClient);
        serverGameLogic.received(new AnimationEndMessage(), IDPlayerClient);

        //tests if the server is in WaitPiece
        assertEquals(serverGameLogic.getCurrentState(), gameState);
        assertEquals(gameState.getCurrentState(), turnState);
        assertEquals(turnState.getCurrentState(), choosePieceState);
        assertEquals(choosePieceState.getCurrentState(), waitingPieceState);

        serverGameLogic.received(new RequestMoveMessage(playerClient.getWaitingPiece()), IDPlayerClient);

        //tests if the server is in WaitPiece
        assertEquals(serverGameLogic.getCurrentState(), gameState);
        assertEquals(gameState.getCurrentState(), turnState);
        assertEquals(turnState.getCurrentState(), movePieceState);
    }

    /**
     * Tests the transition IDPlayerClient NoPiece state to SelectPiece state.
     * UC-ServerState-33
     */
    @Test
    public void testNoPieceToSelectPiece() {
        game.setActiveColor(playerClientColor);
        playerClient.setHandCards(new ArrayList<>());
        turnState.setPlayer(playerClient);

        //sends the server in GameState
        serverGameLogic.setCurrentState(gameState);
        assertEquals(serverGameLogic.getCurrentState(), gameState);

        //sends the server in Turn
        gameState.setCurrentState(turnState);
        assertEquals(gameState.getCurrentState(), turnState);

        //sends the server in ChoosePiece
        assertEquals(turnState.getCurrentState(), rollDiceState);

        game.setDie(new Die(4));
        serverGameLogic.received(new RequestDieMessage(), IDPlayerClient);
        serverGameLogic.received(new AnimationEndMessage(), IDPlayerClient);

        //tests if the server is in SelectPiece
        assertEquals(serverGameLogic.getCurrentState(), gameState);
        assertEquals(gameState.getCurrentState(), turnState);
        assertEquals(turnState.getCurrentState(), choosePieceState);
        assertEquals(choosePieceState.getCurrentState(), selectPieceState);
    }

    /**
     * Tests the transition IDPlayerClient NoPiece state to StartPiece state.
     * UC-ServerState-34
     */
    @Test
    public void testNoPieceToStartPiece() {
        game.setActiveColor(playerClientColor);
        playerClient.setHandCards(new ArrayList<>());
        turnState.setPlayer(playerClient);
        game.getBoard().getInfield()[22].clearOccupant();
        game.getBoard().setPieceOnBoard(30, pieceClient4);

        //sends the server in GameState
        serverGameLogic.setCurrentState(gameState);
        assertEquals(serverGameLogic.getCurrentState(), gameState);

        //sends the server in Turn
        gameState.setCurrentState(turnState);
        assertEquals(gameState.getCurrentState(), turnState);

        //sends the server in ChoosePiece
        assertEquals(turnState.getCurrentState(), rollDiceState);

        game.setDie(new Die(5));
        serverGameLogic.received(new RequestDieMessage(), IDPlayerClient);
        serverGameLogic.received(new AnimationEndMessage(), IDPlayerClient);

        //tests if the server is in StartPiece
        assertEquals(serverGameLogic.getCurrentState(), gameState);
        assertEquals(gameState.getCurrentState(), turnState);
        assertEquals(turnState.getCurrentState(), choosePieceState);
        assertEquals(choosePieceState.getCurrentState(), startPieceState);
    }

    /**
     * Tests that the system stays in the SelectPiece state.
     * UC-ServerState-35
     */
    @Test
    public void testStayInSelectPiece() {
        game.setActiveColor(playerClientColor);
        playerClient.setHandCards(new ArrayList<>());
        turnState.setPlayer(playerClient);

        //sends the server in GameState
        serverGameLogic.setCurrentState(gameState);
        assertEquals(serverGameLogic.getCurrentState(), gameState);

        //sends the server in Turn
        gameState.setCurrentState(turnState);
        assertEquals(gameState.getCurrentState(), turnState);

        //sends the server in ChoosePiece
        assertEquals(turnState.getCurrentState(), rollDiceState);

        game.setDie(new Die(4));
        serverGameLogic.received(new RequestDieMessage(), IDPlayerClient);
        serverGameLogic.received(new AnimationEndMessage(), IDPlayerClient);

        //sends the server in StartPiece
        assertEquals(choosePieceState.getCurrentState(), selectPieceState);

        serverGameLogic.received(deselectTSK, IDPlayerClient);
        serverGameLogic.received(forceContinueGame, IDPlayerClient);
        serverGameLogic.received(joinServer, IDPlayerClient);
        serverGameLogic.received(lobbyNotReady, IDPlayerClient);
        serverGameLogic.received(lobbyReady, IDPlayerClient);
        serverGameLogic.received(requestBriefing, IDPlayerClient);
        serverGameLogic.received(requestDie, IDPlayerClient);
        serverGameLogic.received(selectTSK, IDPlayerClient);
        serverGameLogic.received(startGame, IDPlayerClient);

        //tests if the server is in SelectPiece
        assertEquals(serverGameLogic.getCurrentState(), gameState);
        assertEquals(gameState.getCurrentState(), turnState);
        assertEquals(turnState.getCurrentState(), choosePieceState);
        assertEquals(choosePieceState.getCurrentState(), selectPieceState);
    }

    /**
     * Tests the transition IDPlayerClient SelectPiece state to MovePiece state.
     * UC-ServerState-36
     */
    @Test
    public void testSelectPieceToMovePiece() {
        game.setActiveColor(playerClientColor);
        playerClient.setHandCards(new ArrayList<>());
        turnState.setPlayer(playerClient);

        //sends the server in GameState
        serverGameLogic.setCurrentState(gameState);
        assertEquals(serverGameLogic.getCurrentState(), gameState);

        //sends the server in Turn
        gameState.setCurrentState(turnState);
        assertEquals(gameState.getCurrentState(), turnState);

        //tests the server in rollDiceState
        assertEquals(turnState.getCurrentState(), rollDiceState);

        game.setDie(new Die(4));
        serverGameLogic.received(new RequestDieMessage(), IDPlayerClient);
        serverGameLogic.received(new AnimationEndMessage(), IDPlayerClient);

        //tests if the server in selectPiece
        assertEquals(choosePieceState.getCurrentState(), selectPieceState);

        serverGameLogic.received(new RequestMoveMessage(pieceClient4), IDPlayerClient);

        //tests if the server is in movePiece-state
        assertEquals(serverGameLogic.getCurrentState(), gameState);
        assertEquals(gameState.getCurrentState(), turnState);
        assertEquals(turnState.getCurrentState(), movePieceState);
    }

    /**
     * Tests that the system stays in the StartPiece state.
     * UC-ServerState-37
     */
    @Test
    public void testStayInStartPiece() {
        game.setActiveColor(playerClientColor);
        playerClient.setHandCards(new ArrayList<>());
        turnState.setPlayer(playerClient);
        game.getBoard().getInfield()[22].clearOccupant();
        game.getBoard().setPieceOnBoard(30, pieceClient4);

        //sends the server in GameState
        serverGameLogic.setCurrentState(gameState);
        assertEquals(serverGameLogic.getCurrentState(), gameState);

        //sends the server in Turn
        gameState.setCurrentState(turnState);
        assertEquals(gameState.getCurrentState(), turnState);

        //tests the server in ChoosePiece
        assertEquals(turnState.getCurrentState(), rollDiceState);

        game.setDie(new Die(4));
        serverGameLogic.received(new RequestDieMessage(), IDPlayerClient);
        serverGameLogic.received(new AnimationEndMessage(), IDPlayerClient);

        //tests the server in StartPiece
        assertEquals(choosePieceState.getCurrentState(), startPieceState);

        serverGameLogic.received(deselectTSK, IDPlayerClient);
        serverGameLogic.received(forceContinueGame, IDPlayerClient);
        serverGameLogic.received(joinServer, IDPlayerClient);
        serverGameLogic.received(lobbyNotReady, IDPlayerClient);
        serverGameLogic.received(lobbyReady, IDPlayerClient);
        serverGameLogic.received(requestBriefing, IDPlayerClient);
        serverGameLogic.received(requestDie, IDPlayerClient);
        serverGameLogic.received(selectTSK, IDPlayerClient);
        serverGameLogic.received(startGame, IDPlayerClient);

        //tests if the server is in StartPiece
        assertEquals(serverGameLogic.getCurrentState(), gameState);
        assertEquals(gameState.getCurrentState(), turnState);
        assertEquals(turnState.getCurrentState(), choosePieceState);
        assertEquals(choosePieceState.getCurrentState(), startPieceState);
    }

    /**
     * Tests the transition IDPlayerClient StartPiece state to MovePiece state.
     * UC-ServerState-38
     */
    @Test
    public void testStartPieceToMovePiece() {
        game.setActiveColor(playerClientColor);
        playerClient.setHandCards(new ArrayList<>());
        turnState.setPlayer(playerClient);
        game.getBoard().getInfield()[22].clearOccupant();
        game.getBoard().setPieceOnBoard(30, pieceClient4);

        //sends the server in GameState
        serverGameLogic.setCurrentState(gameState);
        assertEquals(serverGameLogic.getCurrentState(), gameState);

        //sends the server in Turn
        gameState.setCurrentState(turnState);
        assertEquals(gameState.getCurrentState(), turnState);

        //sends the server in ChoosePiece
        assertEquals(turnState.getCurrentState(), rollDiceState);

        game.setDie(new Die(4));
        serverGameLogic.received(new RequestDieMessage(), IDPlayerClient);
        serverGameLogic.received(new AnimationEndMessage(), IDPlayerClient);

        //sends the server in StartPiece
        assertEquals(choosePieceState.getCurrentState(), startPieceState);

        serverGameLogic.received(new RequestMoveMessage(pieceClient4), IDPlayerClient);

        //tests if the server is in movePiece-state
        assertEquals(serverGameLogic.getCurrentState(), gameState);
        assertEquals(gameState.getCurrentState(), turnState);
        assertEquals(turnState.getCurrentState(), movePieceState);
    }
}
