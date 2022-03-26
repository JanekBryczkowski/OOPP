package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class WaitingRoomCtrl {

    private final ServerUtils server;
    private final GameCtrl mainCtrl;

    @FXML
    private Text playersInRoom;

    @Inject
    public WaitingRoomCtrl(ServerUtils server, GameCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    /*
    This functions gets called when a user presses PLAY. The server will send a GET request
    to start the game on the server side
     */
    public void play() {
        System.out.println("USER PRESSED PLAY");
        server.startGame();
    }

    //IMPORTANT NOTATION
    //When fixing the table in the waiting room
    //make sure to implement to this method the removal of the player
    //from the table when pressing the back button

    public void backButton() {
        mainCtrl.showSplashScreen();
    }
}