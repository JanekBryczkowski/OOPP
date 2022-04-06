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

    /**
     * This class is the main controller of the multiplayer game. A Lobby holds a list of players in the lobby.
     * The current
     */
    public List<Lobby> lobbyList = new ArrayList<>();

    public Lobby openLobby = new Lobby(0);

    public int currentLobbyNumber = 0;

    private QuestionController questionController;

    private SimpMessagingTemplate msgs;

    /**
     * Lobby controller constructor.
     *
     * @param questionController - question controller class.
     * @param msgs               - template object.
     */
    public LobbyController(QuestionController questionController, SimpMessagingTemplate msgs) {
        this.questionController = questionController;
        this.msgs = msgs;
    }

    /**
     * Method for adding the lobby to the lobby list.
     *
     * @param lobby - lobby to be added.
     */
    public void addLobby(Lobby lobby) {
        this.lobbyList.add(lobby);
    }

    /**
     * Method for getting the open lobby.
     *
     * @return - returns the currently open lobby.
     */
    public Lobby getOpenLobby() {
        return this.openLobby;
    }

    /**
     * Method for getting all of the lobbies.
     *
     * @return - returns all of the lobbies.
     */
    /*
    Returns every lobby. Gets called by a rest controller
     */
    public List<Lobby> getAllLobbies() {
        List<Lobby> lobbyList = new ArrayList<>();
        for (Lobby lobby : this.lobbyList)
            lobbyList.add(lobby);
        lobbyList.add(openLobby);
        return lobbyList;
    }

    /**
     * This function gets called whenever one player in the lobby clicks on PLAY.
     * The current lobby gets added to the lobby list and a new current lobby is created.
     * The function calls instantiateMultiGame in which a multiplayer game gets started.
     */
    public void startGame() {
        Lobby playingLobby = openLobby;
        this.lobbyList.add(openLobby);
        currentLobbyNumber++;
        openLobby = new Lobby(currentLobbyNumber);
        instantiateMultiGame(playingLobby);
    }


    /**
     * In this function a multiplayer game gets started. The functions cals itself after 15 seconds.
     * These 15 seconds are 10 seconds of answering and 5 seconds to see the correct answer.
     *
     * @param lobby - lobby of the current game.
     */
    public void instantiateMultiGame(Lobby lobby) {
        String destination = "/topic/question" + lobby.lobbyNumber;
        final int[] currentRound = {lobby.roundNumber++};

        int[] secondsPassed = {15};
        Timer myTimer = new Timer();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                secondsPassed[0]--;
                if (secondsPassed[0] == 0) {
                    if (currentRound[0] == 9) {
                        currentRound[0]++;
                        showLeaderBoard(destination, lobby);
                    } else if (currentRound[0] == 19) {
                        showLeaderBoard(destination, lobby);
                        lobbyList.remove(lobby);
                    } else {
                        instantiateMultiGame(lobby);
                    }
                }
            }
        };

        generateAndSendQuestion(destination);

        myTimer.scheduleAtFixedRate(task, 1000, 1000);
        System.out.println(lobby.roundNumber);
        System.out.println(currentRound[0]);
    }

    /**
     * This functions sends a websocket message to the client
     * saying that it is time for the leaderboard
     *
     * @param destination - destination of the websocket message.
     */
    public void showLeaderBoard(String destination, Lobby lobby) {
        WebsocketMessage websocketMessage = new WebsocketMessage("LEADERBOARD");
        websocketMessage.setUserList(lobby.getUserList());

        msgs.convertAndSend(destination, websocketMessage);

        if(lobby.roundNumber == 10){

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
            myTimer.scheduleAtFixedRate(task, 1000, 1000);
        }

    }



    /**
     * In this function a question gets generated and sent to the given destination.
     */
    public void generateAndSendQuestion(String destination) {
        Question question = questionController.getActivities();
        WebsocketMessage websocketMessage = new WebsocketMessage("QUESTION");
        websocketMessage.setQuestion(question);
        System.out.println("SENDING WEBSOCKET MESSAGE to " + destination + " message is " + websocketMessage);
        msgs.convertAndSend(destination, websocketMessage);
    }
}
