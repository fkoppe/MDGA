package pp.mdga.client.gui;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.ComposeFilter;
import com.jme3.post.filters.FXAAFilter;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.shadow.DirectionalLightShadowFilter;
import com.jme3.shadow.EdgeFilteringMode;
import com.jme3.texture.Image;
import com.jme3.texture.Texture2D;

import java.util.ArrayList;
import java.util.List;

/**
 * CardLayer is an application state that manages the rendering and updating of card objects
 * in a separate viewport with post-processing effects.
 */
public class CardLayer extends AbstractAppState {

    public static final int SHADOWMAP_SIZE = 1024 * 8;

    private Node root;
    private Application app;
    private boolean init;

    private List<Spatial> cardBuffer;
    private final FilterPostProcessor fpp;
    private final Camera overlayCam;
    private Texture2D backTexture;
    private FXAAFilter fxaaFilter;
    private ViewPort view;
    private DirectionalLightShadowFilter dlsf;
    DirectionalLight sun;
    ComposeFilter compose;

    /**
     * Constructs a new CardLayer with the specified post-processor, camera, and background texture.
     *
     * @param fpp the FilterPostProcessor to use for post-processing effects
     * @param overlayCam the Camera to use for the overlay
     * @param backTexture the Texture2D to use as the background texture
     */
    public CardLayer(FilterPostProcessor fpp, Camera overlayCam, Texture2D backTexture) {
        this.overlayCam = overlayCam;
        this.fpp = fpp;
        this.backTexture = backTexture;
        cardBuffer = new ArrayList<>();
        init = false;
        fxaaFilter = new FXAAFilter();
        view = null;
        dlsf = null;

        sun = new DirectionalLight();
        sun.setColor(ColorRGBA.White);
        sun.setDirection(new Vector3f(.5f, -.5f, -1));
        compose = new ComposeFilter(backTexture);
        root = new Node("Under gui viewport Root");
    }

    /**
     * Initializes the CardLayer, setting up the viewport, filters, and lighting.
     *
     * @param stateManager the AppStateManager managing this state
     * @param app the Application instance
     */
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        this.app = app;

        view = app.getRenderManager().createMainView("Under gui ViewPort", overlayCam);
        view.setEnabled(true);
        view.setClearFlags(true, true, true);
        view.attachScene(root);
        fpp.setFrameBufferFormat(Image.Format.RGBA8);
        fpp.addFilter(compose);
        fpp.addFilter(fxaaFilter);

        root.addLight(sun);

        dlsf = new DirectionalLightShadowFilter(app.getAssetManager(), SHADOWMAP_SIZE, 3);
        dlsf.setLight(sun);
        dlsf.setEnabled(true);
        dlsf.setEdgeFilteringMode(EdgeFilteringMode.PCFPOISSON);
        dlsf.setShadowIntensity(.5f);
        fpp.addFilter(dlsf);

        view.addProcessor(fpp);

        if (!init) init = true;
    }

    /**
     * Shuts down the CardLayer, removing filters, lights, and clearing buffers.
     */
    public void shutdown() {
        fpp.removeFilter(dlsf);
        dlsf = null;
        root.removeLight(sun);
        fpp.removeFilter(fxaaFilter);
        view.detachScene(root);

        cardBuffer.clear();
        root.detachAllChildren();
    }

    /**
     * Renders the CardLayer, updating the geometric state of the root node.
     *
     * @param rm the RenderManager handling the rendering
     */
    @Override
    public void render(RenderManager rm) {
        root.updateGeometricState();
    }

    /**
     * Updates the CardLayer, attaching buffered cards to the root node and updating its logical state.
     *
     * @param tpf time per frame
     */
    @Override
    public void update(float tpf) {
        if (init && !cardBuffer.isEmpty()) {
            for (Spatial spatial : cardBuffer) {
                root.attachChild(spatial);
            }
            cardBuffer.clear();
        }
        root.updateLogicalState(tpf);
    }

    /**
     * Adds a spatial card to the CardLayer.
     *
     * @param card the Spatial card to add
     */
    public void addSpatial(Spatial card) {
        if (root == null) cardBuffer.add(card);
        else root.attachChild(card);
    }

    /**
     * Deletes a spatial card from the CardLayer.
     *
     * @param spatial the Spatial card to delete
     */
    public void deleteSpatial(Spatial spatial) {
        root.detachChild(spatial);
    }

    /**
     * Gets the overlay camera used by the CardLayer.
     *
     * @return the overlay camera
     */
    public Camera getOverlayCam() {
        return overlayCam;
    }

    /**
     * Gets the root node of the CardLayer.
     *
     * @return the root node
     */
    public Node getRootNode() {
        return root;
    }
}
