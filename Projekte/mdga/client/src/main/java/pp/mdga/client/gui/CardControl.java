package pp.mdga.client.gui;

import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Sphere;
import pp.mdga.client.MdgaApp;
import pp.mdga.client.outline.OutlineControl;

/**
 * CardControl class extends OutlineControl to manage the visual representation
 * and behavior of a card in the game.
 */
public class CardControl extends OutlineControl {

    private static final ColorRGBA HIGHLIGHT_COLOR = ColorRGBA.Yellow;
    private static final int HIGHLIGHT_WIDTH = 9;

    private static final ColorRGBA HOVER_COLOR = ColorRGBA.Green;
    private static final int HOVER_WIDTH = 12;

    private static final ColorRGBA SELECT_COLOR = ColorRGBA.Blue;
    private static final int SELECT_WIDTH = 13;

    private Node root;
    private BitmapText num;

    /**
     * Constructor for CardControl.
     *
     * @param app  the application instance
     * @param fpp  the FilterPostProcessor instance
     * @param cam  the Camera instance
     * @param root the root Node
     */
    public CardControl(MdgaApp app, FilterPostProcessor fpp, Camera cam, Node root) {
        super(app, fpp, cam,
            HIGHLIGHT_COLOR, HIGHLIGHT_WIDTH,
            HOVER_COLOR, HOVER_WIDTH,
            SELECT_COLOR, SELECT_WIDTH
        );

        this.root = root;

        Node rootNum = createNum();
        rootNum.setLocalTranslation(new Vector3f(0.35f, 0.8f, 0));
        root.attachChild(rootNum);
    }

    /**
     * Creates a Node containing a number and a circle geometry.
     *
     * @return the created Node
     */
    private Node createNum() {
        Node rootNum = new Node("root Num");
        Geometry circle = new Geometry("circle", new Sphere(20, 20, 1));
        circle.setLocalTranslation(new Vector3f(0.03f, 0.01f, 1));
        circle.setLocalScale(0.2f);
        Material mat = new Material(getApp().getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Black);
        circle.setMaterial(mat);
//        root.attachChild(circle);
        BitmapFont guiFont = getApp().getAssetManager().loadFont("Fonts/Gunplay.fnt");
        num = new BitmapText(guiFont);
        num.setSize(0.3f);
        num.setText("1");
        num.setColor(ColorRGBA.White);
        num.setLocalTranslation(-num.getLineWidth() / 2, num.getHeight() / 2, 2);
        rootNum.attachChild(circle);
        rootNum.attachChild(num);
        return rootNum;
    }

    /**
     * Sets the number displayed on the card.
     *
     * @param num the number to display
     */
    public void setNumCard(int num) {
        this.num.setText(String.valueOf(num));
    }

    /**
     * Gets the root Node of the card.
     *
     * @return the root Node
     */
    public Node getRoot() {
        return root;
    }

    /**
     * Initializes the spatial properties of the card.
     */
    @Override
    public void initSpatial() {
    }

    private final static Vector3f HIGHLIGHT_Y = new Vector3f(0, 0.4f, 0);
}
