package pp.mdga.client.Dialog;

import org.junit.Before;
import org.junit.Test;
import pp.mdga.client.ClientGameLogic;
import pp.mdga.client.ClientSender;
import pp.mdga.client.DialogsState;
import pp.mdga.client.GameState;
import pp.mdga.client.dialogstate.LobbyState;
import pp.mdga.client.dialogstate.StartDialogState;
import pp.mdga.game.Color;
import pp.mdga.game.Game;
import pp.mdga.game.Piece;
import pp.mdga.game.Player;
import pp.mdga.message.client.DeselectTSKMessage;
import pp.mdga.message.client.LeaveGameMessage;
import pp.mdga.message.client.LobbyNotReadyMessage;
import pp.mdga.message.client.LobbyReadyMessage;
import pp.mdga.message.client.SelectTSKMessage;
import pp.mdga.message.client.StartGameMessage;
import pp.mdga.message.server.UpdateTSKMessage;
import pp.mdga.notification.InfoNotification;
import pp.mdga.notification.TskSelectNotification;
import pp.mdga.server.ServerGameLogic;
import pp.mdga.server.ServerSender;

import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * this test-class tests the testcases T084-T095
 */
public class LobbyStateTest {

    private Game game;

    private ClientSender clientSender;
    private ClientGameLogic clientGameLogic;

    //declare server here
    private ServerGameLogic serverGameLogic;
    private ServerSender serverSender;

    //declare player-Client here
    private Player playerClient;
    private String nameClient = "Client";
    private Color clientColor;
    private int IDClient;

    //declare player-host here
    private Player playerHost;
    private String nameHost = "Host";
    private Color hostColor;
    private int IDHost;

    private DialogsState dialogs;
    private LobbyState lobby;
    private StartDialogState startDialogState;
    private pp.mdga.server.automaton.LobbyState lobbyState;
    private pp.mdga.server.automaton.GameState gameState;
    private GameState clientGameState;

    private SelectTSKMessage selectTSKMessage;
    private DeselectTSKMessage deselectTSKMessage;
    private LobbyReadyMessage readyMessage;
    private LobbyNotReadyMessage notReadyMessage;
    private StartGameMessage startGameMessage;

    private TskSelectNotification tskSelectNotification;

    @Before
    public void setUp() {
        Game game = new Game();

        clientSender = mock(ClientSender.class);
        clientGameLogic = new ClientGameLogic(clientSender);

        serverSender = mock(ServerSender.class);
        serverGameLogic = new ServerGameLogic(serverSender, game);

        IDClient = 1;
        IDHost = 2;

        playerClient = new Player(nameClient);
        clientColor = Color.NONE;
        playerClient.setColor(clientColor);
        playerClient.initialize();
        game.addPlayer(IDClient, playerClient);

        playerHost = new Player(nameHost);
        hostColor = Color.ARMY;
        playerHost.setColor(hostColor);
        playerHost.initialize();
        game.addPlayer(IDHost, playerHost);

        //initialize the playerData
        for(Map.Entry<Integer, Player> entry : game.getPlayers().entrySet()){
            game.addPlayer(entry.getKey(), entry.getValue());
        }

        dialogs = clientGameLogic.getDialogs();
        lobby = dialogs.getLobby();
        startDialogState = dialogs.getStartDialog();
        lobbyState = serverGameLogic.getLobbyState();

        selectTSKMessage = new SelectTSKMessage(Color.CYBER);
        deselectTSKMessage = new DeselectTSKMessage(Color.CYBER);
        readyMessage = new LobbyReadyMessage();
        notReadyMessage = new LobbyNotReadyMessage();
        startGameMessage = new StartGameMessage();

        tskSelectNotification = (TskSelectNotification) clientGameLogic.getNotification();

    }

    // UC-Lobby-01
    @Test
    public void testSelectTSK() {
        // test if client is in the Lobby
        clientGameLogic.setState(dialogs);
        dialogs.setState(lobby);
        assertEquals(dialogs, clientGameLogic.getState());
        assertEquals(lobby, dialogs.getState());

        //test if server is in the lobby state
        serverGameLogic.setCurrentState(lobbyState);
        assertEquals(lobbyState, serverGameLogic.getCurrentState());

        serverGameLogic.received(selectTSKMessage, IDClient);
        assertEquals(Color.CYBER, serverGameLogic.getGame().getPlayerById(IDClient).getColor());
    }

    // UC-Lobby-02
    @Test
    public void testDeselectTSK() {
        // test if client is in the Lobby
        clientGameLogic.setState(dialogs);
        dialogs.setState(lobby);
        assertEquals(dialogs, clientGameLogic.getState());
        assertEquals(lobby, dialogs.getState());

        //test if server is in the lobby state
        serverGameLogic.setCurrentState(lobbyState);
        assertEquals(lobbyState, serverGameLogic.getCurrentState());

        serverGameLogic.received(selectTSKMessage, IDClient);
        assertEquals(Color.CYBER, serverGameLogic.getGame().getPlayerById(IDClient).getColor());
        serverGameLogic.received(deselectTSKMessage, IDClient);
        assertEquals(Color.NONE, serverGameLogic.getGame().getPlayerById(IDClient).getColor());
    }

    // UC-Lobby-03
    @Test
    public void testChangeTSK() {
        // test if client is in the Lobby
        clientGameLogic.setState(dialogs);
        dialogs.setState(lobby);
        assertEquals(dialogs, clientGameLogic.getState());
        assertEquals(lobby, dialogs.getState());

        //test if server is in the lobby state
        serverGameLogic.setCurrentState(lobbyState);
        assertEquals(lobbyState, serverGameLogic.getCurrentState());

        assertEquals(Color.ARMY, serverGameLogic.getGame().getPlayerById(IDHost).getColor());
        serverGameLogic.received(deselectTSKMessage, IDHost);
        assertEquals(Color.NONE, serverGameLogic.getGame().getPlayerById(IDHost).getColor());
        serverGameLogic.received(selectTSKMessage, IDHost);
        assertEquals(Color.CYBER, serverGameLogic.getGame().getPlayerById(IDHost).getColor());
    }

    // UC-Lobby-04
    @Test
    public void testReady() {
        // test if client is in the Lobby
        clientGameLogic.setState(dialogs);
        dialogs.setState(lobby);
        assertEquals(dialogs, clientGameLogic.getState());
        assertEquals(lobby, dialogs.getState());

        //test if server is in the lobby state
        serverGameLogic.setCurrentState(lobbyState);
        assertEquals(lobbyState, serverGameLogic.getCurrentState());

        // Mark a player as not ready
        serverGameLogic.getGame().getPlayerById(IDClient).setReady(false);
        assertFalse(serverGameLogic.getGame().getPlayerById(IDClient).isReady());

        // Send a LobbyReadyMessage and validate the player is marked as  ready
        serverGameLogic.received(readyMessage, IDClient);
        assertTrue(serverGameLogic.getGame().getPlayerById(IDClient).isReady());

        serverGameLogic.received(readyMessage, IDHost);
        assertTrue(serverGameLogic.getGame().getPlayerById(IDHost).isReady());
    }

    // UC-Lobby-05
    @Test
    public void testNotReady() {
        // test if client is in the Lobby
        clientGameLogic.setState(dialogs);
        dialogs.setState(lobby);
        assertEquals(dialogs, clientGameLogic.getState());
        assertEquals(lobby, dialogs.getState());

        // Ensure the server is in the LobbyState
        serverGameLogic.setCurrentState(lobbyState);
        assertEquals(lobbyState, serverGameLogic.getCurrentState());

        // Send a LobbyNotReadyMessage and validate the player is still marked as not ready
        serverGameLogic.received(notReadyMessage, IDClient);
        assertFalse(serverGameLogic.getGame().getPlayerById(IDClient).isReady());

        // Mark a player as ready
        serverGameLogic.getGame().getPlayerById(IDClient).setReady(true);
        assertTrue(serverGameLogic.getGame().getPlayerById(IDClient).isReady());

        // Send a LobbyNotReadyMessage and validate the player is marked as not ready
        serverGameLogic.received(notReadyMessage, IDClient);
        assertFalse(serverGameLogic.getGame().getPlayerById(IDClient).isReady());
    }

    // UC-Lobby-06
    @Test
    public void testLeaveLobby() {
        // test if client is in the Lobby
        clientGameLogic.setState(dialogs);
        dialogs.setState(lobby);
        assertEquals(dialogs, clientGameLogic.getState());
        assertEquals(lobby, dialogs.getState());

        clientGameLogic.getDialogs().getLobby().selectLeave();
        assertEquals(dialogs, clientGameLogic.getState());
        assertEquals(startDialogState, dialogs.getState());
    }

    // UC-Lobby-07
    @Test
    public void testStartGame() {
        // Simulate client in lobby
        clientGameLogic.setState(dialogs);
        dialogs.setState(lobby);
        assertEquals(dialogs, clientGameLogic.getState());
        assertEquals(lobby, dialogs.getState());

        // Simulate server in lobby
        serverGameLogic.setCurrentState(lobbyState);
        assertEquals(lobbyState, serverGameLogic.getCurrentState());

        // Test: Game cannot start with less than 2 players
        serverGameLogic.getGame().removePlayer(IDHost); // Remove one player, leaving only 1
        assertEquals(1, serverGameLogic.getGame().getPlayers().size());

        serverGameLogic.received(new StartGameMessage(), IDClient);
        assertEquals(lobbyState, serverGameLogic.getCurrentState());

        // Test: Game cannot start if not all players are ready
        serverGameLogic.getGame().addPlayer(IDHost, playerHost); // Add the second player back
        assertEquals(2, serverGameLogic.getGame().getPlayers().size());

        serverGameLogic.getGame().getPlayerById(IDClient).setReady(false);
        serverGameLogic.getGame().getPlayerById(IDHost).setReady(true);
        assertFalse(serverGameLogic.getGame().areAllReady());

        serverGameLogic.received(new StartGameMessage(), IDClient);
        assertEquals(lobbyState, serverGameLogic.getCurrentState());

        // Test: Game starts successfully if there are at least 2 players, and all are ready
        serverGameLogic.getGame().getPlayerById(IDClient).setReady(true);
        assertTrue(serverGameLogic.getGame().areAllReady());

//        TODO: Index out of Bounds Exception
//        serverGameLogic.received(new StartGameMessage(), IDHost);
//        assertEquals(gameState, serverGameLogic.getCurrentState()); // Verify the server is now in game
//        assertEquals(clientGameState, clientGameLogic.getState());

    }

    // UC-Lobby-08
    @Test
    public void testShowStatus() {
    }

    // UC-Lobby-09
    @Test
    public void testShowNames() {
    }

    // UC-Lobby-10
    @Test
    public void testShowAvailableTSKs() {
        // TODO: Implement test logic for displaying the list of available tasks (TSKs)
    }

    // UC-Lobby-11
    @Test
    public void testShowAssignedTSKs() {
        // TODO: Implement test logic for showing the tasks assigned to players
    }

    // UC-Lobby-12
    @Test
    public void testServerAssignsTSK() {
        // test if client is in the Lobby
        clientGameLogic.setState(dialogs);
        dialogs.setState(lobby);
        assertEquals(dialogs, clientGameLogic.getState());
        assertEquals(lobby, dialogs.getState());

        //test if server is in the lobby state
        serverGameLogic.setCurrentState(lobbyState);
        assertEquals(lobbyState, serverGameLogic.getCurrentState());

        assertEquals(Color.NONE, serverGameLogic.getGame().getPlayerById(IDClient).getColor());
        Color firstColor = serverGameLogic.getGame().getFirstUnusedColor();
        serverGameLogic.received(readyMessage, IDClient);
        assertTrue(serverGameLogic.getGame().getPlayerById(IDClient).isReady());
        assertEquals(firstColor, serverGameLogic.getGame().getPlayerById(IDClient).getColor());
    }
}
