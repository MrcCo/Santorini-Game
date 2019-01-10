package etf.santorini.sm160425d.GUI;

import etf.santorini.sm160425d.Logic.Board;
import etf.santorini.sm160425d.Logic.Game;
import etf.santorini.sm160425d.boardstates.AIInitial;
import javafx.application.Application;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class GameGUI extends Application {

    private Stage primaryStage;
    private Scene primaryScene;
    private BorderPane primaryPane = new BorderPane();
    private Game game = new Game(this);
    private VBox sideBox = new VBox();
    private Button beginGame = new Button("Start the game");
    private Button aiMove = new Button("AI Move");

    private TextField numberOfAI = new TextField("0");
    private Label numberOfAILabel = new Label("Number of AI players: ");
    private HBox AIBox = new HBox(10);

    private static Label messageLabel = new Label();

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        primaryScene = new Scene(primaryPane, 1220, 810);
        primaryStage.setScene(primaryScene);
        primaryStage.setResizable(false);

        //board gui setup
        primaryPane.setCenter(game.getBoard().getMyBoardGUI());


        //sidebox setup
        sideBox.setMinSize(400, 800);
        sideBox.setSpacing(20);
        sideBox.setPadding(new Insets(20, 0, 0, 20));

        //AIBox setup
        AIBox.getChildren().addAll(numberOfAILabel, numberOfAI);
        sideBox.getChildren().add(AIBox);

        //start game button setup
        sideBox.getChildren().add(beginGame);
        beginGame.setMinSize(330,40);
        beginGame.setOnMouseClicked(e -> {

            Game.numberOfAIPlayers = Integer.parseInt(numberOfAI.getText());

            if (Game.numberOfAIPlayers > 2)                                                                             //just to make sure there are too many AIPlayers
                Game.numberOfAIPlayers = 2;
            if (Game.numberOfAIPlayers < 0)
                Game.numberOfAIPlayers = 0;

            if (Game.numberOfAIPlayers == 2) {                                                                          //if there are no human players go to AIInitial state
                Board.currentBoard.setCurrentBoardState(AIInitial.getInstance());
            }

            Game.gameStarted = true;

        });

        //AIMove button
        sideBox.getChildren().add(aiMove);
        aiMove.setMinSize(330,40);
        aiMove.setOnMouseClicked(e -> {
            if (Game.aiTurn) {
                Board.currentBoard.boardOperation(0, 0);
            }
        });

        //message label
        sideBox.getChildren().add(messageLabel);

        //sidebox put
        primaryPane.setRight(sideBox);



        //stage show
        primaryStage.show();
    }

    public static void setMessageLabelText(String text){
        messageLabel.setText(text);
    }

    public static void main(String args[]) {
        launch(args);
    }


}
