package pp.mdga.client.dialog;

import com.jme3.math.Vector2f;
import com.jme3.scene.Node;
import pp.mdga.client.MdgaApp;
import pp.mdga.client.button.ButtonLeft;
import pp.mdga.client.button.ButtonRight;
import pp.mdga.client.button.MenuButton;
import pp.mdga.client.view.MdgaView;

import java.util.prefs.Preferences;

/**
 * The {@code VideoSettingsDialog} class represents a dialog for configuring video settings,
 * such as resolution and fullscreen mode. It also provides an option to restart the application
 * when certain settings are changed.
 */
public class VideoSettingsDialog extends Dialog {
    private static Preferences prefs = Preferences.userNodeForPackage(JoinDialog.class);

    private ButtonRight fullscreenButton;
    private MenuButton backButton;
    private ButtonRight restartButton;

    private ButtonLeft hdButton9;
    private ButtonLeft fullHdButton9;
    private ButtonLeft wqhdButton9;
    private ButtonRight hdButton10;
    private ButtonRight fullHdButton10;
    private ButtonRight wqhdButton10;

    private final MdgaView view;

    private boolean active = false;

    /**
     * Constructs a {@code VideoSettingsDialog}.
     *
     * @param app  The main application managing the dialog.
     * @param node The root node for attaching UI elements.
     * @param view The view managing navigation and interaction with the video settings dialog.
     */
    public VideoSettingsDialog(MdgaApp app, Node node, MdgaView view) {
        super(app, node);

        this.view = view;

        backButton = new MenuButton(app, node, view::leaveVideoSettings, "Zurück");

        restartButton = new ButtonRight(app, node, MdgaApp::restartApp, "Neustart", 1);

        fullscreenButton = new ButtonRight(app, node, () -> updateResolution(0, 0, 0, true), "Vollbild", 1);

        hdButton9 = new ButtonLeft(app, node, () -> updateResolution(1280, 720, 1.0f, false), "hd 16:9", 10);
        fullHdButton9 = new ButtonLeft(app, node, () -> updateResolution(1920, 1080, 2.25f, false), "full hd 16:9", 10);
        wqhdButton9 = new ButtonLeft(app, node, () -> updateResolution(2560, 1440, 4.0f, false), "wqhd 16:9", 10);


        hdButton10 = new ButtonRight(app, node, () -> updateResolution(1280, 800, 1.0f, false), "hd 16:10", 10);
        fullHdButton10 = new ButtonRight(app, node, () -> updateResolution(1920, 1200, 2.25f, false), "full hd 16:10", 10);
        wqhdButton10 = new ButtonRight(app, node, () -> updateResolution(2560, 1600, 4.0f, false), "wqhd 16:10", 10);

        float offset = 2.8f;

        hdButton9.setPos(new Vector2f(hdButton9.getPos().x, MenuButton.VERTICAL - offset));
        hdButton10.setPos(new Vector2f(hdButton10.getPos().x, MenuButton.VERTICAL - offset));
        fullscreenButton.setPos(new Vector2f(fullscreenButton.getPos().x, MenuButton.VERTICAL - offset));
        offset += 1.5f;

        fullHdButton9.setPos(new Vector2f(fullHdButton9.getPos().x, MenuButton.VERTICAL - offset));
        fullHdButton10.setPos(new Vector2f(fullHdButton10.getPos().x, MenuButton.VERTICAL - offset));
        offset += 1.5f;

        wqhdButton9.setPos(new Vector2f(wqhdButton9.getPos().x, MenuButton.VERTICAL - offset));
        wqhdButton10.setPos(new Vector2f(wqhdButton10.getPos().x, MenuButton.VERTICAL - offset));
        offset += 1.5f;

        backButton.setPos(new Vector2f(0, 1.8f));
    }

    /**
     * Called when the dialog is shown. Displays all buttons and marks the dialog as active.
     */
    @Override
    protected void onShow() {
        active = true;

        hdButton9.show();
        fullHdButton9.show();
        wqhdButton9.show();

        hdButton10.show();
        fullHdButton10.show();
        wqhdButton10.show();

        fullscreenButton.show();
        backButton.show();
    }

    /**
     * Called when the dialog is hidden. Hides all buttons and marks the dialog as inactive.
     */
    @Override
    protected void onHide() {
        active = false;

        hdButton9.hide();
        fullHdButton9.hide();
        wqhdButton9.hide();

        hdButton10.hide();
        fullHdButton10.hide();
        wqhdButton10.hide();

        fullscreenButton.hide();
        backButton.hide();
        restartButton.hide();
    }

    /**
     * Updates the dialog's state. This method can be used for periodic updates while the dialog is active.
     */
    public void update() {
        if (!active) {
            return;
        }
    }

    /**
     * Updates the resolution settings and optionally triggers the restart button if changes are detected.
     *
     * @param width        The width of the resolution.
     * @param height       The height of the resolution.
     * @param imageFactor  The scaling factor for the resolution.
     * @param isFullscreen {@code true} if fullscreen mode is enabled, {@code false} otherwise.
     */
    public void updateResolution(int width, int height, float imageFactor, boolean isFullscreen) {
        if (width != prefs.getInt("width", 1280) || height != prefs.getInt("height", 720) || isFullscreen != prefs.getBoolean("fullscreen", false)) {
            restartButton.show();
        }

        app.updateResolution(width, height, imageFactor, isFullscreen);
    }
}
