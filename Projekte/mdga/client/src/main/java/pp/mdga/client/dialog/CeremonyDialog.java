package pp.mdga.client.dialog;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.scene.Node;
import pp.mdga.client.MdgaApp;
import pp.mdga.client.button.AbstractButton;
import pp.mdga.client.button.LabelButton;
import pp.mdga.client.button.MenuButton;

import java.util.ArrayList;

/**
 * The {@code CeremonyDialog} class displays a dialog containing statistical data in a tabular format.
 * It allows adding rows of statistics and manages their visibility when shown or hidden.
 */
public class CeremonyDialog extends Dialog {
    private ArrayList<ArrayList<LabelButton>> labels;

    float offsetX;

    /**
     * Constructs a {@code CeremonyDialog}.
     *
     * @param app  The main application managing the dialog.
     * @param node The root node for attaching UI elements.
     */
    public CeremonyDialog(MdgaApp app, Node node) {
        super(app, node);

        prepare();
    }

    /**
     * Called when the dialog is shown. Makes all label buttons in the table visible.
     */
    @Override
    protected void onShow() {
        for (ArrayList<LabelButton> row : labels) {
            for (LabelButton b : row) {
                b.show();
            }
        }
    }

    /**
     * Called when the dialog is hidden. Hides all label buttons in the table.
     */
    @Override
    protected void onHide() {
        for (ArrayList<LabelButton> row : labels) {
            for (LabelButton b : row) {
                b.hide();
            }
        }
    }

    /**
     * Adds a row of statistical data to the dialog.
     *
     * @param name The name of the player or category for the row.
     * @param v1   The value for the first column.
     * @param v2   The value for the second column.
     * @param v3   The value for the third column.
     * @param v4   The value for the fourth column.
     * @param v5   The value for the fifth column.
     * @param v6   The value for the sixth column.
     */
    public void addStatisticsRow(String name, int v1, int v2, int v3, int v4, int v5, int v6) {
        float offsetYSmall = 0.5f;

        ArrayList<LabelButton> row = new ArrayList<>();

        Vector2f sizeSmall = new Vector2f(4, 1.2f);
        row.add(new LabelButton(app, node, name, sizeSmall, new Vector2f(), true));
        row.add(new LabelButton(app, node, "" + v1, sizeSmall, new Vector2f(), false));
        row.add(new LabelButton(app, node, "" + v2, sizeSmall, new Vector2f(), false));
        row.add(new LabelButton(app, node, "" + v3, sizeSmall, new Vector2f(), false));
        row.add(new LabelButton(app, node, "" + v4, sizeSmall, new Vector2f(), false));
        row.add(new LabelButton(app, node, "" + v5, sizeSmall, new Vector2f(), false));
        row.add(new LabelButton(app, node, "" + v6, sizeSmall, new Vector2f(), false));

        ColorRGBA colorText = AbstractButton.TEXT_NORMAL.clone();
        colorText.a = 0.2f;

        ColorRGBA colorButton = AbstractButton.BUTTON_NORMAL.clone();
        colorButton.a = 0.2f;

        int j = 0;
        for (LabelButton b : row) {
            if (j > 0) {
                b.setColor(colorText, colorButton);
            }

            b.setPos(new Vector2f(offsetX, MenuButton.VERTICAL - offsetYSmall));
            offsetYSmall += 0.8f;

            j++;
        }

        offsetX += 2.3f;

        labels.add(row);
    }

    /**
     * Prepares the initial layout of the dialog, including header labels.
     */
    public void prepare() {
        offsetX = 0.5f;

        labels = new ArrayList<>();

        ArrayList<LabelButton> first = new ArrayList<>();
        Vector2f size = new Vector2f(4, 1.2f);
        first.add(new LabelButton(app, node, "", size, new Vector2f(), true));
        first.add(new LabelButton(app, node, "Figuren geworfen", size, new Vector2f(), true));
        first.add(new LabelButton(app, node, "Figuren verloren", size, new Vector2f(), true));
        first.add(new LabelButton(app, node, "Gespielte Bonuskarten", size, new Vector2f(), true));
        first.add(new LabelButton(app, node, "Gewürfelte 6en", size, new Vector2f(), true));
        first.add(new LabelButton(app, node, "Gelaufene Felder", size, new Vector2f(), true));
        first.add(new LabelButton(app, node, "Bonusfeldern erreicht", size, new Vector2f(), true));

        float offsetY = 0.5f;

        for (LabelButton b : first) {
            b.setPos(new Vector2f(offsetX, MenuButton.VERTICAL - offsetY));
            offsetY += 0.8f;
        }

        offsetX += 2.3f;

        labels.add(first);
    }
}
