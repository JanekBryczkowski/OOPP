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
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
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
    private Scene leaderBoardScreen;

    private WaitingRoomCtrl waitingRoomCtrl;
    private Scene waitingRoom;

    private ServerUtils server;

    public int points = 0;
    public int round = 1;
    public String username;

    public  boolean firstJokerUsed = false;
    public  boolean secondJokerUsed = false;
    private final int ROUNDS = 10;

    @FXML
    private TextField usernameInput;

    public void initialize(Stage primaryStage, Pair<SplashScreenCtrl, Parent> splash, Pair<QuestionCtrl, Parent> questionCtrl, Pair<LeaderBoardCtrl, Parent> leaderBoardCtrl, Pair<WaitingRoomCtrl, Parent> waitingRoomCtrl) {
        this.primaryStage = primaryStage;

        this.splashScreenCtrl = splash.getKey();
        this.splashScreenScene = new Scene(splash.getValue());

        this.questionCtrl = questionCtrl.getKey();
        this.questionScreen = new Scene(questionCtrl.getValue());

        this.leaderBoardCtrl = leaderBoardCtrl.getKey();
        this.leaderBoardScreen = new Scene(leaderBoardCtrl.getValue());

        this.waitingRoomCtrl = waitingRoomCtrl.getKey();
        this.waitingRoom = new Scene(waitingRoomCtrl.getValue());

        showSplashScreen();
        primaryStage.show();
    }

    //This function is for showing the SplashScreen
    public void showSplashScreen() {
//        System.out.println("Xd 2");
        primaryStage.setTitle("Splash Screen");
        splashScreenCtrl.setSplashScreen();
        splashScreenScene.getStylesheets().add("client.styles/SplashScreenStyle.css");
        primaryStage.setScene(splashScreenScene);
//        usernameInput.setText(splashScreenCtrl.getUsername());
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
        if (round > ROUNDS) {
            questionCtrl.resetPoints();
            showLeaderBoard();
        } else {
            Question question = splashScreenCtrl.getRandomQuestion();
            System.out.println("size" + question.activityList.size());
            switch (question.activityList.size()) {
                case (1): {
                    oneActivityQuestion(question);
                    break;
                }
                case (2): {
                    twoActivityQuestion(question);
                    break;
                }
                case (3): {
                    threeActivityQuestion(question);
                    break;
                }
                default: {
                    threeActivityQuestion(question);
                    break;
                }
            }
        }
    }

    //Setup for a question with three activities
    public void oneActivityQuestion(Question question) {
        questionCtrl.startOneActivityQuestion(question);
        primaryStage.setTitle("Game screen - 1 activity question");
        primaryStage.setScene(questionScreen);
        questionCtrl.setOneActivity();
        questionScreen.getStylesheets().add("client.styles/QuestionScreenStyles.css");
        checkJokers(questionCtrl);
    }

    //Setup for a question with three activities
    public void twoActivityQuestion(Question question) {
        questionCtrl.startTwoActivityQuestion(question);
        primaryStage.setTitle("Game screen - 2 activities question");
        primaryStage.setScene(questionScreen);
        questionCtrl.setTwoActivities();
        questionScreen.getStylesheets().add("client.styles/QuestionScreenStyles.css");
        checkJokers(questionCtrl);
    }

    //Setup for a question with three activities
    public void threeActivityQuestion(Question question) {
        questionCtrl.startThreeActivityQuestion(question);
        primaryStage.setTitle("Game screen - 3 activities question");
        primaryStage.setScene(questionScreen);
        questionCtrl.setThreeActivities();
        questionScreen.getStylesheets().add("client.styles/QuestionScreenStyles.css");
        checkJokers(questionCtrl);
    }

    /*
    Player joining the current lobby of the multi player game
     */
    public void joinCurrentLobby() {
        primaryStage.setTitle("Waiting Room");
        primaryStage.setScene(waitingRoom);
    }

    public void showWaitingRoomScreen() {
        primaryStage.setTitle("Waiting Room");
        waitingRoom.getStylesheets().add("client.styles/WaitingRoomStyle.css");
        primaryStage.setScene(waitingRoom);
    }

    /*
    This function gets called whenever a player receives a question from the server
    in multiplayer mode. The question is printed to the terminal for testing. The scene is set
    to the question screen and on the question controller, the setup function is called with
    the question, which will set up the question properly
     */
    public void startMultiPlayerQuestion(Question question) {
        System.out.println("MADE IT");
        System.out.println(question.toString());
        primaryStage.setTitle("Question");
        primaryStage.setScene(questionScreen);
        questionScreen.getStylesheets().add("client.styles/QuestionScreenStyles.css");
        questionCtrl.setUpMultiPlayerQuestion(question);
    }


    //Function for storing the user and their points in the database and
    //loading the leaderboard scene
    public void showLeaderBoard() {
        questionCtrl.resetPoints();
        questionCtrl.resetJokers();
        leaderBoardCtrl.storePoints();
        leaderBoardCtrl.setLeaderBoard();
        leaderBoardCtrl.setList();
        leaderBoardScreen.getStylesheets().add("client.styles/LeaderBoardScreenStyles.css");
        primaryStage.setScene(leaderBoardScreen);
    }

    public void checkJokers(QuestionCtrl questionCtrl) {
        if (firstJokerUsed) questionCtrl.jokerOne.setDisable(true);
        if (secondJokerUsed) questionCtrl.jokerTwo.setDisable(true);
    }
}