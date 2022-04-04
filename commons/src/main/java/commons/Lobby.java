package commons;

import java.util.ArrayList;
import java.util.List;

public class Lobby {

    private List<User> userList;

    public int lobbyNumber;

    public int roundNumber = 0;

    /**
     * Lobby constructor.
     *
     * @param lobbyNumber - input the lobby number.
     */
    public Lobby(int lobbyNumber) {
        this.userList = new ArrayList<>();
        this.lobbyNumber = lobbyNumber;
    }

    /**
     * Method adding the user to the user list of the lobby.
     *
     * @param user - user to be added.
     * @return - returns a user if it's added.
     */
    public User addUser(User user) {
        this.userList.add(user);
        return user;
    }

    /**
     * Method getting the list of users in the current lobby.
     *
     * @return - returns a list of users in the current lobby.
     */
    public List<User> getUserList() {
        return this.userList;
    }
}
