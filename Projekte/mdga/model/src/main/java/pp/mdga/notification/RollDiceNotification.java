package pp.mdga.notification;

import pp.mdga.game.Color;

/**
 * Notification that a die has been rolled.
 */
public class RollDiceNotification extends Notification {

    private Color color;
    private int eyes;
    private boolean turbo;
    private int multiplier;
    private boolean isRanking;

    //normal

    /**
     * Constructor.
     *
     * @param color the color of the player that rolled the die.
     * @param eyes  the number of eyes that were rolled.
     */
    public RollDiceNotification(Color color, int eyes) {
        this.color = color;
        this.eyes = eyes;
        this.turbo = false;
        this.multiplier = -1;
        this.isRanking = false;
    }

    /**
     * Constructor.
     *
     * @param color the color of the player that rolled the die.
     * @param eyes  the number of eyes that were rolled.
     * @param isRanking  whether the roll is for ranking purposes.
     */
    public RollDiceNotification(Color color, int eyes, boolean isRanking) {
        this.color = color;
        this.eyes = eyes;
        this.turbo = false;
        this.multiplier = -1;
        this.isRanking = isRanking;
    }

    /**
     * Constructor.
     *
     * @param color the color of the player that rolled the die.
     * @param eyes  the number of eyes that were rolled.
     * @param multiplier the multiplier of the roll.
     */
    public RollDiceNotification(Color color, int eyes, int multiplier) {
        this.color = color;
        this.eyes = eyes;
        this.turbo = true;
        this.multiplier = multiplier;
        this.isRanking = false;
    }

    /**
     * Get the color of the player that rolled the die.
     *
     * @return the color of the player that rolled the die.
     */
    public Color getColor() {
        return color;
    }

    /**
     * Get the number of eyes that were rolled.
     *
     * @return the number of eyes that were rolled.
     */
    public int getEyes() {
        return eyes;
    }

    /**
     * Get the multiplier of the roll.
     *
     * @return the multiplier of the roll.
     */
    public int getMultiplier() {
        return multiplier;
    }

    /**
     * Get whether the roll was a turbo roll.
     *
     * @return true if the roll was a turbo roll, false otherwise.
     */
    public boolean isTurbo() {
        return turbo;
    }

    /**
     * Get whether the roll was for ranking purposes.
     *
     * @return true if the roll was for ranking purposes, false otherwise.
     */
    public boolean isRanking() {
        return isRanking;
    }
}
