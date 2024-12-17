package pp.mdga.client.animation;

import com.jme3.material.Material;
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

/**
 * The {@code JetAnimation} class handles the animation of a jet model in a 3D environment.
 * It creates a jet model, animates its movement along a curved path, triggers an explosion at a target point,
 * and performs additional actions upon animation completion.
 */
public class JetAnimation {

    private final MdgaApp app;
    private final Node rootNode;
    private Spatial jetModel;
    private final Vector3f spawnPoint;
    private final Vector3f nodePoint;
    private final Vector3f despawnPoint;
    private final float curveHeight;
    private final float animationDuration;
    private Explosion explosion;
    private Runnable actionAfter;

    /**
     * Constructor for the {@code JetAnimation} class.
     *
     * @param app               The main application managing the jet animation.
     * @param rootNode          The root node to which the jet model will be attached.
     * @param targetPoint       The target point where the explosion will occur.
     * @param curveHeight       The height of the curve for the jet's flight path.
     * @param animationDuration The total duration of the jet animation.
     */
    public JetAnimation(MdgaApp app, Node rootNode, Vector3f targetPoint, float curveHeight, float animationDuration, Runnable actionAfter) {
        Vector3f spawnPoint = targetPoint.add(170, 50, 50);

        Vector3f controlPoint = targetPoint.add(new Vector3f(0, 0, -45));

        Vector3f despawnPoint = targetPoint.add(-100, -100, 40);

        this.app = app;
        this.rootNode = rootNode;
        this.spawnPoint = spawnPoint;
        this.nodePoint = controlPoint;
        this.despawnPoint = despawnPoint;
        this.curveHeight = curveHeight;
        this.animationDuration = animationDuration;

        explosion = new Explosion(app, rootNode, targetPoint);
        this.actionAfter = actionAfter;
    }

    /**
     * Starts the jet animation by spawning the jet model and initiating its movement along the predefined path.
     */
    public void start() {
        app.getAcousticHandler().playSound(MdgaSound.JET);
        spawnJet();
        animateJet();
    }

    /**
     * Spawns the jet model at the designated spawn point, applying material, scaling, and rotation.
     */
    private void spawnJet() {
        jetModel = app.getAssetManager().loadModel(Asset.jet_noGear.getModelPath());
        jetModel.setLocalTranslation(spawnPoint);
        jetModel.scale(Asset.jet_noGear.getSize());
        jetModel.rotate(FastMath.HALF_PI, 0, 0);
        jetModel.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        Material mat = new Material(app.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
        mat.setTexture("DiffuseMap", app.getAssetManager().loadTexture(Asset.jet_noGear.getDiffPath()));
        jetModel.setMaterial(mat);

        rootNode.attachChild(jetModel);
    }

    /**
     * actionAfter
     * Animates the jet along a Bezier curve path, triggers the explosion effect at the appropriate time,
     * and performs cleanup operations after the animation completes.
     */
    private void animateJet() {
        Vector3f controlPoint1 = spawnPoint.add(0, curveHeight, 0);
        Vector3f controlPoint2 = nodePoint.add(0, curveHeight, 0);

        BezierCurve3f curve = new BezierCurve3f(spawnPoint, controlPoint1, controlPoint2, despawnPoint);

        app.getRootNode().addControl(new AbstractControl() {
            private float elapsedTime = 0;

            @Override
            protected void controlUpdate(float tpf) {
                elapsedTime += tpf;
                float progress = elapsedTime / animationDuration;

                if (elapsedTime > 4.2f) {
                    explosion.trigger();
                }

                if (progress > 1) {
                    rootNode.detachChild(jetModel);
                    this.spatial.removeControl(this);
                } else {
                    Vector3f currentPos = curve.interpolate(progress);
                    Vector3f direction = curve.interpolateDerivative(progress).normalizeLocal();
                    jetModel.setLocalTranslation(currentPos);
                    jetModel.lookAt(currentPos.add(direction), Vector3f.UNIT_Z);
                    jetModel.rotate(-FastMath.HALF_PI, 0, (float) Math.toRadians(-25));
                }

                if (elapsedTime > 6.0f) {
                    endAnim();
                }
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
     * The {@code BezierCurve3f} class represents a 3D cubic Bezier curve.
     * It provides methods to interpolate positions and derivatives along the curve.
     */
    private static class BezierCurve3f {
        private final Vector3f p0, p1, p2, p3;

        /**
         * Constructor for the {@code BezierCurve3f} class.
         *
         * @param p0 The starting point of the curve.
         * @param p1 The first control point influencing the curve's shape.
         * @param p2 The second control point influencing the curve's shape.
         * @param p3 The endpoint of the curve.
         */
        public BezierCurve3f(Vector3f p0, Vector3f p1, Vector3f p2, Vector3f p3) {
            this.p0 = p0;
            this.p1 = p1;
            this.p2 = p2;
            this.p3 = p3;
        }

        /**
         * Interpolates a position along the curve at a given progress value {@code t}.
         *
         * @param t The progress value (0.0 to 1.0) along the curve.
         * @return The interpolated position on the curve.
         */
        public Vector3f interpolate(float t) {
            float u = 1 - t;
            float tt = t * t;
            float uu = u * u;
            float uuu = uu * u;
            float ttt = tt * t;

            Vector3f point = p0.mult(uuu);
            point = point.add(p1.mult(3 * uu * t));
            point = point.add(p2.mult(3 * u * tt));
            point = point.add(p3.mult(ttt));
            return point;
        }

        /**
         * Computes the derivative at a given progress value {@code t}, representing the direction along the curve.
         *
         * @param t The progress value (0.0 to 1.0) along the curve.
         * @return The derivative (direction vector) at the specified progress.
         */
        public Vector3f interpolateDerivative(float t) {
            float u = 1 - t;
            float tt = t * t;

            Vector3f derivative = p0.mult(-3 * u * u);
            derivative = derivative.add(p1.mult(3 * u * u - 6 * u * t));
            derivative = derivative.add(p2.mult(6 * u * t - 3 * tt));
            derivative = derivative.add(p3.mult(3 * tt));
            return derivative;
        }
    }
}
