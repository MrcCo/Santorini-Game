package etf.santorini.sm160425d.Logic;

public class MoveLeadingToThisBoard {
    private int rowFrom, colFrom, rowTo, colTo, rowBuilt, colBuilt;
    private Board board;
    private int tokenMoved;

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

    public int calculateFunction() {
        int m = board.getFieldHeight(rowTo, colTo);

        if (board.getFieldHeight(rowTo, colTo) == 3)
            m += 1000;

        int distTokenZero = Math.abs(board.tokens[0].getMyField().getCol() - colBuilt) + Math.abs(board.tokens[0].getMyField().getRow() - rowBuilt);
        int distTokenOne = Math.abs(board.tokens[1].getMyField().getCol() - colBuilt) + Math.abs(board.tokens[1].getMyField().getRow() - rowBuilt);
        int distTokenTwo = Math.abs(board.tokens[2].getMyField().getCol() - colBuilt) + Math.abs(board.tokens[2].getMyField().getRow() - rowBuilt);
        int distTokenThree = Math.abs(board.tokens[3].getMyField().getCol() - colBuilt) + Math.abs(board.tokens[3].getMyField().getRow() - rowBuilt);

        int l = distTokenZero + distTokenOne - distTokenTwo - distTokenThree;

        if (Game.currentPlayer == 1) {
            l = -1 * l;
        }

        l *= board.getFieldHeight(rowBuilt, colBuilt);

        return m + l;
    }

    @Override
    public String toString() {
        return "FROM ROW " + rowFrom + " FROM COL " + colFrom + "\nTO ROW " + rowTo + " TO COL " + colTo + "\nBUILD ROW " + rowBuilt + " BUILD COL " + colBuilt + "\n";
    }
}
