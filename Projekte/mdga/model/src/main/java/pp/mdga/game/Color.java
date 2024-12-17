package pp.mdga.game;

import com.jme3.network.serializing.Serializable;

import java.util.ArrayList;

/**
 * This enumeration represents the four different TSK colors that a player can choose.
 * Additionally, the NONE color indicates the absence of a color.
 */
@Serializable
public enum Color {
    /**
     * Represents the air force color.
     */
    AIRFORCE,
    /**
     * Represents the cyber color.
     */
    CYBER,
    /**
     * Represents the navy color.
     */
    NAVY,
    /**
     * Represents the army color.
     */
    ARMY,
    /**
     * Represents the none color.
     */
    NONE;

    /**
     * This method will be used to return a Color enumeration depending on the given index parameter.
     *
     * @param index as the index of the color inside the values as an Integer.
     * @return a Color enumeration.
     */
    public static Color getColorByIndex(int index) {
        if (index < 0 || index >= values().length) {
            throw new IllegalArgumentException("");
        }

        return values()[index];
    }

    /**
     * This method will be used to calculate the next color inside the sequence.
     *
     * @return color as a Color Enumeration.
     */
    public Color next(Game game) {
        ArrayList<Color> colorsInGame = new ArrayList<>();

        for (Player p : game.getPlayers().values()) {
            if (p.isFinished()) {
                continue;
            }
            colorsInGame.add(p.getColor());
        }

        int current = game.getActiveColor().ordinal();

        int next = (current + 1) % 4;
        while (!colorsInGame.contains(Color.values()[next])) {
            next++;
            next %= 4;
        }

        return Color.values()[next];
    }

    /**
     * This method will be used to return the color of the given index.
     *
     * @param i as the index of the color as an Integer.
     * @return a Color enumeration.
     */
    public static Color getColor(int i) {
        for (Color color : Color.values()) {
            if (color.ordinal() == i) {
                return color;
            }
        }
        return null;
    }
}
