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

    /**
     * This List of users will store the users playing throughout the game.
     */
    public static List<User> userList = new ArrayList<>();

    @FXML
    private Text numberOf;

    @FXML
    private ScrollPane waitingScroll;

    /**
     * Constructor for the WaitingRoomCtrl. We also initialize the gameCtrl, and text number of because these
     * scenes will be set from this class.
     *
     * @param server - specific server provided for the game.
     * @param mainCtrl - game controller for the game inside the constructor class.
     * @param numberOf - text indicating the number of users that are currently in the lobby of the game.
     */
    @Inject
    public WaitingRoomCtrl(ServerUtils server, GameCtrl mainCtrl, Text numberOf) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.numberOf = numberOf;
    }

    /**
     * This functions gets called when a user presses PLAY. The server will send a GET request
     * to start the game on the server side
     **/
    public void play() {
        System.out.println("USER PRESSED PLAY");
        server.startGame();
    }

    /**
     * The functionality for the back button to go back to the splash screen.
     */
    public void backButton() {
        server.removeUser(mainCtrl.username, server.getCurrentLobby());
        mainCtrl.showSplashScreen();
        mainCtrl.subscription.unsubscribe();
    }

    /**
     * Method responsible for setting the list of users in the waiting room scroll pane,
     * as well as the number of players currently waiting in the lobby.
     */
    public void setWaitingRoomTable() {
        userList = new ArrayList<>();
        userList.addAll(server.getUsersInLobby());
        numberOf.setText("");
        if (userList.size() == 1) {
            numberOf.setText(userList.size() + " player in the waiting room");
        } else {
            numberOf.setText(userList.size() + " players in the waiting room");
        }
        showInWaitingRoomTable();
    }

    /**
     * Method responsible for automatically refreshing the waiting room table.
     */
    public void refreshTable() {
        userList.clear();
        setWaitingRoomTable();
    }

    /**
     * Method responsible for setting the list of users in the waiting room scroll pane.
     */
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

    /**
     * Method responsible for doing the same thing as the JOIN button in splash screen,
     * when ENTER is clocked.
     *
     * @param event - keyboard event when ENTER is clicked.
     */
    @FXML
    void keyPressed(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            play();
        }
    }
}