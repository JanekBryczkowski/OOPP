package server.api;

import commons.User;
import commons.WebsocketMessage;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import commons.Lobby;
import server.LobbyController;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final LobbyController lobbyController;

    private final SimpMessagingTemplate msgs;

    /**
     * Constructor for lobbyController.
     *
     * @param lobbyController to be instantiated
     */
    public UserController(LobbyController lobbyController, SimpMessagingTemplate msgs) {
        this.lobbyController = lobbyController;
        this.msgs = msgs;
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
        for (User user : userList)
            if (user.username.toLowerCase(Locale.ROOT).equals(username))
                return false;
        return true;
    }

    /**
     * Post mapping to add a User to the User List of the open lobby.
     *
     * @param user to add to the open lobby User List
     * @return user
     */
    @PostMapping(path = {"", "/"})
    public User postUserToOpenLobby(@RequestBody User user) {
        lobbyController.getOpenLobby().addUser(user);
        String destination = "/topic/question" + String.valueOf(lobbyController.currentLobbyNumber);
        WebsocketMessage websocketMessage = new WebsocketMessage("NEWPLAYER");
        msgs.convertAndSend(destination, websocketMessage);
        return user;
    }

    /**
     * Method for updating the score of the specific user.
     * @param user - user which we need to update the score for.
     */
    @PostMapping("/updateScore")
    public void updateScore(@RequestBody User user) {
        for (User u : lobbyController.lobbyList.get(user.getLobbyNumber()).getUserList()) {
            if (u.getUsername().equals(user.getUsername())) {
                u.setScore(user.getScore());
            }
        }
    }

    /**
     * Post mapping for deleting a user.
     *
     * @param user used to identify User that should be removed
     */
    @PostMapping("/removePlayer")
    public void removeUser(@RequestBody User user) {
        Lobby lobby = lobbyController.lobbyList.get(user.getLobbyNumber());
        for(User u : lobby.getUserList()) {
            if(u.getUsername().equals(user.username))
                lobby.getUserList().remove(u);
        }
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
     *
     * @param lobbyNumber The Lobby we are looking for
     * @return The list of users playing in this lobby
     */
    @GetMapping("/lobby/{lobbyNumber}")
    public List<User> getUsersOfLobby(@PathVariable int lobbyNumber) {
        for (Lobby lobby : lobbyController.getAllLobbies()) {
            if (lobby.lobbyNumber == lobbyNumber) {
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
}
