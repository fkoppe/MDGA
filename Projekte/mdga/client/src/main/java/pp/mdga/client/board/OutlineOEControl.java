package pp.mdga.client.board;

import com.jme3.math.ColorRGBA;
import com.jme3.post.FilterPostProcessor;
import com.jme3.renderer.Camera;
import pp.mdga.client.MdgaApp;
import pp.mdga.client.outline.OutlineControl;

/**
 * OutlineOEControl class extends OutlineControl to manage outline colors and widths
 * for own and enemy objects, including hover and select states.
 */
public class OutlineOEControl extends OutlineControl {
    private static final ColorRGBA OUTLINE_OWN_COLOR = ColorRGBA.White;
    private static final ColorRGBA OUTLINE_ENEMY_COLOR = ColorRGBA.Red;
    private static final ColorRGBA OUTLINE_OWN_HOVER_COLOR = ColorRGBA.Yellow;
    private static final ColorRGBA OUTLINE_ENEMY_HOVER_COLOR = ColorRGBA.Green;
    private static final ColorRGBA OUTLINE_OWN_SELECT_COLOR = ColorRGBA.Cyan;
    private static final ColorRGBA OUTLINE_ENEMY_SELECT_COLOR = ColorRGBA.Orange;
    private static final int OUTLINE_HIGHLIGHT_WIDTH = 8;
    private static final int OUTLINE_HOVER_WIDTH = 8;
    private static final int OUTLINE_SELECT_WIDTH = 10;

    /**
     * Constructor for OutlineOEControl.
     *
     * @param app the MdgaApp instance
     * @param fpp the FilterPostProcessor instance
     * @param cam the Camera instance
     */
    public OutlineOEControl(MdgaApp app, FilterPostProcessor fpp, Camera cam){
        super(app, fpp, cam,
          OUTLINE_OWN_COLOR, OUTLINE_HIGHLIGHT_WIDTH,
          OUTLINE_OWN_HOVER_COLOR, OUTLINE_HOVER_WIDTH,
          OUTLINE_OWN_SELECT_COLOR, OUTLINE_SELECT_WIDTH
        );
    }

    /**
     * Sets the outline colors and enables selection for own objects.
     */
    public void selectableOwn(){
        setHighlightColor(OUTLINE_OWN_COLOR);
        setHoverColor(OUTLINE_OWN_HOVER_COLOR);
        setSelectColor(OUTLINE_OWN_SELECT_COLOR);
        selectableOn();
    }

    /**
     * Sets the outline colors and enables selection for enemy objects.
     */
    public void selectableEnemy(){
        setHighlightColor(OUTLINE_ENEMY_COLOR);
        setHoverColor(OUTLINE_ENEMY_HOVER_COLOR);
        setSelectColor(OUTLINE_ENEMY_SELECT_COLOR);
        selectableOn();
    }
}
