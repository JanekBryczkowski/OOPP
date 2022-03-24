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
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.springframework.messaging.simp.stomp.StompSession;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.net.MalformedURLException;

public class SplashScreenCtrl {

    private final ServerUtils server;
    private final GameCtrl gameCtrl;
    private Stage primaryStage;

    //If mode is set to 0, then single player is active
    //If mode is set to 1, then multi player is active
    public int mode = 0;

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

    @Inject
    public SplashScreenCtrl(ServerUtils server, GameCtrl gameCtrl, QuestionCtrl questionCtrl) throws MalformedURLException {
        this.server = server;
        this.gameCtrl = gameCtrl;
    }

    //This function sets the username and moves to the gamescreen
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
                if (isValidUsername == false)
                    alert.setText("This username is already taken");
                else
                    startMultiPlayerGame();
            }
        }
    }

    /*
    This function gets called whenever a player presses join to start a multiplayer game.
    First a User is made and this user gets send to the server. The server will add this user to the
    current lobby.

    Then, the user GETs the number of the current open lobby. With this number, the player registers
    for the websocket channel of that lobby. Everytime the player receives a question from this channel,
    the startMultiPlayerQuestion function in the gameCtrl is called and the question gets passed. In this
    function, the client side will set up the given question
     */
    public void startMultiPlayerGame() {
        String username = usernameInput.getText();
        gameCtrl.username = usernameInput.getText();
        User user = new User(username, 0);
        server.addUser(user);
        int currentOpenLobby = server.getCurrentLobby();
        String destination = "/topic/question" + String.valueOf(currentOpenLobby);
        StompSession.Subscription subscription = server.registerForMessages(destination, q -> {

            System.out.println("RECEIVED A QUESTION FROM /topic/question");
            Platform.runLater(() -> {
                gameCtrl.startMultiPlayerQuestion(q);
            });

        });
        gameCtrl.subscription = subscription;
        gameCtrl.joinCurrentLobby();
    }

    public void GameRulesButton() {
        gameRules.setVisible(true);
        joinButton.setDisable(true);
        rulesButton.setDisable(true);
        if (alert != null) {
            alert.setText("");
        }
        Big.setEffect(new BoxBlur(1238, 800, 1));

    }

    public void exit() {
        gameRules.setVisible(false);
        joinButton.setDisable(false);
        rulesButton.setDisable(false);
        Big.setEffect(null);

    }


    //This function is a setup for the splash screen.
    public void setSplashScreen() {
//        setUsername.setVisible(false);
//        modeTwo.setVisible(false);
        usernameInput.setText("");
    }

    public Question getRandomQuestion() {
        return server.getQuestion();
    }

    /*
    This function gets called whenever a player switches the mode.
    The switch will change on the front end, and on the backend the mode will change.
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

}
