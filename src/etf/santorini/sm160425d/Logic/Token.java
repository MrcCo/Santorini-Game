package etf.santorini.sm160425d.Logic;

import etf.santorini.sm160425d.GUI.TokenGUI;

public class Token {

    private Field myField;                                                                                              //the field i am on
    private int myPlayer;                                                                                               //the player i belong to
    //private Token brother;
    private TokenGUI myTokenGUI;

    public Token(Field myField, int player){

        this.myPlayer = player;
        this.myField = myField;
        myTokenGUI = new TokenGUI(this);

    }


    public Token(int player){                                                                                           //for copy purposes
        this.myPlayer = player;
        this.myField = null;
        this.myTokenGUI = null;
    }

    public Token copy(){
        Token t = new Token(this.myPlayer);
        return t;
    }

    //getters and setters
    public int getPlayer(){
        return myPlayer;
    }
    public TokenGUI getMyTokenGUI(){
        return  myTokenGUI;
    }
    public Field getMyField(){
        return  myField;
    }
    public void setMyField(Field myField){ this.myField = myField; }


    public void highlight(){
        this.myTokenGUI.highlight();
    }
    public void lowlight(){
        this.myTokenGUI.lowlight();
    }

    //movement methods
    public boolean possibleMove(int row, int col, Board board) {

        if (row < 0 || row >= Board.rows || col < 0 || col >= Board.cols)										            //out of bounds
            return false;

        if(board.getFieldHeight(row,col) >= 4) {                                                                //too high
            return false;
        }

        if (row == this.myField.getRow() && col == this.myField.getCol()) {										        // cannot stay in place
            return false;
        }

        if(board.getFieldHeight(row,col)  - board.getFieldHeight(this.myField.getRow() , this.myField.getCol())  > 1){	//cant jump that high
            return false;
        }

        if (Math.abs(this.myField.getRow()  - row) > 1 || Math.abs(this.myField.getCol() - col) > 1) {							//cant move more than one field awa
            return false;
        }

        if(!board.getFieldFrom(row, col).isFree()){																		//cant move to a already occupied field
            return false;
        }

        return true;
    }

    public boolean hasMovesLeft(Board board){
        boolean ret = false;

        for(int i = 0; i < Board.rows; i++){
            for(int j = 0; j < Board.cols ; j++){
                ret |= this.possibleMove(i,j,board);
            }
        }

        return ret;
    }

    public boolean move(int newRow, int newCol) {

        if(!this.possibleMove(newRow,newCol, Board.currentBoard)){
            return false;
        }

        Board.currentBoard.getFieldFrom(myField.getRow(),myField.getCol()).removeToken();

        Board.currentBoard.getFieldFrom(newRow, newCol).putToken(this);
        this.myField = Board.currentBoard.getFieldFrom(newRow, newCol);


        return true;

    }

    public boolean move(int row, int col, Board board){
        if(!this.possibleMove(row,col, board)){
            return false;
        }

        board.getFieldFrom(myField.getRow(),myField.getCol()).removeToken();
        board.getFieldFrom(row, col).putToken(this);

        this.myField = board.getFieldFrom(row, col);


        return true;
    }

    //building methods
    public boolean possibleBuild(int row, int col, Board board) {

        if (row < 0 || row >= Board.rows || col < 0 || col >= Board.cols)										            //out of bounds
            return false;

        if(board.getFieldHeight(row,col) >= 4) {                                                                //too high
            return false;
        }

        if (row == this.myField.getRow() && col == this.myField.getCol()) {										        // cannot stay in place
            return false;
        }

        if (Math.abs(this.myField.getRow()  - row) > 1 || Math.abs(this.myField.getCol() - col) > 1) {							//cant move more than one field away
            return false;
        }

        if(!board.getFieldFrom(row, col).isFree()){																		//cant move to a already occupied field
            return false;
        }

        return true;
    }                                                   //can build on a field

    public boolean hasBuildsLeft(Board board){
        boolean ret = false;

        for(int i = 0; i < Board.rows; i++){
            for(int j = 0; j < Board.cols ; j++){
                ret |= this.possibleBuild(i,j,board);
            }
        }

        return ret;
    }                                                                      // can build more

    public boolean build(int newRow, int newCol) {                                                                      //builds a block if either he or his brother can build


        if(!this.possibleBuild(newRow,newCol, Board.currentBoard))
            return false;

        Board.currentBoard.getFieldFrom(newRow,newCol).increaseHeight();

        return true;
    }                                                                  //perform building on a defult board

    public boolean build(int newRow, int newCol, Board board) {


        if(!this.possibleBuild(newRow,newCol, board))
            return false;

        board.getFieldFrom(newRow,newCol).increaseHeight();

        return true;
    }                                                     //perform building

}
