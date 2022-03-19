package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Score;
import jakarta.ws.rs.WebApplicationException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;

import java.util.ArrayList;
import java.util.Comparator;
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
    private ScrollPane leaderBoardScrollPane;

    List<Score> topThreeList;
    List<Score> scoreList;
    ObservableList<Score> scores;

    @Inject
    public LeaderBoardCtrl(ServerUtils server, GameCtrl mainCtrl) {
        this.server = server;
        this.gameCtrl = mainCtrl;
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
        scores = FXCollections.observableArrayList();
        scoreList = new ArrayList<>();
        topThreeList = new ArrayList<>();
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
        sortList();
        uploadScoresIntoTheRanking(scoreList);
    }

    private void uploadScoresIntoTheRanking(List<Score> scoreList) {
        VBox vbox = new VBox();
        for (Score score : scoreList) {
            AnchorPane anchorPane = new AnchorPane();
            anchorPane.setMaxHeight(30);
            anchorPane.setMinHeight(30);
            anchorPane.setMinHeight(30);
            anchorPane.setMaxWidth(347);
            anchorPane.setMinWidth(347);
            anchorPane.setPrefWidth(347);
            Label usernameLabel = new Label(score.getUsername());
            usernameLabel.setMaxHeight(30);
            usernameLabel.setMinHeight(30);
            usernameLabel.setPrefHeight(30);
            usernameLabel.setMaxWidth(200);
            usernameLabel.setMinWidth(200);
            usernameLabel.setPrefWidth(200);
            usernameLabel.setLayoutX(0);
            usernameLabel.setLayoutY(0);
            usernameLabel.setStyle("-fx-font-size: 16;");
            usernameLabel.setAlignment(Pos.CENTER);
            Label scoreLabel = new Label(String.valueOf(score.getScore()));
            scoreLabel.setMaxHeight(30);
            scoreLabel.setMinHeight(30);
            scoreLabel.setPrefHeight(30);
            scoreLabel.setMaxWidth(147);
            scoreLabel.setMinWidth(147);
            scoreLabel.setPrefWidth(147);
            scoreLabel.setLayoutX(200);
            scoreLabel.setLayoutY(0);
            scoreLabel.setStyle("-fx-font-size: 16;");
            scoreLabel.setAlignment(Pos.CENTER);
            if (gameCtrl.username.equals(score.getUsername()) && gameCtrl.points == score.getScore()) {
                usernameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16; -fx-background-color: #D5DEB6;");
                scoreLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16; -fx-background-color: #D5DEB6;");
            }
            anchorPane.getChildren().add(usernameLabel);
            anchorPane.getChildren().add(scoreLabel);
            vbox.getChildren().add(anchorPane);
        }
        leaderBoardScrollPane.setContent(vbox);
    }

    public void backToSplash() {
        gameCtrl.points = 0;
        gameCtrl.round = 1;
        gameCtrl.firstJokerUsed = false;
        gameCtrl.secondJokerUsed = false;
        gameCtrl.showSplashScreen();
    }

    public void sortList() {
        scoreList.sort(new Comparator<Score>() {
            public int compare(Score score1, Score score2) {
                return score2.getScore() - score1.getScore();
            }
        });
    }
}
