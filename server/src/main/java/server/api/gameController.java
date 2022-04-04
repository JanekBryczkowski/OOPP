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

    /**
     * Constructor for the game controller class
     *
     * @param mainGame - game controller class
     * @param repo - repository.
     * @param msgs - messaging template.
     * @param lobbyController - lobby controller provided.
     */
    public gameController(Main mainGame, QuestionRepository repo, SimpMessagingTemplate msgs, LobbyController lobbyController) {
        this.repo = repo;
        this.msgs = msgs;
        this.lobbyController = lobbyController;
    }

    /**
     * This mapping is for starting the game. If someone presses the join button in the Waiting Room,
     * this mapping gets called.
     */
    @GetMapping("/start")
    public void startGame() {
        System.out.println("SERVER RECEIVED GET ON /api/questions/start");
        lobbyController.startGame();
    }

    /**
     * When a player joins the lobby, in multiplayer mode, from the Splash Screen, they need to be added
     * to the question web socket of that lobby. For this, the player needs the current lobby number which
     * they get from this request.
     *
     * @return the lobby number associated with the current open lobby.
     */
    @GetMapping("/currentOpenLobby")
    public int currentOpenLobby() {
        return lobbyController.currentLobbyNumber;
    }
}
