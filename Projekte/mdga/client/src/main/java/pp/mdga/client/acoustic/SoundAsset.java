package pp.mdga.client.acoustic;

/**
 * Enum representing various sound assets used in the game.
 * Each constant corresponds to a specific sound effect used throughout the game.
 * These sounds are associated with various events and actions, such as dice rolls,
 * turn changes, piece movements, and game outcomes.
 */
enum SoundAsset {
    DICE_ROLL(""),
    TURN_START(""),
    TURN_END(""),
    PIECE_END(""),
    PIECE_MOVE(""),
    PIECE_LOST(""),
    SELECT(""),
    DESELECT(""),
    HURRY(""),
    VICTORY("LevelUp2.wav"),
    LOST("GameOver.wav"),
    BUTTON_PRESS("menu_button.ogg"),
    ERROR("buzzer.wav"),
    UI_SOUND("ui_sound.ogg"),
    UI_SOUND2("ui_swoosch.wav"),
    UI_CLICK("uiclick.ogg"),
    START("gamestart.ogg"),
    LAUGHT("laughter.wav"),
    POWERUP("powerup.wav"),
    ROBOT_READY("robotReady.wav"),
    UNIT_READY("unitReady.wav"),
    JET("jet-overhead.wav"),
    EXPLOSION_1("exp.ogg"),
    EXPLOSION_2("exp2.ogg"),
    THUNDER("thunder.ogg"),
    UI90("ui90.ogg"),
    BONUS("bonus.ogg"),
    LOSE("lose.ogg"),
    MISSILE("missile.ogg"),
    MATRIX("matrix.wav"),
    CONNECTED("connected.wav"),
    TURRET_ROTATE("turret_rotate.ogg"),
    TANK_SHOOT("tank_shoot.ogg"),
    SHIELD("shield.ogg"),
    SPEED("speed.ogg"),
    SWAP("swap.ogg"),
    ;


    private final String path;

    /**
     * Constructs a new SoundAsset object with the specified name.
     *
     * @param name The name of the sound file.
     */
    SoundAsset(String name) {
        this.path = "Sounds/" + name;
    }

    /**
     * Gets the file path of the sound effect.
     *
     * @return The path to the sound file (relative to the sound folder).
     */
    public String getPath() {
        return path;
    }
}
