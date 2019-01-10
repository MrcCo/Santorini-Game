package etf.santorini.sm160425d.boardstates;

import etf.santorini.sm160425d.Logic.Board;
import etf.santorini.sm160425d.Logic.Game;

public class AIInitial extends BoardState {

    private static AIInitial instance;

    public AIInitial(){
    }

    public static AIInitial getInstance(){
        if(instance == null)
            instance = new AIInitial();
        Game.aiTurn = true;
        return  instance;
    }


    @Override
    public void boardOperation(int row, int col) {
        Board.currentBoard.AIInitialPicks();

        System.out.println("AI INIT CALLED");

        Game.setNextPlayer();

        if(Game.numberOfAIPlayers == 1){
            Board.currentBoard.setCurrentBoardState(PlayState.getInstance());
        }else{
            if(Game.currentPlayer == 0)
                Board.currentBoard.setCurrentBoardState(AIPlayState.getInstance());
        }
    }
}
