package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import com.google.inject.Stage;
import commons.Activity;
import commons.Question;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GameScreenControl {

    private final ServerUtils server;
    private final GameCtrl mainCtrl;
    public Stage primaryStage;

    public int correctAnswer = 0;
    public int jokerOneActive = 1;

    @FXML
    private Text questionText;
    @FXML
    private Text answerOne;
    @FXML
    private Text answerTwo;
    @FXML
    private Text answerThree;
    @FXML
    private Label points;
    @FXML
    private Pane answerOnePane;
    @FXML
    private Pane answerTwoPane;
    @FXML
    private Pane answerThreePane;
    @FXML
    private Text round;
    @FXML
    private Button jokerOne;
    @FXML
    private Button jokerTwo;
    @FXML
    private Button jokerThree;

    @Inject
    public GameScreenControl(ServerUtils server, GameCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /*
    This function is a setup for the GameScreen.
    A question is given as input and this question is displayed on the screen.
     */
    public void setQuestion(Question question) {
        questionText.setText("Which activity takes the most energy?");
        List<Activity> activityList = question.activityList;
        answerOne.setText(question.activityList.get(0).title);
        answerTwo.setText(question.activityList.get(1).title);
        answerThree.setText(question.activityList.get(2).title);
        correctAnswer = 0;
        if(activityList.get(1).consumption > activityList.get(correctAnswer).consumption)
            this.correctAnswer = 1;
        if(activityList.get(2).consumption > activityList.get(correctAnswer).consumption)
            this.correctAnswer = 2;
        enableButtons();
        jokerThree.setVisible(false);
    }

    //This function disables the answer buttons when an answer has been clicked
    public void disableButtons() {
        answerOnePane.setDisable(true);
        answerTwoPane.setDisable(true);
        answerThreePane.setDisable(true);
    }

    //This function enables the answer buttons when a new round starts
    public void enableButtons() {
        answerOnePane.setDisable(false);
        answerTwoPane.setDisable(false);
        answerThreePane.setDisable(false);

    }

    //Function for when the player answers one
    public void answerOneGiven() {
        revealAnswers(answerOnePane, 0);
        disableButtons();
    }

    //Function for when the player answers two
    public void answerTwoGiven() {
        revealAnswers(answerTwoPane, 1);
        disableButtons();
    }

    //Function for when the player answers three
    public void answerThreeGiven() {
        revealAnswers(answerThreePane, 2);
        disableButtons();
    }

    //Remove the coloured borders when a new round starts
    public void removeBorders() {
        answerOnePane.setBorder(null);
        answerTwoPane.setBorder(null);
        answerThreePane.setBorder(null);
        jokerOne.setDisable(false);
        jokerTwo.setDisable(false);
        jokerThree.setDisable(false);
        jokerOne.setBorder(null);
        jokerTwo.setBorder(null);
        jokerOneActive = 1;
    }

    /*
    This function reveals the correct and wrong answer by colouring the borders of the answers.
    It adds points and calls the newQuestion function
     */
    public void revealAnswers(Pane clicked, int click) {
        switch(correctAnswer) {
            case(0):
                answerOnePane.setBorder(new Border(new BorderStroke(Color.GREEN,BorderStrokeStyle.SOLID, new CornerRadii(20), new BorderWidths(5))));
                break;
            case(1):
                answerTwoPane.setBorder(new Border(new BorderStroke(Color.GREEN,BorderStrokeStyle.SOLID, new CornerRadii(20), new BorderWidths(5))));
                break;
            case(2):
                answerThreePane.setBorder(new Border(new BorderStroke(Color.GREEN,BorderStrokeStyle.SOLID, new CornerRadii(20), new BorderWidths(5))));
                break;
            default:
                break;
        }
        if(correctAnswer != click) {
            clicked.setBorder(new Border(new BorderStroke(Color.RED,BorderStrokeStyle.SOLID, new CornerRadii(20), new BorderWidths(5))));
        } else {
            mainCtrl.points += (jokerOneActive * 100);
        }

        points.setText(mainCtrl.points + " points");
        newQuestion();
    }

    //This function will start a new question after 5 seconds.
    public void newQuestion() {
        Timer myTimer = new Timer();
        myTimer.schedule(new TimerTask(){

            @Override
            public void run() {
                Platform.runLater(() -> {
                    removeBorders();
                    round.setText("round " + ++mainCtrl.round);
                    if(mainCtrl.round > 5) {
                        mainCtrl.showLeaderBoard();

                    } else {
                        mainCtrl.showGameScreen();
                    }
                });
            }
        }, 5000);
    }

    //This function returns to the splash screen (for when a user clicks 'back')
    public void backToSplash() {
        mainCtrl.points = 0;
        mainCtrl.round = 1;
        mainCtrl.username = "";
        mainCtrl.showOverview();
    }

    //Function for when joker one is pressed
    public void jokerOne() {
        this.jokerOneActive = 2;
        jokerOne.setBorder(new Border(new BorderStroke(Color.DARKGREEN,BorderStrokeStyle.SOLID, new CornerRadii(20), new BorderWidths(2))));
        jokerOne.setDisable(true);
        jokerTwo.setDisable(true);
        jokerThree.setDisable(true);
    }

    //Function for joker two
    public void jokerTwo() {
        Random random = new Random();
        int i = random.nextInt(2) + 1;
        int disable = (correctAnswer + i) % 3;
        switch(disable) {
            case(0):
                answerOnePane.setDisable(true);
                answerOnePane.setBorder(new Border(new BorderStroke(Color.GREY,BorderStrokeStyle.SOLID, new CornerRadii(20), new BorderWidths(2))));
                break;
            case(1):
                answerTwoPane.setDisable(true);
                answerTwoPane.setBorder(new Border(new BorderStroke(Color.GREY,BorderStrokeStyle.SOLID, new CornerRadii(20), new BorderWidths(2))));
                break;
            case(2):
                answerThreePane.setDisable(true);
                answerThreePane.setBorder(new Border(new BorderStroke(Color.GREY,BorderStrokeStyle.SOLID, new CornerRadii(20), new BorderWidths(2))));
                break;
            default:
                break;
        }
        jokerTwo.setBorder(new Border(new BorderStroke(Color.DARKGREEN,BorderStrokeStyle.SOLID, new CornerRadii(20), new BorderWidths(2))));
        jokerOne.setDisable(true);
        jokerTwo.setDisable(true);
        jokerThree.setDisable(true);
    }

}
