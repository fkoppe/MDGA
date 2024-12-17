package pp.mdga.client.dialog;

import com.jme3.math.Vector2f;
import com.jme3.scene.Node;
import pp.mdga.client.MdgaApp;
import pp.mdga.client.button.ButtonRight;
import pp.mdga.client.button.LabelButton;
import pp.mdga.client.button.MenuButton;
import pp.mdga.game.Color;

/**
 * The {@code InterruptDialog} class represents a dialog that interrupts the game flow,
 * providing a message and the option to force an action if the user is a host.
 */
public class InterruptDialog extends Dialog {
    private ButtonRight forceButton;

    private LabelButton label;

    private String text = "";

    /**
     * Constructs an {@code InterruptDialog}.
     *
     * @param app  The main application managing the dialog.
     * @param node The root node for attaching UI elements.
     */
    public InterruptDialog(MdgaApp app, Node node) {
        super(app, node);

        forceButton = new ButtonRight(app, node, () -> app.getModelSynchronize().force(), "Erzwingen", 1);
    }

    /**
     * Called when the dialog is shown. Displays the label and optionally the force button if the user is the host.
     */

    @Override
    protected void onShow() {
        if (app.getGameLogic().isHost()) {
            forceButton.show();
        }

        label = new LabelButton(app, node, "Warte auf " + text + "...", new Vector2f(5.5f * 1.5f, 2), new Vector2f(0.5f, 0f), false);

        float offset = 2.8f;
        label.setPos(new Vector2f(0, MenuButton.VERTICAL - offset));

        label.show();
    }

    /**
     * Called when the dialog is hidden. Hides the label and the force button.
     */
    @Override
    protected void onHide() {
        forceButton.hide();
        label.hide();
    }

    /**
     * Sets the displayed text based on the specified color.
     *
     * @param color The color used to determine the text (e.g., "Luftwaffe" for AIRFORCE).
     */
    public void setColor(Color color) {
        switch (color) {
            case AIRFORCE:
                text = "Luftwaffe";
                break;
            case ARMY:
                text = "Heer";
                break;
            case NAVY:
                text = "Marine";
                break;
            case CYBER:
                text = "CIR";
                break;
        }
    }
}
