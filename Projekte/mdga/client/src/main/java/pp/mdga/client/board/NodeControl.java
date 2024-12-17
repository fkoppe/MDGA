package pp.mdga.client.board;

import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import pp.mdga.client.MdgaApp;
import pp.mdga.client.outline.OutlineControl;

/**
 * A control that adds highlighting functionality to a node in the game.
 * This class extends {@link OutlineControl} to add an outline effect when the node is highlighted.
 */
public class NodeControl extends OutlineOEControl {

    /**
     * Constructs a {@link NodeControl} with the specified application and post processor.
     * This constructor sets up the necessary elements for highlighting functionality.
     *
     * @param app The {@link MdgaApp} instance to use for the application context.
     * @param fpp The {@link FilterPostProcessor} to apply post-processing effects.
     */
    public NodeControl(MdgaApp app, FilterPostProcessor fpp) {
        super(app, fpp, app.getCamera());
    }

    /**
     * Returns the location of the node in 3D space.
     * This is the node's local translation in the scene.
     *
     * @return The {@link Vector3f} representing the node's location.
     */
    public Vector3f getLocation() {
        return this.getSpatial().getLocalTranslation();
    }
}
