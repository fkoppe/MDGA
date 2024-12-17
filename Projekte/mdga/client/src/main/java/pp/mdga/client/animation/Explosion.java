package pp.mdga.client.animation;

import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh.Type;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.control.AbstractControl;
import pp.mdga.client.MdgaApp;
import pp.mdga.client.acoustic.MdgaSound;

/**
 * The {@code Explosion} class represents an explosion effect in a 3D environment.
 * It manages the creation, configuration, and triggering of particle emitters for fire and smoke effects.
 */
public class Explosion {

    private final Node rootNode;
    private final MdgaApp app;
    private final Vector3f location;
    private ParticleEmitter fire;
    private ParticleEmitter smoke;

    private boolean triggered = false;

    private final Material fireMat;
    private final Material smokeMat;

    /**
     * Constructor for the {@code Explosion} class.
     *
     * @param app      The main application managing the explosion.
     * @param rootNode The root node to which the explosion effects will be attached.
     * @param location The location of the explosion in world coordinates.
     */
    public Explosion(MdgaApp app, Node rootNode, Vector3f location) {
        this.app = app;
        this.rootNode = rootNode;
        this.location = location;

        this.fireMat = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Particle.j3md");
        this.smokeMat = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Particle.j3md");
        fireMat.setTexture("Texture", app.getAssetManager().loadTexture("Images/particle/flame.png"));
        smokeMat.setTexture("Texture", app.getAssetManager().loadTexture("Images/particle/vapor_cloud.png"));
    }

    /**
     * Initializes the particle emitters for the explosion effect.
     * Configures the fire and smoke emitters with appearance, behavior, and lifespan.
     */
    private void initializeEmitter() {
        fire = new ParticleEmitter("Effect", Type.Triangle, 50);
        fire.setMaterial(fireMat);
        fire.setImagesX(2);
        fire.setImagesY(2);
        fire.setSelectRandomImage(true);
        fire.setStartColor(ColorRGBA.Yellow);
        fire.setEndColor(ColorRGBA.Red);
        fire.getParticleInfluencer().setInitialVelocity(new Vector3f(0.2f, 0.2f, 4f));
        fire.getParticleInfluencer().setVelocityVariation(0.4f);
        fire.setStartSize(0.7f);
        fire.setEndSize(1.8f);
        fire.setGravity(0, 0, -0.1f);
        fire.setLowLife(0.5f);
        fire.setHighLife(2.2f);
        fire.setParticlesPerSec(0);

        fire.setLocalTranslation(location);

        smoke = new ParticleEmitter("Effect2", Type.Triangle, 40);
        smoke.setMaterial(smokeMat);
        smoke.setImagesX(3);
        smoke.setImagesY(3);
        smoke.setSelectRandomImage(true);
        smoke.setStartColor(new ColorRGBA(0.5f, 0.5f, 0.5f, 0.5f));
        smoke.setEndColor(new ColorRGBA(ColorRGBA.Black));
        smoke.getParticleInfluencer().setInitialVelocity(new Vector3f(0.0f, 0.0f, 1.5f));
        smoke.getParticleInfluencer().setVelocityVariation(0.5f);
        smoke.setStartSize(0.8f);
        smoke.setEndSize(1.5f);
        smoke.setGravity(0, 0, -0.3f);
        smoke.setLowLife(1.2f);
        smoke.setHighLife(5.5f);
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

        rootNode.attachChild(fire);
        fire.emitAllParticles();
        fire.addControl(new AbstractControl() {
            private float elapsedTime = 0;

            @Override
            protected void controlUpdate(float tpf) {
                elapsedTime += tpf;
                if (elapsedTime > 10f) {
                    rootNode.detachChild(fire);
                    fire.removeControl(this);
                }
            }

            @Override
            protected void controlRender(com.jme3.renderer.RenderManager rm, com.jme3.renderer.ViewPort vp) {
            }
        });

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

