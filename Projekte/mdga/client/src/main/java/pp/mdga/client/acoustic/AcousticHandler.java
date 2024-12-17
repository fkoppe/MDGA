package pp.mdga.client.acoustic;

import com.jme3.system.NanoTimer;
import pp.mdga.client.MdgaApp;
import pp.mdga.client.MdgaState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;
import java.util.prefs.Preferences;

public class AcousticHandler {
    private MdgaApp app;

    private MdgaState state = MdgaState.NONE;

    private boolean playGame = false;
    private ArrayList<MusicAsset> gameTracks = new ArrayList<>();
    private NanoTimer trackTimer = new NanoTimer();

    private boolean fading = false; // Indicates if a fade is in progress
    private NanoTimer fadeTimer = new NanoTimer(); // Timer to track fade progress
    private static final float FADE_DURATION = 2.0f; // Duration for outfade
    private static final float CROSSFADE_DURATION = 1.5f; // Duration for infade
    private GameMusic playing = null; // Currently playing track
    private GameMusic scheduled = null; // Scheduled track to play next
    private GameMusic old = null; // Old track being faded out

    private GameMusic birds;

    private float mainVolume = 0.0f;
    private float musicVolume = 1.0f;
    private float soundVolume = 1.0f;

    private ArrayList<GameSound> sounds = new ArrayList<>();

    private Preferences prefs = Preferences.userNodeForPackage(AcousticHandler.class);

    public AcousticHandler(MdgaApp app) {
        this.app = app;

        mainVolume = prefs.getFloat("mainVolume", 1.0f);
        musicVolume = prefs.getFloat("musicVolume", 1.0f);
        soundVolume = prefs.getFloat("soundVolume", 1.0f);

        birds = new GameMusic(app, MusicAsset.BIRDS, getSoundVolumeTotal(), MusicAsset.BIRDS.getSubVolume(), MusicAsset.BIRDS.getLoop(), 0.0f);
    }

    /**
     * This method updates the acousticHandler and should be called every frame
     */
    public void update() {
        updateVolumeAndTrack();

        if (playGame) {
            updateGameTracks();
        }

        Iterator<GameSound> iterator = sounds.iterator();
        while (iterator.hasNext()) {
            GameSound s = iterator.next();

            s.update(getSoundVolumeTotal());

            if (!s.isPlaying()) {
                iterator.remove();
            }
        }

        birds.update(Math.min(getSoundVolumeTotal(), getMusicVolumeTotal() > 0 ? 0 : 1));
    }

    /**
     * This method instantly plays a sound
     *
     * @param sound the sound to be played
     */
    public void playSound(MdgaSound sound) {
        ArrayList<SoundAssetDelayVolume> assets = new ArrayList<SoundAssetDelayVolume>();
        switch (sound) {
            case LOST:
                assets.add(new SoundAssetDelayVolume(SoundAsset.LOST, 1.0f, 0.0f));
                break;
            case VICTORY:
                assets.add(new SoundAssetDelayVolume(SoundAsset.VICTORY, 1.0f, 0.0f));
                break;
            case BUTTON_PRESSED:
                assets.add(new SoundAssetDelayVolume(SoundAsset.BUTTON_PRESS, 0.7f, 0.0f));
                break;
            case WRONG_INPUT:
                assets.add(new SoundAssetDelayVolume(SoundAsset.ERROR, 1.0f, 0.0f));
                break;
            case UI_CLICK:
                assets.add(new SoundAssetDelayVolume(SoundAsset.UI_CLICK, 0.8f, 0.0f));
                break;
            case START:
                assets.add(new SoundAssetDelayVolume(SoundAsset.START, 0.8f, 0.5f));
                break;
            case THROW:
                assets.add(new SoundAssetDelayVolume(SoundAsset.LAUGHT, 1.0f, 0.2f));
                break;
            case POWERUP:
                assets.add(new SoundAssetDelayVolume(SoundAsset.POWERUP, 1.0f, 0.2f));
                break;
            case SELF_READY:
                assets.add(new SoundAssetDelayVolume(SoundAsset.ROBOT_READY, 1.0f, 0.0f));
                break;
            case OTHER_READY:
                assets.add(new SoundAssetDelayVolume(SoundAsset.UNIT_READY, 1.0f, 0.0f));
                break;
            case OTHER_CONNECTED:
                assets.add(new SoundAssetDelayVolume(SoundAsset.CONNECTED, 1.0f, 0.0f));
                break;
            case NOT_READY:
                assets.add(new SoundAssetDelayVolume(SoundAsset.UI_SOUND, 1.0f, 0.0f));
                break;
            case LEAVE:
                assets.add(new SoundAssetDelayVolume(SoundAsset.UI_SOUND2, 0.6f, 0.0f));
                break;
            case JET:
                assets.add(new SoundAssetDelayVolume(SoundAsset.JET, 1.0f, 0.0f));
                break;
            case EXPLOSION:
                assets.add(new SoundAssetDelayVolume(SoundAsset.EXPLOSION_1, 1.0f, 0f));
                assets.add(new SoundAssetDelayVolume(SoundAsset.EXPLOSION_2, 1.0f, 0f));
                assets.add(new SoundAssetDelayVolume(SoundAsset.THUNDER, 1.0f, 0f));
                break;
            case LOSE:
                assets.add(new SoundAssetDelayVolume(SoundAsset.LOSE, 1.0f, 0.0f));
                break;
            case BONUS:
                assets.add(new SoundAssetDelayVolume(SoundAsset.BONUS, 1.0f, 0.0f));
                break;
            case UI90:
                assets.add(new SoundAssetDelayVolume(SoundAsset.UI90, 1.0f, 0.0f));
                break;
            case MISSILE:
                assets.add(new SoundAssetDelayVolume(SoundAsset.MISSILE, 1.0f, 0.0f));
                break;
            case MATRIX:
                assets.add(new SoundAssetDelayVolume(SoundAsset.MATRIX, 1.0f, 0.0f));
                break;
            case TURRET_ROTATE:
                assets.add(new SoundAssetDelayVolume(SoundAsset.TURRET_ROTATE, 0.7f, 0f));
                break;
            case TANK_SHOOT:
                assets.add(new SoundAssetDelayVolume(SoundAsset.TANK_SHOOT, 0.7f, 0f));
                break;
            case TANK_EXPLOSION:
                assets.add(new SoundAssetDelayVolume(SoundAsset.EXPLOSION_1, 1.0f, 0f));
                break;
            case SHIELD:
                assets.add(new SoundAssetDelayVolume(SoundAsset.SHIELD, 1.0f, 0f));
                break;
            case TURBO:
                assets.add(new SoundAssetDelayVolume(SoundAsset.SPEED, 1.0f, 0.1f));
                assets.add(new SoundAssetDelayVolume(SoundAsset.SPEED, 1.0f, 1.3f));
                break;
            case SWAP:
                assets.add(new SoundAssetDelayVolume(SoundAsset.SWAP, 1.0f, 0f));
                break;
            default:
                break;
        }

        for (SoundAssetDelayVolume sawd : assets) {
            GameSound gameSound = new GameSound(app, sawd.asset(), getSoundVolumeTotal(), sawd.subVolume(), sawd.delay());
            sounds.add(gameSound);
        }
    }

    /**
     * This method fades the played music to fit the state.
     *
     * @param state the state of which the corresponding music should be played to be played
     */
    public void playState(MdgaState state) {
        if (this.state == state) {
            return;
        }
        MusicAsset asset = null;

        birds.pause();

        float pause = 0.0f;

        switch (state) {
            case MAIN:
                playGame = false;
                asset = MusicAsset.MAIN_MENU;
                break;
            case LOBBY:
                playGame = false;
                asset = MusicAsset.LOBBY;
                break;
            case GAME:
                birds.play();
                addGameTracks();
                playGame = true;
                assert (!gameTracks.isEmpty()) : "no more game music available";
                asset = gameTracks.remove(0);
                pause = 2.0f;
                break;
            case CEREMONY:
                playGame = false;
                asset = MusicAsset.CEREMONY;
                break;
            case NONE:
                throw new RuntimeException("no music for state NONE");
        }

        assert (null != asset) : "music sceduling went wrong";

        scheduled = new GameMusic(app, asset, getMusicVolumeTotal(), asset.getSubVolume(), asset.getLoop(), pause);
    }

    /**
     * Performs linear interpolation between two float values.
     *
     * @param start The starting value.
     * @param end   The ending value.
     * @param t     The interpolation factor, typically between 0 and 1.
     * @return The interpolated value between start and end.
     */
    private float lerp(float start, float end, float t) {
        return start + t * (end - start);
    }

    /**
     * Updates the state of audio playback, handling track transitions and volume adjustments.
     * <p>
     * This method ensures smooth transitions between tracks using fade-in and fade-out effects.
     * It also handles cases where no track is playing, starting a scheduled track immediately at full volume.
     * The method prioritizes the latest scheduled track if multiple scheduling occurs quickly.
     * <p>
     * Behavior:
     * 1. If nothing is scheduled and no track is playing, it exits early.
     * 2. If a scheduled track exists and no track is playing, the scheduled track starts immediately at full volume.
     * 3. If a scheduled track exists while a track is playing, it initiates a fade-out for the currently playing track
     * and prepares for the new track to fade in.
     * 4. If a track transition is in progress (fading), it processes the fade-out and fade-in states.
     * If a new track is scheduled during this process, it interrupts the current transition and prioritizes the new track.
     * 5. If no fading is needed and a track is playing, it ensures the track's volume is updated.
     * <p>
     * Special cases:
     * - If no track is playing and a new track is scheduled, it starts the track immediately without fading.
     * - If a new track is scheduled during fading, it resets the transition to prioritize the new track.
     */
    private void updateVolumeAndTrack() {
        if (scheduled == null && !fading && playing == null) {
            // Nothing to do, early exit
            return;
        }

        if (scheduled != null && playing == null && !fading) {
            // No current track, start scheduled track immediately at full volume
            playing = scheduled;
            scheduled = null;
            playing.play();
            playing.update(getMusicVolumeTotal()); // Set volume to full
            return;
        }

        if (scheduled != null && !fading) {
            // Initiate a fade process if a new track is scheduled
            fading = true;
            fadeTimer.reset();
            old = playing; // The currently playing track becomes the old track
            playing = null; // Clear the playing track during the fade process
        }

        if (fading) {
            handleFadeProcess();

            // Handle any interruptions due to newly scheduled tracks
            if (scheduled != null && playing != null && playing != scheduled) {
                // Interrupt the current infade and switch to the new scheduled track
                old = playing; // Treat the currently infading track as the old track
                playing = null; // Reset playing to allow switching
                fadeTimer.reset(); // Restart fade timer for the new track
            }
        } else if (playing != null) {
            // Update volume for the currently playing track
            playing.update(getMusicVolumeTotal());
        } else if (scheduled != null) {
            // If no track is playing and one is scheduled, start it immediately at full volume
            playing = scheduled;
            scheduled = null;
            playing.play();
            playing.update(getMusicVolumeTotal()); // Set volume to full
        }
    }

    /**
     * Manages the fading process during audio track transitions.
     * <p>
     * This method handles the fade-out of the currently playing (old) track, manages any pause between the fade-out
     * and fade-in, and initiates the fade-in for the new track if applicable. It ensures smooth transitions between
     * tracks while maintaining the correct volume adjustments.
     * <p>
     * Behavior:
     * 1. **Outfade:** Gradually decreases the volume of the `old` track over the duration of `FADE_DURATION`.
     * Once the outfade completes, the `old` track is paused and cleared.
     * 2. **Pause Handling:** Waits for a defined pause (if applicable) before initiating the infade for the next track.
     * 3. **Infade:** If a `scheduled` track exists and the outfade and pause are complete, it begins playing
     * the new track (`playing`) and initiates the infade process.
     * <p>
     * Key Details:
     * - The outfade volume adjustment is interpolated linearly from full volume to zero using the `lerp` function.
     * - The pause duration is retrieved from the scheduled track if it is specified.
     * - If a new track is scheduled during the fade process, it is handled by external logic to prioritize transitions.
     * <p>
     * Preconditions:
     * - `fading` is expected to be `true` when this method is called.
     * - The method is invoked as part of the `updateVolumeAndTrack` process.
     */
    private void handleFadeProcess() {
        float time = fadeTimer.getTimeInSeconds();

        // Handle outfade for the old track
        if (old != null && time <= FADE_DURATION) {
            float t = Math.min(time / FADE_DURATION, 1.0f);
            float oldVolume = lerp(1.0f, 0.0f, t);
            old.update(getMusicVolumeTotal() * oldVolume);
        }

        if (old != null && time > FADE_DURATION) {
            // Complete outfade
            old.pause();
            old = null;
        }

        // Handle pause duration before infade
        float pause = (scheduled != null) ? scheduled.getPause() : 0.0f;
        if (time > FADE_DURATION + pause) {
            if (playing == null && scheduled != null) {
                // Begin infade for the new track
                playing = scheduled;
                scheduled = null;
                playing.play(); // Start playing the new track
            }
            handleInfade(time - FADE_DURATION - pause);
        }
    }

    /**
     * Manages the fade-in process for the currently playing track.
     * <p>
     * This method gradually increases the volume of the `playing` track from zero to full volume
     * over the duration of `CROSSFADE_DURATION`. It ensures a smooth transition into the new track.
     * <p>
     * Behavior:
     * 1. If no track is set as `playing`, the method exits early, as there is nothing to fade in.
     * 2. Linearly interpolates the volume of the `playing` track from 0.0 to 1.0 based on the elapsed
     * `infadeTime` and the specified `CROSSFADE_DURATION`.
     * 3. Once the fade-in is complete (when `infadeTime` exceeds `CROSSFADE_DURATION`), the method:
     * - Marks the fade process (`fading`) as complete.
     * - Ensures the `playing` track is updated to its full volume.
     * <p>
     * Key Details:
     * - Uses the `lerp` function to calculate the volume level for the `playing` track during the fade-in.
     * - Ensures the volume is always a value between 0.0 and 1.0.
     * - The `infadeTime` parameter should be relative to the start of the fade-in process.
     * <p>
     * Preconditions:
     * - The `playing` track must be initialized and actively fading in for this method to have an effect.
     * - The method is invoked as part of the `updateVolumeAndTrack` process.
     *
     * @param infadeTime The elapsed time (in seconds) since the fade-in process started.
     */
    private void handleInfade(float infadeTime) {
        if (playing == null) {
            // Nothing to infade
            return;
        }

        // Proceed with the infade for the current playing track
        float t = Math.min(infadeTime / CROSSFADE_DURATION, 1.0f);
        float newVolume = lerp(0.0f, 1.0f, t);
        playing.update(getMusicVolumeTotal() * newVolume);

        if (infadeTime > CROSSFADE_DURATION) {
            // Infade is complete, finalize state
            fading = false;
            playing.update(getMusicVolumeTotal()); // Ensure full volume
        }
    }

    /**
     * Adds a list of game tracks to the gameTracks collection and shuffles them.
     * This method adds predefined game tracks to the track list and shuffles the order.
     */
    private void addGameTracks() {
        Random random = new Random();

        for (int i = 1; i <= 6; i++) {
            gameTracks.add(MusicAsset.valueOf("GAME_" + i));
        }
        Collections.shuffle(gameTracks, random);
    }

    /**
     * Updates the current game tracks. If the currently playing track is nearing its end,
     * a new track will be scheduled to play. If the list of game tracks is empty, it will be refreshed.
     */
    private void updateGameTracks() {
        if (null == playing) {
            return;
        }

        if (playing.nearEnd(10)) {
            if (gameTracks.isEmpty()) {
                addGameTracks();
            }
        }

        if (playing != null && playing.nearEnd(3) && trackTimer.getTimeInSeconds() > 20) {
            trackTimer.reset();

            MusicAsset nextTrack = gameTracks.remove(0);

            scheduled = new GameMusic(app, nextTrack, getMusicVolumeTotal(), nextTrack.getSubVolume(), nextTrack.getLoop(), 0.0f);
        }
    }

    /**
     * Retrieves the main volume level.
     *
     * @return The current main volume level.
     */
    public float getMainVolume() {
        return mainVolume;
    }

    /**
     * Retrieves the music volume level.
     *
     * @return The current music volume level.
     */
    public float getMusicVolume() {
        return musicVolume;
    }

    /**
     * Retrieves the sound volume level.
     *
     * @return The current sound volume level.
     */
    public float getSoundVolume() {
        return soundVolume;
    }

    /**
     * Sets the main volume level.
     *
     * @param mainVolume The desired main volume level.
     */
    public void setMainVolume(float mainVolume) {
        this.mainVolume = mainVolume;
        prefs.putFloat("mainVolume", mainVolume);
    }

    /**
     * Sets the music volume level.
     *
     * @param musicVolume The desired music volume level.
     */
    public void setMusicVolume(float musicVolume) {
        this.musicVolume = musicVolume;
        prefs.putFloat("musicVolume", musicVolume);
    }

    /**
     * Sets the sound volume level.
     *
     * @param soundVolume The desired sound volume level.
     */
    public void setSoundVolume(float soundVolume) {
        this.soundVolume = soundVolume;
        prefs.putFloat("soundVolume", soundVolume);
    }

    /**
     * Calculates the total music volume by multiplying the music volume by the main volume.
     *
     * @return The total music volume.
     */
    float getMusicVolumeTotal() {

        return getMusicVolume() * getMainVolume() / 2;
    }

    /**
     * Calculates the total sound volume by multiplying the sound volume by the main volume.
     *
     * @return The total sound volume.
     */
    float getSoundVolumeTotal() {
        return getSoundVolume() * getMainVolume();
    }
}
