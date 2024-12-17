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
 * Represents a button used in a ceremony screen, with 3D model integration, customizable
 * appearance based on type, and interactive behavior. The button can rotate and display
 * different positions such as FIRST, SECOND, THIRD, and LOST.
 */
public class CeremonyButton extends ClickButton {

    /**
     * Enum representing the possible positions of the button in the ceremony screen.
     */
    public enum Pos {
        FIRST,
        SECOND,
        THIRD,
        LOST,
    }

    /**
     * Fixed width of the button in the UI layout.
     */
    static final float WIDTH = 4.0f;

    /**
     * Node to which the 3D model associated with this button is attached.
     */
    private final Node node3d;

    /**
     * Flag to determine if the button's 3D model should rotate.
     */
    private boolean rotate = false;

    /**
     * The 3D model associated with the button.
     */
    private Spatial model;

    /**
     * Current rotation angle of the button's 3D model.
     */
    private float rot = 180;

    /**
     * The taken state of the button (default is NOT taken).
     */
    private LobbyButton.Taken taken = LobbyButton.Taken.NOT;

    /**
     * A label associated with the button for displaying additional information.
     */
    private LabelButton label;

    /**
     * Constructs a CeremonyButton with specified attributes such as type, position, and label.
     * The button supports both 2D and 3D components for UI and visual effects.
     *
     * @param app    the application instance for accessing resources and settings
     * @param node   the node in the scene graph to which the button belongs
     * @param node3d the node for 3D scene components associated with this button
     * @param tsk    the type/color associated with the button
     * @param pos    the position of the button in the ceremony layout
     * @param name   the label or name displayed on the button
     */
    public CeremonyButton(MdgaApp app, Node node, Node node3d, Color tsk, Pos pos, String name) {
        super(app, node, () -> {
        }, "", new Vector2f(WIDTH, 7), new Vector2f(0, 0));

        this.node3d = node3d;

        label = new LabelButton(app, node, name, new Vector2f(WIDTH, 1), new Vector2f(0, 0), true);

        final float mid = HORIZONTAL / 2;
        final float uiSpacing = 1.4f;
        final float figSpacingX = 0.9f;
        final float figSpacingY = 0.25f;

        float uiX = mid;
        float uiY = 6;
        float figX = 0;
        float figY = -0.32f;

        Asset asset = switch (tsk) {
            case CYBER -> {
                instance.setText("CIR");
                yield Asset.cir;
            }
            case AIRFORCE -> {
                instance.setText("Luftwaffe");
                yield Asset.lw;
            }
            case ARMY -> {
                instance.setText("Heer");
                yield Asset.heer;
            }
            case NAVY -> {
                instance.setText("Marine");
                yield Asset.marine;
            }
            default -> throw new RuntimeException("None is not valid");
        };

        switch (pos) {
            case FIRST:
                rotate = true;
                uiX = 0;
                figY -= 1 * figSpacingY;
                break;
            case SECOND:
                adjust = true;
                label.adjust = true;
                uiX -= uiSpacing;
                uiY -= 1;
                figX -= figSpacingX;
                figY -= 2 * figSpacingY;
                figY -= 0.1f;
                break;
            case THIRD:
                uiX += uiSpacing;
                uiY -= 1.5f;
                figX += figSpacingX;
                figY -= 3 * figSpacingY;
                figY -= 0.07f;
                break;
            case LOST:
                adjust = true;
                label.adjust = true;
                uiX -= 2 * uiSpacing + 0.4f;
                uiX -= WIDTH / 2;
                uiY -= 2.0f;
                figX -= 2.5f * figSpacingX + 0.05f;
                figY -= 4.5f * figSpacingY;
                break;
        }

        setPos(new Vector2f(uiX, uiY));
        label.setPos(new Vector2f(uiX, uiY + 1));

        createModel(asset, new Vector3f(figX, figY, 6));
    }

    /**
     * Handles hover behavior by changing the button's background appearance.
     */
    @Override
    public void onHover() {
        ColorRGBA buttonNormal = BUTTON_NORMAL.clone();
        buttonNormal.a = 0.1f;

        QuadBackgroundComponent background = new QuadBackgroundComponent(buttonNormal);
        instance.setBackground(background);
    }

    /**
     * Handles unhover behavior by resetting the button's background appearance.
     */
    @Override
    public void onUnHover() {
        ColorRGBA buttonNormal = BUTTON_NORMAL.clone();
        buttonNormal.a = 0.1f;

        QuadBackgroundComponent background = new QuadBackgroundComponent(buttonNormal);
        instance.setBackground(background);
    }

    /**
     * Displays the button along with its 3D model and associated label.
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
     * Hides the button along with its 3D model and associated label.
     */
    @Override
    public void hide() {
        node.detachChild(instance);
        node3d.detachChild(model);

        label.hide();
    }

    /**
     * Updates the rotation of the button's 3D model over time.
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
     * Creates a 3D model associated with the button and applies its materials and position.
     *
     * @param asset the asset representing the 3D model and texture
     * @param pos   the initial position of the model in 3D space
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
}
