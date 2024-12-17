package pp.mdga.client.view;

import com.jme3.asset.TextureKey;
import com.jme3.light.AmbientLight;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Texture;
import pp.mdga.client.MdgaApp;
import pp.mdga.client.MdgaState;
import pp.mdga.client.acoustic.MdgaSound;
import pp.mdga.client.button.ButtonLeft;
import pp.mdga.client.button.ButtonRight;
import pp.mdga.client.button.CeremonyButton;
import pp.mdga.client.dialog.CeremonyDialog;
import pp.mdga.game.Color;

import java.util.ArrayList;

/**
 * CeremonyView class handles the display and interaction of the ceremony view in the application.
 */
public class CeremonyView extends MdgaView {
    /**
     * Enum representing the sub-states of the CeremonyView.
     */
    private enum SubState {
        AWARD_CEREMONY,
        STATISTICS,
    }

    private SubState state;

    private Geometry background;
    private Geometry podest;

    private ButtonLeft backButton;
    private ButtonRight continueButton;

    private ArrayList<CeremonyButton> ceremonyButtons;

    private CeremonyDialog ceremonyDialog;

    private AmbientLight ambient = new AmbientLight();

    /**
     * Constructor for CeremonyView.
     *
     * @param app The application instance.
     */
    public CeremonyView(MdgaApp app) {
        super(app);

        backButton = new ButtonLeft(app, guiNode, this::back, "Zurück", 1);
        continueButton = new ButtonRight(app, guiNode, this::forward, "Weiter", 1);

        ceremonyButtons = new ArrayList<>(4);

        ceremonyDialog = new CeremonyDialog(app, guiNode);

        ambient.setColor(new ColorRGBA(0.3f, 0.3f, 0.3f, 1));
    }

    /**
     * Method called when entering the CeremonyView.
     */
    @Override
    public void onEnter() {
        rootNode.addLight(ambient);

        app.getAcousticHandler().playSound(MdgaSound.VICTORY);

        float screenWidth = app.getCamera().getWidth();
        float screenHeight = app.getCamera().getHeight();
        float aspectRatio = screenWidth / screenHeight;

        float scale = 3.5f;

        float distanceFromCamera = 5f;
        float verticalSize = (float) (2 * Math.tan(Math.toRadians(app.getCamera().getFov() / 2)) * distanceFromCamera * scale);
        float horizontalSize = verticalSize * aspectRatio;

        Quad backgroundQuad = new Quad(horizontalSize, verticalSize);
        background = new Geometry("LobbyBackground", backgroundQuad);

        TextureKey backgroundKey = new TextureKey("Images/lobby.png", true);
        Texture backgroundTexture = app.getAssetManager().loadTexture(backgroundKey);
        Material backgroundMaterial = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        backgroundMaterial.setTexture("ColorMap", backgroundTexture);
        background.setMaterial(backgroundMaterial);
        background.setLocalTranslation(-horizontalSize / 2, -verticalSize / 2, -distanceFromCamera);
        rootNode.attachChild(background);

        verticalSize *= 0.99f;

        Quad overlayQuad = new Quad(horizontalSize, verticalSize * 0.8f);
        podest = new Geometry("TransparentOverlay", overlayQuad);

        TextureKey overlayKey = new TextureKey("Images/Ceremony.png", true);
        Texture overlayTexture = app.getAssetManager().loadTexture(overlayKey);
        Material overlayMaterial = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        overlayMaterial.setTexture("ColorMap", overlayTexture);

        overlayMaterial.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        podest.setMaterial(overlayMaterial);

        float overlayDistance = distanceFromCamera - 0.1f;
        podest.setLocalTranslation(-horizontalSize / 2, -verticalSize * 0.415f, -overlayDistance);

        enterSub(SubState.AWARD_CEREMONY);
    }

    /**
     * Method called when leaving the CeremonyView.
     */
    @Override
    public void onLeave() {
        backButton.hide();
        continueButton.hide();

        if (null != background) {
            guiNode.detachChild(background);
        }

        ceremonyButtons.clear();

        rootNode.removeLight(ambient);

        ceremonyDialog.prepare();

        rootNode.detachChild(background);
    }

    /**
     * Method called when entering an overlay.
     *
     * @param overlay The overlay being entered.
     */
    @Override
    protected void onEnterOverlay(Overlay overlay) {
        if (rootNode.hasChild(podest)) {
            rootNode.detachChild(podest);
        }
    }

    /**
     * Method called when leaving an overlay.
     *
     * @param overlay The overlay being left.
     */
    @Override
    protected void onLeaveOverlay(Overlay overlay) {
        enterSub(state);
    }

    /**
     * Method called to update the CeremonyView.
     *
     * @param tpf Time per frame.
     */
    @Override
    protected void onUpdate(float tpf) {
        for (CeremonyButton c : ceremonyButtons) {
            c.update(tpf);
        }
    }

    /**
     * Handles the award ceremony sub-state.
     */
    private void awardCeremony() {
        continueButton.show();

        rootNode.attachChild(podest);

        for (CeremonyButton c : ceremonyButtons) {
            c.show();
        }
    }

    /**
     * Handles the statistics sub-state.
     */
    private void statistics() {
        //background = createBackground("Images/b2.png");
        //guiNode.attachChild(background);

        backButton.show();
        continueButton.show();
        ceremonyDialog.show();
    }

    /**
     * Enters a sub-state of the CeremonyView.
     *
     * @param state The sub-state to enter.
     */
    private void enterSub(SubState state) {
        this.state = state;

        if (rootNode.hasChild(podest)) {
            rootNode.detachChild(podest);
        }

        backButton.hide();
        continueButton.hide();
        for (CeremonyButton c : ceremonyButtons) {
            c.hide();
        }
        ceremonyDialog.hide();

        switch (state) {
            case AWARD_CEREMONY:
                awardCeremony();
                break;
            case STATISTICS:
                statistics();
                break;
        }
    }

    /**
     * Handles the forward button action.
     */
    public void forward() {
        switch (state) {
            case AWARD_CEREMONY:
                enterSub(SubState.STATISTICS);
                break;
            case STATISTICS:
                app.getModelSynchronize().next();
                break;
        }
    }

    /**
     * Handles the back button action.
     */
    private void back() {
        switch (state) {
            case AWARD_CEREMONY:
                //nothing
                break;
            case STATISTICS:
                enterSub(SubState.AWARD_CEREMONY);
                break;
        }
    }

    /**
     * Adds a participant to the ceremony.
     *
     * @param color The color of the participant.
     * @param pos The position of the participant.
     * @param name The name of the participant.
     */
    public void addCeremonyParticipant(Color color, int pos, String name) {
        CeremonyButton button = new CeremonyButton(app, guiNode, rootNode, color, CeremonyButton.Pos.values()[pos - 1], name);

        ceremonyButtons.add(button);

        if (state != null && state.equals(SubState.AWARD_CEREMONY)) {
            button.hide();
            button.show();
        }
    }

    /**
     * Adds a row of statistics.
     *
     * @param name The name of the row.
     * @param v1 Value 1.
     * @param v2 Value 2.
     * @param v3 Value 3.
     * @param v4 Value 4.
     * @param v5 Value 5.
     * @param v6 Value 6.
     */
    public void addStatisticsRow(String name, int v1, int v2, int v3, int v4, int v5, int v6) {
        ceremonyDialog.addStatisticsRow(name, v1, v2, v3, v4, v5, v6);

        ceremonyDialog.hide();
        ceremonyDialog.show();
    }

    /**
     * Cleans up after the game.
     */
    public void afterGameCleanup() {
        ceremonyDialog.prepare();
    }
}
