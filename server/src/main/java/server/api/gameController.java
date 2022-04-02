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


    @GetMapping("/start")
    public void startGame() {
        System.out.println("SERVER RECEIVED GET ON /api/questions/start");
        lobbyController.startGame();
    }

    @GetMapping("/currentOpenLobby")
    public int currentOpenLobby() {
        return lobbyController.currentLobbyNumber;
    }

}
