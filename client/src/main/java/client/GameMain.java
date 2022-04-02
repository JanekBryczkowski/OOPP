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
package client;

import static com.google.inject.Guice.createInjector;

import java.io.IOException;
import java.net.URISyntaxException;

import client.scenes.*;
import com.google.inject.Injector;

import javafx.application.Application;
import javafx.stage.Stage;


public class GameMain extends Application {

    private static final Injector INJECTOR = createInjector(new MyModule());
    private static final MyFXML FXML = new MyFXML(INJECTOR);

    public static void main(String[] args) throws URISyntaxException, IOException {
        System.out.println("Xd 1");
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        var splash = FXML.load(SplashScreenCtrl.class, "client", "scenes", "SplashScreen.fxml");
        var gameThreeScreen = FXML.load(QuestionCtrl.class, "client", "scenes", "QuestionScreen.fxml");
        var leaderBoard = FXML.load(LeaderBoardCtrl.class, "client", "scenes", "LeaderBoardScreen.fxml");
        var waitingRoom = FXML.load(WaitingRoomCtrl.class, "client", "scenes", "WaitingRoom.fxml");
        var admin = FXML.load(AdminCtrl.class, "client", "scenes", "AdminScreen.fxml");


        var gameCtrl = INJECTOR.getInstance(GameCtrl.class);
        gameCtrl.initialize(primaryStage, splash, gameThreeScreen, leaderBoard, waitingRoom, admin);

    }
}