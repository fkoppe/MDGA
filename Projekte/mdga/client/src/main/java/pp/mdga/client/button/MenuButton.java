package pp.mdga.client.button;

import com.jme3.math.Vector2f;
import com.jme3.scene.Node;
import pp.mdga.client.MdgaApp;

/**
 * Represents a button used in menu screens for navigation or executing actions.
 * Inherits from {@link ClickButton} and provides customizable text and actions.
 */
public class MenuButton extends ClickButton {

    /**
     * Constructs a MenuButton with specified properties.
     *
     * @param app    the application instance for accessing resources
     * @param node   the node in the scene graph to which the button belongs
     * @param action the action to execute when the button is clicked
     * @param label  the text label displayed on the button
     */
    public MenuButton(MdgaApp app, Node node, Runnable action, String label) {
        super(app, node, action, label, new Vector2f(5.5f, 2), new Vector2f(0, 0));
    }

    /**
     * Called when the button is hovered over. Can be overridden to define hover-specific behavior.
     * Currently, no additional behavior is implemented.
     */
    @Override
    public void onHover() {
        // Placeholder for hover behavior
    }

    /**
     * Called when the pointer stops hovering over the button. Can be overridden to define unhover-specific behavior.
     * Currently, no additional behavior is implemented.
     */
    @Override
    public void onUnHover() {
        // Placeholder for unhover behavior
    }
}

