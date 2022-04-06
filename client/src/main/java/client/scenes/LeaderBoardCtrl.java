package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import jakarta.ws.rs.WebApplicationException;

import commons.Score;
import commons.User;

import java.util.*;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.util.Duration;

public class LeaderBoardCtrl {

    private final ServerUtils server;
    private final GameCtrl gameCtrl;

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

    @FXML
    private Button waitingRoom;

    @FXML
    private Button splash;

    @FXML
    private Button backButton;

    @FXML
    private ProgressBar nope;

    Timeline bar;

    /**
     * List containing colors for the timer in the half-time leaderboard.
     */
    List<Color> colorsForClockMultiPlayer = Arrays.asList(new Color(0, 0.3, 0.15, 1),
            new Color(0.28, 0.75, 0.33, 1),
            new Color(0.57, 0.94, 0.53, 1),
            new Color(1, 0.95, 0.14, 1),
            new Color(1, 0.74, 0.5, 1),
            new Color(0.97, 0.43, 0.07, 1),
            new Color(0.91, 0.23, 0.08, 1),
            new Color(0.54, 0.06, 0.05, 1),
            new Color(0.39, 0.02, 0.02, 1),
            new Color(0, 0, 0, 1),
            new Color(0, 0, 0, 1));

    List<Score> topThreeList;
    List<Score> scoreList;
    ObservableList<Score> scores;
    List<String> colors = Arrays.asList("#95B826", "#2E380C", "#627819", "#ADC364", "#819E21", "#8B9A46", "#064635", "#116530", "#116530", "#1E5128", "#3E7C17");

    /**
     * Constructor for the Leader Board.
     *
     * @param server - server provided for the current game.
     * @param mainCtrl - game controller of the entire game for single or multiplayer mode.
     */
    @Inject
    public LeaderBoardCtrl(ServerUtils server, GameCtrl mainCtrl) {
        this.server = server;
        this.gameCtrl = mainCtrl;
    }

    /**
     * This function is called when the Leader Board scene is going to show.
     * The usernames along with their scores will be stored in the database for solo player.
     * Surrounded by a try catch block in case an exception is raised during this process.
     * @param userList
     */
    public void storePoints(List<User> userList) {
        try {
            if (gameCtrl.getMode() == 0) server.addScore(new Score(gameCtrl.username, gameCtrl.points));
            else {
                for (User user : userList) {
                    if (user.getUsername().equals(gameCtrl.username)) {
                        user.setScore(gameCtrl.points);
                    }
                }
            }
        } catch (WebApplicationException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }
    }

    /**
     * This function will set up the top three scores in the Leader Board scene.
     * It will retrieve all the Scores from the database and check the length of this List.
     * Depending on the size of the List we can either set up the first player, the first two players or
     * the first three players.
     * @param userList
     */
    public void setLeaderBoard(List<User> userList) {
        scoreList = new ArrayList<>();
        topThreeList = new ArrayList<>();
        if (gameCtrl.getMode() == 0) {
            scores = FXCollections.observableArrayList();
            topThreeList.addAll(server.getTopScores());
        } else if (gameCtrl.getMode() == 1) {
            topThreeList.addAll(getThreeMultiplayer(userList));
        }

        if (topThreeList.size() == 1) {
            firstName.setText(topThreeList.get(0).getUsername());
            firstScore.setText(String.valueOf(topThreeList.get(0).getScore()));
            secondName.setText("");
            secondScore.setText("");
            thirdName.setText("");
            thirdScore.setText("");
        } else if (topThreeList.size() == 2) {
            firstName.setText(topThreeList.get(0).getUsername());
            firstScore.setText(String.valueOf(topThreeList.get(0).getScore()));
            secondName.setText(topThreeList.get(1).getUsername());
            secondScore.setText(String.valueOf(topThreeList.get(1).score));
            thirdName.setText("");
            thirdScore.setText("");
        } else if (topThreeList.size() >= 3) {
            firstName.setText(topThreeList.get(0).getUsername());
            firstScore.setText(String.valueOf(topThreeList.get(0).getScore()));
            secondName.setText(topThreeList.get(1).getUsername());
            secondScore.setText(String.valueOf(topThreeList.get(1).score));
            thirdName.setText(topThreeList.get(2).getUsername());
            thirdScore.setText(String.valueOf(topThreeList.get(2).score));
        }
    }

    /**
     * Creating the list for the top three users in multiplayer mode. Checking the amount
     * of players in the current lobby to check if there is only top two or top three.
     *
     * @param userList a list of all the users in the current lobby
     * @return a list of the top three scores.
     */
    public ArrayList<Score> getThreeMultiplayer(List<User> userList) {
        System.out.println("The userlist gotten is" + userList.toString());
        User first = userList.get(0);
        User second = null;
        User third = null;
        if (userList.size() >= 2) {
            second = userList.get(1);
            if(userList.size() > 2)
                third = userList.get(2);

            for (User user : userList) {
                if (user.getScore() > first.getScore()) {
                    first = user;
                } else if (user.getScore() > second.getScore() && (user.getScore() <= first.getScore() && !(user.username.equals(first.username) ))) {
                    second = user;
                } else if (third != null && user.getScore() > third.getScore() && (user.getScore() <= second.getScore() && !(user.username.equals(second.username)))) {
                    third = user;
                }
            }
        }
        ArrayList<Score> topThreeList = new ArrayList<>();
        topThreeList.add(new Score(first.username, first.score));
        if (second != null)
            topThreeList.add(new Score(second.username, second.score));
        if (third != null)
            topThreeList.add(new Score(third.username, third.score));
        return topThreeList;
    }

    /**
     * This function will set the List of all the Scores in the database. It will also
     * order them by calling the sortList function.
     * If the game is in multiplayer, it will store the scores of the users in the current lobby,
     * if the game is in solo player, it will store the user's username and score in the database and
     * then fetch all the scores from the database to create the leader board.
     * @param userList
     */
    public void setList(List<User> userList) {
        if (gameCtrl.getMode() == 1) {
            for (User u : userList) {
                scoreList.add(new Score(u.getUsername(), u.getScore()));
            }
        } else {
            scores.addAll(server.getScores());
            scoreList.addAll(server.getScores());
        }
        sortList();
        uploadScoresIntoTheRanking(scoreList);
    }

    /**
     * In this function the scroll pane of the Leader Board screen is set up. We retrieve all the Scores
     * from the scoreList parameter and create a label for each one. All labels are added to the ranking list.
     * The user who is currently playing will see their own score in bold.
     *
     * @param scoreList - the list of scores that needs to be displayed in the scroll pane.
     */
    private void uploadScoresIntoTheRanking(List<Score> scoreList) {
        VBox vbox = new VBox();
        int highestScore = findHighestScore(scoreList);
        for (Score score : scoreList) {
            AnchorPane anchorPane = new AnchorPane();
            anchorPane.setMaxHeight(50);
            anchorPane.setMinHeight(50);
            anchorPane.setMinHeight(50);
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
                usernameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16; -fx-background-color: transparent;");
                scoreLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16; -fx-background-color: transparent;");
                anchorPane.setStyle("-fx-background-color: #D5DEB6");
            }
            AnchorPane bar = new AnchorPane();
            bar.setMinHeight(6);
            bar.setMaxHeight(6);
            bar.setPrefWidth(6);
            double ratio = (double) score.score / highestScore;
            if (gameCtrl.round < 5)
                ratio = (double) score.score / 2000;
            double finalWidth = ratio * 317;
            bar.setPrefWidth(finalWidth);
            bar.setMinWidth(finalWidth);
            bar.setMaxWidth(finalWidth);
            bar.setLayoutX(15);
            bar.setLayoutY(26.5);
            bar.setStyle("-fx-background-color: " + getRandomColor() + "; -fx-background-radius: 4;");
            anchorPane.getChildren().add(usernameLabel);
            anchorPane.getChildren().add(scoreLabel);
            anchorPane.getChildren().add(bar);
            vbox.getChildren().add(anchorPane);
        }
        leaderBoardScrollPane.setContent(vbox);
    }

    /**
     * Method responsible for finding the highest score in the list of scores provided.
     *
     * @param scoreList - input list containing scores to search for the highest one.
     * @return - returns the highest score in the list.
     */
    private int findHighestScore(List<Score> scoreList) {
        return scoreList.stream().mapToInt(score -> score.score).max().orElse(Integer.MIN_VALUE);
    }

    /**
     * Method responsible for getting random color from the list of colors.
     *
     * @return - string representation of the random color taken from the list of colors.
     */
    public String getRandomColor() {
        int random = (int) (Math.random() * colors.size() - 1);
        return colors.get(random);
    }

    /**
     * In Multiplayer the Half-time Leaderboard shows up after 10 questions, users will see their score, which
     * they achieved so far.
     */
    public void halfTimeLeaderBoard() {
        List<Score> scores = new ArrayList<>();
        for (User u : WaitingRoomCtrl.userList) {
            scores.add(new Score(u.getUsername(), u.getScore()));
        }
        uploadScoresIntoTheRanking(scores);
        waitingRoom.setVisible(false);
        waitingRoom.setManaged(false);
        splash.setVisible(false);
        splash.setManaged(false);
        backButton.setVisible(true);
        leaderBoardScrollPane.setMinHeight(629);
        leaderBoardScrollPane.setMaxHeight(629);
        leaderBoardScrollPane.setPrefHeight(629);
        nope.setVisible(true);
        playTimerMultiPlayer();
    }

    /**
     * Method that shows the correct timer for the half-time leaderboard.
     */
    private void playTimerMultiPlayer() {
        nope.setStyle("-fx-accent: black;");
        double[] timer = {1};
        bar = new Timeline(new KeyFrame(Duration.millis(10), ev -> {
            timer[0] = timer[0] - 0.001;
            nope.setProgress(timer[0]);
            nope.setStyle("-fx-accent: " + translateColor(colorsForClockMultiPlayer.get(0)));
            if (timer[0] < 0.9) {
                nope.setStyle("-fx-accent: " + translateColor(colorsForClockMultiPlayer.get(1)));
            }
            if (timer[0] < 0.8) {
                nope.setStyle("-fx-accent: " + translateColor(colorsForClockMultiPlayer.get(2)));
            }
            if (timer[0] < 0.7) {
                nope.setStyle("-fx-accent: " + translateColor(colorsForClockMultiPlayer.get(3)));
            }
            if (timer[0] < 0.6) {
                nope.setStyle("-fx-accent: " + translateColor(colorsForClockMultiPlayer.get(4)));
            }
            if (timer[0] < 0.5) {
                nope.setStyle("-fx-accent: " + translateColor(colorsForClockMultiPlayer.get(5)));
            }
            if (timer[0] < 0.4) {
                nope.setStyle("-fx-accent: " + translateColor(colorsForClockMultiPlayer.get(6)));
            }
            if (timer[0] < 0.3) {
                nope.setStyle("-fx-accent: " + translateColor(colorsForClockMultiPlayer.get(7)));
            }
            if (timer[0] < 0.2) {
                nope.setStyle("-fx-accent: " + translateColor(colorsForClockMultiPlayer.get(8)));
            }
            if (timer[0] < 0.1) {
                nope.setStyle("-fx-accent: " + translateColor(colorsForClockMultiPlayer.get(9)));
            }
            if (timer[0] == 0.001) {
                bar.stop();
            }
        }));
        bar.setCycleCount(1000);
        bar.play();
    }

    /**
     * Method that's responsible for converting java color class into the string representation of the color
     * in the hexadecimal system.
     *
     * @param color - provided color with java class color.
     * @return - hexadecimal string number representation.
     */
    public String translateColor(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }

    /**
     * This function sets the Leaderboard back to the original.
     */
    public void endLeaderBoard() {
        nope.setVisible(false);
        splash.setVisible(true);
        splash.setManaged(true);
        backButton.setVisible(false);
        if (SplashScreenCtrl.mode == 0) {
            leaderBoardScrollPane.setMinHeight(535);
            leaderBoardScrollPane.setMaxHeight(535);
            leaderBoardScrollPane.setPrefHeight(535);
            waitingRoom.setVisible(false);
            waitingRoom.setManaged(false);
        } else {
            leaderBoardScrollPane.setMinHeight(417);
            leaderBoardScrollPane.setMaxHeight(417);
            leaderBoardScrollPane.setPrefHeight(417);
            waitingRoom.setVisible(true);
            waitingRoom.setManaged(true);
        }
    }


    /**
     * If the button in the Leaderboard screen for going back to the Splash Screen is pressed, the user will be
     * directed back to the Splash Screen.
     */
    public void backToSplash() {
        if (SplashScreenCtrl.mode == 1) {
            gameCtrl.subscription.unsubscribe();
            server.removeUser(new User(gameCtrl.username, gameCtrl.points, gameCtrl.joinedLobby));
        }
        gameCtrl.points = 0;
        gameCtrl.round = 1;
        gameCtrl.firstJokerSinglePlayerUsed = false;
        gameCtrl.secondJokerSinglePlayerUsed = false;
        gameCtrl.firstJokerMultiPlayerUsed = false;
        gameCtrl.secondJokerMultiPlayerUsed = false;
        gameCtrl.thirdJokerMultiPlayerUsed = false;
        gameCtrl.showSplashScreen();
    }

    /**
     * On the solo player Leaderboard screen, the 'back to the Waiting Room' button is not an option anymore.
     */
    public void backToWaitingRoomButton() {
        if (SplashScreenCtrl.mode == 0) {
            waitingRoom.setVisible(false);
            waitingRoom.setManaged(false);
            splash.setTranslateY(94);
        } else {
            waitingRoom.setVisible(true);
            waitingRoom.setManaged(true);
        }
    }

    /**
     * If the button in the Leaderboard screen for going back to the Waiting Room Screen is pressed, the user will be
     * directed back to the Waiting Room Screen. The user will be added to the current open lobby, and they will be added to
     * the socket for that client.
     */
    public void backToWaitingRoom() {
        server.removeUser(new User(gameCtrl.username, gameCtrl.points, gameCtrl.joinedLobby));
        gameCtrl.subscription.unsubscribe();
        gameCtrl.points = 0;
        gameCtrl.round = 1;
        gameCtrl.firstJokerSinglePlayerUsed = false;
        gameCtrl.secondJokerSinglePlayerUsed = false;
        gameCtrl.firstJokerMultiPlayerUsed = false;
        gameCtrl.secondJokerMultiPlayerUsed = false;
        gameCtrl.thirdJokerMultiPlayerUsed = false;
        gameCtrl.showWaitingRoomScreen();
    }

    /**
     * This function will compare the Scores using the compare method and sort the List of Scores according
     * to that.
     */
    public void sortList() {
        scoreList.sort(new Comparator<Score>() {
            public int compare(Score score1, Score score2) {
                return score2.getScore() - score1.getScore();
            }
        });
    }
}
