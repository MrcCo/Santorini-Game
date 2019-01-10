package etf.santorini.sm160425d.boardstates;

import etf.santorini.sm160425d.Logic.Board;

public abstract class BoardState {


    public BoardState(){

    }

    public abstract void boardOperation(int row, int col);
}
