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
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.util.*;

public class QuestionCtrl {

    private final ServerUtils server;
    private final GameCtrl mainCtrl;
    public Stage primaryStage;

    public int correctAnswer;
    public int jokerOneActive = 1; //double points
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
    public Button jokerOne;
    @FXML
    public Button jokerTwo;
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
    @FXML
    private TextField answerOneInput;
    @FXML
    private Button answerGivenActivityOne;

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
                if (secondsPassed[0] == 1) {
                    secondsLeft.setText("Time left: " + secondsPassed[0] + " second");
                } else if (secondsPassed[0] > 0)
                    secondsLeft.setText("Time left: " + secondsPassed[0] + " seconds");
                else {
                    if (oneActivityAnchorPane.isVisible()) {
                        myTimer.cancel();
                        revealAnswersOneActivities(Integer.MIN_VALUE);
                    }
                    if (threeActivitiesAnchorPane.isVisible()) {
                        myTimer.cancel();
                        revealAnswersThreeActivities(null, 4);
                    }
                }

            }
        };
    }

    /*
    This function is a setup for the GameScreen.
    A question is given as input and this question is displayed on the screen.
     */
    public void startThreeActivityQuestion(Question question) {
        List<Activity> activityList = question.activityList;
        answerOne.setText(question.activityList.get(0).title);
        answerTwo.setText(question.activityList.get(1).title);
        answerThree.setText(question.activityList.get(2).title);
        question.setCorrectAnswer();
        this.correctAnswer = question.correctAnswer;

        enableButtons();

        hideSoloPlayerElements();

        instantiateTimer();

        myTimer.scheduleAtFixedRate(task, 1000, 1000);
    }

    public void startTwoActivityQuestion(Question question) {
        List<Activity> activityList = question.activityList;
        int firstActivityConsumption = question.activityList.get(0).consumption;
        int secondActivityConsumption = question.activityList.get(1).consumption;
        question.setCorrectAnswer();
        this.correctAnswer = question.correctAnswer;

        answerOne.setText(String.valueOf(correctAnswer));
        answerTwo.setText(String.valueOf((int) (correctAnswer * Math.random())));
        answerThree.setText(String.valueOf((int) (correctAnswer * Math.random())));

        enableButtons();

        hideSoloPlayerElements();

        instantiateTimer();

        myTimer.scheduleAtFixedRate(task, 1000, 1000);
    }


    public void startOneActivityQuestion(Question question) {
        answerGivenActivityOne.setDisable(false);
        answerOneInput.setText("");
        answerOneInput.setEditable(true);
        List<Activity> activityList = question.activityList;
        questionText.setText(question.activityList.get(0).title);
        question.setCorrectAnswer();
        this.correctAnswer = question.correctAnswer;
        round.setText(String.valueOf(String.valueOf(correctAnswer)));

        hideSoloPlayerElements();

        instantiateTimer();
        startTimer();
    }

    //This functions starts the timer. When the timer finishes, the answers are revealed
    public void startTimer() {

        myTimer.scheduleAtFixedRate(task, 1000, 1000);
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
        myTimer.cancel();
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
        revealAnswersThreeActivities(answerOnePane, 0);
        disableButtons();
    }

    //Function for when the player answers two
    public void answerTwoGiven() {
        myTimer.cancel();
        revealAnswersThreeActivities(answerTwoPane, 1);
        disableButtons();
    }

    //Function for when the player answers three
    public void answerThreeGiven() {
        myTimer.cancel();
        revealAnswersThreeActivities(answerThreePane, 2);
        disableButtons();
    }

    //Remove the coloured borders when a new round starts
    public void removeBorders() {
        answerOnePane.setStyle("-fx-border-width: 0");
        answerTwoPane.setStyle("-fx-border-width: 0");
        answerThreePane.setStyle("-fx-border-width: 0");
        //answerOnePane.setBorder(null);
        //answerTwoPane.setBorder(null);
        //answerThreePane.setBorder(null);
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
    public void revealAnswersThreeActivities(Pane clicked, int click) {
        int xd = 1;
        switch (xd) {
            case (0):
                answerOnePane.setStyle("-fx-border-color: green; -fx-border-width: 5; -fx-border-radius: 20;");
                //answerOnePane.setBorder(new Border(new BorderStroke(Color.GREEN,BorderStrokeStyle.SOLID, new CornerRadii(20), new BorderWidths(5))));
                break;
            case (1):
                answerTwoPane.setStyle("-fx-border-color: green; -fx-border-width: 5; -fx-border-radius: 20;");
                //answerTwoPane.setBorder(new Border(new BorderStroke(Color.ORANGE,BorderStrokeStyle.SOLID, new CornerRadii(20), new BorderWidths(5))));
                break;
            case (2):
                answerThreePane.setStyle("-fx-border-color: green; -fx-border-width: 5; -fx-border-radius: 20;");
                //answerThreePane.setBorder(new Border(new BorderStroke(Color.ORANGE,BorderStrokeStyle.SOLID, new CornerRadii(20), new BorderWidths(5))));
                break;
            default:
                break;
        }
        if (correctAnswer != click && !(click > 2)) {
            clicked.setStyle("-fx-border-color: red; -fx-border-width: 5; -fx-border-radius: 20;");
            //clicked.setBorder(new Border(new BorderStroke(Color.RED,BorderStrokeStyle.SOLID, new CornerRadii(20), new BorderWidths(5))));
        } else if (correctAnswer == click && !(click > 2)) {
            mainCtrl.points += (jokerOneActive * 100);
        }

        points.setText(mainCtrl.points + " points");
        newQuestion();
    }

    public void answerGivenActivityOne() {
        int answer = Integer.valueOf(answerOneInput.getText());
        revealAnswersOneActivities(answer);
    }

    public void revealAnswersOneActivities(int answerGiven) {

        answerGivenActivityOne.setDisable(true);
        answerOneInput.setEditable(false);
        myTimer.cancel();
        answerOneInput.setText(String.valueOf(correctAnswer));
        if (answerGiven == correctAnswer) {
            mainCtrl.points += (jokerOneActive * 100);
            answerOneInput.setBorder(new Border(new BorderStroke(Color.GREEN, BorderStrokeStyle.SOLID, new CornerRadii(40), new BorderWidths(2))));
        } else {
            answerOneInput.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, new CornerRadii(40), new BorderWidths(2))));
        }
        points.setText(String.valueOf(mainCtrl.points));
        newQuestion();
    }


    //This function will start a new question after 5 seconds.
    public void newQuestion() {
        Timer myTimers = new Timer();
        myTimers.schedule(new TimerTask() {

            @Override
            public void run() {
                Platform.runLater(() -> {
                    removeBorders();
                    round.setText("round " + ++mainCtrl.round);
                    if (mainCtrl.round > 5) {
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
        GameCtrl.firstJokerUsed = false;
        GameCtrl.secondJokerUsed = false;
        jokerOne.setStyle("-fx-border-width: 0");
        jokerTwo.setStyle("-fx-border-width: 0");
        mainCtrl.showSplashScreen();
    }

    //Function for when joker one is pressed
    public void jokerOne() {
        if (!GameCtrl.firstJokerUsed) {
            this.jokerOneActive = 2;
            jokerOne.setStyle("-fx-border-color: darkgreen; -fx-border-width: 5; -fx-border-radius: 30;");
            //jokerOne.setBorder(new Border(new BorderStroke(Color.DARKGREEN, BorderStrokeStyle.SOLID, new CornerRadii(20), new BorderWidths(2))));
            jokerOne.setDisable(true);
            jokerTwo.setDisable(true);
            //jokerThree.setDisable(true);
            GameCtrl.firstJokerUsed = true;
        } else {
            jokerOne.setDisable(true);
        }
    }

    //Function for joker two (Eliminating wrong answer)
    public void jokerTwo() {
        if (!GameCtrl.secondJokerUsed) {
            Random random = new Random();
            int i = random.nextInt(2) + 1;
            int disable = (correctAnswer + i) % 3;
            switch (disable) {
                case (0):
                    answerOnePane.setDisable(true);
                    answerOnePane.setStyle("-fx-border-color: grey; -fx-border-width: 5; -fx-border-radius: 20;");
                    //answerOnePane.setBorder(new Border(new BorderStroke(Color.GREY, BorderStrokeStyle.SOLID, new CornerRadii(20), new BorderWidths(2))));
                    break;
                case (1):
                    answerTwoPane.setDisable(true);
                    answerTwoPane.setStyle("-fx-border-color: grey; -fx-border-width: 5; -fx-border-radius: 20;");
                    //answerTwoPane.setBorder(new Border(new BorderStroke(Color.GREY, BorderStrokeStyle.SOLID, new CornerRadii(20), new BorderWidths(2))));
                    break;
                case (2):
                    answerThreePane.setDisable(true);
                    answerThreePane.setStyle("-fx-border-color: grey; -fx-border-width: 5; -fx-border-radius: 20;");
                    //answerThreePane.setBorder(new Border(new BorderStroke(Color.GREY, BorderStrokeStyle.SOLID, new CornerRadii(20), new BorderWidths(2))));
                    break;
                default:
                    break;
            }
            jokerTwo.setStyle("-fx-border-color: darkgreen; -fx-border-width: 5; -fx-border-radius: 30;");
            //jokerTwo.setBorder(new Border(new BorderStroke(Color.DARKGREEN, BorderStrokeStyle.SOLID, new CornerRadii(20), new BorderWidths(2))));
            jokerOne.setDisable(true);
            jokerTwo.setDisable(true);
            //jokerThree.setDisable(true);
            GameCtrl.secondJokerUsed = true;
        } else {
            jokerTwo.setDisable(true);
        }
    }

    public void setOneActivity() {
        oneActivityAnchorPane.setVisible(true);
        threeActivitiesAnchorPane.setVisible(false);
    }

    public void setThreeActivities() {
        oneActivityAnchorPane.setVisible(false);
        threeActivitiesAnchorPane.setVisible(true);
    }

    public void setTwoActivities() {
        oneActivityAnchorPane.setVisible(false);
        threeActivitiesAnchorPane.setVisible(true);
    }
}
