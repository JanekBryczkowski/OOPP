package server.api;

import commons.User;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import commons.Lobby;
import server.LobbyController;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final LobbyController lobbyController;

    /**
     * Constructor for lobbyController.
     *
     * @param lobbyController to be instantiated
     */
    public UserController(LobbyController lobbyController) {
        this.lobbyController = lobbyController;
    }

    /**
     * Mapping to check that the username input by the user is not already taken by another
     * user in the same lobby as them.
     *
     * @param username Username to be checked against
     * @return Boolean representing if the username is taken
     */
    @GetMapping("/isValidUsername/{username}")
    public boolean isValidUsername(@PathVariable("username") String username) {
        List<User> userList = lobbyController.openLobby.getUserList();
        for(User user : userList)
            if(user.username.toLowerCase(Locale.ROOT).equals(username))
                return false;
        return true;
    }

    /**
     * Post mapping to add a User to the User List of the open lobby.
     *
     * @param user to add to the open lobby User List
     * @return user
     */
    @PostMapping(path = { "", "/" })
    public User postUserToOpenLobby(@RequestBody User user) {
        return lobbyController.getOpenLobby().addUser(user);
    }

    @PostMapping("/updateScore")
    public void updateScore(@RequestBody User user) {
        for(User u : lobbyController.getOpenLobby().getUserList()) {
            if(u.getUsername().equals(user.getUsername())) {
                u.setScore(user.getScore());
            }
        }
    }

    /**
     * Get mapping to remove a User from the current open lobby User List when
     * they unsubscribe from the web socket question channel.
     *
     * @param username used to identify User that should be removed
     */
    @DeleteMapping("/removePlayer/{username}/{lobbyNumber}")
    public void removeUser(@PathVariable("username") String username, @PathVariable int lobbyNumber) {
        Lobby lobby = (Lobby) lobbyController.getAllLobbies().
                stream().filter(x -> x.lobbyNumber==lobbyNumber);
        lobby.getUserList().removeIf(user -> user.getUsername().equals(username));
    }

    /**
     * Get mapping that returns a List of al the Users in the currently open lobby.
     *
     * @return List of User
     */
    @GetMapping("/currentLobby")
    public List<User> getUsersOfOpenLobby() {
        return (List<User>) lobbyController.getOpenLobby().getUserList();
    }

    /**
     * Gets the list of users associated to this lobby number.
     * @param lobbyNumber The Lobby we are looking for
     * @return The list of users playing in this lobby
     */
    @GetMapping("/lobby/{lobbyNumber}")
    public List<User> getUsersOfLobby(@PathVariable int lobbyNumber) {
        for(Lobby lobby : lobbyController.getAllLobbies()) {
            if(lobby.lobbyNumber == lobbyNumber) {
                return lobby.getUserList();
            }
        }
        return null;
    }
    /**
     * Get mapping used to fetch all the lobbies that have been created.
     *
     * @return List of Lobby containing all lobbies
     */
    @GetMapping("/allLobies")
    public List<Lobby> getAllLobbies() {
        return (List<Lobby>) lobbyController.getAllLobbies();
    }

    @MessageMapping("/users")
    @SendTo("/topic/users")
    public User addUser(User user) {
        //postUserToOpenLobby(user);
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

}
