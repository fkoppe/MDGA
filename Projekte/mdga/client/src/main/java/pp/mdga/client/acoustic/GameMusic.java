package pp.mdga.client.acoustic;

import com.jme3.audio.AudioData;
import com.jme3.audio.AudioNode;
import com.jme3.audio.AudioSource;
import pp.mdga.client.MdgaApp;

/**
 * Represents a game music track, including its playback controls and volume settings.
 * This class manages the playback of a music track, allowing for playing, pausing,
 * volume adjustment, and tracking the current status of the music.
 */
class GameMusic {
    private float volume;
    private final float subVolume;
    private final AudioNode music;
    private float pause;

    /**
     * Constructs a new GameMusic object.
     *
     * @param app       The instance of the application, used to access the asset manager.
     * @param asset     The music asset to be played.
     * @param volume    The total volume of the music, adjusted by the main volume.
     * @param subVolume A relative volume that modifies the base music volume, typically a percentage.
     * @param loop      A flag indicating whether the music should loop once it finishes.
     */
    GameMusic(MdgaApp app, MusicAsset asset, float volume, float subVolume, boolean loop, float pause) {
        this.volume = volume;
        this.subVolume = subVolume;
        this.pause = pause;

        music = new AudioNode(app.getAssetManager(), asset.getPath(), AudioData.DataType.Stream);
        music.setPositional(false);
        music.setDirectional(false);
        music.setVolume(volume * subVolume);

        music.setLooping(loop);
    }

    /**
     * Plays the current music track.
     * If the music is already initialized, it starts playback.
     * If the music is not available, no action is performed.
     */
    void play() {
        if (null == music) {
            return;
        }

        music.play();
    }

    /**
     * Pauses the current music track.
     * If the music is not available or is not playing, no action is performed.
     */
    void pause() {
        if (null == music) {
            return;
        }

        music.stop();
    }

    /**
     * Checks if the current music track is playing.
     *
     * @return true if the music is playing, false otherwise.
     */
    boolean isPlaying() {

        return music.getStatus() == AudioSource.Status.Playing;
    }

    /**
     * Checks if the current music track is near the end.
     *
     * @param thresholdSeconds The threshold in seconds. If the remaining time is less than or equal to this value,
     *                         the track is considered near the end.
     * @return true if the track is near its end (within the threshold), false otherwise.
     */
    boolean nearEnd(float thresholdSeconds) {
        if (music == null || !isPlaying()) {
            return false;
        }

        float currentTime = music.getPlaybackTime(); // Current playback time in seconds
        float duration = music.getAudioData().getDuration(); // Total duration in seconds

        if (duration <= 0) {
            return false;
        }

        float remainingTime = duration - currentTime;

        return remainingTime <= thresholdSeconds;
    }

    /**
     * Updates the volume of the music.
     * If the volume has changed, it will adjust the music's volume accordingly.
     *
     * @param newVolume The new total volume for the music.
     */
    void update(float newVolume) {
        if (volume != newVolume) {
            volume = newVolume;
            music.setVolume(volume * subVolume);
        }
    }

    float getPause() {
        return pause;
    }
}
