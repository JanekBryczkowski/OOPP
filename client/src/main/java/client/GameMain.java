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
import client.utils.ServerUtils;
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


//        var overview = FXML.load(QuoteOverviewCtrl.class, "client", "scenes", "QuoteOverview.fxml");
//        var add = FXML.load(AddQuoteCtrl.class, "client", "scenes", "AddQuote.fxml");
//        FXMLLoader loader = new FXMLLoader(new URL("C:\\TU Delft\\OOPP\\repository-template\\client\\src\\main\\resources\\client\\scenes\\QuestionScreen.fxml"));
//        loader.setController(new GameCtrl());
//        Pane mainPane = loader.load();
        var splash = FXML.load(SplashScreenCtrl.class, "client", "scenes", "SplashScreen.fxml");
        var gameThreeScreen = FXML.load(QuestionCtrl.class, "client", "scenes", "QuestionScreen.fxml");
        var leaderBoard = FXML.load(LeaderBoardCtrl.class, "client", "scenes", "LeaderBoardScreen.fxml");
        var waitingRoom = FXML.load(WaitingRoomCtrl.class, "client", "scenes", "WaitingRoom.fxml");
        var admin = FXML.load(AdminCtrl.class, "client", "scenes", "AdminScreen.fxml");


//        var gameScreen = FXML.load(getClass().getResource("SplashScreen.fxml"))

//        FXMLLoader loader = new FXMLLoader();
//        loader.setLocation(getClass().getResource("QuestionScreen.fxml"));
//        Parent root = loader.load();
//        loader.getController().setPrimaryStage(primaryStage);


//        FXMLLoader loader = new FXMLLoader(QuestionThreeCtrl.class, "client", "scenes", "QuestionScreen.fxml");
//        loader.setCharset(new MainController(path));
//        Pane mainPane = loader.load();
//
//        MainController mainController = new MainController(path);
//        Pane mainPane = FXMLLoader.load(getClass().getResource("main.fxml"));
//
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
//        loader.setController(new MainController(path));
//        Pane mainPane = loader.load();
//
//        Window.setMainController(mainController);
//        Window.swap(path+"content.fxml");


        var gameCtrl = INJECTOR.getInstance(GameCtrl.class);
        gameCtrl.initialize(primaryStage, splash, gameThreeScreen, leaderBoard, waitingRoom, admin);

//        primaryStage.setOnCloseRequest(e -> {
//            waitingRoom.getKey().stop();
//        });

    }
}