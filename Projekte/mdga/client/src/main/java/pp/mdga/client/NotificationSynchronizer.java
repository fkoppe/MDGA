package pp.mdga.client;

import com.jme3.system.NanoTimer;
import pp.mdga.client.acoustic.MdgaSound;
import pp.mdga.client.board.BoardHandler;
import pp.mdga.client.gui.GuiHandler;
import pp.mdga.client.view.CeremonyView;
import pp.mdga.client.view.GameView;
import pp.mdga.client.view.LobbyView;
import pp.mdga.game.BonusCard;
import pp.mdga.game.Color;
import pp.mdga.notification.*;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * The NotificationSynchronizer class is responsible for handling and synchronizing notifications
 * received from the game logic and updating the application state accordingly.
 */
public class NotificationSynchronizer {
    private final MdgaApp app;

    private ArrayList<Notification> notifications = new ArrayList<>();

    private NanoTimer timer = new NanoTimer();
    private float delay = 0;

    private static final float STANDARD_DELAY = 2.5f;

    public boolean waitForAnimation = false;

    /**
     * Constructs a NotificationSynchronizer with the specified MdgaApp instance.
     *
     * @param app the MdgaApp instance
     */
    NotificationSynchronizer(MdgaApp app) {
        this.app = app;
    }

    /**
     * Updates the notification synchronizer by processing notifications from the game logic.
     * Handles different types of notifications based on the current application state.
     */
    public void update() {
        while (timer.getTimeInSeconds() >= delay) {
            if (waitForAnimation) {
                return;
            }

            Notification n = app.getGameLogic().getNotification();

            if (n == null) {
                return;
            }

            System.out.println("receive notification:" + n.getClass().getName());

            timer.reset();
            delay = 0;

            if (n instanceof InfoNotification infoNotification) {
                app.getView().showInfo(infoNotification.getMessage(), infoNotification.isError());
                return;
            }

            if (n != null) {
                switch (app.getState()) {
                    case MAIN:
                        handleMain(n);
                        break;
                    case LOBBY:
                        handleLobby(n);
                        break;
                    case GAME:
                        handleGame(n);
                        break;
                    case CEREMONY:
                        handleCeremony(n);
                        break;
                    case NONE:
                        throw new RuntimeException("no notification expected: " + n.getClass().getName());
                }

                if (0 == MdgaApp.DEBUG_MULTIPLIER) {
                    delay = 0;
                }
            }
        }
    }

    /**
     * Handles notifications when the application is in the MAIN state.
     *
     * @param notification the notification to handle
     */
    private void handleMain(Notification notification) {
        if (notification instanceof LobbyDialogNotification) {
            app.enter(MdgaState.LOBBY);
        } else if (notification instanceof StartDialogNotification) {
            //nothing
        } else {
            throw new RuntimeException("notification not expected in main: " + notification.getClass().getName());
        }
    }

    /**
     * Handles notifications when the application is in the LOBBY state.
     *
     * @param notification the notification to handle
     */
    private void handleLobby(Notification notification) {
        LobbyView lobbyView = (LobbyView) app.getView();

        if (notification instanceof TskSelectNotification n) {
            lobbyView.setTaken(n.getColor(), true, n.isSelf(), n.getName());
        } else if (notification instanceof StartDialogNotification) {
            app.afterGameCleanup();
            app.enter(MdgaState.MAIN);
        } else if (notification instanceof TskUnselectNotification n) {
            lobbyView.setTaken(n.getColor(), false, false, null);
        } else if (notification instanceof LobbyReadyNotification lobbyReadyNotification) {
            lobbyView.setReady(lobbyReadyNotification.getColor(), lobbyReadyNotification.isReady());
        } else if (notification instanceof GameNotification n) {
            app.getGameView().setOwnColor(n.getOwnColor());
            app.enter(MdgaState.GAME);
        } else {
            throw new RuntimeException("notification not expected in lobby: " + notification.getClass().getName());
        }
    }

    /**
     * Handles notifications when the application is in the GAME state.
     *
     * @param notification the notification to handle
     */
    private void handleGame(Notification notification) {
        GameView gameView = (GameView) app.getView();
        GuiHandler guiHandler = gameView.getGuiHandler();
        BoardHandler boardHandler = gameView.getBoardHandler();
        ModelSynchronizer modelSynchronizer = app.getModelSynchronize();
        Color ownColor = gameView.getOwnColor();

        if (notification instanceof AcquireCardNotification n) {
            guiHandler.addCardOwn(n.getBonusCard());
            app.getAcousticHandler().playSound(MdgaSound.BONUS);
            delay = STANDARD_DELAY;
        } else if (notification instanceof RankingResponceNotification n) {
            guiHandler.hideText();
            n.getRankingResults().forEach((c, i) -> {
                guiHandler.rollRankingResult(c, i);
            });
            delay = STANDARD_DELAY;
        } else if (notification instanceof ActivePlayerNotification n) {
            guiHandler.hideText();
            boardHandler.hideDice();
            gameView.getGuiHandler().setActivePlayer(n.getColor());
            if (n.getColor() != ownColor) boardHandler.showDice(n.getColor());
            app.getAcousticHandler().playSound(MdgaSound.UI90);
            delay = STANDARD_DELAY;
        } else if (notification instanceof CeremonyNotification ceremonyNotification) {
            CeremonyView ceremonyView = app.getCeremonyView();
            int size = ceremonyNotification.getNames().size();

            if (ceremonyNotification.getPiecesThrown().size() != size ||
                    ceremonyNotification.getPiecesLost().size() != size ||
                    ceremonyNotification.getBonusCardsPlayed().size() != size ||
                    ceremonyNotification.getSixes().size() != size ||
                    ceremonyNotification.getNodesMoved().size() != size ||
                    ceremonyNotification.getBonusNodes().size() != size) {
                throw new IllegalArgumentException("All data lists in CeremonyNotification must have the same size.");
            }

            for (int i = 0; i < size; i++) {
                Color color = ceremonyNotification.getColors().get(i);
                String name = ceremonyNotification.getNames().get(i);
                int v1 = ceremonyNotification.getPiecesThrown().get(i);
                int v2 = ceremonyNotification.getPiecesLost().get(i);
                int v3 = ceremonyNotification.getBonusCardsPlayed().get(i);
                int v4 = ceremonyNotification.getSixes().get(i);
                int v5 = ceremonyNotification.getNodesMoved().get(i);
                int v6 = ceremonyNotification.getBonusNodes().get(i);

                if(i < size - 1) {
                    ceremonyView.addCeremonyParticipant(color, i + 1, name);
                }

                ceremonyView.addStatisticsRow(name, v1, v2, v3, v4, v5, v6);
            }
            app.enter(MdgaState.CEREMONY);
        } else if (notification instanceof DiceNowNotification) {
            guiHandler.hideText();
            guiHandler.showDice();
        } else if (notification instanceof DrawCardNotification n) {
            app.getAcousticHandler().playSound(MdgaSound.BONUS);
            guiHandler.drawCard(n.getColor());
            delay = STANDARD_DELAY;
        } else if (notification instanceof HomeMoveNotification home) {
            boardHandler.movePieceHome(home.getPieceId(), home.getHomeIndex());
            guiHandler.hideText();
            waitForAnimation = true;
        } else if (notification instanceof InterruptNotification notification1) {
            gameView.enterInterrupt(notification1.getColor());
        } else if (notification instanceof MovePieceNotification n) {
            if (n.isMoveStart()) {
                //StartMove
                boardHandler.movePieceStart(n.getPiece(), n.getMoveIndex());
                waitForAnimation = true;
            } else {
                //InfieldMove
                boardHandler.movePiece(n.getPiece(), n.getStartIndex(), n.getMoveIndex());
                waitForAnimation = true;
            }
            guiHandler.hideText();
        } else if (notification instanceof ThrowPieceNotification n) {
            boardHandler.throwPiece(n.getPieceId(), n.getThrowColor());
            waitForAnimation = true;
        } else if (notification instanceof RemoveShieldNotification n) {
            boardHandler.unshieldPiece(n.getPieceUuid());
        } else if (notification instanceof PlayCardNotification n) {
            if (n.getCard() == BonusCard.TURBO) {
                app.getAcousticHandler().playSound(MdgaSound.TURBO);
                guiHandler.turbo();
            } else if (n.getCard() == BonusCard.SHIELD) {
                app.getAcousticHandler().playSound(MdgaSound.SHIELD);
            } else if (n.getCard() == BonusCard.SWAP) {
                app.getAcousticHandler().playSound(MdgaSound.SWAP);
            }
            if (n.getColor() == ownColor) guiHandler.playCardOwn(n.getCard());
            else guiHandler.playCardEnemy(n.getColor(), n.getCard());

            app.getTimerManager().addTask(
                    2.2f * MdgaApp.DEBUG_MULTIPLIER,
                    app.getModelSynchronize()::animationEnd
            );
        } else if (notification instanceof PlayerInGameNotification n) {
            boardHandler.addPlayer(n.getColor(), n.getPiecesList());
            guiHandler.addPlayer(n.getColor(), n.getName());
        } else if (notification instanceof ResumeNotification) {
            gameView.leaveInterrupt();
        } else if (notification instanceof RollDiceNotification n) {
            gameView.getGuiHandler().hideText();
            if (n.getColor() == ownColor) {
                guiHandler.rollDice(n.getEyes(), n.isTurbo() ? n.getMultiplier() : -1);
                waitForAnimation = true;
            } else {
                if (n.isTurbo()) guiHandler.showRolledDiceMult(n.getEyes(), n.getMultiplier(), n.getColor());
                else guiHandler.showRolledDice(n.getEyes(), n.getColor());
            }
        } else if (notification instanceof SelectableCardsNotification n) {
            guiHandler.setSelectableCards(n.getCards());
            gameView.showNoPower();
        } else if (notification instanceof ShieldActiveNotification n) {
            boardHandler.shieldPiece(n.getPieceId());
        } else if (notification instanceof ShieldSuppressedNotification n) {
            boardHandler.suppressShield(n.getPieceId());
        } else if (notification instanceof StartDialogNotification) {
            app.afterGameCleanup();
            app.enter(MdgaState.MAIN);
        } else if (notification instanceof SwapPieceNotification n) {
            boardHandler.swapPieces(n.getFirstPiece(), n.getSecondPiece());
            guiHandler.swap();
        } else if (notification instanceof WaitMoveNotification) {
            //nothing
        } else if (notification instanceof SelectableMoveNotification n) {
            boardHandler.setSelectableMove(n.getPieces(), n.getMoveIndices(), n.getHomeMoves());
            modelSynchronizer.setSwap(false);
        } else if (notification instanceof SelectableSwapNotification n) {
            boardHandler.setSelectableSwap(n.getOwnPieces(), n.getEnemyPieces());
            modelSynchronizer.setSwap(true);
        } else if (notification instanceof SelectableShieldNotification n) {
            boardHandler.setSelectableShield(n.getPieces());
            modelSynchronizer.setSwap(false);
        } else if (notification instanceof TurboActiveNotification) {
            //nothing
        } else if (notification instanceof FinishNotification n) {
            guiHandler.finish(n.getColorFinished());
        } else {
            throw new RuntimeException("notification not expected in game: " + notification.getClass().getName());
        }
    }

    /**
     * Handles notifications when the application is in the CEREMONY state.
     *
     * @param notification the notification to handle
     */
    private void handleCeremony(Notification notification) {
        if (notification instanceof StartDialogNotification) {
            app.afterGameCleanup();
            app.enter(MdgaState.MAIN);
        } else {
            throw new RuntimeException("notification not expected in ceremony: " + notification.getClass().getName());
        }
    }
}
