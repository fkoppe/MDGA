package pp.mdga.client.animation;

import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import pp.mdga.client.Asset;
import pp.mdga.client.MdgaApp;
import pp.mdga.client.acoustic.MdgaSound;
import pp.mdga.client.board.BoardHandler;

/**
 * The {@code MissileAnimation} class handles the animation of a missile moving along a parabolic path
 * towards a target point in a 3D environment. It also triggers an explosion at the target upon impact.
 */
public class MissileAnimation {

    private final Node rootNode;
    private final MdgaApp app;
    private final Vector3f start;
    private final Vector3f target;
    private final float flightTime;
    private Explosion explosion;
    private Spatial missileModel;
    private Runnable actionAfter;
    private ParticleEmitter smoke;

    private Node missileNode = new Node();

    private final Material mat;

    /**
     * Constructor for the {@code MissileAnimation} class.
     *
     * @param app        The main application managing the missile animation.
     * @param rootNode   The root node to which the missile model will be attached.
     * @param target     The target point where the missile will explode.
     * @param flightTime The total flight time of the missile.
     */
    public MissileAnimation(MdgaApp app, Node rootNode, Vector3f target, float flightTime, Runnable actionAfter) {
        this.app = app;
        this.rootNode = rootNode;
        this.flightTime = flightTime;
        this.actionAfter = actionAfter;

        explosion = new Explosion(app, rootNode, target);

        this.target = target.add(new Vector3f(1.5f, -1, 0));


        start = BoardHandler.gridToWorld(12, 0);
        start.add(new Vector3f(0, 0, 0));

        this.mat = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Particle.j3md");
        mat.setTexture("Texture", app.getAssetManager().loadTexture("Images/particle/vapor_cloud.png"));

        smoke = new ParticleEmitter("Effect2", ParticleMesh.Type.Triangle, 400);
        smoke.setMaterial(mat);
        smoke.setImagesX(3);
        smoke.setImagesY(3);
        smoke.setStartColor(ColorRGBA.DarkGray);
        smoke.setEndColor(new ColorRGBA(0.05f, 0.05f, 0.05f, 1));
        smoke.getParticleInfluencer().setInitialVelocity(new Vector3f(0.0f, 0.0f, 0.0f));
        smoke.getParticleInfluencer().setVelocityVariation(0.1f);
        smoke.setStartSize(0.8f);
        smoke.setEndSize(1.5f);
        smoke.setGravity(0, 0, -0.3f);
        smoke.setLowLife(1.2f);
        smoke.setHighLife(3.5f);
        smoke.setParticlesPerSec(100);
        missileNode.attachChild(smoke);
        smoke.move(1, 0.85f, 1.0f);
    }

    /**
     * Starts the missile animation by loading the missile model and initiating its parabolic movement.
     */
    public void start() {
        Smoke s = new Smoke(app, rootNode, start);
        s.trigger();
        loadMissile();
        app.getAcousticHandler().playSound(MdgaSound.MISSILE);
        animateMissile();
    }

    /**
     * Loads the missile model into the scene, applies scaling, material, and sets its initial position.
     */
    private void loadMissile() {
        missileModel = app.getAssetManager().loadModel(Asset.missile.getModelPath());
        missileModel.scale(Asset.missile.getSize());
        missileModel.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        Material mat = new Material(app.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
        mat.setTexture("DiffuseMap", app.getAssetManager().loadTexture(Asset.missile.getDiffPath()));
        missileModel.setMaterial(mat);

        missileNode.setLocalTranslation(start);
        missileNode.attachChild(missileModel);

        rootNode.attachChild(missileNode);
    }

    /**
     * Animates the missile along a parabolic path, triggers the explosion near the target,
     * and removes the missile model after the animation completes.
     */
    private void animateMissile() {
        missileNode.addControl(new AbstractControl() {
            private float elapsedTime = 0;

            @Override
            protected void controlUpdate(float tpf) {
                if (elapsedTime > 6) {
                    endAnim();
                    rootNode.detachChild(missileNode);
                    this.spatial.removeControl(this);
                }

                elapsedTime += tpf;
                float progress = elapsedTime / flightTime;

                if (progress >= 0.55) {
                    smoke.setParticlesPerSec(30);
                }

                if (progress >= 0.7) {
                    smoke.setParticlesPerSec(0);
                }

                if (progress >= 0.95f) {
                    explosion.trigger();
                }

                if (progress >= 1) {
                    explosion.trigger();
                    missileNode.detachChild(missileModel);
                }

                Vector3f currentPosition = computeParabolicPath(start, target, progress);
                missileNode.setLocalTranslation(currentPosition);

                Vector3f direction = computeParabolicPath(start, target, progress + 0.01f)
                        .subtract(currentPosition)
                        .normalizeLocal();
                missileModel.lookAt(currentPosition.add(direction), Vector3f.UNIT_Y);
                missileModel.rotate(0, FastMath.HALF_PI, 0);
            }

            @Override
            protected void controlRender(RenderManager rm, ViewPort vp) {
            }
        });
    }

    private void endAnim() {
        actionAfter.run();
    }

    /**
     * Computes a position along a parabolic path at a given progress value {@code t}.
     *
     * @param start  The starting point of the missile's flight.
     * @param target The target point of the missile's flight.
     * @param t      The progress value (0.0 to 1.0) along the flight path.
     * @return The interpolated position along the parabolic path.
     */
    private Vector3f computeParabolicPath(Vector3f start, Vector3f target, float t) {
        Vector3f midPoint = start.add(target).multLocal(0.5f);
        midPoint.addLocal(0, 0, 20);

        Vector3f startToMid = FastMath.interpolateLinear(t, start, midPoint);
        Vector3f midToTarget = FastMath.interpolateLinear(t, midPoint, target);
        return FastMath.interpolateLinear(t, startToMid, midToTarget);
    }
}
