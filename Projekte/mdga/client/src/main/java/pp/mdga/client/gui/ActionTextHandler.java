package pp.mdga.client.gui;

import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;
import pp.mdga.client.animation.ZoomControl;
import pp.mdga.game.Color;

/**
 * The {@code ActionTextHandler} class manages the display of animated and stylized text messages in the game's UI.
 * It supports dynamic text creation with spacing, color, and effects, such as dice rolls, player actions, and rankings.
 */
class ActionTextHandler {
    private Node root;
    private BitmapFont font;
    private AppSettings appSettings;
    private int ranking;

    float paddingRanked = 100;

    /**
     * Constructs an {@code ActionTextHandler}.
     *
     * @param guiNode      The GUI node where the text messages will be displayed.
     * @param assetManager The asset manager used to load fonts and other assets.
     * @param appSettings  The application settings for positioning and sizing.
     */
    ActionTextHandler(Node guiNode, AssetManager assetManager, AppSettings appSettings) {
        root = new Node("actionTextRoot");
        guiNode.attachChild(root);

        root.setLocalTranslation(center(appSettings.getWidth(), appSettings.getHeight(), Vector3f.ZERO));
        font = assetManager.loadFont("Fonts/Gunplay.fnt");
        this.appSettings = appSettings;
        ranking = 0;
    }

    /**
     * Creates a {@code Node} containing text with specified spacing, size, and colors for each segment of the text.
     *
     * @param textArr  An array of strings representing the text to be displayed.
     * @param spacing  The spacing between individual characters.
     * @param size     The size of the text.
     * @param colorArr An array of {@code ColorRGBA} representing the color for each string in {@code textArr}.
     * @return A {@code Node} containing the styled text with spacing and color applied.
     * @throws RuntimeException if the lengths of {@code textArr} and {@code colorArr} do not match.
     */
    private Node createTextWithSpacing(String[] textArr, float spacing, float size, ColorRGBA[] colorArr) {
        if (textArr.length != colorArr.length) throw new RuntimeException("text and color are not the same length");

        Node textNode = new Node("TextWithSpacing");
        Node center = new Node();
        float xOffset = 0;
        for (int i = 0; i < textArr.length; i++) {
            String text = textArr[i];
            ColorRGBA color = colorArr[i];
            for (char c : text.toCharArray()) {
                BitmapText letter = new BitmapText(font);
                letter.setColor(color);
                letter.setSize(size);
                letter.setText(Character.toString(c));
                letter.setLocalTranslation(xOffset, letter.getHeight() / 2, 0);
                center.attachChild(letter);

                xOffset += letter.getLineWidth() + spacing;
            }
        }

        center.setLocalTranslation(new Vector3f(-xOffset / 2, 0, 0));
        textNode.attachChild(center);
        return textNode;
    }

    /**
     * Creates a {@code Node} containing text with specified spacing, size, and a single color.
     *
     * @param text    The text to be displayed.
     * @param spacing The spacing between individual characters.
     * @param size    The size of the text.
     * @param color   The color of the text.
     * @return A {@code Node} containing the styled text.
     */
    private Node createTextWithSpacing(String text, float spacing, float size, ColorRGBA color) {
        return createTextWithSpacing(new String[]{text}, spacing, size, new ColorRGBA[]{color});
    }

    /**
     * Calculates the center position of a rectangle given its width, height, and an origin position.
     *
     * @param width  The width of the rectangle.
     * @param height The height of the rectangle.
     * @param pos    The origin position of the rectangle.
     * @return A {@code Vector3f} representing the center position.
     */
    private Vector3f center(float width, float height, Vector3f pos) {
        return new Vector3f(pos.x + width / 2, pos.y + height / 2, 0);
    }

    /**
     * Creates and positions a single-line text at the top of the screen with a specified vertical offset.
     *
     * @param name    The text to be displayed.
     * @param spacing The spacing between individual characters.
     * @param size    The size of the text.
     * @param color   The color of the text.
     * @param top     The vertical offset from the top of the screen.
     * @return A {@code Node} containing the styled text positioned at the top.
     */
    private Node createTopText(String name, float spacing, float size, ColorRGBA color, float top) {
        return createTopText(new String[]{name}, spacing, size, new ColorRGBA[]{color}, top);
    }

    /**
     * Creates and positions multi-line text at the top of the screen with specified vertical offset, spacing, and colors.
     *
     * @param name    An array of strings representing the text to be displayed.
     * @param spacing The spacing between individual characters.
     * @param size    The size of the text.
     * @param color   An array of {@code ColorRGBA} representing the color for each string in {@code name}.
     * @param top     The vertical offset from the top of the screen.
     * @return A {@code Node} containing the styled text positioned at the top.
     */
    private Node createTopText(String[] name, float spacing, float size, ColorRGBA color[], float top) {
        Node text = createTextWithSpacing(name, spacing, size, color);
        text.setLocalTranslation(0, (appSettings.getHeight() / 2f) * 0.8f - top, 0);
        root.attachChild(text);
        return text;
    }

    /**
     * Calculates the center position of a rectangle with negative width offset.
     *
     * @param width  The negative width of the rectangle.
     * @param height The height of the rectangle.
     * @param pos    The origin position of the rectangle.
     * @return A {@code Vector3f} representing the center position.
     */
    private Vector3f centerText(float width, float height, Vector3f pos) {
        return center(-width, height, pos);
    }

    /**
     * Displays a message indicating the active player.
     *
     * @param name  The name of the active player.
     * @param color The color representing the player's team.
     */
    void activePlayer(String name, Color color) {
        createTopText(new String[]{name, " ist dran"}, 10, 90, new ColorRGBA[]{playerColorToColorRGBA(color), ColorRGBA.White}, 0).addControl(new ZoomControl());
    }

    /**
     * Displays a message indicating that the current player is active.
     *
     * @param color The color representing the player's team.
     */
    void ownActive(Color color) {
        createTopText(new String[]{"Du", " bist dran"}, 10, 90, new ColorRGBA[]{playerColorToColorRGBA(color), ColorRGBA.White}, 0).addControl(new ZoomControl());
    }

    /**
     * Displays a dice roll result for a player.
     *
     * @param diceNum The number rolled on the dice.
     * @param name    The name of the player.
     * @param color   The color representing the player's team.
     */
    void diceNum(int diceNum, String name, Color color) {
        createTopText(new String[]{name, " würfelt:"}, 10, 90, new ColorRGBA[]{playerColorToColorRGBA(color), ColorRGBA.White}, 0);

        createTopText(String.valueOf(diceNum), 10, 100, ColorRGBA.White, 100);

    }

    /**
     * Displays a dice roll result with a multiplier for a player.
     *
     * @param diceNum The number rolled on the dice.
     * @param mult    The multiplier applied to the dice result.
     * @param name    The name of the player.
     * @param color   The color representing the player's team.
     */
    void diceNumMult(int diceNum, int mult, String name, Color color) {
        createTopText(new String[]{name, " würfelt:"}, 10, 90, new ColorRGBA[]{playerColorToColorRGBA(color), ColorRGBA.White}, 0);

        createTopText(new String[]{String.valueOf(diceNum), " x" + mult + " = " + (diceNum * mult)}, 20, 100, new ColorRGBA[]{ColorRGBA.White, ColorRGBA.Red}, 100);
    }

    /**
     * Displays the dice roll result for the current player.
     *
     * @param diceNum The number rolled on the dice.
     */
    void ownDice(int diceNum) {
        createTopText(String.valueOf(diceNum), 10, 100, ColorRGBA.White, 0);
    }

    /**
     * Displays the dice roll result with a multiplier for the current player.
     *
     * @param diceNum The number rolled on the dice.
     * @param mult    The multiplier applied to the dice result.
     */
    void ownDiceMult(int diceNum, int mult) {
        createTopText(new String[]{String.valueOf(diceNum), " x" + mult + " = " + (diceNum * mult)}, 20, 100, new ColorRGBA[]{ColorRGBA.White, ColorRGBA.Red}, 0);
    }

    /**
     * Displays a message indicating that a specified player received a bonus card.
     *
     * @param name  The name of the player who received the bonus card.
     * @param color The color representing the player's team.
     */
    void drawCard(String name, Color color) {
        createTopText(new String[]{name, " erhält eine Bonuskarte"}, 7, 70, new ColorRGBA[]{playerColorToColorRGBA(color), ColorRGBA.White}, 0).addControl(new ZoomControl());
    }

    /**
     * Displays a message indicating that the current player received a bonus card.
     *
     * @param color The color representing the player's team.
     */
    void drawCardOwn(Color color) {
        createTopText(new String[]{"Du", "  erhälst eine Bonuskarte"}, 5, 70, new ColorRGBA[]{playerColorToColorRGBA(color), ColorRGBA.White}, 0).addControl(new ZoomControl());
    }

    /**
     * Displays a message indicating that a specified player has completed their turn or action.
     *
     * @param name  The name of the player who finished.
     * @param color The color representing the player's team.
     */
    void finishText(String name, Color color) {
        createTopText(new String[]{name, " ist fertig!"}, 7, 70, new ColorRGBA[]{playerColorToColorRGBA(color), ColorRGBA.White}, 0).addControl(new ZoomControl());
    }

    /**
     * Displays a message indicating that the current player has completed their turn or action.
     *
     * @param color The color representing the player's team.
     */
    void finishTextOwn(Color color) {
        createTopText(new String[]{"Du", " bist fertig!"}, 7, 70, new ColorRGBA[]{playerColorToColorRGBA(color), ColorRGBA.White}, 0).addControl(new ZoomControl());
    }

    /**
     * Converts a player's team color to a corresponding {@code ColorRGBA}.
     *
     * @param color The player's team color.
     * @return The corresponding {@code ColorRGBA}.
     * @throws RuntimeException if the color is invalid.
     */
    private ColorRGBA playerColorToColorRGBA(Color color) {
        return switch (color) {
            case ARMY -> ColorRGBA.Green;
            case NAVY -> ColorRGBA.Blue;
            case CYBER -> ColorRGBA.Orange;
            case AIRFORCE -> ColorRGBA.Black;
            default -> throw new RuntimeException("None is not valid");
        };
    }

    /**
     * Hides all text messages displayed by the handler and resets the ranking counter.
     */
    void hide() {
        ranking = 0;
        root.detachAllChildren();
    }

    /**
     * Displays a ranked dice roll result for a specified player.
     *
     * @param name  The name of the player.
     * @param color The color representing the player's team.
     * @param eye   The dice roll result.
     */
    void rollRankingResult(String name, Color color, int eye) {
        createTopText(new String[]{name, ":  " + eye}, 10, 90, new ColorRGBA[]{playerColorToColorRGBA(color), ColorRGBA.White}, paddingRanked * ranking);
        ranking++;
    }

    /**
     * Displays a ranked dice roll result for the current player.
     *
     * @param color The color representing the player's team.
     * @param eye   The dice roll result.
     */
    void rollRankingResultOwn(Color color, int eye) {
        createTopText(new String[]{"Du", ":  " + eye}, 10, 90, new ColorRGBA[]{playerColorToColorRGBA(color), ColorRGBA.White}, paddingRanked * ranking);
        ranking++;
    }

    /**
     * Displays a message prompting the player to roll the dice.
     */
    void diceNow() {
        createTopText("Klicke  zum  Würfeln", 5, 80, ColorRGBA.White, 0);
    }
}
