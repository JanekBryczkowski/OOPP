package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.User;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;
import java.util.List;

public class WaitingRoomCtrl {

    private final ServerUtils server;
    private final GameCtrl mainCtrl;

    private List<User> userList = new ArrayList<>();
    @FXML
    private Text numberOf;

    @FXML
    private ScrollPane waitingScroll;


    @Inject
    public WaitingRoomCtrl(ServerUtils server, GameCtrl mainCtrl, Text numberOf) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.numberOf = numberOf;

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
        System.out.println("Chicken vol2");
        mainCtrl.showSplashScreen();
        mainCtrl.subscription.unsubscribe();
        server.removeUser(mainCtrl.username);
        setWaitingRoomTable();
    }

    public void setWaitingRoomTable() {
        userList.addAll(server.getUsersInLobby());
        numberOf.setText("");
        if (userList.size() == 1) {
            numberOf.setText(userList.size() + " player in the waiting room");
        } else {
            numberOf.setText(userList.size() + " players in the waiting room");
        }
        showInWaitingRoomTable();
    }

    public void showInWaitingRoomTable() {
        VBox vbox = new VBox();
        for (User user : userList) {
            AnchorPane anchorPane = new AnchorPane();
            anchorPane.setMaxHeight(30);
            anchorPane.setMinHeight(30);
            anchorPane.setMaxWidth(620);
            anchorPane.setMinWidth(620);
            Label playerList = new Label(user.getUsername());
            playerList.setWrapText(true);
            playerList.setTextAlignment(TextAlignment.CENTER);
            playerList.setPrefHeight(30);
            playerList.setMaxHeight(30);
            playerList.setMinHeight(30);
            playerList.setMaxWidth(350);
            playerList.setMinWidth(350);
            playerList.setPrefWidth(350);
            playerList.setLayoutX(0);
            playerList.setLayoutY(0);
            playerList.setStyle("-fx-font-size: 16;");
            playerList.setAlignment(Pos.CENTER);

            anchorPane.getChildren().add(playerList);
            vbox.getChildren().add(anchorPane);
        }
        waitingScroll.setContent(vbox);
    }

    /*@FXML
    void keyPressed(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            System.out.println("Chicken");
            play();
        }
    }*/
}