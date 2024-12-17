package pp.mdga.client.gamestate.determinestartplayerstate;

import pp.mdga.client.ClientGameLogic;
import pp.mdga.client.ClientState;
import pp.mdga.client.gamestate.DetermineStartPlayerState;
import pp.mdga.game.Player;
import pp.mdga.game.card.PowerCard;
import pp.mdga.message.client.AnimationEndMessage;
import pp.mdga.notification.AcquireCardNotification;
import pp.mdga.notification.ActivePlayerNotification;
import pp.mdga.notification.DrawCardNotification;
import pp.mdga.notification.MovePieceNotification;

import java.util.Map;

public class Intro extends DetermineStartPlayerStates {

    private final DetermineStartPlayerState parent;

    private int animationCounter = 0;

    /**
     * Constructor for Intro
     *
     * @param parent the parent state
     * @param logic  the client game logic
     */
    public Intro(ClientState parent, ClientGameLogic logic) {
        super(parent, logic);
        this.parent = (DetermineStartPlayerState) parent;
    }

    /**
     * This method is used to get the parent state;
     *
     * @return the parent state
     */
    public DetermineStartPlayerState getParent() {
        return parent;
    }

    /**
     * This method is used to enter this state and play all necessary intro animations.
     */
    @Override
    public void enter() {
        for (Map.Entry<Integer, Player> entry : logic.getGame().getPlayers().entrySet()) {
            logic.addNotification(new MovePieceNotification(entry.getValue().getPieces()[0].getUuid(), entry.getValue().getStartNodeIndex(), true));
            logic.getGame().getBoard().getInfield()[entry.getValue().getStartNodeIndex()].setOccupant(entry.getValue().getPieces()[0]);
            entry.getValue().getWaitingArea()[0] = null;
            animationCounter++;
            for (PowerCard card : entry.getValue().getHandCards()) {
                if (entry.getKey() == logic.getOwnPlayerId()) {
                    logic.addNotification(new AcquireCardNotification(card.getCard()));
                } else {
                    logic.addNotification(new DrawCardNotification(entry.getValue().getColor(), card.getCard()));
                }
            }
        }
    }

    /**
     * This method i s used to exit this state.
     */
    @Override
    public void exit() {
        animationCounter = 0;
    }

    /**
     * This method is used when the view has completed the animation.
     */
    @Override
    public void selectAnimationEnd() {
        animationCounter--;
        if (animationCounter != 0) {
            return;
        }
        logic.send(new AnimationEndMessage());
        if (logic.getGame().getActivePlayerId() == logic.getOwnPlayerId()) {
            parent.getParent().setState(parent.getParent().getTurn());
            logic.addNotification(new ActivePlayerNotification(logic.getGame().getActiveColor()));
        } else {
            parent.getParent().setState(parent.getParent().getWaiting());
            logic.addNotification(new ActivePlayerNotification(logic.getGame().getActiveColor()));
        }
    }

    /**
     * this method is used to return the animationCounter
     *
     * @return int animationCounter
     */
    public int getAnimationCounter(){
        return animationCounter;
    }
}
