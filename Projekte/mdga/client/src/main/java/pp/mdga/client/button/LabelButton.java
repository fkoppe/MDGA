package pp.mdga.client.button;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.scene.Node;
import com.simsilica.lemur.component.QuadBackgroundComponent;
import pp.mdga.client.MdgaApp;

/**
 * A specialized button that can function as a label or a clickable button.
 * It inherits from {@link ClickButton} and allows for flexible usage with or without button-like behavior.
 */
public class LabelButton extends ClickButton {

    /**
     * The color of the text displayed on the label or button.
     */
    private ColorRGBA text = TEXT_NORMAL;

    /**
     * The color of the button's background.
     */
    private ColorRGBA button = BUTTON_NORMAL;

    /**
     * Flag indicating whether this component functions as a button.
     */
    private boolean isButton;

    /**
     * Constructs a LabelButton with specified properties.
     *
     * @param app      the application instance for accessing resources
     * @param node     the node in the scene graph to which the button belongs
     * @param label    the text displayed on the label or button
     * @param size     the size of the label or button
     * @param pos      the position of the label or button in relative units
     * @param isButton whether this component acts as a button or a simple label
     */
    public LabelButton(MdgaApp app, Node node, String label, Vector2f size, Vector2f pos, boolean isButton) {
        super(app, node, () -> {
        }, label, size, pos);

        this.isButton = isButton;

        // Use the same image for hover and normal states
        pictureHover = pictureNormal;
    }

    /**
     * Displays the label or button, attaching it to the scene graph.
     * If the component is a button, it also attaches the background image.
     */
    @Override
    public void show() {
        if (isButton) {
            node.attachChild(pictureNormal);
        }
        release();

        calculateRelative();
        setRelative();
        setImageRelative(pictureNormal);

        instance.setFontSize(fontSize / 2);

        node.attachChild(instance);
    }

    /**
     * Hides the label or button, detaching it from the scene graph.
     */
    @Override
    public void hide() {
        node.detachChild(instance);

        if (node.hasChild(pictureNormal)) {
            node.detachChild(pictureNormal);
        }
        if (node.hasChild(pictureHover)) {
            node.detachChild(pictureHover);
        }
    }

    /**
     * Handles hover behavior, updating the colors of the text and background.
     */
    @Override
    public void onHover() {
        instance.setColor(text);
        instance.setHighlightColor(text);

        QuadBackgroundComponent background = new QuadBackgroundComponent(button);
        instance.setBackground(background);
    }

    /**
     * Handles unhover behavior, restoring the colors of the text and background.
     */
    @Override
    public void onUnHover() {
        instance.setColor(text);
        instance.setHighlightColor(text);

        QuadBackgroundComponent background = new QuadBackgroundComponent(button);
        instance.setBackground(background);
    }

    /**
     * Sets the text displayed on the label or button.
     *
     * @param text the text to display
     */
    public void setText(String text) {
        instance.setText(text);
    }

    /**
     * Sets the colors of the text and background, and refreshes the label or button.
     *
     * @param text   the color of the text
     * @param button the color of the button's background
     */
    public void setColor(ColorRGBA text, ColorRGBA button) {
        this.text = text;
        this.button = button;

        hide();
        show();
    }
}

