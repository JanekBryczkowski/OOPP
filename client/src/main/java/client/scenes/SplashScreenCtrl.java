package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import com.google.inject.Stage;
import commons.Question;
import commons.User;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.springframework.messaging.simp.stomp.StompSession;

import java.net.MalformedURLException;

public class SplashScreenCtrl {

    private final ServerUtils server;
    private final GameCtrl gameCtrl;
    private Stage primaryStage;

    /**
     * If mode is set to 0, then single player is active.
     * If mode is set to 1, then multiplayer is active.
     */
    public static int mode = 0;

    @FXML
    private AnchorPane Big;
    @FXML
    private AnchorPane SplashScreen;
    @FXML
    private Button joinButton;
    @FXML
    private TextField usernameInput;
    @FXML
    private Label logoLabel;
    //    @FXML
//    private AnchorPane modeSwitch;
    @FXML
    private AnchorPane modeOne;
    @FXML
    private Label modeText;
    @FXML
    private Label setUsername;
    @FXML
    private Text alert;
    @FXML
    private AnchorPane gameRules;
    @FXML
    private Button rulesButton;
    @FXML
    private ImageView singlePlayerImageView;
    @FXML
    private ImageView multiPlayerImageView;

    /**
     * Constructor for the SplashScreenCtrl. We also initialize the gameCtrl and questionCtrl because these
     * scenes will be set from this class.
     *
     * @param server
     * @param gameCtrl
     * @param questionCtrl
     * @throws MalformedURLException
     */
    @Inject
    public SplashScreenCtrl(ServerUtils server, GameCtrl gameCtrl, QuestionCtrl questionCtrl) throws MalformedURLException {
        this.server = server;
        this.gameCtrl = gameCtrl;
    }

    /**
     * This function sets the username and moves to the gameScreen.
     * It checks that the username has been input before being able to join, if not, an alert is raised.
     * For solo player, the gameCtrl sets the attributes for the User to play and for multiplayer it checks
     * that this username isn't taken.
     */
    public void join() {
        if (usernameInput.getText().equals("") || usernameInput.getText() == null) {
            alert.setText("Please, provide your username");
        } else {
            alert.setText("");
            if (mode == 0) {
                gameCtrl.setUsername(usernameInput.getText());
                try {
                    gameCtrl.SoloGameRound();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            } else {
                boolean isValidUsername = server.isValidUsername(usernameInput.getText());
                if (!isValidUsername)
                    alert.setText("This username is already taken");
                else
                    startMultiPlayerGame();
            }
        }
    }

    /**
     * This function gets called whenever a player presses join to start a multiplayer game.
     * First a User is made and this user gets send to the server. The server will add this user to the
     * current lobby.
     * <p>
     * Then, the user GETs the number of the current open lobby. With this number, the player registers
     * for the web socket channel of that lobby. Everytime the player receives a question from this channel,
     * the startMultiPlayerQuestion function in the gameCtrl is called and the question gets past. In this
     * function, the client side will set up the given question.
     */
    public void startMultiPlayerGame() {
        String username = usernameInput.getText();
        gameCtrl.username = usernameInput.getText();
        User user = new User(username, 0);
        server.addUser(user);
        int currentOpenLobby = server.getCurrentLobby();
        gameCtrl.joinedLobby = currentOpenLobby;
        String destination = "/topic/question" + String.valueOf(currentOpenLobby);
        System.out.println("Subscribing for " + destination);
        StompSession.Subscription subscription = server.registerForMessages(destination, q -> {

            Platform.runLater(() -> {
                if (q.typeOfMessage.equals("QUESTION")) {
                    System.out.println("CLIENT RECEIVED QUESTION OVER WEBSOCKET");
                    gameCtrl.startMultiPlayerQuestion(q.question);
                } else if (q.typeOfMessage.equals("EMOJIONE")) {
                    System.out.println("CLIENT RECEIVED EMOJIONE OVER WEBSOCKET");
                    gameCtrl.showEmoji(1, q.emojiUsername);
                } else if (q.typeOfMessage.equals("EMOJITWO")) {
                    System.out.println("CLIENT RECEIVED EMOJITWO OVER WEBSOCKET");
                    gameCtrl.showEmoji(2, q.emojiUsername);
                } else if (q.typeOfMessage.equals("EMOJITHREE")) {
                    System.out.println("CLIENT RECEIVED EMOJITHREE OVER WEBSOCKET");
                    gameCtrl.showEmoji(3, q.emojiUsername);
                } else if (q.typeOfMessage.equals("LEADERBOARD")) {
                    System.out.println("TIME FOR LEADERBOARD!");
                    //function for showing the leaderboard
                }

            });

        });
        gameCtrl.subscription = subscription;
        gameCtrl.joinCurrentLobby();

    }

    /**
     * If the Rules button in the Splash Screen is pressed, it will disable the other buttons whilst the
     * rules pop up is there.
     * It also blurs the main scene.
     */
    public void GameRulesButton() {
        gameRules.setVisible(true);
        joinButton.setDisable(true);
        rulesButton.setDisable(true);
        if (alert != null) {
            alert.setText("");
        }
        Big.setEffect(new BoxBlur(1238, 800, 1));

    }

    /**
     * If the Exit button from the Rules pop up is pressed, the scene is set back to the Splash Screen.
     */
    public void exit() {
        gameRules.setVisible(false);
        joinButton.setDisable(false);
        rulesButton.setDisable(false);
        Big.setEffect(null);
    }


    /**
     * This function is a setup for the splash screen.
     */
    public void setSplashScreen() {
        /*if (gameCtrl.username == null || gameCtrl.username.equals("")) {
            usernameInput.setText("");
        } else {
            usernameInput.setText(gameCtrl.username);
        }*/
        System.out.println(gameCtrl.username);
        usernameInput.setText(gameCtrl.username);
    }

    /**
     * This will get a random question from the Activity database
     *
     * @return random question from the serverUtils.
     */
    public Question getRandomQuestion() {
        return server.getQuestion();
    }

    /**
     * This function gets called whenever a player switches the mode.
     * The switch will change on the front end, and on the backend the mode will change.
     */
    public void switchMode() {
        if (mode == 0) {
            changeSwitch();
            mode = 1;
            modeText.setText("Multi Player");
            singlePlayerImageView.setVisible(false);
            multiPlayerImageView.setVisible(true);
        } else {
            changeSwitch();
            mode = 0;
            modeText.setText("Single Player");
            singlePlayerImageView.setVisible(true);
            multiPlayerImageView.setVisible(false);
        }
    }

    /**
     * Function for making the transition of the switch from single player mode to
     * multiplayer mode smooth.
     */
    public void changeSwitch() {
        if (mode == 0) {
            TranslateTransition transition = new TranslateTransition();
            transition.setDuration(Duration.seconds(0.2));
            transition.setToX(71);
            transition.setNode(modeOne);
            transition.play();
        } else {
            TranslateTransition transition = new TranslateTransition();
            transition.setDuration(Duration.seconds(0.2));
            transition.setToX(0);
            transition.setNode(modeOne);
            transition.play();
        }
    }
    public void toAdminScreen() {
        gameCtrl.showAdminScreen();
    }

    @FXML
    void keyPressed(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            join();
        }
    }
}
