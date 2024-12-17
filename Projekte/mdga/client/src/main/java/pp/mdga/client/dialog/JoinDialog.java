package pp.mdga.client.dialog;

import com.jme3.math.Vector2f;
import com.jme3.scene.Node;
import pp.mdga.client.MdgaApp;
import pp.mdga.client.NetworkSupport;
import pp.mdga.client.button.ButtonLeft;
import pp.mdga.client.button.ButtonRight;
import pp.mdga.client.button.InputButton;
import pp.mdga.client.button.MenuButton;
import pp.mdga.client.view.MainView;

import java.util.prefs.Preferences;

/**
 * The {@code JoinDialog} class represents a dialog for joining a network game.
 * It allows users to input an IP address and port number, connect to a server, or navigate back to the previous view.
 */
public class JoinDialog extends NetworkDialog {
    private InputButton ipInput;
    private InputButton portInput;

    private ButtonRight joinButton;
    private ButtonLeft backButton;

    private final MainView view;

    private Preferences prefs = Preferences.userNodeForPackage(JoinDialog.class);

    /**
     * Constructs a {@code JoinDialog}.
     *
     * @param app  The main application managing the dialog.
     * @param node The root node for attaching UI elements.
     * @param view The main view used for navigation and interaction with the dialog.
     */
    public JoinDialog(MdgaApp app, Node node, MainView view) {
        super(app, node, (NetworkSupport) app.getNetworkSupport());

        this.view = view;

        ipInput = new InputButton(app, node, "Ip: ", 15);
        portInput = new InputButton(app, node, "Port: ", 5);
        portInput.setString(prefs.get("joinPort", "11111"));
        ipInput.setString(prefs.get("joinIp", ""));

        joinButton = new ButtonRight(app, node, view::forward, "Spiel beitreten", 10);
        backButton = new ButtonLeft(app, node, view::back, "Zurück", 10);

        float offset = 2.8f;

        ipInput.setPos(new Vector2f(0, MenuButton.VERTICAL - offset));
        offset += 1.5f;

        portInput.setPos(new Vector2f(0, MenuButton.VERTICAL - offset));
        offset += 1.5f;
    }

    /**
     * Called when the dialog is shown. Displays all input fields and buttons.
     */
    @Override
    protected void onShow() {
        ipInput.show();
        portInput.show();
        joinButton.show();
        backButton.show();
    }

    /**
     * Called when the dialog is hidden. Hides all input fields and buttons.
     */
    @Override
    protected void onHide() {
        ipInput.hide();
        portInput.hide();
        joinButton.hide();
        backButton.hide();
    }

    /**
     * Updates the state of the input fields. This method is called periodically to synchronize the dialog state.
     */
    public void update() {
        ipInput.update();
        portInput.update();
    }

    /**
     * Retrieves the currently entered IP address, saves it to preferences, and sets it as the hostname.
     *
     * @return The IP address as a string.
     */
    public String getIpt() {
        prefs.put("joinIp", ipInput.getString());
        setHostname(ipInput.getString());
        return ipInput.getString();
    }

    /**
     * Resets the IP input field to its default value and updates preferences accordingly.
     */
    public void resetIp() {
        ipInput.reset();
        prefs.put("joinIp", "");
    }

    /**
     * Retrieves the currently entered port number, saves it to preferences, and sets it as the active port.
     *
     * @return The port number as a string.
     */
    public String getPort() {
        prefs.put("joinPort", portInput.getString());
        setPortNumber(Integer.parseInt(portInput.getString()));
        return portInput.getString();
    }

    /**
     * Resets the port input field to its default value and updates preferences accordingly.
     */
    public void resetPort() {
        portInput.reset();
        prefs.put("joinPort", "11111");
    }

    /**
     * Connects to the server using the current IP address and port number.
     */
    public void connectToServer() {
        connectServer();
    }

    /**
     * Disconnects from the server if a network connection exists.
     */
    public void disconnect() {
        NetworkSupport network = getNetwork();
        if (network != null) {
            try {
                network.disconnect();
            } catch (Exception e) {
                System.err.println("Error while disconnecting: " + e.getMessage());
            }
        }
    }
}
