package pp.mdga.client.animation;

import com.jme3.math.Vector3f;

import static pp.mdga.client.Util.easeInOut;
import static pp.mdga.client.Util.quadInt;

/**
 * A control that smoothly moves a spatial from an initial position to an end position
 * using a quadratic interpolation, with the option to perform an action after the movement is complete.
 * The movement path includes an intermediate "middle" position at a specified height.
 *
 * <p>Movement speed can be adjusted by modifying the MOVE_SPEED constant. The movement easing follows
 * an ease-in-out curve to create a smooth start and stop effect.
 * </p>
 */
public class MoveControl extends ActionControl {

    private boolean moving;
    private final Vector3f initPos;
    private final Vector3f endPos;
    private final Vector3f middlePos;
    private final float height;
    private final float duration;
    private float timer = 0;
    private boolean easing;

    /**
     * Creates a new MoveControl with specified initial and end positions, and an action to run after the movement.
     * The movement follows a path with a midpoint at a fixed height.
     *
     * @param initPos     The starting position of the spatial.
     * @param endPos      The target position of the spatial.
     * @param actionAfter A Runnable that will be executed after the movement finishes.
     */
    public MoveControl(Vector3f initPos, Vector3f endPos, Runnable actionAfter) {
        this(initPos, endPos, actionAfter, 2, 1, true);
    }

    public MoveControl(Vector3f initPos, Vector3f endPos, Runnable actionAfter, float height, float duration, boolean easing) {
        super(actionAfter);
        moving = false;
        this.initPos = initPos;
        this.endPos = endPos;
        this.height = height;
        this.duration = duration;
        this.easing = easing;
        middlePos = new Vector3f(
                (initPos.x + endPos.x) / 2,
                (initPos.y + endPos.y) / 2,
                height
        );
    }

    /**
     * Initializes the movement by resetting the progress and setting the moving flag to true.
     * This is called automatically when the spatial is set.
     */
    @Override
    protected void initSpatial() {
        moving = true;
        timer = 0;
    }

    /**
     * Updates the movement of the spatial by interpolating its position along the defined path.
     * The movement is smoothed using an easing function.
     * Once the movement reaches the target, the {@link #end()} method is called to finish the movement.
     *
     * @param tpf Time per frame, the time elapsed since the last frame.
     */
    @Override
    protected void controlUpdate(float tpf) {
        if (!moving) return;
        timer += tpf;

        float t = timer / duration;
        if (t >= 1) t = 1;

        float interpolated = easing ? easeInOut(t) : t;

        spatial.setLocalTranslation(quadInt(initPos, middlePos, endPos, interpolated));

        if (t >= 1) end();
    }

    /**
     * Ends the movement by stopping the interpolation, running the action after the movement,
     * and removing this control from the spatial.
     */
    private void end() {
        moving = false;
        spatial.removeControl(this);
        action();
    }


}
