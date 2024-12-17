package pp.mdga.client.view;

import com.jme3.asset.TextureKey;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;
import com.jme3.system.NanoTimer;
import com.jme3.texture.Texture;
import pp.mdga.client.MdgaApp;
import pp.mdga.client.acoustic.MdgaSound;
import pp.mdga.client.button.AbstractButton;
import pp.mdga.client.button.LabelButton;
import pp.mdga.client.button.SettingsButton;
import pp.mdga.client.dialog.AudioSettingsDialog;
import pp.mdga.client.dialog.SettingsDialog;
import pp.mdga.client.dialog.VideoSettingsDialog;

/**
 * Abstract class representing a view in the MDGA application.
 */
public abstract class MdgaView {
    /**
     * Enum representing different types of overlays.
     */
    public enum Overlay {
        INTERRUPT,
        SETTINGS,
    }

    protected MdgaApp app;
    protected Node rootNode = new Node("View Root");
    protected Node guiNode = new Node("View Root GUI");
    protected Node settingsNode = new Node("View Root Overlay");

    private SettingsButton settingsButton;

    private SettingsDialog settingsDialog;
    private VideoSettingsDialog videoSettingsDialog;
    private AudioSettingsDialog audioSettingsDialog;

    protected LabelButton infoLabel = null;
    protected NanoTimer infoTimer = new NanoTimer();

    private int settingsDepth = 0;

    /**
     * Constructor for MdgaView.
     *
     * @param app the application instance
     */
    public MdgaView(MdgaApp app) {
        this.app = app;
        settingsButton = new SettingsButton(app, guiNode, this::enterSettings);

        settingsDialog = new SettingsDialog(app, settingsNode, this);
        videoSettingsDialog = new VideoSettingsDialog(app, settingsNode, this);
        audioSettingsDialog = new AudioSettingsDialog(app, settingsNode, this);
    }

    /**
     * Method to enter the view.
     */
    public void enter() {
        app.getRootNode().attachChild(rootNode);
        app.getGuiNode().attachChild(guiNode);

        settingsButton.show();

        onEnter();
    }

    /**
     * Method to leave the view.
     */
    public void leave() {
        onLeave();

        settingsButton.hide();

        while (settingsDepth > 0) {
            pressEscape();
        }

        app.getRootNode().detachChild(rootNode);
        app.getGuiNode().detachChild(guiNode);
    }

    /**
     * Method to enter an overlay.
     *
     * @param overlay the overlay to enter
     */
    public void enterOverlay(Overlay overlay) {
        app.getGuiNode().detachChild(guiNode);

        onEnterOverlay(overlay);
    }

    /**
     * Method to leave an overlay.
     *
     * @param overlay the overlay to leave
     */
    public void leaveOverlay(Overlay overlay) {
        app.getGuiNode().attachChild(guiNode);

        onLeaveOverlay(overlay);
    }

    /**
     * Method to update the view.
     *
     * @param tpf time per frame
     */
    public void update(float tpf) {
        videoSettingsDialog.update();
        audioSettingsDialog.update();

        if (null != infoLabel && infoTimer.getTimeInSeconds() > 5) {
            infoLabel.hide();
            infoLabel = null;
        }

        onUpdate(tpf);
    }

    /**
     * Abstract method to handle entering the view.
     */
    protected abstract void onEnter();

    /**
     * Abstract method to handle leaving the view.
     */
    protected abstract void onLeave();

    /**
     * Method to handle updating the view.
     *
     * @param tpf time per frame
     */
    protected void onUpdate(float tpf) {
    }

    /**
     * Abstract method to handle entering an overlay.
     *
     * @param overlay the overlay to enter
     */
    protected abstract void onEnterOverlay(Overlay overlay);

    /**
     * Abstract method to handle leaving an overlay.
     *
     * @param overlay the overlay to leave
     */
    protected abstract void onLeaveOverlay(Overlay overlay);

    /**
     * Method to create a background geometry with a texture.
     *
     * @param texturePath the path to the texture
     * @return the created background geometry
     */
    protected Geometry createBackground(String texturePath) {
        TextureKey key = new TextureKey(texturePath, true);
        Texture backgroundTexture = app.getAssetManager().loadTexture(key);

        Material mat = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setTexture("ColorMap", backgroundTexture);

        Quad quad = new Quad(app.getCamera().getWidth(), app.getCamera().getHeight());

        Geometry background = new Geometry("Background", quad);
        background.setMaterial(mat);
        background.setLocalTranslation(0, 0, -2);

        return background;
    }

    /**
     * Method to enter the settings view.
     */
    public void enterSettings() {
        enterOverlay(Overlay.SETTINGS);

        app.getGuiNode().attachChild(settingsNode);

        settingsDialog.show();

        settingsDepth++;
    }

    /**
     * Method to leave the settings view.
     */
    public void leaveSettings() {
        leaveOverlay(Overlay.SETTINGS);

        app.getGuiNode().detachChild(settingsNode);

        settingsDialog.hide();

        settingsDepth--;
    }

    /**
     * Method to enter the video settings view.
     */
    public void enterVideoSettings() {
        settingsDialog.hide();
        videoSettingsDialog.show();

        settingsDepth++;
    }

    /**
     * Method to leave the video settings view.
     */
    public void leaveVideoSettings() {
        settingsDialog.show();
        videoSettingsDialog.hide();

        settingsDepth--;
    }

    /**
     * Method to enter the audio settings view.
     */
    public void enterAudioSettings() {
        settingsDialog.hide();
        audioSettingsDialog.show();

        settingsDepth++;
    }

    /**
     * Method to leave the audio settings view.
     */
    public void leaveAudioSettings() {
        settingsDialog.show();
        audioSettingsDialog.hide();

        settingsDepth--;
    }

    /**
     * Method to leave advanced settings.
     */
    private void leaveAdvanced() {
        settingsDialog.show();
        audioSettingsDialog.hide();
        videoSettingsDialog.hide();
        settingsDepth--;
    }

    /**
     * Method to handle pressing the escape key.
     */
    public void pressEscape() {
        if (settingsDepth == 0) {
            enterSettings();
        } else if (settingsDepth == 1) {
            leaveSettings();
        } else {
            leaveAdvanced();
        }
    }

    /**
     * Method to handle pressing the forward key.
     */
    public void pressForward() {
        if (this instanceof MainView mainView) {
            mainView.forward(false);
            app.getAcousticHandler().playSound(MdgaSound.BUTTON_PRESSED);
        }

        if (this instanceof LobbyView lobbyView) {
            lobbyView.ready();
        }

        if (this instanceof GameView gameView) {
            if (gameView.needConfirm) {
                app.getModelSynchronize().confirm();
            } else if (gameView.needNoPower) {
                app.getModelSynchronize().confirm();
            } else {
                app.getAcousticHandler().playSound(MdgaSound.WRONG_INPUT);
            }
        }

        if (this instanceof CeremonyView ceremonyView) {
            ceremonyView.forward();
        }
    }

    /**
     * Method to show information on the view.
     *
     * @param error the error message
     * @param isError flag indicating if it is an error
     */
    public void showInfo(String error, boolean isError) {
        infoTimer.reset();

        if (null != infoLabel) {
            infoLabel.hide();
        }

        infoLabel = new LabelButton(app, guiNode, error, new Vector2f(5.5f, 2), new Vector2f(0.5f, AbstractButton.VERTICAL - 0.5f), false);

        ColorRGBA color;

        if (isError) {
            color = ColorRGBA.Red.clone();
        } else {
            color = ColorRGBA.Green.clone();
        }

        infoLabel.setColor(ColorRGBA.Black, color);
    }
}
