package pp.mdga.client.button;

import com.jme3.math.Vector2f;
import com.jme3.scene.Node;
import pp.mdga.client.MdgaApp;

/**
 * Represents a specific implementation of a clickable button positioned on the left side.
 * This class extends {@link ClickButton} and provides a predefined position and size for the button.
 * It also includes placeholder methods for handling hover events, which can be customized as needed.
 */
public class ButtonLeft extends ClickButton {

    /**
     * Constructs a ButtonLeft instance with the specified properties.
     *
     * @param app          the application instance for accessing resources and settings
     * @param node         the node in the scene graph to which the button belongs
     * @param action       the action to execute when the button is clicked
     * @param label        the text label to display on the button
     * @param narrowFactor a factor to adjust position of the button
     */
    public ButtonLeft(MdgaApp app, Node node, Runnable action, String label, int narrowFactor) {
        super(app, node, action, label, new Vector2f(5, 2), new Vector2f(0.5f * narrowFactor, 1.8f));
    }

    /**
     * Called when the button is hovered over by the pointer.
     * Subclasses can override this method to define specific hover behavior.
     */
    @Override
    public void onHover() {
        // Placeholder for hover behavior
    }

    /**
     * Called when the pointer stops hovering over the button.
     * Subclasses can override this method to define specific unhover behavior.
     */
    @Override
    public void onUnHover() {
        // Placeholder for unhover behavior
    }
}

