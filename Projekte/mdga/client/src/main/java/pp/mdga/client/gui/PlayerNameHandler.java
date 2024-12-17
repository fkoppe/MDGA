package pp.mdga.client.gui;

import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.system.AppSettings;
import com.jme3.ui.Picture;
import pp.mdga.game.Color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Handles the display and management of player names and their associated data.
 */
public class PlayerNameHandler {
    private final BitmapFont playerFont;
    private final Node playerNameNode;
    private final List<Color> playerOrder;
    private final Map<Color, String> colorNameMap;
    private final Map<Color, Integer> colorCardMap;

    private final AppSettings appSettings;
    private final AssetManager assetManager;
    private Color ownColor;

    private static final float PADDING_TOP = 35;
    private static final float PADDING_LEFT = 50;
    private static final float MARGIN_NAMES = 50;
    private static final float IMAGE_SIZE = 50;
    private static final float TEXT_SIZE = 28;
    private static final ColorRGBA NORMAL_COLOR = ColorRGBA.White;
    private static final ColorRGBA ACTIVE_COLOR = ColorRGBA.Blue;
    private static final ColorRGBA OWN_COLOR = ColorRGBA.Cyan;

    private final Node guiNode;

    /**
     * Constructs a PlayerNameHandler.
     *
     * @param guiNode the GUI node to attach player names to
     * @param assetManager the asset manager to load resources
     * @param appSettings the application settings
     */
    public PlayerNameHandler(Node guiNode, AssetManager assetManager, AppSettings appSettings) {
        this.guiNode = guiNode;

        playerFont = assetManager.loadFont("Fonts/Gunplay.fnt");
        playerNameNode = new Node("player name node");
        playerOrder = new ArrayList<>();
        colorNameMap = new HashMap<>();
        colorCardMap = new HashMap<>();
        this.appSettings = appSettings;
        this.assetManager = assetManager;
    }

    /**
     * Shows the player names on the GUI.
     */
    public void show() {
        guiNode.attachChild(playerNameNode);
    }

    /**
     * Hides the player names from the GUI.
     */
    public void hide() {
        guiNode.detachChild(playerNameNode);
    }

    /**
     * Draws the player names and their associated data on the GUI.
     */
    private void drawPlayers() {
        playerNameNode.detachAllChildren();

        for (int i = 0; i < playerOrder.size(); i++) {
            Color color = playerOrder.get(i);
            if (!colorNameMap.containsKey(color)) throw new RuntimeException(color + " isn't mapped to a name");

            Node nameParent = new Node("nameParent");
            nameParent.attachChild(createColor(color));
            BitmapText name = createName(colorNameMap.get(color), i == 0, color == ownColor);
            nameParent.attachChild(name);
            if (colorCardMap.getOrDefault(color, 0) > 0) {
                Picture pic = createHandCard(name.getLineWidth());
                nameParent.attachChild(pic);
                nameParent.attachChild(createCardNum(colorCardMap.get(color), pic.getWidth(), pic.getLocalTranslation().getX()));
            }
            nameParent.setLocalTranslation(50, appSettings.getWindowHeight() - PADDING_TOP - MARGIN_NAMES * i, 0);
            playerNameNode.attachChild(nameParent);
        }
    }

    /**
     * Creates a BitmapText object to display the number of cards a player has.
     *
     * @param num the number of cards
     * @param lastWidth the width of the last element
     * @param lastX the x position of the last element
     * @return a BitmapText object displaying the number of cards
     */
    private Spatial createCardNum(int num, float lastWidth, float lastX) {
        BitmapText hudText = new BitmapText(playerFont);
        //renderedSize = 45
        hudText.setSize(TEXT_SIZE);
        hudText.setColor(NORMAL_COLOR);
        hudText.setText(String.valueOf(num));
        hudText.setLocalTranslation(lastX + lastWidth + 20, hudText.getHeight() / 2, 0);
        return hudText;
    }

    /**
     * Creates a Picture object to display a hand card image.
     *
     * @param width the width of the previous element
     * @return a Picture object displaying a hand card image
     */
    private Picture createHandCard(float width) {
        Picture pic = new Picture("HUD Picture");
        pic.setImage(assetManager, "./Images/handcard.png", true);
        pic.setWidth(IMAGE_SIZE);
        pic.setHeight(IMAGE_SIZE);
        pic.setPosition(-pic.getWidth() / 2 + width + PADDING_LEFT * 2, -pic.getHeight() / 2);
        return pic;
    }

    /**
     * Returns the image path for a given color.
     *
     * @param color the color to get the image path for
     * @return the image path for the given color
     */
    private String imagePath(Color color) {
        String root = "./Images/name_pictures/";
        return switch (color) {
            case ARMY -> root + "HEER_IMAGE.png";
            case NAVY -> root + "MARINE_IMAGE.png";
            case CYBER -> root + "CIR_IMAGE.png";
            case AIRFORCE -> root + "LW_IMAGE.png";
            default -> throw new RuntimeException("None is not valid");
        };
    }

    /**
     * Creates a Picture object to display a color image.
     *
     * @param color the color to create the image for
     * @return a Picture object displaying the color image
     */
    private Spatial createColor(Color color) {
        Picture pic = new Picture("HUD Picture");
        pic.setImage(assetManager, imagePath(color), true);
        pic.setWidth(IMAGE_SIZE);
        pic.setHeight(IMAGE_SIZE);
        pic.setPosition(-pic.getWidth() / 2, -pic.getHeight() / 2);
        return pic;
    }

    /**
     * Creates a BitmapText object to display a player's name.
     *
     * @param name the player's name
     * @param first whether the player is the first in the list
     * @param own whether the player is the current user
     * @return a BitmapText object displaying the player's name
     */
    private BitmapText createName(String name, boolean first, boolean own) {
        BitmapText hudText = new BitmapText(playerFont);
        //renderedSize = 45
        hudText.setSize(TEXT_SIZE);
        hudText.setColor(first ? ACTIVE_COLOR : own ? OWN_COLOR : NORMAL_COLOR);
        hudText.setText(own ? name + " (Du)" : name);
        hudText.setLocalTranslation(PADDING_LEFT, hudText.getHeight() / 2, 0);
        return hudText;
    }

    /**
     * Adds a player to the handler.
     *
     * @param color the color associated with the player
     * @param name the name of the player
     * @param own whether the player is the current user
     */
    public void addPlayer(Color color, String name, boolean own) {
        if (own) ownColor = color;
        colorNameMap.put(color, name);
        playerOrder.add(color);
        drawPlayers();
    }

    /**
     * Sets the active player.
     *
     * @param color the color associated with the active player
     */
    public void setActivePlayer(Color color) {
        if (playerOrder.get(0) == color) return;
        Color oldFirst = playerOrder.remove(0);
        playerOrder.remove(color);
        playerOrder.add(0, color);
        playerOrder.add(oldFirst);

        drawPlayers();
    }

    /**
     * Gets the name of a player by their color.
     *
     * @param color the color associated with the player
     * @return the name of the player
     */
    public String getName(Color color) {
        if (!colorNameMap.containsKey(color)) throw new RuntimeException("color is not in colorNameMap");
        return colorNameMap.get(color);
    }

    /**
     * Adds a card to a player.
     *
     * @param color the color associated with the player
     */
    public void addCard(Color color) {
        colorCardMap.put(color, colorCardMap.getOrDefault(color, 0) + 1);
        drawPlayers();
    }

    /**
     * Removes a card from a player.
     *
     * @param color the color associated with the player
     */
    public void removeCard(Color color) {
        if (colorCardMap.containsKey(color)) {
            colorCardMap.put(color, colorCardMap.getOrDefault(color, 0) - 1);
            if (colorCardMap.get(color) <= 0) colorCardMap.remove(color);
        }
        drawPlayers();
    }
}
