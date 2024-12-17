package pp.mdga.client;

import com.jme3.app.SimpleApplication;
import com.jme3.system.AppSettings;
import com.simsilica.lemur.GuiGlobals;
import pp.mdga.client.acoustic.AcousticHandler;
import pp.mdga.client.animation.TimerManager;
import pp.mdga.client.dialog.JoinDialog;
import pp.mdga.client.view.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.prefs.Preferences;

/**
 * Main application class for the MdgaApp game.
 * This class extends {@link SimpleApplication} and manages the game's lifecycle, states, and main components.
 */
public class MdgaApp extends SimpleApplication {

    private static Preferences prefs = Preferences.userNodeForPackage(JoinDialog.class);

    /**
     * Handles acoustic effects and state-based sounds.
     */
    private AcousticHandler acousticHandler;

    /**
     * Synchronizes notifications throughout the application.
     */
    private NotificationSynchronizer notificationSynchronizer;

    /**
     * Manages input events and synchronization.
     */
    private InputSynchronizer inputSynchronizer;

    /**
     * Synchronizes game models.
     */
    private ModelSynchronizer modelSynchronizer;

    /**
     * The currently active view in the application.
     */
    private MdgaView view = null;

    /**
     * The current state of the application.
     */
    private MdgaState state = null;

    /**
     * Scale for rendering images.
     */
    private final float imageScale = prefs.getInt("scale", 1);

    /**
     * The main menu view.
     */
    private MainView mainView;

    /**
     * The lobby view.
     */
    private LobbyView lobbyView;

    /**
     * The game view.
     */
    private GameView gameView;

    /**
     * The ceremony view.
     */
    private CeremonyView ceremonyView;

    /**
     * The client game logic.
     */
    private ClientGameLogic clientGameLogic;

    private ExecutorService executor;

    private ServerConnection networkConnection;

    private final TimerManager timerManager = new TimerManager();


    public static final int DEBUG_MULTIPLIER = 1;

    /**
     * Constructs a new MdgaApp instance.
     * Initializes the network connection and client game logic.
     */
    public MdgaApp() {
        networkConnection = new NetworkSupport(this);
        this.clientGameLogic = new ClientGameLogic(networkConnection);
    }

    /**
     * Main entry point for the application.
     * Configures settings and starts the application.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        AppSettings settings = new AppSettings(true);
        settings.setSamples(128);

        if (prefs.getBoolean("fullscreen", false)) {
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int screenWidth = (int) screenSize.getWidth();
            int screenHeight = (int) screenSize.getHeight();

            settings.setResolution(screenWidth, screenHeight);

            settings.setFullscreen(true);
        } else {
            settings.setWidth(prefs.getInt("width", 1280));
            settings.setHeight(prefs.getInt("height", 720));
        }

        settings.setCenterWindow(true);
        settings.setVSync(false);
        settings.setTitle("MDGA");
        settings.setVSync(true);
        MdgaApp app = new MdgaApp();
        app.setSettings(settings);
        app.setShowSettings(false);
        app.setPauseOnLostFocus(false);
        app.setDisplayStatView(false);

        try {
            if (!System.getProperty("os.name").toLowerCase().contains("mac")) {
                settings.setIcons(new BufferedImage[]{
                    ImageIO.read(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResourceAsStream("Images/icon/icon128.png")))
                });
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        app.start();
    }

    /**
     * Initializes the application by setting up handlers, views, and entering the default state.
     */
    @Override
    public void simpleInitApp() {
        GuiGlobals.initialize(this);

        inputManager.deleteMapping("SIMPLEAPP_Exit");

        flyCam.setEnabled(false);

        acousticHandler = new AcousticHandler(this);
        notificationSynchronizer = new NotificationSynchronizer(this);
        inputSynchronizer = new InputSynchronizer(this);
        modelSynchronizer = new ModelSynchronizer(this);

        mainView = new MainView(this);
        lobbyView = new LobbyView(this);
        gameView = new GameView(this);
        ceremonyView = new CeremonyView(this);

        enter(MdgaState.MAIN);
    }

    /**
     * Updates the application on each frame. Updates the view, acoustic handler, and notifications.
     *
     * @param tpf time per frame, used for smooth updating
     */
    @Override
    public void simpleUpdate(float tpf) {
        view.update(tpf);
        acousticHandler.update();
        notificationSynchronizer.update();
        inputSynchronizer.update(tpf);
        timerManager.update(tpf);
    }

    /**
     * Transitions the application to a new state.
     *
     * @param state the new state to enter
     * @throws RuntimeException if attempting to enter the {@link MdgaState#NONE} state
     */
    public void enter(MdgaState state) {
        if (null != view) {
            view.leave();
        }

        this.state = state;

        switch (state) {
            case MAIN:
                view = mainView;
                clientGameLogic = new ClientGameLogic(networkConnection);
                break;
            case LOBBY:
                view = lobbyView;
                break;
            case GAME:
                view = gameView;
                break;
            case CEREMONY:
                view = ceremonyView;
                break;
            case NONE:
                throw new RuntimeException("Cannot enter state NONE");
        }

        acousticHandler.playState(state);

        view.enter();
    }


    /**
     * Gets the acoustic handler.
     *
     * @return the {@link AcousticHandler} instance
     */
    public AcousticHandler getAcousticHandler() {
        return acousticHandler;
    }

    /**
     * Gets the current state of the application.
     *
     * @return the current {@link MdgaState}
     */
    public MdgaState getState() {
        return state;
    }

    /**
     * Gets the image scaling factor.
     *
     * @return the image scale as a float
     */
    public float getImageScale() {
        return imageScale;
    }

    /**
     * Gets the currently active view.
     *
     * @return the active {@link MdgaView}
     */
    public MdgaView getView() {
        return view;
    }

    /**
     * Gets the model synchronizer.
     *
     * @return the {@link ModelSynchronizer} instance
     */
    public ModelSynchronizer getModelSynchronize() {
        return modelSynchronizer;
    }

    /**
     * Gets the input synchronizer.
     *
     * @return the {@link InputSynchronizer} instance
     */
    public InputSynchronizer getInputSynchronize() {
        return inputSynchronizer;
    }

    /**
     * Gets the notification synchronizer.
     *
     * @return the {@link NotificationSynchronizer} instance
     */
    public NotificationSynchronizer getNotificationSynchronizer() {
        return notificationSynchronizer;
    }

    /**
     * Prepares the app for a new game cycle.
     */
    public void setup() {
    }

    /**
     * Gets the client game logic.
     *
     * @return the {@link ClientGameLogic} instance
     */
    public ClientGameLogic getGameLogic() {
        return clientGameLogic;
    }

    /**
     * Gets the executor service.
     *
     * @return the {@link ExecutorService} instance
     */
    public ExecutorService getExecutor() {
        if (this.executor == null) {
            this.executor = Executors.newCachedThreadPool();
        }

        return this.executor;
    }

    /**
     * Gets the network connection.
     *
     * @return the {@link ServerConnection} instance
     */
    public ServerConnection getNetworkSupport() {
        return networkConnection;
    }

    /**
     * Updates the resolution settings.
     *
     * @param width the new width
     * @param height the new height
     * @param imageFactor the new image factor
     * @param isFullscreen whether the game is in fullscreen mode
     */
    public void updateResolution(int width, int height, float imageFactor, boolean isFullscreen) {
        if (isFullscreen) {
            int baseWidth = 1280;
            int baseHeight = 720;
            float baseAspectRatio = (float) baseWidth / baseHeight;
            float newAspectRatio = (float) width / height;

            float scaleFactor = Math.max((float) width / baseWidth, (float) height / baseHeight);

            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int screenWidth = (int) screenSize.getWidth();
            int screenHeight = (int) screenSize.getHeight();
            settings.setResolution(screenWidth, screenHeight);
            settings.setFullscreen(true);

            prefs.putFloat("scale", scaleFactor);
            prefs.putBoolean("fullscreen", true);
        } else {
            prefs.putInt("width", width);
            prefs.putInt("height", height);
            prefs.putFloat("scale", imageFactor);
            prefs.putBoolean("fullscreen", false);
        }
    }

    /**
     * Restarts the application.
     */
    public static void restartApp() {
        try {
            String javaBin = System.getProperty("java.home") + "/bin/java";
            String classPath = System.getProperty("java.class.path");
            String className = System.getProperty("sun.java.command");

            ProcessBuilder builder = new ProcessBuilder(
                    javaBin, "-cp", classPath, className
            );

            builder.start();

            System.exit(0);
        } catch (Exception e) {
            throw new RuntimeException("restart failed");
        }
    }

    /**
     * Cleans up the application after a game.
     */
    public void afterGameCleanup() {
        MainView main = (MainView) mainView;

        main.getJoinDialog().disconnect();
        if (clientGameLogic.isHost()) {
            main.getHostDialog().shutdownServer();
        }

        ceremonyView.afterGameCleanup();
    }

    /**
     * Gets the game view.
     *
     * @return the {@link GameView} instance
     */
    public GameView getGameView() {
        return gameView;
    }

    /**
     * Gets the timer manager.
     *
     * @return the {@link TimerManager} instance
     */
    public TimerManager getTimerManager() {
        return timerManager;
    }

    /**
     * Gets the ceremony view.
     *
     * @return the {@link CeremonyView} instance
     */
    public CeremonyView getCeremonyView() {
        return ceremonyView;
    }

    @Override
    public void destroy() {
        afterGameCleanup();
        if (executor != null) {
            executor.shutdown();
        }
        super.destroy();
    }
}

