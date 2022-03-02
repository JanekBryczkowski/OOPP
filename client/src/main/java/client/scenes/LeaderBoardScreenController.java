package client.scenes;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LeaderBoardScreenController /*implements Initializable*/ {

    /*@FXML
    private ScrollPane leaderBoardScrollPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //TODO: injection
        VBox content = new VBox();
        for (int i = 0; i < 10; i++) {
            Node node;
            try {
                node = FXMLLoader.load(getClass().getResource("/client/scenes/LeaderBoardEntryScreenController.fxml"));
                content.getChildren().add(node);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        leaderBoardScrollPane.setContent(content);
    }*/
}
