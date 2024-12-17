package pp.mdga.client.acoustic;

/**
 * Enum representing various music assets used in the game.
 * Each constant corresponds to a specific music track, along with its properties such as file path,
 * looping behavior, and relative volume (subVolume).
 * These music assets are used to control the music that plays in different parts of the game, such as menus and in-game music.
 */
enum MusicAsset {
    MAIN_MENU("Spaceship.ogg", true, 1.0f),
    LOBBY("DeadPlanet.ogg", true, 1.0f),
    CEREMONY("80s,Disco,Life.ogg", true, 1.0f),
    GAME_1("NeonRoadTrip.ogg", 0.5f),
    GAME_2("NoPressureTrance.ogg", 0.5f),
    GAME_3("TheSynthRave.ogg", 0.5f),
    GAME_4("LaserParty.ogg", 0.5f),
    GAME_5("RetroNoir.ogg", 0.5f),
    GAME_6("SpaceInvaders.ogg", 0.5f),
    BIRDS("nature-ambience.ogg", true, 1.0f);

    private final String path;
    private final boolean loop;
    private final float subVolume;
    private static final String ROOT = "Music/";

    /**
     * Constructs a new MusicAsset object with the specified name and sub-volume.
     * The track will not loop by default.
     *
     * @param name      The name of the music file.
     * @param subVolume A relative volume that modifies the base volume of the track (typically a percentage).
     */
    MusicAsset(String name, float subVolume) {
        this.path = ROOT + name;
        this.loop = false;
        this.subVolume = subVolume;
    }

    /**
     * Constructs a new MusicAsset object with the specified name, loop flag, and sub-volume.
     *
     * @param name      The name of the music file.
     * @param loop      If true, the track will loop; otherwise, it will play once.
     * @param subVolume A relative volume that modifies the base volume of the track (typically a percentage).
     */
    MusicAsset(String name, boolean loop, float subVolume) {
        this.path = ROOT + name;
        this.loop = loop;
        this.subVolume = subVolume;
    }

    /**
     * Gets the file path of the music track.
     *
     * @return The path to the music file (relative to the music folder).
     */
    public String getPath() {
        return path;
    }

    /**
     * Gets whether the music track should loop.
     *
     * @return true if the track should loop, false otherwise.
     */
    public boolean getLoop() {
        return loop;
    }

    /**
     * Gets the relative volume (subVolume) for the music track.
     *
     * @return The relative volume for the track, typically a value between 0.0 and 1.0.
     */
    public float getSubVolume() {
        return subVolume;
    }
}
