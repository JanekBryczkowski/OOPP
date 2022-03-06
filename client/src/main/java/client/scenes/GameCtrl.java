/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client.scenes;

import client.utils.ServerUtils;
import commons.Activity;
import commons.Question;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

public class GameCtrl {

    private Stage primaryStage;

    private SplashScreenCtrl overviewCtrl;
    private Scene overview;

    private GameScreenControl gameCtrl;
    private Scene gameScreen;

    private AddQuoteCtrl addCtrl;
    private Scene add;

    private LeaderBoardCtrl leaderBoardCtrl;
    private Scene leaderBoard;

    private ServerUtils server;

    public int points = 0;
    public int round = 1;
    public String username;

    public void initialize(Stage primaryStage, Pair<SplashScreenCtrl, Parent> overview, Pair<GameScreenControl, Parent> gameCtrl, Pair<LeaderBoardCtrl, Parent> leaderBoardCtrl) {
        this.primaryStage = primaryStage;

        this.overviewCtrl = overview.getKey();
        this.overview = new Scene(overview.getValue());

        this.gameCtrl = gameCtrl.getKey();
        this.gameScreen = new Scene(gameCtrl.getValue());

        this.leaderBoardCtrl = leaderBoardCtrl.getKey();
        this.leaderBoard = new Scene(leaderBoardCtrl.getValue());

        showOverview();
        primaryStage.show();
    }

    //This function is for showing the SplashScreen
    public void showOverview() {
        primaryStage.setTitle("Quotes: Overview");
        overviewCtrl.setSplashScreen();
        primaryStage.setScene(overview);
        primaryStage.setFullScreen(true);
        primaryStage.setFullScreenExitHint("");
    }

    //Setter for username
    public void setUsername(String username) {
        this.username = username;
    }

    //This function is for showing the gamescreen
    public void showGameScreen() {
        //Plays 5 rounds
        if(round > 5) {
            showLeaderBoard();
        } else {
            Question question = overviewCtrl.getRandomQuestion();
            switch (question.activityList.size()) {
                case (1):
                    threeActivityQuestion(question);
                    break;
                case (2):
                    threeActivityQuestion(question);
                    break;
                case (3):
                    threeActivityQuestion(question);
                    break;
                default:
                    break;
            }
        }
    }

    //Setup for a question with three activities
    public void threeActivityQuestion(Question question) {
        gameCtrl.setQuestion(question);
        primaryStage.setScene(gameScreen);
        primaryStage.setFullScreen(true);
    }

    //Function for showing the leaderboard
    public void showLeaderBoard() {
        leaderBoardCtrl.setLeaderBoard();
        primaryStage.setScene(leaderBoard);
        primaryStage.setFullScreen(true);

    }


}