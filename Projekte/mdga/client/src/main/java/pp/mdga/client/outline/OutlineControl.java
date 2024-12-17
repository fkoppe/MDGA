package pp.mdga.client.outline;

import com.jme3.math.ColorRGBA;
import com.jme3.post.FilterPostProcessor;
import com.jme3.renderer.Camera;
import pp.mdga.client.InitControl;
import pp.mdga.client.MdgaApp;

/**
 * A control that provides outline functionality to a spatial object.
 * This class is responsible for adding an outline effect to a spatial
 * object, allowing it to be highlighted or deselected.
 */
public class OutlineControl extends InitControl {
    /**
     * The {@link SelectObjectOutliner} responsible for managing the outline effect.
     */
    private final SelectObjectOutliner selectObjectOutliner;
    private final MdgaApp app;
    private boolean hoverable = false;
    private boolean highlight = false;
    private boolean selectable = false;
    private boolean select = false;
    private ColorRGBA highlightColor;
    private int highlightWidth;
    private ColorRGBA hoverColor;
    private int hoverWidth;
    private ColorRGBA selectColor;
    private int selectWidth;

    /**
     * Constructs an {@code OutlineControl} with default thickness for the object outline.
     *
     * @param app The main application managing the outline control.
     * @param fpp The {@code FilterPostProcessor} used for post-processing effects.
     */
    public OutlineControl(MdgaApp app, FilterPostProcessor fpp, Camera cam,
                          ColorRGBA highlightColor, int highlightWidth,
                          ColorRGBA hoverColor, int hoverWidth,
                          ColorRGBA selectColor, int selectWidth
    ) {
        this.app = app;
        this.highlightColor = highlightColor;
        this.highlightWidth = highlightWidth;
        this.hoverColor = hoverColor;
        this.hoverWidth = hoverWidth;
        this.selectColor = selectColor;
        this.selectWidth = selectWidth;
        selectObjectOutliner = new SelectObjectOutliner(fpp, app.getRenderManager(), app.getAssetManager(), cam, app);
    }

    /**
     * Applies an outline to the spatial object with the given color and width.
     *
     * @param color The {@link ColorRGBA} representing the color of the outline.
     * @param width The width of the outline.
     */
    public void outline(ColorRGBA color, int width) {
        outlineOff();
        selectObjectOutliner.select(spatial, color, width);
    }

    /**
     * Removes the outline effect from the spatial object.
     */
    public void outlineOff() {
        selectObjectOutliner.deselect(spatial);
    }

    /**
     * Retrieves the instance of the {@code MdgaApp} associated with this control.
     *
     * @return The {@code MdgaApp} instance.
     */
    public MdgaApp getApp() {
        return app;
    }

    public void highlightOn() {
        highlight = true;
        outline(highlightColor, highlightWidth);
    }

    public void highlightOff() {
        highlight = false;
        outlineOff();
    }

    public void hoverOn() {
        if (!hoverable) return;
        outline(hoverColor, hoverWidth);
    }

    public void hoverOff() {
        if (!hoverable) return;

        if (select) selectOn();
        else if (highlight) highlightOn();
        else outlineOff();
    }

    public void selectOn() {
        if (!selectable) return;
        select = true;
        outline(selectColor, selectWidth);
    }

    public void selectOff() {
        select = false;
        if (highlight) highlightOn();
        else outlineOff();
    }

    public void selectableOn(){
        setSelectable(true);
        setHoverable(true);
        highlightOn();
        select = false;
    }

    public void selectableOff(){
        setSelectable(false);
        setHoverable(false);
        highlightOff();
        select = false;
    }

    private void setSelectable(boolean selectable) {
        this.selectable = selectable;
    }

    public boolean isSelected() {
        return select;
    }

    public boolean isSelectable() {
        return selectable;
    }

    public boolean isHoverable() {
        return hoverable;
    }

    private void setHoverable(boolean hoverable) {
        this.hoverable = hoverable;
    }

    public void setHighlightColor(ColorRGBA color){
        highlightColor = color;
    }

    public void setHighlightWidth(int width){
        highlightWidth = width;
    }

    public void setHoverColor(ColorRGBA color){
        hoverColor = color;
    }

    public void setHoverWidth(int width){
        hoverWidth = width;
    }

    public void setSelectColor(ColorRGBA color){
        selectColor = color;
    }

    public void setSelectWidth(int width){
        selectWidth = width;
    }
}
