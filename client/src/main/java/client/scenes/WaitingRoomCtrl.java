package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.User;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

public class WaitingRoomCtrl {

    private final ServerUtils server;
    private final GameCtrl mainCtrl;

    private List<User> userList = new ArrayList<>();

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


}
