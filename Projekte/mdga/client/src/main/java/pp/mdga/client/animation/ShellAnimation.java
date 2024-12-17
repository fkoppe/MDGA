package pp.mdga.client.animation;

import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import pp.mdga.client.Asset;
import pp.mdga.client.MdgaApp;
import pp.mdga.client.acoustic.MdgaSound;
import pp.mdga.client.board.TankTopControl;

import java.util.Timer;
import java.util.TimerTask;

import static com.jme3.material.Materials.LIGHTING;

/**
 * ShellAnimation class handles the animation of a shell being fired from a tank.
 */
public class ShellAnimation extends ActionControl {
    private static final float FLYING_DURATION = 1.25f;
    private static final float FLYING_HEIGHT = 12f;
    private TankTopControl tankTopControl;
    private MdgaApp app;

    /**
     * Constructor for ShellAnimation.
     *
     * @param tankTopControl the control for the tank top
     * @param app the application instance
     * @param actionAfter the action to perform after the animation
     */
    public ShellAnimation(TankTopControl tankTopControl, MdgaApp app, Runnable actionAfter) {
        super(actionAfter);
        this.tankTopControl = tankTopControl;
        this.app = app;
    }

    /**
     * Initializes the spatial for the animation.
     */
    @Override
    protected void initSpatial() {
        tankTopControl.rotate(spatial.getLocalTranslation(), this::shoot);
        app.getAcousticHandler().playSound(MdgaSound.TURRET_ROTATE);
        //app.getRootNode().attachChild(createShell());
    }

    /**
     * Calculates the shooting position based on the tank's turret rotation.
     *
     * @return the shooting position as a Vector3f
     */
    private Vector3f getShootPos() {
        Vector3f localOffset = new Vector3f(0, -5.4f, 2.9f);
        Quaternion turretRotation = tankTopControl.getSpatial().getLocalRotation();
        Vector3f transformedOffset = turretRotation.mult(localOffset);
        return tankTopControl.getSpatial().getLocalTranslation().add(transformedOffset);
    }

    /**
     * Handles the shooting action, including sound and visual effects.
     */
    private void shoot() {
        app.getAcousticHandler().playSound(MdgaSound.TANK_SHOOT);
        Vector3f shootPos = getShootPos();
        createEffect(
                shootPos,
                "Images/particle/flame.png",
                2, 2,
                1, 3,
                1f,
                0.3f, 0.7f,
                new ColorRGBA(1f, 0.8f, 0.4f, 0.5f),
                new ColorRGBA(1f, 0f, 0f, 0f)
        );
        createEffect(
                shootPos,
                "Images/particle/vapor_cloud.png",
                3, 3,
                0.3f, 0.8f,
                10,
                0.1f, 0.35f,
                new ColorRGBA(0.5f, 0.5f, 0.5f, 0.5f),
                ColorRGBA.Black
        );

        Spatial shell = createShell();
        app.getRootNode().attachChild(shell);
        shell.addControl(new ShellControl(this::hitExplosion, shootPos, spatial.getLocalTranslation(), FLYING_HEIGHT, FLYING_DURATION, app.getAssetManager()));
    }

    /**
     * Creates the shell model and sets its initial properties.
     *
     * @return the created shell as a Spatial
     */
    private Spatial createShell() {
        Spatial model = app.getAssetManager().loadModel(Asset.shell.getModelPath());
        model.scale(.16f);
        model.setLocalTranslation(tankTopControl.getSpatial().getLocalTranslation());

        Vector3f shootPos = tankTopControl.getSpatial().getLocalTranslation();
        Vector3f targetPos = spatial.getLocalTranslation();
        Vector3f direction = targetPos.subtract(shootPos).normalize();

        Quaternion rotation = new Quaternion();
        rotation.lookAt(direction, new Vector3f(1, 0, 0)); // Assuming UNIT_Y is the up vector

        model.setLocalRotation(rotation);
        model.rotate(FastMath.HALF_PI, 0, 0);

        Material mat = new Material(app.getAssetManager(), LIGHTING);
        mat.setBoolean("UseMaterialColors", true);
        ColorRGBA color = ColorRGBA.fromRGBA255(143, 117, 0, 255);
        mat.setColor("Diffuse", color);
        mat.setColor("Ambient", color);
        model.setMaterial(mat);
        return model;
    }

    /**
     * Handles the explosion effect when the shell hits a target.
     */
    private void hitExplosion() {
        app.getAcousticHandler().playSound(MdgaSound.TANK_EXPLOSION);
        createEffect(
                spatial.getLocalTranslation().setZ(1),
                "Images/particle/flame.png",
                2, 2,
                1, 5,
                2f,
                0.3f, 0.7f,
                new ColorRGBA(1f, 0.8f, 0.4f, 0.5f),
                new ColorRGBA(1f, 0f, 0f, 0f)
        );
        app.getTimerManager().addTask(0.8f, super::action);
    }

    /**
     * Creates a particle effect at the specified position.
     *
     * @param shootPos the position to create the effect
     * @param image the image to use for the particles
     * @param x the number of columns in the texture
     * @param y the number of rows in the texture
     * @param startSize the initial size of the particles
     * @param endSize the final size of the particles
     * @param velocity the initial velocity of the particles
     * @param lowLife the minimum lifetime of the particles
     * @param highLife the maximum lifetime of the particles
     * @param start the starting color of the particles
     * @param end the ending color of the particles
     */
    private void createEffect(Vector3f shootPos,
                              String image,
                              int x, int y,
                              float startSize, float endSize,
                              float velocity,
                              float lowLife, float highLife,
                              ColorRGBA start, ColorRGBA end) {
        // Create a particle emitter for the explosion
        ParticleEmitter explosionEmitter = new ParticleEmitter("Explosion", ParticleMesh.Type.Triangle, 100);
        Material explosionMat = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Particle.j3md");
        explosionMat.setTexture("Texture", app.getAssetManager().loadTexture(image));
        explosionEmitter.setMaterial(explosionMat);

        // Particle properties
        explosionEmitter.setImagesX(x); // Columns in the texture
        explosionEmitter.setImagesY(y); // Rows in the texture
        explosionEmitter.setSelectRandomImage(true); // Randomize images for variety

        explosionEmitter.setStartColor(start); // Bright yellowish orange
        explosionEmitter.setEndColor(end); // Fade to transparent red

        explosionEmitter.setStartSize(startSize); // Initial size
        explosionEmitter.setEndSize(endSize); // Final size
        explosionEmitter.setLowLife(lowLife); // Minimum lifetime of particles
        explosionEmitter.setHighLife(highLife); // Maximum lifetime of particles
        explosionEmitter.setGravity(0, 0, 1); // Gravity to pull particles down
        explosionEmitter.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 0, velocity));
        explosionEmitter.getParticleInfluencer().setVelocityVariation(1f); // Adds randomness to the initial velocity
        explosionEmitter.setFacingVelocity(true); // Particles face their velocity direction
        explosionEmitter.setLocalTranslation(shootPos);
        explosionEmitter.setParticlesPerSec(0);
        explosionEmitter.emitAllParticles();
        app.getRootNode().attachChild(explosionEmitter);
        app.getTimerManager().addTask(1, ()->app.getRootNode().detachChild(explosionEmitter));
    }
}
