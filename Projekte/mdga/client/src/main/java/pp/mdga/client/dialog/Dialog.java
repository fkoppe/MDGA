package pp.mdga.client.dialog;

import com.jme3.scene.Node;
import pp.mdga.client.MdgaApp;

/**
 * The {@code Dialog} class serves as an abstract base class for dialogs in the application.
 * It provides functionality for showing and hiding the dialog and defines abstract methods
 * for custom behavior when the dialog is shown or hidden.
 */
public abstract class Dialog {
    protected final MdgaApp app;
    protected final Node node = new Node();

    private final Node root;

    /**
     * Constructs a {@code Dialog}.
     *
     * @param app  The main application managing the dialog.
     * @param node The root node to which the dialog's node will be attached.
     */
    Dialog(MdgaApp app, Node node) {
        this.app = app;
        this.root = node;
    }

    /**
     * Shows the dialog by attaching its node to the root node and invoking the {@code onShow} method.
     */
    public void show() {
        root.attachChild(node);

        onShow();
    }

    /**
     * Hides the dialog by detaching its node from the root node and invoking the {@code onHide} method.
     */
    public void hide() {
        root.detachChild(node);

        onHide();
    }

    /**
     * Called when the dialog is shown. Subclasses must implement this method to define custom behavior.
     */
    protected abstract void onShow();

    /**
     * Called when the dialog is hidden. Subclasses must implement this method to define custom behavior.
     */
    protected abstract void onHide();
}
