package pp.mdga;

import java.util.ResourceBundle;

/**
 * Provides access to the resource bundle of the game.
 *
 * @see #BUNDLE
 */
public class Resources {
    /**
     * Create Resources constants.
     */
    public static final int MAX_PLAYERS = 4;
    public static final int MAX_PIECES = 4;
    public static final int MAX_EYES = 6;

    /**
     * The resource bundle for the MDGA game.
     */
    public static final ResourceBundle BUNDLE = ResourceBundle.getBundle("mdga"); //NON-NLS

    /**
     * Gets a string for the given key from the resource bundle in {@linkplain #BUNDLE}.
     *
     * @param key the key for the desired string
     * @return the string for the given key
     * @throws NullPointerException               if {@code key} is {@code null}
     * @throws java.util.MissingResourceException if no object for the given key can be found
     * @throws ClassCastException                 if the object found for the given key is not a string
     */
    public static String stringLookup(String key) {
        return BUNDLE.getString(key);
    }

    /**
     * Gets a int for the given key from the resource bundle in {@linkplain #BUNDLE}.
     *
     * @param key the key for the desired string
     * @return the string for the given key
     * @throws NullPointerException               if {@code key} is {@code null}
     * @throws java.util.MissingResourceException if no object for the given key can be found
     * @throws ClassCastException                 if the object found for the given key is not a string
     */
    public static int intLookup(String key) {
        return Integer.parseInt(BUNDLE.getString(key));
    }

    /**
     * Gets a boolean for the given key from the resource bundle in {@linkplain #BUNDLE}.
     *
     * @param key the key for the desired string
     * @return the string for the given key
     * @throws NullPointerException               if {@code key} is {@code null}
     * @throws java.util.MissingResourceException if no object for the given key can be found
     * @throws ClassCastException                 if the object found for the given key is not a string
     */
    public static boolean boolLookup(String key) {
        return Boolean.parseBoolean(BUNDLE.getString(key));
    }

    /**
     * Gets a double for the given key from the resource bundle in {@linkplain #BUNDLE}.
     *
     * @param key the key for the desired string
     * @return the string for the given key
     * @throws NullPointerException               if {@code key} is {@code null}
     * @throws java.util.MissingResourceException if no object for the given key can be found
     * @throws ClassCastException                 if the object found for the given key is not a string
     */
    public static double doubleLookup(String key) {
        return Double.parseDouble(BUNDLE.getString(key));
    }

    /**
     * Private constructor to prevent instantiation.
     */
    private Resources() { /* do not instantiate */ }
}
