package pp.mdga.client.button;

import com.jme3.math.Vector2f;
import com.jme3.scene.Node;
import com.simsilica.lemur.component.IconComponent;
import pp.mdga.client.MdgaApp;

/**
 * Represents a button in the settings menu, designed to display an icon and handle hover effects.
 * Inherits from {@link ClickButton} and customizes its behavior for settings functionality.
 */
public class SettingsButton extends ClickButton {

    /**
     * The icon displayed on the button, which changes based on hover state.
     */
    private IconComponent icon;

    /**
     * Constructs a SettingsButton with a predefined size and position.
     *
     * @param app    the application instance for accessing resources
     * @param node   the node in the scene graph to which the button belongs
     * @param action the action to execute when the button is clicked
     */
    public SettingsButton(MdgaApp app, Node node, Runnable action) {
        super(app, node, action, "", new Vector2f(2, 2), new Vector2f(HORIZONTAL - 0.5f, VERTICAL - 0.5f));

        // Enable adjustment for positioning
        adjust = true;

        pictureNormal.setImage(app.getAssetManager(), "Images/Settings_Button_normal.png", true);
        pictureHover.setImage(app.getAssetManager(), "Images/Settings_Button_hover.png", true);
    }

    /**
     * Handles hover behavior by changing the icon to the hover state.
     */
    @Override
    public void onHover() {
    }

    /**
     * Handles unhover behavior by restoring the icon to the normal state.
     */
    @Override
    public void onUnHover() {
    }
}

