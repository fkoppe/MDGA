package pp.mdga.client.gamestate.turnstate.powercardstate;

import pp.mdga.client.ClientGameLogic;
import pp.mdga.client.ClientState;
import pp.mdga.client.gamestate.turnstate.PowerCardState;
import pp.mdga.game.BonusCard;
import pp.mdga.game.Player;
import pp.mdga.game.card.PowerCard;
import pp.mdga.message.client.NoPowerCardMessage;
import pp.mdga.message.client.SelectCardMessage;
import pp.mdga.message.server.DiceNowMessage;
import pp.mdga.message.server.PlayCardMessage;
import pp.mdga.message.server.PossibleCardsMessage;
import pp.mdga.message.server.PossiblePieceMessage;
import pp.mdga.notification.SelectableCardsNotification;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * State where the player can choose a power card
 */
public class ChoosePowerCardState extends PowerCardStates {

    private final PowerCardState parent;
    private ArrayList<PowerCard> possibleCards = new ArrayList<>();

    /**
     * Constructor
     *
     * @param parent parent state
     * @param logic  game logic
     */
    public ChoosePowerCardState(ClientState parent, ClientGameLogic logic) {
        super(parent, logic);
        this.parent = (PowerCardState) parent;
    }

    /**
     * Enter the state
     */
    @Override
    public void enter() {
    }

    /**
     * Exit the state
     */
    @Override
    public void exit() {
        possibleCards = new ArrayList<>();
    }

    /**
     * Set the possible cards
     *
     * @param msg possible cards message
     */
    @Override
    public void received(PossibleCardsMessage msg) {
        possibleCards = (ArrayList<PowerCard>) msg.getPossibleCards();
        ArrayList<BonusCard> possibleBonusCards = new ArrayList<>();
        for (PowerCard card : possibleCards) {
            if (!possibleBonusCards.contains(card.getCard())) {
                possibleBonusCards.add(card.getCard());
            }
        }
        logic.addNotification(new SelectableCardsNotification(possibleBonusCards));
    }

    /**
     * Select a card
     *
     * @param card card to select
     */
    @Override
    public void selectCard(BonusCard card) {
        Player player = logic.getGame().getPlayers().get(logic.getOwnPlayerId());
        ArrayList<PowerCard> handCards = player.getHandCards();
        if (card != null) {
            PowerCard select = player.getPowerCardByType(card);
            if (select == null) {
                select = select;
            }
            logic.send(new SelectCardMessage(select));
        } else {
            logic.send(new NoPowerCardMessage());
        }
    }

    /**
     * Receive a card
     *
     * @param msg card message
     */
    @Override
    public void received(PlayCardMessage msg) {
        if (msg.getCard().getCard().equals(BonusCard.TURBO)) {
            logic.getGame().setDiceModifier(msg.getDiceModifier());
            parent.getParent().getPlayPowerCard().setPlayCard(msg);
            logic.getGame().setTurboFlag(true);
            parent.getParent().setState(parent.getParent().getPlayPowerCard());
        }
    }

    /**
     * Receive a die now message
     *
     * @param msg dice now message
     */
    @Override
    public void received(DiceNowMessage msg) {
        parent.getParent().setState(parent.getParent().getRollDice());
    }

    /**
     * Receive a possible piece message and decide if the player can swap or shield
     *
     * @param msg possible piece message
     */
    @Override
    public void received(PossiblePieceMessage msg) {
        if (msg.getEnemyPossiblePieces().isEmpty()) {
            parent.getShield().setPossiblePieces(msg.getOwnPossiblePieces().stream().map(piece -> logic.getGame().getPieceThroughUUID(piece.getUuid())).collect(Collectors.toCollection(ArrayList::new)));
            parent.setState(parent.getShield());
        } else {
            System.out.println("Should enter Swap State");
            parent.getSwap().setPossibleOwnPieces(msg.getOwnPossiblePieces().stream().map(piece -> logic.getGame().getPieceThroughUUID(piece.getUuid())).collect(Collectors.toCollection(ArrayList::new)));
            parent.getSwap().setPossibleEnemyPieces(msg.getEnemyPossiblePieces().stream().map(piece -> logic.getGame().getPieceThroughUUID(piece.getUuid())).collect(Collectors.toCollection(ArrayList::new)));
            parent.setState(parent.getSwap());
        }
    }
}
