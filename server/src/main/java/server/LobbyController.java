package server;

import commons.Lobby;
import commons.Question;
import commons.WebsocketMessage;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import server.api.QuestionController;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@Controller
public class LobbyController {

    /*
    This class is the main controller of the multiplayer game. A Lobby holds a list of players in the lobby.
    The current
     */

    private List<Lobby> lobbyList = new ArrayList<>();
    public Lobby openLobby = new Lobby(0);
    public int currentLobbyNumber = 0;

    private QuestionController questionController;
    private SimpMessagingTemplate msgs;

    public LobbyController(QuestionController questionController, SimpMessagingTemplate msgs) {
        this.questionController = questionController;
        this.msgs = msgs;
    }

    public void addLobby(Lobby lobby) {
        this.lobbyList.add(lobby);
    }

    public Lobby getOpenLobby() {
        return this.openLobby;
    }

    /*
    Returns every lobby. Gets called by a rest controller
     */
    public List<Lobby> getAllLobbies() {
        List<Lobby> lobbyList = new ArrayList<>();
        for(Lobby lobby : this.lobbyList)
            lobbyList.add(lobby);
        lobbyList.add(openLobby);
        return lobbyList;
    }

    /*
    This function gets called whenever one player in the lobby clicks on PLAY.
    The current lobby gets added to the lobby list and a new current lobby is created.
    The functions cals instantiateMultiGame in which a multiplayer game gets started.
     */
    public void startGame() {
        System.out.println("START GAME");
        Lobby playingLobby = openLobby;
        this.lobbyList.add(openLobby);
        currentLobbyNumber++;
        openLobby = new Lobby(currentLobbyNumber);
        instantiateMultiGame(playingLobby);
    }

    /*
    In this function a multiplayer game gets started. The functions cals itself after 15 seconds.
    These 15 seconds are 10 seconds of answering and 5 seconds to see the correct answer.
     */
    public void instantiateMultiGame(Lobby lobby) {
        String destination = "/topic/question" + String.valueOf(lobby.lobbyNumber);
        int currentRound = lobby.roundNumber++;

        int[] secondsPassed = {15};
        Timer myTimer = new Timer();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                secondsPassed[0]--;
                if(secondsPassed[0] == 0) {
                    if(currentRound == 3) {
                        showLeaderBoard(destination, lobby);
                    } else if (currentRound == 6) {
                        myTimer.cancel();
                        showLeaderBoard(destination, lobby);
                    } else {
                        instantiateMultiGame(lobby);
                    }
                }
            }
        };

        generateAndSendQuestion(destination);

        myTimer.scheduleAtFixedRate(task, 1000,1000);
    }

    /**
     * This functions sends a websocket message to the client
     * saying that it is time for the leaderboard
     *
     * @param destination
     */
    public void showLeaderBoard(String destination, Lobby lobby) {
        WebsocketMessage websocketMessage = new WebsocketMessage("LEADERBOARD");
        websocketMessage.setUserList(lobby.getUserList());

        msgs.convertAndSend(destination, websocketMessage);

        if(!(lobby.roundNumber == 6)) {
            int[] secondsPassed = {15};
            Timer myTimer = new Timer();

            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    secondsPassed[0]--;
                    if (secondsPassed[0] == 0) {
                        instantiateMultiGame(lobby);
                    }
                }

            };
            myTimer.scheduleAtFixedRate(task, 1000,1000);
        }
    }

    /**
     * In this function a question gets generated and send to the given destination.
     */
    public void generateAndSendQuestion(String destination) {
        Question question = questionController.getActivities();
        WebsocketMessage websocketMessage = new WebsocketMessage("QUESTION");
        websocketMessage.setQuestion(question);
        System.out.println("SENDING WEBSOCKET MESSAGE to " + destination + " message is " + websocketMessage);
        msgs.convertAndSend(destination, websocketMessage);
    }


}
