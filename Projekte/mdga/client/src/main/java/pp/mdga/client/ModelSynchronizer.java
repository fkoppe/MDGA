package pp.mdga.client;

import pp.mdga.client.view.GameView;
import pp.mdga.game.BonusCard;
import pp.mdga.game.Color;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The ModelSynchronizer class is responsible for synchronizing the model state with the view and game logic.
 */
public class ModelSynchronizer {
    private static final Logger LOGGER = Logger.getLogger(ModelSynchronizer.class.getName());
    private MdgaApp app;

    private UUID a;
    private UUID b;
    private BonusCard card;
    private boolean swap;

    /**
     * Constructor for ModelSynchronizer.
     *
     * @param app the MdgaApp instance
     */
    ModelSynchronizer(MdgaApp app) {
        this.app = app;
        swap = false;
    }

    /**
     * Handles the end of an animation.
     */
    public void animationEnd() {
        if (app.getNotificationSynchronizer().waitForAnimation) {
            app.getNotificationSynchronizer().waitForAnimation = false;
        } else {
            app.getGameLogic().selectAnimationEnd();
        }
    }

    /**
     * Selects a piece or swap based on the current state.
     *
     * @param a the first UUID
     * @param b the second UUID
     */
    public void select(UUID a, UUID b) {
        if (swap) selectSwap(a, b);
        else selectPiece(a);
    }

    /**
     * Selects a swap between two pieces.
     *
     * @param a the first UUID
     * @param b the second UUID
     */
    public void selectSwap(UUID a, UUID b) {
        LOGGER.log(Level.INFO, "selectPiece");
        this.a = a;
        this.b = b;

        GameView gameView = (GameView) app.getView();
        if (a != null && b != null) {
            gameView.needConfirm();
        } else {
            gameView.noConfirm();
        }
    }

    /**
     * Selects a single piece.
     *
     * @param piece the UUID of the piece
     */
    public void selectPiece(UUID piece) {
        LOGGER.log(Level.INFO, "selectPiece");

        this.a = piece;

        GameView gameView = (GameView) app.getView();
        if (piece != null) {
            gameView.needConfirm();
        } else {
            gameView.noConfirm();
        }
    }

    /**
     * Selects a bonus card.
     *
     * @param card the BonusCard instance
     */
    public void selectCard(BonusCard card) {
        LOGGER.log(Level.INFO, "selectCard");

        this.card = card;

        GameView gameView = (GameView) app.getView();

        if (card != null) {
            gameView.needConfirm();
        } else {
            gameView.showNoPower();
        }
    }

    /**
     * Confirms the current selection.
     */
    public void confirm() {
        LOGGER.log(Level.INFO, "confirm");

        GameView gameView = (GameView) app.getView();

        gameView.getGuiHandler().hideText();

        if (a != null && b != null) {
            app.getGameLogic().selectPiece(a);
            app.getGameLogic().selectPiece(b);
            gameView.getBoardHandler().clearSelectable();
        } else if (a != null) {
            app.getGameLogic().selectPiece(a);
            gameView.getBoardHandler().clearSelectable();
        } else {
            app.getGameLogic().selectCard(card);
            gameView.getGuiHandler().clearSelectableCards();
        }

        a = null;
        b = null;
        card = null;

        gameView.noConfirm();
        gameView.hideNoPower();
    }

    /**
     * Selects a TSK color.
     *
     * @param color the Color instance
     */
    public void selectTsk(Color color) {
        app.getGameLogic().selectTsk(color);
    }

    /**
     * Unselects a TSK color.
     *
     * @param color the Color instance
     */
    public void unselectTsk(Color color) {
        app.getGameLogic().deselectTSK(color);
    }

    /**
     * Handles the event of rolling dice.
     */
    public void rolledDice() {
        app.getGameLogic().selectDice();
    }

    /**
     * Sets the player's name.
     *
     * @param name the player's name
     */
    public void setName(String name) {
        LOGGER.log(Level.INFO, "setName: {0}", name);
        app.getGameLogic().selectName(name);
    }

    /**
     * Sets the player's ready status.
     *
     * @param ready the ready status
     */
    public void setReady(boolean ready) {
        app.getGameLogic().selectReady(ready);
    }

    /**
     * Sets the host port.
     *
     * @param port the host port
     */
    public void setHost(int port) {
        app.getGameLogic().selectJoin("");
    }

    /**
     * Sets the join IP and port.
     *
     * @param ip the IP address
     * @param port the port number
     */
    public void setJoin(String ip, int port) {
        app.getGameLogic().selectJoin(ip);
    }

    /**
     * Handles the event of leaving the game.
     */
    public void leave() {
        app.getGameLogic().selectLeave();
    }

    /**
     * Enters a specific game state.
     *
     * @param state the MdgaState instance
     */
    public void enter(MdgaState state) {
        LOGGER.log(Level.INFO, "enter: {0}", state);
        //app.enter(state);
    }

    /**
     * Proceeds to the next game state.
     */
    public void next() {
        app.getGameLogic().selectNext();
    }

    /**
     * Sets the swap state.
     *
     * @param swap the swap state
     */
    public void setSwap(boolean swap) {
        this.swap = swap;
    }

    /**
     * Forces an action.
     */
    public void force() {
        // Implementation needed
    }
}
