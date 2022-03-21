package server.api;

import commons.User;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import commons.Lobby;
import server.LobbyController;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final LobbyController lobbyController;

    public UserController(LobbyController lobbyController) {
        this.lobbyController = lobbyController;
    }


    @PostMapping(path = { "", "/" })
    public User postUserToOpenLobby(@RequestBody User user) {
//            listeners.forEach((k,l) -> l.accept(user));
            return lobbyController.getOpenLobby().addUser(user);
    }

    @GetMapping("/currentLobby")
    public List<User> getUsersOfOpenLobby() {
        return (List<User>) lobbyController.getOpenLobby().getUserList();
    }

    @GetMapping("/allLobies")
    public List<Lobby> getAllLobbies() {
        return (List<Lobby>) lobbyController.getAllLobbies();
    }

    @MessageMapping("/users")
    @SendTo("/topic/users")
    public User addUser(User user) {
//        postUserToOpenLobby(user);
        return user;
    }




//    private Map<Object, Consumer<User>> listeners = new HashMap<>();
//
//    @GetMapping("/updates")
//    public DeferredResult<ResponseEntity<User>> getUpdates() {
//        var noContent = ResponseEntity.status(HttpStatus.NO_CONTENT).build();
//        var res = new DeferredResult<ResponseEntity<User>>(5000L, noContent);
//
//        var key = new Object();
//        listeners.put(key, q -> {
//            res.setResult(ResponseEntity.ok(q));
//        });
//
//        res.onCompletion(() -> {
//            listeners.remove(key);
//        });
//        listeners.forEach((k,l) -> System.out.println(l.toString()));
//
//        return res;
//    }


//    @GetMapping("/")
//    public List<Question> getQuestions() {
//        return repo.findAll();
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<Question> getById(@PathVariable("id") long id) {
//        if (id < 0 || !repo.existsById(id)) {
//            return ResponseEntity.badRequest().build();
//        }
//        return ResponseEntity.ok(repo.getById(id));
//    }
//
//    private static boolean isNullOrEmpty(String s) {
//        return s == null || s.isEmpty();
//    }
//
//    @PostMapping(path = { "", "/" })
//    public ResponseEntity<Question> add(@RequestBody Question question) {
//
//        if (question==null || isNullOrEmpty(question.title)) {
//            return ResponseEntity.badRequest().build();
//        }
//
//        Question saved = repo.save(question);
//        return ResponseEntity.ok(saved);
//    }
//
//    @DeleteMapping("/{id}")
//    void deleteQuestion(@PathVariable long id) {
//        repo.deleteById(id);
//    }
}
