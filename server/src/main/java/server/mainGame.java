package server;

import commons.User;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import java.util.ArrayList;

@SpringBootApplication
@EntityScan(basePackages = { "commons", "server" })
public class mainGame {

    ArrayList<User> users;

    public mainGame() {
        this.users = new ArrayList<>();
    }

    public static String returnString() {
        return "KOEKOEK";
    }

    public User addUser(User user) {
        this.users.add(user);
        return user;
    }

    public ArrayList<User> getUsers() {
        return this.users;
    }

}
