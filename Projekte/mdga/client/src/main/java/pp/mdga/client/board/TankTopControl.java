package pp.mdga.client.board;

import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import pp.mdga.client.InitControl;

import static pp.mdga.client.Util.linInt;

/**
 * Controls the rotation of the tank's top part to face an enemy position.
 */
public class TankTopControl extends InitControl {

    private float timer = 0; // Time elapsed
    private final static float DURATION = 1.5f; // Total rotation duration in seconds
    private boolean rotating = false; // Flag to track if rotation is active
    private float startAngle = 0;
    private float endAngle = 0;
    private Runnable actionAfter = null;

    /**
     * Updates the control each frame.
     *
     * @param tpf Time per frame
     */
    @Override
    protected void controlUpdate(float tpf) {
        if (!rotating) return;

        // Update the timer
        timer += tpf;

        // Calculate interpolation factor (0 to 1)
        float t = timer / DURATION;

        if (t >= 1) t = 1;

        float curAngle = linInt(startAngle, endAngle, t);

        // Interpolate the rotation
        Quaternion interpolatedRotation = new Quaternion();
        interpolatedRotation.fromAngleAxis((float) Math.toRadians(curAngle), Vector3f.UNIT_Z);

        // Apply the interpolated rotation to the spatial
        spatial.setLocalRotation(interpolatedRotation);

        if (t >= 1) {
            rotating = false;
            if (actionAfter != null) actionAfter.run();
        }
    }

    /**
     * Initiates the rotation of the tank's top part to face the enemy position.
     *
     * @param enemyPos The position of the enemy
     * @param actionAfter The action to execute after the rotation is complete
     */
    public void rotate(Vector3f enemyPos, Runnable actionAfter) {
        if (spatial == null) throw new RuntimeException("spatial is null");

        startAngle = getOwnAngle();
        endAngle = getEnemyAngle(enemyPos);

        // Adjust endAngle to ensure the shortest path
        float deltaAngle = endAngle - startAngle;
        if (deltaAngle > 180) {
            endAngle -= 360; // Rotate counterclockwise
        } else if (deltaAngle < -180) {
            endAngle += 360; // Rotate clockwise
        }

        timer = 0;
        rotating = true;
        this.actionAfter = actionAfter; // Store the action to execute after rotation
    }

    /**
     * Calculates the angle to the enemy position.
     *
     * @param enemyPos The position of the enemy
     * @return The angle to the enemy in degrees
     */
    private float getEnemyAngle(Vector3f enemyPos) {
        // Direction to the enemy in the XY plane
        Vector3f direction = enemyPos.subtract(spatial.getLocalTranslation());
        direction.z = 0; // Project to XY plane
        direction.normalizeLocal();

        Vector3f reference = Vector3f.UNIT_Y.mult(-1);

        // Calculate the angle between the direction vector and the reference vector
        float angle = FastMath.acos(reference.dot(direction));

        // Determine rotation direction using the cross product
        Vector3f cross = reference.cross(direction);
        if (cross.z < 0) {
            angle = -angle;
        }

        return (float) Math.toDegrees(angle); // Return the absolute angle in degrees
    }

    /**
     * Calculates the tank's current angle.
     *
     * @return The tank's current angle in degrees
     */
    private float getOwnAngle() {
        // Tank's forward direction in the XY plane
        Vector3f forward = spatial.getLocalRotation().mult(Vector3f.UNIT_Y);
        forward.z = 0; // Project to XY plane
        forward.normalizeLocal();

        // Reference vector: Positive X-axis
        Vector3f reference = Vector3f.UNIT_Y;

        // Calculate the angle between the forward vector and the reference vector
        float angle = FastMath.acos(reference.dot(forward));

        // Determine rotation direction using the cross product
        Vector3f cross = reference.cross(forward);
        if (cross.z < 0) { // For Z-up, check the Z component of the cross product
            angle = -angle;
        }

        return (float) Math.toDegrees(angle); // Return the absolute angle in radians
    }
}
