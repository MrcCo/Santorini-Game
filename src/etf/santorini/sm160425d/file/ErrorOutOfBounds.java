package etf.santorini.sm160425d.file;

public class ErrorOutOfBounds extends Throwable {
    private int row, col;

    public ErrorOutOfBounds(int row, int col) {
        this.row = row;
        this.col = col;
    }

    @Override
    public String toString() {
        return "Out of bounds row: " + this.row + " col: " + this.col;
    }
}
