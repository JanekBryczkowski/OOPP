package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import com.google.inject.Stage;
import commons.Question;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class QuestionCtrl {

    private final ServerUtils server;
    private final GameCtrl mainCtrl;
    public Stage primaryStage;

    public boolean multiplayer;
    public boolean answered;
    public int answer;

    public int correctAnswer;
    public int jokerOneActive = 1; //double points
    final int[] secondsPassed = {15};
    Timer myTimer;
    TimerTask task;

    private final int ROUNDS = 10;

    @FXML
    private Label questionText;
    @FXML
    private Label answerOne;
    @FXML
    private Label answerTwo;
    @FXML
    private Label answerThree;
    @FXML
    private Text points;
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
    @FXML
    private Text lowerBoundary;
    @FXML
    private Text upperBoundary;
    @FXML
    private ImageView mainImage;

    double randomLower;
    double randomUpper;
    int lowerBoundaryNumber;
    int upperBoundaryNumber;

    @Inject
    public QuestionCtrl(ServerUtils server, GameCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    private int checkNumberOfCharacters(String string) {
        char[] characters = string.toCharArray();

        return characters.length;
    }

    //Every new round, a new timer and new timertask have to be instantiated
    public void instantiateTimer() {
        secondsPassed[0] = 15;
        myTimer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                secondsPassed[0]--;
                if (!multiplayer) {
                    if (secondsPassed[0] == 1) {
                        secondsLeft.setText("Time left: " + secondsPassed[0] + " second");
                    } else if (secondsPassed[0] > 0)
                        secondsLeft.setText("Time left: " + secondsPassed[0] + " seconds");
                    else {
                        if (oneActivityAnchorPane.isVisible()) {
                            revealAnswersOneActivities();
                        }
                        if (threeActivitiesAnchorPane.isVisible()) {
                            revealAnswersThreeActivities(null, 4);
                        }
                    }
                }
                else {
                    if (secondsPassed[0] == 6) {
                        if (!answered)
                            secondsLeft.setText("Time left to answer: " + (secondsPassed[0] - 5) + " second");
                        else
                            secondsLeft.setText("Time till answers revealed: " + (secondsPassed[0] - 5) + " second");
                    } else if (secondsPassed[0] > 5)
                        if (!answered)
                            secondsLeft.setText("Time left to answer: " + (secondsPassed[0] - 5) + " seconds");
                        else
                            secondsLeft.setText("Time till answers revealed: " + (secondsPassed[0] - 5) + " seconds");
                    else if (secondsPassed[0] > 0) {
                        secondsLeft.setText("Answers revealed! Next round starting soon");
                        if (oneActivityAnchorPane.isVisible()) {
                            revealAnswersOneActivities();
                        }
                        if (threeActivitiesAnchorPane.isVisible()) {
                            if (!answered)
                                revealAnswersThreeActivities(null, 4);
                            else {
                                if (answer == 1)
                                    revealAnswersThreeActivities(answerOnePane, 1);
                                else if (answer == 2)
                                    revealAnswersThreeActivities(answerTwoPane, 2);
                                else
                                    revealAnswersThreeActivities(answerThreePane, 3);
                            }
                        }
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

        Path imageFile = Paths.get("client/src/main/resources/client.activityBank/" + question.activityList.get(0).image_path);
        System.out.println(imageFile);
        try {
            mainImage.setImage(new Image(imageFile.toUri().toURL().toExternalForm()));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        answerOne.setText(question.activityList.get(0).title);
        answerTwo.setText(question.activityList.get(1).title);
        answerThree.setText(question.activityList.get(2).title);
        questionText.setText("Which of these activities takes more energy?");
        questionText.setStyle("-fx-font-size: 47;");
        question.setCorrectAnswer();
        this.correctAnswer = question.correctAnswer;

        enableButtons();

        if (!multiplayer)
            hideSoloPlayerElements();

        instantiateTimer();
        myTimer.scheduleAtFixedRate(task, 1000, 1000);

        jokerTwo.setText("Eliminate one wrong answer");
    }

    public void startTwoActivityQuestion(Question question) {
        Path imageFile = Paths.get("client/src/main/resources/client.activityBank/" + question.activityList.get(0).image_path);
        System.out.println(imageFile);
        try {
            mainImage.setImage(new Image(imageFile.toUri().toURL().toExternalForm()));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        int firstActivityConsumption = question.activityList.get(0).consumption;
        int secondActivityConsumption = question.activityList.get(1).consumption;
        question.setCorrectAnswer();
        this.correctAnswer = question.correctAnswer;

        String finalAnswerString;
        int finalAnswerInteger;
        if (firstActivityConsumption > secondActivityConsumption) {
            finalAnswerInteger = firstActivityConsumption / secondActivityConsumption;
            questionText.setText("How much more energy does (" + question.activityList.get(0).title +
                    ") take comparing to (" + question.activityList.get(1).title + ")?");
            questionText.setStyle("-fx-font-size: 30;");
        } else {
            finalAnswerInteger = secondActivityConsumption / firstActivityConsumption;
            questionText.setText("How much more energy does (" + question.activityList.get(1).title +
                    ") take comparing to (" + question.activityList.get(0).title + ")?");
            questionText.setStyle("-fx-font-size: 30;");
        }
        finalAnswerString = String.valueOf(finalAnswerInteger);

        jokerTwo.setText("Eliminate one wrong answer");

        if (correctAnswer == 1) {
            answerOne.setText(finalAnswerString);
            answerTwo.setText(String.valueOf((int) (finalAnswerInteger * (Math.random() * 139 + 10) / 100)));
            answerThree.setText(String.valueOf((int) (finalAnswerInteger * (Math.random() * 139 + 10) / 100)));
        } else if (correctAnswer == 2) {
            answerOne.setText(String.valueOf((int) (finalAnswerInteger * (Math.random() * 139 + 10) / 100)));
            answerTwo.setText(finalAnswerString);
            answerThree.setText(String.valueOf((int) (finalAnswerInteger * (Math.random() * 139 + 10) / 100)));
        } else if (correctAnswer == 3) {
            answerOne.setText(String.valueOf((int) (finalAnswerInteger * (Math.random() * 139 + 10) / 100)));
            answerTwo.setText(String.valueOf((int) (finalAnswerInteger * (Math.random() * 139 + 10) / 100)));
            answerThree.setText(finalAnswerString);
        }

        if (!multiplayer)
            hideSoloPlayerElements();
        instantiateTimer();
        myTimer.scheduleAtFixedRate(task, 1000, 1000);
        enableButtons();
    }

    /*
    In this function the multiplayer question gets set up. For now, the only thing done is setting the
    question title to the title of the first activity. This needs to be changed so that it checks how long
    the question is. This function will also have to instantiate a timer.
     */
    public void setUpMultiPlayerQuestion(Question question) {
        System.out.println("MP question size" + question.activityList.size());
        removeBorders();
        switch (question.activityList.size()) {
            case (1): {
                startOneActivityQuestion(question);
                setOneActivity();
                break;
            }
            case (2): {
                startTwoActivityQuestion(question);
                setTwoActivities();
                break;
            }
            case (3): {
                startThreeActivityQuestion(question);
                setThreeActivities();
                break;
            }
            default: {
                break;
            }
        }

    }

    public void startOneActivityQuestion(Question question) {
        Path imageFile = Paths.get("client/src/main/resources/client.activityBank/" + question.activityList.get(0).image_path);
        System.out.println(imageFile);
        try {
            mainImage.setImage(new Image(imageFile.toUri().toURL().toExternalForm()));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        answerGivenActivityOne.setDisable(false);
        answerOneInput.setText("");
        answerOneInput.setEditable(true);
        questionText.setText("How much does it take: " + question.activityList.get(0).title + "?");
        questionText.setStyle("-fx-font-size: 37;");

        question.setCorrectAnswer();
        this.correctAnswer = question.correctAnswer;
        round.setText(String.valueOf(correctAnswer));

        if (!multiplayer)
            hideSoloPlayerElements();

        instantiateTimer();
        startTimer();

        jokerTwo.setText("Narrow down the boundaries");
        setUpTheBoundaries();
    }

    private void setUpTheBoundaries() {
        randomLower = (Math.random() * 39 + 1) / 100;
        randomUpper = (Math.random() * 39 + 1) / 100;
        lowerBoundaryNumber = correctAnswer - (int) (correctAnswer * randomLower);
        upperBoundaryNumber = correctAnswer + (int) (correctAnswer * randomUpper);
        lowerBoundary.setText(String.valueOf(lowerBoundaryNumber));
        upperBoundary.setText(String.valueOf(upperBoundaryNumber));

        System.out.println("correct: " + correctAnswer);
        System.out.println("random lower: " + randomLower);
        System.out.println("random upper: " + randomUpper);
        System.out.println("lower: " + lowerBoundaryNumber);
        System.out.println("upper: " + upperBoundaryNumber);
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
    }

    //This function enables the answer buttons when a new round starts
    public void enableButtons() {
        answerOnePane.setDisable(false);
        answerTwoPane.setDisable(false);
        answerThreePane.setDisable(false);

    }

    //Function for when the player OKs the input for 1activity question
    public void answerNumberGiven() {
        if (!multiplayer)
            revealAnswersOneActivities();
        disableButtons();
    }

    //Function for when the player answers one
    public void answerOneGiven() {
        if (!multiplayer)
            revealAnswersThreeActivities(answerOnePane, 1);
        else {
            answered = true;
            answer = 1;
        }
        disableButtons();
    }

    //Function for when the player answers two
    public void answerTwoGiven() {
        if (!multiplayer)
            revealAnswersThreeActivities(answerTwoPane, 2);
        else {
            answered = true;
            answer = 2;
        }
        disableButtons();
    }

    //Function for when the player answers three
    public void answerThreeGiven() {
        if (!multiplayer)
            revealAnswersThreeActivities(answerThreePane, 3);
        else {
            answered = true;
            answer = 3;
        }
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
        myTimer.cancel();

        jokerOne.setDisable(true);
        jokerTwo.setDisable(true);
        switch (correctAnswer) {
            case (1):
                answerOnePane.setStyle("-fx-border-color: green; -fx-border-width: 5; -fx-border-radius: 20;");
                //answerOnePane.setBorder(new Border(new BorderStroke(Color.GREEN,BorderStrokeStyle.SOLID, new CornerRadii(20), new BorderWidths(5))));
                break;
            case (2):
                answerTwoPane.setStyle("-fx-border-color: green; -fx-border-width: 5; -fx-border-radius: 20;");
                //answerTwoPane.setBorder(new Border(new BorderStroke(Color.ORANGE,BorderStrokeStyle.SOLID, new CornerRadii(20), new BorderWidths(5))));
                break;
            case (3):
                answerThreePane.setStyle("-fx-border-color: green; -fx-border-width: 5; -fx-border-radius: 20;");
                //answerThreePane.setBorder(new Border(new BorderStroke(Color.ORANGE,BorderStrokeStyle.SOLID, new CornerRadii(20), new BorderWidths(5))));
                break;
            default:
                break;
        }
        if (correctAnswer != click && !(click > 3)) {
            clicked.setStyle("-fx-border-color: red; -fx-border-width: 5; -fx-border-radius: 20;");
            //clicked.setBorder(new Border(new BorderStroke(Color.RED,BorderStrokeStyle.SOLID, new CornerRadii(20), new BorderWidths(5))));
        } else if (correctAnswer == click && !(click > 3)) {
            System.out.println(mainCtrl.points);
            mainCtrl.points += (jokerOneActive * 10 * secondsPassed[0]);
            System.out.println(mainCtrl.points);
        }

        points.setText(mainCtrl.points + " points");
        if (!multiplayer)
            newQuestion();
        else {
            answer = 0;
            answered = false;
        }
    }

    /*public int answerGivenActivityOne() {
        return Integer.valueOf(answerOneInput.getText());
        revealAnswersOneActivities(answer);
    }*/

    public void revealAnswersOneActivities() {
        myTimer.cancel();

        jokerOne.setDisable(true);
        jokerTwo.setDisable(true);
        int answerGiven;
        String input = answerOneInput.getText();
        if (input.equals("") || input == null) {
            answerGiven = 0;
        } else {
            answerGiven = Integer.parseInt(input);
        }

        answerGivenActivityOne.setDisable(true);
        answerOneInput.setEditable(false);
        answerOneInput.setText("correct: " + correctAnswer);
        if (answerGiven == correctAnswer) {
            mainCtrl.points += (jokerOneActive * 10 * secondsPassed[0]);
            answerOneInput.setBorder(new Border(new BorderStroke(Color.GREEN, BorderStrokeStyle.SOLID, new CornerRadii(40), new BorderWidths(2))));
        } else if (answerGiven > lowerBoundaryNumber && answerGiven < upperBoundaryNumber) {
            mainCtrl.points += (jokerOneActive * calculatePointsForOpenAnswer(correctAnswer, answerGiven));
            answerOneInput.setBorder(new Border(new BorderStroke(Color.ORANGE, BorderStrokeStyle.SOLID, new CornerRadii(40), new BorderWidths(2))));
        } else {
            mainCtrl.points += (jokerOneActive * calculatePointsForOpenAnswer(correctAnswer, answerGiven));
            answerOneInput.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, new CornerRadii(40), new BorderWidths(2))));
        }
        points.setText(mainCtrl.points + " points");
        if (!multiplayer)
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
                    if (mainCtrl.round > ROUNDS) {
                        mainCtrl.showLeaderBoard();
                    } else {
                        try {
                            mainCtrl.SoloGameRound();
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }, 1000);
    }

    //This function returns to the splash screen (for when a user clicks 'back')
    public void backToSplash() {
        System.out.println(mainCtrl.getMode());
        mainCtrl.subscription.unsubscribe();
        mainCtrl.points = 0;
        mainCtrl.round = 1;
        mainCtrl.username = "";
        mainCtrl.firstJokerUsed = false;
        mainCtrl.secondJokerUsed = false;
        jokerOne.setStyle("-fx-border-width: 0");
        jokerTwo.setStyle("-fx-border-width: 0");
        jokerOne.setDisable(false);
        jokerTwo.setDisable(false);
        answerOnePane.setStyle("-fx-border-width: 0;");
        answerTwoPane.setStyle("-fx-border-width: 0;");
        answerThreePane.setStyle("-fx-border-width: 0;");
        myTimer.cancel();
        points.setText("0 points");
        mainCtrl.showSplashScreen();
        myTimer.cancel();
    }

    //Function for when joker one is pressed
    public void jokerOne() {
        if (!mainCtrl.firstJokerUsed) {
            this.jokerOneActive = 2;
            jokerOne.setStyle("-fx-border-color: darkgreen; -fx-border-width: 5; -fx-border-radius: 30;");
            //jokerOne.setBorder(new Border(new BorderStroke(Color.DARKGREEN, BorderStrokeStyle.SOLID, new CornerRadii(20), new BorderWidths(2))));
            jokerOne.setDisable(true);
            jokerTwo.setDisable(true);
            //jokerThree.setDisable(true);
            mainCtrl.firstJokerUsed = true;
        } else {
            jokerOne.setDisable(true);
        }
    }

    //Function for joker two (Eliminating wrong answer)
    public void jokerTwo() {
        if (!mainCtrl.secondJokerUsed) {
            if (oneActivityAnchorPane.isVisible()) {
                int difference = (int) (Math.random() * (correctAnswer - lowerBoundaryNumber));
                int newLowerBoundaryNumber = lowerBoundaryNumber + difference;
                int newUpperBoundaryNumber = upperBoundaryNumber - difference;
                lowerBoundary.setText(String.valueOf(newLowerBoundaryNumber));
                upperBoundary.setText(String.valueOf(newUpperBoundaryNumber));
                jokerTwo.setStyle("-fx-border-color: darkgreen; -fx-border-width: 5; -fx-border-radius: 30;");
                //jokerTwo.setBorder(new Border(new BorderStroke(Color.DARKGREEN, BorderStrokeStyle.SOLID, new CornerRadii(20), new BorderWidths(2))));
                jokerOne.setDisable(true);
                jokerTwo.setDisable(true);
                //jokerThree.setDisable(true);
                mainCtrl.secondJokerUsed = true;
            } else if (threeActivitiesAnchorPane.isVisible()) {
                int random = correctAnswer;
                while (random == correctAnswer) {
                    random = (int) (Math.random() * 3 + 1);
                }

                switch (random) {
                    case (1):
                        answerOnePane.setDisable(true);
                        answerOnePane.setStyle("-fx-border-color: grey; -fx-border-width: 5; -fx-border-radius: 20;");
                        //answerOnePane.setBorder(new Border(new BorderStroke(Color.GREY, BorderStrokeStyle.SOLID, new CornerRadii(20), new BorderWidths(2))));
                        break;
                    case (2):
                        answerTwoPane.setDisable(true);
                        answerTwoPane.setStyle("-fx-border-color: grey; -fx-border-width: 5; -fx-border-radius: 20;");
                        //answerTwoPane.setBorder(new Border(new BorderStroke(Color.GREY, BorderStrokeStyle.SOLID, new CornerRadii(20), new BorderWidths(2))));
                        break;
                    case (3):
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
                mainCtrl.secondJokerUsed = true;
            }
        } else {
            jokerTwo.setDisable(true);
        }
    }

    public void setOneActivity() {
        oneActivityAnchorPane.setVisible(true);
        threeActivitiesAnchorPane.setVisible(false);
    }

    public void setTwoActivities() {
        oneActivityAnchorPane.setVisible(false);
        threeActivitiesAnchorPane.setVisible(true);
    }

    public void setThreeActivities() {
        oneActivityAnchorPane.setVisible(false);
        threeActivitiesAnchorPane.setVisible(true);
    }

    public int calculatePointsForOpenAnswer(int correctAnswer, int givenAnswer) {
        if (givenAnswer < lowerBoundaryNumber || givenAnswer > upperBoundaryNumber) {
            return 0;
        } else if (correctAnswer > givenAnswer) {
            double percentage = ((double) (givenAnswer - lowerBoundaryNumber) / (double) (correctAnswer - lowerBoundaryNumber));
            return (int) (percentage * 10 * secondsPassed[0]);
        } else {
            double percentage = ((double) (upperBoundaryNumber - givenAnswer) / (double) (upperBoundaryNumber - correctAnswer));
            return (int) (percentage * 10 * secondsPassed[0]);
        }
    }

    public void resetJokers() {
        jokerOne.setStyle("-fx-border-width: 0");
        jokerTwo.setStyle("-fx-border-width: 0");
    }

    public void resetPoints() {
        points.setText("0 points");
    }

    public void validateInput() {
        answerOneInput.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    answerOneInput.setText(newValue.replaceAll("[^\\d]", ""));
                } else {
                    answerOneInput.setText(newValue);
                }
            }
        });
    }
}
