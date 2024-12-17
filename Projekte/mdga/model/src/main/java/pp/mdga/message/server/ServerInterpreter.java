package pp.mdga.message.server;

/**
 * An interface for processing server messages.
 * Implementations of this interface can be used to handle different types of server messages.
 */
public interface ServerInterpreter {
    /**
     * Handles an ActivePlayer message received from the server.
     *
     * @param msg the ActivePlayer message received
     */
    void received(ActivePlayerMessage msg);

    /**
     * Handles an AnyPiece message received from the server.
     *
     * @param msg the AnyPiece message received
     */
    void received(AnyPieceMessage msg);

    /**
     * Handles a Briefing message received from the server.
     *
     * @param msg the Briefing message received
     */
    void received(BriefingMessage msg);

    /**
     * Handles a Ceremony message received from the server.
     *
     * @param msg the Ceremony message received
     */
    void received(CeremonyMessage msg);

    /**
     * Handles a Die message received from the server.
     *
     * @param msg the Die message received
     */
    void received(DieMessage msg);

    /**
     * Handles a DiceAgain message received from the server.
     *
     * @param msg the DiceAgain message received
     */
    void received(DiceAgainMessage msg);

    /**
     * Handles a DiceNow message received from the server.
     *
     * @param msg the DiceNow message received
     */
    void received(DiceNowMessage msg);

    /**
     * Handles an EndOfTurn message received from the server.
     *
     * @param msg the EndOfTurn message received
     */
    void received(EndOfTurnMessage msg);

    /**
     * Handles a LobbyAccept message received from the server.
     *
     * @param msg the LobbyAccept message received
     */
    void received(LobbyAcceptMessage msg);

    /**
     * Handles a LobbyDeny message received from the server.
     *
     * @param msg the LobbyDeny message received
     */
    void received(LobbyDenyMessage msg);

    /**
     * Handles a LobbyPlayerJoin message received from the server.
     *
     * @param msg the LobbyPlayerJoin message received
     */
    void received(LobbyPlayerJoinedMessage msg);

    /**
     * Handles a LobbyPlayerLeave message received from the server.
     *
     * @param msg the LobbyPlayerLeave message received
     */
    void received(LobbyPlayerLeaveMessage msg);

    /**
     * Handles a MoveMessage message received from the server.
     *
     * @param msg the MoveMessage message received
     */
    void received(MoveMessage msg);

    /**
     * Handles a NoTurn message received from the server.
     *
     * @param msg the NoTurn message received
     */
    void received(NoTurnMessage msg);

    /**
     * Handles a PauseGame message received from the server.
     *
     * @param msg the PauseGame message received
     */
    void received(PauseGameMessage msg);

    /**
     * Handles a PlayCard message received from the server.
     *
     * @param msg the PlayCard message received
     */
    void received(PlayCardMessage msg);

    /**
     * Handles a PossibleCard message received from the server.
     *
     * @param msg the PossibleCard message received
     */
    void received(PossibleCardsMessage msg);

    /**
     * Handles a PossiblePiece message received from the server.
     *
     * @param msg the PossiblePiece message received
     */
    void received(PossiblePieceMessage msg);

    /**
     * Handles a RankingResponse message received from the server.
     *
     * @param msg the RankingResponse message received
     */
    void received(RankingResponseMessage msg);

    /**
     * Handles a RankingRollAgain message received from the server.
     *
     * @param msg the RankingRollAgain message received
     */
    void received(RankingRollAgainMessage msg);

    /**
     * Handles a ReconnectBriefing message received from the server.
     *
     * @param msg the ReconnectBriefing message received
     */
    void received(ReconnectBriefingMessage msg);

    /**
     * Handles a ResumeGame message received from the server.
     *
     * @param msg the ResumeGame message received
     */
    void received(ResumeGameMessage msg);

    /**
     * Handles a ServerStartGame message received from the server.
     *
     * @param msg the ServerStartGame message received
     */
    void received(ServerStartGameMessage msg);

    /**
     * Handles a StartPiece message received from the server.
     *
     * @param msg the StartPiece message received
     */
    void received(StartPieceMessage msg);

    /**
     * Handles a UpdateReady message received from the server.
     *
     * @param msg the UpdateReady message received
     */
    void received(UpdateReadyMessage msg);

    /**
     * Handles a UpdateTSK message received from the server.
     *
     * @param msg the UpdateTSK message received
     */
    void received(UpdateTSKMessage msg);

    /**
     * Handles a WaitPiece message received from the server.
     *
     * @param msg the WaitPiece message received
     */
    void received(WaitPieceMessage msg);

    /**
     * Handles a Spectator message received from the server.
     *
     * @param msg the Spectator message received.
     */
    void received(SpectatorMessage msg);

    /**
     * Handles a SelectPiece message received from the server.
     *
     * @param msg the SelectPiece message received.
     */
    void received(SelectPieceMessage msg);

    /**
     * Handles a Shutdown message received from the server.
     *
     * @param msg the Shutdown message received.
     */
    void received(ShutdownMessage msg);

    /**
     * Handles a IncorrectRequest message received from the server.
     *
     * @param msg the IncorrectRequest message received.
     */
    void received(IncorrectRequestMessage msg);

    void received(ChoosePieceStateMessage choosePieceStateMessage);

    void received(DrawCardMessage drawCardMessage);
}
