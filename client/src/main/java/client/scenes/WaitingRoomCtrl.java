package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

public class WaitingRoomCtrl {

    private final ServerUtils server;
    private final GameCtrl mainCtrl;
    SplashScreenCtrl splashScreenCtrl;

    private List<User> userList = new ArrayList<>();

    @FXML
    private Text playersInRoom;
    @FXML
    private Button backButton;
    @FXML
    private TextField usernameInput;


    @Inject
    public WaitingRoomCtrl(ServerUtils server, GameCtrl mainCtrl, SplashScreenCtrl splashScreenCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.splashScreenCtrl = splashScreenCtrl;
    }

    /*
    This functions gets called when a user presses PLAY. The server will send a GET request
    to start the game on the server side
     */
    public void play() {
        System.out.println("USER PRESSED PLAY");
        server.startGame();
    }

    // IMPORTAN NOTATION
    //When making the Players waiting table, this method needs
    // to be updated with the functionality of removing the player
    // from the table of the Players in Waiting room
    public void backButton() {
        mainCtrl.showSplashScreen();
//        usernameInput.setText(splashScreenCtrl.getUsername());
    }


}
