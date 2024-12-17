package pp.mdga.client;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;

/**
 * An abstract control class that serves as a base for initializing spatial objects
 * in jMonkeyEngine. This class overrides the controlUpdate and controlRender methods
 * from the AbstractControl class, providing default empty implementations,
 * and adds the ability to initialize spatial objects when they are set.
 */
public abstract class InitControl extends AbstractControl {

    @Override
    protected void controlUpdate(float tpf) {

    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {

    }

    /**
     * Sets the spatial object to be controlled. This method also initializes the spatial
     * if it is being set for the first time.
     *
     * @param spatial The spatial object to control.
     */
    @Override
    public void setSpatial(Spatial spatial) {
        if (this.spatial == null && spatial != null) {
            super.setSpatial(spatial);
            initSpatial();
        }
    }

    /**
     * Initializes the spatial object. This method can be overridden by subclasses
     * to define custom initialization logic for the spatial.
     * This method is called automatically when the spatial is set for the first time.
     */
    protected void initSpatial() {
        // Default empty implementation. Override to add initialization logic.
    }
}
