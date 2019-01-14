package etf.santorini.sm160425d.Logic;

public class MoveLeadingToThisBoard {
    private int rowFrom, colFrom, rowTo, colTo, rowBuilt, colBuilt;
    private Board board;
    private int tokenMoved;
    private int tempPlayer;

    public int getTokenMoved() {
        return tokenMoved;
    }

    public void setTokenMoved(int tokenMoved) {
        this.tokenMoved = tokenMoved;
    }

    public MoveLeadingToThisBoard(Board board) {
        this.board = board;
    }

    public int getRowFrom() {
        return rowFrom;
    }

    public void setRowFrom(int rowFrom) {
        this.rowFrom = rowFrom;
    }

    public int getColFrom() {
        return colFrom;
    }

    public void setColFrom(int colFrom) {
        this.colFrom = colFrom;
    }

    public int getRowTo() {
        return rowTo;
    }

    public void setRowTo(int rowTo) {
        this.rowTo = rowTo;
    }

    public int getColTo() {
        return colTo;
    }

    public void setColTo(int colTo) {
        this.colTo = colTo;
    }

    public int getRowBuilt() {
        return rowBuilt;
    }

    public void setRowBuilt(int rowBuilt) {
        this.rowBuilt = rowBuilt;
    }

    public int getColBuilt() {
        return colBuilt;
    }

    public void setColBuilt(int colBuilt) {
        this.colBuilt = colBuilt;
    }


    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public int calculateFunction(int tempPlayer) {

        int score = 0;

        Token[] myTokens = board.getPlayersTokens(Game.currentPlayer);
        Token[] opTokens = board.getPlayersTokens(Game.getNextPlayer());


        int myTokenZeroDistance = myTokens[0].getMyField().getRow() + myTokens[0].getMyField().getCol() - rowBuilt - colBuilt;
        int myTokenOneDistance = myTokens[1].getMyField().getRow() + myTokens[1].getMyField().getCol() - rowBuilt - colBuilt;
        int opTokenZeroDistance = opTokens[0].getMyField().getRow() + opTokens[0].getMyField().getCol() - rowBuilt - colBuilt;
        int opTokenOneDistance = opTokens[1].getMyField().getRow() + opTokens[1].getMyField().getCol() - rowBuilt - colBuilt;

        int l = board.getFieldHeight(rowBuilt, colBuilt) * (myTokenZeroDistance + myTokenOneDistance - opTokenZeroDistance - opTokenOneDistance);                   //project prediction


        //encourage my tokens to go up
        if (myTokens[0].getMyField().getMyHeight() == 3)
            score += 1000;
        if (myTokens[0].getMyField().getMyHeight() == 2)
            score += 300;
        if (myTokens[0].getMyField().getMyHeight() == 1)
            score += 30;
        if (myTokens[1].getMyField().getMyHeight() == 3)
            score += 1000;
        if (myTokens[1].getMyField().getMyHeight() == 2)
            score += 300;
        if (myTokens[1].getMyField().getMyHeight() == 1)
            score += 30;

        //discourage oponent
        if (opTokens[0].getMyField().getMyHeight() == 3)
            score -= 1000;
        if (opTokens[0].getMyField().getMyHeight() == 2)
            score -= 100;
        if (opTokens[0].getMyField().getMyHeight() == 1)
            score -= 30;
        if (opTokens[1].getMyField().getMyHeight() == 3)
            score -= 1000;
        if (opTokens[1].getMyField().getMyHeight() == 2)
            score -= 100;
        if (opTokens[1].getMyField().getMyHeight() == 1)
            score -= 30;


        if (tempPlayer == Game.currentPlayer) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    int newRow = rowTo + Board.arrayRow[i];
                    int newCol = colTo + Board.arrayRow[j];
                    if (myTokens[0].possibleMove(newRow, newCol, board)) {
                        score += 20 * board.getFieldHeight(newRow, newCol);                                             //encourage climbing
                        if (board.getFieldHeight(newRow, newCol) == 3)                                                  //encourage winning even more
                            score += 400;
                    } else {
                        if (myTokens[1].possibleMove(newRow, newCol, board)) {
                            score += 20 * board.getFieldHeight(newRow, newCol);
                            if (board.getFieldHeight(newRow, newCol) == 3)
                                score += 400;
                        } else {
                            score -= 20;                                                                                //discourage unsuccessful moves
                        }
                    }

                }
            }
            score -= board.numberOfWinningMoves(Game.getNextPlayer()) * 200;                                             //discourage a move that has oponent winning
        }else{
            l = -1 * l;
        }

        return score + l;
    }

    //for debug only
    @Override
    public String toString() {
        return "FROM ROW " + rowFrom + " FROM COL " + colFrom + "\nTO ROW " + rowTo + " TO COL " + colTo + "\nBUILD ROW "
                + rowBuilt + " BUILD COL " + colBuilt + "\n FJA PROCENE " + this.calculateFunction(tempPlayer) + "\n";
    }
}
