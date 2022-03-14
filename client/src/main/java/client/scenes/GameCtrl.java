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
import commons.Question;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

public class GameCtrl {

    private Stage primaryStage;

    private SplashScreenCtrl splashScreenCtrl;
    private Scene splashScreenScene;

    private QuestionCtrl questionCtrl;
    private Scene questionScreen;

    private AddQuoteCtrl addCtrl;
    private Scene add;

    private LeaderBoardCtrl leaderBoardCtrl;
    private Scene leaderBoard;

    private ServerUtils server;

    public int points = 0;
    public int round = 1;
    public String username;

    public void initialize(Stage primaryStage, Pair<SplashScreenCtrl, Parent> splash, Pair<QuestionCtrl, Parent> questionCtrl, Pair<LeaderBoardCtrl, Parent> leaderBoardCtrl) {
        this.primaryStage = primaryStage;

        this.splashScreenCtrl = splash.getKey();
        this.splashScreenScene = new Scene(splash.getValue());

        this.questionCtrl = questionCtrl.getKey();
        this.questionScreen = new Scene(questionCtrl.getValue());

        this.leaderBoardCtrl = leaderBoardCtrl.getKey();
        this.leaderBoard = new Scene(leaderBoardCtrl.getValue());

        showSplashScreen();
        primaryStage.show();
    }

    //This function is for showing the SplashScreen
    public void showSplashScreen() {
        System.out.println("Xd 2");
        primaryStage.setTitle("Splash Screen");
        splashScreenCtrl.setSplashScreen();
        splashScreenScene.getStylesheets().add("client.styles/SplashScreenStyle.css");
        primaryStage.setScene(splashScreenScene);
        //primaryStage.setFullScreen(true);
        //primaryStage.setFullScreenExitHint("");
    }

    //Setter for username
    public void setUsername(String username) {
        this.username = username;
    }

    //This function is for showing the gamescreen
    public void SoloGameRound() {
        System.out.println("Xd 3");
        //Plays 5 rounds
        if (round > 5) {
            System.out.println("Xd 4");
            showLeaderBoard();
        } else {
            System.out.println("Xd 5");
            Question question = splashScreenCtrl.getRandomQuestion();
            System.out.println("size" + question.activityList.size());
            int xd = 1; //JUST FOR TESTING PURPOSES. IF 1, THEN 1 ACTIVITY, IF 3 THEN 3 ACTIVITIES
            switch (/*question.activityList.size()*/xd) {
                case (1): {
                    System.out.println("Xd 6");
                    oneActivityQuestion(question);
                    break;
                }
                case (2): {
                    System.out.println("Xd 7");
                    threeActivityQuestion(question);
                    break;
                }
                case (3): {
                    System.out.println("Xd 8");
                    threeActivityQuestion(question);
                    break;
                }
                default: {
                    System.out.println("Xd 9");
                    threeActivityQuestion(question);
                    break;
                }
            }
        }
    }

    //Setup for a question with three activities
    public void oneActivityQuestion(Question question) {
        questionCtrl.startQuestion(question);
        primaryStage.setTitle("Game screen - 1 activity question");
        primaryStage.setScene(questionScreen);
        questionCtrl.setOneActivity();
        questionScreen.getStylesheets().add("client.styles/QuestionScreenStyles.css");
    }

    //Setup for a question with three activities
    public void threeActivityQuestion(Question question) {
        questionCtrl.startQuestion(question);
        primaryStage.setTitle("Game screen - 3 activities question");
        primaryStage.setScene(questionScreen);
        questionCtrl.setThreeActivities();
        questionScreen.getStylesheets().add("client.styles/QuestionScreenStyles.css");
    }

    //Function for showing the leaderboard
    public void showLeaderBoard() {
        leaderBoardCtrl.setLeaderBoard();
        primaryStage.setScene(leaderBoard);
    }
}