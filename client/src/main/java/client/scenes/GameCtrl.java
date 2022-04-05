package client.scenes;

import commons.Question;

import commons.User;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Pair;
import org.springframework.messaging.simp.stomp.StompSession;

import java.awt.*;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GameCtrl {

    public Stage primaryStage;

    private SplashScreenCtrl splashScreenCtrl;
    private Scene splashScreenScene;

    private QuestionCtrl questionCtrl;
    private Scene questionScreen;

    private LeaderBoardCtrl leaderBoardCtrl;
    private Scene leaderBoardScreen;

    private WaitingRoomCtrl waitingRoomCtrl;
    private Scene waitingRoom;

    private AdminCtrl adminCtrl;
    private Scene admin;

    public int points = 0;
    public int round = 1;
    public String username = "";
    public int joinedLobby;

    public boolean firstJokerSinglePlayerUsed = false;
    public boolean secondJokerSinglePlayerUsed = false;

    public boolean firstJokerMultiPlayerUsed = false;
    public boolean secondJokerMultiPlayerUsed = false;
    public boolean thirdJokerMultiPlayerUsed = false;

    public StompSession.Subscription subscription = null;
    public boolean multiplayer;

    private DialogPane dialogPane;

    /**
     * This is for the multiplayer game, since there is a half-time Leaderboard set in the 11th round,
     * we need 21 rounds all together.
     */
    public final int MULTIROUNDS = 21;

    private final int ROUNDS = 20;

    /**
     * Initializes all the controllers and all the scenes that are used throughout the game.
     * The game starts in the Splash Screen, so we call showSplashScreen to make it visible
     * We show the Primary Stage.
     *
     * @param primaryStage - main stage used for the application.
     * @param splash - splash screen controller provided for the constructor.
     * @param questionCtrl - question screen controller provided for the constructor.
     * @param leaderBoardCtrl - leaderboard screen controller provided for the constructor.
     * @param waitingRoomCtrl - waiting room screen controller provided for the constructor.
     */
    public void initialize(Stage primaryStage, Pair<SplashScreenCtrl, Parent> splash, Pair<QuestionCtrl, Parent> questionCtrl, Pair<LeaderBoardCtrl, Parent> leaderBoardCtrl, Pair<WaitingRoomCtrl, Parent> waitingRoomCtrl, Pair<AdminCtrl, Parent> adminCtrl) {
        this.primaryStage = primaryStage;

        this.splashScreenCtrl = splash.getKey();
        this.splashScreenScene = new Scene(splash.getValue());

        this.questionCtrl = questionCtrl.getKey();
        this.questionScreen = new Scene(questionCtrl.getValue());

        this.leaderBoardCtrl = leaderBoardCtrl.getKey();
        this.leaderBoardScreen = new Scene(leaderBoardCtrl.getValue());

        this.waitingRoomCtrl = waitingRoomCtrl.getKey();
        this.waitingRoom = new Scene(waitingRoomCtrl.getValue());

        this.adminCtrl = adminCtrl.getKey();
        this.admin = new Scene(adminCtrl.getValue());

        primaryStage.getIcons().add(new Image("client.images/environmentLogo.png"));

        showSplashScreen();
        primaryStage.show();
        primaryStage.setResizable(false);

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to exit the application?");
                Toolkit.getDefaultToolkit().beep();
                primaryStage.getScene().getRoot().setEffect(new GaussianBlur(200));
                dialogPane = alert.getDialogPane();
                dialogPane.getScene().setFill(Color.TRANSPARENT);
                alert.initStyle(StageStyle.TRANSPARENT);
                alert.initStyle(StageStyle.UNDECORATED);
                ImageView imageView = new ImageView("client.images/earthLogo.png");
                imageView.setFitWidth(72);
                imageView.setFitHeight(74);
                alert.setGraphic(imageView);
                dialogPane.getStylesheets().add("client.styles/AlertBox.css");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() != ButtonType.OK) {
                    event.consume();
                    primaryStage.getScene().getRoot().setEffect(null);
                }
            }
        });
    }

    /**
     * This function is for showing the SplashScreen.
     */
    public void showSplashScreen() {
        primaryStage.setTitle("Splash Screen");
        splashScreenCtrl.setSplashScreen();
        splashScreenScene.getStylesheets().add("client.styles/SplashScreenStyle.css");
        primaryStage.setScene(splashScreenScene);
    }

    /**
     * Get the mode of the game (single player / multiplayer).
     *
     * @return - returning integer indicating the mode of the game (single player / multiplayer).
     */
    public int getMode() {
        return splashScreenCtrl.mode;
    }

    /**
     * Set the mode of the game. If it's single player, it's 0, otherwise 1.
     *
     * @param mode - setting the mode of the game to either single (0) or multiplayer (1).
     */
    public void setMode(int mode) {
        splashScreenCtrl.mode = mode;
    }

    /**
     * Setter for username.
     *
     * @param username - username provided for specific user in the game controller.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * This function is for showing the Game Screen.
     * The number of rounds are checked. If this variable is larger than the set number of rounds,
     * then the points for the user are reset as the current game is over and the function for showing the
     * Leader Board is called.
     * If the number of rounds has not yet reached the maximum, then we generate another random question
     * and depending on the number of activities in the question, we call a function tailored to word the new question.
     */
    public void SoloGameRound() throws MalformedURLException {
        setMode(0);
        if (round > ROUNDS) {
            questionCtrl.resetPoints();
            showLeaderBoard(new ArrayList<>());
        } else {
            Question question = splashScreenCtrl.getRandomQuestion();
            switch (question.activityList.size()) {
                case (1): {
                    oneActivityQuestion(question);
                    break;
                }
                case (2): {
                    twoActivityQuestion(question);
                    break;
                }
                case (3): {
                    threeActivityQuestion(question);
                    break;
                }
            }
        }
    }

    /**
     * Setup for a question with one activity.
     * The setOneActivity function is called.
     *
     * @param question - input question with the list of one activity.
     */
    public void oneActivityQuestion(Question question) {
        questionCtrl.startOneActivityQuestion(question);
        primaryStage.setTitle("Game screen - 1 activity question");
        if (getMode() == 1) primaryStage.setTitle("Game screen - 1 activities question - Multiplayer");
        else primaryStage.setTitle("Game screen - 1 activities question - Single player");
        primaryStage.setScene(questionScreen);
        questionCtrl.setOneActivity();
        questionScreen.getStylesheets().add("client.styles/QuestionScreenStyles.css");
        checkJokers(questionCtrl);
    }

    /**
     * Method for refreshing the waiting room scroll pane in the waiting room controller class.
     */
    public void refreshPlayers() {
        waitingRoomCtrl.refreshTable();
    }

    /**
     * Setup for a question with two activities.
     * The setTwoActivities function is called.
     *
     * @param question - input question with the list of two activities.
     */
    public void twoActivityQuestion(Question question) {
        questionCtrl.startTwoActivityQuestion(question);
        if (getMode() == 1) primaryStage.setTitle("Game screen - 2 activities question - Multiplayer");
        else primaryStage.setTitle("Game screen - 2 activities question - Single player");
        primaryStage.setScene(questionScreen);
        questionCtrl.setTwoActivities();
        questionScreen.getStylesheets().add("client.styles/QuestionScreenStyles.css");
        checkJokers(questionCtrl);
    }

    /**
     * Setup for a question with three activities
     * The setThreeActivities function is called.
     *
     * @param question - input question with the list of three activities.
     */
    public void threeActivityQuestion(Question question) throws MalformedURLException {
        questionCtrl.startThreeActivityQuestion(question);
        if (getMode() == 1) primaryStage.setTitle("Game screen - 3 activities question - Multiplayer");
        else primaryStage.setTitle("Game screen - 3 activities question - Single player");
        primaryStage.setScene(questionScreen);
        questionCtrl.setThreeActivities();
        questionScreen.getStylesheets().add("client.styles/QuestionScreenStyles.css");
        checkJokers(questionCtrl);
    }

    /**
     * Player joining the current lobby of the multiplayer game.
     */
    public void joinCurrentLobby() {
        primaryStage.setTitle("Waiting Room");
        primaryStage.setScene(waitingRoom);
        waitingRoom.getStylesheets().add("client.styles/WaitingRoomStyle.css");
        waitingRoomCtrl.setWaitingRoomTable();
    }

    /**
     * The scene is set to show the Waiting Room.
     */
    public void showWaitingRoomScreen() {
        primaryStage.setTitle("Waiting Room");
        waitingRoom.getStylesheets().add("client.styles/WaitingRoomStyle.css");
        primaryStage.setScene(waitingRoom);
    }

    /**
     * This function gets called whenever a player receives a question from the server
     * in multiplayer mode. The question is printed to the terminal for testing. The scene is set
     * to the question screen and on the question controller, the setup function is called with
     * the question, which will set up the question properly.
     *
     * @param question - question provided for the multiplayer round, containing either 1, 2, or 3 activities.
     */
    public void startMultiPlayerQuestion(Question question) {
        questionCtrl.setupEmoji();
        setMode(1);

            questionScreen.getStylesheets().add("client.styles/QuestionScreenStyles.css");
            primaryStage.setScene(questionScreen);
            primaryStage.setTitle("Question");

            checkJokers(questionCtrl);
            setMode(1);
            questionCtrl.setUpMultiPlayerQuestion(question);
            round++;
    }

    /**
     * Function for storing the user and their points in the database is called.
     * The Leader Board Scene is set, and we create a list with all the users in the database
     * to output to the scroll pane when the scene is set as the Primary Stage.
     * @param userList
     */
    public void showLeaderBoard(List<User> userList) {
        questionCtrl.resetPoints();
        questionCtrl.resetJokers();
        leaderBoardCtrl.endLeaderBoard();

        leaderBoardCtrl.storePoints(userList);
        leaderBoardCtrl.setLeaderBoard(userList);

        leaderBoardCtrl.setList(userList);
        questionCtrl.jokerOneMultiPlayer.setBorder(null);
        questionCtrl.jokerTwoSinglePlayer.setBorder(null);
        questionCtrl.jokerThreeMultiPlayer.setBorder(null);
        leaderBoardCtrl.backToWaitingRoomButton();
        leaderBoardScreen.getStylesheets().add("client.styles/LeaderBoardScreenStyles.css");
        primaryStage.setScene(leaderBoardScreen);
        if (getMode() == 1) {
            primaryStage.setTitle("LeaderBoard Screen - Multiplayer");
            for (int i = 0; i < userList.size(); i++) {
                if (userList.get(i).username.equals(username)) {
                    userList.get(i).setScore(points);
                    break;
                }
            }
        } else primaryStage.setTitle("LeaderBoard Screen - Single player");
    }

    /**
     * The half-time Leader Board Scene is set, this is called during the Multiplayer game.
     */
    public void showHalfTimeLeaderBoard(List<User> userList) {
        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).username.equals(username)) {
                userList.get(i).setScore(points);
                break;
            }
        }

        leaderBoardCtrl.storePoints(userList);
        leaderBoardCtrl.setLeaderBoard(userList);
        leaderBoardCtrl.setList(userList);
        leaderBoardCtrl.halfTimeLeaderBoard();
        leaderBoardScreen.getStylesheets().add("client.styles/LeaderBoardScreenStyles.css");
        primaryStage.setScene(leaderBoardScreen);

    }

    /**
     * This function gets called whenever another player has clicked on joker three.
     */
    public void jokerThree() {
        questionCtrl.jokerThree();
    }

    /**
     * This function will check if the jokers have been used by the user playing.
     * If a user has been used, then that joker will be disabled for the remaining of the game.
     *
     * @param questionCtrl - provided question controller class to check jokers for.
     */
    public void checkJokers(QuestionCtrl questionCtrl) {
        if (firstJokerSinglePlayerUsed) questionCtrl.jokerOneSinglePlayer.setDisable(true);
        if (secondJokerSinglePlayerUsed) questionCtrl.jokerTwoSinglePlayer.setDisable(true);
    }

    /**
     * Method responsible for sending the accurate message to show the correct emoji for all
     * multiplayer users.
     *
     * @param emojiNumber - number indicating which emoji was clicked.
     * @param username - username of the user who clicked certain emoji.
     */
    public void showEmoji(int emojiNumber, String username) {
        switch (emojiNumber) {
            case (1):
                questionCtrl.showEmojiOne(username);
                break;
            case (2):
                questionCtrl.showEmojiTwo(username);
                break;
            case (3):
                questionCtrl.showEmojiThree(username);
                break;
            default:
                break;
        }
    }

    /**
     * Method adding the admin screen to the main primary stage, that's responsible for allowing
     * users to add and modify existing activities.
     */
    public void showAdminScreen() {
        primaryStage.setTitle("Admin Screen");
        admin.getStylesheets().add("client.styles/AdminScreenStyle.css");
        primaryStage.setScene(admin);
        adminCtrl.setTable();
    }

    /**
     * Method indicating which joker has been used by what user in the multiplayer game.
     *
     * @param username - username of the user who used certain joker.
     * @param joker - number indicating the joker user used.
     */
    public void jokerUsed(String username, int joker) {
        questionCtrl.showUsedJoker(username, joker);
    }
}