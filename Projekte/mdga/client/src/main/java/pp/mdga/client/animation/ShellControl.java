package pp.mdga.client.animation;

import com.jme3.asset.AssetManager;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;

/**
 * ShellControl is responsible for controlling the movement and visual effects of a shell.
 */
public class ShellControl extends ActionControl {
    private final Vector3f shootPos;
    private final Vector3f endPos;
    private final float height;
    private final float duration;
    private Vector3f oldPos;
    private ParticleEmitter emitter;
    private AssetManager assetManager;

    /**
     * Constructs a new ShellControl.
     *
     * @param runnable the action to perform when the shell reaches its destination
     * @param shootPos the starting position of the shell
     * @param endPos the ending position of the shell
     * @param height the height of the shell's trajectory
     * @param duration the duration of the shell's flight
     * @param assetManager the asset manager to load resources
     */
    public ShellControl(Runnable runnable, Vector3f shootPos, Vector3f endPos, float height, float duration, AssetManager assetManager) {
        super(runnable);
        this.shootPos = shootPos;
        this.endPos = endPos;
        this.height = height;
        this.duration = duration;
        this.assetManager = assetManager;
    }

    /**
     * Initializes the spatial with the necessary controls and particle emitter.
     */
    @Override
    protected void initSpatial() {
        spatial.addControl(new MoveControl(
                shootPos,
                endPos,
                () -> {
                    emitter.killAllParticles();
                    emitter.setParticlesPerSec(0);
                    emitter.removeFromParent();
                    spatial.removeControl(this);
                    spatial.removeFromParent();
                    action();
                },
                height,
                duration,
                false
        ));
        oldPos = spatial.getLocalTranslation().clone();
        createEmitter();
    }

    /**
     * Creates and configures the particle emitter for the shell trail.
     */
    private void createEmitter() {
        emitter = new ParticleEmitter("ShellTrail", ParticleMesh.Type.Triangle, 200);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
        mat.setTexture("Texture", assetManager.loadTexture("Images/particle/line.png")); // Nutze eine schmale, linienartige Textur
        emitter.setMaterial(mat);

        // Comic-Style Farben
        emitter.setStartColor(new ColorRGBA(1f, 1f, 1f, 1f)); // Reinweiß
        emitter.setEndColor(new ColorRGBA(1f, 1f, 1f, 0f)); // Transparent

        // Partikelgröße und Lebensdauer
        emitter.setStartSize(0.15f); // Startgröße
        emitter.setEndSize(0.1f); // Endgröße
        emitter.setLowLife(0.14f); // Sehr kurze Lebensdauer
        emitter.setHighLife(0.14f);

        emitter.setGravity(0, 0, 0); // Keine Gravitation
        emitter.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 0, 0));
        emitter.getParticleInfluencer().setVelocityVariation(0f); // Kein Variationsspielraum

        // Hohe Dichte für eine glatte Spur
        emitter.setParticlesPerSec(500);

        // Zur Shell hinzufügen
        spatial.getParent().attachChild(emitter);
    }

    /**
     * Updates the control, adjusting the shell's rotation and emitter position.
     *
     * @param tpf time per frame
     */
    @Override
    protected void controlUpdate(float tpf) {
        Vector3f direction = spatial.getLocalTranslation().subtract(oldPos).normalize();
        if (direction.lengthSquared() > 0) {
            spatial.getLocalRotation().lookAt(direction, Vector3f.UNIT_X);
            spatial.rotate(FastMath.HALF_PI, 0, 0);
        }
        oldPos = spatial.getLocalTranslation().clone();

        emitter.setLocalTranslation(spatial.getLocalTranslation().clone());
    }
}
