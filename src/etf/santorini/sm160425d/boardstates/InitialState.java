package etf.santorini.sm160425d.boardstates;

import etf.santorini.sm160425d.Logic.Board;
import etf.santorini.sm160425d.Logic.Game;
import etf.santorini.sm160425d.Logic.Token;
import etf.santorini.sm160425d.file.MyFileWriter;

public class InitialState extends BoardState {

    private static InitialState instance;
    public static int numberOfPicks = 0;

    public InitialState() {
        numberOfPicks = 0;
    }

    public static InitialState getInstance() {
        if (instance == null)
            instance = new InitialState();

        Game.aiTurn = false;
        numberOfPicks = 0;
        return instance;
    }


    @Override
    public void boardOperation(int row, int col) {

        MyFileWriter.getInstance().printToFile(row, col);

        switch (Game.numberOfAIPlayers) {

            case (0):
                if (InitialState.numberOfPicks == 2) {                                                             //now it is second players turn to pick
                    Board.currentBoard.getMyGame().currentPlayer = 1;
                }
                if (Board.currentBoard.getFieldFrom(row, col).isFree()) {                                                 //just to make sure

                    Token token = new Token(Board.currentBoard.getFieldFrom(row, col),
                            Board.currentBoard.getMyGame().currentPlayer);                                              //create new token
                    Board.currentBoard.tokens[numberOfPicks] = token;

                    if (Board.currentBoard.putToken(row, col, token)) {                                                 //if i manage to put a token on the board increase num of picks

                        if(InitialState.numberOfPicks == 1)
                            MyFileWriter.getInstance().printNLToFile();

                        InitialState.numberOfPicks++;
                    }

                }

                if (InitialState.numberOfPicks == 4) {
                    Game.currentPlayer = 0;
                    MyFileWriter.getInstance().printNLToFile();
                    Board.currentBoard.setCurrentBoardState(PlayState.getInstance());
                }
                break;

            case (1):                                                                                                    //if we have a AI oponent let him play

                if (Board.currentBoard.getFieldFrom(row, col).isFree()) {                                                 //just to make sure

                    Token token = new Token(Board.currentBoard.getFieldFrom(row, col),
                            Board.currentBoard.getMyGame().currentPlayer);                                              //create new token
                    Board.currentBoard.tokens[numberOfPicks] = token;

                    if (Board.currentBoard.putToken(row, col, token)) {                                                 //if i manage to put a token on the board increase num of picks
                        InitialState.numberOfPicks++;
                    }

                }

                if (InitialState.numberOfPicks == 2) {                                                                    //let AI play
                    Game.currentPlayer = 1;
                    MyFileWriter.getInstance().printNLToFile();
                    Board.currentBoard.setCurrentBoardState(AIInitial.getInstance());
                }

                break;
        }
    }
}

