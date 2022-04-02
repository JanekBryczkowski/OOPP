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

    public UserController(LobbyController lobbyController, SimpMessagingTemplate msgs) {
        this.lobbyController = lobbyController;
        this.msgs = msgs;
    }

    @GetMapping("/isValidUsername/{username}")
    public boolean isValidUsername(@PathVariable("username") String username) {
        List<User> userList = lobbyController.openLobby.getUserList();
        for(User user : userList)
            if(user.username.toLowerCase(Locale.ROOT).equals(username))
                return false;
        return true;
    }

    @PostMapping(path = { "", "/" })
    public User postUserToOpenLobby(@RequestBody User user) {
            lobbyController.getOpenLobby().addUser(user);
            String destination = "/topic/question" + String.valueOf(lobbyController.currentLobbyNumber);
            WebsocketMessage websocketMessage = new WebsocketMessage("NEWPLAYER");
            msgs.convertAndSend(destination, websocketMessage);
            return user;
    }

    @DeleteMapping("/removePlayer/{username}")
    public void removeUser(@PathVariable("username") String username) {
        List<User> userList = lobbyController.getOpenLobby().getUserList();
        for (User user : userList) {
            if (user.getUsername().equals(username)) {
                userList.remove(user);
            }
        }
    }

    @GetMapping("/currentLobby")
    public List<User> getUsersOfOpenLobby() {
        return (List<User>) lobbyController.getOpenLobby().getUserList();
    }

    @GetMapping("/allLobies")
    public List<Lobby> getAllLobbies() {
        return (List<Lobby>) lobbyController.getAllLobbies();
    }

}
