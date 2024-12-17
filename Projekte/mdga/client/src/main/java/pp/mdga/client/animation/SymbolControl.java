package pp.mdga.client.animation;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import pp.mdga.client.InitControl;
import pp.mdga.game.BonusCard;

/**
 * A control that manages the animation of symbols representing different bonus card states.
 * The symbol can animate with zoom, rotation, and translation effects based on the state of the bonus card.
 *
 * <p>The control supports three main states: SHIELD, SWAP, and TURBO. Each state has its own specific animation logic:
 * <ul>
 *   <li>SHIELD: Zooms in and out, with a scaling effect.</li>
 *   <li>SWAP: Rotates the symbol 360 degrees.</li>
 *   <li>TURBO: Moves the symbol along the Y-axis with a zoom effect.</li>
 * </ul>
 * </p>
 */
public class SymbolControl extends InitControl {
    private boolean zoomingIn = false;
    private boolean zoomingOut = false;
    private float zoomSpeed = 1f;
    private float zoomFactor = 3f;
    private float progress = 0;
    private BonusCard state;
    private float rotationSpeed = 0.8f;
    private Quaternion initialRotation = null;
    private float y = 5;

    /**
     * Updates the symbol animation based on the current bonus card state.
     * The method calls the corresponding update method for each state (SHIELD, SWAP, TURBO).
     *
     * @param tpf Time per frame, the time elapsed since the last frame.
     */
    @Override
    protected void controlUpdate(float tpf) {
        if (state == null) return;
        switch (state) {
            case SHIELD -> shieldUpdate(tpf);
            case SWAP -> swapUpdate(tpf);
            case TURBO -> turboUpdate(tpf);
            case HIDDEN -> throw new RuntimeException("forbidden state");
        }
    }

    /**
     * Updates the symbol when the state is SHIELD. The symbol zooms in and then zooms out.
     * When the zooming out finishes, the symbol is removed from the parent spatial.
     *
     * @param tpf Time per frame, the time elapsed since the last frame.
     */
    private void shieldUpdate(float tpf) {
        if (zoomingIn) {
            progress += tpf * zoomSpeed;
            if (progress > 1) progress = 1;
            spatial.setLocalScale(lerp(0, zoomFactor, easeOut(progress)));
            if (progress >= 1) {
                zoomingIn = false;
                zoomingOut = true;
                progress = 0;
            }
        } else if (zoomingOut) {
            progress += tpf * zoomSpeed;
            spatial.setLocalScale(lerp(zoomFactor, 0, easeIn(progress)));
            if (progress > 1) {
                zoomingIn = false;
                spatial.removeFromParent();
                state = null;
                progress = 0;
            }
        }
    }

    /**
     * Updates the symbol when the state is SWAP. The symbol rotates 360 degrees.
     * After the rotation finishes, the symbol is removed from the parent spatial.
     *
     * @param tpf Time per frame, the time elapsed since the last frame.
     */
    private void swapUpdate(float tpf) {
        if (initialRotation == null) {
            initialRotation = spatial.getLocalRotation().clone();
        }

        progress += tpf * rotationSpeed;
        if (progress < 0) return;

        float angle = lerp(0, 360, easeInOut(progress));

        Quaternion newRotation = new Quaternion();
        newRotation.fromAngleAxis((float) Math.toRadians(angle), new Vector3f(0, 1, 0));

        spatial.setLocalRotation(initialRotation.mult(newRotation));

        if (progress >= 1.2f) {
            state = null;
            initialRotation = null;
            progress = 0;
            spatial.removeFromParent();
        }
    }

    /**
     * Updates the symbol when the state is TURBO. The symbol moves along the Y-axis with a zoom effect.
     * After the movement finishes, the symbol is removed from the parent spatial.
     *
     * @param tpf Time per frame, the time elapsed since the last frame.
     */
    private void turboUpdate(float tpf) {
        if (zoomingIn) {
            progress += tpf * zoomSpeed;
            if (progress > 1) progress = 1;
            float y = lerp(-this.y, 0, easeOut(progress));
            spatial.setLocalTranslation(0, y, 0);
            if (progress >= 1) {
                zoomingIn = false;
                zoomingOut = true;
                progress = 0;
            }
        } else if (zoomingOut) {
            progress += tpf * zoomSpeed;
            float y = lerp(0, this.y, easeIn(progress));
            spatial.setLocalTranslation(0, y, 0);
            if (progress > 1) {
                zoomingIn = false;
                spatial.removeFromParent();
                state = null;
            }
        }
    }

    /**
     * Starts the SHIELD animation by zooming the symbol in and out.
     * The symbol will first zoom in and then zoom out, and will be removed from the parent spatial once done.
     */
    public void shield() {
        if (state != null) throw new RuntimeException("another state is avtive");
        state = BonusCard.SHIELD;
        zoomingIn = true;
        zoomingOut = false;
        progress = 0;
        spatial.setLocalScale(1f);
    }

    /**
     * Starts the SWAP animation by rotating the symbol 360 degrees.
     * The symbol will rotate once and then be removed from the parent spatial.
     */
    public void swap() {
        if (state != null) throw new RuntimeException("another state is avtive");
        spatial.setLocalScale(3);
        state = BonusCard.SWAP;
        progress = -0.2f;
    }

    /**
     * Starts the TURBO animation by moving the symbol along the Y-axis.
     * The symbol will move upwards and then return to its initial position.
     */
    public void turbo() {
        if (state != null) throw new RuntimeException("another state is avtive");
        spatial.setLocalScale(2);
        state = BonusCard.TURBO;
        zoomingIn = true;
        zoomingOut = false;
        progress = 0;
    }

    /**
     * Performs linear interpolation between two values.
     *
     * @param start The starting value.
     * @param end   The target value.
     * @param t     The interpolation parameter (0 <= t <= 1).
     * @return The interpolated value.
     */
    private static float lerp(float start, float end, float t) {
        return (1 - t) * start + t * end;
    }

    /**
     * Ease-out function for smoothing the interpolation.
     *
     * @param t The interpolation parameter (0 <= t <= 1).
     * @return The eased value.
     */
    private static float easeOut(float t) {
        return (float) Math.sqrt(1 - Math.pow(t - 1, 2));
    }

    /**
     * Ease-in-out function for smoothing the interpolation.
     *
     * @param t The interpolation parameter (0 <= t <= 1).
     * @return The eased value.
     */
    private float easeInOut(float t) {
        if (t > 1) t = 1;
        return (float) -(Math.cos(Math.PI * t) - 1) / 2;
    }

    /**
     * Ease-in function for smoothing the interpolation.
     *
     * @param t The interpolation parameter (0 <= t <= 1).
     * @return The eased value.
     */
    private float easeIn(float t) {
        return t * t * t * t;
    }
}
