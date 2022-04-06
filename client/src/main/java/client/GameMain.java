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

    /**
     * Main method that is responsible for starting the client side application.
     *
     * @param args - arguments for the main functions.
     * @throws URISyntaxException - URI exception.
     * @throws IOException - Input, output exception.
     */
    public static void main(String[] args) throws URISyntaxException, IOException {
        launch();
    }

    /**
     * Start method that is necessary for the initialization of the FXML modules, etc.
     *
     * @param primaryStage - main stage that is used throughout the application.
     * @throws IOException - Input, output exception.
     */
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