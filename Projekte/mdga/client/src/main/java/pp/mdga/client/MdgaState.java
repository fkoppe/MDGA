package pp.mdga.client;

/**
 * Enum representing the various states of the MdgaApp application.
 * Each state corresponds to a distinct phase or mode of the application.
 */
public enum MdgaState {

    /**
     * Represents an undefined or uninitialized state.
     * This state should not be entered during normal application execution.
     */
    NONE,

    /**
     * Represents the main menu state.
     * This is typically the first state entered when the application starts.
     */
    MAIN,

    /**
     * Represents the lobby state where players can prepare or wait before starting a game.
     */
    LOBBY,

    /**
     * Represents the main gameplay state where the core game mechanics take place.
     */
    GAME,

    /**
     * Represents the ceremony state, typically used for post-game events or celebrations.
     */
    CEREMONY;
}
