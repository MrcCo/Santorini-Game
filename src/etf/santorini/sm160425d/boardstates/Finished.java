package etf.santorini.sm160425d.boardstates;

import etf.santorini.sm160425d.GUI.GameGUI;
import etf.santorini.sm160425d.Logic.Game;

public class Finished extends BoardState {

    private static Finished instance;
    private int winner;

    public static Finished getInstance(int winner) {
        if (instance == null)
            instance = new Finished();

        GameGUI.setMessageLabelText("We have a winner - player " + (instance.winner + 1) + "\n" +GameGUI.getMessageLabelText());

        Game.end = true;
        instance.winner = winner;
        return instance;
    }

    @Override
    public void boardOperation(int row, int col) {
    }
}
