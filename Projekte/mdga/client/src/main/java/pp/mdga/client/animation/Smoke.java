package pp.mdga.client.animation;

import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.control.AbstractControl;
import pp.mdga.client.MdgaApp;
import pp.mdga.client.acoustic.MdgaSound;

public class Smoke {

    private final Node rootNode;
    private final MdgaApp app;
    private final Vector3f location;
    private ParticleEmitter smoke;

    private boolean triggered = false;

    private final Material mat;

    /**
     * Constructor for the {@code Explosion} class.
     *
     * @param app      The main application managing the explosion.
     * @param rootNode The root node to which the explosion effects will be attached.
     * @param location The location of the explosion in world coordinates.
     */
    public Smoke(MdgaApp app, Node rootNode, Vector3f location) {
        this.app = app;
        this.rootNode = rootNode;
        this.location = location;

        this.mat = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Particle.j3md");
        mat.setTexture("Texture", app.getAssetManager().loadTexture("Images/particle/vapor_cloud.png"));
    }

    /**
     * Initializes the particle emitters for the explosion effect.
     * Configures the fire and smoke emitters with appearance, behavior, and lifespan.
     */
    private void initializeEmitter() {
        smoke = new ParticleEmitter("Effect2", ParticleMesh.Type.Triangle, 50);
        smoke.setMaterial(mat);
        smoke.setImagesX(3);
        smoke.setImagesY(3);
        smoke.setSelectRandomImage(true);
        smoke.setStartColor(ColorRGBA.DarkGray);
        smoke.setEndColor(new ColorRGBA(0.05f, 0.05f, 0.05f, 1));
        smoke.getParticleInfluencer().setInitialVelocity(new Vector3f(0.2f, 0.2f, 4f));
        smoke.getParticleInfluencer().setVelocityVariation(0.5f);
        smoke.setStartSize(0.7f);
        smoke.setEndSize(3f);
        smoke.setGravity(0, 0, -0.3f);
        smoke.setLowLife(1.2f);
        smoke.setHighLife(2.5f);
        smoke.setParticlesPerSec(0);

        smoke.setLocalTranslation(location);

        app.getAcousticHandler().playSound(MdgaSound.EXPLOSION);
    }

    /**
     * Triggers the explosion effect by attaching and activating the particle emitters for fire and smoke.
     * Both emitters are automatically detached after a predefined duration.
     */
    public void trigger() {
        if (!triggered) {
            triggered = true;
            initializeEmitter();
        }

        rootNode.attachChild(smoke);
        smoke.emitAllParticles();
        smoke.addControl(new AbstractControl() {
            private float elapsedTime = 0;

            @Override
            protected void controlUpdate(float tpf) {
                elapsedTime += tpf;
                if (elapsedTime > 10f) {
                    rootNode.detachChild(smoke);
                    smoke.removeControl(this);
                }
            }

            @Override
            protected void controlRender(com.jme3.renderer.RenderManager rm, com.jme3.renderer.ViewPort vp) {
            }
        });
    }
}
