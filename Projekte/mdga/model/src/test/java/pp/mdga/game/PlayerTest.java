package pp.mdga.game;

import org.junit.Before;
import org.junit.Test;
import pp.mdga.game.card.PowerCard;
import pp.mdga.message.server.ServerMessage;
import pp.mdga.server.ServerGameLogic;
import pp.mdga.server.ServerSender;
import pp.mdga.server.automaton.GameState;
import pp.mdga.server.automaton.game.TurnState;
import pp.mdga.server.automaton.game.turn.ChoosePieceState;
import pp.mdga.server.automaton.game.turn.PlayPowerCardState;
import pp.mdga.server.automaton.game.turn.PowerCardState;
import pp.mdga.server.automaton.game.turn.RollDiceState;
import pp.mdga.server.automaton.game.turn.choosepiece.SelectPieceState;
import pp.mdga.server.automaton.game.turn.choosepiece.StartPieceState;
import pp.mdga.server.automaton.game.turn.choosepiece.WaitingPieceState;
import pp.mdga.server.automaton.game.turn.rolldice.FirstRollState;
import pp.mdga.visitor.Visitor;

import java.util.ArrayList;
import java.util.Map;

import static org.junit.Assert.assertTrue;

public class PlayerTest {

    //declare game here
    private Game game;

    //declare server here
    private ServerGameLogic serverGameLogic;

    //declare player-Client here
    private Player playerClient;
    private String nameClient = "Client";
    private Color clientColor;

    //declare pieces of client here
    private Piece pieceClient0;
    private Piece pieceClient1;
    private Piece pieceClient2;
    private Piece pieceClient3;

    //declare player-host here
    private Player playerHost;
    private String nameHost = "Host";
    private Color hostColor;

    //declare pieces of host here
    private Piece pieceHost0;
    private Piece pieceHost1;
    private Piece pieceHost2;
    private Piece pieceHost3;

    //ID's for player
    private int IDHost;
    private int IDClient;

    //declare states
    GameState gameState;
    TurnState turnState;
    RollDiceState rollDiceState;
    FirstRollState firstRollState;
    ChoosePieceState choosePieceState;
    SelectPieceState selectPieceState;
    PowerCardState powerCardState;
    PlayPowerCardState playPowerCardState;
    WaitingPieceState waitingPieceState;
    StartPieceState startPieceState;

    //declare dies
    private Die die1;
    private Die die2;
    private Die die3;
    private Die die4;
    private Die die5;
    private Die die6;

    //declare PowerCards here
    PowerCard swapCard = new PowerCard() {
        @Override
        public void accept(Visitor visitor) {

        }
    };

    PowerCard shieldCard = new PowerCard() {
        @Override
        public void accept(Visitor visitor) {

        }
    };

    PowerCard turboCard = new PowerCard() {
        @Override
        public void accept(Visitor visitor) {}
    };

    private ArrayList<PowerCard> handCards = new ArrayList<>();

    @Before
    public void Setup() {
        game = new Game();
        //initialize server here
        serverGameLogic = new ServerGameLogic(new ServerSender() {
            @Override
            public void send(int id, ServerMessage message) {

            }

            @Override
            public void broadcast(ServerMessage message) {

            }

            @Override
            public void disconnectClient(int id) {

            }

            @Override
            public void shutdown() {

            }
        }, game);

        IDHost = 1;
        IDClient = 2;

        //declare player-Client here
        playerClient = new Player(nameClient);
        clientColor = Color.ARMY;
        playerClient.setColor(clientColor);
        playerClient.initialize();
        playerClient.setHandCards(handCards);
        game.addPlayer(IDClient, playerClient);

        //declare player-host here
        playerHost = new Player(nameHost);
        hostColor = Color.NAVY;
        playerHost.setColor(hostColor);
        playerHost.initialize();
        playerHost.setHandCards(handCards);
        game.addPlayer(IDHost, playerHost);

        //initialize the playerData
        for(Map.Entry<Integer, Player> entry : game.getPlayers().entrySet()){
            game.addPlayer(entry.getKey(), entry.getValue());
        }


        //declare pieces of client here
        pieceClient0 = game.getPlayerByColor(clientColor).getWaitingArea()[0];
        pieceClient1 = game.getPlayerByColor(clientColor).getWaitingArea()[1];
        pieceClient2 = game.getPlayerByColor(clientColor).getWaitingArea()[2];
        pieceClient3 = game.getPlayerByColor(clientColor).getWaitingArea()[3];

        //clear waiting-area of Client
        for (int i = 0; i < 4; i++) {
            game.getPlayerByColor(clientColor).getWaitingArea()[i] = null;
        }


        //declare pieces of host here
        pieceHost0 = game.getPlayerByColor(hostColor).getWaitingArea()[0];
        pieceHost1 = game.getPlayerByColor(hostColor).getWaitingArea()[1];
        pieceHost2 = game.getPlayerByColor(hostColor).getWaitingArea()[2];
        pieceHost3 = game.getPlayerByColor(hostColor).getWaitingArea()[3];

        //clear waiting-area of Client
        for (int i = 0; i < 4; i++) {
            game.getPlayerByColor(hostColor).getWaitingArea()[i] = null;
        }

        PieceState active = PieceState.ACTIVE;
        PieceState home = PieceState.HOME;
        //set the Client-pieces here
        game.getBoard().setPieceOnBoard(25, pieceClient0);
        pieceClient0.setState(active);
        game.getPlayerByColor(clientColor).setPieceInHome(1, pieceClient1);
        pieceClient1.setState(home);
        game.getBoard().setPieceOnBoard(19, pieceClient2);
        pieceClient2.setState(active);


        //set the host-pieces here
        game.getBoard().setPieceOnBoard(28, pieceHost0); //for UC 02,14 ,15, 03.01,4
        pieceHost0.setState(active);
        game.getBoard().setPieceOnBoard(18, pieceHost1); //for UC 1, 10, 17, 16
        pieceHost1.setState(active);
        game.getPlayerByColor(hostColor).addWaitingPiece(pieceClient2);//set in waitingArea fur uc 5
        game.getBoard().setPieceOnBoard(0, pieceHost3); //for uc 9
        pieceHost3.setState(active);

        //initializes the states
        gameState= serverGameLogic.getGameState();
        turnState= gameState.getTurnState();
        rollDiceState= turnState.getRollDiceState();
        firstRollState= rollDiceState.getFirstRollState();
        choosePieceState= turnState.getChoosePieceState();
        selectPieceState= choosePieceState.getSelectPieceState();
        powerCardState = turnState.getPowerCardState();
        playPowerCardState = turnState.getPlayPowerCardState();
        waitingPieceState =serverGameLogic.getGameState().getTurnState().getChoosePieceState().getWaitingPieceState();
        startPieceState = serverGameLogic.getGameState().getTurnState().getChoosePieceState().getStartPieceState();

        //initialize dies
        die1 = new Die(1);
        die2= new Die(2);
        die3 = new Die(3);
        die4 = new Die(4);
        die5 = new Die(5);
        die6 = new Die(6);

        handCards = new ArrayList<>();
        handCards.add(shieldCard);
        handCards.add(swapCard);
        handCards.add(turboCard);
    }

    @Test
    public void testRollNo6AndCanMove() {

    }@Test
    public void testRoll6AndCanMove() {

    }@Test
    public void testDieChance() {
        Die die = new Die();
        die.setDieModifier(1);

        ArrayList<Integer> one = new ArrayList<>();
        ArrayList<Integer> two = new ArrayList<>();
        ArrayList<Integer> three = new ArrayList<>();
        ArrayList<Integer> four = new ArrayList<>();
        ArrayList<Integer> five = new ArrayList<>();
        ArrayList<Integer> six = new ArrayList<>();
        for (int i = 0; i < 6000000; i++) {
            die.modify();
            switch (die.shuffle()) {
                case 1 -> one.add(1);
                case 2 -> two.add(2);
                case 3 -> three.add(3);
                case 4 -> four.add(4);
                case 5 -> five.add(5);
                case 6 -> six.add(6);
            }
        }

        //test with 2% range for the correct changes
        assertTrue(980000 < one.size() && one.size() < 1020000);
        assertTrue(980000 < two.size() && two.size() < 1020000);
        assertTrue(980000 < three.size() && three.size() < 1020000);
        assertTrue(980000 < four.size() && four.size() < 1020000);
        assertTrue(980000 < five.size() && five.size() < 1020000);
        assertTrue(980000 < six.size() && six.size() < 1020000);
    }

    @Test
    public void testRollNo6AndCantMove() {
    }
    @Test
    public void testRoll6AndCantMove() {
    }
    @Test
    public void testSelectOwnPieceBeforeRollingDice() {
    }
    @Test
    public void testSwitchPieceBeforeRollingDice() {
    }
    @Test
    public void testSelectOwnPieceAfterRollingDice() {
    }
    @Test
    public void testSwitchPieceAfterRollingDice() {
    }
    @Test
    public void testSelectOpponentPiece() {
    }
    @Test
    public void testSwitchOpponentPiece() {
    }
    @Test
    public void testDeselectPiece() {

    }
    @Test
    public void testSelectBonusCard() {

    }
@Test
    public void testDeselectBonusCard() {
    }
@Test
    public void testPlayTurboCard() {

    }
@Test
    public void testPlayShieldCard() {
    }
@Test
    public void testPlaySwapCard() {
    }
@Test
    public void testConfirmChoice() {
    }
}
