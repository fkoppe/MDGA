package pp.mdga.client.outline;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.MaterialDef;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.post.Filter;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.texture.FrameBuffer;

/**
 * OutlineProFilter is a custom filter for rendering outlines around objects.
 */
public class OutlineProFilter extends Filter {

    private OutlinePreFilter outlinePreFilter;
    private ColorRGBA outlineColor = new ColorRGBA(0, 1, 0, 1);
    private float outlineWidth = 1;

    /**
     * Constructs an OutlineProFilter with the specified OutlinePreFilter.
     *
     * @param outlinePreFilter the pre-filter used for outlining
     */
    public OutlineProFilter(OutlinePreFilter outlinePreFilter) {
        super("OutlineFilter");
        this.outlinePreFilter = outlinePreFilter;
    }

    /**
     * Initializes the filter with the given parameters.
     *
     * @param assetManager the asset manager
     * @param renderManager the render manager
     * @param vp the viewport
     * @param w the width of the viewport
     * @param h the height of the viewport
     */
    @Override
    protected void initFilter(AssetManager assetManager, RenderManager renderManager, ViewPort vp, int w, int h) {
        MaterialDef matDef = (MaterialDef) assetManager.loadAsset("MatDefs/SelectObjectOutliner/OutlinePro.j3md");
        material = new Material(matDef);
        material.setVector2("Resolution", new Vector2f(w, h));
        material.setColor("OutlineColor", outlineColor);
        material.setFloat("OutlineWidth", outlineWidth);
    }

    /**
     * Called before rendering each frame.
     *
     * @param tpf time per frame
     */
    @Override
    protected void preFrame(float tpf) {
        super.preFrame(tpf);
        material.setTexture("OutlineDepthTexture", outlinePreFilter.getOutlineTexture());
    }

    /**
     * Called after rendering each frame.
     *
     * @param renderManager the render manager
     * @param viewPort the viewport
     * @param prevFilterBuffer the previous filter buffer
     * @param sceneBuffer the scene buffer
     */
    @Override
    protected void postFrame(RenderManager renderManager, ViewPort viewPort, FrameBuffer prevFilterBuffer, FrameBuffer sceneBuffer) {
        super.postFrame(renderManager, viewPort, prevFilterBuffer, sceneBuffer);
    }

    /**
     * Returns the material used by this filter.
     *
     * @return the material
     */
    @Override
    protected Material getMaterial() {
        return material;
    }

    /**
     * Returns the outline color.
     *
     * @return the outline color
     */
    public ColorRGBA getOutlineColor() {
        return outlineColor;
    }

    /**
     * Sets the outline color.
     *
     * @param outlineColor the new outline color
     */
    public void setOutlineColor(ColorRGBA outlineColor) {
        this.outlineColor = outlineColor;
        if (material != null) {
            material.setColor("OutlineColor", outlineColor);
        }
    }

    /**
     * Returns the outline width.
     *
     * @return the outline width
     */
    public float getOutlineWidth() {
        return outlineWidth;
    }

    /**
     * Sets the outline width.
     *
     * @param outlineWidth the new outline width
     */
    public void setOutlineWidth(float outlineWidth) {
        this.outlineWidth = outlineWidth;
        if (material != null) {
            material.setFloat("OutlineWidth", outlineWidth);
        }
    }

    /**
     * Returns the OutlinePreFilter.
     *
     * @return the OutlinePreFilter
     */
    public OutlinePreFilter getOutlinePreFilter() {
        return outlinePreFilter;
    }

}
