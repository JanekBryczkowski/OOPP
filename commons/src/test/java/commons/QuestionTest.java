package commons;

import org.junit.jupiter.api.Test;

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

}
