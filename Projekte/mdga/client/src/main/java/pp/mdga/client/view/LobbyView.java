package pp.mdga.client.view;

import com.jme3.asset.TextureKey;
import com.jme3.light.AmbientLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Texture;
import pp.mdga.client.MdgaApp;
import pp.mdga.client.acoustic.MdgaSound;
import pp.mdga.client.button.ButtonLeft;
import pp.mdga.client.button.ButtonRight;
import pp.mdga.client.button.LobbyButton;
import pp.mdga.game.Color;

/**
 * Represents the lobby view in the game.
 */
public class LobbyView extends MdgaView {
    private Geometry background;

    private ButtonLeft leaveButton;
    private ButtonRight readyButton;
    private ButtonRight startButton;

    private LobbyButton cyberButton;
    private LobbyButton airforceButton;
    private LobbyButton armyButton;
    private LobbyButton navyButton;

    private AmbientLight ambient = new AmbientLight();

    private boolean isReady = false;

    private Color own = null;

    /**
     * Constructs a new LobbyView.
     *
     * @param app the application instance
     */
    public LobbyView(MdgaApp app) {
        super(app);

        leaveButton = new ButtonLeft(app, guiNode, this::leaveLobby, "Verlassen", 1);
        readyButton = new ButtonRight(app, guiNode, this::ready, "Bereit", 1);
        startButton = new ButtonRight(app, guiNode, () -> app.getGameLogic().selectStart(), "Starten", 7);

        cyberButton = new LobbyButton(app, guiNode, rootNode, () -> toggleTsk(Color.CYBER), Color.CYBER);
        airforceButton = new LobbyButton(app, guiNode, rootNode, () -> toggleTsk(Color.AIRFORCE), Color.AIRFORCE);
        armyButton = new LobbyButton(app, guiNode, rootNode, () -> toggleTsk(Color.ARMY), Color.ARMY);
        navyButton = new LobbyButton(app, guiNode, rootNode, () -> toggleTsk(Color.NAVY), Color.NAVY);

        ambient.setColor(new ColorRGBA(0.3f, 0.3f, 0.3f, 1));
    }

    /**
     * Called when entering the lobby view.
     */
    @Override
    public void onEnter() {
        app.getCamera().setParallelProjection(true);
        float aspect = (float) app.getCamera().getWidth() / app.getCamera().getHeight();
        float size = 1.65f;
        app.getCamera().setFrustum(-1000, 1000, -aspect * size, aspect * size, size, -size);

        leaveButton.show();
        readyButton.show();

        if (app.getGameLogic().isHost()) {
            startButton.show();
        }

        cyberButton.show();
        airforceButton.show();
        armyButton.show();
        navyButton.show();

        rootNode.addLight(ambient);

        float screenWidth = app.getCamera().getWidth();
        float screenHeight = app.getCamera().getHeight();

        float aspectRatio = screenWidth / screenHeight;
        float scale = 3.5f;

        float quadWidth = scale * aspectRatio;
        float quadHeight = scale;

        Quad quad = new Quad(quadWidth, quadHeight);
        background = new Geometry("LobbyBackground", quad);

        TextureKey key = new TextureKey("Images/lobby.png", true);
        Texture texture = app.getAssetManager().loadTexture(key);
        Material material = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        material.setTexture("ColorMap", texture);
        background.setMaterial(material);

        background.setLocalTranslation(-quadWidth / 2, -quadHeight / 2, -5);

        rootNode.attachChild(background);
    }

    /**
     * Called when leaving the lobby view.
     */
    @Override
    public void onLeave() {
        leaveButton.hide();
        readyButton.hide();
        startButton.hide();

        airforceButton.hide();
        armyButton.hide();
        navyButton.hide();
        cyberButton.hide();

        rootNode.removeLight(ambient);

        app.getCamera().setParallelProjection(false);

        app.getCamera().setFrustumPerspective(
                45.0f,
                (float) app.getCamera().getWidth() / app.getCamera().getHeight(),
                0.1f,
                1000.0f
        );

        app.getCamera().setLocation(new Vector3f(0, 0, 10));
        app.getCamera().lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);

        airforceButton.setReady(false);
        armyButton.setReady(false);
        navyButton.setReady(false);
        cyberButton.setReady(false);

        airforceButton.setTaken(LobbyButton.Taken.NOT, null);
        armyButton.setTaken(LobbyButton.Taken.NOT, null);
        navyButton.setTaken(LobbyButton.Taken.NOT, null);
        cyberButton.setTaken(LobbyButton.Taken.NOT, null);

        rootNode.detachChild(background);
    }

    /**
     * Called on each frame update.
     *
     * @param tpf time per frame
     */
    @Override
    protected void onUpdate(float tpf) {
        airforceButton.update(tpf);
        armyButton.update(tpf);
        navyButton.update(tpf);
        cyberButton.update(tpf);
    }

    @Override
    protected void onEnterOverlay(Overlay overlay) {
        // No implementation needed
    }

    @Override
    protected void onLeaveOverlay(Overlay overlay) {
        // No implementation needed
    }

    /**
     * Sets the taken status of a color.
     *
     * @param color the color
     * @param isTaken whether the color is taken
     * @param isSelf whether the color is taken by the player
     * @param name the name of the player who took the color
     */
    public void setTaken(Color color, boolean isTaken, boolean isSelf, String name) {
        LobbyButton.Taken taken;

        if (isTaken) {
            if (isSelf) {
                own = color;
                taken = LobbyButton.Taken.SELF;
            } else {
                taken = LobbyButton.Taken.OTHER;
            }
        } else {
            if (isSelf) {
                own = null;
            }

            taken = LobbyButton.Taken.NOT;
        }

        switch (color) {
            case CYBER:
                cyberButton.setTaken(taken, name);
                break;
            case AIRFORCE:
                airforceButton.setTaken(taken, name);
                break;
            case ARMY:
                armyButton.setTaken(taken, name);
                break;
            case NAVY:
                navyButton.setTaken(taken, name);
                break;
        }
    }

    /**
     * Sets the ready status of a color.
     *
     * @param color the color
     * @param isReady whether the color is ready
     */
    public void setReady(Color color, boolean isReady) {
        LobbyButton button = switch (color) {
            case CYBER -> cyberButton;
            case AIRFORCE -> airforceButton;
            case ARMY -> armyButton;
            case NAVY -> navyButton;
            default -> throw new RuntimeException("None is not valid");
        };

        button.setReady(isReady);

        if (button.getTaken() == LobbyButton.Taken.SELF) {
            this.isReady = isReady;
        }

        if (!isReady) {
            app.getAcousticHandler().playSound(MdgaSound.NOT_READY);
        } else {
            if (button.getTaken() != LobbyButton.Taken.SELF) {
                app.getAcousticHandler().playSound(MdgaSound.OTHER_READY);
            }
        }
    }

    /**
     * Toggles the task selection for a color.
     *
     * @param color the color
     */
    private void toggleTsk(Color color) {
        LobbyButton.Taken taken = LobbyButton.Taken.NOT;

        switch (color) {
            case CYBER:
                taken = cyberButton.getTaken();
                break;
            case AIRFORCE:
                taken = airforceButton.getTaken();
                break;
            case ARMY:
                taken = armyButton.getTaken();
                break;
            case NAVY:
                taken = navyButton.getTaken();
                break;
        }

        if (isReady) {
            setReady(own, false);
        }

        switch (taken) {
            case NOT:
                app.getModelSynchronize().selectTsk(color);
                break;
            case SELF:
                app.getModelSynchronize().unselectTsk(color);
                break;
            case OTHER:
                //nothing
                break;
        }
    }

    /**
     * Sets the player as ready.
     */
    public void ready() {
        if (own == null) {
            app.getAcousticHandler().playSound(MdgaSound.WRONG_INPUT);
            return;
        }

        if (!isReady) {
            app.getAcousticHandler().playSound(MdgaSound.SELF_READY);
        }

        app.getModelSynchronize().setReady(!isReady);
    }

    /**
     * Leaves the lobby.
     */
    private void leaveLobby() {
        app.getModelSynchronize().leave();
    }
}
