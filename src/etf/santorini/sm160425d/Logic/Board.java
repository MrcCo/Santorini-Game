package etf.santorini.sm160425d.Logic;

import etf.santorini.sm160425d.GUI.BoardGUI;
import etf.santorini.sm160425d.boardstates.BoardState;
import etf.santorini.sm160425d.boardstates.Finished;
import etf.santorini.sm160425d.boardstates.InitialState;

import java.awt.event.MouseListener;
import java.util.ArrayList;

import static java.lang.System.exit;

public class Board {

    public static final int rows = 5;
    public static final int cols = 5;
    public static Board currentBoard = null;
    public static int[] arrayRow = {-1, 0, 1};
    public static int[] arrayCol = {-1, 0, 1};

    public Token tokens[] = new Token[4];

    private Field matrix[][] = new Field[rows][cols];
    private BoardGUI myBoardGUI;
    private Game myGame;
    private BoardState currentBoardState;

    public Board(Game game) {
        myGame = game;
        myBoardGUI = new BoardGUI(this);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = new Field(this, i, j);
                myBoardGUI.add(i, j, matrix[i][j].getFieldGUI());
            }
        }
        currentBoardState = InitialState.getInstance();
    }

    //COPY AND MINIMAX RELATED METHODS
    private MoveLeadingToThisBoard moveLeadingToThisBoard;

    public MoveLeadingToThisBoard getMoveLeadingToThisBoard() {
        return moveLeadingToThisBoard;
    }

    public Board(MoveLeadingToThisBoard mldttb) {

        this.moveLeadingToThisBoard = mldttb;
        matrix = new Field[rows][cols];
        tokens = new Token[4];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = null;
            }
        }


    }

    public Board copy(MoveLeadingToThisBoard mlttb) {
        Board ret = new Board(mlttb);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Field temp = this.getFieldFrom(i, j).copy(ret);

                if (temp.getMyToken() != null) {
                    Token t = temp.getMyToken();
                    if (t.getPlayer() == 0) {
                        if (ret.tokens[0] == null) {
                            ret.tokens[0] = t;
                            ret.tokens[0].setMyField(temp);
                        } else {
                            ret.tokens[1] = t;
                            ret.tokens[1].setMyField(temp);
                        }
                    } else {
                        if (ret.tokens[2] == null) {
                            ret.tokens[2] = t;
                            ret.tokens[2].setMyField(temp);
                        } else {
                            ret.tokens[3] = t;
                            ret.tokens[3].setMyField(temp);
                        }
                    }
                }

                ret.setFieldOn(i, j, temp);
            }
        }

        return ret;
    }

    public void setMoveLeadingToThisBoard(MoveLeadingToThisBoard mlttb) {
        this.moveLeadingToThisBoard = mlttb;
    }

    //TOKEN AND PLAYER RELATED METHODS
    public static void moveTokenFromTo(int rowFrom, int colFrom, int rowTo, int colTo, Board board) {                                        //used in minimax
        board.matrix[rowFrom][colFrom].getMyToken().move(rowTo, colTo, board);
    }

    public void moveTokenFromTo(int rowFrom, int colFrom, int rowTo, int colTo) {
        Board.currentBoard.matrix[rowFrom][colFrom].getMyToken().move(rowTo, colTo);
    }


    public Token[] getPlayersTokens(int player) {
        Token cTokens[] = new Token[2];

        if (player == 0) {
            cTokens[0] = this.tokens[0];
            cTokens[1] = this.tokens[1];
        } else {
            cTokens[0] = this.tokens[2];
            cTokens[1] = this.tokens[3];
        }

        return cTokens;
    }

    public static boolean currentPlayerWon(){
        Token[] tokens = Board.currentBoard.getPlayersTokens(Game.currentPlayer);
        if(tokens[0].getMyField().getMyHeight() == 3 || tokens[1].getMyField().getMyHeight() == 3 ){
            return true;
        }

        if()
        return false;
    }                                                                       //check if current player reached lvl 3

    public static boolean playerHasAnyBuildsLeft(int player) {                                                                  //check if a player can move on the current board
        Token tokens[] = Board.currentBoard.getPlayersTokens(player);

        return tokens[0].hasMovesLeft(Board.currentBoard) || tokens[1].hasMovesLeft(Board.currentBoard);
    }                                                       //check if player has any moves left on the CURRENT BOARD

    public boolean playerHasAnyMovesLeft(int player) {
        Token tokens[] = this.getPlayersTokens(player);

        return tokens[0].hasMovesLeft(this) || tokens[0].hasMovesLeft(this);
    }                                                               //checks if player can move on any board

    public boolean putToken(int row, int col, Token token) {
        if (matrix[row][col].isFree()) {
            matrix[row][col].putToken(token);
            return true;
        }
        return false;
    }

    //getters and setters
    public BoardGUI getMyBoardGUI() {
        return myBoardGUI;
    }

    public Game getMyGame() {
        return myGame;
    }

    public void setCurrentBoardState(BoardState bs) {                                                                    //sets current board state
        this.currentBoardState = bs;
    }

    public void boardOperation(int row, int col) {
        currentBoardState.boardOperation(row, col);
    }

    public Field getFieldFrom(int i, int j) {                                                                            //gets i,j field
        if (i < 0 || i > rows || j < 0 || j > cols) {
            return null;
        }
        return matrix[i][j];
    }

    public int getFieldHeight(int i, int j) {
        return matrix[i][j].getMyHeight();
    }

    public void setFieldOn(int i, int j, Field field) {
        if (i >= 0 && i < rows && j >= 0 && j < cols)
            this.matrix[i][j] = field;
    }


    //AI METHODS
    public void AIInitialPicks() {
        int count = 0;
        while (count < 2) {                                                                                               //generate
            int row = (int) (Math.random() * 5);
            int col = (int) (Math.random() * 5);

            if (Board.currentBoard.getFieldFrom(row, col).isFree()) {                                                 //just to make sure

                Token token = new Token(Board.currentBoard.getFieldFrom(row, col), Game.currentPlayer);                                                                                   //create new token
                Board.currentBoard.tokens[Game.currentPlayer * 2 + count] = token;

                if (Board.currentBoard.putToken(row, col, token)) {                                                 //if i manage to put a token on the board increase num of picks
                    count++;
                }

            }
        }
    }

    public boolean AIFullMove() {

        MoveLeadingToThisBoard best = Board.currentBoard.copy(null).getTheBestMove();

        if (best == null) {
            System.out.println("NEMA NAJBOOLJEG");
            return false;
        }

        System.out.println(best);

        Board.currentBoard.moveTokenFromTo(best.getRowFrom(), best.getColFrom(), best.getRowTo(), best.getColTo());
        Board.currentBoard.getFieldFrom(best.getRowBuilt(), best.getColBuilt()).increaseHeight();

        return true;
    }

    public static ArrayList<Board> generateAllBoards(Board board, int tempPlayer) {

        ArrayList<Board> possibleMoveBoards = new ArrayList<Board>();
        Board copy;
        Token tokens[];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {

                copy = board.copy(null);
                tokens = copy.getPlayersTokens(tempPlayer);

                if (tokens[0] == null || tokens[1] == null) {
                    System.out.println("NEST NIJE OK");
                }

                int newRow = tokens[0].getMyField().getRow() + Board.arrayRow[i];
                int newCol = tokens[0].getMyField().getCol() + Board.arrayCol[j];


                int oldRow = tokens[0].getMyField().getRow();
                int oldCol = tokens[0].getMyField().getCol();

                if (tokens[0].move(newRow, newCol, copy)) {

                    MoveLeadingToThisBoard newMove = new MoveLeadingToThisBoard(null);                                      //collect movement data
                    newMove.setRowFrom(oldRow);
                    newMove.setColFrom(oldCol);
                    newMove.setRowTo(tokens[0].getMyField().getRow());
                    newMove.setColTo(tokens[0].getMyField().getCol());
                    newMove.setTokenMoved(0);

                    copy.setMoveLeadingToThisBoard(newMove);
                    newMove.setBoard(copy);

                    possibleMoveBoards.add(copy);                                                                       //all boards only after moving

                }
            }
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {

                copy = board.copy(null);
                tokens = copy.getPlayersTokens(tempPlayer);

                int newRow = tokens[1].getMyField().getRow() + Board.arrayRow[i];
                int newCol = tokens[1].getMyField().getCol() + Board.arrayCol[j];

                int oldRow = tokens[1].getMyField().getRow();
                int oldCol = tokens[1].getMyField().getCol();

                if (tokens[1].move(newRow, newCol, copy)) {

                    MoveLeadingToThisBoard newMove = new MoveLeadingToThisBoard(null);
                    newMove.setRowFrom(oldRow);
                    newMove.setColFrom(oldCol);
                    newMove.setRowTo(tokens[1].getMyField().getRow());
                    newMove.setColTo(tokens[1].getMyField().getCol());
                    newMove.setTokenMoved(1);
                    copy.setMoveLeadingToThisBoard(newMove);
                    newMove.setBoard(copy);
                    possibleMoveBoards.add(copy);                                                                       //all boards only after moving

                }
            }
        }


        ArrayList<Board> allMoves = new ArrayList<Board>();

        while (!possibleMoveBoards.isEmpty()) {

            Board temp = possibleMoveBoards.get(0);
            possibleMoveBoards.remove(0);

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {

                    copy = temp.copy(null);
                    tokens = copy.getPlayersTokens(tempPlayer);

                    int newRow = tokens[0].getMyField().getRow() + Board.arrayRow[i];
                    int newCol = tokens[0].getMyField().getCol() + Board.arrayCol[j];

                    if (temp.getMoveLeadingToThisBoard().getTokenMoved() != 1) {


                        if (tokens[0].build(newRow, newCol, copy)) {
                            MoveLeadingToThisBoard newMove = new MoveLeadingToThisBoard(null);
                            newMove.setRowFrom(temp.getMoveLeadingToThisBoard().getRowFrom());
                            newMove.setColFrom(temp.getMoveLeadingToThisBoard().getColFrom());
                            newMove.setRowTo(temp.getMoveLeadingToThisBoard().getRowTo());
                            newMove.setColTo(temp.getMoveLeadingToThisBoard().getColTo());

                            newMove.setRowBuilt(tokens[0].getMyField().getRow() + Board.arrayRow[i]);
                            newMove.setColBuilt(tokens[0].getMyField().getCol() + Board.arrayCol[j]);

                            copy.setMoveLeadingToThisBoard(newMove);

                            newMove.setBoard(copy);
                            allMoves.add(copy);

                        }
                    }
                }
            }

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {

                    copy = temp.copy(null);
                    tokens = copy.getPlayersTokens(tempPlayer);

                    int newRow = tokens[1].getMyField().getRow() + Board.arrayRow[i];
                    int newCol = tokens[1].getMyField().getCol() + Board.arrayCol[j];


                    if (temp.getMoveLeadingToThisBoard().getTokenMoved() != 0) {

                        if (tokens[1].build(newRow, newCol, copy)) {
                            MoveLeadingToThisBoard newMove = new MoveLeadingToThisBoard(null);
                            newMove.setRowFrom(temp.getMoveLeadingToThisBoard().getRowFrom());
                            newMove.setColFrom(temp.getMoveLeadingToThisBoard().getColFrom());
                            newMove.setRowTo(temp.getMoveLeadingToThisBoard().getRowTo());
                            newMove.setColTo(temp.getMoveLeadingToThisBoard().getColTo());
                            newMove.setRowBuilt(newRow);
                            newMove.setColBuilt(newCol);

                            copy.setMoveLeadingToThisBoard(newMove);
                            newMove.setBoard(copy);

                            allMoves.add(copy);
                        }
                    }
                }

            }
        }
        return allMoves;
    }

    public static int minimax(Board board, int depth, boolean isMax) {

        if (depth == Game.maxDepth) {                                 //terminal state
            return board.moveLeadingToThisBoard.calculateFunction();
        }

        int tempPlayer;

        if (isMax) {                                                                                                      //if isMax player is current
            tempPlayer = Game.currentPlayer;
        } else {
            tempPlayer = (Game.currentPlayer + 1) % 2;                                                                //it is the other player
        }

        ArrayList<Board> allMoves = Board.generateAllBoards(board, tempPlayer);

        int best = 0;
        if (isMax) {
            best = Integer.MIN_VALUE;

            for (Board b : allMoves) {
                best = Math.max(best, minimax(b, depth + 1, !isMax));
            }
        } else {
            best = Integer.MAX_VALUE;

            for (Board b : allMoves) {
                best = Math.min(best, minimax(b, depth + 1, !isMax));
            }
        }

        return best;

    }

    public static int minimaxAB(Board board, int depth, boolean isMax, int alpha, int beta) {

        if (depth == Game.maxDepth) {                                 //terminal state
            return board.moveLeadingToThisBoard.calculateFunction();
        }

        int tempPlayer;

        if (isMax) {                                                                                                      //if isMax player is current
            tempPlayer = Game.currentPlayer;
        } else {
            tempPlayer = (Game.currentPlayer + 1) % 2;                                                                //it is the other player
        }

        ArrayList<Board> allMoves = Board.generateAllBoards(board, tempPlayer);

        int best = 0;
        int test = 0;
        if (isMax) {
            best = Integer.MIN_VALUE;

            for (Board b : allMoves) {
                test = Math.max(best, minimaxAB(b, depth + 1, !isMax, alpha, beta));
                best = Math.max(best, test);
                alpha = Math.max(alpha, best);
                if (beta <= alpha)
                    break;
            }
        } else {
            best = Integer.MAX_VALUE;

            for (Board b : allMoves) {
                best = Math.min(best, minimax(b, depth + 1, !isMax));
                test = Math.min(best, minimaxAB(b, depth + 1, !isMax, alpha, beta));
                best = Math.min(best, test);
                beta = Math.min(beta, best);
                if (beta <= alpha)
                    break;
            }
        }

        return best;

    }


    public MoveLeadingToThisBoard getTheBestMove() {

        Board temp = Board.currentBoard.copy(null);
        ArrayList<Board> allMoves = temp.generateAllBoards(temp, Game.currentPlayer);

        int best = Integer.MIN_VALUE;
        Board bestBoard = null;

        for (Board iter : allMoves) {
            //int val = Board.minimax(iter, 0, true);
            int val = Board.minimaxAB(iter, 0, true, Integer.MIN_VALUE, Integer.MAX_VALUE);
            if (val > best) {
                best = val;
                bestBoard = iter;
            }
        }
        if (bestBoard != null) {
            System.out.println("My next move is:" + bestBoard.getMoveLeadingToThisBoard());
            return bestBoard.getMoveLeadingToThisBoard();
        } else {
            return null;
        }
    }

}
