package pp.mdga.server.automaton.game.turn.choosepiece;

import pp.mdga.game.*;
import pp.mdga.server.ServerGameLogic;
import pp.mdga.server.automaton.ServerState;
import pp.mdga.server.automaton.game.turn.ChoosePieceState;

public abstract class ChoosePieceAutomatonState extends ServerState {
    /**
     * Create ChoosePieceAutomatonState attributes.
     */
    protected final ChoosePieceState choosePieceAutomaton;
    private final System.Logger LOGGER = System.getLogger(ChoosePieceAutomatonState.class.getName());

    /**
     * Constructs a server state of the specified game logic.
     *
     * @param choosePieceAutomaton as the automaton of the choose piece state as a ChoosePieceState object.
     * @param logic                the game logic
     */
    public ChoosePieceAutomatonState(ChoosePieceState choosePieceAutomaton, ServerGameLogic logic) {
        super(logic);
        this.choosePieceAutomaton = choosePieceAutomaton;
    }

    /**
     * returns true, if the piece can move inside the home or infield or move from the infield to the home
     *
     * @param piece the piece that will be checked
     * @return true, if the piece can move
     */
    protected boolean canMove(Piece piece) {
        System.out.println("Server: reached canMove() for piece: " + piece);
        int steps = logic.getGame().getDiceModifier() * logic.getGame().getDiceEyes();
        int pieceIdx = logic.getGame().getBoard().getInfieldIndexOfPiece(piece);
        int startIdx = logic.getGame().getActivePlayer().getStartNodeIndex();
        int normPieceIdx = (-startIdx + pieceIdx + 40) % 40;
        int targetIdx = normPieceIdx + steps;

        //checks if the piece can move in the home
        if (piece.getState().equals(PieceState.HOME)) {
            if (canPieceMoveInHome(piece, steps)) {
                return true;
            }
        } else {
            if (canPieceMoveInHome(piece, steps)) {
                return true;
            }
            //checks if the piece will not go over its home-field
            else if (40 > targetIdx && (!piece.getState().equals(PieceState.HOME) || !piece.getState().equals(PieceState.HOMEFINISHED))) {
                Color activeColor = logic.getGame().getActiveColor();
                Node tartgetNode = logic.getGame().getBoard().getInfield()[(pieceIdx + steps) % 40];
                //checks if the target-node is not occupied by an own color
                if (!tartgetNode.isOccupied(activeColor)) {
                    //checks if the targetNode is not occupied or the occupant ha no shield
                    if (tartgetNode.isOccupied()) {
                        if (tartgetNode.getOccupant().getShield().equals(ShieldState.ACTIVE)) {
                            System.out.println("Server: targetNode.getOccupant().getShield().equals(ShieldState.ACTIVE" + tartgetNode.getOccupant().getShield().equals(ShieldState.ACTIVE));
                            return false;
                        }
                    }
                    return true;
                }
            }
            //returns false it the piece can't move
            return false;
        }
        return false;
    }

    /**
     * tests if the piece can move in the home or inside the home
     *
     * @param piece
     * @param steps
     * @return false, if the piece can't move in the home
     */
    protected boolean canPieceMoveInHome(Piece piece, int steps) {
        //tests if the piece can move inside the home
        steps = logic.getGame().getDiceModifier() * logic.getGame().getDiceEyes();
        System.out.println("Server: reached canPieceMoveInHome for piece: " + piece + " and the steps: " + steps);

        if (piece.getState().equals(PieceState.HOME)) {
            int homeIdx = logic.getGame().getActivePlayer().getHomeIndexOfPiece(piece);
            System.out.println("Server: reached canPieceMoveInHome for piece: " + piece + " and the steps: " + steps + " and the homeIndex: " + homeIdx + " and the if-clause ((3 - homeIdx) >= steps - 1): " + ((3 - homeIdx) >= steps - 1));
            //tests if the steps are less than the possible movement
            if ((3 - homeIdx) >= steps - 1) {
                return !jumpOver(steps, homeIdx, false);
            } else {
                return false;
            }
        }
        //tests if the piece can move in the home
        else if (piece.getState() == PieceState.ACTIVE) {
            int pieceIdx = logic.getGame().getBoard().getInfieldIndexOfPiece(piece);
            int startIdx = logic.getGame().getActivePlayer().getStartNodeIndex();
            int normPieceIdx = (-startIdx + pieceIdx + 40) % 40;
            int targetIdx = normPieceIdx + steps;
            System.out.println("Server: canPieceHomeMove: else: Active: with pieceIndex: " + pieceIdx + " and the steps: " + steps + " and startIdx: " + startIdx + " and targetIdx: " + targetIdx + " and the if-statement (targetIdx >= 40): " + (targetIdx >= 40));
            if (targetIdx >= 40) {
                int stepsToHome = 39 - normPieceIdx;
                int restMovement = steps - stepsToHome - 1;
                System.out.println("Server: canPieceHomeMove:else: restMovement: " + restMovement + " and the if-clause(restMovement >= 3):" + (restMovement >= 3));
                if (restMovement >= 4) return false;
                return !jumpOver(restMovement, 0, true);
            }
        } else {
            return false;
        }
        return false;
    }

    /**
     * tests if the piece, when moved in or inside a home, must jump over another piece
     *
     * @param stepsInHome the steps walked inside the home
     * @param homeIdx     the index of the piece inside, or 0 if come from outside
     * @param outside     a boolean, if the piece moves inside a house or in a house
     * @return false, if there is no piece to jump over
     */
    private boolean jumpOver(int stepsInHome, int homeIdx, boolean outside) {
        //tests if the piece comes from the outside in the home
        if (outside) {
            System.out.println("Server: jumpOver: for the homeIndex: " + homeIdx + " ,stepsInHome: " + stepsInHome + " , outside: " + outside + " annd the targetIndex: " + stepsInHome);
            if (stepsInHome > 3) return true;
            if (logic.getGame().getActivePlayer().getHomeNodes()[stepsInHome].isOccupied()) return true;
            for (int i = 0; i <= stepsInHome; i++) {
                if (logic.getGame().getActivePlayer().getHomeNodes()[i].isOccupied()) return true;
            }
        }
        //tests if the piece jumps over a piece from inside the home
        else {
            int targetIndex = stepsInHome + homeIdx;
            System.out.println("Server: jumpOver: for the homeIndex: " + homeIdx + " ,stepsInHome: " + stepsInHome + " , outside: " + outside + " and the targetIndex: " + targetIndex);
            if (targetIndex > 3) return true;
            if (logic.getGame().getActivePlayer().getHomeNodes()[targetIndex].isOccupied()) return true;
            for (int i = 1 + homeIdx; i <= targetIndex; i++) {
                if (logic.getGame().getActivePlayer().getHomeNodes()[i].isOccupied()) return true;
            }
        }
        return false;
    }

    /**
     * returns the index in the home, where the piece will go in the home
     *
     * @param piece the give piece
     * @return the index in the home, where the piece will go
     */
    protected int getHomeTargetIdx(Piece piece, int steps) {
        //tests if the piece is active and move in the house
        if (piece.getState() == PieceState.ACTIVE) {
            //gets the id's
            int pieceIdx = logic.getGame().getBoard().getInfieldIndexOfPiece(piece);
            int startIdx = logic.getGame().getActivePlayer().getStartNodeIndex();
            //normalize the idx
            int normPieceIdx = (-startIdx + pieceIdx + 40) % 40;
            //calculate the steps to the home
            int stepsToHome = 39 - normPieceIdx;
            //calculates the rest-movement inside a home
            int restMovement = steps - stepsToHome - 1;
            System.out.println("Server: getHomeTargetIndex for the piece: " + piece + " with the index: " + restMovement);
            return restMovement;
        }
        //the else handles the logic if the piece is in the home and moves inside the house
        else {
            int pieceHomeIdx = logic.getGame().getActivePlayer().getHomeIndexOfPiece(piece);
            System.out.println("Server: getHomeTargetIndex for the piece: " + piece + " with the index: " + (pieceHomeIdx + steps));
            return pieceHomeIdx + steps;
        }
    }

    /**
     * returns the target index inside the infield
     *
     * @param piece the piece given
     * @return the index in the infield
     */
    protected int getInfieldTarget(Piece piece, int steps) {
        int index = logic.getGame().getBoard().getInfieldIndexOfPiece(piece);
        System.out.println("Server: calculated the targetIndex in the Infield for:" + piece + "with the value" + ((steps + index) % 40));
        return (steps + index) % 40;
    }

    /**
     * this method is used for calculating the targetIndex
     *
     * @param piece the piece give
     * @return the index
     */
    protected int calculateTargetIndex(Piece piece) {
        int steps = logic.getGame().getDiceModifier() * logic.getGame().getDiceEyes();
        if (canPieceMoveInHome(piece, steps)) {
            System.out.println("Server: ChoosePieceStateAutomaton: calculate index in home:" + getHomeTargetIdx(piece, steps));
            return getHomeTargetIdx(piece, steps);
        }
        return getInfieldTarget(piece, steps);
    }
}
