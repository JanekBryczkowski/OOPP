package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MessageTest {

    @Test
    public void constructorTest() {
        Message message = new Message(new Lobby(2), new Question());
        assertNotNull(message);
    }

    @Test
    public void notEqualsHashCode() {
        Message a = new Message(new Lobby(2), new Question());
        Message b = new Message(new Lobby(3), new Question());
        assertNotEquals(a, b);
        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void toStringTest() {
        Message a = new Message(new Lobby(2), new Question());
        String b = a.toString();
        assertEquals(b, a.toString());
    }
}
