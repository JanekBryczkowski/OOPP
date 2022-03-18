package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import com.google.inject.Stage;
import commons.Question;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.net.MalformedURLException;

public class SplashScreenCtrl {

    private final ServerUtils server;
    private final GameCtrl gameCtrl;
    private final QuestionCtrl questionCtrl;
    private Stage primaryStage;


    @FXML
    private AnchorPane Big;
    @FXML
    private AnchorPane SplashScreen;
    @FXML
    private Button joinButton;
    @FXML
    private TextField usernameInput;
    @FXML
    private Text alert;
    @FXML
    private AnchorPane gameRules;


    @Inject
    public SplashScreenCtrl(ServerUtils server, GameCtrl gameCtrl, QuestionCtrl questionCtrl) throws MalformedURLException {
        this.server = server;
        this.gameCtrl = gameCtrl;
        this.questionCtrl = questionCtrl;
    }

    //This function sets the username and moves to the gamescreen
    public void join() {
        if (usernameInput.getText().equals("") || usernameInput.getText() == null) {
            //alert.setText("Please, provide your username");
        } else {
            //alert.setText("");
            gameCtrl.setUsername(usernameInput.getText());
            gameCtrl.SoloGameRound();
        }
    }

    public void GameRulesButton() {
        gameRules.setVisible(true);
        //SplashScreen.setEffect(BoxBlur);

    }

    public void exit() {
        gameRules.setVisible(false);
    }


    //This function is a setup for the splash screen.
    public void setSplashScreen() {
        usernameInput.setText("");
    }

    public Question getRandomQuestion() {
        return server.getQuestion();
    }

}
