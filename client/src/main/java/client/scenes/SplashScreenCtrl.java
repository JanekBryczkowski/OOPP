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
    private final GameCtrl mainCtrl;
    private final QuestionThreeCtrl questionThreeCtrl;
    private Stage primaryStage;


    @FXML
    private Button joinButton;
    @FXML
    private TextField usernameInput;
    @FXML
    private Label logoLabel;


    @Inject
    public SplashScreenCtrl(ServerUtils server, GameCtrl mainCtrl, Stage primaryStage, QuestionThreeCtrl questionThreeCtrl) throws MalformedURLException {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.questionThreeCtrl = questionThreeCtrl;
    }

    //This function sets the username and moves to the gamescreen
    public void join() {
        if(usernameInput.getText() == "") {
            logoLabel.setText("SET USERNAME");
        } else {
            mainCtrl.setUsername(usernameInput.getText());
            mainCtrl.SoloGameRound();
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
