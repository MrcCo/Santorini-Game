package etf.santorini.sm160425d.Logic;

import etf.santorini.sm160425d.GUI.GameGUI;
import etf.santorini.sm160425d.fileWriter.MyFileWriter;

public class Game {

    public static int algorithmSelected = 0;
    public static int currentPlayer;
    public static int numberOfAIPlayers;
    public static boolean gameStarted = false;
    public static boolean aiTurn = false;
    public static int maxDepth = 4;
    public static MyFileWriter writer = new MyFileWriter("Game.txt");
    Board board;
    GameGUI gameGUI;


    public Game(GameGUI gameGUI) {
        this.gameGUI = gameGUI;
        currentPlayer = 0;
        this.board = Board.currentBoard = new Board(this);
    }

    public Board getBoard() {
        return board;
    }

    public static int getNextPlayer() {
        return (Game.currentPlayer + 1) % 2;
    }

    public static void setNextPlayer() {
        Game.currentPlayer = (Game.currentPlayer + 1) % 2;
    }


}
