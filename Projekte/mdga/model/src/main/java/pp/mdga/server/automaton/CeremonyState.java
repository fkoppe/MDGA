package pp.mdga.server.automaton;

import pp.mdga.game.Color;
import pp.mdga.message.server.CeremonyMessage;
import pp.mdga.message.server.ShutdownMessage;
import pp.mdga.server.ServerGameLogic;

/**
 *
 */
public class CeremonyState extends ServerState {
    /**
     * Create LobbyState constants.
     */
    private static final System.Logger LOGGER = System.getLogger(CeremonyState.class.getName());

    /**
     * Constructor.
     *
     * @param logic as the server game logic which is the automaton as a ServerGameLogic object.
     */
    public CeremonyState(ServerGameLogic logic) {
        super(logic);
    }

    /**
     * This method is used to create a CeremonyNotification
     *
     * @return the created CeremonyNotification
     */
    private CeremonyMessage createCeremonyMessage() {
        CeremonyMessage message = new CeremonyMessage();
        for (var player : logic.getGame().getPlayerRanking().entrySet()) {
            message.getColors().add(player.getValue().getColor());
            message.getNames().add(player.getValue().getName());
            message.getSixes().add(player.getValue().getPlayerStatistic().getDiced6());
            message.getBonusNodes().add(player.getValue().getPlayerStatistic().getActivatedBonusNodes());
            message.getPiecesLost().add(player.getValue().getPlayerStatistic().getPiecesBeingThrown());
            message.getPiecesThrown().add(player.getValue().getPlayerStatistic().getPiecesThrown());
            message.getNodesMoved().add(player.getValue().getPlayerStatistic().getTraveledNodes());
            message.getBonusCardsPlayed().add(player.getValue().getPlayerStatistic().getCardsPlayed());
        }

        message.getNames().add("GAME OVERALL");
        message.getColors().add(Color.NONE);
        message.getNodesMoved().add(logic.getGame().getGameStatistics().getTraveledNodes());
        message.getSixes().add(logic.getGame().getGameStatistics().getDiced6());
        message.getPiecesThrown().add(logic.getGame().getGameStatistics().getPiecesThrown());
        message.getPiecesLost().add(logic.getGame().getGameStatistics().getPiecesBeingThrown());
        message.getBonusNodes().add(logic.getGame().getGameStatistics().getActivatedBonusNodes());
        message.getBonusCardsPlayed().add(logic.getGame().getGameStatistics().getCardsPlayed());

        return message;
    }

    /**
     * This method will be used whenever this state will be entered.
     */
    @Override
    public void enter() {
        LOGGER.log(System.Logger.Level.DEBUG, "Entered CeremonyState state.");
        logic.getServerSender().broadcast(createCeremonyMessage());
        logic.getServerSender().shutdown();
    }

    /**
     * This method will be used whenever this state will be exited.
     */
    @Override
    public void exit() {
        LOGGER.log(System.Logger.Level.DEBUG, "Exited CeremonyState state.");
    }
}
