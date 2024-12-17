package pp.mdga.client.button;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.simsilica.lemur.*;
import com.simsilica.lemur.component.QuadBackgroundComponent;
import pp.mdga.client.MdgaApp;

/**
 * Represents a slider button component with a label, providing functionality for
 * adjusting values in a graphical user interface. It includes increment, decrement,
 * and thumb buttons, allowing for interactive adjustments.
 */
public class SliderButton extends AbstractButton {

    /**
     * The label displayed next to the slider.
     */
    private Label label;

    /**
     * The slider component for adjusting values.
     */
    private Slider slider;

    /**
     * A container to hold the label and slider for layout management.
     */
    private Container container = new Container();

    /**
     * The size of the slider button in relative units.
     */
    protected Vector2f size;

    /**
     * Constructs a SliderButton with the specified label.
     *
     * @param app   the application instance for accessing resources
     * @param node  the node in the scene graph to which the slider button belongs
     * @param label the text label displayed alongside the slider
     */
    public SliderButton(MdgaApp app, Node node, String label) {
        super(app, node);

        this.label = new Label(label);
        this.label.setColor(TEXT_NORMAL);

        // Configure the slider
        slider = new Slider("slider");

        // Configure decrement button
        slider.getDecrementButton().setText(" - ");
        slider.getDecrementButton().setFont(font);
        slider.getDecrementButton().setFocusColor(TEXT_NORMAL);
        slider.getDecrementButton().setTextVAlignment(VAlignment.Bottom);
        slider.getDecrementButton().setColor(TEXT_NORMAL);
        slider.getDecrementButton().setHighlightColor(TEXT_NORMAL);

        // Configure increment button
        slider.getIncrementButton().setText(" + ");
        slider.getIncrementButton().setFont(font);
        slider.getIncrementButton().setFocusColor(TEXT_NORMAL);
        slider.getIncrementButton().setTextVAlignment(VAlignment.Bottom);
        slider.getIncrementButton().setColor(TEXT_NORMAL);
        slider.getIncrementButton().setHighlightColor(TEXT_NORMAL);

        // Configure thumb button
        slider.getThumbButton().setText("X");
        slider.getThumbButton().setFont(font);
        slider.getThumbButton().setFocusColor(TEXT_NORMAL);
        slider.getThumbButton().setColor(TEXT_NORMAL);
        slider.getThumbButton().setHighlightColor(TEXT_NORMAL);

        // Set slider background
        QuadBackgroundComponent background = new QuadBackgroundComponent(BUTTON_NORMAL);
        slider.setBackground(background);

        // Set label background
        QuadBackgroundComponent labelBackground = new QuadBackgroundComponent(BUTTON_NORMAL);
        this.label.setBackground(labelBackground);

        // Configure the label font
        this.label.setFont(font);
        this.label.setTextHAlignment(HAlignment.Center);

        // Default position and size
        pos = new Vector2f(0, 0);
        size = new Vector2f(6f, 1);

        // Add label and slider to container
        container.addChild(this.label);
        container.addChild(slider);
    }

    /**
     * Displays the slider button by attaching its container to the scene graph.
     */
    @Override
    public void show() {
        calculateRelative();
        setRelative();

        node.attachChild(container);
    }

    /**
     * Hides the slider button by detaching its container from the scene graph.
     */
    @Override
    public void hide() {
        node.detachChild(container);
    }

    /**
     * Sets the relative size and position of the slider button based on screen resolution.
     */
    protected void setRelative() {
        this.label.setFontSize(fontSize);

        // Set font sizes for slider components
        slider.getDecrementButton().setFontSize(fontSize);
        slider.getIncrementButton().setFontSize(fontSize);
        slider.getThumbButton().setFontSize(fontSize);

        // Set slider size
        slider.setPreferredSize(new Vector3f(size.x * widthStep, size.y * heightStep, 0));

        float xAdjust = 0.0f;
        if (adjust) {
            xAdjust = container.getPreferredSize().x;
        }

        // Set container position
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
     * Retrieves the current percentage value of the slider.
     *
     * @return the current percentage value as a float
     */
    public float getPercent() {
        return (float) slider.getModel().getPercent();
    }

    /**
     * Sets the slider to the specified percentage value.
     *
     * @param percent the percentage value to set
     */
    public void setPercent(float percent) {
        slider.getModel().setPercent(percent);
    }
}

