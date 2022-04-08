package commons;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class QuestionTest {

    @Test
    public void constructorTest() {
        Question question = new Question();
        assertNotNull(question);
    }

    @Test
    public void equalsHashCode() {
        Question a = new Question();
        Question b = new Question();
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void testSetCorrectAnswer() {
        Question a = new Question();
        a.activityList.add(new Activity("abc", "abc", "abc", 123, "abc", 123));
        a.setCorrectAnswer();
        assertEquals(123, a.correctAnswer);
        a.activityList.add(new Activity("abc", "abc", "abc", 123, "abc", 123));
        a.setCorrectAnswer();
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        assertTrue(list.contains(a.correctAnswer));
        a.activityList.add(new Activity("abc", "abc", "abc", 123, "abc", 123));
        a.setCorrectAnswer();
        assertEquals(1, a.correctAnswer);
    }

    @Test
    public void toStringTest() {
        Question a = new Question();
        a.activityList.add(new Activity("abc", "abc", "abc", 123, "abc", 123));
        a.setCorrectAnswer();
        String b = a.toString();
        assertEquals(b, a.toString());
    }
}
