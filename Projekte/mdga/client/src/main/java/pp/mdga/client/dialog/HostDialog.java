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
 * The {@code HostDialog} class represents a dialog for hosting a network game session.
 * It allows users to input a port number, start hosting a server, and navigate back to the previous view.
 */
public class HostDialog extends NetworkDialog {
    private InputButton portInput;

    private ButtonRight hostButton;
    private ButtonLeft backButton;

    private final MainView view;

    private Preferences prefs = Preferences.userNodeForPackage(JoinDialog.class);

    /**
     * Constructs a {@code HostDialog}.
     *
     * @param app  The main application managing the dialog.
     * @param node The root node for attaching UI elements.
     * @param view The main view used for navigation and interaction with the dialog.
     */
    public HostDialog(MdgaApp app, Node node, MainView view) {
        super(app, node, (NetworkSupport) app.getNetworkSupport());

        this.view = view;

        portInput = new InputButton(app, node, "Port: ", 5);
        portInput.setString(prefs.get("hostPort", "11111"));

        hostButton = new ButtonRight(app, node, view::forward, "Spiel hosten", 10);
        backButton = new ButtonLeft(app, node, view::back, "Zurück", 10);

        float offset = 2.8f;

        portInput.setPos(new Vector2f(0, MenuButton.VERTICAL - offset));
        offset += 1.5f;
    }

    /**
     * Called when the dialog is shown. Displays all input fields and buttons.
     */
    @Override
    protected void onShow() {
        portInput.show();
        hostButton.show();
        backButton.show();
    }

    /**
     * Called when the dialog is hidden. Hides all input fields and buttons.
     */
    @Override
    protected void onHide() {
        portInput.hide();
        hostButton.hide();
        backButton.hide();
    }

    /**
     * Updates the state of the port input field.
     * This method is called periodically to synchronize the dialog state.
     */
    public void update() {
        portInput.update();
    }

    /**
     * Retrieves the currently entered port number, saves it to preferences, and sets it as the active port.
     *
     * @return The port number as a string.
     */
    public String getPort() {
        prefs.put("hostPort", portInput.getString());
        setPortNumber(Integer.parseInt(portInput.getString()));
        return portInput.getString();
    }

    /**
     * Resets the port input field to its default value and updates preferences accordingly.
     */
    public void resetPort() {
        portInput.reset();
        prefs.put("hostPort", "11111");
    }

    /**
     * Starts the server to host a network game.
     */
    public void hostServer() {
        startServer();
    }

    /**
     * Connects to the server as a client.
     */
    public void connectServerAsClient() {
        connectServer();
    }
}
