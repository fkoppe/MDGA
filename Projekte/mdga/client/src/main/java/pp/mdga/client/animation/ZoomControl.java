package pp.mdga.client.animation;

import pp.mdga.client.InitControl;

/**
 * A control that applies a zoom effect to a spatial, smoothly scaling it in and out.
 * The zoom effect can be customized with speed and scaling factor.
 *
 * <p>The control supports zooming in and out with ease-in and ease-out transitions.
 * It starts by zooming in, and once complete, it zooms out, eventually removing the spatial from its parent when the animation ends.</p>
 */
public class ZoomControl extends InitControl {
    private boolean zoomingIn = false;
    private boolean zoomingOut = false;
    private float progress = 0;
    private float zoomSpeed = 1f;
    private float zoomFactor = 1f;

    /**
     * Constructs a new ZoomControl with the default zoom speed.
     */
    public ZoomControl() {
    }

    /**
     * Constructs a new ZoomControl with a specified zoom speed.
     *
     * @param speed The speed at which the zoom effect occurs.
     */
    public ZoomControl(float speed) {
        zoomSpeed = speed;
    }

    /**
     * Initializes the spatial for the zoom effect. This method is called when the control is added to the spatial.
     * It sets the zooming state to zooming in.
     */
    @Override
    protected void initSpatial() {
        zoomingIn = true;
    }

    /**
     * Updates the zoom effect over time, either zooming in or zooming out.
     *
     * @param tpf Time per frame, the time elapsed since the last frame.
     */
    @Override
    protected void controlUpdate(float tpf) {
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
                zoomingOut = false;
                end();
            }
        }
    }

    /**
     * Ends the zoom animation by removing the spatial from its parent and the control from the spatial.
     */
    private void end() {
        spatial.removeFromParent();
        spatial.removeControl(this);
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
     * Ease-out function for smoothing the zoom-in transition.
     *
     * @param x The interpolation parameter (0 <= x <= 1).
     * @return The eased value.
     */
    private float easeOut(float x) {
        return x == 1 ? 1 : (float) (1 - Math.pow(2, -10 * x));

    }

    /**
     * Ease-in function for smoothing the zoom-out transition.
     *
     * @param x The interpolation parameter (0 <= x <= 1).
     * @return The eased value.
     */
    private float easeIn(float x) {
        return x == 0 ? 0 : (float) Math.pow(2, 10 * x - 10);

    }
}
