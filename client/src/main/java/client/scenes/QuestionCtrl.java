package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import com.google.inject.Stage;
import commons.Question;
import commons.WebsocketMessage;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
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
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.util.*;
import java.util.Timer;

public class QuestionCtrl {

    private final ServerUtils server;
    private final GameCtrl gameCtrl;
    public Stage primaryStage;

    public boolean multiplayer;
    public boolean answered;
    public int answer;

    public int correctAnswer;
    public int jokerOneActive = 1; //double points
    int[] secondsPassed = {15};
    Timer myTimer;
    TimerTask task;

    private final int ROUNDS = 3;

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
    private Text singlePlayerSecondsLeft;
    @FXML
    private Text multiPlayerSecondsLeft;
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
    private Text gainedPoints;
    @FXML
    private Label emojiOneLabel;

    @FXML
    private Label emojiTwoLabel;

    @FXML
    private Label emojiThreeLabel;

    @FXML
    private ImageView mainImage;

    @FXML
    private AnchorPane mainAnchorPane;

    @FXML
    private Arc clock;

    double randomLower;
    double randomUpper;
    int lowerBoundaryNumber;
    int upperBoundaryNumber;

    List<Color> colorsForClockSinglePlayer = Arrays.asList(new Color(0, 0.3, 0.15, 1),
            new Color(0.07, 0.51, 0.23, 1),
            new Color(0.28, 0.75, 0.33, 1),
            new Color(0.57, 0.94, 0.53, 1),
            new Color(1, 0.95, 0.14, 1),
            new Color(1, 0.79, 0.01, 1),
            new Color(1, 0.74, 0.5, 1),
            new Color(0.99, 0.66, 0.35, 1),
            new Color(1, 0.62, 0.27, 1),
            new Color(0.97, 0.43, 0.07, 1),
            new Color(0.99, 0.31, 0.31, 1),
            new Color(0.91, 0.23, 0.08, 1),
            new Color(0.54, 0.06, 0.05, 1),
            new Color(0.39, 0.02, 0.02, 1),
            new Color(0, 0, 0, 1),
            new Color(0, 0, 0, 1));

    List<Color> colorsForClockMultiPlayer = Arrays.asList(new Color(0, 0.3, 0.15, 1),
            new Color(0.28, 0.75, 0.33, 1),
            new Color(0.57, 0.94, 0.53, 1),
            new Color(1, 0.95, 0.14, 1),
            new Color(1, 0.74, 0.5, 1),
            new Color(1, 0.62, 0.27, 1),
            new Color(0.97, 0.43, 0.07, 1),
            new Color(0.91, 0.23, 0.08, 1),
            new Color(0.54, 0.06, 0.05, 1),
            new Color(0.39, 0.02, 0.02, 1),
            new Color(0, 0, 0, 1),
            new Color(0, 0, 0, 1));

    /**
     * Constructor for QuestionCtrl and instantiation of the server and the gameCtrl.
     *
     * @param server
     * @param gameCtrl
     */
    @Inject
    public QuestionCtrl(ServerUtils server, GameCtrl gameCtrl) {
        this.server = server;
        this.gameCtrl = gameCtrl;
        //answersGiven.setText(gameCtrl.round + " / 10 rounds");
    }

    /**
     * Every new round, a new timer and new timer task have to be instantiated.
     * The run method from the TimerTask class is called to start the 15s countdown timer.
     * The mode(single player/ multiplayer) is checked because if the mode is in solo mode, then once the user has answered,
     * the answer can be revealed immediately. However, in multiplayer mode, the user needs to wait the whole 15s for everyone
     * else in the game to have answered.
     */
    public void instantiateTimer() {
        secondsPassed[0] = 15;
        myTimer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                if (!multiplayer) {
                    secondsPassed[0]--;
                    clock.setStartAngle(0.0);
                    double proportion = (double) secondsPassed[0] / 15.0;
                    double finalNumber = proportion * 360;
                    clock.setLength(finalNumber);
                    clock.setType(ArcType.ROUND);
                    clock.setFill(colorsForClockSinglePlayer.get(15 - secondsPassed[0]));
                    if (secondsPassed[0] == 1) {
                        singlePlayerSecondsLeft.setText("Time left: " + secondsPassed[0] + " second");
                    } else if (secondsPassed[0] > 0)
                        singlePlayerSecondsLeft.setText("Time left: " + secondsPassed[0] + " seconds");
                    else {
                        if (oneActivityAnchorPane.isVisible()) {
                            revealAnswersOneActivities();
                        }
                        if (threeActivitiesAnchorPane.isVisible()) {
                            revealAnswersThreeActivities(null, 4);
                        }
                    }
                } else {
                    secondsPassed[0]--;
                    clock.setStartAngle(0.0);
                    double proportion = (double) (secondsPassed[0] - 5) / 10.0;
                    double finalNumber = proportion * 360;
                    clock.setLength(finalNumber);
                    clock.setType(ArcType.ROUND);
                    clock.setFill(colorsForClockMultiPlayer.get(15 - secondsPassed[0]));
                    if (secondsPassed[0] == 6) {
                        if (!answered)
                            multiPlayerSecondsLeft.setText("Time left to answer: " + (secondsPassed[0] - 5) + " second");
                        else
                            multiPlayerSecondsLeft.setText("Time till answers revealed: " + (secondsPassed[0] - 5) + " second");
                    } else if (secondsPassed[0] > 5)
                        if (!answered)
                            multiPlayerSecondsLeft.setText("Time left to answer: " + (secondsPassed[0] - 5) + " seconds");
                        else
                            multiPlayerSecondsLeft.setText("Time till answers revealed: " + (secondsPassed[0] - 5) + " seconds");
                    else if (secondsPassed[0] > 0) {
                        multiPlayerSecondsLeft.setText("Answers revealed! Starting next round!");
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

    /**
     * This function is a setup for the GameScreen when there is a three activity question.
     * A question is given as input and this question is displayed on the screen.
     *
     * @param question is the question that will be set up in the Scene.
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
        answersGiven.setText(gameCtrl.round + " / 10 rounds");
    }

    /**
     * This function is a setup for the GameScreen when there is a one activity question.
     *
     * @param question : A question is given as input and this question is displayed on the screen.
     */
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
        answersGiven.setText(gameCtrl.round + " / 10 rounds");
    }

    /**
     * In this function the multiplayer question gets set up. For now, the only thing done is setting the
     * question title to the title of the first activity. This needs to be changed so that it checks how long
     * the question is. This function will also have to instantiate a timer.
     *
     * @param question given as input and this question is displayed on the screen.
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

    public void setupJoker() {
        emojiOne.setOnMouseClicked(event -> {
            WebsocketMessage websocketMessage = new WebsocketMessage("EMOJIONE");
            websocketMessage.setEmojiUsername(gameCtrl.username);
            server.send("/topic/question" + gameCtrl.joinedLobby, websocketMessage);
        });
        emojiTwo.setOnMouseClicked(event -> {
            WebsocketMessage websocketMessage = new WebsocketMessage("EMOJITWO");
            websocketMessage.setEmojiUsername(gameCtrl.username);
            server.send("/topic/question" + gameCtrl.joinedLobby, websocketMessage);
        });
        emojiThree.setOnMouseClicked(event -> {
            WebsocketMessage websocketMessage = new WebsocketMessage("EMOJITHREE");
            websocketMessage.setEmojiUsername(gameCtrl.username);
            server.send("/topic/question" + gameCtrl.joinedLobby, websocketMessage);
        });
    }

    /**
     * This function is a setup for the GameScreen when there is a three activity question.
     * A question is given as input and this question is displayed on the screen.
     *
     * @param question given as input and this question is displayed on the screen.
     */
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

        answersGiven.setText(gameCtrl.round + " / 10 rounds");
    }

    /**
     * Function containing an algorithm to calculate the boundaries around a one activity question.
     * These boundaries are not too far off the answer, enough to guide the user without revealing the answer and are
     * also computed in a way that the answer will not lie in the middle of both boundaries.
     */
    private void setUpTheBoundaries() {
        randomLower = (Math.random() * 39 + 1) / 100;
        randomUpper = (Math.random() * 39 + 1) / 100;
        lowerBoundaryNumber = correctAnswer - (int) (correctAnswer * randomLower);
        upperBoundaryNumber = correctAnswer + (int) (correctAnswer * randomUpper);
        lowerBoundary.setText(formatNumber(lowerBoundaryNumber));
        upperBoundary.setText(formatNumber(upperBoundaryNumber));
    }

    /**
     * Correct format of a number as an int.
     *
     * @param number that will be formatted.
     * @return String of formatted number.
     */
    private String formatNumber(int number) {
        NumberFormat myFormat = NumberFormat.getInstance();
        myFormat.setGroupingUsed(true);
        return myFormat.format(number);
    }

    /**
     * Correct format of a number as a String.
     *
     * @param number that will be formatted.
     * @return String of correctly formatted number.
     */
    private String formatNumberString(String number) {
        if (number.equals("")) return "";
        else {
            int numberInInt = Integer.parseInt(number);
            NumberFormat myFormat = NumberFormat.getInstance();
            myFormat.setGroupingUsed(true);
            return myFormat.format(numberInInt);
        }
    }

    /**
     * This functions starts the timer. When the timer finishes, the answers are revealed.
     */
    public void startTimer() {
        myTimer.scheduleAtFixedRate(task, 1000, 1000);
    }


    /**
     * This function is for hiding the elements on solo player that do not make sense.
     */
    public void hideSoloPlayerElements() {
//        jokerThree.setVisible(false);
        emojiOne.setDisable(false);
        emojiTwo.setDisable(false);
        emojiThree.setDisable(false);

    }


    /**
     * This function disables the answer buttons when an answer has been clicked.
     */
    public void disableButtons() {
        answerOnePane.setDisable(true);
        answerTwoPane.setDisable(true);
        answerThreePane.setDisable(true);
    }

    /**
     * This function enables the answer buttons when a new round starts.
     */
    public void enableButtons() {
        answerOnePane.setDisable(false);
        answerTwoPane.setDisable(false);
        answerThreePane.setDisable(false);
    }

    /**
     * Function for when the player OKs the input for a one activity question.
     * UNUSED.
     */
    public void answerNumberGiven() {
        if (!multiplayer)
            revealAnswersOneActivities();
        disableButtons();
    }

    /**
     * Function for when the player answers one.
     */
    public void answerOneGiven() {
        if (!multiplayer)
            revealAnswersThreeActivities(answerOnePane, 1);
        else {
            answered = true;
            answer = 1;
        }
        disableButtons();
    }

    /**
     * Function for when the player answers two.
     */
    public void answerTwoGiven() {
        if (!multiplayer)
            revealAnswersThreeActivities(answerTwoPane, 2);
        else {
            answered = true;
            answer = 2;
        }
        disableButtons();
    }

    /**
     * Function for when the player answers three.
     */
    public void answerThreeGiven() {
        if (!multiplayer)
            revealAnswersThreeActivities(answerThreePane, 3);
        else {
            answered = true;
            answer = 3;
        }
        disableButtons();
    }

    /**
     * Remove the coloured borders when a new round starts.
     */
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

    /**
     * This function reveals the correct and wrong answer by colouring the borders of the answers.
     * It adds points and calls the newQuestion function.
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
            int pointsGainedInRound = jokerOneActive * 10 * secondsPassed[0];
            gainedPoints.setText("+ " + pointsGainedInRound + " points");

            gainedPoints.setVisible(true);
            Timer myTimers = new Timer();
            myTimers.schedule(new TimerTask() {

                @Override
                public void run() {
                    gainedPoints.setVisible(false);
                }
            }, 2000);

            gameCtrl.points += pointsGainedInRound;
        }
        points.setText(gameCtrl.points + " points");
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

    /**
     * This function reveals the correct answer by filling in the answer text box with the
     * correct number.
     * It adds points and calls the newQuestion function.
     */
    public void revealAnswersOneActivities() {
        myTimer.cancel();

        jokerOne.setDisable(true);
        jokerTwo.setDisable(true);
        int answerGiven;
        String input = answerOneInput.getText();
        if (input.equals("") || input == null) {
            answerGiven = 0;
        } else {
            answerGiven = formatNumberBack(input);
        }

        answerGivenActivityOne.setDisable(true);
        answerOneInput.setEditable(false);
        answerOneInput.setText("correct: " + correctAnswer);
        if (answerGiven == correctAnswer) {
            int pointsGainedInRound = jokerOneActive * 10 * secondsPassed[0];
            gameCtrl.points += pointsGainedInRound;
            gainedPoints.setText("+ " + pointsGainedInRound + " points");

            gainedPoints.setVisible(true);
            Timer myTimers = new Timer();
            myTimers.schedule(new TimerTask() {

                @Override
                public void run() {
                    gainedPoints.setVisible(false);
                }
            }, 2000);

            answerOneInput.setBorder(new Border(new BorderStroke(Color.GREEN, BorderStrokeStyle.SOLID, new CornerRadii(40), new BorderWidths(2))));
        } else if (answerGiven > lowerBoundaryNumber && answerGiven < upperBoundaryNumber) {
            int pointsGainedInRound = jokerOneActive * calculatePointsForOpenAnswer(correctAnswer, answerGiven);
            gameCtrl.points += pointsGainedInRound;
            gainedPoints.setText("+ " + pointsGainedInRound + " points");

            gainedPoints.setVisible(true);
            Timer myTimers = new Timer();
            myTimers.schedule(new TimerTask() {

                @Override
                public void run() {
                    gainedPoints.setVisible(false);
                }
            }, 2000);

            answerOneInput.setBorder(new Border(new BorderStroke(Color.ORANGE, BorderStrokeStyle.SOLID, new CornerRadii(40), new BorderWidths(2))));
        } else {
            int pointsGainedInRound = jokerOneActive * calculatePointsForOpenAnswer(correctAnswer, answerGiven);
            gameCtrl.points += pointsGainedInRound;
            gainedPoints.setText("+ " + pointsGainedInRound + " points");

            gainedPoints.setVisible(true);
            Timer myTimers = new Timer();
            myTimers.schedule(new TimerTask() {

                @Override
                public void run() {
                    gainedPoints.setVisible(false);
                }
            }, 2000);

            answerOneInput.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, new CornerRadii(40), new BorderWidths(2))));
        }
        points.setText(gameCtrl.points + " points");
        if (!multiplayer)
            newQuestion();
    }

    /**
     * Formats numbers so that large numbers do not contain any ','.
     *
     * @param number as a String.
     * @return a number.
     */
    public int formatNumberBack(String number) {
        if (number.equals("")) return 0;
        String number2 = number.replaceAll(",", "");
        return Integer.parseInt(number2);
    }

    /**
     * This function will start a new question after 5 seconds.
     */
    public void newQuestion() {
        Timer myTimers = new Timer();
        myTimers.schedule(new TimerTask() {

            @Override
            public void run() {
                Platform.runLater(() -> {
                    removeBorders();
                    round.setText("round " + ++gameCtrl.round);
                    if (gameCtrl.round > ROUNDS) {
                        gameCtrl.showLeaderBoard();
                    } else {
                        try {
                            gameCtrl.SoloGameRound();
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }, 1000);
    }

    /**
     * This function returns to the splash screen (for when a user clicks 'BACK') from any round in the question page.
     */
    public void backToSplash() {
        if (multiplayer) gameCtrl.subscription.unsubscribe();
        gainedPoints.setText("");
        gameCtrl.points = 0;
        gameCtrl.round = 1;
        gameCtrl.firstJokerUsed = false;
        gameCtrl.secondJokerUsed = false;
        jokerOne.setStyle("-fx-border-width: 0");
        jokerTwo.setStyle("-fx-border-width: 0");
        jokerOne.setDisable(false);
        jokerTwo.setDisable(false);
        answerOnePane.setStyle("-fx-border-width: 0;");
        answerTwoPane.setStyle("-fx-border-width: 0;");
        answerThreePane.setStyle("-fx-border-width: 0;");
        myTimer.cancel();
        points.setText("0 points");
        gameCtrl.showSplashScreen();
        myTimer.cancel();
    }

    /**
     * Function for when joker one is pressed. Jokers are disabled for the remainder of the round.
     */
    public void jokerOne() {
        if (!gameCtrl.firstJokerUsed) {
            this.jokerOneActive = 2;
            jokerOne.setStyle("-fx-border-color: darkgreen; -fx-border-width: 5; -fx-border-radius: 30;");
            //jokerOne.setBorder(new Border(new BorderStroke(Color.DARKGREEN, BorderStrokeStyle.SOLID, new CornerRadii(20), new BorderWidths(2))));
            jokerOne.setDisable(true);
            jokerTwo.setDisable(true);
            //jokerThree.setDisable(true);
            gameCtrl.firstJokerUsed = true;
        } else {
            jokerOne.setDisable(true);
        }
    }

    /**
     * Function for joker two (Eliminating wrong answer).
     */
    public void jokerTwo() {
        if (!gameCtrl.secondJokerUsed) {
            if (oneActivityAnchorPane.isVisible()) {
                int difference = (int) (Math.random() * (correctAnswer - lowerBoundaryNumber));
                int newLowerBoundaryNumber = lowerBoundaryNumber + difference;
                int newUpperBoundaryNumber = upperBoundaryNumber - difference;
                lowerBoundary.setText(formatNumber(newLowerBoundaryNumber));
                upperBoundary.setText(formatNumber(newUpperBoundaryNumber));
                jokerTwo.setStyle("-fx-border-color: darkgreen; -fx-border-width: 5; -fx-border-radius: 30;");
                //jokerTwo.setBorder(new Border(new BorderStroke(Color.DARKGREEN, BorderStrokeStyle.SOLID, new CornerRadii(20), new BorderWidths(2))));
                jokerOne.setDisable(true);
                jokerTwo.setDisable(true);
                //jokerThree.setDisable(true);
                gameCtrl.secondJokerUsed = true;
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
                gameCtrl.secondJokerUsed = true;
            }
        } else {
            jokerTwo.setDisable(true);
        }
    }

    /**
     * Sets the anchor pane for a one activity question visible in the Question Screen.
     */
    public void setOneActivity() {
        oneActivityAnchorPane.setVisible(true);
        threeActivitiesAnchorPane.setVisible(false);
    }

    /**
     * Sets the anchor pane for a two activity question visible in the Question Screen.
     */
    public void setTwoActivities() {
        oneActivityAnchorPane.setVisible(false);
        threeActivitiesAnchorPane.setVisible(true);
    }

    /**
     * Sets the anchor pane for a three activity question visible in the Question Screen.
     */
    public void setThreeActivities() {
        oneActivityAnchorPane.setVisible(false);
        threeActivitiesAnchorPane.setVisible(true);
    }

    /**
     * Calculates the number of points that should be awarded to a user in a one activity question.
     * This is calculated by an algorithm that considers two things: how close your numerical answer
     * was to the actual answer and how long you took to answer. The closer you are to the answer and
     * the less time you take, the more points you get awarded.
     *
     * @param correctAnswer is the correct numerical answer.
     * @param givenAnswer   is the answer input by the user.
     * @return int representing the points awarded to the user.
     */
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

    /**
     * Resets the borders of the jokers in the next round after they are used.
     */
    public void resetJokers() {
        jokerOne.setStyle("-fx-border-width: 0");
        jokerTwo.setStyle("-fx-border-width: 0");
    }

    /**
     * Setting the text of the points to 0 if the user didn't achieve any points in that round.
     */
    public void resetPoints() {
        points.setText("0 points");
    }

    /**
     * Function that checks that only numerical answers are input into the text box for a one
     * activity question.
     */
    public void validateInput() {
        answerOneInput.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue.length() < 11) {
                    if (!newValue.matches("\\d*")) {
                        answerOneInput.setText(formatNumberString(newValue.replaceAll("[^\\d]", "")));
                    } else {
                        answerOneInput.setText(formatNumberString(newValue));
                    }
                } else {
                    answerOneInput.setText(oldValue);
                }
            }
        });
    }

//    emojiOne.setOnAction(new EventHandler() {
//
//        @Override
//        public void handle(ActionEvent event) {
//            System.out.println("Hi there! You clicked me!");
//        }
//    });

    public void showEmojiOne(String username) {

        ScaleTransition transition = new ScaleTransition();
        transition.setByX(1.3);
        transition.setByY(1.3);
        transition.setDuration(Duration.seconds(0.3));
        transition.setNode(emojiOne);
        transition.setAutoReverse(true);
        transition.setCycleCount(4);
        transition.play();
        emojiOne.toFront();
        PauseTransition pauseTransition = new PauseTransition(Duration.seconds(1.2));
        emojiOneLabel.setText(username);
        emojiOneLabel.setVisible(true);
        pauseTransition.play();
        pauseTransition.setOnFinished(e -> emojiOneLabel.setVisible(false));
    }

    public void showEmojiTwo(String username) {
        ScaleTransition transition = new ScaleTransition();
        transition.setByX(1.3);
        transition.setByY(1.3);
        transition.setDuration(Duration.seconds(0.3));
        transition.setNode(emojiTwo);
        transition.setAutoReverse(true);
        transition.setCycleCount(4);
        transition.play();
        emojiTwo.toFront();
        PauseTransition pauseTransition = new PauseTransition(Duration.seconds(1.2));
        emojiTwoLabel.setText(username);
        emojiTwoLabel.setVisible(true);
        pauseTransition.play();
        pauseTransition.setOnFinished(e -> emojiTwoLabel.setVisible(false));
    }

    public void showEmojiThree(String username) {
        ScaleTransition transition = new ScaleTransition();
        transition.setByX(1.3);
        transition.setByY(1.3);
        transition.setDuration(Duration.seconds(0.3));
        transition.setNode(emojiThree);
        transition.setAutoReverse(true);
        transition.setCycleCount(4);
        transition.play();
        emojiThree.toFront();
        PauseTransition pauseTransition = new PauseTransition(Duration.seconds(1.2));
        emojiThreeLabel.setText(username);
        emojiThreeLabel.setVisible(true);
        pauseTransition.play();
        pauseTransition.setOnFinished(e -> emojiThreeLabel.setVisible(false));
    }

}
