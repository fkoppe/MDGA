package pp.mdga.client;

import com.jme3.math.Vector3f;

public class Util {
    private Util() {
    }

    /**
     * Performs linear interpolation between two values.
     *
     * @param start The starting value.
     * @param end   The ending value.
     * @param t     A parameter between 0 and 1 representing the interpolation progress.
     * @return The interpolated value.
     */
    public static float linInt(float start, float end, float t) {
        return start + t * (end - start);
    }

    /**
     * Performs quadratic interpolation between three points.
     *
     * @param p1 The initial point.
     * @param p2 The middle point.
     * @param p3 The final point.
     * @param t  The interpolation parameter (0 <= t <= 1).
     * @return The interpolated point.
     */
    public static Vector3f quadInt(Vector3f p1, Vector3f p2, Vector3f p3, float t) {
        // Quadratic interpolation: (1-t)^2 * p1 + 2 * (1-t) * t * p2 + t^2 * p3
        float oneMinusT = 1 - t;
        return p1.mult(oneMinusT * oneMinusT)
                .add(p2.mult(2 * oneMinusT * t))
                .add(p3.mult(t * t));
    }

    /**
     * A smooth ease-in-out function for interpolation.
     * It accelerates and decelerates the interpolation for a smoother effect.
     *
     * @param x The interpolation parameter (0 <= x <= 1).
     * @return The adjusted interpolation value.
     */
    public static float easeInOut(float x) {
        return x < 0.5 ? 4 * x * x * x : (float) (1 - Math.pow(-2 * x + 2, 3) / 2);
    }
}
