package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import com.google.inject.Stage;
import commons.Activity;
import commons.Question;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class QuestionCtrl {

    private final ServerUtils server;
    private final GameCtrl mainCtrl;
    public Stage primaryStage;

    public int correctAnswer;
    public int jokerOneActive = 1;
    final int[] secondsPassed = {10};
    Timer myTimer;
    TimerTask task;

    @FXML
    private Label questionText;
    @FXML
    private Label answerOne;
    @FXML
    private Label answerTwo;
    @FXML
    private Label answerThree;
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
    @FXML
    private Text secondsLeft;
    @FXML
    private ImageView emojiOne;
    @FXML
    private ImageView emojiTwo;
    @FXML
    private ImageView emojiThree;
    @FXML
    private Label answersGiven;
    @FXML
    private AnchorPane threeActivitiesAnchorPane;
    @FXML
    private AnchorPane oneActivityAnchorPane;

    @Inject
    public QuestionCtrl(ServerUtils server, GameCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    //Every new round, a new timer and new timertask have to be instantiated
    public void instantiateTimer() {
        secondsPassed[0] = 10;
        myTimer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                secondsPassed[0]--;
                if(secondsPassed[0] == 1) {
                    secondsLeft.setText("Time left: " + secondsPassed[0] + " second");
                } else if (secondsPassed[0] > 0)
                    secondsLeft.setText("Time left: " + secondsPassed[0] + " seconds");
                else {
//                            myTimer.cancel();
                    revealAnswers(null, 4);
                }

            }
        };
    }

    /*
    This function is a setup for the GameScreen.
    A question is given as input and this question is displayed on the screen.
     */
    public void startQuestion(Question question) {
        List<Activity> activityList = question.activityList;
        answerOne.setText(question.activityList.get(0).title);
        answerTwo.setText(question.activityList.get(1).title);
        answerThree.setText(question.activityList.get(2).title);
        question.setCorrectAnswer();
        this.correctAnswer = question.correctAnswer;

        enableButtons();

        hideSoloPlayerElements();

        instantiateTimer();

        startTimer();

    }

    //This functions starts the timer. When the timer finishes, the answers are revealed
    public void startTimer() {
//        myTimer.scheduleAtFixedRate(new TimerTask(){
//
//            @Override
//            public void run() {
//                Platform.runLater(() -> {
//                    secondsPassed[0]--;
//                    Platform.runLater(() -> {
//                                if(secondsPassed[0] == 1) {
//                                    secondsLeft.setText("Time left: " + secondsPassed[0] + " second");
//                                } else if (secondsPassed[0] > 0)
//                                    secondsLeft.setText("Time left: " + secondsPassed[0] + " seconds");
//                                else {
//                                    myTimer.cancel();
//                                    revealAnswers(null, 4);
//                                }
//                            }
//                    );
//                });
//            }
//        }, 1000,1000);
        myTimer.scheduleAtFixedRate(task, 1000,1000);
    }

    //This function is for hiding the elements on solo player that do not make sense
    public void hideSoloPlayerElements() {
        //jokerThree.setVisible(false);
        emojiOne.setVisible(false);
        emojiTwo.setVisible(false);
        emojiThree.setVisible(false);
        answersGiven.setVisible(false);
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
        myTimer.cancel();
        revealAnswers(answerOnePane, 0);
        disableButtons();
    }

    //Function for when the player answers two
    public void answerTwoGiven() {
        myTimer.cancel();
        revealAnswers(answerTwoPane, 1);
        disableButtons();
    }

    //Function for when the player answers three
    public void answerThreeGiven() {
        myTimer.cancel();
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
        //jokerThree.setDisable(false);
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
        if(correctAnswer != click && !(click > 2)) {
            clicked.setBorder(new Border(new BorderStroke(Color.RED,BorderStrokeStyle.SOLID, new CornerRadii(20), new BorderWidths(5))));
        } else if (correctAnswer == click && !(click > 2)){
            mainCtrl.points += (jokerOneActive * 100);
        }

        points.setText(mainCtrl.points + " points");
        newQuestion();
    }



    //This function will start a new question after 5 seconds.
    public void newQuestion() {
        Timer myTimers = new Timer();
        myTimers.schedule(new TimerTask(){

            @Override
            public void run() {
                Platform.runLater(() -> {
                    removeBorders();
                    round.setText("round " + ++mainCtrl.round);
                    if(mainCtrl.round > 5) {
                        mainCtrl.showLeaderBoard();

                    } else {
                        mainCtrl.SoloGameRound();
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
        mainCtrl.showSplashScreen();
    }

    //Function for when joker one is pressed
    public void jokerOne() {
        this.jokerOneActive = 2;
        jokerOne.setBorder(new Border(new BorderStroke(Color.DARKGREEN,BorderStrokeStyle.SOLID, new CornerRadii(20), new BorderWidths(2))));
        jokerOne.setDisable(true);
        jokerTwo.setDisable(true);
        //jokerThree.setDisable(true);
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
        //jokerThree.setDisable(true);
    }

    public void setOneActivity() {
        oneActivityAnchorPane.setVisible(true);
        threeActivitiesAnchorPane.setVisible(false);
    }

    public void setThreeActivities(){
        oneActivityAnchorPane.setVisible(false);
        threeActivitiesAnchorPane.setVisible(true);
    }
}
