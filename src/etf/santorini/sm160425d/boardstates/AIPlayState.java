package etf.santorini.sm160425d.boardstates;


import etf.santorini.sm160425d.GUI.GameGUI;
import etf.santorini.sm160425d.Logic.Board;
import etf.santorini.sm160425d.Logic.Game;

public class AIPlayState extends BoardState {

    private static AIPlayState instance;

    public AIPlayState() {
    }

    public static AIPlayState getInstance() {
        if (instance == null)
            instance = new AIPlayState();

        if (Game.numberOfAIPlayers == 1) {                                                                        //sminka setup
            GameGUI.setMessageLabelText("\tMilorad's turn");
        }
        if (Game.numberOfAIPlayers == 2) {
            if (Game.currentPlayer == 0)
                GameGUI.setMessageLabelText("\tZivorad's turn");
            else
                GameGUI.setMessageLabelText("\tMomcilo's turn");
        }

        Game.aiTurn = true;
        return instance;
    }


    @Override
    public void boardOperation(int row, int col) {

        boolean succ = Board.currentBoard.AIFullMove();                                                                                //do AI move

        if (succ == false) {
            if(Game.numberOfAIPlayers == 1)
                GameGUI.setMessageLabelText("Teach me master :(");
            else
                GameGUI.setMessageLabelText("AI always wins muahahahahahah!");

            Board.currentBoard.setCurrentBoardState(Finished.getInstance(Game.getNextPlayer()));
        }

        if (Board.currentPlayerWon() == true) {                                                                         //finish the game if i won
            GameGUI.setMessageLabelText("AI always wins muahahahahahah!");
            Board.currentBoard.setCurrentBoardState(Finished.getInstance(Game.currentPlayer));
            Board.currentBoard.boardOperation(0,0);

        }

        Game.setNextPlayer();                                                                                           //set next player

        if (Game.numberOfAIPlayers == 1) {                                                                               //if AI is playing vs hunab let him play
            Board.currentBoard.setCurrentBoardState(PlayState.getInstance());
        }

        if (!Game.aiTurn)
            System.out.println("NEXT IS MAN" + Game.currentPlayer);                                                     //TODO MOVE TO GUI
    }
}
