package etf.santorini.sm160425d.Logic;

import etf.santorini.sm160425d.GUI.BoardGUI;
import etf.santorini.sm160425d.boardstates.BoardState;
import etf.santorini.sm160425d.boardstates.InitialState;
import etf.santorini.sm160425d.file.MyFileWriter;

import java.util.ArrayList;

public class Board {

    public static final int rows = 5;
    public static final int cols = 5;
    public static Board currentBoard = null;
    public static int[] arrayRow = {-1, 0, 1};
    public static int[] arrayCol = {-1, 0, 1};

    public Token tokens[] = new Token[4];
    public int myScore;

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
                ret.matrix[i][j] = temp;
            }
        }

        for (int i = 0; i < 4; i++) {
            ret.tokens[i] = this.tokens[i].copy();
            ret.tokens[i].setMyField(ret.matrix[this.tokens[i].getMyField().getRow()][this.tokens[i].getMyField().getCol()]);
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

    public boolean gameOver() {
        Token[] tokens = Board.currentBoard.getPlayersTokens(Game.currentPlayer);
        if (tokens[0].getMyField().getMyHeight() == 3 || tokens[1].getMyField().getMyHeight() == 3) {
            return true;
        }

        return false;
    }                                                                       //check if current player reached lvl 3

    public static boolean gameOver(Board board) {
        for (int i = 0; i < 4; i++) {
            if (board.tokens[i].getMyField().getMyHeight() == 3)
                return true;
        }
        return false;
    }


    public static boolean playerWon(Board board, int player) {
        Token[] tokens = board.getPlayersTokens(player);
        return tokens[0].getMyField().getMyHeight() == 3 || tokens[1].getMyField().getMyHeight() == 3;
    }

    public static boolean currentPlayerWon() {
        Token[] tokens = Board.currentBoard.getPlayersTokens(Game.currentPlayer);
        return tokens[0].getMyField().getMyHeight() == 3 || tokens[1].getMyField().getMyHeight() == 3;
    }

    public static boolean playerHasAnyBuildsLeft(int player) {                                                                  //check if a player can move on the current board
        Token tokens[] = Board.currentBoard.getPlayersTokens(player);

        return tokens[0].hasMovesLeft(Board.currentBoard) || tokens[1].hasMovesLeft(Board.currentBoard);
    }                                                       //check if player has any moves left on the CURRENT BOARD

    public boolean playerHasAnyMovesLeft(int player) {
        Token tokens[] = this.getPlayersTokens(player);

        return tokens[0].hasMovesLeft(this) || tokens[1].hasMovesLeft(this);
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
                    MyFileWriter.getInstance().printToFile(row, col);
                }

            }
        }
        MyFileWriter.getInstance().printNLToFile();
    }

    public boolean AIFullMove() {

        MoveLeadingToThisBoard best;


        if (Game.algorithmSelected == 0)
            best = minimax(Board.currentBoard.copy(null), 0, true);
        else
            best = minimaxAB(Board.currentBoard.copy(null), 0, true, Integer.MIN_VALUE, Integer.MAX_VALUE);

        if (best == null) {
            this.getMyGame().gameGUI.setMessageLabelText("NEMA NAJBOOLJEG");
            return false;
        }

        if (best != null) {

            MyFileWriter.getInstance().printToFile(best.getRowFrom(), best.getColFrom());
            MyFileWriter.getInstance().printToFile(best.getRowTo(), best.getColTo());
            MyFileWriter.getInstance().printToFile(best.getRowBuilt(), best.getColBuilt());
            MyFileWriter.getInstance().printNLToFile();

        }


        Board.currentBoard.moveTokenFromTo(best.getRowFrom(), best.getColFrom(), best.getRowTo(), best.getColTo());
        Board.currentBoard.getFieldFrom(best.getRowBuilt(), best.getColBuilt()).increaseHeight();


        return true;
    }

    public static ArrayList<Board> generateAllBoards(Board board, int tempPlayer) {

        int maxProcena = Integer.MIN_VALUE;
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
                    possibleMoveBoards.add(copy);                                                                       //all boards only after movin


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

                    if (temp.getMoveLeadingToThisBoard().getTokenMoved() == 0) {


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


                    if (temp.getMoveLeadingToThisBoard().getTokenMoved() == 1) {

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

    public static MoveLeadingToThisBoard minimax(Board board, int depth, boolean isMax) {

        int score = 0;
        MoveLeadingToThisBoard temp;
        MoveLeadingToThisBoard bestMove = null;
        int tempPlayer;

        if (isMax) {                                                                                                    //if isMax player is current
            tempPlayer = Game.currentPlayer;
        } else {
            tempPlayer = Game.getNextPlayer();                                                                          //it is the other player
        }

        ArrayList<Board> allMoves = Board.generateAllBoards(board.copy(null), tempPlayer);

        if (depth == Game.maxDepth) {
            board.myScore = board.moveLeadingToThisBoard.calculateFunction(tempPlayer);
            return board.moveLeadingToThisBoard;
        }

        if (Board.gameOver(board)) {
            board.myScore = board.moveLeadingToThisBoard.calculateFunction(tempPlayer);
            return board.moveLeadingToThisBoard;
        }

        if (allMoves.isEmpty()) {                                                                                         //nema vise poteza
            if (tempPlayer == Game.currentPlayer) board.myScore -= 1000;
            else board.myScore = 1000;
            return board.moveLeadingToThisBoard;
        }


        if (isMax) {
            score = Integer.MIN_VALUE;

            for (Board b : allMoves) {
                temp = minimax(b, depth + 1, !isMax);

                if (temp.getBoard().myScore > score) {
                    score = temp.getBoard().myScore;
                    bestMove = b.moveLeadingToThisBoard;
                }

            }
        } else {
            score = Integer.MAX_VALUE;

            for (Board b : allMoves) {
                temp = minimax(b, depth + 1, !isMax);

                if (temp.getBoard().myScore < score) {
                    score = temp.getBoard().myScore;
                    bestMove = b.moveLeadingToThisBoard;
                }

            }
        }

        return bestMove;

    }

    public static MoveLeadingToThisBoard minimaxAB(Board board, int depth, boolean isMax, int alpha, int beta) {

        int score = 0;
        MoveLeadingToThisBoard temp;
        MoveLeadingToThisBoard bestMove = null;
        int tempPlayer;


        if (isMax) {                                                                                                    //if isMax player is current
            tempPlayer = Game.currentPlayer;
        } else {
            tempPlayer = Game.getNextPlayer();                                                                          //it is the other player
        }

        ArrayList<Board> allMoves = Board.generateAllBoards(board, tempPlayer);

        if (depth == Game.maxDepth) {
            board.myScore = board.moveLeadingToThisBoard.calculateFunction(tempPlayer);
            return board.moveLeadingToThisBoard;
        }

        if (Board.gameOver(board)) {
            board.myScore = board.moveLeadingToThisBoard.calculateFunction(tempPlayer);
            return board.moveLeadingToThisBoard;
        }

        if (allMoves.isEmpty()) {                                                                                         //nema vise poteza
            if (tempPlayer == Game.currentPlayer) board.myScore -= 100;
            else board.myScore += 100;
            return board.moveLeadingToThisBoard;
        }


        if (isMax) {
            score = Integer.MIN_VALUE;

            for (Board b : allMoves) {
                temp = minimaxAB(b, depth + 1, !isMax, alpha, beta);

                if (temp.getBoard().myScore > score) {
                    score = temp.getBoard().myScore;
                    bestMove = b.moveLeadingToThisBoard;
                }
                alpha = Math.max(alpha, score);
                if (beta <= alpha)
                    break;

            }
        } else {
            score = Integer.MAX_VALUE;

            for (Board b : allMoves) {
                temp = minimaxAB(b, depth + 1, !isMax, alpha, beta);

                if (temp.getBoard().myScore < score) {
                    score = temp.getBoard().myScore;
                    bestMove = b.moveLeadingToThisBoard;
                }

                beta = Math.min(beta, score);
                if (beta <= alpha)
                    break;

            }
        }

        return bestMove;

    }

    //optimization methods
    public int numberOfWinningMoves(int player) {
        Token tokens[] = this.getPlayersTokens(Game.getNextPlayer());
        int ret = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int checkRow = tokens[0].getMyField().getRow() + Board.arrayRow[i];
                int checkCol = tokens[0].getMyField().getRow() + Board.arrayRow[j];

                if (checkRow > 0 && checkCol > 0 && checkCol < Board.cols && checkRow < Board.rows) {
                    if (this.getFieldHeight(checkRow, checkCol) == 3)
                        ret++;
                }

                checkRow = tokens[1].getMyField().getRow() + Board.arrayRow[i];
                checkCol = tokens[1].getMyField().getRow() + Board.arrayRow[j];

                if (checkRow > 0 && checkCol > 0 && checkCol < Board.cols && checkRow < Board.rows) {
                    if (this.getFieldHeight(checkRow, checkCol) == 3)
                        ret++;
                }
            }
        }
        return ret;
    }

    public boolean isPlayerWinningMove(int row, int col, int player) {
        Token[] tokens = this.getPlayersTokens(player);

        if (tokens[0].possibleMove(row, col, this) && this.getFieldHeight(row, col) == 3)
            return true;


        if (tokens[1].possibleMove(row, col, this) && this.getFieldHeight(row, col) == 3)
            return true;

        return false;
    }

    public boolean jumpable(int row, int col, int player) {
        Token[] tokens = this.getPlayersTokens(player);

        if (tokens[0].possibleMove(row, col, this))
            return true;


        if (tokens[1].possibleMove(row, col, this))
            return true;

        return false;
    }

    //game load method

    public static void loadGame(String game) {
        String moves[] = game.split(" ");



        if (moves.length < 3)
            return;
        int row = -1;
        int col = -1;
        int cnt = 0;
        for (int i = 0; i < 4; i++) {
            if (i >= 2)
                cnt = 1;
            row = Board.cToInt(moves[i].charAt(0));
            col = Integer.parseInt(String.valueOf(moves[i].charAt(1)));
            Board.currentBoard.tokens[i] = new Token(Board.currentBoard.getFieldFrom(row, col), cnt);
            Board.currentBoard.putToken(row, col, Board.currentBoard.tokens[i]);
            System.out.println(row + " "+ col);
        }

        int rowFrom;
        int rowTo;
        int rowBuilt;
        int colFrom;
        int colTo;
        int colBuilt;
        for (int i = 3; i <= moves.length - 3; i += 3) {
            rowFrom = Board.cToInt(moves[i].charAt(0));
            colFrom = Integer.parseInt(String.valueOf(moves[i].charAt(1)));
            rowTo = Board.cToInt(moves[i + 1].charAt(0));
            colTo = Integer.parseInt(String.valueOf(moves[i + 1].charAt(1)));
            rowBuilt = Board.cToInt(moves[i + 2].charAt(0));
            colBuilt = Integer.parseInt(String.valueOf(moves[i + 2].charAt(1)));

            System.out.println(rowFrom + " " + colFrom + " " + rowTo + " " + colTo);

            Board.currentBoard.moveTokenFromTo(rowFrom, colFrom, rowTo, colTo);
            Board.currentBoard.getFieldFrom(rowBuilt, colBuilt).increaseHeight();
            cnt++;
        }
        Game.currentPlayer = cnt % 2;
    }

    public static int cToInt(Character s) {
        if (s == 'A')
            return 0;
        if (s == 'B')
            return 1;
        if (s == 'C')
            return 2;
        if (s == 'D')
            return 3;
        if (s == 'E')
            return 4;
        return -1;
    }


}
