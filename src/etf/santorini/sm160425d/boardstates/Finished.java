package etf.santorini.sm160425d.boardstates;

import etf.santorini.sm160425d.GUI.GameGUI;

public class Finished extends BoardState {

    private static Finished instance;
    private int winner;

    public static Finished getInstance(int winner) {
        if (instance == null)
            instance = new Finished();

        GameGUI.setMessageLabelText("Dzaba stiskas gotova igra a pobedio je " + (instance.winner + 1));
        instance.winner = winner;
        return instance;
    }

    @Override
    public void boardOperation(int row, int col) {
        GameGUI.setMessageLabelText("Dzaba stiskas gotova igra a pobedio je " + (instance.winner + 1));
    }
}
