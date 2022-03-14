package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ScoresTest {

    @Test
    public void constructorTest() {
    Scores scores = new Scores("abc", 123);
    assertNotNull(scores);
}

    @Test
    public void usernameTest() {
        Scores scores = new Scores("abc", 123);
        assertEquals("abc", scores.username);
    }

    @Test
    public void scoreTest() {
        Scores scores = new Scores("abc", 123);
        assertEquals(123, scores.score);
    }

    @Test
    public void getUsernameTest() {
        Scores scores = new Scores("abc", 123);
        assertEquals("abc", scores.getUsername());
    }

    @Test
    public void getScoreTest() {
        Scores scores = new Scores("abc", 123);
        assertEquals(123, scores.getScore());
    }

    @Test
    public void equalsHashCode() {
        Scores a = new Scores("abc", 123);
        Scores b = new Scores("abc", 123);
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void notEqualsHashCode() {
        Scores a = new Scores("abc", 123);
        Scores b = new Scores("def", 123);
        assertNotEquals(a, b);
        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void hasToString() {
        Scores scores = new Scores("abc", 123);
        assertEquals("Scores{username='abc', score=123}", scores.toString());
    }
}
