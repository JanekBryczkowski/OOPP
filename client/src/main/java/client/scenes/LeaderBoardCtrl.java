package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class LeaderBoardCtrl {

    private final ServerUtils server;
    private final GameCtrl mainCtrl;

    @FXML
    private Label scoreOne;
    @FXML
    private Label oneName;

    @Inject
    public LeaderBoardCtrl(ServerUtils server, GameCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public void setLeaderBoard() {
        scoreOne.setText(String.valueOf(mainCtrl.points));
        oneName.setText(mainCtrl.username);
    }


    public void backToSplash() {
        mainCtrl.points = 0;
        mainCtrl.round = 1;
        mainCtrl.showSplashScreen();
    }

}
