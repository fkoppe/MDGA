package pp.mdga.client.Dialog;

import org.junit.Before;
import org.junit.Test;
import pp.mdga.client.ClientGameLogic;
import pp.mdga.client.ClientSender;
import pp.mdga.client.DialogsState;
import pp.mdga.client.dialogstate.LobbyState;
import pp.mdga.client.dialogstate.NetworkDialogState;
import pp.mdga.client.dialogstate.StartDialogState;
import pp.mdga.game.Color;
import pp.mdga.game.Game;
import pp.mdga.game.Player;
import pp.mdga.server.ServerGameLogic;
import pp.mdga.server.ServerSender;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * this test-class tests the testcases T072-T075
 */
public class StartDialogTest {

    private Game game;

    private ClientSender clientSender;
    private ClientGameLogic clientGameLogic;

    //declare player-Client here
    private Player playerClient;
    private String nameClient = "Client";
    private Color clientColor;
    private int IDClient;

    private DialogsState dialogs;
    private StartDialogState startDialogState;
    private NetworkDialogState networkDialogState;

    @Before
    public void setUp() {

        clientSender = mock(ClientSender.class);
        clientGameLogic = new ClientGameLogic(clientSender);

        playerClient = new Player();
        IDClient = 1;

        dialogs = clientGameLogic.getDialogs();
        startDialogState = dialogs.getStartDialog();
        networkDialogState = dialogs.getNetworkDialog();
    }

    // UC-StartDialog-01
    @Test
    public void testEnterName() {
        // test if client is in the Start Dialog
        clientGameLogic.setState(dialogs);
        dialogs.setState(startDialogState);
        assertEquals(dialogs, clientGameLogic.getState());
        assertEquals(startDialogState, dialogs.getState());

        assertEquals(null, clientGameLogic.getOwnPlayerName());
        clientGameLogic.setOwnPlayerName(nameClient);
        assertEquals(nameClient, clientGameLogic.getOwnPlayerName());
    }

    // UC-StartDialog-02
    @Test
    public void testJoinServer() {
        // test if client is in the Start Dialog
        clientGameLogic.setState(dialogs);
        dialogs.setState(startDialogState);
        assertEquals(dialogs, clientGameLogic.getState());
        assertEquals(startDialogState, dialogs.getState());

        clientGameLogic.getDialogs().setState(networkDialogState);
        assertEquals(dialogs, clientGameLogic.getState());
        assertEquals(networkDialogState, dialogs.getState());
    }

    // UC-StartDialog-03
    @Test
    public void testHostServer() {
        // test if client is in the Start Dialog
        clientGameLogic.setState(dialogs);
        dialogs.setState(startDialogState);
        assertEquals(dialogs, clientGameLogic.getState());
        assertEquals(startDialogState, dialogs.getState());

        clientGameLogic.getDialogs().setState(networkDialogState);
        clientGameLogic.setHost(true);
        assertEquals(dialogs, clientGameLogic.getState());
        assertEquals(networkDialogState, dialogs.getState());
        assertEquals(true, clientGameLogic.isHost());
    }

    // UC-StartDialog-04
    @Test
    public void testExitGame() {
    }
}
