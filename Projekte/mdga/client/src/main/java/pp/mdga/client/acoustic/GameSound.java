package pp.mdga.client.acoustic;

import com.jme3.audio.AudioData;
import com.jme3.audio.AudioNode;
import com.jme3.audio.AudioSource;
import com.jme3.system.NanoTimer;
import pp.mdga.client.MdgaApp;

/**
 * Represents a game sound effect, with control over playback, volume, and timing.
 * This class manages the playback of a sound effect, including starting playback after a delay,
 * adjusting volume, and tracking whether the sound has finished playing.
 */
class GameSound {
    private float volume;
    final private float subVolume;

    private final AudioNode sound;

    private boolean playing = false;
    private boolean finished = false;

    private float delay = 0.0f;
    private NanoTimer timer = null;

    /**
     * Constructs a new GameSound object.
     *
     * @param app       The instance of the application, used to access the asset manager.
     * @param asset     The sound asset to be played.
     * @param volume    The total volume of the sound, adjusted by the main volume.
     * @param subVolume A relative volume that modifies the base sound volume, typically a percentage.
     * @param delay     The delay before the sound starts playing, in seconds.
     */
    GameSound(MdgaApp app, SoundAsset asset, float volume, float subVolume, float delay) {
        this.volume = volume;
        this.subVolume = subVolume;
        this.delay = delay;

        sound = new AudioNode(app.getAssetManager(), asset.getPath(), AudioData.DataType.Buffer);
        sound.setPositional(false);
        sound.setDirectional(false);
        sound.setLooping(false);
        sound.setVolume(volume * subVolume);

        timer = new NanoTimer();
    }

    /**
     * Checks if the sound is currently playing.
     *
     * @return true if the sound is playing, false otherwise.
     */
    boolean isPlaying() {
        return !finished;
    }

    /**
     * Updates the sound playback, adjusting the volume if necessary, and starting
     * the sound after the specified delay.
     *
     * @param newVolume The new total volume for the sound.
     */
    void update(float newVolume) {
        if (!playing && timer.getTimeInSeconds() > delay) {
            sound.play();
            playing = true;
        }

        if (!playing) {
            return;
        }

        if (volume != newVolume) {
            volume = newVolume;
            sound.setVolume(volume * subVolume);
        }

        if (sound != null && sound.getStatus() == AudioSource.Status.Playing) {
            finished = true;
        }
    }
}
