package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LobbyTest {

    @Test
    public void ConstructorTest() {
        Lobby lobby = new Lobby(1);
        assertNotNull(lobby);
    }

    @Test
    public void lobbyNumberTest() {
        Lobby lobby = new Lobby(1);
        assertEquals(1, lobby.lobbyNumber);
    }

    @Test
    public void getUserListTest() {
        Lobby lobby = new Lobby(1);
        assertNotNull(lobby.getUserList());
    }

    @Test
    public void addUserTest() {
        Lobby lobby = new Lobby(1);
        User user = new User("username", 1, 1);
        lobby.addUser(user);
        assertTrue(lobby.getUserList().contains(user));
    }

}
