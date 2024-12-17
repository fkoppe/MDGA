package pp.mdga.message.client;

/**
 * Visitor interface for processing all client messages.
 */
public interface ClientInterpreter {
    /**
     * Processes a received AnimationEnd message.
     *
     * @param msg  the AnimationEnd message to be processed
     * @param from the connection ID from which the message was received
     */
    void received(AnimationEndMessage msg, int from);

    /**
     * Processes a received DeselectTSK message.
     *
     * @param msg  the DeselectTSK message to be processed
     * @param from the connection ID from which the message was received
     */
    void received(DeselectTSKMessage msg, int from);

    /**
     * Processes a received StartGame message.
     *
     * @param msg  the StartGame message to be processed
     * @param from the connection ID from which the message was received
     */
    void received(StartGameMessage msg, int from);

    /**
     * Processes a received JoinedLobby message.
     *
     * @param msg  the JoinedLobby message to be processed
     * @param from the connection ID from which the message was received
     */
    void received(JoinedLobbyMessage msg, int from);

    /**
     * Processes a received LeaveGame message.
     *
     * @param msg  the LeaveGame message to be processed
     * @param from the connection ID from which the message was received
     */
    void received(LeaveGameMessage msg, int from);

    /**
     * Processes a received LobbyNotReady message.
     *
     * @param msg  the LobbyNotReady message to be processed
     * @param from the connection ID from which the message was received
     */
    void received(LobbyNotReadyMessage msg, int from);

    /**
     * Processes a received LobbyReady message.
     *
     * @param msg  the LobbyReady message to be processed
     * @param from the connection ID from which the message was received
     */
    void received(LobbyReadyMessage msg, int from);

    /**
     * Processes a received Disconnected message.
     *
     * @param msg  the Disconnected message to be processed
     * @param from the connection ID from which the message was received
     */
    void received(DisconnectedMessage msg, int from);

    /**
     * Processes a received RequestBriefing message.
     *
     * @param msg  the RequestBriefing message to be processed
     * @param from the connection ID from which the message was received
     */
    void received(RequestBriefingMessage msg, int from);

    /**
     * Processes a received RequestDie message.
     *
     * @param msg  the RequestDie message to be processed
     * @param from the connection ID from which the message was received
     */
    void received(RequestDieMessage msg, int from);

    /**
     * Processes a received RequestMove message.
     *
     * @param msg  the RequestMove message to be processed
     * @param from the connection ID from which the message was received
     */
    void received(RequestMoveMessage msg, int from);

    /**
     * Processes a received RequestPlayCard message.
     *
     * @param msg  the RequestPlayCard message to be processed
     * @param from the connection ID from which the message was received
     */
    void received(RequestPlayCardMessage msg, int from);

    /**
     * Processes a received SelectCard message.
     *
     * @param msg  the SelectCard message to be processed
     * @param from the connection ID from which the message was received
     */
    void received(SelectCardMessage msg, int from);

    /**
     * Processes a received SelectTSK message.
     *
     * @param msg  the SelectTSK message to be processed
     * @param from the connection ID from which the message was received
     */
    void received(SelectTSKMessage msg, int from);

    /**
     * Processes a received ForceContinueGame message.
     *
     * @param msg  the ForceContinueGame message to be processed
     * @param from the connection ID from which the message was received
     */
    void received(ForceContinueGameMessage msg, int from);

    /**
     * Processes a received ClientStartGame message.
     *
     * @param msg  the ClientStartGame message to be processed
     * @param from the connection ID from which the message was received
     */
    void received(ClientStartGameMessage msg, int from);

    /**
     * Processes a received NoPowerCard message.
     *
     * @param msg  the NoPowerCard message to be processed
     * @param from the connection ID from which the message was received
     */
    void received(NoPowerCardMessage msg, int from);

    /**
     * Processes a received SelectedPieces message.
     *
     * @param msg  the SelectedPieces message to be processed
     * @param from the connection ID from which the message was received
     */
    void received(SelectedPiecesMessage msg, int from);
}
