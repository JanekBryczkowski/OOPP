package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserTest {

    @Test
    public void constructorTest() {
        User user = new User("abc", 123, 0);
        assertNotNull(user);
    }

    @Test
    public void usernameTest() {
        User user = new User("abc", 123, 0);
        assertEquals("abc", user.username);
    }

    @Test
    public void scoreTest() {
        User user = new User("abc", 123, 0);
        assertEquals(123, user.score);
    }

    @Test
    public void lobbyTest() {
        User user = new User("abc", 123, 2);
        assertEquals(2, user.lobbyNumber);
    }

    @Test
    public void getUsernameTest() {
        User user = new User("abc", 123, 0);
        assertEquals("abc", user.getUsername());
    }

    @Test
    public void getScoreTest() {
        User user = new User("abc", 123, 0);
        assertEquals(123, user.getScore());
    }

    @Test
    public void getLobbyNumberTest() {
        User user = new User("abc", 123, 3);
        assertEquals(3, user.getLobbyNumber());
    }

    @Test
    public void equalsHashCode() {
        User a = new User("abc", 123, 0);
        User b = new User("abc", 123, 0);
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void notEqualsHashCode() {
        User a = new User("abc", 123, 0);
        User b = new User("def", 123, 0);
        assertNotEquals(a, b);
        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void hasToString() {
        String actual = new User("a", 2, 0).toString();
        assertTrue(actual.contains(User.class.getSimpleName()));
        assertTrue(actual.contains("a"));
        assertTrue(actual.contains("2"));
    }

    @Test
    public void emptyConstructorTest(){
        User user = new User();
        assertNotNull(user);
    }

    @Test
    public void setScoreTest(){
        User user = new User();
        user.setScore(12);
        assertEquals(12, user.getScore());
    }
}
