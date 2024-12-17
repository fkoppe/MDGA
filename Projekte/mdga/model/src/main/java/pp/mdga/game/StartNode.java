package pp.mdga.game;

import com.jme3.network.serializing.Serializable;

/**
 * Represents a start node.
 */
@Serializable
public class StartNode extends Node {
    /**
     * The color of the node.
     */
    private Color color;

    /**
     * Creates a new start node with the given color.
     *
     * @param color the color of the node
     */
    public StartNode(Color color) {
        super(null);
        this.color = color;
    }

    /**
     * Default constructor for serialization.
     */
    private StartNode() {
        super(null);
        color = Color.NONE;
    }

    /**
     * This method is used to get the color of the node
     *
     * @return the color of the node
     */
    public Color getColor() {
        return color;
    }

    /**
     * This method is used to set the color of the node
     *
     * @param color the new color of the node
     */
    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public boolean isStart() {
        return true;
    }
}
