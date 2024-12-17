package pp.mdga.client;

import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.*;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.scene.control.AbstractControl;
import pp.mdga.client.board.OutlineOEControl;
import pp.mdga.client.gui.CardControl;
import pp.mdga.client.gui.DiceControl;
import pp.mdga.client.view.GameView;
import pp.mdga.game.Color;

import java.util.List;
import java.util.UUID;

public class InputSynchronizer {

    private MdgaApp app;
    private InputManager inputManager;

    protected boolean rightMousePressed = false;
    private float rotationAngle = 180f;
    private int scrollValue = 0;
    private CardControl hoverCard;
    private OutlineOEControl hoverPiece;

    private boolean clickAllowed = true;

    private boolean isRotateLeft = false;
    private boolean isRotateRight = false;

    /**
     * Constructor initializes the InputSynchronizer with the application context.
     * Sets up input mappings and listeners for user interactions.
     *
     * @param app The application instance
     */
    InputSynchronizer(MdgaApp app) {
        this.app = app;

        this.inputManager = app.getInputManager();
        hoverCard = null;
        hoverPiece = null;
        setupInput();
    }

    /**
     * Updates the rotation angle based on user input.
     *
     * @param tpf The time per frame.
     */
    public void update(float tpf) {
        if (isRotateLeft && isRotateRight) {
            return;
        }
        if (isRotateLeft) {
            rotationAngle += 180 * tpf;
        }
        if (isRotateRight) {
            rotationAngle -= 180 * tpf;
        }
    }

    /**
     * Configures input mappings for various actions and binds them to listeners.
     */
    private void setupInput() {
        inputManager.addMapping("Settings", new KeyTrigger(KeyInput.KEY_ESCAPE));
        inputManager.addMapping("Forward", new KeyTrigger(KeyInput.KEY_RETURN));

        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_Q));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_E));

        inputManager.addMapping("RotateRightMouse", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
        inputManager.addMapping("MouseLeft", new MouseAxisTrigger(MouseInput.AXIS_X, false)); // Left movement
        inputManager.addMapping("MouseRight", new MouseAxisTrigger(MouseInput.AXIS_X, true)); // Right movement
        inputManager.addMapping("MouseScrollUp", new MouseAxisTrigger(MouseInput.AXIS_WHEEL, false)); // Scroll up
        inputManager.addMapping("MouseScrollDown", new MouseAxisTrigger(MouseInput.AXIS_WHEEL, true)); // Scroll down
        inputManager.addMapping("Test", new KeyTrigger(KeyInput.KEY_J));
        inputManager.addMapping("Click", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));


        inputManager.addListener(actionListener, "Settings", "Forward", "RotateRightMouse", "Click", "Left", "Right", "Test");
        inputManager.addListener(analogListener, "MouseLeft", "MouseRight", "MouseScrollUp", "MouseScrollDown");
    }

    /**
     * Handles action-based input events such as key presses and mouse clicks.
     */
    private final ActionListener actionListener = new ActionListener() {
        @Override
        public void onAction(String name, boolean isPressed, float tpf) {
            if (name.equals("Settings") && isPressed) {
                app.getView().pressEscape();
            }
            if (name.equals("Forward") && isPressed) {
                app.getView().pressForward();
            }
            if (name.equals("RotateRightMouse")) {
                rightMousePressed = isPressed;
            }
            if (name.equals("Click") && isPressed) {
                if (!clickAllowed) {
                    return;
                }

                if (app.getView() instanceof GameView gameView) {
                    DiceControl diceSelect = checkHover(gameView.getGuiHandler().getCardLayerCamera(), gameView.getGuiHandler().getCardLayerRootNode(), DiceControl.class);
                    CardControl cardLayerSelect = checkHover(gameView.getGuiHandler().getCardLayerCamera(), gameView.getGuiHandler().getCardLayerRootNode(), CardControl.class);
                    OutlineOEControl boardSelect = checkHover(app.getCamera(), app.getRootNode(), OutlineOEControl.class);

                    if (diceSelect != null) {
                        app.getModelSynchronize().rolledDice();
                    } else if (cardLayerSelect != null) {
                        if (cardLayerSelect.isSelectable()) gameView.getGuiHandler().selectCard(cardLayerSelect);
                    } else if (boardSelect != null) {
                        if (boardSelect.isSelectable()) gameView.getBoardHandler().pieceSelect(boardSelect);
                    }
                }


            }
            if (name.equals("Left")) {
                isRotateLeft = !isRotateLeft;
            }
            if (name.equals("Right")) {
                isRotateRight = !isRotateRight;
            }
            if (name.equals("Test") && isPressed) {
                if (app.getView() instanceof GameView gameView) {
                    //Test Code
                }
            }
        }
    };

    /**
     * Handles analog-based input events such as mouse movement and scrolling.
     */
    private final AnalogListener analogListener = new AnalogListener() {
        @Override
        public void onAnalog(String name, float value, float tpf) {
            if (name.equals("MouseLeft") && rightMousePressed) {
                rotationAngle -= value * 360f;
            } else if (name.equals("MouseRight") && rightMousePressed) {
                rotationAngle += value * 360f;
            } else if (name.equals("MouseScrollUp")) {
                scrollValue = Math.max(1, scrollValue - 5);
            } else if (name.equals("MouseScrollDown")) {
                scrollValue = Math.min(100, scrollValue + 5);
            } else if (name.equals("MouseLeft") || name.equals("MouseRight") || name.equals("MouseVertical")) {
                hoverPiece();
                hoverCard();
            }
        }
    };

    /**
     * Detects the hovered piece and updates its hover state.
     */
    private <T extends AbstractControl> T checkHover(Camera cam, Node root, Class<T> controlType) {
        if (cam == null || root == null || controlType == null) return null;
        CollisionResults results = new CollisionResults();
        Ray ray = new Ray(cam.getLocation(), getMousePos(cam).subtract(cam.getLocation()).normalize());
        root.collideWith(ray, results);
        for (CollisionResult collisionResult : results) {
            if (collisionResult.getGeometry().getControl(controlType) != null)
                return collisionResult.getGeometry().getControl(controlType);
        }
        return null;
    }

    /**
     * Detects the hovered card and updates its hover state.
     */
    private <T extends AbstractControl> T checkHoverOrtho(Camera cam, Node root, Class<T> controlType) {
        if (cam == null || root == null || controlType == null) return null;
        CollisionResults results = new CollisionResults();
        Vector3f mousePos = getMousePos(cam);
        mousePos.setZ(cam.getLocation().getZ());
        Ray ray = new Ray(mousePos, getMousePos(cam).subtract(mousePos).normalize());
        root.collideWith(ray, results);
        if (results.size() > 0) {
            for (CollisionResult res : results) {
                T control = res.getGeometry().getControl(controlType);
                if (control != null) return control;
            }
        }
        return null;
    }

    /**
     * Handles the hover state for a piece in the game.
     * Checks if a piece is being hovered over, updates the hover state, and triggers hover effects.
     */
    private void hoverPiece() {
        if (app.getView() instanceof GameView gameView) {
            OutlineOEControl control = checkPiece();
            if (control != null) {
                if (control != hoverPiece) {
                    pieceOff(gameView);
                    hoverPiece = control;
                    if(hoverPiece.isHoverable()) gameView.getBoardHandler().hoverOn(hoverPiece);
                }
            } else {
                pieceOff(gameView);
            }
        }
    }

    /**
     * Handles the hover state for a card in the game.
     * Checks if a card is being hovered over, updates the hover state, and triggers hover effects.
     */
    private void hoverCard() {
        if (app.getView() instanceof GameView gameView) {
            CardControl control = checkCard(gameView);
            if (control != null) {
                if (control != hoverCard) {
                    cardOff();
                    hoverCard = control;
                    hoverCard.hoverOn();
                }
            } else {
                cardOff();
            }
        }
    }

    /**
     * Checks if a piece is being hovered over in the 3D game world.
     *
     * @return The PieceControl of the hovered piece, or null if no piece is hovered.
     */
    private OutlineOEControl checkPiece() {
        return checkHover(app.getCamera(), app.getRootNode(), OutlineOEControl.class);
    }

    /**
     * Checks if a card is being hovered over in the 2D card layer.
     *
     * @param gameView The current game view.
     * @return The CardControl of the hovered card, or null if no card is hovered.
     */
    private CardControl checkCard(GameView gameView) {
        return checkHoverOrtho(
                gameView.getGuiHandler().getCardLayerCamera(),
                gameView.getGuiHandler().getCardLayerRootNode(),
                CardControl.class
        );
    }

    /**
     * Disables the hover effect on the currently hovered piece, if any.
     */
    private void pieceOff(GameView gameView) {
        if (hoverPiece != null) {
            if(hoverPiece.isHoverable()) gameView.getBoardHandler().hoverOff(hoverPiece);
        }
        hoverPiece = null;
    }

    /**
     * Disables the hover effect on the currently hovered card, if any.
     */
    private void cardOff() {
        if (hoverCard != null) hoverCard.hoverOff();
        hoverCard = null;
    }

    /**
     * Retrieves the current mouse position in the 3D world using the specified camera.
     *
     * @param cam The camera used for determining the mouse position.
     * @return A Vector3f representing the mouse position in the 3D world.
     */
    private Vector3f getMousePos(Camera cam) {
        Vector2f mousePositionScreen = inputManager.getCursorPosition();
        Vector3f world = cam.getWorldCoordinates(mousePositionScreen, 0);
        if (cam.isParallelProjection()) world.setZ(0);
        return world;
    }

    /**
     * Gets the current rotation angle of the game element.
     *
     * @return The rotation angle in degrees, normalized to 360 degrees.
     */
    public float getRotation() {
        return (rotationAngle / 2) % 360;
    }

    public void setRotation(float rotationAngle) {
        this.rotationAngle = rotationAngle;
    }

    /**
     * Gets the current scroll value.
     *
     * @return The scroll value as an integer.
     */
    public int getScroll() {
        return scrollValue;
    }

    public void setClickAllowed(boolean allowed) {
        clickAllowed = allowed;
    }

    public boolean isClickAllowed() {
        return clickAllowed;
    }
}
