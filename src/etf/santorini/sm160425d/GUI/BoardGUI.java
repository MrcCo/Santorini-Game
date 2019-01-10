package etf.santorini.sm160425d.GUI;

import etf.santorini.sm160425d.Logic.Board;
import javafx.geometry.Insets;
import javafx.scene.layout.TilePane;

public class BoardGUI extends TilePane {

    private FieldGUI matrix[][] = new FieldGUI[Board.rows][Board.cols];
    Board board;

    public BoardGUI(Board board) {

        this.setPrefColumns(board.cols);
        this.setPrefRows(board.rows);
        this.setHgap(8);
        this.setVgap(8);
        this.setPadding(new Insets(10, 0, 10, 10));

        for (int i = 0; i < board.rows; i++) {
            for (int j = 0; j < board.cols; j++) {
                matrix[i][j] = null;                  //TODO edit this constructor
            }
        }
    }


    public void add(int i, int j, FieldGUI fg) {                                                                         //adds field gui to the matrix
        matrix[i][j] = fg;
        this.getChildren().add(matrix[i][j]);
    }

    public void add(int row, int col, TokenGUI token) {                                                                                        //adds token to a field of the matrix
        matrix[row][col].putToken(token);
    }


}
