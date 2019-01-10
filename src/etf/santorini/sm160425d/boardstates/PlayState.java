package etf.santorini.sm160425d.boardstates;

import etf.santorini.sm160425d.GUI.GameGUI;
import etf.santorini.sm160425d.Logic.Board;
import etf.santorini.sm160425d.Logic.Game;
import etf.santorini.sm160425d.Logic.Token;

import java.util.ArrayList;

public class PlayState extends BoardState {

    private static PlayState instance;

    public enum Move {
        SELECT, MOVE, BUILD;
    }

    public static Move move;
    public Token tokenWeAreWorkingWith;                                                                                 //token we are selecting, or moving

    public PlayState() {
        move = Move.SELECT;
    }


    public static PlayState getInstance() {
        if (instance == null)
            instance = new PlayState();

        Game.aiTurn = false;
        move = Move.SELECT;
        return instance;
    }

    @Override
    public void boardOperation(int row, int col) {


        if (move == Move.SELECT) {
                                                                                                                    //we are selecting a token
            GameGUI.setMessageLabelText("");
            if (Board.currentBoard.getFieldFrom(row, col).isFree()) {                                              //there is no token on the field
                GameGUI.setMessageLabelText("NEVALIDAN SELECT");                                                             //TODO MOVE TO GUI
                return;
            }

            tokenWeAreWorkingWith = Board.currentBoard.getFieldFrom(row, col).getMyToken();                      //get token on the field

            if (tokenWeAreWorkingWith != null && tokenWeAreWorkingWith.getPlayer() == Board.currentBoard.getMyGame().currentPlayer) {     //check if it is current players token
                if (tokenWeAreWorkingWith.hasMovesLeft(Board.currentBoard)) {                                     //if we selected an imobile token but player can move
                    tokenWeAreWorkingWith.highlight();
                    Game.writer.printToFile(row,col,false);
                    move = Move.MOVE;
                    return;
                }
            } else {
                GameGUI.setMessageLabelText("NEVALIDAN SELECT");                                                             //TODO MOVE TO GUI
                return;
            }
        } else {

            if (move == Move.MOVE) {

                tokenWeAreWorkingWith.lowlight();

                if (tokenWeAreWorkingWith.move(row, col)) {                                                        //check if movement can be performed

                    Game.writer.printToFile(row,col,false);

                    GameGUI.setMessageLabelText("");

                    if (Board.currentPlayerWon()) {                                                                     //check if player won
                        GameGUI.setMessageLabelText("IMAMO POBEDNIKA");                                                          //TODO move to GUI
                        Board.currentBoard.setCurrentBoardState(Finished.getInstance(Game.currentPlayer));
                    }

                    if (!Board.playerHasAnyBuildsLeft(Game.currentPlayer)) {                                      //check if i can build anywhere
                        int winner = (Game.currentPlayer + 1) % 2;
                        GameGUI.setMessageLabelText("IMAMO POBEDNIKA I TO JE " + winner);
                        Board.currentBoard.setCurrentBoardState(Finished.getInstance(winner));                          //game over
                    }

                    move = Move.BUILD;

                } else {                                                                                                   //bad move
                    GameGUI.setMessageLabelText("NEVALIDAN POMERAJ");
                    tokenWeAreWorkingWith.highlight();
                    return;
                }

            } else {

                if (move == Move.BUILD) {
                    if (tokenWeAreWorkingWith.build(row, col)) {

                        Game.writer.printToFile(row,col,true);

                        GameGUI.setMessageLabelText("");

                        if (Game.numberOfAIPlayers == 1) {
                            Game.aiTurn = true;
                            Board.currentBoard.setCurrentBoardState(AIPlayState.getInstance());
                        } else {
                            move = Move.SELECT;
                        }

                        Game.currentPlayer = (Game.currentPlayer + 1) % 2;

                    }else{
                        GameGUI.setMessageLabelText("NEVALIDAN BUILD");
                    }
                }

            }
        }
    }


}
