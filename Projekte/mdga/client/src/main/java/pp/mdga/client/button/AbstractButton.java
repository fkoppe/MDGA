package pp.mdga.client.button;

import com.jme3.font.BitmapFont;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.scene.Node;
import com.jme3.ui.Picture;
import pp.mdga.client.MdgaApp;

/**
 * Represents an abstract base class for creating customizable button components in a graphical user interface.
 * This class provides the framework for rendering buttons with different visual states, such as normal and pressed,
 * and supports position adjustments and font customization.
 *
 * <p>Subclasses must implement the {@link #show()} and {@link #hide()} methods to define how the button
 * is displayed and hidden in the application.</p>
 */
public abstract class AbstractButton {
    /**
     * Color representing the normal state of the button.
     */
    public static final ColorRGBA BUTTON_NORMAL = ColorRGBA.fromRGBA255(233, 236, 239, 255);

    /**
     * Color representing the pressed state of the button.
     */
    public static final ColorRGBA BUTTON_PRESSED = ColorRGBA.fromRGBA255(105, 117, 89, 255);

    /**
     * Color representing the normal state of the button text.
     */
    public static final ColorRGBA TEXT_NORMAL = ColorRGBA.Black;

    /**
     * Color representing the pressed state of the button text.
     */
    public static final ColorRGBA TEXT_PRESSED = ColorRGBA.fromRGBA255(180, 195, 191, 255);

    /**
     * The image representing the normal state of the button.
     */
    protected Picture pictureNormal = new Picture("normalButton");

    /**
     * The image representing the hover state of the button.
     */
    protected Picture pictureHover = new Picture("normalButton");

    /**
     * The number of horizontal divisions for calculating relative sizes.
     */
    public static final float HORIZONTAL = 16;

    /**
     * The number of vertical divisions for calculating relative sizes.
     */
    public static final float VERTICAL = 9;

    /**
     * The font used for rendering text on the button.
     */
    protected BitmapFont font;

    /**
     * Reference to the application instance for accessing assets and settings.
     */
    protected final MdgaApp app;

    /**
     * Node in the scene graph to which the button belongs.
     */
    protected final Node node;

    /**
     * The position of the button in 2D space.
     */
    protected Vector2f pos;

    /**
     * Factor for scaling the font size.
     */
    protected float fontSizeFactor = 1.0f;

    /**
     * Computed font size based on scaling factor and screen dimensions.
     */
    protected float fontSize;

    /**
     * Computed horizontal step size based on screen dimensions.
     */
    protected float horizontalStep;

    /**
     * Computed vertical step size based on screen dimensions.
     */
    protected float verticalStep;

    /**
     * Computed height step size based on vertical steps.
     */
    protected float heightStep;

    /**
     * Computed width step size based on horizontal steps.
     */
    protected float widthStep;

    /**
     * Flag indicating whether adjustments are applied to the button.
     */
    protected boolean adjust = false;

    /**
     * Constructs an AbstractButton instance with the specified application context and scene node.
     * Initializes the button's visual elements and font.
     *
     * @param app  the application instance for accessing resources
     * @param node the node in the scene graph to which the button is attached
     */
    public AbstractButton(MdgaApp app, Node node) {
        this.app = app;
        this.node = node;

        pictureNormal.setImage(app.getAssetManager(), "Images/General_Button_normal.png", true);
        pictureHover.setImage(app.getAssetManager(), "Images/General_Button_hover.png", true);

        font = app.getAssetManager().loadFont("Fonts/Gunplay.fnt");
    }

    /**
     * Displays the button. Implementation must define how the button is rendered on the screen.
     */
    public abstract void show();

    /**
     * Hides the button. Implementation must define how the button is removed from the screen.
     */
    public abstract void hide();

    /**
     * Sets the position of the button in 2D space.
     *
     * @param pos the position to set
     */
    public void setPos(Vector2f pos) {
        this.pos = pos;
    }

    /**
     * Calculates relative sizes and dimensions for the button based on the screen resolution.
     */
    protected void calculateRelative() {
        fontSize = fontSizeFactor * 15 * (float) app.getCamera().getWidth() / 720;

        horizontalStep = (float) app.getCamera().getWidth() / HORIZONTAL;
        verticalStep = (float) app.getCamera().getHeight() / VERTICAL;
        heightStep = verticalStep / 2;
        widthStep = horizontalStep / 2;
    }

    public Vector2f getPos() {
        return pos;
    }
}
