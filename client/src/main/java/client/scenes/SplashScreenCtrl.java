package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import com.google.inject.Stage;
import commons.Question;
import commons.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.net.MalformedURLException;

public class SplashScreenCtrl {

    private final ServerUtils server;
    private final GameCtrl gameCtrl;
    private final QuestionCtrl questionCtrl;
    private Stage primaryStage;

    //If mode is set to 0, then single player is active
    //If mode is set to 1, then multi player is active
    private int mode = 0;

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
    private AnchorPane modeTwo;
    @FXML
    private Label modeText;
    @FXML
    private Label setUsername;

    @Inject
    public SplashScreenCtrl(ServerUtils server, GameCtrl gameCtrl, Stage primaryStage, QuestionCtrl questionCtrl) throws MalformedURLException {
        this.server = server;
        this.gameCtrl = gameCtrl;
        this.questionCtrl = questionCtrl;
    }

    //This function sets the username and moves to the gamescreen
    public void join() {
        if (usernameInput.getText().equals("") || usernameInput.getText() == null) {
            setUsername.setVisible(true);
        }
        else {
            if(mode == 0)
                startSinglePlayerGame();
            else
                startMultiPlayerGame();
        }

    }

    public void startSinglePlayerGame() {
            gameCtrl.setUsername(usernameInput.getText());
            gameCtrl.SoloGameRound();
    }

    public void startMultiPlayerGame() {
        String username = usernameInput.getText();
        User user = new User(username,0);
        server.addUser(user);
        gameCtrl.startMultiPlayer();
    }

    //This function is a setup for the splash screen.
    public void setSplashScreen() {
        setUsername.setVisible(false);
        modeTwo.setVisible(false);
        usernameInput.setText("");
    }

    public Question getRandomQuestion() {
        return server.getQuestion();
    }

    public void switchMode() {
        if(mode == 0) {
            modeOne.setVisible(false);
            modeTwo.setVisible(true);
            mode = 1;
            modeText.setText("Multi Player");
        } else {
            modeOne.setVisible(true);
            modeTwo.setVisible(false);
            mode = 0;
            modeText.setText("Single Player");
        }
    }

}
