package pp.mdga.notification;

import pp.mdga.game.Color;

public class FinishNotification extends Notification {

    private final Color colorFinished;

    /**
     * Constructor.
     */
    public FinishNotification(Color colorFinished) {
        this.colorFinished = colorFinished;
    }

    /**
     * This method will return the colorFinished attribute of FinishNotification class.
     *
     * @return colorFinished as a Color object.
     */
    public Color getColorFinished() {
        return colorFinished;
    }
}
