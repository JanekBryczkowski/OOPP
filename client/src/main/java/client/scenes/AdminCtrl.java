package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Activity;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;

import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;


public class AdminCtrl {

    private final ServerUtils server;
    private final GameCtrl gameCtrl;
    private String imagePath = "79/China-Briefing-Investing-in-Chinas-Green-Industries.jpg";
    List<Activity> activityList = new ArrayList<>();

    @FXML
    private ScrollPane scroll;
    @FXML
    private TextField idText;
    @FXML
    private TextField activityText;
    @FXML
    private TextField consumptionText;
    @FXML
    private Text alert;



    /**
     * Connecting the page with the server and the GameCtrl.
     * @param server
     * @param gameCtrl
     */
    @Inject
    public AdminCtrl(ServerUtils server, GameCtrl gameCtrl) {
        this.server = server;
        this.gameCtrl = gameCtrl;
    }

    /**
     * Adds all the activities to the table and calls the function showAllActivities.
     */
    public void setTable() {
        System.out.println("xd");
        activityList.addAll(server.showAll());
        showAllActivities(activityList);
    }

    /**
     * Sets the table where all the activities are going to appear in the AdminScreen.
     * @param activityList
     */
    public void showAllActivities(List<Activity> activityList) {
        VBox vbox = new VBox();
        for (Activity activity : activityList) {
            AnchorPane anchorPane = new AnchorPane();
            anchorPane.setMaxHeight(30 * ((int) (activity.title.length() / 35) + 1));
            anchorPane.setMinHeight(30 * ((int) (activity.title.length() / 35) + 1));
            anchorPane.setMinHeight(30 * ((int) (activity.title.length() / 35) + 1));
            anchorPane.setMaxWidth(620);
            anchorPane.setMinWidth(620);
            anchorPane.setPrefWidth(620);
            Label id = new Label(String.valueOf(activity.id));
            id.setWrapText(true);
            id.setTextAlignment(TextAlignment.CENTER);
            id.setMaxHeight(30 * ((int) (activity.title.length() / 35) + 1));
            id.setMinHeight(30 * ((int) (activity.title.length() / 35) + 1));
            id.setPrefHeight(30 * ((int) (activity.title.length() / 35) + 1));
            id.setMaxWidth(190);
            id.setMinWidth(190);
            id.setPrefWidth(190);
            id.setLayoutX(8);
            id.setLayoutY(0);
            id.setStyle("-fx-font-size: 16;");
            id.setAlignment(Pos.CENTER_LEFT);
            Label activityLabel = new Label(activity.title);
            activityLabel.setWrapText(true);
            activityLabel.setTextAlignment(TextAlignment.CENTER);
            activityLabel.setPrefHeight(30 * ((int) (activity.title.length() / 35) + 1));
            activityLabel.setMaxHeight(30 * ((int) (activity.title.length() / 35) + 1));
            activityLabel.setMinHeight(30 * ((int) (activity.title.length() / 35) + 1));
            activityLabel.setMaxWidth(350);
            activityLabel.setMinWidth(350);
            activityLabel.setPrefWidth(350);
            activityLabel.setLayoutX(155);
            activityLabel.setLayoutY(0);
            activityLabel.setStyle("-fx-font-size: 16;");
            activityLabel.setAlignment(Pos.CENTER);
            Label consumption = new Label(String.valueOf(activity.consumption));
            consumption.setMaxHeight(30 * ((int) (activity.title.length() / 35) + 1));
            consumption.setMinHeight(30 * ((int) (activity.title.length() / 35) + 1));
            consumption.setPrefHeight(30 * ((int) (activity.title.length() / 35) + 1));
            consumption.setMaxWidth(190);
            consumption.setMinWidth(190);
            consumption.setPrefWidth(190);
            consumption.setLayoutX(450);
            consumption.setLayoutY(0);
            consumption.setStyle("-fx-font-size: 16;");
            consumption.setAlignment(Pos.CENTER);
            anchorPane.getChildren().add(id);
            anchorPane.getChildren().add(activityLabel);
            anchorPane.getChildren().add(consumption);
            vbox.getChildren().add(anchorPane);
        }
        scroll.setContent(vbox);
    }

    //Giving the option to the Admin to edit an activity by entering its id.
    public void editActivity() {
        if (idText.getText() == null || idText.getText().equals("")) {
            alert.setFill(Color.RED);
            alert.setText("Input ID of the activity");
            PauseTransition pauseTransition = new PauseTransition(Duration.seconds(5));
            pauseTransition.play();
            pauseTransition.setOnFinished(event -> {
                alert.setText("");
            });
        } else if ((activityText.getText().equals("") || activityText.getText() == null) && (consumptionText.getText().equals("") || consumptionText.getText() == null)) {
            alert.setFill(Color.RED);
            alert.setText("Please add the new activity title or the new consumption number, or both");
            PauseTransition pauseTransition = new PauseTransition(Duration.seconds(5));
            pauseTransition.play();
            pauseTransition.setOnFinished(event -> {
                alert.setText("");
            });
        } else {
            //System.out.println(server.getByID(idText.getText()));
            List<Activity> activtiyList = server.showAll();
            for (Activity act : activityList) {
                if (act.getId().equals(idText.getText())) {
                    Activity activity = act;
                    server.deleteQuestion(act.getId());
                    if (activityText.getText().equals("") || activityText.getText() == null) {
                        activity.setConsumption_in_wh(Integer.parseInt(consumptionText.getText()));
                    } else if (consumptionText.getText().equals("") || consumptionText.getText() == null) {
                        activity.setTitle(activityText.getText());
                    } else {
                        activity.setConsumption_in_wh(Integer.parseInt(consumptionText.getText()));
                        activity.setTitle(activityText.getText());
                    }
                    server.addActivity(activity);
                    alert.setFill(Color.GREEN);
                    alert.setText("Activity edited");
                    PauseTransition pauseTransition = new PauseTransition(Duration.seconds(5));
                    pauseTransition.play();
                    pauseTransition.setOnFinished(event -> {
                        alert.setText("");
                    });
                }
            }
            /*Activity activity = server.getByID("38-coffee");
            System.out.println(activity);
            System.out.println("activity created");
            server.deleteQuestion(idText.getText());
            System.out.println("activity deleted");*/
//        boolean done = false;
//        for (Activity act : activityList) {
//            if (idText.getText().equals(act.getId())) {
//                Activity activity = act;
//                server.deleteQuestion(act.getId());
//                done = true;
//            }
//            if (done) {
//                break;
//            }
//        }
        }

    }

    public void backToSplash() {
        gameCtrl.showSplashScreen();
    }

    public void deleteActivity() {
        if (idText.getText().equals("") || idText.getText() == null) {
            alert.setFill(Color.RED);
            alert.setText("Input ID of the activity");
            PauseTransition pauseTransition = new PauseTransition(Duration.seconds(5));
            pauseTransition.play();
            pauseTransition.setOnFinished(event -> {
                alert.setText("");
            });
        } else {
            for (Activity activity : activityList) {
                if (activity.getId().equals(idText.getText())) {
                    server.deleteQuestion(activity.getId());
                    System.out.println(activity.getId());
                    alert.setFill(Color.GREEN);
                    alert.setText("Activity deleted");
                    PauseTransition pauseTransition = new PauseTransition(Duration.seconds(5));
                    pauseTransition.play();
                    pauseTransition.setOnFinished(event -> {
                        alert.setText("");
                    });
                }
            }
        }
    }

    public void addActivity() {
        if (idText.getText() == null || idText.getText().equals("")) {
            alert.setFill(Color.RED);
            alert.setText("Input ID of the activity");
            PauseTransition pauseTransition = new PauseTransition(Duration.seconds(5));
            pauseTransition.play();
            pauseTransition.setOnFinished(event -> {
                alert.setText("");
            });
        } else if ((activityText.getText().equals("") || activityText.getText() == null) && (consumptionText.getText().equals("") || consumptionText.getText() == null)) {
            alert.setFill(Color.RED);
            alert.setText("Please add the new activity title or the new consumption number, or both");
            PauseTransition pauseTransition = new PauseTransition(Duration.seconds(5));
            pauseTransition.play();
            pauseTransition.setOnFinished(event -> {
                alert.setText("");
            });
        } else {
            Activity activity = new Activity(idText.getText(), imagePath, activityText.getText(), Integer.parseInt(consumptionText.getText()), null, Integer.parseInt(consumptionText.getText()));
            server.addActivity(activity);
            alert.setFill(Color.GREEN);
            alert.setText("Activity Added");
            PauseTransition pauseTransition = new PauseTransition(Duration.seconds(5));
            pauseTransition.play();
            pauseTransition.setOnFinished(event -> {
                alert.setText("");
            });
        }
    }


}
