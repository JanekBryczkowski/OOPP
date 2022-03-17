package server;

import commons.User;

import java.util.ArrayList;
import java.util.List;

public class Lobby {

    private List<User> userList;

    public Lobby() {
        this.userList = new ArrayList<>();
    }

    public User addUser(User user) {
        this.userList.add(user);
        return user;
    }

    public List<User> getUserList() {
        return this.userList;
    }


}
