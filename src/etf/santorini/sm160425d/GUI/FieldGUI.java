package etf.santorini.sm160425d.GUI;

import etf.santorini.sm160425d.Logic.Board;
import etf.santorini.sm160425d.Logic.Field;
import etf.santorini.sm160425d.Logic.Game;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class FieldGUI extends StackPane {

    Field myField;

    public FieldGUI(Field field) {
        this.myField = field;

        Rectangle base = new Rectangle(150, 150);               //adding base rectangle
        base.setFill(Color.BISQUE);
        base.setStroke(Color.BLACK);
        this.getChildren().add(base);

        this.setOnMouseClicked(event -> {                   //TODO FINISH MOUSE LISTENER
            GameGUI.setResetPressed();

            if (Game.gameStarted && !Game.aiTurn)
                Board.currentBoard.boardOperation(myField.getRow(), myField.getCol());

        });

    }

    public void putBlock() {

        Rectangle block = new Rectangle((5 - myField.getMyHeight()) * 30, (5 - myField.getMyHeight()) * 30);

        if (myField.getMyHeight() == 4) {
            block.setFill(Color.BLUE);
        } else {
            block.setFill(Color.WHITE);
        }

        block.setStroke(Color.BLACK);

        this.getChildren().add(block);
    }

    public void putToken(TokenGUI t) {
        this.getChildren().add(t);
    }

    public void removeToken(TokenGUI t) {
        this.getChildren().remove(t);
    }


}
