package pp.mdga.client.animation;

import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh.Type;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import pp.mdga.client.MdgaApp;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * MatrixAnimation class handles the animation of radar and matrix particle effects.
 */
public class MatrixAnimation extends ActionControl {
    private MdgaApp app;
    private static final Random RANDOM = new Random();
    private Vector3f radarPos;
    private Runnable runnable;
    private boolean init = false;
    private List<ParticleEmitter> activeEmitter = new ArrayList<>();
    private ParticleEmitter radarEmitter = null;
    private float timeElapsed = 0f;

    /**
     * Enum representing the states of the matrix animation.
     */
    private enum MatrixState {
        RADAR_ON,
        RADAR_OFF,
        MATRIX_ON,
        MATRIX_OFF
    }

    private MatrixState state;

    /**
     * Constructor for MatrixAnimation.
     *
     * @param app       the application instance
     * @param radarPos  the position of the radar
     * @param runnable  the runnable action to be executed
     */
    public MatrixAnimation(MdgaApp app, Vector3f radarPos, Runnable runnable) {
        super(runnable);
        this.app = app;
        this.radarPos = radarPos;
    }

    /**
     * Initializes the spatial and sets the initial state to RADAR_ON.
     */
    @Override
    protected void initSpatial() {
        state = MatrixState.RADAR_ON;
        timeElapsed = 0;
        init = true;
        radar();
    }

    /**
     * Updates the control based on the time per frame (tpf).
     *
     * @param tpf the time per frame
     */
    @Override
    protected void controlUpdate(float tpf) {
        if (!init) return;

        timeElapsed += tpf;

        switch (state) {
            case RADAR_ON -> {
                if (timeElapsed >= 2f) {
                    state = MatrixState.RADAR_OFF;
                    timeElapsed = 0;
                    radarEmitter.setParticlesPerSec(0);
                    app.getTimerManager().addTask(3f, () -> app.enqueue(() -> {
                        app.getRootNode().detachChild(radarEmitter);
                        System.out.println("delete radar");
                        return null;
                    }));
                }
            }
            case RADAR_OFF -> {
                if (timeElapsed >= 0.1f) {
                    state = MatrixState.MATRIX_ON;
                    timeElapsed = 0;
                    matrix();
                }
            }
            case MATRIX_ON -> {
                if (timeElapsed >= 3f) {
                    state = MatrixState.MATRIX_OFF;
                    timeElapsed = 0;
                    turnOff();
                    app.getTimerManager().addTask(3f, () -> app.enqueue(() -> {
                        for (ParticleEmitter particleEmitter : activeEmitter) {
                            app.getRootNode().detachChild(particleEmitter);
                        }
                        System.out.println("delete particle");
                        return null;
                    }));
                }
            }
            case MATRIX_OFF -> {
                if (timeElapsed >= 0.5f) {
                    init = false;
                    spatial.removeControl(this);
                    action();
                }
            }
        }
    }

    /**
     * Turns off all active particle emitters.
     */
    private void turnOff() {
        for (ParticleEmitter particleEmitter : activeEmitter) {
            particleEmitter.setParticlesPerSec(0f);
        }
    }

    /**
     * Initializes the radar particle emitter.
     */
    private void radar() {
        Material mat = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Particle.j3md");
        mat.setTexture("Texture", app.getAssetManager().loadTexture("Images/particle/radar_beam.png"));
        ParticleEmitter emitter = new ParticleEmitter("Effect", Type.Triangle, 50);
        emitter.setMaterial(mat);
        emitter.setImagesX(1); // columns
        emitter.setImagesY(1); // rows
        emitter.setSelectRandomImage(true);
        emitter.setStartColor(ColorRGBA.White);
        emitter.setEndColor(ColorRGBA.Black);
        emitter.getParticleInfluencer().setInitialVelocity(new Vector3f(0f, 0f, 2));
        emitter.getParticleInfluencer().setVelocityVariation(0f);
        emitter.setStartSize(0.1f);
        emitter.setEndSize(10);
        emitter.setGravity(0, 0, 0);
        float life = 2.6f;
        emitter.setLowLife(life);
        emitter.setHighLife(life);
        emitter.setLocalTranslation(radarPos.add(new Vector3f(0, 0, 5)));
        emitter.setParticlesPerSec(1.8f);
        app.getRootNode().attachChild(emitter);
        radarEmitter = emitter;
    }

    /**
     * Initializes multiple matrix particle streams.
     */
    private void matrix() {
        for (int i = 0; i < 5; i++) {
            particleStream(
                    generateMatrixColor(),
                    generateMatrixColor(),
                    getRandomFloat(0, 1f),
                    getRandomPosition(),
                    getRandomFloat(1, 2)
            );
        }
    }

    /**
     * Creates a particle stream with the specified parameters.
     *
     * @param start     the start color of the particles
     * @param end       the end color of the particles
     * @param speedVar  the speed variation of the particles
     * @param pos       the position of the particles
     * @param spawnVar  the spawn rate variation of the particles
     */
    private void particleStream(ColorRGBA start, ColorRGBA end, float speedVar, Vector3f pos, float spawnVar) {
        Material mat = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Particle.j3md");
        mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        mat.setTexture("Texture", app.getAssetManager().loadTexture("Images/particle/particle_cir.png"));
        ParticleEmitter matrix = new ParticleEmitter("Effect", Type.Triangle, 50);
        matrix.setMaterial(mat);
        matrix.setImagesX(2); // columns
        matrix.setImagesY(1); // rows
        matrix.setSelectRandomImage(true);
        matrix.setStartColor(start);
        matrix.setEndColor(end);
        matrix.getParticleInfluencer().setInitialVelocity(new Vector3f(0f, 0f, -6f - speedVar));
        matrix.getParticleInfluencer().setVelocityVariation(0f);
        matrix.setStartSize(0.4f);
        matrix.setEndSize(0.6f);
        matrix.setGravity(0, 0, 2f);
        matrix.setLowLife(3f);
        matrix.setHighLife(3f);
        matrix.setLocalTranslation(spatial.getLocalTranslation().add(pos).add(new Vector3f(0, 0, 15)));
        matrix.setParticlesPerSec(spawnVar);
        app.getRootNode().attachChild(matrix);
        activeEmitter.add(matrix);
    }

    /**
     * Generates a random position vector.
     *
     * @return a random position vector
     */
    public static Vector3f getRandomPosition() {
        // Generate a random angle in radians (0 to 2π)
        float angle = (float) (2 * Math.PI * RANDOM.nextDouble());

        // Generate a random radius with uniform distribution
        float radius = (float) Math.sqrt(RANDOM.nextDouble());
        radius *= 1f;

        // Convert polar coordinates to Cartesian
        float x = radius * (float) Math.cos(angle);
        float y = radius * (float) Math.sin(angle);

        return new Vector3f(x, y, 0);
    }

    /**
     * Generates a random float between the specified start and end values.
     *
     * @param start the start value
     * @param end   the end value
     * @return a random float between start and end
     */
    public static float getRandomFloat(float start, float end) {
        if (start > end) {
            throw new IllegalArgumentException("Start must be less than or equal to end.");
        }
        return start + RANDOM.nextFloat() * (end - start);
    }

    /**
     * Generates a random color for the matrix particles.
     *
     * @return a random ColorRGBA object
     */
    public static ColorRGBA generateMatrixColor() {
        // Red is dominant
        float red = 0.8f + RANDOM.nextFloat() * 0.2f;  // Red channel: 0.8 to 1.0
        // Green is moderately high
        float green = 0.4f + RANDOM.nextFloat() * 0.3f;  // Green channel: 0.4 to 0.7
        // Blue is minimal
        float blue = RANDOM.nextFloat() * 0.2f;  // Blue channel: 0.0 to 0.2
        float alpha = 1.0f;  // Fully opaque

        return new ColorRGBA(red, green, blue, alpha);
    }
}
