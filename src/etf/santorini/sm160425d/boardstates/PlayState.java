package etf.santorini.sm160425d.boardstates;

import etf.santorini.sm160425d.GUI.GameGUI;
import etf.santorini.sm160425d.Logic.Board;
import etf.santorini.sm160425d.Logic.Game;
import etf.santorini.sm160425d.Logic.Token;
import etf.santorini.sm160425d.file.MyFileWriter;

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

            if(!Board.currentBoard.playerHasAnyMovesLeft(Game.getNextPlayer())){
                Board.currentBoard.setCurrentBoardState(Finished.getInstance(Game.getNextPlayer()));
                Board.currentBoard.boardOperation(0,0);
                return;
            }
                                                                                                                    //we are selecting a token
            GameGUI.setMessageLabelText("");
            if (Board.currentBoard.getFieldFrom(row, col).isFree()) {                                              //there is no token on the field
                GameGUI.setMessageLabelText("SELECT AGAIN PLEASE, THIS ONE WAS INCORRECT");
                return;
            }

            tokenWeAreWorkingWith = Board.currentBoard.getFieldFrom(row, col).getMyToken();                      //get token on the field

            if (tokenWeAreWorkingWith != null && tokenWeAreWorkingWith.getPlayer() == Board.currentBoard.getMyGame().currentPlayer) {     //check if it is current players token
                if (tokenWeAreWorkingWith.hasMovesLeft(Board.currentBoard)) {                                     //if we selected an imobile token but player can move
                    tokenWeAreWorkingWith.highlight();
                    MyFileWriter.getInstance().printToFile(row, col);
                    move = Move.MOVE;
                    return;
                }
            } else {
                GameGUI.setMessageLabelText("SELECT AGAIN PLEASE, THIS ONE WAS INCORRECT");
                return;
            }
        } else {

            if (move == Move.MOVE) {

                tokenWeAreWorkingWith.lowlight();

                if (tokenWeAreWorkingWith.move(row, col)) {                                                        //check if movement can be performed

                    MyFileWriter.getInstance().printToFile(row, col);

                    GameGUI.setMessageLabelText("Current player " + Game.currentPlayer);

                    if (Board.currentPlayerWon()) {                                                                     //check if player won
                        GameGUI.setMessageLabelText("");
                        Board.currentBoard.setCurrentBoardState(Finished.getInstance(Game.currentPlayer));
                    }

                    if (!Board.playerHasAnyBuildsLeft(Game.currentPlayer)) {                                      //check if i can build anywhere
                        int winner = (Game.currentPlayer + 1) % 2;
                        GameGUI.setMessageLabelText("");
                        Board.currentBoard.setCurrentBoardState(Finished.getInstance(winner));                          //game over
                    }

                    move = Move.BUILD;

                } else {                                                                                                   //bad move
                    GameGUI.setMessageLabelText("CANNOT MOVE THERE BUDDY");
                    tokenWeAreWorkingWith.highlight();
                    return;
                }

            } else {

                if (move == Move.BUILD) {
                    if (tokenWeAreWorkingWith.build(row, col)) {

                        MyFileWriter.getInstance().printToFile(row, col);
                        MyFileWriter.getInstance().printNLToFile();

                        GameGUI.setMessageLabelText("Current player " + Game.currentPlayer);

                        if (Game.numberOfAIPlayers == 1) {
                            Game.aiTurn = true;
                            Board.currentBoard.setCurrentBoardState(AIPlayState.getInstance());
                        } else {
                            move = Move.SELECT;
                        }

                        Game.currentPlayer = (Game.currentPlayer + 1) % 2;

                        GameGUI.setMessageLabelText("Next player is: player " + Game.currentPlayer);

                    }else{
                        GameGUI.setMessageLabelText("CANNOT BUILD THERE PAL");
                    }
                }

            }
        }
    }


}
