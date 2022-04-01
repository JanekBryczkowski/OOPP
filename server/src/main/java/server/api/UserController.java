package server.api;

import commons.User;
import commons.WebsocketMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
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

    public UserController(LobbyController lobbyController, SimpMessagingTemplate msgs) {
        this.lobbyController = lobbyController;
        this.msgs = msgs;
    }

    /**
     * Mapping for checking if a username is valid and not already used.
     * Gets called whenever someone wants to join the current open lobby.
     *
     * @param username  The username that needs to be checked
     * @return          A boolean indicating if the username is valid or not
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
     * This function gets called whenever a user needs to be added to the current open lobby
     *
     * @param user  A user that need to be added to the current open lobby
     * @return      Returns the user that is added to the current open lobby
     */
    @PostMapping(path = { "", "/" })
    public User postUserToOpenLobby(@RequestBody User user) {
            lobbyController.getOpenLobby().addUser(user);
            String destination = "/topic/question" + String.valueOf(lobbyController.currentLobbyNumber);
            WebsocketMessage websocketMessage = new WebsocketMessage("NEWPLAYER");
            msgs.convertAndSend(destination, websocketMessage);
            return user;
    }

    /**
     * A mapping for removing a user from the current open lobby
     *
     * @param username  the username of the user that needs to be removed
     */
    @DeleteMapping("/removePlayer/{username}")
    public void removeUser(@PathVariable("username") String username) {
        List<User> userList = lobbyController.getOpenLobby().getUserList();
        for (User user : userList) {
            if (user.getUsername().equals(username)) {
                userList.remove(user);
            }
        }
    }

    /**
     * A mapping for getting a list of the users in the current open lobby
     *
     * @return  A list with the current users in the open lobby
     */
    @GetMapping("/currentLobby")
    public List<User> getUsersOfOpenLobby() {
        return (List<User>) lobbyController.getOpenLobby().getUserList();
    }

    /**
     * A mapping for returning the users in all lobbies of the application
     *
     * @return  A list with all the users currently in a lobby
     */
    @GetMapping("/allLobies")
    public List<Lobby> getAllLobbies() {
        return (List<Lobby>) lobbyController.getAllLobbies();
    }

}
