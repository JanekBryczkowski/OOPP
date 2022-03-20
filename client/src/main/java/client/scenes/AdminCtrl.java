package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Activity;

import javafx.geometry.Pos;
//import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class AdminCtrl {

    private final ServerUtils server;
    private final GameCtrl gameCtrl;
    List<Activity> activityList = new ArrayList<>();


//    @FXML
//    private Button inspectButton;

//    @FXML
//    private TextField activityText;
//
//    @FXML
//    private TextField consumptionText;
//
//    @FXML
//    private TextField deleteText;

//    @FXML
//    private ScrollPane scroll;



    //Connecting the page with the server, and the GameCtrl
    @Inject
    public AdminCtrl(ServerUtils server, GameCtrl gameCtrl) {
        this.server = server;
        this.gameCtrl = gameCtrl;
    }

    public void setTable() {
        activityList.addAll(server.showAll());
        showAllActivities(activityList);
    }

    public void showAllActivities(List<Activity> activityList) {
        VBox vbox = new VBox();
        for (Activity activity : activityList) {
            AnchorPane anchorPane = new AnchorPane();
            anchorPane.setMaxHeight(30);
            anchorPane.setMinHeight(30);
            anchorPane.setMinHeight(30);
            anchorPane.setMaxWidth(347);
            anchorPane.setMinWidth(347);
            anchorPane.setPrefWidth(347);
            Label activityText = new Label();
            activityText.setMaxHeight(30);
            activityText.setMinHeight(30);
            activityText.setPrefHeight(30);
            activityText.setMaxWidth(200);
            activityText.setMinWidth(200);
            activityText.setPrefWidth(200);
            activityText.setLayoutX(0);
            activityText.setLayoutY(0);
            activityText.setStyle("-fx-font-size: 16;");
            activityText.setAlignment(Pos.CENTER);
            Label Consumption = new Label();
            Consumption.setMaxHeight(30);
            Consumption.setMinHeight(30);
            Consumption.setPrefHeight(30);
            Consumption.setMaxWidth(147);
            Consumption.setMinWidth(147);
            Consumption.setPrefWidth(147);
            Consumption.setLayoutX(200);
            Consumption.setLayoutY(0);
            Consumption.setStyle("-fx-font-size: 16;");
            Consumption.setAlignment(Pos.CENTER);
            anchorPane.getChildren().add(activityText);
            anchorPane.getChildren().add(Consumption);
            vbox.getChildren().add(anchorPane);
        }
//        scroll.setContent(vbox);

    }




}
