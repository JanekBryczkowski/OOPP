package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserTest {

    @Test
    public void constructorTest() {
        User user = new User("abc", 123);
        assertNotNull(user);
    }

    @Test
    public void usernameTest() {
        User user = new User("abc", 123);
        assertEquals("abc", user.username);
    }

    @Test
    public void scoreTest() {
        User user = new User("abc", 123);
        assertEquals(123, user.score);
    }

    @Test
    public void getUsernameTest() {
        User user = new User("abc", 123);
        assertEquals("abc", user.getUsername());
    }

    @Test
    public void getScoreTest() {
        User user = new User("abc", 123);
        assertEquals(123, user.getScore());
    }

    @Test
    public void equalsHashCode() {
        User a = new User("abc", 123);
        User b = new User("abc", 123);
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void notEqualsHashCode() {
        User a = new User("abc", 123);
        User b = new User("def", 123);
        assertNotEquals(a, b);
        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void hasToString() {
        String actual = new User("a", 2).toString();
        assertTrue(actual.contains(User.class.getSimpleName()));
        assertTrue(actual.contains("a"));
        assertTrue(actual.contains("2"));
    }
}
