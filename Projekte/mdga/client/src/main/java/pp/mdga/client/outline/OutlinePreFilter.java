package pp.mdga.client.outline;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.post.Filter;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.Renderer;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.texture.FrameBuffer;
import com.jme3.texture.Image.Format;
import com.jme3.texture.Texture;


/**
 * OutlinePreFilter is a custom filter used to apply an outline effect to objects.
 */
public class OutlinePreFilter extends Filter {

    private Pass normalPass;
    private RenderManager renderManager;

    /**
     * Creates an OutlinePreFilter.
     */
    public OutlinePreFilter() {
        super("OutlinePreFilter");
    }

    /**
     * Indicates that this filter requires a depth texture.
     *
     * @return true, indicating that a depth texture is required.
     */
    @Override
    protected boolean isRequiresDepthTexture() {
        return true;
    }

    /**
     * Called after the render queue is processed.
     *
     * @param queue the render queue.
     */
    @Override
    protected void postQueue(RenderQueue queue) {
        Renderer r = renderManager.getRenderer();
        r.setFrameBuffer(normalPass.getRenderFrameBuffer());
        renderManager.getRenderer().clearBuffers(true, true, false);
    }

    /**
     * Called after the frame is rendered.
     *
     * @param renderManager the render manager.
     * @param viewPort the viewport.
     * @param prevFilterBuffer the previous filter buffer.
     * @param sceneBuffer the scene buffer.
     */
    @Override
    protected void postFrame(RenderManager renderManager, ViewPort viewPort, FrameBuffer prevFilterBuffer, FrameBuffer sceneBuffer) {
        super.postFrame(renderManager, viewPort, prevFilterBuffer, sceneBuffer);
    }

    /**
     * Returns the material used by this filter.
     *
     * @return the material.
     */
    @Override
    protected Material getMaterial() {
        return material;
    }

    /**
     * Returns the texture containing the outline.
     *
     * @return the outline texture.
     */
    public Texture getOutlineTexture() {
        return normalPass.getRenderedTexture();
    }

    /**
     * Initializes the filter.
     *
     * @param manager the asset manager.
     * @param renderManager the render manager.
     * @param vp the viewport.
     * @param w the width.
     * @param h the height.
     */
    @Override
    protected void initFilter(AssetManager manager, RenderManager renderManager, ViewPort vp, int w, int h) {
        this.renderManager = renderManager;
        normalPass = new Pass();
        normalPass.init(renderManager.getRenderer(), w, h, Format.RGBA8, Format.Depth);
        material = new Material(manager, "MatDefs/SelectObjectOutliner/OutlinePre.j3md");
    }

    /**
     * Cleans up the filter.
     *
     * @param r the renderer.
     */
    @Override
    protected void cleanUpFilter(Renderer r) {
        normalPass.cleanup(r);
    }

}
