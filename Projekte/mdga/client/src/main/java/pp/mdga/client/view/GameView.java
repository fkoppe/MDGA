package pp.mdga.client.view;

import com.jme3.post.FilterPostProcessor;
import com.jme3.scene.Node;
import pp.mdga.client.MdgaApp;
import pp.mdga.client.acoustic.MdgaSound;
import pp.mdga.client.board.BoardHandler;
import pp.mdga.client.board.CameraHandler;
import pp.mdga.client.button.ButtonLeft;
import pp.mdga.client.button.ButtonRight;
import pp.mdga.client.dialog.InterruptDialog;
import pp.mdga.client.gui.GuiHandler;
import pp.mdga.game.Color;

/**
 * Represents the view for the game.
 */
public class GameView extends MdgaView {
    private BoardHandler boardHandler;
    private CameraHandler camera;
    private GuiHandler guiHandler;

    private ButtonLeft leaveButton;
    public ButtonRight confirmButton;

    private ButtonRight noPowerButton;

    private Color ownColor = null;

    private InterruptDialog interruptDialog;

    private FilterPostProcessor fpp;

    public boolean needConfirm = false;
    public boolean needNoPower = false;

    private Node guiHandlerNode = new Node();

    /**
     * Constructs a new GameView.
     *
     * @param app the application instance
     */
    public GameView(MdgaApp app) {
        super(app);

        leaveButton = new ButtonLeft(app, settingsNode, () -> app.getModelSynchronize().leave(), "Spiel verlassen", 1);

        confirmButton = new ButtonRight(app, guiNode, () -> app.getModelSynchronize().confirm(), "Bestätigen", 1);

        noPowerButton = new ButtonRight(app, guiNode, () -> app.getModelSynchronize().confirm(), "Verzichten", 1);

        interruptDialog = new InterruptDialog(app, guiNode);

        fpp = new FilterPostProcessor(app.getAssetManager());
        this.camera = new CameraHandler(app, fpp);
        this.boardHandler = new BoardHandler(app, rootNode, fpp);

        guiHandler = new GuiHandler(app, guiHandlerNode);

        guiNode.attachChild(guiHandlerNode);
    }

    /**
     * Called when entering the view.
     */
    @Override
    public void onEnter() {
        camera.init(ownColor);
        boardHandler.init();
        guiHandler.init(ownColor);

        app.getViewPort().addProcessor(fpp);

        app.getAcousticHandler().playSound(MdgaSound.START);
    }

    /**
     * Called when leaving the view.
     */
    @Override
    public void onLeave() {
        boardHandler.shutdown();
        guiHandler.shutdown();
        camera.shutdown();

        confirmButton.hide();
        noPowerButton.hide();

        app.getViewPort().removeProcessor(fpp);
    }

    /**
     * Called to update the view.
     *
     * @param tpf time per frame
     */
    @Override
    public void onUpdate(float tpf) {
        camera.update(app.getInputSynchronize().getScroll(), app.getInputSynchronize().getRotation());
    }

    /**
     * Called when entering an overlay.
     *
     * @param overlay the overlay being entered
     */
    @Override
    protected void onEnterOverlay(Overlay overlay) {
        if (overlay == Overlay.SETTINGS) {
            leaveButton.show();
        }
    }

    /**
     * Called when leaving an overlay.
     *
     * @param overlay the overlay being left
     */
    @Override
    protected void onLeaveOverlay(Overlay overlay) {
        if (overlay == Overlay.SETTINGS) {
            leaveButton.hide();
        }
    }

    /**
     * Leaves the game.
     */
    private void leaveGame() {
        app.getModelSynchronize().leave();
    }

    /**
     * Gets the board handler.
     *
     * @return the board handler
     */
    public BoardHandler getBoardHandler() {
        return boardHandler;
    }

    /**
     * Gets the GUI handler.
     *
     * @return the GUI handler
     */
    public GuiHandler getGuiHandler() {
        return guiHandler;
    }

    /**
     * Sets the player's color.
     *
     * @param ownColor the player's color
     */
    public void setOwnColor(Color ownColor) {
        this.ownColor = ownColor;
    }

    /**
     * Gets the player's color.
     *
     * @return the player's color
     */
    public Color getOwnColor() {
        return ownColor;
    }

    /**
     * Shows the confirm button and hides the no power button.
     */
    public void needConfirm() {
        noPowerButton.hide();
        confirmButton.show();

        needConfirm = true;
    }

    /**
     * Hides the confirm button.
     */
    public void noConfirm() {
        confirmButton.hide();

        needConfirm = false;
    }

    /**
     * Shows the no power button and hides the confirm button.
     */
    public void showNoPower() {
        confirmButton.hide();
        noPowerButton.show();

        needNoPower = true;
    }

    /**
     * Hides the no power button.
     */
    public void hideNoPower() {
        noPowerButton.hide();
        needNoPower = false;
    }

    /**
     * Enters the interrupt state with the specified color.
     *
     * @param color the color to set
     */
    public void enterInterrupt(Color color) {
        enterOverlay(Overlay.INTERRUPT);

        guiNode.detachChild(guiHandlerNode);
        app.getGuiNode().attachChild(guiNode);

        app.getInputSynchronize().setClickAllowed(false);

        interruptDialog.setColor(color);
        interruptDialog.show();
    }

    /**
     * Leaves the interrupt state.
     */
    public void leaveInterrupt() {
        leaveOverlay(Overlay.INTERRUPT);

        app.getGuiNode().detachChild(guiNode);
        guiNode.attachChild(guiHandlerNode);

        app.getInputSynchronize().setClickAllowed(true);

        app.getAcousticHandler().playSound(MdgaSound.START);

        interruptDialog.hide();
    }
}
