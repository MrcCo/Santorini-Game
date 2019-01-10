package etf.santorini.sm160425d.GUI;

import etf.santorini.sm160425d.Logic.Board;
import etf.santorini.sm160425d.Logic.Game;
import etf.santorini.sm160425d.boardstates.AIInitial;
import javafx.application.Application;
import javafx.event.Event;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class GameGUI extends Application {

    private Stage primaryStage;
    private Scene primaryScene;
    private BorderPane primaryPane = new BorderPane();
    private Game game = new Game(this);
    private VBox sideBox = new VBox();
    private Button beginGame = new Button("Start the game");
    private TextField numberOfAI = new TextField("0");
    private Button aiMove = new Button("AI Move");

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        primaryScene = new Scene(primaryPane, 1200, 800);
        primaryStage.setScene(primaryScene);
        primaryStage.setResizable(false);

        //board gui setup
        primaryPane.setCenter(game.getBoard().getMyBoardGUI());


        //sidebox setup
        sideBox.setMinSize(400, 800);

        sideBox.getChildren().add(numberOfAI);

        sideBox.getChildren().add(beginGame);
        beginGame.setOnMouseClicked(e -> {
            Game.numberOfAIPlayers = Integer.parseInt(numberOfAI.getText());

            if (Game.numberOfAIPlayers > 2)
                Game.numberOfAIPlayers = 2;
            if (Game.numberOfAIPlayers < 0)
                Game.numberOfAIPlayers = 0;

            if (Game.numberOfAIPlayers == 2) {
                Board.currentBoard.setCurrentBoardState(AIInitial.getInstance());
            }


            Game.gameStarted = true;

        });

        sideBox.getChildren().add(aiMove);
        aiMove.setOnMouseClicked(e -> {
            if (Game.aiTurn) {
                System.out.println("OOOOOK ?");
                Board.currentBoard.boardOperation(0, 0);
            }
        });
        primaryPane.setRight(sideBox);


        //stage show
        primaryStage.show();
    }

    public static void main(String args[]) {
        launch(args);
    }


}
