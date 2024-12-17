package pp.mdga.client.dialog;

import com.jme3.math.Vector2f;
import com.jme3.scene.Node;
import pp.mdga.client.MdgaApp;
import pp.mdga.client.button.MenuButton;
import pp.mdga.client.view.MdgaView;

/**
 * The {@code SettingsDialog} class represents a dialog for navigating to various settings sections,
 * such as video and audio settings, or returning to the previous view.
 */
public class SettingsDialog extends Dialog {
    private MenuButton videoButton;
    private MenuButton audioButton;
    private MenuButton backButton;

    private final MdgaView view;

    /**
     * Constructs a {@code SettingsDialog}.
     *
     * @param app  The main application managing the dialog.
     * @param node The root node for attaching UI elements.
     * @param view The view managing navigation and interaction with the settings dialog.
     */
    public SettingsDialog(MdgaApp app, Node node, MdgaView view) {
        super(app, node);

        this.view = view;

        videoButton = new MenuButton(app, node, view::enterVideoSettings, "Video");
        audioButton = new MenuButton(app, node, view::enterAudioSettings, "Audio");
        backButton = new MenuButton(app, node, view::leaveSettings, "Zurück");

        float offset = 2.8f;

        videoButton.setPos(new Vector2f(0, MenuButton.VERTICAL - offset));
        offset += 1.25f;

        audioButton.setPos(new Vector2f(0, MenuButton.VERTICAL - offset));

        backButton.setPos(new Vector2f(0, 1.8f));
    }

    /**
     * Called when the dialog is shown. Displays all buttons for video settings, audio settings, and back navigation.
     */
    @Override
    protected void onShow() {
        videoButton.show();
        audioButton.show();
        backButton.show();
    }

    /**
     * Called when the dialog is hidden. Hides all buttons for video settings, audio settings, and back navigation.
     */
    @Override
    protected void onHide() {
        videoButton.hide();
        audioButton.hide();
        backButton.hide();
    }
}
