package pp.mdga.notification;

/**
 * Notification that is to give information to the player.
 */
public class InfoNotification extends Notification {

    private final String message;

    private boolean isError = false;

    /**
     * Constructor.
     */
    public InfoNotification(String info) {
        this.message = info;
    }

    /**
     * Constructor.
     */
    public InfoNotification(String info, boolean isError) {
        this.message = info;
        this.isError = isError;
    }

    /**
     * This method will return the message attribute of InfoNotification class.
     *
     * @return message as a String object.
     */
    public String getMessage() {
        return message;
    }

    /**
     * This method will return the isError attribute of InfoNotification class.
     *
     * @return isError as a boolean object.
     */
    public boolean isError() {
        return isError;
    }
}
