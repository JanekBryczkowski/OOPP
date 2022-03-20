package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Activity;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class AdminCtrl {

    private final ServerUtils server;
    private final GameCtrl gameCtrl;
    List<Activity> activityList = new ArrayList<>();


    @FXML
    private Button inspectButton;

    @FXML
    private TextField activityText;

    @FXML
    private TextField consumptionText;

    @FXML
    private TextField deleteText;



    //Connecting the page with the server, and the GameCtrl
    @Inject
    public AdminCtrl(ServerUtils server, GameCtrl gameCtrl) {
        this.server = server;
        this.gameCtrl = gameCtrl;
    }

    public void setTable() {
        activityList.addAll(server.showAll());
    }




}
