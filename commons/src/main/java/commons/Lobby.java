package commons;

import java.util.ArrayList;
import java.util.List;

public class Lobby {

    private List<User> userList;
    public int lobbyNumber;
    public int roundNumber = 0;

    public Lobby(int lobbyNumber) {
        this.userList = new ArrayList<>();
        this.lobbyNumber = lobbyNumber;
    }

    public User addUser(User user) {
        this.userList.add(user);
        return user;
    }

    public List<User> getUserList() {
        return this.userList;
    }

}
