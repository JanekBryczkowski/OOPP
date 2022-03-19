package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Score;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
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
//    @FXML
//    private Label secondName;
//
//    @FXML
//    private Label secondScore;
//
//    @FXML
//    private Label thirdName;
//
//    @FXML
//    private Label thirdScore;

    List<Score> scoreList = new ArrayList<>();


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

//    public void calculateTopThree() {
//        first = new Score("a", 0);
//        second = new Score("a", 0);
//        third = new Score("a", 0);
//        List<Score> scoreList = server.getScores();
//        for(Score score : scoreList) {
//            if(score.score > first.getScore()) {
//                first = score;
//            } else if(score.score > second.getScore() && score.score < first.getScore()) {
//                second = score;
//            } else if(score.score > third.getScore() && score.score < second.getScore()) {
//                third = score;
//            }
//        }
//    }

    public void setLeaderBoard() {
        firstName.setText(gameCtrl.username);
        firstScore.setText(String.valueOf(gameCtrl.points));
        // correct way to do it but not fully working
//        scoreList.addAll(server.getScores());
//        firstName.setText(scoreList.get(0).getUsername());
//        firstScore.setText(String.valueOf(scoreList.get(0).getScore()));
//        secondName.setText(scoreList.get(1).getUsername());
//        secondScore.setText(String.valueOf(scoreList.get(1).score));
//        thirdName.setText(scoreList.get(2).getUsername());
//        thirdScore.setText(String.valueOf(scoreList.get(2).score));
    }

    public void backToSplash() {
        gameCtrl.points = 0;
        gameCtrl.round = 1;
        gameCtrl.firstJokerUsed = false;
        gameCtrl.secondJokerUsed = false;
        gameCtrl.showSplashScreen();
    }

}
