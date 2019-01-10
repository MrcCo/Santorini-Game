package etf.santorini.sm160425d.boardstates;

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


        if (move == Move.SELECT) {                                                                                      //we are selecting a token

            if (Board.currentBoard.getFieldFrom(row, col).isFree()) {                                              //there is no token on the field
                System.out.println("NEVALIDAN SELECT");                                                             //TODO MOVE TO GUI
                return;
            }

            tokenWeAreWorkingWith = Board.currentBoard.getFieldFrom(row, col).getMyToken();                      //get token on the field

            if (tokenWeAreWorkingWith != null && tokenWeAreWorkingWith.getPlayer() == Board.currentBoard.getMyGame().currentPlayer) {     //check if it is current players token
                if (tokenWeAreWorkingWith.hasMovesLeft(Board.currentBoard)) {                                     //if we selected an imobile token but player can move
                    tokenWeAreWorkingWith.highlight();
                    move = Move.MOVE;                                                                           //diskutabilno
                    return;
                }
            } else {
                System.out.println("NEVALIDAN SELECT");                                                             //TODO MOVE TO GUI
                return;
            }
        } else {

            if (move == Move.MOVE) {

                tokenWeAreWorkingWith.lowlight();

                if (tokenWeAreWorkingWith.move(row, col)) {                                                        //check if movement can be performed

                    if (Board.currentPlayerWon()) {                                                                     //check if player won
                        System.out.println("IMAMO POBEDNIKA");                                                          //TODO move to GUI
                        Board.currentBoard.setCurrentBoardState(Finished.getInstance(Game.currentPlayer));
                    }

                    if (!Board.playerHasAnyBuildsLeft(Game.currentPlayer)) {                                      //check if i can build anywhere
                        int winner = (Game.currentPlayer + 1) % 2;
                        System.out.println("IMAMO POBEDNIKA I TO JE " + winner);
                        Board.currentBoard.setCurrentBoardState(Finished.getInstance(winner));                          //game over
                    }

                    move = Move.BUILD;

                } else {                                                                                                   //bad move
                    System.out.println("NEVALIDAN POMERAJ");
                    tokenWeAreWorkingWith.highlight();
                    return;
                }

            } else {

                if (move == Move.BUILD) {
                    if (tokenWeAreWorkingWith.build(row, col)) {

                        if (Game.numberOfAIPlayers == 1) {
                            Game.aiTurn = true;
                            Board.currentBoard.setCurrentBoardState(AIPlayState.getInstance());
                        } else {
                            move = Move.SELECT;
                        }

                        Game.currentPlayer = (Game.currentPlayer + 1) % 2;

                    }
                }

            }
        }
    }


}
