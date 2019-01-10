package etf.santorini.sm160425d.boardstates;


import etf.santorini.sm160425d.Logic.Board;
import etf.santorini.sm160425d.Logic.Game;

public class AIPlayState extends BoardState {

    private static AIPlayState instance;

    public AIPlayState(){
    }

    public static AIPlayState getInstance(){
        if(instance == null)
            instance = new AIPlayState();
        Game.aiTurn = true;
        return  instance;
    }


    @Override
    public void boardOperation(int row, int col) {
        System.out.println("NOW IS AI TURN");                                                                           //TODO MOVE TO GUI

        Board.currentBoard.AIFullMove();                                                                                //do AI move



        if(Game.numberOfAIPlayers == 1) {                                                                             //if AI is playing vs hunab let him play
            Game.aiTurn = false;
            Board.currentBoard.setCurrentBoardState(PlayState.getInstance());
        }
        if(!Game.aiTurn)
            System.out.println("NEXT IS MAN" + Game.currentPlayer);                                                     //TODO MOVE TO GUI
    }
}
