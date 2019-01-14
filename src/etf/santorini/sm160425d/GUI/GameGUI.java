package etf.santorini.sm160425d.GUI;

import etf.santorini.sm160425d.Logic.Board;
import etf.santorini.sm160425d.Logic.Game;
import etf.santorini.sm160425d.boardstates.AIInitial;
import etf.santorini.sm160425d.file.MyFileReader;
import etf.santorini.sm160425d.file.MyFileWriter;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.FileNotFoundException;


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

    private TextField depthTextField = new TextField("3");
    private Label maxDepthLabel = new Label("Max depth: ");
    private HBox DepthBox = new HBox(10);

    RadioButton minimaxRB = new RadioButton("Minimax"), minimaxABRB = new RadioButton("Minimax + alpha beta pruning");
    ToggleGroup algorithms = new ToggleGroup();
    HBox algorithmHBox = new HBox();

    private static Label messageLabel = new Label();


    private Button restartButton = new Button("Restart game");
    private static boolean resetPressed = false;


    public static void setResetPressed() {
        GameGUI.resetPressed = false;
    }

    //sminka
    private Circle playerOneCircle = new Circle(75, 75, 20);
    private Label playerOneLabel = new Label("\tPlayer One");
    private HBox playerOneSminka = new HBox();

    private Circle playerTwoCircle = new Circle(75, 75, 20);
    private Label playerTwoLabel = new Label("\tPlayer Two");
    private HBox playerTwoSminka = new HBox();

    //save game
    private TextField fileName = new TextField("Game.txt");
    private Label file = new Label("File name");
    private HBox fileBox = new HBox(10);

    //load button
    private Button loadButton = new Button("Load game");

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

        //player one sminka setup
        playerOneCircle.setStroke(Color.BLUE);
        playerOneCircle.setFill(Color.BLUE);
        playerOneSminka.getChildren().addAll(playerOneCircle, playerOneLabel);
        sideBox.getChildren().add(playerOneSminka);

        //player two sminka setup
        playerTwoCircle.setStroke(Color.BLACK);
        playerTwoCircle.setFill(Color.RED);
        playerTwoSminka.getChildren().addAll(playerTwoCircle, playerTwoLabel);
        sideBox.getChildren().add(playerTwoSminka);

        //AIBox setup
        AIBox.getChildren().addAll(numberOfAILabel, numberOfAI);
        sideBox.getChildren().add(AIBox);

        //Depth setup
        DepthBox.getChildren().addAll(maxDepthLabel, depthTextField);
        DepthBox.setPadding(new Insets(0, 0, 0, 35));
        sideBox.getChildren().add(DepthBox);

        //algorithm setup
        algorithms.getToggles().addAll(minimaxRB, minimaxABRB);
        minimaxRB.setSelected(true);
        algorithmHBox.getChildren().addAll(minimaxRB, minimaxABRB);
        sideBox.getChildren().add(algorithmHBox);

        //start game button setup
        sideBox.getChildren().add(beginGame);
        beginGame.setMinSize(330, 40);
        beginGame.setOnMouseClicked(e -> {
            if (!Game.gameStarted && !Game.end) {
                Game.maxDepth = Integer.parseInt(depthTextField.getText());
                Game.numberOfAIPlayers = Integer.parseInt(numberOfAI.getText());
                try {
                    String fn = fileName.getText();
                    if(fn.equals(""))
                        fn = "game.txt";

                    MyFileWriter.getInstance().startAgain(fn);
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                }

                this.setResetPressed();

                if (Game.maxDepth <= 0)
                    Game.maxDepth = 1;
                if (Game.maxDepth > 5)                                                                                   //max reasonable depth, but why that tho
                    Game.maxDepth = 5;

                if (algorithms.getSelectedToggle() == minimaxRB)
                    Game.algorithmSelected = 0;
                else
                    Game.algorithmSelected = 1;

                if (Game.numberOfAIPlayers > 2)                                                                             //just to make sure there are too many AIPlayers
                    Game.numberOfAIPlayers = 2;
                if (Game.numberOfAIPlayers < 0)
                    Game.numberOfAIPlayers = 0;

                if (Game.numberOfAIPlayers == 0) {
                    playerOneLabel.setText("\tPlayer one - human");
                    playerTwoLabel.setText("\tPlayer two - human");
                }

                if (Game.numberOfAIPlayers == 1) {                                                                        //sminka setup
                    playerOneLabel.setText("\tPlayer one - human");
                    playerTwoLabel.setText("\tPlayer two - AI Milorad");
                }
                if (Game.numberOfAIPlayers == 2) {
                    playerOneLabel.setText("\tPlayer one - AI - Zivorad");
                    playerTwoLabel.setText("\tPlayer two - AI - Miomir");
                }

                if (Game.numberOfAIPlayers == 2) {                                                                          //if there are no human players go to AIInitial state
                    Board.currentBoard.setCurrentBoardState(AIInitial.getInstance());
                }

                Game.gameStarted = true;
            }
        });

        //AIMove button
        sideBox.getChildren().add(aiMove);
        aiMove.setMinSize(330, 40);
        aiMove.setOnMouseClicked(e -> {
            if (Game.aiTurn) {
                this.setResetPressed();

                Board.currentBoard.boardOperation(0, 0);

                if (!Game.end) {
                    if (Game.numberOfAIPlayers == 1) {                                                                        //sminka setup
                        GameGUI.setMessageLabelText("\tHuman's turn");
                    }
                    if (Game.numberOfAIPlayers == 2) {
                        if (Game.currentPlayer == 0)
                            GameGUI.setMessageLabelText("\tZivorad's turn");
                        else
                            GameGUI.setMessageLabelText("\tMomcilo's turn");
                    }
                }
                return;
            }
            if (!Game.aiTurn) {
                GameGUI.setMessageLabelText("\tStill Human's turn");
                return;
            }

        });

        //restart button
        sideBox.getChildren().add(restartButton);
        restartButton.setMinSize(330, 40);
        restartButton.setOnMouseClicked(e -> {
            if (!Game.end) {
                if (!resetPressed) {
                    GameGUI.setMessageLabelText("Game is not over yet. If you really want to restart press again");
                    resetPressed = true;
                } else {
                    this.game = new Game(this);
                    primaryPane.setCenter(game.getBoard().getMyBoardGUI());
                    Game.gameStarted = false;
                    Game.end = false;
                    resetPressed = false;
                    MyFileWriter.getInstance().save();
                }
                return;
            }

            if (Game.end) {
                GameGUI.setMessageLabelText("");
                this.game = new Game(this);
                primaryPane.setCenter(game.getBoard().getMyBoardGUI());
                Game.gameStarted = false;
                Game.end = false;
                resetPressed = false;
                MyFileWriter.getInstance().save();
            }
        });

        //load button
        sideBox.getChildren().add(loadButton);
        loadButton.setMinSize(330, 40);
        loadButton.setOnMouseClicked(e -> {
            String fn = fileName.getText();
            this.game = new Game(this);
            primaryPane.setCenter(game.getBoard().getMyBoardGUI());

            if(fn.equals("")){
                Game.gameStarted = true;
                Game.currentPlayer = 0;
                Game.end = false;
                resetPressed = false;
            }
            String res = MyFileReader.getInstance().readFile(fn);
            Board.loadGame(res);
            Game.gameStarted = true;
            Game.end = false;
            resetPressed = false;


        });
        //file box
        fileBox.getChildren().addAll(file, fileName);
        sideBox.getChildren().add(fileBox);

        //message label
        sideBox.getChildren().add(messageLabel);


        //sidebox put
        primaryPane.setRight(sideBox);


        //stage show
        primaryStage.show();
    }

    public static void setMessageLabelText(String text) {
        messageLabel.setText(text);
    }

    public static String getMessageLabelText() {
        return GameGUI.messageLabel.getText();
    }

    public static void main(String args[]) {
        launch(args);
    }


}
