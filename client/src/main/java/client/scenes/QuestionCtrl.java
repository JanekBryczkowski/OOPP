package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import com.google.inject.Stage;

import commons.Question;
import commons.WebsocketMessage;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
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

    public boolean answered;
    public int answer;

    public int correctAnswer;
    public int jokerOneActive = 1; //double points
    public int[] secondsPassed = {15};
    Timer myTimer = new Timer();
    TimerTask task;

    private final int ROUNDS = 20;

    private boolean emojiOneCurrentlyBeingChanged = false;
    private boolean emojiTwoCurrentlyBeingChanged = false;
    private boolean emojiThreeCurrentlyBeingChanged = false;

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
    public Button jokerOneSinglePlayer;

    @FXML
    public Button jokerTwoSinglePlayer;

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
    private AnchorPane jokersForSinglePlayer;

    @FXML
    private AnchorPane jokersForMultiPlayer;

    @FXML
    public Button jokerOneMultiPlayer;

    @FXML
    public Button jokerTwoMultiPlayer;

    @FXML
    public Button jokerThreeMultiPlayer;

    @FXML
    private Text nameText;

    @FXML
    private ProgressBar nope;

    @FXML
    private AnchorPane gainedPointsAnchorPane;

    @FXML
    private Label jokerUsed;

    double randomLower;
    double randomUpper;
    int lowerBoundaryNumber;
    int upperBoundaryNumber;

    Timeline bar;

    /**
     * Color array for timer for single player.
     */
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

    /**
     * Color array for timer for multiplayer.
     */
    List<Color> colorsForClockMultiPlayer = Arrays.asList(new Color(0, 0.3, 0.15, 1),
            new Color(0.28, 0.75, 0.33, 1),
            new Color(0.57, 0.94, 0.53, 1),
            new Color(1, 0.95, 0.14, 1),
            new Color(1, 0.74, 0.5, 1),
            new Color(0.97, 0.43, 0.07, 1),
            new Color(0.91, 0.23, 0.08, 1),
            new Color(0.54, 0.06, 0.05, 1),
            new Color(0.39, 0.02, 0.02, 1),
            new Color(0, 0, 0, 1),
            new Color(0, 0, 0, 1));

    /**
     * Constructor for QuestionCtrl and instantiation of the server and the gameCtrl.
     *
     * @param server - server provided for the game.
     * @param gameCtrl - game controller provided for the game that is either single or multiplayer.
     */
    @Inject
    public QuestionCtrl(ServerUtils server, GameCtrl gameCtrl) {
        this.server = server;
        this.gameCtrl = gameCtrl;
    }

    /**
     * Every new round, a new timer and new timer task have to be instantiated.
     * The run method from the TimerTask class is called to start the 15s countdown timer.
     * The mode(single player/ multiplayer) is checked because if the mode is in solo mode, then once the user has answered,
     * the answer can be revealed immediately. However, in multiplayer mode, the user needs to wait the whole 15s for everyone
     * else in the game to have answered.
     */
    public void instantiateTimer() {
        if (gameCtrl.getMode() == 0) {
            singlePlayerSecondsLeft.setText("Time left: 15 seconds");
            multiPlayerSecondsLeft.setText("");
        } else {
            singlePlayerSecondsLeft.setText("");
            multiPlayerSecondsLeft.setText("Time left to answer: 10 seconds");
        }
        secondsPassed[0] = 15;
        myTimer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                if (gameCtrl.getMode() == 0) {
                    secondsPassed[0]--;
                    if (secondsPassed[0] == 1) {
                        singlePlayerSecondsLeft.setText("Time left: " + secondsPassed[0] + " second");
                    } else if (secondsPassed[0] > 1) {
                        singlePlayerSecondsLeft.setText("Time left: " + secondsPassed[0] + " seconds");
                    } else {
                        if (oneActivityAnchorPane.isVisible()) {
                            revealAnswersOneActivities();
                        }
                        if (threeActivitiesAnchorPane.isVisible()) {
                            revealAnswersThreeActivities(null, 4);
                        }
                    }
                } else {
                    secondsPassed[0]--;
                    if (secondsPassed[0] == 6) {
                        if (!answered)
                            multiPlayerSecondsLeft.setText("Time left to answer: " + (secondsPassed[0] - 5) + " second");
                        else
                            multiPlayerSecondsLeft.setText("Time until answer revealed: " + (secondsPassed[0] - 5) + " second");
                    } else if (secondsPassed[0] > 6)
                        if (!answered)
                            multiPlayerSecondsLeft.setText("Time left to answer: " + (secondsPassed[0] - 5) + " seconds");
                        else
                            multiPlayerSecondsLeft.setText("Time until answer revealed: " + (secondsPassed[0] - 5) + " seconds");
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
     * Method responsible for initializing timer (at the bottom of the screen) for the single player game.
     */
    private void playTimerSinglePlayer() {
        nope.setStyle("-fx-accent: black;");
        double[] timer = {1};
        bar = new Timeline(new KeyFrame(Duration.millis(15), ev -> {
            timer[0] = timer[0] - 0.001;
            nope.setProgress(timer[0]);
            nope.setStyle("-fx-accent: " + translateColor(colorsForClockSinglePlayer.get(0)));
            if (timer[0] < 0.94) {
                nope.setStyle("-fx-accent: " + translateColor(colorsForClockSinglePlayer.get(1)));
            }
            if (timer[0] < 0.88) {
                nope.setStyle("-fx-accent: " + translateColor(colorsForClockSinglePlayer.get(2)));
            }
            if (timer[0] < 0.82) {
                nope.setStyle("-fx-accent: " + translateColor(colorsForClockSinglePlayer.get(3)));
            }
            if (timer[0] < 0.76) {
                nope.setStyle("-fx-accent: " + translateColor(colorsForClockSinglePlayer.get(4)));
            }
            if (timer[0] < 0.7) {
                nope.setStyle("-fx-accent: " + translateColor(colorsForClockSinglePlayer.get(5)));
            }
            if (timer[0] < 0.64) {
                nope.setStyle("-fx-accent: " + translateColor(colorsForClockSinglePlayer.get(6)));
            }
            if (timer[0] < 0.58) {
                nope.setStyle("-fx-accent: " + translateColor(colorsForClockSinglePlayer.get(7)));
            }
            if (timer[0] < 0.52) {
                nope.setStyle("-fx-accent: " + translateColor(colorsForClockSinglePlayer.get(8)));
            }
            if (timer[0] < 0.46) {
                nope.setStyle("-fx-accent: " + translateColor(colorsForClockSinglePlayer.get(9)));
            }
            if (timer[0] < 0.4) {
                nope.setStyle("-fx-accent: " + translateColor(colorsForClockSinglePlayer.get(10)));
            }
            if (timer[0] < 0.34) {
                nope.setStyle("-fx-accent: " + translateColor(colorsForClockSinglePlayer.get(11)));
            }
            if (timer[0] < 0.28) {
                nope.setStyle("-fx-accent: " + translateColor(colorsForClockSinglePlayer.get(12)));
            }
            if (timer[0] < 0.22) {
                nope.setStyle("-fx-accent: " + translateColor(colorsForClockSinglePlayer.get(13)));
            }
            if (timer[0] < 0.16) {
                nope.setStyle("-fx-accent: " + translateColor(colorsForClockSinglePlayer.get(14)));
            }
            if (timer[0] <= 0.1) {
                nope.setStyle("-fx-accent: " + translateColor(colorsForClockSinglePlayer.get(15)));
            }
            if (timer[0] == 0.001) {
                bar.stop();
            }
        }));
        bar.setCycleCount(1000);
        bar.play();
    }

    /**
     * Method responsible for initializing timer (at the bottom of the screen) for the multiplayer game.
     */
    private void playTimerMultiPlayer() {
        nope.setStyle("-fx-accent: black;");
        double[] timer = {1};
        bar = new Timeline(new KeyFrame(Duration.millis(10), ev -> {
            timer[0] = timer[0] - 0.001;
            nope.setProgress(timer[0]);
            nope.setStyle("-fx-accent: " + translateColor(colorsForClockMultiPlayer.get(0)));
            if (timer[0] < 0.9) {
                nope.setStyle("-fx-accent: " + translateColor(colorsForClockMultiPlayer.get(1)));
            }
            if (timer[0] < 0.8) {
                nope.setStyle("-fx-accent: " + translateColor(colorsForClockMultiPlayer.get(2)));
            }
            if (timer[0] < 0.7) {
                nope.setStyle("-fx-accent: " + translateColor(colorsForClockMultiPlayer.get(3)));
            }
            if (timer[0] < 0.6) {
                nope.setStyle("-fx-accent: " + translateColor(colorsForClockMultiPlayer.get(4)));
            }
            if (timer[0] < 0.5) {
                nope.setStyle("-fx-accent: " + translateColor(colorsForClockMultiPlayer.get(5)));
            }
            if (timer[0] < 0.4) {
                nope.setStyle("-fx-accent: " + translateColor(colorsForClockMultiPlayer.get(6)));
            }
            if (timer[0] < 0.3) {
                nope.setStyle("-fx-accent: " + translateColor(colorsForClockMultiPlayer.get(7)));
            }
            if (timer[0] < 0.2) {
                nope.setStyle("-fx-accent: " + translateColor(colorsForClockMultiPlayer.get(8)));
            }
            if (timer[0] < 0.1) {
                nope.setStyle("-fx-accent: " + translateColor(colorsForClockMultiPlayer.get(9)));
            }
            if (timer[0] == 0.001) {
                bar.stop();
            }
        }));
        bar.setCycleCount(1000);
        bar.play();
    }

    /**
     * Method that's responsible for converting java color class into the string representation of the color
     * in the hexadecimal system.
     *
     * @param color - provided color with java class color.
     * @return - hexadecimal string number representation.
     */
    public String translateColor(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }

    /**
     * This function is a setup for the GameScreen when there is a three activity question.
     * The function is counting the rounds, and if it's in multiplayer mode it subtracts one, because for one round it's
     * showing the half-time Leaderboard.
     * A question is given as input and this question is displayed on the screen.
     *
     * @param question - the question that will be set up in the Scene.
     */
    public void startThreeActivityQuestion(Question question) {
        nameText.setText("Name: " + gameCtrl.username);
        if (gameCtrl.getMode() == 0) {
            playTimerSinglePlayer();
            jokersForSinglePlayer.setVisible(true);
            jokersForMultiPlayer.setVisible(false);
            singlePlayerSecondsLeft.setVisible(true);
            multiPlayerSecondsLeft.setVisible(false);
        } else {
            playTimerMultiPlayer();
            jokersForSinglePlayer.setVisible(false);
            jokersForMultiPlayer.setVisible(true);
            singlePlayerSecondsLeft.setVisible(false);
            multiPlayerSecondsLeft.setVisible(true);
        }
        Path imageFile = Paths.get("client/src/main/resources/client.activityBank/" + question.activityList.get(0).image_path);
        System.out.println(imageFile);
        try {
            mainImage.setImage(new Image(imageFile.toUri().toURL().toExternalForm()));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        if (question.activityList.get(0).title.length() > 56) {
            answerOne.setText(question.activityList.get(0).title);
            answerOne.setStyle("-fx-font-size: 15;");
        } else {
            answerOne.setText(question.activityList.get(0).title);
            answerOne.setStyle("-fx-font-size: 25;");
        }

        if (question.activityList.get(1).title.length() > 56) {
            answerTwo.setText(question.activityList.get(1).title);
            answerTwo.setStyle("-fx-font-size: 15;");
        } else {
            answerTwo.setText(question.activityList.get(1).title);
            answerTwo.setStyle("-fx-font-size: 25;");
        }

        if (question.activityList.get(2).title.length() > 56) {
            answerThree.setText(question.activityList.get(2).title);
            answerThree.setStyle("-fx-font-size: 15;");
        } else {
            answerThree.setText(question.activityList.get(2).title);
            answerThree.setStyle("-fx-font-size: 25;");
        }

        questionText.setText("Which of these activities takes more energy?");
        questionText.setStyle("-fx-font-size: 47;");
        question.setCorrectAnswer();
        this.correctAnswer = question.correctAnswer;

        enableButtons();

        if (gameCtrl.getMode() == 0)
            hideSoloPlayerElements();

        instantiateTimer();
        myTimer.scheduleAtFixedRate(task, 1000, 1000);
        jokerTwoSinglePlayer.setText("Eliminate one wrong answer");
        jokerTwoMultiPlayer.setText("Eliminate one wrong answer");

        answersGiven.setText(gameCtrl.round + " / 20 rounds");
    }

    /**
     * This function is a setup for the GameScreen when there is a one activity question.
     * The function is counting the rounds, and if it's in multiplayer mode it subtracts one, because for one round it's
     * showing the half-time Leaderboard.
     *
     * @param question - a question is given as input and this question is displayed on the screen.
     */
    public void startTwoActivityQuestion(Question question) {
        nameText.setText("Name: " + gameCtrl.username);
        if (gameCtrl.getMode() == 0) {
            playTimerSinglePlayer();
            jokersForSinglePlayer.setVisible(true);
            jokersForMultiPlayer.setVisible(false);
            singlePlayerSecondsLeft.setVisible(true);
            multiPlayerSecondsLeft.setVisible(false);
        } else {
            playTimerMultiPlayer();
            jokersForSinglePlayer.setVisible(false);
            jokersForMultiPlayer.setVisible(true);
            singlePlayerSecondsLeft.setVisible(false);
            multiPlayerSecondsLeft.setVisible(true);
        }
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
        answerOne.setStyle("-fx-font-size: 25;");
        answerTwo.setStyle("-fx-font-size: 25;");
        answerThree.setStyle("-fx-font-size: 25;");

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

        jokerTwoSinglePlayer.setText("Eliminate one wrong answer");
        jokerTwoMultiPlayer.setText("Eliminate one wrong answer");

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

        if (gameCtrl.getMode() == 0)
            hideSoloPlayerElements();
        instantiateTimer();
        myTimer.scheduleAtFixedRate(task, 1000, 1000);
        enableButtons();

        answersGiven.setText(gameCtrl.round + " / 20 rounds");
    }

    /**
     * In this function the multiplayer question gets set up. For now, the only thing done is setting the
     * question title to the title of the first activity. This needs to be changed so that it checks how long
     * the question is. This function will also have to instantiate a timer.
     *
     * @param question - given as input and this question is displayed on the screen.
     */
    public void setUpMultiPlayerQuestion(Question question) {
        System.out.println("MP question size" + question.activityList.size());
        removeBorders();
        jokersForSinglePlayer.setVisible(false);
        jokersForMultiPlayer.setVisible(true);
        if (!gameCtrl.firstJokerMultiPlayerUsed) jokerOneMultiPlayer.setDisable(false);
        if (!gameCtrl.secondJokerMultiPlayerUsed) jokerTwoMultiPlayer.setDisable(false);
        if (!gameCtrl.thirdJokerMultiPlayerUsed) jokerThreeMultiPlayer.setDisable(false);
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

    /**
     * Method sending the websocket for the time when user uses one out of three emojis available for the
     * multiplayer game.
     */
    public void setupEmoji() {
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
     * The function is counting the rounds, and if it's in multiplayer mode it subtracts one, because for one round it's
     * showing the half-time Leaderboard.
     *
     * @param question - given as input and this question is displayed on the screen.
     */
    public void startOneActivityQuestion(Question question) {
        nameText.setText("Name: " + gameCtrl.username);
        if (gameCtrl.getMode() == 0) {
            playTimerSinglePlayer();
            jokersForSinglePlayer.setVisible(true);
            jokersForMultiPlayer.setVisible(false);
            singlePlayerSecondsLeft.setVisible(true);
            multiPlayerSecondsLeft.setVisible(false);
        } else {
            playTimerMultiPlayer();
            jokersForSinglePlayer.setVisible(false);
            jokersForMultiPlayer.setVisible(true);
            singlePlayerSecondsLeft.setVisible(false);
            multiPlayerSecondsLeft.setVisible(true);
        }
        Path imageFile = Paths.get("client/src/main/resources/client.activityBank/" + question.activityList.get(0).image_path);
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

        if (gameCtrl.getMode() == 0)
            hideSoloPlayerElements();

        instantiateTimer();
        startTimer();

        jokerTwoSinglePlayer.setText("Narrow down the boundaries");
        jokerTwoMultiPlayer.setText("Narrow down the boundaries");
        setUpTheBoundaries();

        answersGiven.setText(gameCtrl.round + " / 20 rounds");
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
     * @param number - that will be formatted.
     * @return String - formatted number in String form.
     */
    private String formatNumber(int number) {
        NumberFormat myFormat = NumberFormat.getInstance();
        myFormat.setGroupingUsed(true);
        return myFormat.format(number);
    }

    /**
     * Correct format of a number as a String.
     *
     * @param number - that will be formatted.
     * @return String - formatted number in String form.
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
     * Function for when the player answers one.
     */
    public void answerOneGiven() {
        if (gameCtrl.getMode() == 0)
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
        if (gameCtrl.getMode() == 0)
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
        if (gameCtrl.getMode() == 0)
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
        jokerOneSinglePlayer.setDisable(false);
        jokerTwoSinglePlayer.setDisable(false);
        jokerOneSinglePlayer.setBorder(null);
        jokerTwoSinglePlayer.setBorder(null);
        jokerOneMultiPlayer.setBorder(null);
        jokerTwoMultiPlayer.setBorder(null);
        jokerThreeMultiPlayer.setBorder(null);
        jokerOneActive = 1;
    }

    /**
     * This function reveals the correct and wrong answer by colouring the borders of the answers.
     * It adds points and calls the newQuestion function.
     */
    public void revealAnswersThreeActivities(Pane clicked, int click) {
        myTimer.cancel();

        jokerOneSinglePlayer.setDisable(true);
        jokerTwoSinglePlayer.setDisable(true);
        switch (correctAnswer) {
            case (1):
                answerOnePane.setStyle("-fx-border-color: green; -fx-border-width: 5; -fx-border-radius: 20;");
                break;
            case (2):
                answerTwoPane.setStyle("-fx-border-color: green; -fx-border-width: 5; -fx-border-radius: 20;");
                break;
            case (3):
                answerThreePane.setStyle("-fx-border-color: green; -fx-border-width: 5; -fx-border-radius: 20;");
                break;
            default:
                break;
        }
        if (correctAnswer != click && !(click > 3)) {
            clicked.setStyle("-fx-border-color: red; -fx-border-width: 5; -fx-border-radius: 20;");
        } else if (correctAnswer == click && !(click > 3)) {
            int pointsGainedInRound = jokerOneActive * 10 * secondsPassed[0];
            gainedPoints.setText("+ " + pointsGainedInRound + " points");
            gainedPoints.setVisible(true);
            gainedPointsAnchorPane.setVisible(true);
            Timer myTimers = new Timer();
            myTimers.schedule(new TimerTask() {

                @Override
                public void run() {
                    gainedPoints.setVisible(false);
                    gainedPointsAnchorPane.setVisible(false);
                }
            }, 2000);

            gameCtrl.points += pointsGainedInRound;
        }
        points.setText(gameCtrl.points + " points");
        if (gameCtrl.getMode() == 0)
            newQuestion();
        else {
            answer = 0;
            answered = false;
        }
    }

    /**
     * This function reveals the correct answer by filling in the answer text box with the
     * correct number.
     * It adds points and calls the newQuestion function.
     */
    public void revealAnswersOneActivities() {
        myTimer.cancel();

        jokerOneSinglePlayer.setDisable(true);
        jokerTwoSinglePlayer.setDisable(true);
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
            gainedPointsAnchorPane.setVisible(true);
            Timer myTimers = new Timer();
            myTimers.schedule(new TimerTask() {

                @Override
                public void run() {
                    gainedPoints.setVisible(false);
                    gainedPointsAnchorPane.setVisible(false);
                }
            }, 2000);

            answerOneInput.setBorder(new Border(new BorderStroke(Color.GREEN, BorderStrokeStyle.SOLID, new CornerRadii(40), new BorderWidths(2))));
        } else if (answerGiven > lowerBoundaryNumber && answerGiven < upperBoundaryNumber) {
            int pointsGainedInRound = jokerOneActive * calculatePointsForOpenAnswer(correctAnswer, answerGiven);
            gameCtrl.points += pointsGainedInRound;
            gainedPoints.setText("+ " + pointsGainedInRound + " points");

            gainedPoints.setVisible(true);
            gainedPointsAnchorPane.setVisible(true);
            Timer myTimers = new Timer();
            myTimers.schedule(new TimerTask() {

                @Override
                public void run() {
                    gainedPoints.setVisible(false);
                    gainedPointsAnchorPane.setVisible(false);
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
        if (gameCtrl.getMode() == 0)
            newQuestion();
    }

    /**
     * Formats numbers so that large numbers do contain ',' every 3 integers from the right.
     *
     * @param number - number in String form.
     * @return int - the actual number.
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
        jokersForSinglePlayer.setVisible(true);
        jokersForMultiPlayer.setVisible(false);
        singlePlayerSecondsLeft.setVisible(true);
        multiPlayerSecondsLeft.setVisible(false);
        myTimers.schedule(new TimerTask() {

            @Override
            public void run() {
                Platform.runLater(() -> {
                    removeBorders();
                    ++gameCtrl.round;
                    if (gameCtrl.round > ROUNDS) {
                        gameCtrl.showLeaderBoard(new ArrayList<>());
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
        if (gameCtrl.getMode() == 1) {
            gameCtrl.subscription.unsubscribe();
            gainedPoints.setText("");
            gameCtrl.points = 0;
            gameCtrl.round = 1;
            gameCtrl.username = "";
            gameCtrl.firstJokerSinglePlayerUsed = false;
            gameCtrl.secondJokerSinglePlayerUsed = false;
            gameCtrl.firstJokerMultiPlayerUsed = false;
            gameCtrl.secondJokerMultiPlayerUsed = false;
            gameCtrl.thirdJokerMultiPlayerUsed = false;
            jokerOneSinglePlayer.setStyle("-fx-border-width: 0");
            jokerTwoSinglePlayer.setStyle("-fx-border-width: 0");
            jokerOneSinglePlayer.setDisable(false);
            jokerTwoSinglePlayer.setDisable(false);
            answerOnePane.setStyle("-fx-border-width: 0;");
            answerTwoPane.setStyle("-fx-border-width: 0;");
            answerThreePane.setStyle("-fx-border-width: 0;");
            myTimer.cancel();
            points.setText("0 points");
            gameCtrl.showSplashScreen();
        } else if (gameCtrl.getMode() == 0) {
            myTimer.cancel();
            gameCtrl.points = 0;
            gameCtrl.round = 1;
            gameCtrl.firstJokerSinglePlayerUsed = false;
            gameCtrl.secondJokerSinglePlayerUsed = false;
            gameCtrl.firstJokerMultiPlayerUsed = false;
            gameCtrl.secondJokerMultiPlayerUsed = false;
            gameCtrl.thirdJokerMultiPlayerUsed = false;
            gameCtrl.showSplashScreen();
        }
    }

    /**
     * Function for when joker one is pressed. Jokers are disabled for the remainder of the round.
     */
    public void jokerOneSinglePlayer() {
        if (!gameCtrl.firstJokerSinglePlayerUsed) {
            this.jokerOneActive = 2;
            jokerOneSinglePlayer.setStyle("-fx-border-color: darkgreen; -fx-border-width: 5; -fx-border-radius: 30;");
            jokerOneSinglePlayer.setDisable(true);
            jokerTwoSinglePlayer.setDisable(true);
            gameCtrl.firstJokerSinglePlayerUsed = true;
        } else {
            jokerOneSinglePlayer.setDisable(true);
        }
    }

    /**
     * Function for joker one (double points) for multiplayer.
     */
    public void jokerOneMultiPlayer() {
        if (!gameCtrl.firstJokerMultiPlayerUsed) {
            WebsocketMessage websocketMessage = new WebsocketMessage("JOKERUSED");
            websocketMessage.emojiUsername = gameCtrl.username;
            websocketMessage.jokerUsed = 1;
            server.send("/topic/question" + gameCtrl.joinedLobby, websocketMessage);
            this.jokerOneActive = 2;
            jokerOneMultiPlayer.setStyle("-fx-border-color: darkgreen; -fx-border-width: 5; -fx-border-radius: 30;");
            jokerOneMultiPlayer.setDisable(true);
            jokerTwoMultiPlayer.setDisable(true);
            jokerThreeMultiPlayer.setDisable(true);
            gameCtrl.firstJokerMultiPlayerUsed = true;
        } else {
            jokerOneMultiPlayer.setDisable(true);
        }
    }

    /**
     * Function for when a player uses joker three in multiplayer
     */
    public void sendJokerThree() {
        if (!gameCtrl.thirdJokerMultiPlayerUsed) {
            WebsocketMessage websocketMessage = new WebsocketMessage("JOKERUSED");
            websocketMessage.emojiUsername = gameCtrl.username;
            websocketMessage.jokerUsed = 3;
            server.send("/topic/question" + gameCtrl.joinedLobby, websocketMessage);
            secondsPassed[0] += 5;
            WebsocketMessage websocketMessagee = new WebsocketMessage("JOKERTHREE");
            server.send("/topic/question" + gameCtrl.joinedLobby, websocketMessagee);
            jokerOneMultiPlayer.setDisable(true);
            jokerTwoMultiPlayer.setDisable(true);
            jokerThreeMultiPlayer.setDisable(true);
            gameCtrl.thirdJokerMultiPlayerUsed = true;
        } else {
        jokerOneMultiPlayer.setDisable(true);
    }
    }

    /**
     * Function for joker two (Eliminating wrong answer).
     */
    public void jokerTwoSinglePlayer() {
        if (!gameCtrl.secondJokerSinglePlayerUsed) {
            if (oneActivityAnchorPane.isVisible()) {
                int difference = (int) (Math.random() * (correctAnswer - lowerBoundaryNumber));
                int newLowerBoundaryNumber = lowerBoundaryNumber + difference;
                int newUpperBoundaryNumber = upperBoundaryNumber - difference;
                lowerBoundary.setText(formatNumber(newLowerBoundaryNumber));
                upperBoundary.setText(formatNumber(newUpperBoundaryNumber));
                jokerTwoSinglePlayer.setStyle("-fx-border-color: darkgreen; -fx-border-width: 5; -fx-border-radius: 30;");
                jokerOneSinglePlayer.setDisable(true);
                jokerTwoSinglePlayer.setDisable(true);
                gameCtrl.secondJokerSinglePlayerUsed = true;
            } else if (threeActivitiesAnchorPane.isVisible()) {
                int random = correctAnswer;
                while (random == correctAnswer) {
                    random = (int) (Math.random() * 3 + 1);
                }

                switch (random) {
                    case (1):
                        answerOnePane.setDisable(true);
                        answerOnePane.setStyle("-fx-border-color: grey; -fx-border-width: 5; -fx-border-radius: 20;");
                        break;
                    case (2):
                        answerTwoPane.setDisable(true);
                        answerTwoPane.setStyle("-fx-border-color: grey; -fx-border-width: 5; -fx-border-radius: 20;");
                        break;
                    case (3):
                        answerThreePane.setDisable(true);
                        answerThreePane.setStyle("-fx-border-color: grey; -fx-border-width: 5; -fx-border-radius: 20;");
                        break;
                    default:
                        break;
                }
                jokerTwoSinglePlayer.setStyle("-fx-border-color: darkgreen; -fx-border-width: 5; -fx-border-radius: 30;");
                jokerOneSinglePlayer.setDisable(true);
                jokerTwoSinglePlayer.setDisable(true);
                gameCtrl.secondJokerSinglePlayerUsed = true;
            }
        } else {
            jokerTwoSinglePlayer.setDisable(true);
        }
    }

    /**
     * Function for joker two (Eliminating wrong answer) for multiplayer.
     */
    public void jokerTwoMultiPlayer() {
        if (!gameCtrl.secondJokerMultiPlayerUsed) {
            if (oneActivityAnchorPane.isVisible()) {
                WebsocketMessage websocketMessage = new WebsocketMessage("JOKERUSED");
                websocketMessage.emojiUsername = gameCtrl.username;
                websocketMessage.jokerUsed = 2;
                server.send("/topic/question" + gameCtrl.joinedLobby, websocketMessage);
                int differenceLower = (int) (Math.random() * (correctAnswer - lowerBoundaryNumber));
                int differenceUpper = (int) (Math.random() * (upperBoundaryNumber - correctAnswer));
                int newLowerBoundaryNumber = lowerBoundaryNumber + differenceLower;
                int newUpperBoundaryNumber = upperBoundaryNumber - differenceUpper;
                lowerBoundary.setText(formatNumber(newLowerBoundaryNumber));
                upperBoundary.setText(formatNumber(newUpperBoundaryNumber));
                jokerTwoMultiPlayer.setStyle("-fx-border-color: darkgreen; -fx-border-width: 5; -fx-border-radius: 30;");
                jokerOneMultiPlayer.setDisable(true);
                jokerTwoMultiPlayer.setDisable(true);
                jokerThreeMultiPlayer.setDisable(true);
                gameCtrl.secondJokerMultiPlayerUsed = true;
            } else if (threeActivitiesAnchorPane.isVisible()) {
                int random = correctAnswer;
                while (random == correctAnswer) {
                    random = (int) (Math.random() * 3 + 1);
                }

                switch (random) {
                    case (1):
                        answerOnePane.setDisable(true);
                        answerOnePane.setStyle("-fx-border-color: grey; -fx-border-width: 5; -fx-border-radius: 20;");
                        break;
                    case (2):
                        answerTwoPane.setDisable(true);
                        answerTwoPane.setStyle("-fx-border-color: grey; -fx-border-width: 5; -fx-border-radius: 20;");
                        break;
                    case (3):
                        answerThreePane.setDisable(true);
                        answerThreePane.setStyle("-fx-border-color: grey; -fx-border-width: 5; -fx-border-radius: 20;");
                        break;
                    default:
                        break;
                }
                jokerTwoMultiPlayer.setStyle("-fx-border-color: darkgreen; -fx-border-width: 5; -fx-border-radius: 30;");
                jokerOneMultiPlayer.setDisable(true);
                jokerTwoMultiPlayer.setDisable(true);
                jokerThreeMultiPlayer.setDisable(true);
                gameCtrl.secondJokerMultiPlayerUsed = true;
            }
        } else {
            jokerTwoMultiPlayer.setDisable(true);
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
        jokerOneSinglePlayer.setStyle("-fx-border-width: 0");
        jokerTwoSinglePlayer.setStyle("-fx-border-width: 0");
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

    /**
     * Method responsible for showing emoji 1 for all of the multiplayer game users.
     *
     * @param username - username of the user who used the first emoji.
     */
    public void showEmojiOne(String username) {
        if (!emojiOneCurrentlyBeingChanged) {
            emojiOneCurrentlyBeingChanged = true;
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
            pauseTransition.setOnFinished(e -> {
                emojiOneLabel.setVisible(false);
                emojiOneCurrentlyBeingChanged = false;
            });
        }
    }

    /**
     * Method responsible for showing emoji 2 for all of the multiplayer game users.
     *
     * @param username - username of the user who used the second emoji.
     */
    public void showEmojiTwo(String username) {
        if (!emojiTwoCurrentlyBeingChanged) {
            emojiTwoCurrentlyBeingChanged = true;
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
            pauseTransition.setOnFinished(e -> {
                emojiTwoLabel.setVisible(false);
                emojiTwoCurrentlyBeingChanged = false;
            });
        }
    }

    /**
     * Method responsible for showing emoji 3 for all of the multiplayer game users.
     *
     * @param username - username of the user who used the third emoji.
     */
    public void showEmojiThree(String username) {
        if (!emojiThreeCurrentlyBeingChanged) {
            emojiThreeCurrentlyBeingChanged = true;
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
            pauseTransition.setOnFinished(e -> {
                emojiThreeLabel.setVisible(false);
                emojiThreeCurrentlyBeingChanged = false;
            });
        }
    }

    /**
     * This function gets called whenever another player uses joker three
     */
    public void jokerThree() {
        secondsPassed[0] -= 5;
    }

    /**
     * Method responsible for doing the same thing as the OK button in 1 activity question,
     * when ENTER is clocked.
     *
     * @param event - keyboard event when ENTER is clicked.
     */
    @FXML
    void clickEnter(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            revealAnswersOneActivities();
        }
    }

    /**
     * Method responsible for showing to all of the multiplayer users who and what joker used at
     * a certain moment.
     *
     * @param username - username of the user who used certain joker.
     * @param joker - integer indicating the joker number used by a certain user.
     */
    public void showUsedJoker(String username, int joker) {
        PauseTransition pauseTransition = new PauseTransition(Duration.seconds(2.2));
        jokerUsed.setText(username + " has used joker " + joker);
        jokerUsed.setVisible(true);
        pauseTransition.play();
        pauseTransition.setOnFinished(e -> {
            jokerUsed.setVisible(false);
        });
    }
}
