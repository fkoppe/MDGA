package pp.mdga.client.clientState;

import org.junit.Before;
import org.junit.Test;
import pp.mdga.client.CeremonyState;
import pp.mdga.client.ClientGameLogic;
import pp.mdga.client.ClientSender;
import pp.mdga.client.DialogsState;
import pp.mdga.client.GameState;
import pp.mdga.client.InterruptState;
import pp.mdga.client.ceremonystate.PodiumState;
import pp.mdga.client.ceremonystate.StatisticsState;
import pp.mdga.client.dialogstate.LobbyState;
import pp.mdga.client.dialogstate.NetworkDialogState;
import pp.mdga.client.dialogstate.StartDialogState;
import pp.mdga.client.gamestate.AnimationState;
import pp.mdga.client.gamestate.DetermineStartPlayerState;
import pp.mdga.client.gamestate.SpectatorState;
import pp.mdga.client.gamestate.TurnState;
import pp.mdga.client.gamestate.WaitingState;
import pp.mdga.client.gamestate.determinestartplayerstate.RollRankingDiceState;
import pp.mdga.client.gamestate.determinestartplayerstate.WaitRankingState;
import pp.mdga.client.gamestate.turnstate.*;
import pp.mdga.client.gamestate.turnstate.choosepiecestate.*;
import pp.mdga.client.gamestate.turnstate.powercardstate.*;
import pp.mdga.client.gamestate.turnstate.powercardstate.ShieldState;
import pp.mdga.game.*;
import pp.mdga.game.card.*;
import pp.mdga.message.client.ClientMessage;
import pp.mdga.message.server.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * this test-class tests the testcases T170-T239
 */
public class ClientStateTest {

    //declare game here
    private Game game;

    //declare IP-Address
    private static final String IP = "168.192.88.101";

    //declare states here
    private AnimationState animation;
    private CeremonyState ceremony;
    private ChoosePieceState choosePiece;
    private ChoosePowerCardState choosePowerCard;
    private ClientGameLogic clientGameLogic;
    private DetermineStartPlayerState determineStartPlayer;
    private DialogsState dialogs;
    private GameState gameState;
    private InterruptState interrupt;
    private LobbyState lobby;
    private MovePieceState movePiece;
    private NetworkDialogState networkDialog;
    private NoPieceState noPiece;
    private PlayPowerCardState playPowerCard;
    private PodiumState podium;
    private PowerCardState powerCard;
    private RollDiceState rollDice;
    private RollRankingDiceState rollRankingDice;
    private SelectPieceState selectPiece;
    private ShieldState shield;
    private SpectatorState spectator;
    private StartDialogState startDialog;
    private StartPieceState startPiece;
    private StatisticsState statistics;
    private SwapState swap;
    private TurnState turnState;
    private WaitingState waiting;
    private WaitingPieceState waitingPiece;
    private WaitRankingState waitRanking;

    //declare server-messages here
    private ShutdownMessage shutdownMessage;
    private ActivePlayerMessage activePlayer;
    private AnyPieceMessage anyPiece;
    private BriefingMessage briefing;
    private CeremonyMessage ceremonyMessage;
    private DieMessage die;
    private DiceAgainMessage diceAgain;
    private DiceNowMessage diceNow;
    private EndOfTurnMessage endOfTurn;
    private LobbyAcceptMessage lobbyAccept;
    private LobbyDenyMessage lobbyDeny;
    private LobbyPlayerJoinedMessage lobbyPlayerJoin;
    private LobbyPlayerLeaveMessage lobbyPlayerLeave;
    private MoveMessage moveMessage;
    private NoTurnMessage noTurn;
    private PauseGameMessage interruptMessage;
    private PlayCardMessage playCardSwap;
    private PlayCardMessage playCardShield;
    private PlayCardMessage playCardTurbo;
    private PossibleCardsMessage possibleCard;
    private PossiblePieceMessage possiblePiece;
    private PossiblePieceMessage possiblePieceSwap;
    private PossiblePieceMessage possiblePieceShield;
    private RankingResponseMessage rankingResponse;
    private RankingRollAgainMessage rankingRollAgain;
    private ReconnectBriefingMessage reconnectBriefing;
    private ResumeGameMessage resumeGame;
    private ServerStartGameMessage startGame;
    private StartPieceMessage startPieceMessage;
    private UpdateReadyMessage updateReady;
    private UpdateTSKMessage updateTSK;
    private WaitPieceMessage waitPiece;
    private SpectatorMessage spectatorMessage;
    private SelectPieceMessage selectPieceMessage;

    private Color enemyColor;
    private int from;
    private String name;
    private Color color;
    private PowerCard swapCard;
    private PowerCard shieldCard;
    private PowerCard turboCard;

    //initialize ownPlayer
    private Player player;

    //initialize other player
    private Player enemy;

    //declare own piece
    private Piece ownPiece;

    //declare enemy piece
    private Piece enemyPiece;

    @Before
    public void setUp() {
        //initialize the clientGameLogic
        clientGameLogic = new ClientGameLogic(new ClientSender() {
            @Override
            public void send(ClientMessage msg) {

            }
        });

        //initialize the game
        game = clientGameLogic.getGame();

        //initialize the playerID
        from = 1234;
        name = "Daniel";
        color = Color.ARMY;

        //initialize a player
        player = new Player(name);
        player.setColor(color);
        player.initialize();
        game.addPlayer(1234, player);
        game.setActiveColor(color);

        ownPiece = player.getWaitingPiece();
        ownPiece.setState(PieceState.ACTIVE);
        player.removeWaitingPiece(ownPiece);
        game.getBoard().setPieceOnBoard(22, ownPiece);

        swapCard = new SwapCard();
        shieldCard = new ShieldCard();
        turboCard = new TurboCard();

        player.addHandCard(turboCard);
        player.addHandCard(swapCard);
        player.addHandCard(shieldCard);

        //declare ownPlayer

        //declare other player
        enemy = new Player(name);
        enemy.setColor(Color.CYBER);
        enemyColor = Color.CYBER;
        game.addPlayer(2345, enemy);
        enemy.initialize();

        enemyPiece = enemy.getWaitingPiece();
        enemyPiece.setState(PieceState.ACTIVE);
        enemy.removeWaitingPiece(enemyPiece);
        game.getBoard().setPieceOnBoard(33, enemyPiece);

        //sets the player in the game

        clientGameLogic.getGame().addPlayer(0, player);
        clientGameLogic.getGame().addPlayer(1, enemy);
        clientGameLogic.getGame().getBoard().setPieceOnBoard(15, ownPiece);
        clientGameLogic.getGame().getBoard().setPieceOnBoard(25, enemyPiece);

        //initialize the messages from the server
        shutdownMessage = new ShutdownMessage();
        activePlayer = new ActivePlayerMessage(color);
        anyPiece = new AnyPieceMessage();
        briefing = new BriefingMessage();
        ceremonyMessage = new CeremonyMessage();
        die = new DieMessage(3);
        diceAgain = new DiceAgainMessage();
        diceNow = new DiceNowMessage();
        endOfTurn = new EndOfTurnMessage();
        lobbyAccept = new LobbyAcceptMessage();
        lobbyDeny = new LobbyDenyMessage();
        lobbyPlayerJoin = new LobbyPlayerJoinedMessage(from, player, true);
        lobbyPlayerLeave = new LobbyPlayerLeaveMessage(from);
        moveMessage = new MoveMessage(ownPiece, false, 25);
        noTurn = new NoTurnMessage();
        interruptMessage = new PauseGameMessage();
        playCardSwap = new PlayCardMessage(swapCard, new ArrayList<>(List.of(ownPiece, enemyPiece)), 1);
        playCardShield = new PlayCardMessage(shieldCard, new ArrayList<>(List.of(ownPiece)), 1);
        playCardTurbo = new PlayCardMessage(turboCard, new ArrayList<>(List.of(ownPiece)), 1);
        possibleCard = new PossibleCardsMessage(new ArrayList<>(List.of(swapCard, shieldCard, turboCard)));
        possiblePieceShield = PossiblePieceMessage.shieldPossiblePieces(new ArrayList<>(List.of(ownPiece)));
        possiblePieceSwap = PossiblePieceMessage.swapPossiblePieces(new ArrayList<>(List.of(ownPiece)), new ArrayList<>(List.of(enemyPiece)));
        possiblePiece = new PossiblePieceMessage();
        rankingResponse = new RankingResponseMessage();
        rankingRollAgain = new RankingRollAgainMessage();
        reconnectBriefing = new ReconnectBriefingMessage(game);
        resumeGame = new ResumeGameMessage();
        startGame = new ServerStartGameMessage();
        startPieceMessage = new StartPieceMessage(ownPiece.getUuid(), 25);
        updateReady = new UpdateReadyMessage(2345, true);
        updateTSK = new UpdateTSKMessage(2345, Color.NAVY, false);
        waitPiece = new WaitPieceMessage(ownPiece.getUuid());
        interruptMessage = new PauseGameMessage();
        spectatorMessage = new SpectatorMessage();
        selectPieceMessage = new SelectPieceMessage(new ArrayList<>(List.of(ownPiece)), new ArrayList<>(List.of(false)), new ArrayList<>(List.of(25)));

        //initialize the client-state
        //sets the clientGameLogic-states
        dialogs = clientGameLogic.getDialogs();
        gameState = clientGameLogic.getGameState();
        ceremony = clientGameLogic.getCeremony();
        interrupt = clientGameLogic.getInterrupt();
        //sets the dialogs-states
        startDialog = dialogs.getStartDialog();
        networkDialog = dialogs.getNetworkDialog();
        lobby = dialogs.getLobby();
        //sets the ceremony-states
        podium = ceremony.getPodiumState();
        statistics = ceremony.getStatisticsState();
        //sets the game-states
        determineStartPlayer = gameState.getDetermineStartPlayer();
        waiting = gameState.getWaiting();
        animation = gameState.getAnimation();
        turnState = gameState.getTurn();
        spectator = gameState.getSpectator();
        //sets the determineStartPlayer-states
        rollRankingDice = determineStartPlayer.getRollRankingDice();
        waitRanking = determineStartPlayer.getWaitRanking();
        //sets the turn-states
        powerCard = turnState.getPowerCard();
        playPowerCard = turnState.getPlayPowerCard();
        rollDice = turnState.getRollDice();
        choosePiece = turnState.getChoosePiece();
        movePiece = turnState.getMovePiece();
        //sets the powerCard-states
        choosePowerCard = powerCard.getChoosePowerCard();
        shield = powerCard.getShield();
        swap = powerCard.getSwap();
        //sets the choosePiece-states
        noPiece = choosePiece.getNoPiece();
        waitingPiece = choosePiece.getWaitingPiece();
        selectPiece = choosePiece.getSelectPiece();
        startPiece = choosePiece.getStartPiece();
    }

    /**
     * UC-ClientState-01: Test the initial-state of the ClientState.
     */
    @Test
    public void testInitialStateClientState() {
        //tests if the clientGameLogic is in Dialogs
        assertEquals(clientGameLogic.getState(), dialogs);
        //tests if the statemachine is in StartDialog
        assertEquals(dialogs.getState(), startDialog);
    }

    /**
     * UC-ClientState-02: Test the transition from dialogs to the game state.
     */
    @Test
    public void testDialogsToGame() {
        //tests if the client is in Dialogs
        clientGameLogic.setState(dialogs);
        dialogs.setState(lobby);
        assertEquals(clientGameLogic.getState(), dialogs);
        assertEquals(dialogs.getState(), lobby);

        //sends all messages, which don't indicate a statechange
        clientGameLogic.received(activePlayer);
        clientGameLogic.received(anyPiece);
        clientGameLogic.received(briefing);
        clientGameLogic.received(ceremonyMessage);
        clientGameLogic.received(die);
        clientGameLogic.received(diceAgain);
        clientGameLogic.received(diceNow);
        clientGameLogic.received(endOfTurn);
        clientGameLogic.received(lobbyAccept);
        clientGameLogic.received(lobbyDeny);
        clientGameLogic.received(lobbyPlayerJoin);
        clientGameLogic.received(lobbyPlayerLeave);
        clientGameLogic.received(moveMessage);
        clientGameLogic.received(noTurn);
        clientGameLogic.received(interruptMessage);
        clientGameLogic.received(playCardSwap);
        clientGameLogic.received(playCardShield);
        clientGameLogic.received(playCardTurbo);
        clientGameLogic.received(possibleCard);
        clientGameLogic.received(possiblePiece);
        clientGameLogic.received(rankingResponse);
        clientGameLogic.received(rankingRollAgain);
        clientGameLogic.received(reconnectBriefing);
        clientGameLogic.received(resumeGame);
        clientGameLogic.received(startPieceMessage);
        clientGameLogic.received(waitPiece);
        clientGameLogic.received(spectatorMessage);
        clientGameLogic.received(selectPieceMessage);

        //tests if the client is still in lobby
        assertEquals(clientGameLogic.getState(), dialogs);
        assertEquals(dialogs.getState(), lobby);

        //sends the startGame-Message to the client
        clientGameLogic.received(startGame);

        //tests if the client is in the gameState after receiving the message
        assertEquals(clientGameLogic.getState(), gameState);

        //tests if the new State of the GameStateMachine is in DetermineStartPlayer
        assertEquals(gameState.getState(), determineStartPlayer);

        //tests if the new State of DetermineStartPlayer is RollRankingDice
        assertEquals(determineStartPlayer.getState(), rollRankingDice);
    }

    /**
     * UC-ClientState-03: Test the transition from dialogs to the end state of ClientState .
     */
    @Test
    public void testDialogsToClientStateEndState() {
    }

    /**
     * UC-ClientState-04: Test the transition from ClientGame to Ceremony.
     */
    @Test
    public void testClientGameToCeremony() {
        //sends the ClientGameLogic in GameState
        clientGameLogic.setState(gameState);
        assertEquals(clientGameLogic.getState(), gameState);

        //sends the gameState in wait
        gameState.setState(waiting);
        assertEquals(gameState.getState(), waiting);

        clientGameLogic.received(activePlayer);
        clientGameLogic.received(anyPiece);
        clientGameLogic.received(briefing);
        clientGameLogic.received(diceAgain);
        clientGameLogic.received(diceNow);
        clientGameLogic.received(endOfTurn);
        clientGameLogic.received(lobbyAccept);
        clientGameLogic.received(lobbyDeny);
        clientGameLogic.received(lobbyPlayerJoin);
        clientGameLogic.received(lobbyPlayerLeave);
        clientGameLogic.received(moveMessage);
        clientGameLogic.received(noTurn);
        clientGameLogic.received(playCardSwap);
        clientGameLogic.received(playCardShield);
        clientGameLogic.received(playCardTurbo);
        clientGameLogic.received(possibleCard);
        clientGameLogic.received(possiblePiece);
        clientGameLogic.received(reconnectBriefing);
        clientGameLogic.received(resumeGame);
        clientGameLogic.received(startGame);
        clientGameLogic.received(startPieceMessage);
        clientGameLogic.received(updateReady);
        clientGameLogic.received(updateTSK);
        clientGameLogic.received(waitPiece);
        clientGameLogic.received(spectatorMessage);
        clientGameLogic.received(selectPieceMessage);

        //tests if the client is still in game-state
        assertEquals(clientGameLogic.getState(), gameState);

        //sends the Ceremony-Message to the client
        clientGameLogic.received(ceremonyMessage);

        //tests if the client is in the ceremony after receiving the message
        assertEquals(clientGameLogic.getState(), ceremony);

        //tests if the state of ceremony is Podium
        assertEquals(ceremony.getState(), podium);
    }

    /**
     * UC-ClientState-05: Test the transition from ClientGame substates to Interrupt.
     */
    @Test
    public void testClientGameSubStatesToInterrupt() {
        //sends the ClientGameLogic in GameState
        clientGameLogic.setState(gameState);
        assertEquals(clientGameLogic.getState(), gameState);

        //sends the Ceremony-Message to the client
        clientGameLogic.received(interruptMessage);

        //tests if the client-automaton is in the interrupt state
        assertEquals(clientGameLogic.getState(), interrupt);
    }

    /**
     * UC-ClientState-06: Test the transition from the game state to dialogs.
     */
    @Test
    public void testGameToDialogs() {
        //sends the client into the GameState
        clientGameLogic.setState(gameState);
        assertEquals(clientGameLogic.getState(), gameState);

        //receives a GameClosedMessage
        clientGameLogic.received(shutdownMessage);

        assertEquals(clientGameLogic.getState(), dialogs);
        assertEquals(dialogs.getState(), startDialog);
    }

    /**
     * UC-ClientState-07: Test remaining in the Interrupt state.
     */
    @Test
    public void testStayInInterrupt() {
        //sends the ClientGameLogic in Interrupt
        clientGameLogic.setState(interrupt);
        assertEquals(clientGameLogic.getState(), interrupt);

        //sends all messages, which don't indicate a statechange
        clientGameLogic.received(activePlayer);
        clientGameLogic.received(anyPiece);
        clientGameLogic.received(briefing);
        clientGameLogic.received(ceremonyMessage);
        clientGameLogic.received(die);
        clientGameLogic.received(diceAgain);
        clientGameLogic.received(diceNow);
        clientGameLogic.received(endOfTurn);
        clientGameLogic.received(lobbyAccept);
        clientGameLogic.received(lobbyDeny);
        clientGameLogic.received(lobbyPlayerJoin);
        clientGameLogic.received(lobbyPlayerLeave);
        clientGameLogic.received(moveMessage);
        clientGameLogic.received(noTurn);
        clientGameLogic.received(interruptMessage);
        clientGameLogic.received(playCardSwap);
        clientGameLogic.received(playCardShield);
        clientGameLogic.received(playCardTurbo);
        clientGameLogic.received(possibleCard);
        clientGameLogic.received(possiblePiece);
        clientGameLogic.received(rankingResponse);
        clientGameLogic.received(rankingRollAgain);
        clientGameLogic.received(reconnectBriefing);
        clientGameLogic.received(startGame);
        clientGameLogic.received(startPieceMessage);
        clientGameLogic.received(updateReady);
        clientGameLogic.received(updateTSK);
        clientGameLogic.received(waitPiece);
        clientGameLogic.received(spectatorMessage);
        clientGameLogic.received(selectPieceMessage);

        //tests if the client is in the interrupt
        assertEquals(clientGameLogic.getState(), interrupt);
    }

    /**
     * UC-ClientState-08: Test the transition from ClientInterrupt to the game state.
     */
    @Test
    public void testClientInterruptToGame() {
        //sends the ClientGameLogic in Interrupt
        clientGameLogic.setState(interrupt);
        assertEquals(clientGameLogic.getState(), interrupt);
        interrupt.setPreviousState(gameState);

        //sends the continue message
        clientGameLogic.received(resumeGame);

        //tests if the client is in the game
        assertEquals(clientGameLogic.getState(), gameState);
    }

    /**
     * UC-ClientState-09: Test the transition from Interrupt to dialogs.
     */
    @Test
    public void testInterruptToDialogs() {
        //sends the ClientGameLogic in Interrupt
        clientGameLogic.setState(interrupt);
        assertEquals(clientGameLogic.getState(), interrupt);

        //send the server-closed
        clientGameLogic.received(shutdownMessage);

        //tests if the client is in the startDialog
        assertEquals(clientGameLogic.getState(), dialogs);
        assertEquals(dialogs.getState(), startDialog);
    }

    /**
     * UC-ClientState-10: Test the transition from Ceremony to dialogs.
     */
    @Test
    public void testCeremonyToDialogs() {
        //send the client in the ceremony
        clientGameLogic.setState(ceremony);
        assertEquals(clientGameLogic.getState(), ceremony);

        //sends the ceremony machine in the statistics
        ceremony.setState(statistics);
        assertEquals(ceremony.getState(), statistics);

        //simulate the button next
        clientGameLogic.selectNext();

        assertEquals(clientGameLogic.getState(), dialogs);
        assertEquals(dialogs.getState(), startDialog);
    }

    /**
     * UC-ClientState-11: Test the transition from StartDialog to NetworkDialog1.
     */
    @Test
    public void testStartDialogToNetworkDialog1() {

        // sends the clientGameLogic in StartDialog
        clientGameLogic.setState(dialogs);
        assertEquals(clientGameLogic.getState(), dialogs);

        //sends the DialogsState in StartDialog
        dialogs.setState(startDialog);
        assertEquals(dialogs.getState(), startDialog);

        //simulate the button-push for the join-Button
        clientGameLogic.selectName(name);

        //tests if the client is in the network-dialog
        assertEquals(clientGameLogic.getState(), dialogs);
        assertEquals(dialogs.getState(), networkDialog);

        //tests if the client is not host
        assertFalse(clientGameLogic.isHost());
    }

    /**
     * UC-ClientState-12: Test the transition from StartDialog to NetworkDialog2.
     */
    @Test
    public void testStartDialogToNetworkDialog2() {

        // sends the clientGameLogic in StartDialog
        clientGameLogic.setState(dialogs);
        assertEquals(clientGameLogic.getState(), dialogs);

        //sends the DialogsState in StartDialog
        dialogs.setState(startDialog);
        assertEquals(dialogs.getState(), startDialog);

        //simulate the button-push for the host-Button
        clientGameLogic.selectName(name);

        //tests if the client is in the network-dialog
        assertEquals(clientGameLogic.getState(), dialogs);
        assertEquals(dialogs.getState(), networkDialog);

        clientGameLogic.received(new LobbyAcceptMessage(1234));

        //tests if the client is host
        assertTrue(clientGameLogic.isHost());
    }

    /**
     * UC-ClientState-13: Test the transition from StartDialog to the dialogs end state.
     */
    @Test
    public void testStartDialogToDialogsEndState() {
    }

    /**
     * UC-ClientState-14: Test the transitions from NetworkDialog to StartDialog.
     */
    @Test
    public void testNetworkDialogToStartDialog() {
        // sends the clientGameLogic in StartDialog
        clientGameLogic.setState(dialogs);
        assertEquals(clientGameLogic.getState(), dialogs);

        //sends the DialogsState in NetworkDialog
        dialogs.setState(networkDialog);
        assertEquals(dialogs.getState(), networkDialog);

        //simulate the leave-Btn push
        clientGameLogic.selectLeave();

        //tests if the client is in startDialog
        assertEquals(clientGameLogic.getState(), dialogs);
        assertEquals(dialogs.getState(), startDialog);
    }

    /**
     * UC-ClientState-15: Test the transition from NetworkDialog to NetworkDialog1.
     */
    @Test
    public void testNetworkDialogToNetworkDialog1() {
        // sends the clientGameLogic in StartDialog
        clientGameLogic.setState(dialogs);
        assertEquals(clientGameLogic.getState(), dialogs);

        //sends the DialogsState in NetworkDialog
        dialogs.setState(networkDialog);
        assertEquals(dialogs.getState(), networkDialog);

        //simulate all input, that don't indicate a state-change
        clientGameLogic.selectPiece(null);
        clientGameLogic.selectCard(null);
        clientGameLogic.selectTsk(color);
        clientGameLogic.selectDice();
        clientGameLogic.selectName(name);
        clientGameLogic.selectReady(true);
        clientGameLogic.selectHost(name);
        clientGameLogic.selectAnimationEnd();
        clientGameLogic.selectStart();

        //tests if the client is still in the network-dialog
        assertEquals(clientGameLogic.getState(), dialogs);
        assertEquals(dialogs.getState(), networkDialog);
    }

    /**
     * UC-ClientState-16: Test the transition from NetworkDialog to NetworkDialog2.
     */
    @Test
    public void testNetworkDialogToNetworkDialog2() {
    }

    /**
     * UC-ClientState-17: Test the transition from NetworkDialog to Lobby.
     */
    @Test
    public void testNetworkDialogToLobby() {
        // sends the clientGameLogic in StartDialog
        clientGameLogic.setState(dialogs);
        assertEquals(clientGameLogic.getState(), dialogs);

        //sends the DialogsState in NetworkDialog
        dialogs.setState(networkDialog);
        assertEquals(dialogs.getState(), networkDialog);

        //simulate connect to server with send lobby request
        clientGameLogic.selectJoin("IP");
        clientGameLogic.received(new LobbyAcceptMessage(1234));

        assertEquals(clientGameLogic.getState(), dialogs);
        assertEquals(dialogs.getState(), lobby);
    }

    /**
     * UC-ClientState-18: Test the transition from Lobby to StartDialog.
     */
    @Test
    public void testLobbyToStartDialog() {
        // sends the clientGameLogic in StartDialog
        clientGameLogic.setState(dialogs);
        assertEquals(clientGameLogic.getState(), dialogs);

        //sends the DialogsState in Lobby
        dialogs.setState(lobby);
        assertEquals(dialogs.getState(), lobby);

        //simulate the leave-Btn
        clientGameLogic.selectLeave();

        //tests if the client is in the Start-Dialog
        assertEquals(clientGameLogic.getState(), dialogs);
        assertEquals(dialogs.getState(), startDialog);
    }

    /**
     * UC-ClientState-19: Test remaining in the Lobby state.
     */
    @Test
    public void testStayInLobby() {
        // sends the clientGameLogic in StartDialog
        clientGameLogic.setState(dialogs);
        assertEquals(clientGameLogic.getState(), dialogs);

        //sends the DialogsState in Lobby
        dialogs.setState(lobby);
        assertEquals(dialogs.getState(), lobby);

        //simulate all input that don't indicate a state-change
        clientGameLogic.selectPiece(null);
        clientGameLogic.selectCard(null);
        clientGameLogic.selectTsk(color);
        clientGameLogic.selectDice();
        clientGameLogic.selectName(name);
        clientGameLogic.selectReady(true);
        clientGameLogic.selectHost(name);
        clientGameLogic.selectJoin(name);
        clientGameLogic.selectAnimationEnd();

        //sends all messages, which don't indicate a statechange
        clientGameLogic.received(activePlayer);
        clientGameLogic.received(anyPiece);
        clientGameLogic.received(briefing);
        clientGameLogic.received(ceremonyMessage);
        clientGameLogic.received(die);
        clientGameLogic.received(diceAgain);
        clientGameLogic.received(diceNow);
        clientGameLogic.received(endOfTurn);
        clientGameLogic.received(lobbyAccept);
        clientGameLogic.received(lobbyDeny);
        clientGameLogic.received(lobbyPlayerJoin);
        clientGameLogic.received(lobbyPlayerLeave);
        clientGameLogic.received(moveMessage);
        clientGameLogic.received(noTurn);
        clientGameLogic.received(interruptMessage);
        clientGameLogic.received(playCardSwap);
        clientGameLogic.received(playCardShield);
        clientGameLogic.received(playCardTurbo);
        clientGameLogic.received(possibleCard);
        clientGameLogic.received(possiblePiece);
        clientGameLogic.received(rankingResponse);
        clientGameLogic.received(rankingRollAgain);
        clientGameLogic.received(reconnectBriefing);
        clientGameLogic.received(resumeGame);
        clientGameLogic.received(startPieceMessage);
        clientGameLogic.received(updateReady);
        clientGameLogic.received(updateTSK);
        clientGameLogic.received(waitPiece);
        clientGameLogic.received(spectatorMessage);
        clientGameLogic.received(selectPieceMessage);

        assertEquals(clientGameLogic.getState(), dialogs);
        assertEquals(dialogs.getState(), lobby);
    }

    /**
     * UC-ClientState-20: Test the transition from Lobby to RollRankingDice.
     */
    @Test
    public void testLobbyToRollRankingDice() {
        //sends the clientStatemachine in Dialogs
        clientGameLogic.setState(dialogs);
        assertEquals(clientGameLogic.getState(), dialogs);

        //sends the clientStatemachine in Lobby
        dialogs.setState(lobby);
        assertEquals(dialogs.getState(), lobby);

        //sends the message to start the game
        clientGameLogic.received(startGame);

        //tests if the clientStateMachine is in the GameState
        assertEquals(clientGameLogic.getState(), gameState);

        //tests if the clientStateMachine is in the DetermineStartPlayer
        assertEquals(gameState.getState(), determineStartPlayer);

        //tests if the clientStateMachine is in the RollRankingDice
        assertEquals(determineStartPlayer.getState(), rollRankingDice);
    }

    /**
     * UC-ClientState-21: Test the transition from DetermineStartPlayer to Wait.
     */
    @Test
    public void testDetermineStartPlayerToWait() {
        //sends the clientGameLogic into the gameState
        clientGameLogic.setState(gameState);
        assertEquals(clientGameLogic.getState(), gameState);

        //sends the gameState into the DetermineStartPlayer-state
        gameState.setState(determineStartPlayer);
        assertEquals(gameState.getState(), determineStartPlayer);

        //sends the determineStartPlayer into WaitRanking
        determineStartPlayer.setState(waitRanking);
        assertEquals(determineStartPlayer.getState(), waitRanking);

        //sends the message, that indicate a statechange to wait
        clientGameLogic.received(new ActivePlayerMessage(enemyColor));
        clientGameLogic.selectAnimationEnd();

        //tests if the client is in the intro
        assertEquals(clientGameLogic.getState(), gameState);
        assertEquals(gameState.getState(), determineStartPlayer);
        assertEquals(determineStartPlayer.getState(), determineStartPlayer.getIntro());

        int animationCounter = determineStartPlayer.getIntro().getAnimationCounter();
        for (int i = 0; i < animationCounter; i++) {
            clientGameLogic.selectAnimationEnd();
        }

        //tests if the client is in the Wait-State
        assertEquals(clientGameLogic.getState(), gameState);
        assertEquals(gameState.getState(), waiting);
    }

    /**
     * UC-ClientState-22: Test the transition from Wait to Animation.
     */
    @Test
    public void testWaitToAnimation() {
        //sends the ClientGameLogic in GameState
        clientGameLogic.setState(gameState);
        assertEquals(clientGameLogic.getState(), gameState);

        //sends the client in WaitState
        gameState.setState(waiting);
        assertEquals(gameState.getState(), waiting);

        //sends the moveMessage
        clientGameLogic.received(moveMessage);

        //tests if a piece is moved,that the client goes into Animation
        assertEquals(clientGameLogic.getState(), gameState);
        assertEquals(gameState.getState(), animation);

        //sends the client in WaitState
        gameState.setState(waiting);
        assertEquals(gameState.getState(), waiting);

        //sends the playTurboCard-message
        clientGameLogic.received(playCardTurbo);

        //tests if a powerCard is played,that the client goes into Animation
        assertEquals(clientGameLogic.getState(), gameState);
        assertEquals(gameState.getState(), animation);

        //sends the client in WaitState
        gameState.setState(waiting);
        assertEquals(gameState.getState(), waiting);

        //sends the activePlayer-message
        clientGameLogic.received(moveMessage);

        //tests if a die is rolled,that the client goes into Animation
        assertEquals(clientGameLogic.getState(), gameState);
        assertEquals(gameState.getState(), animation);
    }

    /**
     * UC-ClientState-23: Test the transition from Wait to Turn.
     */
    @Test
    public void testWaitToTurn() {
        //sends client in gameState
        game.setActiveColor(enemyColor);
        clientGameLogic.setState(gameState);
        assertEquals(clientGameLogic.getState(), gameState);

        //sends the gameState in the Waiting
        gameState.setState(waiting);
        assertEquals(gameState.getState(), waiting);

        //the client receives the message ActivePlayer
        clientGameLogic.received(activePlayer);

        //tests if the client is in GameState
        assertEquals(clientGameLogic.getState(), gameState);

        //tests if Client is in Turn
        assertEquals(gameState.getState(), turnState);

        //tests if Client is in PowerCard
        assertEquals(turnState.getState(), powerCard);

        //tests if Client is in ChoosePowerCard
        assertEquals(powerCard.getState(), choosePowerCard);
    }

    /**
     * UC-ClientState-24: Test the transition from Wait to GameEndState.
     */
    @Test
    public void testWaitToGameEndState() {
        //sends client in gameState
        clientGameLogic.setState(gameState);
        assertEquals(clientGameLogic.getState(), gameState);

        //sends the gameState in the Waiting
        gameState.setState(waiting);
        assertEquals(gameState.getState(), waiting);

        //send the ceremony-Message
        clientGameLogic.received(ceremonyMessage);

        //tests if the client is in the ceremony
        assertEquals(clientGameLogic.getState(), ceremony);
        assertEquals(ceremony.getState(), podium);
    }

    /**
     * UC-ClientState-25: Test the transition from Turn substates to GameEndState.
     */
    @Test
    public void testTurnSubStatesToGameEndState() {
        //sends the clientGameLogic in gameState
        clientGameLogic.setState(gameState);
        assertEquals(clientGameLogic.getState(), gameState);

        //sends the gameState in turnState
        gameState.setState(turnState);
        assertEquals(gameState.getState(), turnState);

        //sends the turnState in movePiece
        turnState.setState(movePiece);
        assertEquals(turnState.getState(), movePiece);

        //tests all messages, that don't indicate a state-change
        clientGameLogic.received(activePlayer);
        clientGameLogic.received(anyPiece);
        clientGameLogic.received(briefing);
        clientGameLogic.received(die);
        clientGameLogic.received(diceAgain);
        clientGameLogic.received(lobbyAccept);
        clientGameLogic.received(lobbyDeny);
        clientGameLogic.received(lobbyPlayerJoin);
        clientGameLogic.received(lobbyPlayerLeave);
        clientGameLogic.received(moveMessage);
        clientGameLogic.received(noTurn);
        clientGameLogic.received(playCardSwap);
        clientGameLogic.received(playCardShield);
        clientGameLogic.received(playCardTurbo);
        clientGameLogic.received(possibleCard);
        clientGameLogic.received(possiblePiece);
        clientGameLogic.received(rankingResponse);
        clientGameLogic.received(rankingRollAgain);
        clientGameLogic.received(reconnectBriefing);
        clientGameLogic.received(resumeGame);
        clientGameLogic.received(startGame);
        clientGameLogic.received(startPieceMessage);
        clientGameLogic.received(updateReady);
        clientGameLogic.received(updateTSK);
        clientGameLogic.received(waitPiece);
        clientGameLogic.received(selectPieceMessage);

        //tests if the client is still in MovePiece
        assertEquals(clientGameLogic.getState(), gameState);
        assertEquals(gameState.getState(), turnState);
        assertEquals(turnState.getState(), movePiece);

        //sends the spectator message
        clientGameLogic.received(ceremonyMessage);

        //tests if the client is in spectator-state
        assertEquals(clientGameLogic.getState(), ceremony);
        assertEquals(ceremony.getState(), podium);
    }

    /**
     * UC-ClientState-26: Test the transition from Turn substates to Wait.
     */
    @Test
    public void testTurnSubStatesToWait() {
        //sends the clientGameLogic in gameState
        clientGameLogic.setState(gameState);
        assertEquals(clientGameLogic.getState(), gameState);

        //sends the gameState in turnState
        gameState.setState(turnState);
        assertEquals(gameState.getState(), turnState);

        //sends the turnState in movePiece
        turnState.setState(movePiece);
        assertEquals(turnState.getState(), movePiece);

        //tests all messages, that don't indicate a state-change
        clientGameLogic.received(anyPiece);
        clientGameLogic.received(briefing);
        clientGameLogic.received(die);
        clientGameLogic.received(lobbyAccept);
        clientGameLogic.received(lobbyDeny);
        clientGameLogic.received(lobbyPlayerJoin);
        clientGameLogic.received(lobbyPlayerLeave);
        clientGameLogic.received(noTurn);
        clientGameLogic.received(playCardSwap);
        clientGameLogic.received(playCardShield);
        clientGameLogic.received(playCardTurbo);
        clientGameLogic.received(possibleCard);
        clientGameLogic.received(possiblePiece);
        clientGameLogic.received(rankingResponse);
        clientGameLogic.received(rankingRollAgain);
        clientGameLogic.received(reconnectBriefing);
        clientGameLogic.received(resumeGame);
        clientGameLogic.received(startGame);
        clientGameLogic.received(startPieceMessage);
        clientGameLogic.received(updateReady);
        clientGameLogic.received(updateTSK);
        clientGameLogic.received(waitPiece);
        clientGameLogic.received(selectPieceMessage);

        //tests if the client is still in MovePiece
        assertEquals(clientGameLogic.getState(), gameState);
        assertEquals(gameState.getState(), turnState);
        assertEquals(turnState.getState(), movePiece);

        //sends the endOfTurn message
        clientGameLogic.received(endOfTurn);

        //tests if the client is in waiting-state
        assertEquals(clientGameLogic.getState(), gameState);
        assertEquals(gameState.getState(), waiting);

        /*
         * tests thr transition from choosePiece to Wait
         */

        //sends the clientGameLogic in gameState
        clientGameLogic.setState(gameState);
        assertEquals(clientGameLogic.getState(), gameState);

        //sends the gameState in turnState
        gameState.setState(turnState);
        assertEquals(gameState.getState(), turnState);

        //sends the turnState in choosePiece
        turnState.setState(choosePiece);
        assertEquals(turnState.getState(), choosePiece);

        //sends the choosePiece in NoPiece
        choosePiece.setState(noPiece);
        assertEquals(choosePiece.getState(), noPiece);

        //tests all messages, that don't indicate a state-change
        clientGameLogic.received(activePlayer);
        clientGameLogic.received(anyPiece);
        clientGameLogic.received(briefing);
        clientGameLogic.received(die);
        clientGameLogic.received(lobbyAccept);
        clientGameLogic.received(lobbyDeny);
        clientGameLogic.received(lobbyPlayerJoin);
        clientGameLogic.received(lobbyPlayerLeave);
        clientGameLogic.received(moveMessage);
        clientGameLogic.received(noTurn);
        clientGameLogic.received(playCardSwap);
        clientGameLogic.received(playCardShield);
        clientGameLogic.received(playCardTurbo);
        clientGameLogic.received(possibleCard);
        clientGameLogic.received(possiblePiece);
        clientGameLogic.received(rankingResponse);
        clientGameLogic.received(rankingRollAgain);
        clientGameLogic.received(reconnectBriefing);
        clientGameLogic.received(resumeGame);
        clientGameLogic.received(startGame);
        clientGameLogic.received(updateReady);
        clientGameLogic.received(updateTSK);
        clientGameLogic.received(spectatorMessage);

        //tests if the client is still in MovePiece
        assertEquals(clientGameLogic.getState(), gameState);
        assertEquals(gameState.getState(), turnState);
        assertEquals(turnState.getState(), choosePiece);
        assertEquals(choosePiece.getState(), noPiece);

        //sends the endOfTurn message
        clientGameLogic.received(endOfTurn);

        //tests if the client is in waiting-state
        assertEquals(clientGameLogic.getState(), gameState);
        assertEquals(gameState.getState(), waiting);

        /*
         * tests the transition from RollDice to Wait
         */

        //sends the clientGameLogic in gameState
        clientGameLogic.setState(gameState);
        assertEquals(clientGameLogic.getState(), gameState);

        //sends the gameState in turnState
        gameState.setState(turnState);
        assertEquals(gameState.getState(), turnState);

        //sends the turnState in movePiece
        turnState.setState(movePiece);
        assertEquals(turnState.getState(), movePiece);

        //tests all messages, that don't indicate a state-change
        clientGameLogic.received(anyPiece);
        clientGameLogic.received(briefing);
        clientGameLogic.received(lobbyAccept);
        clientGameLogic.received(lobbyDeny);
        clientGameLogic.received(lobbyPlayerJoin);
        clientGameLogic.received(lobbyPlayerLeave);
        clientGameLogic.received(noTurn);
        clientGameLogic.received(playCardSwap);
        clientGameLogic.received(playCardShield);
        clientGameLogic.received(playCardTurbo);
        clientGameLogic.received(possibleCard);
        clientGameLogic.received(possiblePiece);
        clientGameLogic.received(rankingResponse);
        clientGameLogic.received(rankingRollAgain);
        clientGameLogic.received(reconnectBriefing);
        clientGameLogic.received(resumeGame);
        clientGameLogic.received(startGame);
        clientGameLogic.received(startPieceMessage);
        clientGameLogic.received(updateReady);
        clientGameLogic.received(updateTSK);
        clientGameLogic.received(waitPiece);
        clientGameLogic.received(selectPieceMessage);

        //tests if the client is still in MovePiece
        assertEquals(clientGameLogic.getState(), gameState);
        assertEquals(gameState.getState(), turnState);
        assertEquals(turnState.getState(), movePiece);

        //sends the endOfTurn message
        clientGameLogic.received(endOfTurn);

        //tests if the client is in waiting-state
        assertEquals(clientGameLogic.getState(), gameState);
        assertEquals(gameState.getState(), waiting);
    }

    /**
     * UC-ClientState-27: Test the transition from Turn substates to Spectator.
     */
    @Test
    public void testTurnSubStatesToSpectator() {
        //sends the clientGameLogic in gameState
        clientGameLogic.setState(gameState);
        assertEquals(clientGameLogic.getState(), gameState);

        //sends the gameState in turnState
        gameState.setState(turnState);
        assertEquals(gameState.getState(), turnState);

        //sends the turnState in movePiece
        turnState.setState(movePiece);
        assertEquals(turnState.getState(), movePiece);

        //tests all messages, that don't indicate a state-change
        clientGameLogic.received(activePlayer);
        clientGameLogic.received(anyPiece);
        clientGameLogic.received(briefing);
        clientGameLogic.received(die);
        clientGameLogic.received(diceAgain);
        clientGameLogic.received(lobbyAccept);
        clientGameLogic.received(lobbyDeny);
        clientGameLogic.received(lobbyPlayerJoin);
        clientGameLogic.received(lobbyPlayerLeave);
        clientGameLogic.received(moveMessage);
        clientGameLogic.received(noTurn);
        clientGameLogic.received(playCardSwap);
        clientGameLogic.received(playCardShield);
        clientGameLogic.received(playCardTurbo);
        clientGameLogic.received(possibleCard);
        clientGameLogic.received(possiblePiece);
        clientGameLogic.received(rankingResponse);
        clientGameLogic.received(rankingRollAgain);
        clientGameLogic.received(reconnectBriefing);
        clientGameLogic.received(resumeGame);
        clientGameLogic.received(startGame);
        clientGameLogic.received(startPieceMessage);
        clientGameLogic.received(updateReady);
        clientGameLogic.received(updateTSK);
        clientGameLogic.received(waitPiece);
        clientGameLogic.received(selectPieceMessage);

        //tests if the client is still in MovePiece
        assertEquals(clientGameLogic.getState(), gameState);
        assertEquals(gameState.getState(), turnState);
        assertEquals(turnState.getState(), movePiece);

        //sends the spectator message
        clientGameLogic.received(spectatorMessage);

        //tests if the client is in spectator-state
        assertEquals(clientGameLogic.getState(), gameState);
        assertEquals(gameState.getState(), spectator);
    }

    /**
     * UC-ClientState-28: Test the transition from Spectator to GameEndState.
     */
    @Test
    public void testSpectatorToGameEndState() {
        //sends the client in the gameState
        clientGameLogic.setState(gameState);
        assertEquals(clientGameLogic.getState(), gameState);

        //sends the client in the spectator-state
        gameState.setState(spectator);
        assertEquals(gameState.getState(), spectator);

        //sends all messages that indicate no state-change
        clientGameLogic.received(anyPiece);
        clientGameLogic.received(briefing);
        clientGameLogic.received(endOfTurn);
        clientGameLogic.received(lobbyAccept);
        clientGameLogic.received(lobbyDeny);
        clientGameLogic.received(lobbyPlayerJoin);
        clientGameLogic.received(lobbyPlayerLeave);
        clientGameLogic.received(noTurn);
        clientGameLogic.received(possibleCard);
        clientGameLogic.received(possiblePiece);
        clientGameLogic.received(rankingResponse);
        clientGameLogic.received(rankingRollAgain);
        clientGameLogic.received(reconnectBriefing);
        clientGameLogic.received(resumeGame);
        clientGameLogic.received(startGame);
        clientGameLogic.received(startPieceMessage);
        clientGameLogic.received(updateReady);
        clientGameLogic.received(updateTSK);
        clientGameLogic.received(waitPiece);
        clientGameLogic.received(spectatorMessage);
        clientGameLogic.received(selectPieceMessage);

        //tests if the client is still in spectator
        assertEquals(clientGameLogic.getState(), gameState);
        assertEquals(gameState.getState(), spectator);

        //sends the gameEnd-Message
        clientGameLogic.received(ceremonyMessage);

        //tests if the client is in the podium-state
        assertEquals(clientGameLogic.getState(), ceremony);
        assertEquals(ceremony.getState(), podium);
    }

    /**
     * UC-ClientState-29: Test the transition from PowerCard substates to PlayPowerCard.
     */
    @Test
    public void testPowerCardSubStatesToPlayPowerCard() {
        /*
         * sends the client in the swap-state
         */
        //sends the ClientGameLogic in GameState
        clientGameLogic.setState(gameState);
        assertEquals(clientGameLogic.getState(), gameState);

        //sends the gameState in the Turn
        gameState.setState(turnState);
        assertEquals(gameState.getState(), turnState);

        //sends the turnState in PlayPowerCard
        turnState.setState(powerCard);
        assertEquals(turnState.getState(), powerCard);

        //sends the client in the ChoosePowerCard
        powerCard.setState(swap);
        assertEquals(powerCard.getState(), swap);

        //sends the playCardMessage
        swap.setPossibleEnemyPieces(new ArrayList<>(List.of(enemyPiece)));
        swap.setPossibleOwnPieces(new ArrayList<>(List.of(ownPiece)));
        clientGameLogic.received(playCardSwap);

        //tests if the client is in playPowerCard
        assertEquals(clientGameLogic.getState(), gameState);
        assertEquals(gameState.getState(), turnState);
        assertEquals(turnState.getState(), playPowerCard);

        /*
         * send the client in the shield-state
         */
        //sends the ClientGameLogic in GameState
        clientGameLogic.setState(gameState);
        assertEquals(clientGameLogic.getState(), gameState);

        //sends the gameState in the Turn
        gameState.setState(turnState);
        assertEquals(gameState.getState(), turnState);

        //sends the turnState in PlayPowerCard
        turnState.setState(powerCard);
        assertEquals(turnState.getState(), powerCard);

        shield.setPossiblePieces(new ArrayList<>(List.of(ownPiece)));

        //sends the client in the ChoosePowerCard
        powerCard.setState(powerCard.getShield());
        assertEquals(powerCard.getState(), shield);

        //sends the client in shieldState

        //sends the playCardMessage
        clientGameLogic.received(playCardShield);

        //tests if the client is in playPowerCard
        assertEquals(clientGameLogic.getState(), gameState);
        assertEquals(gameState.getState(), turnState);
        assertEquals(turnState.getState(), playPowerCard);
    }

    /**
     * UC-ClientState-30: Test the transition from PowerCard substates to RollDice.
     * <p>
     * tests the transition from ChoosePowerCard from PowerCard to RollDice
     */
    @Test
    public void testPowerCardSubStatesToRollDice() {
        //sends the ClientGameLogic in GameState
        clientGameLogic.setState(gameState);
        assertEquals(clientGameLogic.getState(), gameState);

        //sends the gameState in the Turn
        gameState.setState(turnState);
        assertEquals(gameState.getState(), turnState);

        //sends the turnState in PlayPowerCard
        turnState.setState(powerCard);
        assertEquals(turnState.getState(), powerCard);

        //sends the client in the ChoosePowerCard
        powerCard.setState(choosePowerCard);
        assertEquals(powerCard.getState(), choosePowerCard);

        //sends RollDiceMessage
        clientGameLogic.received(diceNow);

        //tests if the client is in rollDice
        assertEquals(clientGameLogic.getState(), gameState);
        assertEquals(gameState.getState(), turnState);
        assertEquals(turnState.getState(), rollDice);
    }

    /**
     * UC-ClientState-31: Test staying in the PlayPowerCard state.
     */
    @Test
    public void testStayInPlayPowerCard() {
        //sends the ClientGameLogic in GameState
        clientGameLogic.setState(gameState);
        assertEquals(clientGameLogic.getState(), gameState);

        //sends the gameState in the Turn
        gameState.setState(turnState);
        assertEquals(gameState.getState(), turnState);

        //sends the turnState in PlayPowerCard
        playPowerCard.setPlayCard(playCardShield);
        turnState.setState(turnState.getPlayPowerCard());
        assertEquals(turnState.getState(), playPowerCard);

        //sends all messages that don't indicate a state-change
        clientGameLogic.received(activePlayer);
        clientGameLogic.received(anyPiece);
        clientGameLogic.received(briefing);
        clientGameLogic.received(die);
        clientGameLogic.received(endOfTurn);
        clientGameLogic.received(lobbyAccept);
        clientGameLogic.received(lobbyDeny);
        clientGameLogic.received(lobbyPlayerJoin);
        clientGameLogic.received(lobbyPlayerLeave);
        clientGameLogic.received(moveMessage);
        clientGameLogic.received(noTurn);
        clientGameLogic.received(playCardSwap);
        clientGameLogic.received(playCardShield);
        clientGameLogic.received(playCardTurbo);
        clientGameLogic.received(possibleCard);
        clientGameLogic.received(possiblePiece);
        clientGameLogic.received(rankingResponse);
        clientGameLogic.received(rankingRollAgain);
        clientGameLogic.received(reconnectBriefing);
        clientGameLogic.received(resumeGame);
        clientGameLogic.received(startGame);
        clientGameLogic.received(startPieceMessage);
        clientGameLogic.received(updateReady);
        clientGameLogic.received(updateTSK);
        clientGameLogic.received(waitPiece);
        clientGameLogic.received(spectatorMessage);
        clientGameLogic.received(selectPieceMessage);

        //tests if the client is in PlayPowerCard
        assertEquals(clientGameLogic.getState(), gameState);
        assertEquals(gameState.getState(), turnState);
        assertEquals(turnState.getState(), playPowerCard);
    }

    /**
     * UC-ClientState-32: Test the transition from PlayPowerCard to RollDice.
     */
    @Test
    public void testPlayPowerCardToRollDice() {
        //sends the ClientGameLogic in GameState
        clientGameLogic.setState(gameState);
        assertEquals(clientGameLogic.getState(), gameState);

        //sends the gameState in the Turn
        gameState.setState(turnState);
        assertEquals(gameState.getState(), turnState);

        //sends the turnState in PlayPowerCard
        playPowerCard.setPlayCard(playCardShield);
        turnState.setState(playPowerCard);
        assertEquals(turnState.getState(), playPowerCard);

        //sends all messages that don't indicate a state-change
        clientGameLogic.received(activePlayer);
        clientGameLogic.received(anyPiece);
        clientGameLogic.received(briefing);
        clientGameLogic.received(die);
        clientGameLogic.received(diceAgain);
        clientGameLogic.received(endOfTurn);
        clientGameLogic.received(lobbyAccept);
        clientGameLogic.received(lobbyDeny);
        clientGameLogic.received(lobbyPlayerJoin);
        clientGameLogic.received(lobbyPlayerLeave);
        clientGameLogic.received(moveMessage);
        clientGameLogic.received(noTurn);
        clientGameLogic.received(playCardSwap);
        clientGameLogic.received(playCardShield);
        clientGameLogic.received(playCardTurbo);
        clientGameLogic.received(possibleCard);
        clientGameLogic.received(possiblePiece);
        clientGameLogic.received(rankingResponse);
        clientGameLogic.received(rankingRollAgain);
        clientGameLogic.received(reconnectBriefing);
        clientGameLogic.received(resumeGame);
        clientGameLogic.received(startGame);
        clientGameLogic.received(startPieceMessage);
        clientGameLogic.received(updateReady);
        clientGameLogic.received(updateTSK);
        clientGameLogic.received(waitPiece);
        clientGameLogic.received(spectatorMessage);
        clientGameLogic.received(selectPieceMessage);

        //tests if the client is in RollDice
        assertEquals(clientGameLogic.getState(), gameState);
        assertEquals(gameState.getState(), turnState);
        assertEquals(turnState.getState(), playPowerCard);

        //sends the die-message
        int count = playPowerCard.getExtraAnimationCounter();
        for (int i = 0; i < count; i++) {
            clientGameLogic.selectAnimationEnd();
        }
        clientGameLogic.selectAnimationEnd();
        clientGameLogic.received(diceNow);

        //tests if the client is in RollDice
        assertEquals(clientGameLogic.getState(), gameState);
        assertEquals(gameState.getState(), turnState);
        assertEquals(turnState.getState(), rollDice);
    }

    /**
     * UC-ClientState-33: Test staying in the RollDice state.
     */
    @Test
    public void testStayInRollDice() {
        //sends the ClientGameLogic in GameState
        clientGameLogic.setState(gameState);
        assertEquals(clientGameLogic.getState(), gameState);

        //sends the gameState in the Turn
        gameState.setState(turnState);
        assertEquals(gameState.getState(), turnState);

        //sends the turn-state into the rollDice-state
        turnState.setState(rollDice);
        assertEquals(turnState.getState(), rollDice);

        //sends all messages, that don't indicate a state-change
        clientGameLogic.received(activePlayer);
        clientGameLogic.received(anyPiece);
        clientGameLogic.received(briefing);
        clientGameLogic.received(diceAgain);
        clientGameLogic.received(diceNow);
        clientGameLogic.received(endOfTurn);
        clientGameLogic.received(lobbyAccept);
        clientGameLogic.received(lobbyDeny);
        clientGameLogic.received(lobbyPlayerJoin);
        clientGameLogic.received(lobbyPlayerLeave);
        clientGameLogic.received(moveMessage);
        clientGameLogic.received(playCardSwap);
        clientGameLogic.received(playCardShield);
        clientGameLogic.received(playCardTurbo);
        clientGameLogic.received(possibleCard);
        clientGameLogic.received(possiblePiece);
        clientGameLogic.received(rankingResponse);
        clientGameLogic.received(rankingRollAgain);
        clientGameLogic.received(reconnectBriefing);
        clientGameLogic.received(resumeGame);
        clientGameLogic.received(startGame);
        clientGameLogic.received(startPieceMessage);
        clientGameLogic.received(updateReady);
        clientGameLogic.received(updateTSK);
        clientGameLogic.received(waitPiece);
        clientGameLogic.received(spectatorMessage);
        clientGameLogic.received(selectPieceMessage);

        //tests if the client is in RollDice
        assertEquals(clientGameLogic.getState(), gameState);
        assertEquals(gameState.getState(), turnState);
        assertEquals(turnState.getState(), rollDice);
    }

    /**
     * UC-ClientState-34: Test the transition from RollDice to ChoosePiece.
     */
    @Test
    public void testRollDiceToChoosePiece() {
        //sends the ClientGameLogic in GameState
        clientGameLogic.setState(gameState);
        assertEquals(clientGameLogic.getState(), gameState);

        //sends the gameState in the Turn
        gameState.setState(turnState);
        assertEquals(gameState.getState(), turnState);

        //sends the turn-state into the rollDice-state
        turnState.setState(rollDice);
        assertEquals(turnState.getState(), rollDice);

        //sends the die-message
        clientGameLogic.received(die);
        clientGameLogic.received(new ChoosePieceStateMessage());

        //tests if the client is in ChoosePiece-state
        assertEquals(clientGameLogic.getState(), gameState);
        assertEquals(gameState.getState(), turnState);
        assertEquals(turnState.getState(), choosePiece);
        assertEquals(choosePiece.getState(), noPiece);
    }

    /**
     * UC-ClientState-35: Test the transition from RollDice to Wait.
     */
    @Test
    public void testRollDiceToWait() {
        //sends the ClientGameLogic in GameState
        clientGameLogic.setState(gameState);
        assertEquals(clientGameLogic.getState(), gameState);

        //sends the gameState in the Turn
        gameState.setState(turnState);
        assertEquals(gameState.getState(), turnState);

        //sends the turn-state into the rollDice-state
        turnState.setState(rollDice);
        assertEquals(turnState.getState(), rollDice);

        //sends the noTurn-message
        clientGameLogic.received(noTurn);

        //tests if the client is in wait
        assertEquals(clientGameLogic.getState(), gameState);
        assertEquals(gameState.getState(), waiting);
    }

    /**
     * UC-ClientState-36: Test the transition from ChoosePiece to Wait.
     * <p>
     * this method tests the transition from noPiece to Wait
     */
    @Test
    public void testChoosePieceToWait() {
        //sends the client into noPiece
        clientGameLogic.setState(gameState);
        assertEquals(clientGameLogic.getState(), gameState);

        //sends the gameState into Turn
        gameState.setState(turnState);
        assertEquals(gameState.getState(), turnState);

        //sends the turnState into choosePiece
        turnState.setState(choosePiece);
        assertEquals(turnState.getState(), choosePiece);

        //sends the choosePiece into noTurn
        choosePiece.setState(noPiece);
        assertEquals(choosePiece.getState(), noPiece);

        //send the noTurnMessage
        clientGameLogic.received(endOfTurn);

        //tests if the client is in the wait-state
        assertEquals(clientGameLogic.getState(), gameState);
        assertEquals(gameState.getState(), waiting);
    }

    /**
     * UC-ClientState-37: Test the transition from ChoosePiece to MovePiece.
     * <p>
     * this method tests the transition from noPiece to Wait
     */
    @Test
    public void testChoosePieceToMovePiece() {
        /*
         * tests the transition from waitingPiece to MovePiece
         */
        //sends the client into noPiece
        clientGameLogic.setState(gameState);
        assertEquals(clientGameLogic.getState(), gameState);

        //sends the gameState into Turn
        gameState.setState(turnState);
        assertEquals(gameState.getState(), turnState);

        //sends the turnState into choosePiece
        turnState.setState(choosePiece);
        assertEquals(turnState.getState(), choosePiece);

        //sends the choosePiece into waitingPiece
        choosePiece.setState(waitingPiece);
        assertEquals(choosePiece.getState(), waitingPiece);

        //sends the movePieceMessage
        clientGameLogic.received(moveMessage);

        //tests if the client is in the movePiece-state
        assertEquals(clientGameLogic.getState(), gameState);
        assertEquals(gameState.getState(), turnState);
        assertEquals(turnState.getState(), movePiece);

        /*
         * tests the transition from startPiece to MovePiece
         */
        //sends the client into noPiece
        clientGameLogic.setState(gameState);
        assertEquals(clientGameLogic.getState(), gameState);

        //sends the gameState into Turn
        gameState.setState(turnState);
        assertEquals(gameState.getState(), turnState);

        //sends the turnState into choosePiece
        turnState.setState(choosePiece);
        assertEquals(turnState.getState(), choosePiece);

        //sends the choosePiece into startPiece
        choosePiece.setState(startPiece);
        assertEquals(choosePiece.getState(), startPiece);

        //sends the movePieceMessage
        clientGameLogic.received(moveMessage);

        //tests if the client is in the movePiece-state
        assertEquals(clientGameLogic.getState(), gameState);
        assertEquals(gameState.getState(), turnState);
        assertEquals(turnState.getState(), movePiece);

        /*
         * tests the transition from selectPiece to MovePiece
         */
        //sends the client into noPiece
        clientGameLogic.setState(gameState);
        assertEquals(clientGameLogic.getState(), gameState);

        //sends the gameState into Turn
        gameState.setState(turnState);
        assertEquals(gameState.getState(), turnState);

        //sends the turnState into choosePiece
        turnState.setState(choosePiece);
        assertEquals(turnState.getState(), choosePiece);

        //sends the choosePiece into selectPiece
        choosePiece.setState(selectPiece);
        assertEquals(choosePiece.getState(), selectPiece);

        //sends the movePieceMessage
        clientGameLogic.received(moveMessage);

        //tests if the client is in the movePiece-state
        assertEquals(clientGameLogic.getState(), gameState);
        assertEquals(gameState.getState(), turnState);
        assertEquals(turnState.getState(), movePiece);
    }

    /**
     * UC-ClientState-38: Test the transition from MovePiece to Wait.
     */
    @Test
    public void testMovePieceToWait() {
        //sends the client into noPiece
        clientGameLogic.setState(gameState);
        assertEquals(clientGameLogic.getState(), gameState);

        //sends the gameState into Turn
        gameState.setState(turnState);
        assertEquals(gameState.getState(), turnState);

        //sends the turnState into movePiece
        turnState.setState(movePiece);
        assertEquals(turnState.getState(), movePiece);

        //sends the endOfTurnMessage
        clientGameLogic.received(endOfTurn);

        //tests if the client is in the movePiece-state
        assertEquals(clientGameLogic.getState(), gameState);
        assertEquals(gameState.getState(), waiting);
    }

    /**
     * UC-ClientState-39: Test the transition from MovePiece to Spectator.
     */
    @Test
    public void testMovePieceToSpectator() {
        //sends the client into noPiece
        clientGameLogic.setState(gameState);
        assertEquals(clientGameLogic.getState(), gameState);

        //sends the gameState into Turn
        gameState.setState(turnState);
        assertEquals(gameState.getState(), turnState);

        //sends the turnState into movePiece
        turnState.setState(movePiece);
        assertEquals(turnState.getState(), movePiece);

        //sends the spectator-message
        clientGameLogic.received(spectatorMessage);

        //tests if the client is in Spectator
        assertEquals(clientGameLogic.getState(), gameState);
        assertEquals(gameState.getState(), spectator);
    }

    /**
     * UC-ClientState-40: Test the transition from MovePiece to Ceremony.
     */
    @Test
    public void testMovePieceToCeremony() {
        //sends the client into noPiece
        clientGameLogic.setState(gameState);
        assertEquals(clientGameLogic.getState(), gameState);

        //sends the gameState into Turn
        gameState.setState(turnState);
        assertEquals(gameState.getState(), turnState);

        //sends the turnState into movePiece
        turnState.setState(movePiece);
        assertEquals(turnState.getState(), movePiece);

        //sends the spectator-message
        clientGameLogic.received(ceremonyMessage);

        //tests if the client is in Spectator
        assertEquals(clientGameLogic.getState(), ceremony);
        assertEquals(ceremony.getState(), podium);
    }

    /**
     * UC-ClientState-41: Test staying in the ChoosePowerCard state.
     */
    @Test
    public void testStayInChoosePowerCard() {
        //sends the client into noPiece
        clientGameLogic.setState(gameState);
        assertEquals(clientGameLogic.getState(), gameState);

        //sends the gameState into Turn
        gameState.setState(turnState);
        assertEquals(gameState.getState(), turnState);

        //sends the turnState into choosePowerCardPiece
        turnState.setState(choosePowerCard);
        assertEquals(turnState.getState(), choosePowerCard);

        //send all messages, that don't indicate a state-change
        clientGameLogic.received(activePlayer);
        clientGameLogic.received(anyPiece);
        clientGameLogic.received(briefing);
        clientGameLogic.received(lobbyAccept);
        clientGameLogic.received(lobbyDeny);
        clientGameLogic.received(lobbyPlayerJoin);
        clientGameLogic.received(lobbyPlayerLeave);
        clientGameLogic.received(moveMessage);
        clientGameLogic.received(noTurn);
        clientGameLogic.received(possibleCard);
        clientGameLogic.received(possiblePiece);
        clientGameLogic.received(rankingResponse);
        clientGameLogic.received(rankingRollAgain);
        clientGameLogic.received(reconnectBriefing);
        clientGameLogic.received(resumeGame);
        clientGameLogic.received(startGame);
        clientGameLogic.received(startPieceMessage);
        clientGameLogic.received(updateReady);
        clientGameLogic.received(updateTSK);
        clientGameLogic.received(waitPiece);
        clientGameLogic.received(spectatorMessage);
        clientGameLogic.received(selectPieceMessage);

        //tests if the client is still in the choosePowerCard-State
        assertEquals(clientGameLogic.getState(), gameState);
        assertEquals(gameState.getState(), turnState);
        assertEquals(turnState.getState(), choosePowerCard);
    }

    /**
     * UC-ClientState-42: Test the transition from ChoosePowerCard to RollDice.
     */
    @Test
    public void testChoosePowerCardToRollDice() {
        //sends the ClientGameLogic in GameState
        clientGameLogic.setState(gameState);
        assertEquals(clientGameLogic.getState(), gameState);

        //sends the gameState in the Turn
        gameState.setState(turnState);
        assertEquals(gameState.getState(), turnState);

        //sends the turnState in PowerCard
        turnState.setState(powerCard);
        assertEquals(turnState.getState(), powerCard);

        //sends the turnState in ChoosePiece
        powerCard.setState(choosePowerCard);
        assertEquals(powerCard.getState(), choosePowerCard);

        //sends the rollDice-Message
        clientGameLogic.received(diceNow);

        //tests if the turnState is in RollDice
        assertEquals(clientGameLogic.getState(), gameState);
        assertEquals(gameState.getState(), turnState);
        assertEquals(turnState.getState(), rollDice);
    }

    /**
     * UC-ClientState-43: Test the transition from ChoosePowerCard to Swap.
     */
    @Test
    public void testChoosePowerCardToSwap() {
        //sends the ClientGameLogic in GameState
        clientGameLogic.setState(gameState);
        assertEquals(clientGameLogic.getState(), gameState);

        //sends the gameState in the Turn
        gameState.setState(turnState);
        assertEquals(gameState.getState(), turnState);

        //sends the turnState in PowerCard
        turnState.setState(powerCard);
        assertEquals(turnState.getState(), powerCard);

        //sends the turnState in ChoosePiece
        powerCard.setState(choosePowerCard);
        assertEquals(powerCard.getState(), choosePowerCard);

        //sends the possiblePieces-Message
        clientGameLogic.received(possiblePieceSwap);

        //tests if the client is in Swap
        assertEquals(clientGameLogic.getState(), gameState);
        assertEquals(gameState.getState(), turnState);
        assertEquals(turnState.getState(), powerCard);
        assertEquals(powerCard.getState(), swap);
    }

    /**
     * UC-ClientState-44: Test the transition from ChoosePowerCard to Shield.
     */
    @Test
    public void testChoosePowerCardToShield() {
        //sends the ClientGameLogic in GameState
        clientGameLogic.setState(gameState);
        assertEquals(clientGameLogic.getState(), gameState);

        //sends the gameState in the Turn
        gameState.setState(turnState);
        assertEquals(gameState.getState(), turnState);

        //sends the turnState in PowerCard
        turnState.setState(powerCard);
        assertEquals(turnState.getState(), powerCard);

        //sends the turnState in ChoosePiece
        powerCard.setState(choosePowerCard);
        assertEquals(powerCard.getState(), choosePowerCard);

        //sends the possiblePiece-Message
        clientGameLogic.received(possiblePieceShield);

        //tests if the client is in Shield
        assertEquals(clientGameLogic.getState(), gameState);
        assertEquals(gameState.getState(), turnState);
        assertEquals(turnState.getState(), powerCard);
        assertEquals(powerCard.getState(), shield);
    }

    /**
     * UC-ClientState-45: Test staying in the Shield state.
     */
    @Test
    public void testStayInShield() {
        //sends the ClientGameLogic in GameState
        clientGameLogic.setState(gameState);
        assertEquals(clientGameLogic.getState(), gameState);

        //sends the gameState in the Turn
        gameState.setState(turnState);
        assertEquals(gameState.getState(), turnState);

        //sends the turnState in PowerCard
        turnState.setState(powerCard);
        assertEquals(turnState.getState(), powerCard);

        //sends the turnState in Shield
        powerCard.setState(shield);
        assertEquals(powerCard.getState(), shield);

        //send the messages, which don't force a statechange
        clientGameLogic.received(activePlayer);
        clientGameLogic.received(anyPiece);
        clientGameLogic.received(briefing);
        clientGameLogic.received(die);
        clientGameLogic.received(endOfTurn);
        clientGameLogic.received(lobbyAccept);
        clientGameLogic.received(lobbyDeny);
        clientGameLogic.received(lobbyPlayerJoin);
        clientGameLogic.received(lobbyPlayerLeave);
        clientGameLogic.received(moveMessage);
        clientGameLogic.received(noTurn);
        clientGameLogic.received(possibleCard);
        clientGameLogic.received(possiblePiece);
        clientGameLogic.received(rankingResponse);
        clientGameLogic.received(rankingRollAgain);
        clientGameLogic.received(reconnectBriefing);
        clientGameLogic.received(resumeGame);
        clientGameLogic.received(startGame);
        clientGameLogic.received(startPieceMessage);
        clientGameLogic.received(updateReady);
        clientGameLogic.received(updateTSK);
        clientGameLogic.received(waitPiece);
        clientGameLogic.received(spectatorMessage);
        clientGameLogic.received(selectPieceMessage);

        //tests if the client is in PlayPowerCard
        assertEquals(clientGameLogic.getState(), gameState);
        assertEquals(gameState.getState(), turnState);
        assertEquals(turnState.getState(), powerCard);
        assertEquals(powerCard.getState(), shield);
    }

    /**
     * UC-ClientState-46: Test the transition from Shield to PowerCardEndState.
     */
    @Test
    public void testShieldToPowerCardEndState() {
        //sends the ClientGameLogic in GameState
        clientGameLogic.setState(gameState);
        assertEquals(clientGameLogic.getState(), gameState);

        //sends the gameState in the Turn
        gameState.setState(turnState);
        assertEquals(gameState.getState(), turnState);

        //sends the turnState in PowerCard
        turnState.setState(powerCard);
        assertEquals(turnState.getState(), powerCard);

        //sends the turnState in Shield
        powerCard.setState(shield);
        assertEquals(powerCard.getState(), shield);

        //sends all messages that don't indicate a state-change
        clientGameLogic.received(activePlayer);
        clientGameLogic.received(anyPiece);
        clientGameLogic.received(briefing);
        clientGameLogic.received(die);
        clientGameLogic.received(endOfTurn);
        clientGameLogic.received(lobbyAccept);
        clientGameLogic.received(lobbyDeny);
        clientGameLogic.received(lobbyPlayerJoin);
        clientGameLogic.received(lobbyPlayerLeave);
        clientGameLogic.received(moveMessage);
        clientGameLogic.received(noTurn);
        clientGameLogic.received(possibleCard);
        clientGameLogic.received(possiblePiece);
        clientGameLogic.received(rankingResponse);
        clientGameLogic.received(rankingRollAgain);
        clientGameLogic.received(reconnectBriefing);
        clientGameLogic.received(resumeGame);
        clientGameLogic.received(startGame);
        clientGameLogic.received(startPieceMessage);
        clientGameLogic.received(updateReady);
        clientGameLogic.received(updateTSK);
        clientGameLogic.received(waitPiece);
        clientGameLogic.received(spectatorMessage);
        clientGameLogic.received(selectPieceMessage);

        //tests if the client is still in shield
        assertEquals(clientGameLogic.getState(), gameState);
        assertEquals(gameState.getState(), turnState);
        assertEquals(turnState.getState(), powerCard);
        assertEquals(powerCard.getState(), shield);

        //sends the playPowerCardMessage
        clientGameLogic.received(playCardShield);

        //tests if the client is in PlayPowerCard
        assertEquals(clientGameLogic.getState(), gameState);
        assertEquals(gameState.getState(), turnState);
        assertEquals(turnState.getState(), playPowerCard);
    }

    /**
     * UC-ClientState-47: Test the transition from Swap to PowerCardEndState.
     */
    @Test
    public void testSwapToPowerCardEndState() {
        //sends the ClientGameLogic in GameState
        clientGameLogic.setState(gameState);
        assertEquals(clientGameLogic.getState(), gameState);

        //sends the gameState in the Turn
        gameState.setState(turnState);
        assertEquals(gameState.getState(), turnState);

        //sends the turnState in PowerCard
        turnState.setState(powerCard);
        assertEquals(turnState.getState(), powerCard);

        //sends the turnState in Swap
        powerCard.setState(swap);
        assertEquals(powerCard.getState(), swap);

        //sends the playCardMessage
        clientGameLogic.received(playCardSwap);

        //tests if the client is in PlayPowerCard
        assertEquals(clientGameLogic.getState(), gameState);
        assertEquals(gameState.getState(), turnState);
        assertEquals(turnState.getState(), playPowerCard);
    }

    /**
     * UC-ClientState-48: Test no piece in WaitingPiece state.
     */
    @Test
    public void testNoPieceInWaitingPiece() {
        //sends the ClientGameLogic in GameState
        clientGameLogic.setState(gameState);
        assertEquals(clientGameLogic.getState(), gameState);

        //sends the gameState in the Turn
        gameState.setState(turnState);
        assertEquals(gameState.getState(), turnState);

        //sends the turnState in ChoosePiece
        turnState.setState(choosePiece);
        assertEquals(turnState.getState(), choosePiece);

        //sends the choosePiece in NoPiece
        choosePiece.setState(noPiece);
        assertEquals(choosePiece.getState(), noPiece);

        //send other messages, that there is no state change
        clientGameLogic.received(anyPiece);
        clientGameLogic.received(briefing);
        clientGameLogic.received(lobbyAccept);
        clientGameLogic.received(lobbyDeny);
        clientGameLogic.received(lobbyPlayerJoin);
        clientGameLogic.received(lobbyPlayerLeave);
        clientGameLogic.received(possibleCard);
        clientGameLogic.received(possiblePiece);
        clientGameLogic.received(rankingResponse);
        clientGameLogic.received(rankingRollAgain);
        clientGameLogic.received(reconnectBriefing);
        clientGameLogic.received(resumeGame);
        clientGameLogic.received(startGame);
        clientGameLogic.received(updateReady);
        clientGameLogic.received(updateTSK);
        clientGameLogic.received(spectatorMessage);

        //sends to the clientGameLogic the message WaitPiece
        clientGameLogic.received(waitPiece);

        assertEquals(clientGameLogic.getState(), gameState);
        assertEquals(gameState.getState(), turnState);
        assertEquals(turnState.getState(), choosePiece);
        assertEquals(choosePiece.getState(), waitingPiece);
    }

    /**
     * UC-ClientState-49: Test no piece in SelectedPiece state.
     */
    @Test
    public void testNoPieceInSelectedPiece() {
        //sends the ClientGameLogic in GameState
        clientGameLogic.setState(gameState);
        assertEquals(clientGameLogic.getState(), gameState);

        //sends the gameState in the Turn
        gameState.setState(turnState);
        assertEquals(gameState.getState(), turnState);

        //sends the turnState in ChoosePiece
        turnState.setState(choosePiece);
        assertEquals(turnState.getState(), choosePiece);

        //sends the choosePiece in NoPiece
        choosePiece.setState(noPiece);
        assertEquals(choosePiece.getState(), noPiece);

        //send other messages, to test that there is no state change
        clientGameLogic.received(activePlayer);
        clientGameLogic.received(anyPiece);
        clientGameLogic.received(briefing);
        clientGameLogic.received(die);
        clientGameLogic.received(lobbyAccept);
        clientGameLogic.received(lobbyDeny);
        clientGameLogic.received(lobbyPlayerJoin);
        clientGameLogic.received(lobbyPlayerLeave);
        clientGameLogic.received(moveMessage);
        clientGameLogic.received(playCardSwap);
        clientGameLogic.received(playCardShield);
        clientGameLogic.received(playCardTurbo);
        clientGameLogic.received(possibleCard);
        clientGameLogic.received(possiblePiece);
        clientGameLogic.received(rankingResponse);
        clientGameLogic.received(rankingRollAgain);
        clientGameLogic.received(reconnectBriefing);
        clientGameLogic.received(resumeGame);
        clientGameLogic.received(startGame);
        clientGameLogic.received(updateReady);
        clientGameLogic.received(updateTSK);
        clientGameLogic.received(spectatorMessage);

        //sends to the clientGameLogic the message SelectPiece
        clientGameLogic.received(selectPieceMessage);

        assertEquals(clientGameLogic.getState(), gameState);
        assertEquals(gameState.getState(), turnState);
        assertEquals(turnState.getState(), choosePiece);
        assertEquals(choosePiece.getState(), selectPiece);
    }

    /**
     * UC-ClientState-50: Test no piece in StartPiece state.
     */
    @Test
    public void testNoPieceInStartPiece() {
        //sends the ClientGameLogic in GameState
        clientGameLogic.setState(gameState);
        assertEquals(clientGameLogic.getState(), gameState);

        //sends the gameState in the Turn
        gameState.setState(turnState);
        assertEquals(gameState.getState(), turnState);

        //sends the turnState in ChoosePiece
        turnState.setState(choosePiece);
        assertEquals(turnState.getState(), choosePiece);

        //sends the choosePiece in NoPiece
        choosePiece.setState(noPiece);
        assertEquals(choosePiece.getState(), noPiece);

        //sends other messages, that there is no state change
        clientGameLogic.received(activePlayer);
        clientGameLogic.received(anyPiece);
        clientGameLogic.received(briefing);
        clientGameLogic.received(die);
        clientGameLogic.received(lobbyAccept);
        clientGameLogic.received(lobbyDeny);
        clientGameLogic.received(lobbyPlayerJoin);
        clientGameLogic.received(lobbyPlayerLeave);
        clientGameLogic.received(moveMessage);
        clientGameLogic.received(playCardSwap);
        clientGameLogic.received(playCardShield);
        clientGameLogic.received(playCardTurbo);
        clientGameLogic.received(possibleCard);
        clientGameLogic.received(possiblePiece);
        clientGameLogic.received(rankingResponse);
        clientGameLogic.received(rankingRollAgain);
        clientGameLogic.received(reconnectBriefing);
        clientGameLogic.received(resumeGame);
        clientGameLogic.received(startGame);
        clientGameLogic.received(updateReady);
        clientGameLogic.received(updateTSK);
        clientGameLogic.received(spectatorMessage);

        //sends to the clientGameLogic the message StartPiece
        clientGameLogic.received(startPieceMessage);

        assertEquals(clientGameLogic.getState(), gameState);
        assertEquals(gameState.getState(), turnState);
        assertEquals(turnState.getState(), choosePiece);
        assertEquals(choosePiece.getState(), startPiece);
    }

    /**
     * UC-ClientState-51: Test no piece in Wait state.
     */
    @Test
    public void testNoPieceInWait() {
        //sends the ClientGameLogic in GameState
        clientGameLogic.setState(gameState);
        assertEquals(clientGameLogic.getState(), gameState);

        //sends the gameState in the Turn
        gameState.setState(turnState);
        assertEquals(gameState.getState(), turnState);

        //sends the turnState in ChoosePiece
        turnState.setState(choosePiece);
        assertEquals(turnState.getState(), choosePiece);

        //sends the choosePieceState in NoPiece
        choosePiece.setState(noPiece);
        assertEquals(choosePiece.getState(), noPiece);

        //test other messages, that there is no state change
        clientGameLogic.received(activePlayer);
        clientGameLogic.received(anyPiece);
        clientGameLogic.received(briefing);
        clientGameLogic.received(die);
        clientGameLogic.received(lobbyAccept);
        clientGameLogic.received(lobbyDeny);
        clientGameLogic.received(lobbyPlayerJoin);
        clientGameLogic.received(lobbyPlayerLeave);
        clientGameLogic.received(moveMessage);
        clientGameLogic.received(playCardSwap);
        clientGameLogic.received(playCardShield);
        clientGameLogic.received(playCardTurbo);
        clientGameLogic.received(possibleCard);
        clientGameLogic.received(possiblePiece);
        clientGameLogic.received(rankingResponse);
        clientGameLogic.received(rankingRollAgain);
        clientGameLogic.received(reconnectBriefing);
        clientGameLogic.received(resumeGame);
        clientGameLogic.received(startGame);
        clientGameLogic.received(updateReady);
        clientGameLogic.received(updateTSK);
        clientGameLogic.received(spectatorMessage);

        //tests if the client is still in noPiece
        assertEquals(clientGameLogic.getState(), gameState);
        assertEquals(gameState.getState(), turnState);
        assertEquals(turnState.getState(), choosePiece);
        assertEquals(choosePiece.getState(), noPiece);

        //sends to the clientGameLogic the message NoTurn
        clientGameLogic.received(endOfTurn);

        assertEquals(clientGameLogic.getState(), gameState);
        assertEquals(gameState.getState(), waiting);
    }

    /**
     * UC-ClientState-52: Test staying in the WaitingPiece state.
     */
    @Test
    public void testStayInWaitingPiece() {
        //sends the ClientGameLogic in GameState
        clientGameLogic.setState(gameState);
        assertEquals(clientGameLogic.getState(), gameState);

        //sends the gameState in the Turn
        gameState.setState(turnState);
        assertEquals(gameState.getState(), turnState);

        //sends the turnState in ChoosePiece
        turnState.setState(choosePiece);
        assertEquals(turnState.getState(), choosePiece);

        //sends the choosePieceState in WaitingPiece
        choosePiece.setState(waitingPiece);
        assertEquals(choosePiece.getState(), waitingPiece);

        //send all sever-messages except interrupt and moveMessage to test, if there are no state change
        clientGameLogic.received(activePlayer);
        clientGameLogic.received(anyPiece);
        clientGameLogic.received(briefing);
        clientGameLogic.received(die);
        clientGameLogic.received(diceAgain);
        clientGameLogic.received(diceNow);
        clientGameLogic.received(endOfTurn);
        clientGameLogic.received(lobbyAccept);
        clientGameLogic.received(lobbyDeny);
        clientGameLogic.received(lobbyPlayerJoin);
        clientGameLogic.received(lobbyPlayerLeave);
        clientGameLogic.received(noTurn);
        clientGameLogic.received(playCardSwap);
        clientGameLogic.received(playCardShield);
        clientGameLogic.received(playCardTurbo);
        clientGameLogic.received(possibleCard);
        clientGameLogic.received(possiblePiece);
        clientGameLogic.received(rankingResponse);
        clientGameLogic.received(rankingRollAgain);
        clientGameLogic.received(reconnectBriefing);
        clientGameLogic.received(resumeGame);
        clientGameLogic.received(startGame);
        clientGameLogic.received(startPieceMessage);
        clientGameLogic.received(updateReady);
        clientGameLogic.received(updateTSK);
        clientGameLogic.received(waitPiece);
        clientGameLogic.received(spectatorMessage);
        clientGameLogic.received(selectPieceMessage);

        //tests, if the client is still in WaitingPiece
        assertEquals(clientGameLogic.getState(), gameState);
        assertEquals(gameState.getState(), turnState);
        assertEquals(turnState.getState(), choosePiece);
        assertEquals(choosePiece.getState(), waitingPiece);
    }

    /**
     * UC-ClientState-53: Test the WaitingPiece to ChoosePiece end state.
     */
    @Test
    public void testWaitingPieceInChoosePieceEndState() {
        //sends the ClientGameLogic in GameState
        clientGameLogic.setState(gameState);
        assertEquals(clientGameLogic.getState(), gameState);

        //sends the gameState in the Turn
        gameState.setState(turnState);
        assertEquals(gameState.getState(), turnState);

        //sends the turnState in ChoosePiece
        turnState.setState(choosePiece);
        assertEquals(turnState.getState(), choosePiece);

        //sends the choosePieceState in WaitingPiece
        choosePiece.setState(waitingPiece);
        assertEquals(choosePiece.getState(), waitingPiece);

        //send the move-message to the clientGameLogic to force a state change
        clientGameLogic.received(moveMessage);

        assertEquals(clientGameLogic.getState(), gameState);
        assertEquals(gameState.getState(), turnState);
        assertEquals(turnState.getState(), movePiece);
    }

    /**
     * UC-ClientState-54: Test staying in the SelectedPiece state.
     */
    @Test
    public void testStayInSelectedPiece() {
        //sends the ClientGameLogic in GameState
        clientGameLogic.setState(gameState);
        assertEquals(clientGameLogic.getState(), gameState);

        //sends the gameState in the Turn
        gameState.setState(turnState);
        assertEquals(gameState.getState(), turnState);

        //sends the turnState in ChoosePiece
        turnState.setState(choosePiece);
        assertEquals(turnState.getState(), choosePiece);

        //sends the choosePieceState in SelectPiece;
        choosePiece.setState(selectPiece);
        assertEquals(choosePiece.getState(), selectPiece);

        //send all server messages which don't force a state change here
        clientGameLogic.received(activePlayer);
        clientGameLogic.received(anyPiece);
        clientGameLogic.received(briefing);
        clientGameLogic.received(die);
        clientGameLogic.received(diceAgain);
        clientGameLogic.received(diceNow);
        clientGameLogic.received(endOfTurn);
        clientGameLogic.received(lobbyAccept);
        clientGameLogic.received(lobbyDeny);
        clientGameLogic.received(lobbyPlayerJoin);
        clientGameLogic.received(lobbyPlayerLeave);
        clientGameLogic.received(noTurn);
        clientGameLogic.received(playCardSwap);
        clientGameLogic.received(playCardShield);
        clientGameLogic.received(playCardTurbo);
        clientGameLogic.received(possibleCard);
        clientGameLogic.received(possiblePiece);
        clientGameLogic.received(rankingResponse);
        clientGameLogic.received(rankingRollAgain);
        clientGameLogic.received(reconnectBriefing);
        clientGameLogic.received(resumeGame);
        clientGameLogic.received(startGame);
        clientGameLogic.received(startPieceMessage);
        clientGameLogic.received(updateReady);
        clientGameLogic.received(updateTSK);
        clientGameLogic.received(waitPiece);
        clientGameLogic.received(spectatorMessage);
        clientGameLogic.received(selectPieceMessage);

        assertEquals(clientGameLogic.getState(), gameState);
        assertEquals(gameState.getState(), turnState);
        assertEquals(turnState.getState(), choosePiece);
        assertEquals(choosePiece.getState(), selectPiece);
    }

    /**
     * UC-ClientState-55: Test the SelectedPiece to ChoosePiece end state.
     */
    @Test
    public void testSelectedPieceInChoosePieceEndState() {
        //sends the ClientGameLogic in GameState
        clientGameLogic.setState(gameState);
        assertEquals(clientGameLogic.getState(), gameState);

        //sends the gameState in the Turn
        gameState.setState(turnState);
        assertEquals(gameState.getState(), turnState);

        //sends the turnState in ChoosePiece
        turnState.setState(choosePiece);
        assertEquals(turnState.getState(), choosePiece);

        //sends the choosePieceState in SelectPiece
        choosePiece.setState(selectPiece);
        assertEquals(choosePiece.getState(), selectPiece);

        //sends the move-message
        clientGameLogic.received(moveMessage);

        assertEquals(clientGameLogic.getState(), gameState);
        assertEquals(gameState.getState(), turnState);
        assertEquals(turnState.getState(), movePiece);
    }

    /**
     * UC-ClientState-56: Test staying in the StartPiece state.
     */
    @Test
    public void testStayInStartPiece() {
        //sends the ClientGameLogic in GameState
        clientGameLogic.setState(gameState);
        assertEquals(clientGameLogic.getState(), gameState);

        //sends the gameState in the Turn
        gameState.setState(turnState);
        assertEquals(gameState.getState(), turnState);

        //sends the turnState in ChoosePiece
        turnState.setState(choosePiece);
        assertEquals(turnState.getState(), choosePiece);

        //sends the choosePieceState in StartPiece
        choosePiece.setState(startPiece);
        assertEquals(choosePiece.getState(), startPiece);

        //send all messages which don't force a state change
        clientGameLogic.received(activePlayer);
        clientGameLogic.received(anyPiece);
        clientGameLogic.received(briefing);
        clientGameLogic.received(die);
        clientGameLogic.received(diceAgain);
        clientGameLogic.received(diceNow);
        clientGameLogic.received(endOfTurn);
        clientGameLogic.received(lobbyAccept);
        clientGameLogic.received(lobbyDeny);
        clientGameLogic.received(lobbyPlayerJoin);
        clientGameLogic.received(lobbyPlayerLeave);
        clientGameLogic.received(noTurn);
        clientGameLogic.received(playCardSwap);
        clientGameLogic.received(playCardShield);
        clientGameLogic.received(playCardTurbo);
        clientGameLogic.received(possibleCard);
        clientGameLogic.received(possiblePiece);
        clientGameLogic.received(rankingResponse);
        clientGameLogic.received(rankingRollAgain);
        clientGameLogic.received(reconnectBriefing);
        clientGameLogic.received(resumeGame);
        clientGameLogic.received(startGame);
        clientGameLogic.received(startPieceMessage);
        clientGameLogic.received(updateReady);
        clientGameLogic.received(updateTSK);
        clientGameLogic.received(waitPiece);
        clientGameLogic.received(spectatorMessage);
        clientGameLogic.received(selectPieceMessage);

        assertEquals(clientGameLogic.getState(), gameState);
        assertEquals(gameState.getState(), turnState);
        assertEquals(turnState.getState(), choosePiece);
        assertEquals(choosePiece.getState(), startPiece);
    }

    /**
     * UC-ClientState-57: Test the StartPiece to ChoosePiece-end-state.
     */
    @Test
    public void testStartPieceToChoosePieceEndState() {
        //sends the ClientGameLogic in GameState
        clientGameLogic.setState(gameState);
        assertEquals(clientGameLogic.getState(), gameState);

        //sends the gameState in the Turn
        gameState.setState(turnState);
        assertEquals(gameState.getState(), turnState);

        //sends the turnState in ChoosePiece
        turnState.setState(choosePiece);
        assertEquals(turnState.getState(), choosePiece);

        //sends the choosePieceState in StartPiece
        choosePiece.setState(startPiece);
        assertEquals(choosePiece.getState(), startPiece);

        //send the message that don't indicate a statechange
        clientGameLogic.received(activePlayer);
        clientGameLogic.received(anyPiece);
        clientGameLogic.received(briefing);
        clientGameLogic.received(die);
        clientGameLogic.received(endOfTurn);
        clientGameLogic.received(lobbyAccept);
        clientGameLogic.received(lobbyDeny);
        clientGameLogic.received(lobbyPlayerJoin);
        clientGameLogic.received(lobbyPlayerLeave);
        clientGameLogic.received(noTurn);
        clientGameLogic.received(playCardSwap);
        clientGameLogic.received(playCardShield);
        clientGameLogic.received(playCardTurbo);
        clientGameLogic.received(possibleCard);
        clientGameLogic.received(possiblePiece);
        clientGameLogic.received(rankingResponse);
        clientGameLogic.received(rankingRollAgain);
        clientGameLogic.received(reconnectBriefing);
        clientGameLogic.received(resumeGame);
        clientGameLogic.received(startGame);
        clientGameLogic.received(startPieceMessage);
        clientGameLogic.received(updateReady);
        clientGameLogic.received(updateTSK);
        clientGameLogic.received(waitPiece);
        clientGameLogic.received(spectatorMessage);
        clientGameLogic.received(selectPieceMessage);

        //tests if the client ist still in StartPiece
        assertEquals(clientGameLogic.getState(), gameState);
        assertEquals(gameState.getState(), turnState);
        assertEquals(turnState.getState(), choosePiece);
        assertEquals(choosePiece.getState(), startPiece);

        //send MovePieceMessage
        clientGameLogic.received(moveMessage);

        //tests if the client is in the movePiece-state
        assertEquals(clientGameLogic.getState(), gameState);
        assertEquals(gameState.getState(), turnState);
        assertEquals(turnState.getState(), movePiece);
    }

    /**
     * UC-ClientState-58: Test the transition from RollRankingDice to WaitRanking.
     */
    @Test
    public void testRollRankingDiceToWaitRanking() {
        //sends the ClientGameLogic in GameState
        clientGameLogic.setState(gameState);
        assertEquals(clientGameLogic.getState(), gameState);

        //sends the gameState in the determineStartPlayer
        gameState.setState(determineStartPlayer);
        assertEquals(gameState.getState(), determineStartPlayer);

        //sends the turnState in ChoosePiece
        determineStartPlayer.setState(rollRankingDice);
        assertEquals(determineStartPlayer.getState(), rollRankingDice);

        //sends all messages that don't indicate a state-change
        clientGameLogic.received(activePlayer);
        clientGameLogic.received(anyPiece);
        clientGameLogic.received(briefing);
        clientGameLogic.received(diceAgain);
        clientGameLogic.received(diceNow);
        clientGameLogic.received(endOfTurn);
        clientGameLogic.received(lobbyAccept);
        clientGameLogic.received(lobbyDeny);
        clientGameLogic.received(lobbyPlayerJoin);
        clientGameLogic.received(lobbyPlayerLeave);
        clientGameLogic.received(moveMessage);
        clientGameLogic.received(noTurn);
        clientGameLogic.received(playCardSwap);
        clientGameLogic.received(playCardShield);
        clientGameLogic.received(playCardTurbo);
        clientGameLogic.received(possibleCard);
        clientGameLogic.received(possiblePiece);
        clientGameLogic.received(rankingResponse);
        clientGameLogic.received(rankingRollAgain);
        clientGameLogic.received(reconnectBriefing);
        clientGameLogic.received(resumeGame);
        clientGameLogic.received(startGame);
        clientGameLogic.received(startPieceMessage);
        clientGameLogic.received(updateReady);
        clientGameLogic.received(updateTSK);
        clientGameLogic.received(waitPiece);
        clientGameLogic.received(spectatorMessage);
        clientGameLogic.received(selectPieceMessage);

        //test if the client is still in rollRankingDice
        assertEquals(clientGameLogic.getState(), gameState);
        assertEquals(gameState.getState(), determineStartPlayer);
        assertEquals(determineStartPlayer.getState(), rollRankingDice);

        //sends die-message
        clientGameLogic.received(die);

        //test if the client is in waitRanking
        assertEquals(clientGameLogic.getState(), gameState);
        assertEquals(gameState.getState(), determineStartPlayer);
        assertEquals(determineStartPlayer.getState(), waitRanking);
    }

    /**
     * UC-ClientState-59: Test the transition from WaitRanking to RollRankingDice.
     */
    @Test
    public void testWaitRankingToRollRankingDice() {
        //sends the ClientGameLogic in GameState
        clientGameLogic.setState(gameState);
        assertEquals(clientGameLogic.getState(), gameState);

        //sends the gameState in the determineStartPlayer
        gameState.setState(determineStartPlayer);
        assertEquals(gameState.getState(), determineStartPlayer);

        //sends the turnState in ChoosePiece
        determineStartPlayer.setState(waitRanking);
        assertEquals(determineStartPlayer.getState(), waitRanking);

        //sends all messages that don't indicate a state-change
        clientGameLogic.received(activePlayer);
        clientGameLogic.received(anyPiece);
        clientGameLogic.received(briefing);
        clientGameLogic.received(die);
        clientGameLogic.received(lobbyAccept);
        clientGameLogic.received(lobbyDeny);
        clientGameLogic.received(lobbyPlayerJoin);
        clientGameLogic.received(lobbyPlayerLeave);
        clientGameLogic.received(moveMessage);
        clientGameLogic.received(noTurn);
        clientGameLogic.received(playCardSwap);
        clientGameLogic.received(playCardShield);
        clientGameLogic.received(playCardTurbo);
        clientGameLogic.received(possibleCard);
        clientGameLogic.received(possiblePiece);
        clientGameLogic.received(rankingResponse);
        clientGameLogic.received(reconnectBriefing);
        clientGameLogic.received(resumeGame);
        clientGameLogic.received(startGame);
        clientGameLogic.received(startPieceMessage);
        clientGameLogic.received(updateReady);
        clientGameLogic.received(updateTSK);
        clientGameLogic.received(waitPiece);
        clientGameLogic.received(spectatorMessage);
        clientGameLogic.received(selectPieceMessage);

        //test if the client is still in waitRanking
        assertEquals(clientGameLogic.getState(), gameState);
        assertEquals(gameState.getState(), determineStartPlayer);
        assertEquals(determineStartPlayer.getState(), waitRanking);

        //sends rankingRollAgain-message
        clientGameLogic.received(diceNow);

        //test if the client is in rollRankingDice
        assertEquals(clientGameLogic.getState(), gameState);
        assertEquals(gameState.getState(), determineStartPlayer);
        assertEquals(determineStartPlayer.getState(), rollRankingDice);
    }

    /**
     * UC-ClientState-60: Test the transition from WaitRanking to the EndState of determining the starting player.
     */
    @Test
    public void testWaitRankingToEndStateDetermineStartingPlayer() {
        //sends the ClientGameLogic in GameState
        clientGameLogic.setState(gameState);
        assertEquals(clientGameLogic.getState(), gameState);

        //sends the gameState in the determineStartPlayer
        gameState.setState(determineStartPlayer);
        assertEquals(gameState.getState(), determineStartPlayer);

        //sends the turnState in ChoosePiece
        determineStartPlayer.setState(waitRanking);
        assertEquals(determineStartPlayer.getState(), waitRanking);

        //sends all messages that don't indicate a state-change
        clientGameLogic.received(anyPiece);
        clientGameLogic.received(briefing);
        clientGameLogic.received(lobbyAccept);
        clientGameLogic.received(lobbyDeny);
        clientGameLogic.received(lobbyPlayerJoin);
        clientGameLogic.received(lobbyPlayerLeave);
        clientGameLogic.received(moveMessage);
        clientGameLogic.received(noTurn);
        clientGameLogic.received(playCardSwap);
        clientGameLogic.received(playCardShield);
        clientGameLogic.received(playCardTurbo);
        clientGameLogic.received(possibleCard);
        clientGameLogic.received(possiblePiece);
        clientGameLogic.received(rankingResponse);
        clientGameLogic.received(reconnectBriefing);
        clientGameLogic.received(resumeGame);
        clientGameLogic.received(startGame);
        clientGameLogic.received(startPieceMessage);
        clientGameLogic.received(updateReady);
        clientGameLogic.received(updateTSK);
        clientGameLogic.received(spectatorMessage);
        clientGameLogic.received(selectPieceMessage);

        //test if the client is still in waitRanking
        assertEquals(clientGameLogic.getState(), gameState);
        assertEquals(gameState.getState(), determineStartPlayer);
        assertEquals(determineStartPlayer.getState(), waitRanking);

        clientGameLogic.received(new ActivePlayerMessage(enemyColor));
        clientGameLogic.selectAnimationEnd();

        //test if the client is in intro
        assertEquals(clientGameLogic.getState(), gameState);
        assertEquals(gameState.getState(), determineStartPlayer);
        assertEquals(determineStartPlayer.getState(), determineStartPlayer.getIntro());

        //sends endOfTurn-message
        int animationCounter = determineStartPlayer.getIntro().getAnimationCounter();
        for (int i = 0; i < animationCounter; i++) {
            clientGameLogic.selectAnimationEnd();
        }

        //test if the client is in wait
        assertEquals(clientGameLogic.getState(), gameState);
        assertEquals(gameState.getState(), waiting);
    }

    /**
     * UC-ClientState-61: Test the transition from Podium to Statistics.
     */
    @Test
    public void testPodiumToStatistics() {
        //sends the clientGameLogic into Ceremony
        clientGameLogic.setState(ceremony);
        assertEquals(clientGameLogic.getState(), ceremony);

        //sends the ceremony into podium
        ceremony.setState(podium);
        assertEquals(ceremony.getState(), podium);

        //simulate button-push
        clientGameLogic.selectNext();

        //tests if the client is in statistics
        assertEquals(clientGameLogic.getState(), ceremony);
        assertEquals(ceremony.getState(), statistics);
    }

    /**
     * UC-ClientState-62: Test the transition from Statistics to the Ceremony end state.
     */
    @Test
    public void testStatisticsToCeremonyEndState() {
        //sends the client in ceremony
        clientGameLogic.setState(ceremony);
        assertEquals(clientGameLogic.getState(), ceremony);

        //sends the ceremony in statistics
        ceremony.setState(statistics);
        assertEquals(ceremony.getState(), statistics);

        //simulate button-push
        clientGameLogic.selectNext();

        //tests if client is in startDialog
        assertEquals(clientGameLogic.getState(), dialogs);
        assertEquals(dialogs.getState(), startDialog);
    }
}
