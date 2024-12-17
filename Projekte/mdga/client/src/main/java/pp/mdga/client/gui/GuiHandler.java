package pp.mdga.client.gui;

import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.texture.FrameBuffer;
import com.jme3.texture.Image;
import com.jme3.texture.Texture2D;
import pp.mdga.client.MdgaApp;
import pp.mdga.game.BonusCard;
import pp.mdga.game.Color;

import java.util.List;

/**
 * Handles the GUI elements and interactions for the game.
 */
public class GuiHandler {
    private final MdgaApp app;
    private final CardLayerHandler cardLayerHandler;
    private final PlayerNameHandler playerNameHandler;
    private final ActionTextHandler actionTextHandler;
    private Color ownColor;

    private FrameBuffer backFrameBuffer;

    /**
     * Constructs a new GuiHandler.
     *
     * @param app the application instance
     * @param guiNode the GUI node
     */
    public GuiHandler(MdgaApp app, Node guiNode) {
        this.app = app;
        this.ownColor = ownColor;

        backFrameBuffer = new FrameBuffer(app.getCamera().getWidth(), app.getCamera().getHeight(), 1);
        Texture2D backTexture = new Texture2D(app.getCamera().getWidth(), app.getCamera().getHeight(), Image.Format.RGBA8);
        backFrameBuffer.setDepthTarget(FrameBuffer.FrameBufferTarget.newTarget(Image.Format.Depth));
        backFrameBuffer.addColorTarget(FrameBuffer.FrameBufferTarget.newTarget(backTexture));

        cardLayerHandler = new CardLayerHandler(app, backTexture);
        playerNameHandler = new PlayerNameHandler(guiNode, app.getAssetManager(), app.getContext().getSettings());
        actionTextHandler = new ActionTextHandler(guiNode, app.getAssetManager(), app.getContext().getSettings());
    }

    /**
     * Initializes the GUI handler with the player's color.
     *
     * @param ownColor the player's color
     */
    public void init(Color ownColor) {
        cardLayerHandler.init();
        playerNameHandler.show();
        this.ownColor = ownColor;
        app.getViewPort().setOutputFrameBuffer(backFrameBuffer);
    }

    /**
     * Shuts down the GUI handler.
     */
    public void shutdown() {
        cardLayerHandler.shutdown();
        app.getViewPort().setOutputFrameBuffer(null);
    }

    /**
     * Rolls the dice and handles the result.
     *
     * @param rollNum the number rolled
     * @param mult the multiplier
     */
    public void rollDice(int rollNum, int mult) {
        cardLayerHandler.rollDice(rollNum, () -> {
            if (mult == -1) actionTextHandler.ownDice(rollNum);
            else actionTextHandler.ownDiceMult(rollNum, mult);
            hideDice();
            app.getModelSynchronize().animationEnd();
            app.getModelSynchronize().animationEnd();
        });
    }

    /**
     * Shows the rolled dice with a multiplier for a specific player.
     *
     * @param rollNum the number rolled
     * @param mult the multiplier
     * @param color the player's color
     */
    public void showRolledDiceMult(int rollNum, int mult, Color color) {
        String name = playerNameHandler.getName(color);
        if (mult == -1) actionTextHandler.diceNum(rollNum, name, color);
        else actionTextHandler.diceNumMult(rollNum, mult, name, color);
    }

    /**
     * Shows the rolled dice for a specific player.
     *
     * @param rollNum the number rolled
     * @param color the player's color
     */
    public void showRolledDice(int rollNum, Color color) {
        actionTextHandler.diceNum(rollNum, playerNameHandler.getName(color), color);
    }

    /**
     * Displays the dice on the screen.
     */
    public void showDice() {
        cardLayerHandler.showDice();
        actionTextHandler.diceNow();
    }

    /**
     * Hides the dice from the screen.
     */
    public void hideDice() {
        cardLayerHandler.hideDice();
    }

    /**
     * Adds a card to the player's hand.
     *
     * @param card the card to add
     */
    public void addCardOwn(BonusCard card) {
        cardLayerHandler.addCard(card);
        playerNameHandler.addCard(ownColor);
        actionTextHandler.drawCardOwn(ownColor);
    }

    /**
     * Plays a card from the player's hand.
     *
     * @param card the card to play
     */
    public void playCardOwn(BonusCard card) {
        getEffectByCard(card);
        cardLayerHandler.removeCard(card);
        playerNameHandler.removeCard(ownColor);
    }

    /**
     * Plays a card from an enemy player's hand.
     *
     * @param color the enemy player's color
     * @param card the card to play
     */
    public void playCardEnemy(Color color, BonusCard card) {
        getEffectByCard(card);
        playerNameHandler.removeCard(color);
    }

    /**
     * Gets the effect of a card and applies it.
     *
     * @param bonus the card to get the effect from
     */
    private void getEffectByCard(BonusCard bonus) {
        switch (bonus) {
            case SWAP -> swap();
            case TURBO -> turbo();
            case SHIELD -> shield();
            default -> throw new RuntimeException("invalid card");
        }
    }

    /**
     * Clears all selectable cards.
     */
    public void clearSelectableCards() {
        cardLayerHandler.clearSelectableCards();
    }

    /**
     * Sets the selectable cards.
     *
     * @param select the list of selectable cards
     */
    public void setSelectableCards(List<BonusCard> select) {
        cardLayerHandler.setSelectableCards(select);
    }

    /**
     * Selects a card.
     *
     * @param cardControl the card control to select
     */
    public void selectCard(CardControl cardControl) {
        cardLayerHandler.selectCard(cardControl);
    }

    /**
     * Gets the camera for the card layer.
     *
     * @return the card layer camera
     */
    public Camera getCardLayerCamera() {
        return cardLayerHandler.getCardLayerCamera();
    }

    /**
     * Gets the root node for the card layer.
     *
     * @return the card layer root node
     */
    public Node getCardLayerRootNode() {
        return cardLayerHandler.getCardLayer().getRootNode();
    }

    /**
     * Adds a player to the game.
     *
     * @param color the player's color
     * @param name the player's name
     */
    public void addPlayer(Color color, String name) {
        playerNameHandler.addPlayer(color, name, color == ownColor);
    }

    /**
     * Sets the active player.
     *
     * @param color the active player's color
     */
    public void setActivePlayer(Color color) {
        playerNameHandler.setActivePlayer(color);

        if (ownColor == color) actionTextHandler.ownActive(color);
        else actionTextHandler.activePlayer(playerNameHandler.getName(color), color);
    }

    /**
     * Applies the shield effect.
     */
    public void shield() {
        cardLayerHandler.shield();
    }

    /**
     * Applies the swap effect.
     */
    public void swap() {
        cardLayerHandler.swap();
    }

    /**
     * Applies the turbo effect.
     */
    public void turbo() {
        cardLayerHandler.turbo();
    }

    /**
     * Hides the action text.
     */
    public void hideText() {
        actionTextHandler.hide();
    }

    /**
     * Draws a card for an enemy player.
     *
     * @param color the enemy player's color
     */
    public void drawCard(Color color) {
        //Color != ownColor
        actionTextHandler.drawCard(playerNameHandler.getName(color), color);
        playerNameHandler.addCard(color);
    }

    /**
     * Displays the finish text for a player.
     *
     * @param color the player's color
     */
    public void finish(Color color) {
        if (ownColor == color) actionTextHandler.finishTextOwn(color);
        else actionTextHandler.finishText(playerNameHandler.getName(color), color);
    }

    /**
     * Displays the ranking result for a player.
     *
     * @param color the player's color
     * @param eye the ranking result
     */
    public void rollRankingResult(Color color, int eye) {
        if (ownColor == color) actionTextHandler.rollRankingResultOwn(color, eye);
        else actionTextHandler.rollRankingResult(playerNameHandler.getName(color), color, eye);
    }
}
