package pp.mdga.client.animation;

import pp.mdga.client.InitControl;

public class ActionControl extends InitControl {
    private final Runnable runnable;

    /**
     * Constructs a new ActionControl object with the specified action.
     *
     * @param runnable The action to be performed.
     */
    public ActionControl(Runnable runnable) {
        this.runnable = runnable;
    }

    /**
     * Performs the action associated with this control.
     */
    protected void action() {
        if (null != runnable) {
            runnable.run();
        }
    }
}
