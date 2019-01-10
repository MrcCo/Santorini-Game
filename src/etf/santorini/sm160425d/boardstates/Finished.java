package etf.santorini.sm160425d.boardstates;

public class Finished extends BoardState {

    private static Finished instance;
    private int winner;

    public static Finished getInstance(int winner) {
        if (instance == null)
            instance = new Finished();

        instance.winner = winner;
        return instance;
    }

    @Override
    public void boardOperation(int row, int col) {
        System.out.println("Dzaba stiskas gotova igra a pobedio je " + (instance.winner + 1));
    }
}
