package pp.mdga.client.board;

import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.FXAAFilter;
import com.jme3.post.ssao.SSAOFilter;
import com.jme3.scene.Spatial;
import com.jme3.shadow.DirectionalLightShadowFilter;
import com.jme3.shadow.EdgeFilteringMode;
import com.jme3.util.SkyFactory;
import com.jme3.util.SkyFactory.EnvMapType;
import pp.mdga.client.MdgaApp;
import pp.mdga.game.Color;

/**
 * Handles the camera position, rotation, and lighting effects for the game.
 * Provides methods for camera initialization, updates based on user input, and shutdown operations.
 */
public class CameraHandler {
    MdgaApp app;

    private DirectionalLight sun;
    private AmbientLight ambient;

    private static final int SHADOWMAP_SIZE = 1024 * 8;

    private Vector3f defaultCameraPosition;
    private Quaternion defaultCameraRotation;

    FilterPostProcessor fpp;
    DirectionalLightShadowFilter dlsf;

    Spatial sky;
    private Color ownColor;
    private boolean init;
    private boolean initRot;
    private SSAOFilter ssaoFilter;
    private FXAAFilter fxaaFilter;

    /**
     * Constructor for the CameraHandler. Initializes the camera settings and lighting.
     *
     * @param app The main application instance that provides the camera and root node.
     * @param fpp The FilterPostProcessor used for post-processing effects.
     */
    public CameraHandler(MdgaApp app, FilterPostProcessor fpp) {
        init = false;
        initRot = false;
        this.app = app;
        this.fpp = fpp;
        // Save the default camera state
        this.defaultCameraPosition = app.getCamera().getLocation().clone();
        this.defaultCameraRotation = app.getCamera().getRotation().clone();

        sun = new DirectionalLight();
        sun.setColor(ColorRGBA.White);
        sun.setDirection(new Vector3f(0.3f, 0, -1));

        ambient = new AmbientLight();
        ambient.setColor(new ColorRGBA(0.3f, 0.3f, 0.3f, 1));

        dlsf = new DirectionalLightShadowFilter(app.getAssetManager(), SHADOWMAP_SIZE, 1);
        dlsf.setLight(sun);
        dlsf.setEnabled(true);
        dlsf.setEdgeFilteringMode(EdgeFilteringMode.PCFPOISSON);
        dlsf.setShadowIntensity(0.7f);
        ssaoFilter = new SSAOFilter(6, 10f, 0.33f, 0.61f);
//        ssaoFilter = new SSAOFilter();
        fxaaFilter = new FXAAFilter();

        sky = SkyFactory.createSky(app.getAssetManager(), "Images/sky/sky.dds", EnvMapType.EquirectMap).rotate(FastMath.HALF_PI * 1, 0, FastMath.HALF_PI * 0.2f);


    }

    /**
     * Initializes the camera with a specific color orientation.
     * Adds lights, sky, and shadow filters to the scene.
     *
     * @param ownColor The color that defines the initial camera view angle.
     */
    public void init(Color ownColor) {
        app.getRootNode().addLight(sun);
        app.getRootNode().addLight(ambient);
        app.getRootNode().attachChild(sky);
        fpp.addFilter(dlsf);
        fpp.addFilter(ssaoFilter);
        fpp.addFilter(fxaaFilter);
        init = true;
        initRot = true;
        this.ownColor = ownColor;
        app.getInputSynchronize().setRotation(getInitAngleByColor(ownColor) * 2);
    }

    /**
     * Shuts down the camera handler by removing all lights, sky, and filters,
     * and resets the camera position and rotation to its default state.
     */
    public void shutdown() {
        init = false;
        fpp.removeFilter(fxaaFilter);
        fpp.removeFilter(ssaoFilter);
        fpp.removeFilter(dlsf);
        app.getRootNode().detachChild(sky);
        app.getRootNode().removeLight(ambient);
        app.getRootNode().removeLight(sun);


        // Reset the camera to its default state
        app.getCamera().setLocation(defaultCameraPosition);
        app.getCamera().setRotation(defaultCameraRotation);
    }

    /**
     * Updates the camera position and rotation based on user input (scroll and rotation).
     * Adjusts the vertical angle and radius based on zoom and rotation values.
     *
     * @param scroll   The scroll input, determining zoom level.
     * @param rotation The rotation input, determining camera orientation.
     */
    public void update(float scroll, float rotation) {
        if (!init) return;
        float scrollValue = Math.max(0, Math.min(scroll, 100));

        float rotationValue = rotation % 360;
        if (rotationValue < 0) {
            rotationValue += 360;
        }


        float radius;

        float verticalAngle;
        if (scroll < 100f) {
            verticalAngle = 20f + (scrollValue / 100f) * 45f;
            radius = 30f;
        } else {
            verticalAngle = 90f;
            rotationValue = getAngleByColor(ownColor);
            radius = 50f;
        }
        float verticalAngleRadians = FastMath.DEG_TO_RAD * verticalAngle;

        float z = radius * FastMath.sin(verticalAngleRadians);
        float x = radius * FastMath.cos(verticalAngleRadians) * FastMath.sin(FastMath.DEG_TO_RAD * rotationValue);
        float y = radius * FastMath.cos(verticalAngleRadians) * FastMath.cos(FastMath.DEG_TO_RAD * rotationValue);

        Vector3f cameraPosition = new Vector3f(x, y, z);
        app.getCamera().setLocation(cameraPosition);

        app.getCamera().lookAt(Vector3f.ZERO, Vector3f.UNIT_Z);
    }

    /**
     * Returns the camera angle based on the specified color.
     *
     * @param color The color used to determine the camera angle.
     * @return The camera angle in degrees.
     */
    private float getAngleByColor(Color color) {
        return switch (color) {
            case ARMY -> 0;
            case AIRFORCE -> 90;
            case NAVY -> 270;
            case CYBER -> 180;
            default -> throw new RuntimeException("None is not allowed");
        };
    }

    /**
     * Returns the initial camera angle based on the specified color.
     *
     * @param color The color used to determine the camera angle.
     * @return The initial camera angle in degrees.
     */
    private float getInitAngleByColor(Color color) {
        return (getAngleByColor(color) + 180) % 360;
    }

}
