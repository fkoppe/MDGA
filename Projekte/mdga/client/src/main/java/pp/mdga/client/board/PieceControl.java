package pp.mdga.client.board;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import pp.mdga.client.Asset;
import pp.mdga.client.MdgaApp;
import pp.mdga.client.outline.OutlineControl;

/**
 * A control that manages the behavior and properties of a game piece, such as its rotation,
 * position, shield activation, and highlighting. This class extends {@link OutlineControl}
 * to provide outline functionality and includes additional features like shield effects,
 * hover states, and selection states.
 */
public class PieceControl extends OutlineOEControl {
    private final float initRotation;
    private final AssetManager assetManager;
    private Spatial shieldRing;
    private final Material shieldMat;

    private static final float SHIELD_SPEED = 1f;
    private static final float SHIELD_TRANSPARENCY = 0.6f;
    private static final ColorRGBA SHIELD_COLOR = new ColorRGBA(0, 0.9f, 1, SHIELD_TRANSPARENCY);
    private static final ColorRGBA SHIELD_SUPPRESSED_COLOR = new ColorRGBA(1f, 0.5f, 0, SHIELD_TRANSPARENCY);
    private static final float SHIELD_Z = 0f;

    private final Node parentNode;
    private boolean enemy;
    private boolean hoverable;
    private boolean highlight;
    private boolean selectable;
    private boolean select;

    /**
     * Constructs a {@link PieceControl} with the specified initial rotation, asset manager,
     * application, and post-processor.
     *
     * @param initRotation The initial rotation of the piece in degrees.
     * @param assetManager The {@link AssetManager} used for loading models and materials.
     * @param app          The {@link MdgaApp} instance to use for the application context.
     * @param fpp          The {@link FilterPostProcessor} to apply post-processing effects.
     */
    public PieceControl(float initRotation, AssetManager assetManager, MdgaApp app, FilterPostProcessor fpp) {
        super(app, fpp, app.getCamera());
        this.parentNode = new Node();
        this.initRotation = initRotation;
        this.assetManager = assetManager;
        this.shieldRing = null;
        this.shieldMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        this.shieldMat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
    }

    /**
     * Gets the current rotation of the piece in degrees.
     *
     * @return The rotation of the piece in degrees.
     */
    public float getRotation() {
        return (float) Math.toDegrees(spatial.getLocalRotation().toAngleAxis(new Vector3f(0, 0, 1)));
    }

    /**
     * Sets the rotation of the piece to the specified value in degrees.
     *
     * @param rot The rotation in degrees to set.
     */
    public void setRotation(float rot) {
        if (rot < 0) rot = -360;

        Quaternion quaternion = new Quaternion();
        quaternion.fromAngleAxis((float) Math.toRadians(rot), new Vector3f(0, 0, 1));
        spatial.setLocalRotation(quaternion);
    }

    /**
     * Gets the current location (position) of the piece.
     *
     * @return The location of the piece as a {@link Vector3f}.
     */
    public Vector3f getLocation() {
        return spatial.getLocalTranslation();
    }

    /**
     * Updates the piece control every frame. If the shield is active, it will rotate.
     *
     * @param delta The time difference between frames (time per frame).
     */
    @Override
    protected void controlUpdate(float delta) {
        if (shieldRing != null) {
            shieldRing.rotate(0, 0, delta * SHIELD_SPEED);
            shieldRing.setLocalTranslation(spatial.getLocalTranslation().add(new Vector3f(0, 0, SHIELD_Z)));
        }
    }

    /**
     * Sets the location (position) of the piece.
     *
     * @param loc The location to set as a {@link Vector3f}.
     */
    public void setLocation(Vector3f loc) {
        this.spatial.setLocalTranslation(loc);
    }

    /**
     * Initializes the spatial object and sets its rotation.
     * This also moves the spatial to a new parent node for organizational purposes.
     */
    @Override
    public void initSpatial() {
        setRotation(this.initRotation);

        Node oldParent = spatial.getParent();
        this.parentNode.setName(spatial.getName() + " Parent");
        oldParent.detachChild(this.getSpatial());
        this.parentNode.attachChild(this.getSpatial());
        oldParent.attachChild(this.parentNode);
    }

    public void rotateInit() {
        setRotation(initRotation);
    }

    /**
     * Activates the shield around the piece.
     * This adds a visual shield effect in the form of a rotating ring.
     */
    public void activateShield() {
        if (shieldRing != null) {
            deactivateShield();
        }
        shieldRing = assetManager.loadModel(Asset.shieldRing.getModelPath());
        shieldRing.scale(1f);
        shieldRing.rotate((float) Math.toRadians(0), 0, (float) Math.toRadians(0));
        shieldRing.setLocalTranslation(spatial.getLocalTranslation().add(new Vector3f(0, 0, SHIELD_Z)));


        shieldRing.setQueueBucket(RenderQueue.Bucket.Transparent); // Render in the transparent bucket
        shieldMat.setColor("Color", SHIELD_COLOR);
        shieldRing.setMaterial(shieldMat);

        parentNode.attachChild(shieldRing);
    }

    /**
     * Deactivates the shield by removing the shield ring from the scene.
     */

    public void deactivateShield() {
        parentNode.detachChild(shieldRing);
        shieldRing = null;
    }

    /**
     * Suppresses the shield, changing its color to a suppressed state.
     */
    public void suppressShield() {
        assert (shieldRing != null) : "PieceControl: shieldRing is not set";
        shieldMat.setColor("Color", SHIELD_SUPPRESSED_COLOR);
    }

    public void setMaterial(Material mat) {
        spatial.setMaterial(mat);
    }

    public Material getMaterial() {
        return ((Geometry) getSpatial()).getMaterial();
    }
}
