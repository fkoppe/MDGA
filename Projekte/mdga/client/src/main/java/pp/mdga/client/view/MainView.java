package pp.mdga.client.view;

import com.jme3.scene.Geometry;
import pp.mdga.client.MdgaApp;
import pp.mdga.client.acoustic.MdgaSound;
import pp.mdga.client.dialog.HostDialog;
import pp.mdga.client.dialog.JoinDialog;
import pp.mdga.client.dialog.StartDialog;

/**
 * MainView class that extends MdgaView and manages the main view of the application.
 */
public class MainView extends MdgaView {
    /**
     * Enum representing the different sub-states of the MainView.
     */
    private enum SubState {
        HOST,
        JOIN,
        MAIN,
    }

    private SubState state;

    private Geometry background;

    private StartDialog startDialog;
    private JoinDialog joinDialog;
    private HostDialog hostDialog;

    /**
     * Constructor for MainView.
     *
     * @param app the MdgaApp instance
     */
    public MainView(MdgaApp app) {
        super(app);

        startDialog = new StartDialog(app, guiNode, this);
        joinDialog = new JoinDialog(app, guiNode, this);
        hostDialog = new HostDialog(app, guiNode, this);

        background = createBackground("Images/startmenu.png");
    }

    /**
     * Called when the view is entered.
     */
    @Override
    public void onEnter() {
        app.setup();

        guiNode.attachChild(background);

        enterSub(SubState.MAIN);
    }

    /**
     * Called when the view is left.
     */
    @Override
    public void onLeave() {
        startDialog.hide();
        joinDialog.hide();
        hostDialog.hide();

        guiNode.detachChild(background);
    }

    /**
     * Called to update the view.
     *
     * @param tpf time per frame
     */
    @Override
    public void onUpdate(float tpf) {
        startDialog.update();
        joinDialog.update();
        hostDialog.update();
    }

    /**
     * Called when an overlay is entered.
     *
     * @param overlay the overlay being entered
     */
    @Override
    protected void onEnterOverlay(Overlay overlay) {
        guiNode.detachChild(background);
        settingsNode.attachChild(background);
    }

    /**
     * Called when an overlay is left.
     *
     * @param overlay the overlay being left
     */
    @Override
    protected void onLeaveOverlay(Overlay overlay) {
        settingsNode.detachChild(background);
        guiNode.attachChild(background);
    }

    /**
     * Shows the join menu.
     */
    private void joinMenu() {
        startDialog.hide();
        hostDialog.hide();

        joinDialog.show();
    }

    /**
     * Shows the host menu.
     */
    private void hostMenu() {
        startDialog.hide();
        joinDialog.hide();

        hostDialog.show();
    }

    /**
     * Shows the main menu.
     */
    private void mainMenu() {
        joinDialog.hide();
        hostDialog.hide();

        startDialog.show();
    }

    /**
     * Attempts to host a game.
     */
    private void tryHost() {
        int port = 0;
        String text = hostDialog.getPort();
        app.getGameLogic().selectHost("");

        try {
            port = Integer.parseInt(text);

            if (port >= 1 && port <= 65535) {
                app.getModelSynchronize().setName(startDialog.getName());
                hostDialog.hostServer();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {
                }
                hostDialog.connectServerAsClient();
                app.getModelSynchronize().setHost(port);
                //app.getAcousticHandler().playSound(MdgaSound.WRONG_INPUT);
                return;
            }
        } catch (NumberFormatException ignored) {
        }

        hostDialog.resetPort();
        app.getAcousticHandler().playSound(MdgaSound.WRONG_INPUT);
    }

    /**
     * Attempts to join a game.
     */
    private void tryJoin() {
        int port = 0;
        String ip = joinDialog.getIpt();
        String portText = joinDialog.getPort();
        app.getGameLogic().selectJoin("");

        try {
            // Validate the port
            port = Integer.parseInt(portText);
            if (port < 1 || port > 65535) {
                throw new IllegalArgumentException("Invalid port");
            }
            joinDialog.setPortNumber(port);
            // Validate the IP address
            if (isValidIpAddress(ip)) {
                app.getModelSynchronize().setName(startDialog.getName());
                joinDialog.setHostname(ip);
                joinDialog.connectToServer();
                return;
            }
        } catch (IllegalArgumentException e) {
            // Invalid input, fall through to reset
        }

        joinDialog.resetPort();
        joinDialog.resetIp();
        app.getAcousticHandler().playSound(MdgaSound.WRONG_INPUT);
    }

    /**
     * Validates an IP address.
     *
     * @param ip the IP address to validate
     * @return true if the IP address is valid, false otherwise
     */
    private boolean isValidIpAddress(String ip) {
        String ipRegex =
                "^(25[0-5]|2[0-4][0-9]|[0-1]?[0-9][0-9]?)\\." +
                        "(25[0-5]|2[0-4][0-9]|[0-1]?[0-9][0-9]?)\\." +
                        "(25[0-5]|2[0-4][0-9]|[0-1]?[0-9][0-9]?)\\." +
                        "(25[0-5]|2[0-4][0-9]|[0-1]?[0-9][0-9]?)$";

        return ip != null && ip.matches(ipRegex);
    }

    /**
     * Enters a sub-state.
     *
     * @param state the sub-state to enter
     */
    private void enterSub(SubState state) {
        this.state = state;

        if (null != background) {
            rootNode.detachChild(background);
        }

        switch (state) {
            case HOST:
                hostMenu();
                break;
            case JOIN:
                joinMenu();
                break;
            case MAIN:
                mainMenu();
                break;
        }
    }

    /**
     * Forwards the state based on the current sub-state.
     */
    public void forward() {
        switch (state) {
            case HOST:
                tryHost();
                break;
            case JOIN:
                tryJoin();
                break;
            case MAIN:
                throw new RuntimeException("call forward(boolean host) insted of forward()");
        }
    }

    /**
     * Forwards the state based on the current sub-state and a boolean flag.
     *
     * @param host a boolean flag indicating whether to host or join
     */
    public void forward(boolean host) {
        switch (state) {
            case HOST:
                tryHost();
                break;
            case JOIN:
                tryJoin();
                break;
            case MAIN:
                if (host) {
                    enterSub(SubState.HOST);
                    //TODO playSound
                } else {
                    enterSub(SubState.JOIN);
                    //TODO: playSound
                }
                break;
        }
    }

    /**
     * Goes back to the main menu from the current sub-state.
     */
    public void back() {
        switch (state) {
            case HOST:
                enterSub(SubState.MAIN);
                //TODO: playSound
                break;
            case JOIN:
                enterSub(SubState.MAIN);
                //TODO: playSound
                break;
            case MAIN:
                //nothing
                break;
        }
    }

    /**
     * Gets the JoinDialog instance.
     *
     * @return the JoinDialog instance
     */
    public JoinDialog getJoinDialog() {
        return joinDialog;
    }

    /**
     * Gets the HostDialog instance.
     *
     * @return the HostDialog instance
     */
    public HostDialog getHostDialog() {
        return hostDialog;
    }
}
