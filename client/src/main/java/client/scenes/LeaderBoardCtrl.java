package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class LeaderBoardCtrl {

    private final ServerUtils server;
    private final GameCtrl gameCtrl;

    @FXML
    private Label scoreOne;
    @FXML
    private Label oneName;

    @Inject
    public LeaderBoardCtrl(ServerUtils server, GameCtrl mainCtrl) {
        this.server = server;
        this.gameCtrl = mainCtrl;
    }

    public void setLeaderBoard() {
        scoreOne.setText(String.valueOf(gameCtrl.points));
        oneName.setText(gameCtrl.username);
    }


    public void backToSplash() {
        gameCtrl.points = 0;
        gameCtrl.round = 1;
        gameCtrl.showSplashScreen();
    }

}
