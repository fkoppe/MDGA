package pp.mdga.client.gui;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import pp.mdga.client.Asset;
import pp.mdga.client.MdgaApp;

import static com.jme3.material.Materials.LIGHTING;
import static com.jme3.material.Materials.UNSHADED;

/**
 * DiceControl class handles the rolling and spinning behavior of a dice in the game.
 */
public class DiceControl extends AbstractControl {
    private Quaternion targetRotation;
    private final Vector3f angularVelocity = new Vector3f();
    private float deceleration = 1f;
    private float timeElapsed = 0.0f;
    private float rollDuration = 1f;
    private static final int ANGULAR_MIN = 5;
    private static final int ANGULAR_MAX = 15;
    private static final int ANGULAR_SPIN = 10;
    private boolean isRolling = false;
    private boolean slerp = false;
    private boolean spin = false;
    private final AssetManager assetManager;
    private Runnable actionAfter;

    /**
     * Constructor for DiceControl.
     *
     * @param assetManager the asset manager to load models and textures
     */
    public DiceControl(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    /**
     * Updates the control each frame.
     *
     * @param tpf time per frame
     */
    @Override
    protected void controlUpdate(float tpf) {
        float clampedTpf = Math.min(tpf, 0.05f); // Max 50 ms per frame
        if (isRolling) {
            if (!slerp) {
                // Apply rotational velocity to the dice
                spinWithAngularVelocity(clampedTpf);

                // Gradually reduce rotational velocity (simulate deceleration)
                angularVelocity.subtractLocal(
                        angularVelocity.mult(deceleration * clampedTpf)
                );

                // Stop rolling when angular velocity is close to zero
                if (angularVelocity.lengthSquared() <= 3f || MdgaApp.DEBUG_MULTIPLIER == 0) {
                    slerp = true;
                }
            } else {
                timeElapsed += clampedTpf * rollDuration;

                if (timeElapsed > 1.0f) timeElapsed = 1.0f;
                Quaternion interpolated = spatial.getLocalRotation().clone();
                interpolated.slerp(targetRotation, timeElapsed);
                spatial.setLocalRotation(interpolated);

                // Stop rolling once duration is complete
                if (timeElapsed >= 1.0f * MdgaApp.DEBUG_MULTIPLIER) {
                    isRolling = false;
                    slerp = false;
                    actionAfter.run();
                }
            }
        } else if (spin) {
            spinWithAngularVelocity(clampedTpf);
        }
    }

    /**
     * Applies rotational velocity to the dice.
     *
     * @param tpf time per frame
     */
    private void spinWithAngularVelocity(float tpf) {
        Quaternion currentRotation = spatial.getLocalRotation();
        Quaternion deltaRotation = new Quaternion();
        deltaRotation.fromAngles(
                angularVelocity.x * tpf,
                angularVelocity.y * tpf,
                angularVelocity.z * tpf
        );
        spatial.setLocalRotation(currentRotation.mult(deltaRotation));
    }

    /**
     * Renders the control.
     *
     * @param rm the render manager
     * @param vp the viewport
     */
    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }

    /**
     * Initiates the dice roll.
     *
     * @param diceNum the number on the dice to roll to
     * @param actionAfter the action to perform after the roll
     */
    public void rollDice(int diceNum, Runnable actionAfter) {
        if (isRolling) return;
        spin = false;
        slerp = false;
        timeElapsed = 0;
        this.actionAfter = actionAfter;
        angularVelocity.set(
                FastMath.nextRandomInt(ANGULAR_MIN, ANGULAR_MAX),
                FastMath.nextRandomInt(ANGULAR_MIN, ANGULAR_MAX),
                FastMath.nextRandomInt(ANGULAR_MIN, ANGULAR_MAX)
        );

        targetRotation = getRotationForDiceNum(diceNum);

        isRolling = true;
    }

    /**
     * Gets the target rotation for a given dice number.
     *
     * @param diceNum the number on the dice
     * @return the target rotation as a Quaternion
     */
    private Quaternion getRotationForDiceNum(int diceNum) {
        return switch (diceNum) {
            case 1 -> new Quaternion().fromAngleAxis((float) (1 * (Math.PI / 2)), Vector3f.UNIT_X);
            case 2 -> new Quaternion().fromAngleAxis((float) (1 * (Math.PI / 2)), Vector3f.UNIT_Y);
            case 3 -> new Quaternion().fromAngleAxis((float) (0 * (Math.PI / 2)), Vector3f.UNIT_X);
            case 4 -> new Quaternion().fromAngleAxis((float) (2 * (Math.PI / 2)), Vector3f.UNIT_Y);
            case 5 -> new Quaternion().fromAngleAxis((float) (3 * (Math.PI / 2)), Vector3f.UNIT_Y);
            case 6 -> new Quaternion().fromAngleAxis((float) (3 * (Math.PI / 2)), Vector3f.UNIT_X);
            default -> throw new IllegalArgumentException("Invalid dice number: " + diceNum);
        };
    }

    /**
     * Linear interpolation function.
     *
     * @param t the interpolation factor
     * @return the interpolated value
     */
    public static float lerp(float t) {
        return (float) Math.sqrt(1 - Math.pow(t - 1, 2));
    }

    /**
     * Sets a random rotation for the dice.
     */
    public void randomRotation() {
        Quaternion randomRotation = new Quaternion();
        randomRotation.fromAngles(
                FastMath.nextRandomFloat() * FastMath.TWO_PI, // Random X rotation
                FastMath.nextRandomFloat() * FastMath.TWO_PI, // Random Y rotation
                FastMath.nextRandomFloat() * FastMath.TWO_PI  // Random Z rotation
        );
        spatial.setLocalRotation(randomRotation);
    }

    /**
     * Initiates the dice spin.
     */
    public void spin() {
        angularVelocity.set(ANGULAR_SPIN, ANGULAR_SPIN, ANGULAR_SPIN);
        spin = true;
    }

    /**
     * Hides the dice by removing it from the parent node.
     */
    public void hide() {
        spatial.removeFromParent();
        spin = false;
        isRolling = false;
        slerp = false;
    }

    /**
     * Creates the dice model and sets its initial properties.
     *
     * @param pos the position to place the dice
     * @param scale the scale of the dice
     * @param shadow whether the dice should cast and receive shadows
     */
    public void create(Vector3f pos, float scale, boolean shadow) {
        Spatial spatial = assetManager.loadModel(Asset.dice.getModelPath());
        Material mat;
        if (shadow) {
            mat = new Material(assetManager, LIGHTING);
            mat.setTexture("DiffuseMap", assetManager.loadTexture(Asset.dice.getDiffPath()));
            spatial.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        } else {
            mat = new Material(assetManager, UNSHADED);
            mat.setTexture("ColorMap", assetManager.loadTexture(Asset.dice.getDiffPath()));
        }
        spatial.setMaterial(mat);
        spatial.setLocalScale(scale);
        spatial.setLocalTranslation(pos);
        spatial.rotate((float) Math.toRadians(90), (float) Math.toRadians(180), (float) Math.toRadians(180));
        spatial.addControl(this);
    }

    /**
     * Sets the position of the dice.
     *
     * @param pos the new position
     */
    public void setPos(Vector3f pos) {
        spatial.setLocalTranslation(pos);
    }

}
