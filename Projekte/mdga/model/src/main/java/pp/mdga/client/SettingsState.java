package pp.mdga.client;

import pp.mdga.client.settingsstate.AudioSettingsState;
import pp.mdga.client.settingsstate.MainSettingsState;
import pp.mdga.client.settingsstate.SettingStates;
import pp.mdga.client.settingsstate.VideoSettingsState;

public class SettingsState extends ClientState {

    /**
     * The current state of the settings
     */
    private SettingStates currentState;

    private final MainSettingsState mainSettingsState = new MainSettingsState(this, logic);
    private final AudioSettingsState audioSettingsState = new AudioSettingsState(this, logic);
    private final VideoSettingsState videoSettingsState = new VideoSettingsState(this, logic);

    /**
     * Creates a new SettingsState
     *
     * @param parent the parent state
     * @param logic  the game logic
     */
    public SettingsState(ClientState parent, ClientGameLogic logic) {
        super(parent, logic);
    }

    /**
     * Enters the current state
     */
    @Override
    public void enter() {
        currentState = mainSettingsState;
    }

    /**
     * Exits the current state
     */
    @Override
    public void exit() {
        currentState.exit();
    }

    /**
     * Changes the current state
     */
    public SettingStates getState() {
        return currentState;
    }

    /**
     * Returns the main settings state
     */
    public MainSettingsState getMainSettingsState() {
        return mainSettingsState;
    }

    /**
     * Returns the audio settings state
     */
    public AudioSettingsState getAudioSettingsState() {
        return audioSettingsState;
    }

    /**
     * Returns the video settings state
     */
    public VideoSettingsState getVideoSettingsState() {
        return videoSettingsState;
    }
}
