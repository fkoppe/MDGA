package pp.mdga.client.button;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.simsilica.lemur.*;
import com.simsilica.lemur.component.QuadBackgroundComponent;
import pp.mdga.client.MdgaApp;

/**
 * Represents an input button with a label and a text field, allowing users to input text.
 * The button is designed for graphical user interfaces and supports configurable
 * size, position, and character limit.
 */
public class InputButton extends AbstractButton {

    /**
     * The label associated with the input field, displayed above or beside the text field.
     */
    private Label label;

    /**
     * The text field where users input their text.
     */
    private TextField field;

    /**
     * A container to hold the label and the text field for layout management.
     */
    private Container container = new Container();

    /**
     * The maximum allowed length of the input text.
     */
    private final int maxLenght;

    /**
     * The size of the input button in relative units.
     */
    protected Vector2f size;

    /**
     * Constructs an InputButton with the specified label, character limit, and other properties.
     *
     * @param app       the application instance for accessing resources
     * @param node      the node in the scene graph to which the input button belongs
     * @param label     the label displayed with the input field
     * @param maxLenght the maximum number of characters allowed in the input field
     */
    public InputButton(MdgaApp app, Node node, String label, int maxLenght) {
        super(app, node);

        this.label = new Label(label);
        this.maxLenght = maxLenght;

        // Configure label properties
        this.label.setColor(TEXT_NORMAL);

        // Configure text field properties
        field = new TextField("");
        field.setColor(TEXT_NORMAL);
        field.setTextHAlignment(HAlignment.Left);
        field.setTextVAlignment(VAlignment.Center);

        // Set background for the text field
        QuadBackgroundComponent grayBackground = new QuadBackgroundComponent(BUTTON_NORMAL);
        field.setBackground(grayBackground);

        // Set fonts for label and text field
        this.label.setFont(font);
        field.setFont(font);

        // Default position and size
        pos = new Vector2f(0, 0);
        size = new Vector2f(5.5f, 1);

        // Add components to the container
        container.addChild(this.label);
        container.addChild(field);
    }

    /**
     * Displays the input button by attaching it to the scene graph node.
     */
    @Override
    public void show() {
        calculateRelative();
        setRelative();

        node.attachChild(container);
    }

    /**
     * Hides the input button by detaching it from the scene graph node.
     */
    @Override
    public void hide() {
        node.detachChild(container);
    }

    /**
     * Updates the input field, enforcing the character limit.
     * Trims the text if it exceeds the maximum allowed length.
     */
    public void update() {
        String text = field.getText();
        int length = text.length();

        if (length > maxLenght) {
            field.setText(text.substring(0, maxLenght));
        }
    }

    /**
     * Adjusts the relative size and position of the input button based on the screen resolution.
     */
    protected void setRelative() {
        this.label.setFontSize(fontSize);
        field.setFontSize(fontSize);

        field.setPreferredSize(new Vector3f(size.x * widthStep, size.y * heightStep, 0));

        float xAdjust = 0.0f;
        if (adjust) {
            xAdjust = container.getPreferredSize().x;
        }

        container.setLocalTranslation(pos.x * horizontalStep - xAdjust, pos.y * verticalStep, -1);

        final float horizontalMid = ((float) app.getCamera().getWidth() / 2) - (container.getPreferredSize().x / 2);
        final float verticalMid = ((float) app.getCamera().getHeight() / 2) - container.getPreferredSize().y / 2;

        if (0 == pos.x) {
            container.setLocalTranslation(horizontalMid, container.getLocalTranslation().y, -1);
        }

        if (0 == pos.y) {
            container.setLocalTranslation(container.getLocalTranslation().x, verticalMid, -1);
        }
    }

    /**
     * Retrieves the text currently entered in the input field.
     *
     * @return the current text in the input field
     */
    public String getString() {
        return field.getText();
    }

    /**
     * Sets the text of the input field to the specified string.
     *
     * @param string the text to set in the input field
     */
    public void setString(String string) {
        field.setText(string);
    }

    /**
     * Resets the input field by clearing its text.
     */
    public void reset() {
        field.setText("");
    }
}
