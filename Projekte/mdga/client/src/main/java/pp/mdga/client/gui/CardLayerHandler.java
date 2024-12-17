package pp.mdga.client.gui;

import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.renderer.Camera;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture2D;
import pp.mdga.client.Asset;
import pp.mdga.client.MdgaApp;
import pp.mdga.client.animation.SymbolControl;
import pp.mdga.game.BonusCard;

import java.util.*;

/**
 * Handles the card layer in the GUI, including card management and dice control.
 */
public class CardLayerHandler {
    private static final Vector3f START = new Vector3f(-1.8f, -3.5f, 0);
    private static final Vector3f MARGIN = new Vector3f(1.8f, 0, 0);
    private static final float CARDLAYER_CAMERA_ZOOM = 4;

    private final MdgaApp app;
    private final FilterPostProcessor fpp;
    private final Texture2D backTexture;

    private Camera cardLayerCamera;
    private CardLayer cardLayer;
    private DiceControl diceControl;

    private final Map<BonusCard, CardControl> bonusCardControlMap = new HashMap<>();
    private final List<BonusCard> cardOrder = new ArrayList<>();
    private final Map<BonusCard, Integer> bonusCardIntegerMap = new HashMap<>();
    private final Set<CardControl> selectableCards = new HashSet<>();

    private BonusCard cardSelect = null;
    private boolean show = false;

    /**
     * Constructs a CardLayerHandler.
     *
     * @param app the application instance
     * @param backTexture the background texture
     */
    public CardLayerHandler(MdgaApp app, Texture2D backTexture) {
        this.app = app;
        this.fpp = new FilterPostProcessor(app.getAssetManager());
        this.backTexture = backTexture;
    }

    /**
     * Initializes the card layer and dice control.
     */
    public void init() {
        cardLayerCamera = createOverlayCam();
        cardLayer = new CardLayer(fpp, cardLayerCamera, backTexture);
        app.getStateManager().attach(cardLayer);

        diceControl = new DiceControl(app.getAssetManager());
        diceControl.create(new Vector3f(0, 0, 0), 1f, false);
    }

    /**
     * Shuts down the card layer and clears selectable cards.
     */
    public void shutdown() {
        clearSelectableCards();
        if (cardLayer != null) {
            cardLayer.shutdown();
            app.getStateManager().detach(cardLayer);
        }
        cardLayer = null;
    }

    /**
     * Rolls the dice with a specified number and action to perform after rolling.
     *
     * @param rollNum the number to roll (must be between 1 and 6)
     * @param actionAfter the action to perform after rolling
     */
    public void rollDice(int rollNum, Runnable actionAfter) {
        if (!(1 <= rollNum && rollNum <= 6)) throw new RuntimeException("rollNum is not in the range [1,6]");
        diceControl.rollDice(rollNum, actionAfter);
    }

    /**
     * Shows the dice on the card layer.
     */
    public void showDice() {
        if (show) return;
        show = true;
        cardLayer.addSpatial(diceControl.getSpatial());
        diceControl.spin();
    }

    /**
     * Hides the dice from the card layer.
     */
    public void hideDice() {
        show = false;
        diceControl.hide();
    }

    /**
     * Adds a card to the card layer.
     *
     * @param card the card to add
     */
    public void addCard(BonusCard card) {
        if (card == BonusCard.HIDDEN) throw new RuntimeException("Can't add hidden card to GUI");

        if (!bonusCardControlMap.containsKey(card)) {
            cardOrder.add(card);
        }

        int newNum = bonusCardIntegerMap.getOrDefault(card, 0) + 1;
        bonusCardIntegerMap.put(card, newNum);

        updateCard();
    }

    /**
     * Removes a card from the card layer.
     *
     * @param card the card to remove
     */
    public void removeCard(BonusCard card) {
        if (bonusCardControlMap.containsKey(card)) {
            bonusCardIntegerMap.put(card, bonusCardIntegerMap.get(card) - 1);

            if (bonusCardIntegerMap.get(card) <= 0) {
                bonusCardIntegerMap.remove(card);
                cardOrder.remove(card);
            }
            updateCard();
        } else throw new RuntimeException("card is not in bonusCardControlMap");
    }

    /**
     * Clears all selectable cards.
     */
    public void clearSelectableCards() {
        for (CardControl control : selectableCards) {
            control.selectableOff();
        }
        selectableCards.clear();
        cardSelect = null;
    }

    /**
     * Updates the card layer with the current cards.
     */
    private void updateCard() {
        for (BonusCard card : bonusCardControlMap.keySet()) {
            CardControl control = bonusCardControlMap.get(card);
            cardLayer.deleteSpatial(control.getRoot());
        }
        bonusCardControlMap.clear();

        for (int i = 0; i < cardOrder.size(); i++) {
            BonusCard card = cardOrder.get(i);
            CardControl control = createCard(bonusToAsset(card), nextPos(i));
            control.setNumCard(bonusCardIntegerMap.get(card));
            cardLayer.addSpatial(control.getRoot());
            bonusCardControlMap.put(card, control);
        }
    }

    /**
     * Sets the selectable cards.
     *
     * @param select the list of cards to set as selectable
     */
    public void setSelectableCards(List<BonusCard> select) {
        for (BonusCard card : select) {
            selectableCards.add(bonusCardControlMap.get(card));
        }
        for (CardControl control : selectableCards) {
            control.selectableOn();
        }
    }

    /**
     * Selects a card control.
     *
     * @param cardControl the card control to select
     */
    public void selectCard(CardControl cardControl) {
        if (cardControl.isSelected()) {
            cardControl.selectOff();
            cardSelect = null;
        } else {
            for (CardControl control : selectableCards) {
                control.selectOff();
            }
            cardControl.selectOn();
            cardSelect = getKeyByValue(bonusCardControlMap, cardControl);
        }

        app.getModelSynchronize().selectCard(cardSelect);
    }

    /**
     * Gets the card layer camera.
     *
     * @return the card layer camera
     */
    public Camera getCardLayerCamera() {
        return cardLayerCamera;
    }

    /**
     * Adds a shield symbol to the card layer.
     */
    public void shield() {
        SymbolControl control = createSymbol(Asset.shieldSymbol);
        cardLayer.addSpatial(control.getSpatial());
        control.shield();
    }

    /**
     * Adds a swap symbol to the card layer.
     */
    public void swap() {
        SymbolControl control = createSymbol(Asset.swapSymbol);
        cardLayer.addSpatial(control.getSpatial());
        control.swap();
    }

    /**
     * Adds a turbo symbol to the card layer.
     */
    public void turbo() {
        SymbolControl control = createSymbol(Asset.turboSymbol);
        cardLayer.addSpatial(control.getSpatial());
        control.turbo();
    }

    /**
     * Converts a bonus card to its corresponding asset.
     *
     * @param card the bonus card
     * @return the corresponding asset
     */
    private Asset bonusToAsset(BonusCard card) {
        return switch (card) {
            case TURBO -> Asset.turboCard;
            case SHIELD -> Asset.shieldCard;
            case SWAP -> Asset.swapCard;
            case HIDDEN -> throw new RuntimeException("HIDDEN is not allowed in GUI");
        };
    }

    /**
     * Calculates the next position for a card.
     *
     * @param i the index of the card
     * @return the next position vector
     */
    private Vector3f nextPos(int i) {
        return START.add(MARGIN.mult(i));
    }

    /**
     * Creates an overlay camera for the card layer.
     *
     * @return the created overlay camera
     */
    private Camera createOverlayCam() {
        Camera originalCam = app.getCamera();
        Camera overlayCam = new Camera(originalCam.getWidth(), originalCam.getHeight());
        overlayCam.setParallelProjection(true);
        float aspect = (float) originalCam.getWidth() / originalCam.getHeight();
        float size = CARDLAYER_CAMERA_ZOOM;
        overlayCam.setFrustum(-1000, 1000, -aspect * size, aspect * size, size, -size);
        overlayCam.setLocation(new Vector3f(0, 0, 10));
        overlayCam.lookAt(new Vector3f(0, 0, 0), Vector3f.UNIT_Y);
        return overlayCam;
    }

    /**
     * Gets the key associated with a value in a map.
     *
     * @param map the map to search
     * @param value the value to find the key for
     * @param <K> the type of keys in the map
     * @param <V> the type of values in the map
     * @return the key associated with the value, or null if not found
     */
    private <K, V> K getKeyByValue(Map<K, V> map, V value) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * Test method to add sample cards to the card layer.
     */
    public void test() {
        addCard(BonusCard.SHIELD);
        addCard(BonusCard.SHIELD);
        addCard(BonusCard.TURBO);
        addCard(BonusCard.SWAP);
    }

    /**
     * Creates a card control for a given asset and position.
     *
     * @param card the asset representing the card
     * @param pos the position to place the card
     * @return the created card control
     */
    private CardControl createCard(Asset card, Vector3f pos) {
        Node rootCard = new Node("Root Card");
        Spatial spatial = app.getAssetManager().loadModel(card.getModelPath());
        rootCard.attachChild(spatial);
        Material mat = new Material(app.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
        mat.setTexture("DiffuseMap", app.getAssetManager().loadTexture(card.getDiffPath()));
        spatial.setMaterial(mat);
        spatial.setLocalScale(1f);
        rootCard.setLocalTranslation(pos);
        spatial.rotate((float) Math.toRadians(90), (float) Math.toRadians(180), (float) Math.toRadians(180));
        spatial.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        CardControl control = new CardControl(app, fpp, cardLayer.getOverlayCam(), rootCard);
        spatial.addControl(control);
        return control;
    }

    /**
     * Creates a symbol control for a given asset.
     *
     * @param asset the asset representing the symbol
     * @return the created symbol control
     */
    private SymbolControl createSymbol(Asset asset) {
        Spatial spatial = app.getAssetManager().loadModel(asset.getModelPath());
        Material mat = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setTexture("ColorMap", app.getAssetManager().loadTexture(asset.getDiffPath()));
        spatial.setMaterial(mat);
        spatial.setLocalScale(1f);
        spatial.rotate((float) Math.toRadians(90), (float) Math.toRadians(180), (float) Math.toRadians(180));
        SymbolControl control = new SymbolControl();
        spatial.addControl(control);
        return control;
    }

    /**
     * Gets the card layer.
     *
     * @return the card layer
     */
    public CardLayer getCardLayer() {
        return cardLayer;
    }
}
