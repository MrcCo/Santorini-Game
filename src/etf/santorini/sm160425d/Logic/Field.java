package etf.santorini.sm160425d.Logic;

import etf.santorini.sm160425d.GUI.*;

public class Field {

    private static final int maxHeight = 5;

    private FieldGUI myFieldGUI;
    private int row, col;
    private int myHeight;
    private Board myBoard;
    private Token myToken = null;
    private boolean free;

    public Field(Board board, int row, int col) {
        this.myBoard = board;
        this.row = row;
        this.col = col;
        this.free = true;
        this.myHeight = 0;
        myFieldGUI = new FieldGUI(this);
    }

    //copy method
    public Field(Board board, int row, int col, boolean free, int height, Token token) {
        this.row = row;
        this.col = col;
        this.free = free;
        this.myBoard = board;
        this.myToken = token;
        this.myHeight = height;
    }

    public Field copy(Board board) {
        Field ret;
        if (this.myToken != null)
            ret = new Field(board, this.row, this.col, this.free, this.myHeight, this.myToken.copy());
        else
            ret = new Field(board, this.row, this.col, this.free, this.myHeight, null);
        return ret;
    }

    //getters and setters
    public FieldGUI getFieldGUI() {
        return myFieldGUI;
    }

    public void increaseHeight() {
        if (myHeight <= 3) {
            myHeight++;
            if (myFieldGUI != null)
                myFieldGUI.putBlock();
        }
    }

    public int getMyHeight() {
        return myHeight;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public boolean isFree() {
        return this.free;
    }

    public void setTaken() {                                                                                             //sets free flag to false
        free = false;
    }

    public void setFree() {                                                                                              //sets free flag to true
        free = true;
    }

    public Board getBoard() {
        return myBoard;
    }

    public boolean putToken(Token token) {                                                                               //puts token on the field

        this.myToken = token;
        this.setTaken();

        if (myFieldGUI != null)
            myFieldGUI.putToken(token.getMyTokenGUI());

        return true;

    }

    public boolean removeToken() {                                                                                       //removes the token from the field

        if (myToken != null) {
            this.myToken = null;
            this.setFree();
            return true;
        }

        return false;
    }

    public Token getMyToken() {
        return myToken;
    }

}
