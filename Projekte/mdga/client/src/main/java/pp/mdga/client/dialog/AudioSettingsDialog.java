package pp.mdga.client.dialog;

import com.jme3.math.Vector2f;
import com.jme3.scene.Node;
import pp.mdga.client.MdgaApp;
import pp.mdga.client.button.MenuButton;
import pp.mdga.client.button.SliderButton;
import pp.mdga.client.view.MdgaView;

/**
 * The {@code AudioSettingsDialog} class represents a dialog for adjusting audio settings in the application.
 * It provides controls for managing main volume, music volume, and sound effect volume, and includes
 * a button to return to the previous menu.
 */
public class AudioSettingsDialog extends Dialog {
    private final MdgaView view;

    private SliderButton mainVolume;
    private SliderButton musicVolume;
    private SliderButton soundVolume;

    private MenuButton backButton;

    private boolean active = false;

    /**
     * Constructs an {@code AudioSettingsDialog}.
     *
     * @param app  The main application managing the dialog.
     * @param node The root node for attaching UI elements.
     * @param view The current view, used for navigation and interaction with the dialog.
     */
    public AudioSettingsDialog(MdgaApp app, Node node, MdgaView view) {
        super(app, node);

        this.view = view;

        mainVolume = new SliderButton(app, node, "Gesamt Lautstärke");
        musicVolume = new SliderButton(app, node, "Musik Lautstärke");
        soundVolume = new SliderButton(app, node, "Effekt Lautstärke");

        backButton = new MenuButton(app, node, view::leaveAudioSettings, "Zurück");

        float offset = 2.8f;

        mainVolume.setPos(new Vector2f(0, MenuButton.VERTICAL - offset));
        offset += 1.0f;

        musicVolume.setPos(new Vector2f(0, MenuButton.VERTICAL - offset));
        offset += 1.0f;

        soundVolume.setPos(new Vector2f(0, MenuButton.VERTICAL - offset));

        backButton.setPos(new Vector2f(0, 1.8f));
    }

    /**
     * Called when the dialog is shown. Initializes and displays the volume controls and back button.
     */
    @Override
    protected void onShow() {
        active = true;

        mainVolume.setPercent(app.getAcousticHandler().getMainVolume());
        musicVolume.setPercent(app.getAcousticHandler().getMusicVolume());
        soundVolume.setPercent(app.getAcousticHandler().getSoundVolume());

        backButton.show();

        mainVolume.show();
        musicVolume.show();
        soundVolume.show();
    }

    /**
     * Called when the dialog is hidden. Hides all volume controls and the back button.
     */
    @Override
    protected void onHide() {
        active = false;

        backButton.hide();

        mainVolume.hide();
        musicVolume.hide();
        soundVolume.hide();
    }

    /**
     * Updates the application audio settings based on the current values of the sliders.
     * This method is called continuously while the dialog is active.
     */
    public void update() {
        if (!active) {
            return;
        }

        app.getAcousticHandler().setMainVolume(mainVolume.getPercent());
        app.getAcousticHandler().setMusicVolume(musicVolume.getPercent());
        app.getAcousticHandler().setSoundVolume(soundVolume.getPercent());
    }
}
