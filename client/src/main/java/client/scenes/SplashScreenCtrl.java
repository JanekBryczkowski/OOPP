package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import com.google.inject.Stage;
import commons.Question;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.MalformedURLException;

public class SplashScreenCtrl {

    private final ServerUtils server;
    private final GameCtrl gameCtrl;
    private final QuestionThreeCtrl questionThreeCtrl;
    private Stage primaryStage;


    @FXML
    private Button joinButton;
    @FXML
    private TextField usernameInput;
    @FXML
    private Label logoLabel;


    @Inject
    public SplashScreenCtrl(ServerUtils server, GameCtrl gameCtrl, Stage primaryStage, QuestionThreeCtrl questionThreeCtrl) throws MalformedURLException {
        this.server = server;
        this.gameCtrl = gameCtrl;
        this.questionThreeCtrl = questionThreeCtrl;
    }

    //This function sets the username and moves to the gamescreen
    public void join() {
        if (usernameInput.getText().equals("") || usernameInput.getText() == null) {
            logoLabel.setText("SET USERNAME");
        } else {
            gameCtrl.setUsername(usernameInput.getText());
            gameCtrl.SoloGameRound();
        }
    }

    //This function is a setup for the splash screen.
    public void setSplashScreen() {
        usernameInput.setText("");
    }

    public Question getRandomQuestion() {
        return server.getQuestion();
    }

}
