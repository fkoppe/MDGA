package pp.mdga.client.button;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.simsilica.lemur.component.QuadBackgroundComponent;
import pp.mdga.client.Asset;
import pp.mdga.client.MdgaApp;
import pp.mdga.game.Color;

/**
 * Represents a button in a multiplayer lobby screen. The button supports multiple states
 * (not taken, self, other) and displays a 3D model alongside its label. It can also indicate readiness
 * and interactively respond to hover and click events.
 */
public class LobbyButton extends ClickButton {

    /**
     * Enum representing the possible ownership states of the lobby button.
     */
    public enum Taken {
        NOT,   // The button is not taken
        SELF,  // The button is taken by the user
        OTHER  // The button is taken by another user
    }

    /**
     * Color for a lobby button that is taken by another user.
     */
    static final ColorRGBA LOBBY_TAKEN = ColorRGBA.fromRGBA255(193, 58, 59, 100);

    /**
     * Color for a lobby button that is ready but not hovered.
     */
    static final ColorRGBA LOBBY_READY = ColorRGBA.fromRGBA255(55, 172, 190, 100);

    /**
     * Color for a lobby button that is ready and hovered.
     */
    static final ColorRGBA LOBBY_READY_HOVER = ColorRGBA.fromRGBA255(17, 211, 218, 100);

    /**
     * Color for a lobby button owned by the user in normal state.
     */
    static final ColorRGBA LOBBY_SELF_NORMAL = ColorRGBA.fromRGBA255(0, 151, 19, 100);

    /**
     * Color for a lobby button owned by the user when hovered.
     */
    static final ColorRGBA LOBBY_SELF_HOVER = ColorRGBA.fromRGBA255(0, 230, 19, 100);

    /**
     * Fixed width for the lobby button.
     */
    static final float WIDTH = 4.0f;

    /**
     * Node to which the 3D model associated with this button is attached.
     */
    private final Node node3d;

    /**
     * Indicates whether the 3D model should rotate.
     */
    private boolean rotate = false;

    /**
     * The 3D model displayed alongside the button.
     */
    private Spatial model;

    /**
     * The rotation angle of the 3D model.
     */
    private float rot = 180;

    /**
     * The current ownership state of the lobby button.
     */
    private Taken taken = Taken.NOT;

    /**
     * Label displayed on the lobby button.
     */
    private LabelButton label;

    /**
     * Indicates whether the button represents a ready state.
     */
    private boolean isReady = false;

    /**
     * Constructs a LobbyButton with specified properties, including a 3D model and label.
     *
     * @param app    the application instance for accessing resources
     * @param node   the node in the scene graph to which the button belongs
     * @param node3d the node for 3D scene components associated with this button
     * @param action the action to execute when the button is clicked
     * @param tsk    the type or category of the button (e.g., CYBER, AIRFORCE)
     */
    public LobbyButton(MdgaApp app, Node node, Node node3d, Runnable action, Color tsk) {
        super(app, node, action, "", new Vector2f(WIDTH, 7), new Vector2f(0, 0));

        this.node3d = node3d;

        label = new LabelButton(app, node, "- leer -", new Vector2f(WIDTH, 1), new Vector2f(0, 0), true);

        final float mid = HORIZONTAL / 2;
        final float uiSpacing = 0.4f;
        final float figSpacing = 0.51f;

        float uiX = mid;
        float figX = 0;
        Asset asset = null;

        // Configure the button based on its type
        switch (tsk) {
            case CYBER:
                adjust = true;
                label.adjust = true;
                uiX -= 3 * uiSpacing;
                uiX -= WIDTH / 2;
                asset = Asset.cir;
                figX -= 3 * figSpacing;
                instance.setText("CIR");
                break;
            case AIRFORCE:
                adjust = true;
                label.adjust = true;
                uiX -= uiSpacing;
                asset = Asset.lw;
                figX -= figSpacing;
                instance.setText("Luftwaffe");
                break;
            case ARMY:
                uiX += uiSpacing;
                asset = Asset.heer;
                figX += figSpacing;
                instance.setText("Heer");
                break;
            case NAVY:
                uiX += 3 * uiSpacing;
                uiX += WIDTH / 2;
                asset = Asset.marine;
                figX += 3 * figSpacing;
                instance.setText("Marine");
                break;
        }

        setPos(new Vector2f(uiX, 6));
        label.setPos(new Vector2f(uiX, 7));

        createModel(asset, new Vector3f(figX, -0.55f, 6));
    }

    /**
     * Handles hover behavior, updating the button's color and enabling rotation.
     */
    @Override
    public void onHover() {
        ColorRGBA buttonPressed = BUTTON_PRESSED.clone();

        switch (taken) {
            case NOT:
                buttonPressed.a = 0.3f;
                break;
            case SELF:
                buttonPressed = LOBBY_SELF_HOVER;
                break;
            case OTHER:
                buttonPressed = LOBBY_TAKEN;
                break;
        }

        if (isReady) {
            buttonPressed = LOBBY_READY_HOVER;
        }

        QuadBackgroundComponent background = new QuadBackgroundComponent(buttonPressed);
        instance.setBackground(background);

        rotate = true;
    }

    /**
     * Handles unhover behavior, restoring the button's color and disabling rotation.
     */
    @Override
    public void onUnHover() {
        ColorRGBA buttonNormal = BUTTON_NORMAL.clone();

        switch (taken) {
            case NOT:
                buttonNormal.a = 0.3f;
                break;
            case SELF:
                buttonNormal = LOBBY_SELF_NORMAL;
                break;
            case OTHER:
                buttonNormal = LOBBY_TAKEN;
                break;
        }

        if (isReady) {
            buttonNormal = LOBBY_READY;
        }

        QuadBackgroundComponent background = new QuadBackgroundComponent(buttonNormal);
        instance.setBackground(background);

        rotate = false;
    }

    /**
     * Displays the lobby button and its associated components.
     */
    @Override
    public void show() {
        release();

        calculateRelative();
        setRelative();

        node.attachChild(instance);
        node3d.attachChild(model);

        label.show();
    }

    /**
     * Hides the lobby button and its associated components.
     */
    @Override
    public void hide() {
        node.detachChild(instance);
        node3d.detachChild(model);

        label.hide();
    }

    /**
     * Updates the 3D model's rotation if the button is being hovered.
     *
     * @param tpf time per frame, used for smooth rotation calculations
     */
    public void update(float tpf) {
        if (rotate) {
            rot += 140.0f * tpf;
            rot %= 360;
        } else {
            rot = 180;
        }

        model.setLocalRotation(new Quaternion().fromAngles(
                (float) Math.toRadians(90),
                (float) Math.toRadians(rot),
                (float) Math.toRadians(180)
        ));
    }

    /**
     * Creates the 3D model associated with the lobby button and applies textures and positioning.
     *
     * @param asset the asset representing the 3D model
     * @param pos   the initial position of the 3D model
     */
    private void createModel(Asset asset, Vector3f pos) {
        String modelName = asset.getModelPath();
        String texName = asset.getDiffPath();

        model = app.getAssetManager().loadModel(modelName);
        model.scale(asset.getSize() / 2);
        model.rotate(
                (float) Math.toRadians(90),
                (float) Math.toRadians(rot),
                (float) Math.toRadians(180)
        );
        model.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);

        model.setLocalTranslation(pos);

        Material mat = new Material(app.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
        mat.setTexture("DiffuseMap", app.getAssetManager().loadTexture(texName));
        model.setMaterial(mat);
    }

    /**
     * Gets the current ownership state of the lobby button.
     *
     * @return the current state of the button
     */
    public Taken getTaken() {
        return taken;
    }

    /**
     * Sets the ownership state of the lobby button and updates its label accordingly.
     *
     * @param taken the new ownership state
     * @param name  the name to display on the button
     */
    public void setTaken(Taken taken, String name) {
        this.taken = taken;

        if (taken == Taken.NOT) {
            label.setText("- leer -");
            isReady = false;
        } else {
            label.setText(name);
        }
        onUnHover();
    }

    /**
     * Sets the ready state of the lobby button and updates its appearance.
     *
     * @param isReady whether the button represents a ready state
     */
    public void setReady(boolean isReady) {
        this.isReady = isReady;
        onUnHover();
    }
}

