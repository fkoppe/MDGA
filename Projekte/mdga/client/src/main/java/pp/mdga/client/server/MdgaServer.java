package pp.mdga.client.server;

import com.jme3.network.*;
import com.jme3.network.serializing.Serializer;
import com.jme3.network.serializing.serializers.EnumSerializer;
import pp.mdga.Resources;
import pp.mdga.game.*;
import pp.mdga.game.card.*;
import pp.mdga.message.client.*;
import pp.mdga.message.server.*;
import pp.mdga.server.ServerGameLogic;
import pp.mdga.server.ServerSender;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.LogManager;

/**
 * Server implementing the visitor pattern as MessageReceiver for ClientMessages
 */
public class MdgaServer implements MessageListener<HostedConnection>, ConnectionListener, ServerSender {
    private static final Logger LOGGER = System.getLogger(MdgaServer.class.getName());

    private Server myServer;
    private static int port;
    private final ServerGameLogic logic;
    private final BlockingQueue<ReceivedMessage> pendingMessages = new LinkedBlockingQueue<>();
    private volatile boolean running = true;

    static {
        // Configure logging
        LogManager manager = LogManager.getLogManager();
        try {
            manager.readConfiguration(new FileInputStream("logging.properties"));
            LOGGER.log(Level.INFO, "Successfully read logging properties"); //NON-NLS
        } catch (IOException e) {
            LOGGER.log(Level.INFO, e.getMessage());
        }
    }

    /**
     * Constructor.
     *
     * @param port as the port for this server
     */
    public MdgaServer(int port) {
        MdgaServer.port = port;
        LOGGER.log(Level.INFO, "Creating MdgaServer"); //NON-NLS
        logic = new ServerGameLogic(this, new Game());
    }

    /**
     * Main method to start the server.
     */
    public void run() {
        startServer();
        while (running) {
            processNextMessage();
        }
        shutdownServerResources();
    }

    /**
     * Starts the server and initializes listeners.
     */
    private void startServer() {
        try {
            LOGGER.log(Level.INFO, "Starting server...");
            unlockSerializers();//NON-NLS
            myServer = Network.createServer(port);
            initializeSerializables();
            myServer.start();
            registerListeners();
            LOGGER.log(Level.INFO, "Server started: {0}", myServer.isRunning()); //NON-NLS
        } catch (IOException e) {
            LOGGER.log(Level.ERROR, "Couldn't start server: {0}", e.getMessage()); //NON-NLS
            exit();
        }
    }

    /**
     * Process the next message in the queue.
     */
    private void processNextMessage() {
        try {
            ReceivedMessage message = pendingMessages.take(); // This is a blocking call
            message.process(logic);
        } catch (InterruptedException ex) {
            LOGGER.log(Level.INFO, "Server thread interrupted, shutting down..."); //NON-NLS
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Register serializable classes.
     */
    private void initializeSerializables() {

        Serializer.registerClass(UUID.class, new UUIDSerializer());
        Serializer.registerClass(AnimationEndMessage.class);
        Serializer.registerClass(ClientStartGameMessage.class);
        Serializer.registerClass(DeselectTSKMessage.class);
        Serializer.registerClass(ForceContinueGameMessage.class);
        Serializer.registerClass(StartGameMessage.class);
        Serializer.registerClass(JoinedLobbyMessage.class);
        Serializer.registerClass(LeaveGameMessage.class);
        Serializer.registerClass(LobbyNotReadyMessage.class);
        Serializer.registerClass(LobbyReadyMessage.class);
        Serializer.registerClass(NoPowerCardMessage.class);
        Serializer.registerClass(RequestBriefingMessage.class);
        Serializer.registerClass(RequestDieMessage.class);
        Serializer.registerClass(RequestMoveMessage.class);
        Serializer.registerClass(RequestPlayCardMessage.class);
        Serializer.registerClass(SelectCardMessage.class);
        Serializer.registerClass(SelectedPiecesMessage.class);
        Serializer.registerClass(SelectPieceMessage.class);
        Serializer.registerClass(SelectTSKMessage.class);
        Serializer.registerClass(ActivePlayerMessage.class);
        Serializer.registerClass(AnyPieceMessage.class);
        Serializer.registerClass(BriefingMessage.class);
        Serializer.registerClass(CeremonyMessage.class);
        Serializer.registerClass(DieMessage.class);
        Serializer.registerClass(DiceAgainMessage.class);
        Serializer.registerClass(DiceNowMessage.class);
        Serializer.registerClass(EndOfTurnMessage.class);
        Serializer.registerClass(LobbyAcceptMessage.class);
        Serializer.registerClass(LobbyDenyMessage.class);
        Serializer.registerClass(LobbyPlayerJoinedMessage.class);
        Serializer.registerClass(LobbyPlayerLeaveMessage.class);
        Serializer.registerClass(MoveMessage.class);
        Serializer.registerClass(NoTurnMessage.class);
        Serializer.registerClass(PauseGameMessage.class);
        Serializer.registerClass(PlayCardMessage.class);
        Serializer.registerClass(PossibleCardsMessage.class);
        Serializer.registerClass(PossiblePieceMessage.class);
        Serializer.registerClass(RankingResponseMessage.class);
        Serializer.registerClass(RankingRollAgainMessage.class);
        Serializer.registerClass(ReconnectBriefingMessage.class);
        Serializer.registerClass(ResumeGameMessage.class);
        Serializer.registerClass(ServerStartGameMessage.class);
        Serializer.registerClass(ShutdownMessage.class);
        Serializer.registerClass(StartPieceMessage.class);
        Serializer.registerClass(UpdateReadyMessage.class);
        Serializer.registerClass(UpdateTSKMessage.class);
        Serializer.registerClass(WaitPieceMessage.class);
        Serializer.registerClass(IncorrectRequestMessage.class);
        Serializer.registerClass(SpectatorMessage.class);
        Serializer.registerClass(Player.class);
        Serializer.registerClass(Statistic.class);
        Serializer.registerClass(Board.class);
        Serializer.registerClass(Node.class);
        Serializer.registerClass(Piece.class);
        Serializer.registerClass(BonusNode.class);
        Serializer.registerClass(StartNode.class);
        Serializer.registerClass(HomeNode.class);
        Serializer.registerClass(PowerCard.class);
        Serializer.registerClass(TurboCard.class);
        Serializer.registerClass(SwapCard.class);
        Serializer.registerClass(ShieldCard.class);
        Serializer.registerClass(HiddenCard.class);
        Serializer.registerClass(ChoosePieceStateMessage.class);
        Serializer.registerClass(DrawCardMessage.class);

        Serializer.registerClass(Color.class, new EnumSerializer());
        Serializer.registerClass(PieceState.class, new EnumSerializer());
        Serializer.registerClass(ShieldState.class, new EnumSerializer());
        Serializer.registerClass(BonusCard.class, new EnumSerializer());
    }

    /**
     * Register listeners for the server.
     */
    private void registerListeners() {
        myServer.addMessageListener(this, AnimationEndMessage.class);
        myServer.addMessageListener(this, ClientStartGameMessage.class);
        myServer.addMessageListener(this, DeselectTSKMessage.class);
        myServer.addMessageListener(this, DisconnectedMessage.class);
        myServer.addMessageListener(this, ForceContinueGameMessage.class);
        myServer.addMessageListener(this, StartGameMessage.class);
        myServer.addMessageListener(this, JoinedLobbyMessage.class);
        myServer.addMessageListener(this, LeaveGameMessage.class);
        myServer.addMessageListener(this, LobbyNotReadyMessage.class);
        myServer.addMessageListener(this, LobbyReadyMessage.class);
        myServer.addMessageListener(this, NoPowerCardMessage.class);
        myServer.addMessageListener(this, RequestBriefingMessage.class);
        myServer.addMessageListener(this, RequestDieMessage.class);
        myServer.addMessageListener(this, RequestMoveMessage.class);
        myServer.addMessageListener(this, RequestPlayCardMessage.class);
        myServer.addMessageListener(this, SelectCardMessage.class);
        myServer.addMessageListener(this, SelectedPiecesMessage.class);
        myServer.addMessageListener(this, SelectTSKMessage.class);
        myServer.addConnectionListener(this);
    }

    /**
     * This method will be used to receive network messages from the given source parameter.
     * It will check if the given message parameter is a ClientMessage object. If yes it will call the messageReceived
     * method with the casted ClientMessage object.
     *
     * @param source  as the connection which sends the message as a HostedConnection object.
     * @param message as the received message as a Message object.
     */
    @Override
    public void messageReceived(HostedConnection source, Message message) {
        if (message instanceof ClientMessage) {
            this.messageReceived(source, (ClientMessage) message);
        }
    }

    /**
     * This method will be used to received network messages from the given source parameter.
     * It will add the given message parameter to the pendingMessage attribute of MdgaServer after creating
     * a ReceivedMessage object with it and its id.
     *
     * @param source  as the connection which sends the message as a HostedConnection object.
     * @param message as the received message as a Message object.
     */
    private void messageReceived(HostedConnection source, ClientMessage message) {
        System.out.println("server received from: " + source.getId() + " " + message.getClass().getName());
        pendingMessages.add(new ReceivedMessage(message, source.getId()));
    }

    /**
     * This method will be used to handle all connections which are connected to the server.
     * It will check if the maximum number of connected clients are already reached. If yes it will send a
     * LobbyDenyMessage to the given hostedConnection parameter and close it, otherwise it will send a
     * LobbyAcceptMessage to the given hostedConnection parameter. In Addition, if the number of connected clients is
     * equal to 1 it will set the host of the game to the id of the given hostedConnection parameter.
     *
     * @param server           as the server which is contains all connections as a Server object.
     * @param hostedConnection as the connection which is added to the server as a HostedConnection object.
     */
    @Override
    public void connectionAdded(Server server, HostedConnection hostedConnection) {
        System.out.println("new connection " + hostedConnection); //NON-NLS
        LOGGER.log(Level.DEBUG, "new connection {0}", hostedConnection); //NON-NLS

        if (this.myServer.getConnections().size() > Resources.MAX_PLAYERS) {
            this.logic.getServerSender().send(hostedConnection.getId(), new LobbyDenyMessage());
            hostedConnection.close("");
        } else {
            if (hostedConnection.getAddress().contains("127.0.0.1") && this.logic.getGame().getHost() == -1) {
                this.logic.getGame().setHost(hostedConnection.getId());
                this.logic.getServerSender().send(hostedConnection.getId(), new LobbyAcceptMessage(hostedConnection.getId()));
            } else {
                this.logic.getServerSender().send(hostedConnection.getId(), new LobbyAcceptMessage());
            }
        }
    }

    @Override
    public void connectionRemoved(Server server, HostedConnection hostedConnection) {
        LOGGER.log(Level.INFO, "connection closed: {0}", hostedConnection); //NON-NLS
        final Player player = logic.getGame().getPlayerById(hostedConnection.getId());
        if (player == null)
            LOGGER.log(Level.INFO, "closed connection does not belong to an active player"); //NON-NLS
        else { //NON-NLS
            LOGGER.log(Level.INFO, "closed connection belongs to {0}", player); //NON-NLS
            // exit(0);
            this.handleDisconnect(hostedConnection.getId());
        }
    }

    /**
     * This method will be used to handle unintentional disconnections from players.
     *
     * @param id as the id of the disconnected player.
     */
    public void handleDisconnect(int id) {
        this.logic.received(new DisconnectedMessage(), id);
    }

    /**
     * Stops the server thread gracefully.
     */
    public void exit() {
        LOGGER.log(Level.INFO, "Requesting server shutdown"); //NON-NLS
        running = false;
        pendingMessages.add(new ReceivedMessage(new AnimationEndMessage(), -1));
    }

    /**
     * Send the specified message to the specified connection.
     *
     * @param id      the connection id
     * @param message the message
     */
    @Override
    public void send(int id, ServerMessage message) {
        if (myServer == null || !myServer.isRunning()) {
            LOGGER.log(Level.ERROR, "no server running when trying to send {0}", message); //NON-NLS
            return;
        }
        final HostedConnection connection = myServer.getConnection(id);
        if (connection != null) {
            System.out.println("server sends to: " + id + " " + message.getClass().getName());
            connection.send(message);
        } else LOGGER.log(Level.ERROR, "there is no connection with id={0}", id); //NON-NLS
    }

    /**
     * This method will be used to send the given message parameter to all connected players which are saved inside the
     * players attribute of Game class.
     *
     * @param message as the message which will be sent to all players as a ServerMessage.
     */
    @Override
    public void broadcast(ServerMessage message) {
        for (Map.Entry<Integer, Player> entry : this.logic.getGame().getPlayers().entrySet()) {
            this.send(entry.getKey(), message);
        }
    }

    /**
     * This method will be used to diconenect the client depending on the given id parameter.
     *
     * @param id as the connection id of the client as an Integer.
     */
    @Override
    public void disconnectClient(int id) {
        if (myServer.getConnection(id) != null) {
            this.myServer.getConnection(id).close("");
        } else {
            LOGGER.log(Level.ERROR, "no connection with id={0}", id); //NON-NLS
        }
    }

    /**
     * This method will be used to shut down the server.
     * It will iterate threw all connections of myServer attribute and check if they are equal to null. If not they will
     * be closed. After that the myServer attribute will be closed and this program will be exited with the exit code 0.
     */
    @Override
    public void shutdown() {
        this.exit();
    }

    /**
     * Gracefully shutdown server resources like connections and sockets.
     */
    private void shutdownServerResources() {
        LOGGER.log(Level.INFO, "Shutting down server resources"); //NON-NLS
        if (myServer != null && myServer.isRunning()) {
            for (HostedConnection client : myServer.getConnections()) {
                if (client != null) client.close("Server shutting down.");
            }
            myServer.close();
        }
    }

    /**
     * This method will be used to unlock the Serializer registry.
     */
    private static void unlockSerializers() {
        try {
            Field lockField = Serializer.class.getDeclaredField("locked");
            lockField.setAccessible(true);
            lockField.setBoolean(null, false); // Unlock the Serializer registry
        } catch (NoSuchFieldException | IllegalAccessException e) {
            System.err.println("Failed to unlock the Serializer registry: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
