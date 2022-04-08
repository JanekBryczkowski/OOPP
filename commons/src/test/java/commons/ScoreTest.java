package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ScoreTest {

    @Test
    public void constructorTest() {
    Score score = new Score("abc", 123);
    Score score1 = new Score();
    assertNotNull(score);
    assertNotNull(score1);
}

    @Test
    public void usernameTest() {
        Score score = new Score("abc", 123);
        assertEquals("abc", score.username);
    }

    @Test
    public void scoreTest() {
        Score score = new Score("abc", 123);
        assertEquals(123, score.score);
    }

    @Test
    public void getUsernameTest() {
        Score score = new Score("abc", 123);
        assertEquals("abc", score.getUsername());
    }

    @Test
    public void getScoreTest() {
        Score score = new Score("abc", 123);
        assertEquals(123, score.getScore());
    }

    @Test
    public void equalsHashCode() {
        Score a = new Score("abc", 123);
        Score b = new Score("abc", 123);
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void notEqualsHashCode() {
        Score a = new Score("abc", 123);
        Score b = new Score("def", 123);
        assertNotEquals(a, b);
        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void hasToString() {
        String actual = new Score("a", 2).toString();
        assertTrue(actual.contains(Score.class.getSimpleName()));
        assertTrue(actual.contains("a"));
        assertTrue(actual.contains("2"));
    }
}
