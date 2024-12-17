package pp.mdga.client;

import pp.mdga.Resources;
import pp.mdga.game.BonusCard;
import pp.mdga.game.Color;
import pp.mdga.game.Piece;
import pp.mdga.message.server.*;
import pp.mdga.notification.InfoNotification;
import pp.mdga.notification.StartDialogNotification;

import java.lang.System.Logger.Level;

public abstract class ClientState implements Observer, ServerInterpreter {
    protected static final System.Logger LOGGER = System.getLogger(ClientState.class.getName());

    protected ClientState parent;

    protected ClientGameLogic logic;

    protected ClientState(ClientState parent, ClientGameLogic logic) {
        this.parent = parent;
        this.logic = logic;
    }

    public abstract void enter();

    public abstract void exit();

    public ClientState getParent() {
        return parent;
    }

    public void update() {/* do nothing */}

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

    @Override
    public void received(ActivePlayerMessage msg) {
        LOGGER.log(Level.DEBUG, "Received {0} not allowed.", msg.toString());
    }

    @Override
    public void received(AnyPieceMessage msg) {
        LOGGER.log(Level.DEBUG, "Received {0} not allowed.", msg.toString());
    }

    @Override
    public void received(BriefingMessage msg) {
        LOGGER.log(Level.DEBUG, "Received {0} not allowed.", msg.toString());
    }

    @Override
    public void received(CeremonyMessage msg) {
        LOGGER.log(Level.DEBUG, "Received {0} not allowed.", msg.toString());
    }

    @Override
    public void received(DieMessage msg) {
        LOGGER.log(Level.DEBUG, "Received {0} not allowed.", msg.toString());
    }

    @Override
    public void received(DiceAgainMessage msg) {
        LOGGER.log(Level.DEBUG, "Received {0} not allowed.", msg.toString());
    }

    @Override
    public void received(DiceNowMessage msg) {
        LOGGER.log(Level.DEBUG, "Received {0} not allowed.", msg.toString());
    }

    @Override
    public void received(EndOfTurnMessage msg) {
        LOGGER.log(Level.DEBUG, "Received {0} not allowed.", msg.toString());
    }

    @Override
    public void received(LobbyAcceptMessage msg) {
        LOGGER.log(Level.DEBUG, "Received {0} not allowed.", msg.toString());
    }

    @Override
    public void received(LobbyDenyMessage msg) {
        LOGGER.log(Level.DEBUG, "Received {0} not allowed.", msg.toString());
    }

    @Override
    public void received(LobbyPlayerJoinedMessage msg) {
        LOGGER.log(Level.DEBUG, "Received {0} not allowed.", msg.toString());
    }

    @Override
    public void received(LobbyPlayerLeaveMessage msg) {
        LOGGER.log(Level.DEBUG, "Received {0} not allowed.", msg.toString());
    }

    @Override
    public void received(ChoosePieceStateMessage msg) {
        LOGGER.log(Level.DEBUG, "Received {0} not allowed.", msg.toString());
    }

    @Override
    public void received(DrawCardMessage msg) {
        LOGGER.log(Level.DEBUG, "Received {0} not allowed.", msg.toString());
    }

    @Override
    public void received(MoveMessage msg) {
        LOGGER.log(Level.DEBUG, "Received {0} not allowed.", msg.toString());
    }

    @Override
    public void received(NoTurnMessage msg) {
        LOGGER.log(Level.DEBUG, "Received {0} not allowed.", msg.toString());
    }

    @Override
    public void received(PauseGameMessage msg) {
        LOGGER.log(Level.DEBUG, "Received {0} not allowed.", msg.toString());
    }

    @Override
    public void received(PlayCardMessage msg) {
        LOGGER.log(Level.DEBUG, "Received {0} not allowed.", msg.toString());
    }

    @Override
    public void received(PossibleCardsMessage msg) {
        LOGGER.log(Level.DEBUG, "Received {0} not allowed.", msg.toString());
    }

    @Override
    public void received(PossiblePieceMessage msg) {
        LOGGER.log(Level.DEBUG, "Received {0} not allowed.", msg.toString());
    }

    @Override
    public void received(RankingResponseMessage msg) {
        LOGGER.log(Level.DEBUG, "Received {0} not allowed.", msg.toString());
    }

    @Override
    public void received(RankingRollAgainMessage msg) {
        LOGGER.log(Level.DEBUG, "Received {0} not allowed.", msg.toString());
    }

    @Override
    public void received(ReconnectBriefingMessage msg) {
        LOGGER.log(Level.DEBUG, "Received {0} not allowed.", msg.toString());
    }

    @Override
    public void received(ResumeGameMessage msg) {
        LOGGER.log(Level.DEBUG, "Received {0} not allowed.", msg.toString());
    }

    @Override
    public void received(ServerStartGameMessage msg) {
        LOGGER.log(Level.DEBUG, "Received {0} not allowed.", msg.toString());
    }

    @Override
    public void received(ShutdownMessage msg) {
        logic.addNotification(new InfoNotification(Resources.stringLookup("server.shutdown")));
        logic.addNotification(new StartDialogNotification());
        logic.setState(logic.getDialogs());
    }

    @Override
    public void received(StartPieceMessage msg) {
        LOGGER.log(Level.DEBUG, "Received {0} not allowed.", msg.toString());
    }

    @Override
    public void received(UpdateReadyMessage msg) {
        LOGGER.log(Level.DEBUG, "Received {0} not allowed.", msg.toString());
    }

    @Override
    public void received(UpdateTSKMessage msg) {
        LOGGER.log(Level.DEBUG, "Received {0} not allowed.", msg.toString());
    }

    @Override
    public void received(SpectatorMessage msg) {
        LOGGER.log(Level.DEBUG, "Received {0} not allowed.", msg.toString());
    }

    @Override
    public void received(SelectPieceMessage msg) {
        LOGGER.log(Level.DEBUG, "Received {0} not allowed.", msg.toString());
    }

    @Override
    public void received(WaitPieceMessage msg) {
        LOGGER.log(Level.DEBUG, "Received {0} not allowed.", msg.toString());
    }

    @Override
    public void received(IncorrectRequestMessage msg) {
        LOGGER.log(Level.DEBUG, "Received {0} not allowed.", msg.toString());
    }

    public void selectPiece(Piece piece) {
        LOGGER.log(Level.DEBUG, "Selecting piece not allowed.");
    }

    public void selectCard(BonusCard card) {
        LOGGER.log(Level.DEBUG, "Selecting card not allowed.");
    }

    public void selectTSK(Color color) {
        LOGGER.log(Level.DEBUG, "Selecting TSK not allowed.");
    }

    public void selectDice() {
        LOGGER.log(Level.DEBUG, "Selecting dice not allowed.");
    }

    public void setName(String name) {
        LOGGER.log(Level.DEBUG, "Setting name not allowed.");
    }

    public void selectReady() {
        LOGGER.log(Level.DEBUG, "Selecting ready not allowed.");
    }

    public void selectHost(String name) {
        LOGGER.log(Level.DEBUG, "Selecting host not allowed.");
    }

    public void selectJoin(String string) {
        LOGGER.log(Level.DEBUG, "Selecting join not allowed.");
    }

    public void selectLeave() {
        LOGGER.log(Level.DEBUG, "Selecting leave not allowed.");
    }

    public void deselectTSK(Color color) {
        LOGGER.log(Level.DEBUG, "Deselecting TSK not allowed.");
    }

    public void selectUnready() {
        LOGGER.log(Level.DEBUG, "Selecting unready not allowed.");
    }

    public void selectStart() {
        LOGGER.log(Level.DEBUG, "Starting not allowed");
    }

    public void selectAnimationEnd() {
        LOGGER.log(Level.DEBUG, "Animation end not allowed");
    }

    public void selectNext() {
        LOGGER.log(Level.DEBUG, "Next not allowed");
    }

    public void selectResume() {
        LOGGER.log(Level.DEBUG, "Resume not allowed");
    }
}
