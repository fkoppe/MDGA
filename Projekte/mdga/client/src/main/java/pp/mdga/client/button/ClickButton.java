package pp.mdga.client.button;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.ui.Picture;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.HAlignment;
import com.simsilica.lemur.VAlignment;
import com.simsilica.lemur.component.QuadBackgroundComponent;
import pp.mdga.client.MdgaApp;
import pp.mdga.client.acoustic.MdgaSound;

/**
 * Abstract base class for creating interactive buttons with click functionality.
 * This class extends {@link AbstractButton} and provides additional behavior such as
 * click handling, hover effects, and alignment management.
 */
public abstract class ClickButton extends AbstractButton {

    /**
     * The action to be executed when the button is clicked.
     */
    protected final Runnable action;

    /**
     * The label or text displayed on the button.
     */
    protected String label;

    /**
     * The size of the button in relative units.
     */
    protected Vector2f size;

    /**
     * The instance of the button being managed.
     */
    protected Button instance;

    /**
     * Constructs a ClickButton with the specified properties.
     *
     * @param app    the application instance for accessing resources
     * @param node   the node in the scene graph to which the button belongs
     * @param action the action to execute on button click
     * @param label  the text label displayed on the button
     * @param size   the size of the button
     * @param pos    the position of the button in relative units
     */
    ClickButton(MdgaApp app, Node node, Runnable action, String label, Vector2f size, Vector2f pos) {
        super(app, node);

        this.action = action;
        this.label = label;
        this.pos = pos;
        this.size = size;

        instance = new Button(label);

        // Add click behavior
        instance.addClickCommands((button) -> {
            app.getAcousticHandler().playSound(MdgaSound.BUTTON_PRESSED);
            action.run();
        });

        // Set text alignment
        instance.setTextHAlignment(HAlignment.Center);
        instance.setTextVAlignment(VAlignment.Center);

        // Add hover commands
        instance.addCommands(Button.ButtonAction.HighlightOn, (button) -> click());
        instance.addCommands(Button.ButtonAction.HighlightOff, (button) -> release());

        // Set font and colors
        instance.setFont(font);
        instance.setFocusColor(TEXT_NORMAL);

        calculateRelative();
        setRelative();
    }

    /**
     * Displays the button by attaching it and its background image to the node.
     */
    @Override
    public void show() {
        node.attachChild(pictureNormal);
        release();

        calculateRelative();
        setRelative();
        setImageRelative(pictureNormal);

        node.attachChild(instance);
    }

    /**
     * Hides the button by detaching it and its background images from the node.
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
     * Abstract method to define hover behavior. Must be implemented by subclasses.
     */
    protected abstract void onHover();

    /**
     * Abstract method to define unhover behavior. Must be implemented by subclasses.
     */
    protected abstract void onUnHover();

    /**
     * Handles the button click behavior, including visual feedback and sound effects.
     */
    protected void click() {
        instance.setColor(TEXT_PRESSED);
        instance.setHighlightColor(TEXT_PRESSED);

        QuadBackgroundComponent background = new QuadBackgroundComponent(BUTTON_PRESSED);
        instance.setBackground(background);

        app.getAcousticHandler().playSound(MdgaSound.UI_CLICK);

        if (node.hasChild(pictureNormal)) {
            node.detachChild(pictureNormal);
            setImageRelative(pictureHover);
            node.attachChild(pictureHover);
        }

        onHover();
    }

    /**
     * Resets the button to its normal state after a click or hover event.
     */
    protected void release() {
        instance.setColor(TEXT_NORMAL);
        instance.setHighlightColor(TEXT_NORMAL);

        QuadBackgroundComponent background = new QuadBackgroundComponent(BUTTON_NORMAL);
        instance.setBackground(background);

        if (node.hasChild(pictureHover)) {
            node.detachChild(pictureHover);
            setImageRelative(pictureNormal);
            node.attachChild(pictureNormal);
        }

        onUnHover();
    }

    /**
     * Sets the relative size and position of the button based on screen dimensions.
     */
    protected void setRelative() {
        instance.setFontSize(fontSize);

        instance.setPreferredSize(new Vector3f(size.x * widthStep, size.y * heightStep, 0));

        float xAdjust = 0.0f;
        if (adjust) {
            xAdjust = instance.getPreferredSize().x;
        }

        instance.setLocalTranslation(pos.x * horizontalStep - xAdjust, pos.y * verticalStep, -1);

        final float horizontalMid = ((float) app.getCamera().getWidth() / 2) - (instance.getPreferredSize().x / 2);
        final float verticalMid = ((float) app.getCamera().getHeight() / 2) - instance.getPreferredSize().y / 2;

        if (0 == pos.x) {
            instance.setLocalTranslation(horizontalMid, instance.getLocalTranslation().y, instance.getLocalTranslation().z);
        }

        if (0 == pos.y) {
            instance.setLocalTranslation(instance.getLocalTranslation().x, verticalMid, instance.getLocalTranslation().z);
        }
    }

    /**
     * Sets the relative size and position of the button's background image.
     *
     * @param picture the background image to set
     */
    protected void setImageRelative(Picture picture) {
        if (null == picture) {
            return;
        }

        final float larger = 10;

        picture.setWidth(instance.getPreferredSize().x + larger);
        picture.setHeight(instance.getPreferredSize().y + larger);

        picture.setLocalTranslation(
                instance.getLocalTranslation().x - larger / 2,
                (instance.getLocalTranslation().y - picture.getHeight()) + larger / 2,
                instance.getLocalTranslation().z + 0.01f
        );
    }
}

