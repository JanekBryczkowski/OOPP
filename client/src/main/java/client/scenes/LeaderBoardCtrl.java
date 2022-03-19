package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Score;
import jakarta.ws.rs.WebApplicationException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Modality;

import java.util.ArrayList;
import java.util.List;

public class LeaderBoardCtrl {

    private final ServerUtils server;
    private final GameCtrl gameCtrl;
    private Score first;
    private Score second;
    private Score third;

    @FXML
    private Text firstName;

    @FXML
    private Text firstScore;
    @FXML
    private Text secondName;

    @FXML
    private Text secondScore;

    @FXML
    private Text thirdName;

    @FXML
    private Text thirdScore;

    @FXML
    private ScrollPane leaderBoardList;


    @FXML
    private TableColumn<Integer, String> scoreTable;
    @FXML
    private TableColumn<String, String> usernameTable;
    @FXML
    private TableView table;

    List<Score> topThreeList = new ArrayList<>();
    List<Score> scoreList = new ArrayList<>();
    ObservableList<Score> scores;


    @Inject
    public LeaderBoardCtrl(ServerUtils server, GameCtrl mainCtrl) {
        this.server = server;
        this.gameCtrl = mainCtrl;
        scores = FXCollections.observableArrayList();
    }

    public void storePoints() {
        try {
            Score score = new Score(gameCtrl.username, gameCtrl.points);
            server.addScore(score);
        } catch (WebApplicationException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }
    }

    public void setLeaderBoard() {
        topThreeList.addAll(server.getTopScores());
        firstName.setText(topThreeList.get(0).getUsername());
        firstScore.setText(String.valueOf(topThreeList.get(0).getScore()));
        secondName.setText(topThreeList.get(1).getUsername());
        secondScore.setText(String.valueOf(topThreeList.get(1).score));
        thirdName.setText(topThreeList.get(2).getUsername());
        thirdScore.setText(String.valueOf(topThreeList.get(2).score));
    }

    public void setList() {
        scores.addAll(server.getScores());
        scoreList.addAll(server.getScores());
        usernameTable.setCellValueFactory(new PropertyValueFactory<>("username"));
        scoreTable.setCellValueFactory(new PropertyValueFactory<>("score"));

        System.out.println(scores);
//        table.setItems(scores);
    }

    public void backToSplash() {
        gameCtrl.points = 0;
        gameCtrl.round = 1;
        gameCtrl.firstJokerUsed = false;
        gameCtrl.secondJokerUsed = false;
        gameCtrl.showSplashScreen();
    }

}
