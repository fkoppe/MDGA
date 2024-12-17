package pp.mdga.client.Dialog;

import org.junit.Before;
import org.junit.Test;
import pp.mdga.Resources;
import pp.mdga.client.ClientGameLogic;
import pp.mdga.client.ClientSender;
import pp.mdga.client.DialogsState;
import pp.mdga.client.dialogstate.LobbyState;
import pp.mdga.client.dialogstate.NetworkDialogState;
import pp.mdga.client.dialogstate.StartDialogState;
import pp.mdga.game.Color;
import pp.mdga.game.Game;
import pp.mdga.game.Player;
import pp.mdga.message.client.JoinedLobbyMessage;
import pp.mdga.message.server.LobbyAcceptMessage;
import pp.mdga.message.server.LobbyDenyMessage;
import pp.mdga.notification.InfoNotification;
import pp.mdga.server.ServerGameLogic;
import pp.mdga.server.ServerSender;

import java.util.prefs.Preferences;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

/**
 * this test-class tests the testcases T079-T083
 */
public class NetworkDialogClientTest {

    private final String IP = "127.0.0.1";
    private final int PORT = 8080;

    private Game game;

    private Preferences preferences;

    private ClientSender clientSender;
    private ClientGameLogic clientGameLogic;

    private ServerSender serverSender;
    private ServerGameLogic serverGameLogic;

    //declare player-Client here
    private Player playerClient;
    private String nameClient = "Client";
    private int IDClient;

    //declare player-host here
    private Player playerHost;
    private String nameHost = "Host";
    private Color hostColor;
    private int IDHost;

    private DialogsState dialogs;
    private NetworkDialogState networkDialogState;
    private StartDialogState startDialogState;
    private LobbyState lobby;
    private pp.mdga.server.automaton.LobbyState lobbyState;

    private JoinedLobbyMessage joinedLobbyMessage;
    private LobbyDenyMessage lobbyDenyMessage;
    private LobbyAcceptMessage lobbyAcceptMessage;

    private InfoNotification infoNotification;

    @Before
    public void setUp() {
        game = new Game();

        clientSender = mock(ClientSender.class);
        serverSender = mock(ServerSender.class);

        serverGameLogic = new ServerGameLogic(serverSender, game);
        clientGameLogic = new ClientGameLogic(clientSender);

        playerClient = new Player(nameClient);
        IDClient = 1;

        playerHost = new Player(nameHost);
        hostColor = Color.ARMY;
        IDHost = 2;
        game.addPlayer(IDHost, playerHost);

        dialogs = clientGameLogic.getDialogs();
        networkDialogState = dialogs.getNetworkDialog();
        startDialogState = dialogs.getStartDialog();
        lobby = dialogs.getLobby();
        lobbyState = serverGameLogic.getLobbyState();

        joinedLobbyMessage = new JoinedLobbyMessage();
        lobbyDenyMessage = new LobbyDenyMessage();
        lobbyAcceptMessage = new LobbyAcceptMessage();

        infoNotification = new InfoNotification(Resources.stringLookup("lobby.deny.join"));

        preferences = Preferences.userNodeForPackage(NetworkDialogClientTest.class);
    }

    // UC-NetworkDialogClient-01
    @Test
    public void testEnterIP() {
        // test if client is in the Lobby
        clientGameLogic.setState(dialogs);
        dialogs.setState(networkDialogState);
        assertEquals(dialogs, clientGameLogic.getState());
        assertEquals(networkDialogState, dialogs.getState());

        clientGameLogic.selectJoin(IP);
        assertEquals(IP, preferences.get("IP", IP));
    }

    // UC-NetworkDialogClient-02
    @Test
    public void testEnterPort() {
    }

    // UC-NetworkDialogClient-03
    @Test
    public void testConnectToServer() {
        // test if client is in the Lobby
        clientGameLogic.setState(dialogs);
        dialogs.setState(networkDialogState);
        assertEquals(dialogs, clientGameLogic.getState());
        assertEquals(networkDialogState, dialogs.getState());

        //test if server is in the lobby state
        serverGameLogic.setCurrentState(lobbyState);
        assertEquals(lobbyState, serverGameLogic.getCurrentState());

        //Server accepts connection
        clientGameLogic.getDialogs().getNetworkDialog().received(lobbyAcceptMessage);
        assertEquals(dialogs, clientGameLogic.getState());
        assertEquals(lobby, dialogs.getState());

        serverGameLogic.received(joinedLobbyMessage, IDClient);
        assertTrue(serverGameLogic.getGame().getPlayers().containsKey(IDClient));
    }

    @Test
    public void testCantConnectToServer() {
        // test if client is in the Lobby
        clientGameLogic.setState(dialogs);
        dialogs.setState(networkDialogState);
        assertEquals(dialogs, clientGameLogic.getState());
        assertEquals(networkDialogState, dialogs.getState());

        //test if server is in the lobby state
        serverGameLogic.setCurrentState(lobbyState);
        assertEquals(lobbyState, serverGameLogic.getCurrentState());

        //test if client receives a lobby deny notification and stays in the NetworkDialog
        clientGameLogic.getDialogs().getNetworkDialog().received(lobbyDenyMessage);
        infoNotification =(InfoNotification) clientGameLogic.getNotification();
        assertEquals(Resources.stringLookup("lobby.deny.join"),infoNotification.getMessage());
        assertEquals(dialogs, clientGameLogic.getState());
        assertEquals(startDialogState, dialogs.getState());    }

    // UC-NetworkDialogClient-04
    @Test
    public void testCancelJoining() {
        // test if client is in the Lobby
        clientGameLogic.setState(dialogs);
        dialogs.setState(networkDialogState);
        assertEquals(dialogs, clientGameLogic.getState());
        assertEquals(networkDialogState, dialogs.getState());

        //tests if client can return to the Start Menu
        clientGameLogic.selectLeave();
        assertEquals(dialogs, clientGameLogic.getState());
        assertEquals(startDialogState, dialogs.getState());

    }
}
