package pp.mdga.client.outline;

import com.jme3.asset.AssetManager;
import com.jme3.math.ColorRGBA;
import com.jme3.post.FilterPostProcessor;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import pp.mdga.client.MdgaApp;

/**
 * This class is responsible for outlining selected objects in the scene.
 */
public class SelectObjectOutliner {

    private final FilterPostProcessor fpp;
    private final RenderManager renderManager;
    private final AssetManager assetManager;
    private final Camera cam;
    private boolean selected;
    private ViewPort outlineViewport = null;
    //    private OutlineFilter outlineFilter = null;
    private OutlineProFilter outlineFilter = null;
    private final MdgaApp app;

    /**
     * Constructor for SelectObjectOutliner.
     *
     * @param fpp the FilterPostProcessor
     * @param renderManager the RenderManager
     * @param assetManager the AssetManager
     * @param cam the Camera
     * @param app the MdgaApp instance
     */
    public SelectObjectOutliner(FilterPostProcessor fpp, RenderManager renderManager, AssetManager assetManager, Camera cam, MdgaApp app) {
        this.selected = false;
        this.fpp = fpp;
        this.renderManager = renderManager;
        this.assetManager = assetManager;
        this.cam = cam;
        this.app = app;
    }

    /**
     * Deselects the currently selected object, removing the outline effect.
     *
     * @param model the Spatial model to deselect
     */
    public void deselect(Spatial model) {
        if (selected) {
            selected = false;
            hideOutlineFilterEffect(model);
        }
    }

//    /**
//     * Selects an object and applies an outline effect.
//     *
//     * @param model the Spatial model to select
//     * @param color the ColorRGBA for the outline
//     */
//    public void select(Spatial model, ColorRGBA color) {
//        if (!selected) {
//            selected = true;
//            showOutlineFilterEffect(model, width, color);
//        }
//    }

    /**
     * Selects an object and applies an outline effect.
     *
     * @param model the Spatial model to select
     * @param color the ColorRGBA for the outline
     * @param width the width of the outline
     */
    public void select(Spatial model, ColorRGBA color, int width) {
        if (!selected) {
            selected = true;
            showOutlineFilterEffect(model, width, color);
        }
    }

    /**
     * Hides the outline effect from the selected object.
     *
     * @param model the Spatial model to hide the outline effect from
     */
    private void hideOutlineFilterEffect(Spatial model) {
        outlineFilter.setEnabled(false);
        outlineFilter.getOutlinePreFilter().setEnabled(false);
        fpp.removeFilter(outlineFilter);
        outlineViewport.detachScene(model);
        outlineViewport.clearProcessors();
        renderManager.removePreView(outlineViewport);
        outlineViewport = null;
    }

    /**
     * Shows the outline effect on the selected object.
     *
     * @param model the Spatial model to show the outline effect on
     * @param width the width of the outline
     * @param color the ColorRGBA for the outline
     */
    private void showOutlineFilterEffect(Spatial model, int width, ColorRGBA color) {
        outlineViewport = renderManager.createPreView("outlineViewport", cam);
        FilterPostProcessor outlineFpp = new FilterPostProcessor(assetManager);

        OutlinePreFilter outlinePreFilter = new OutlinePreFilter();
        outlineFpp.addFilter(outlinePreFilter);
        outlineViewport.attachScene(model);
        outlineViewport.addProcessor(outlineFpp);

        outlineFilter = new OutlineProFilter(outlinePreFilter);
        outlineFilter.setOutlineColor(color);
        outlineFilter.setOutlineWidth(width);

        fpp.addFilter(outlineFilter);
    }
}
