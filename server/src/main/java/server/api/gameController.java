package server.api;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.LobbyController;
import server.database.QuestionRepository;
import server.Main;

@RestController
@RequestMapping("/api/game")
public class gameController {

    private final LobbyController lobbyController;
    private final QuestionRepository repo;
    private SimpMessagingTemplate msgs;

    public gameController(Main mainGame, QuestionRepository repo, SimpMessagingTemplate msgs, LobbyController lobbyController) {
        this.repo = repo;
        this.msgs = msgs;
        this.lobbyController = lobbyController;
    }

    /**
    This mapping is for starting the game. If someone presses the join button on the waiting room,
    this mapping gets called and starts a multi player game
     */
    @GetMapping("/start")
    public void startGame() {
        System.out.println("SERVER RECEIVED GET ON /api/questions/start");
        lobbyController.startGame();
    }

    /**
     * When a player joins the lobby from the splashscreen, he/she needs to be added to question websocket
     * of that lobby. For this the player needs the current lobby number which he/she gets from this request.
     *
     * @return  The number of the current open lobby
     */
    @GetMapping("/currentOpenLobby")
    public int currentOpenLobby() {
        return lobbyController.currentLobbyNumber;
    }

}
