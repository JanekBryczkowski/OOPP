package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ActivityTest {

    @Test
    public void constructorTest() {
        Activity activity = new Activity("abc", "abc", "abc", 123, "abc", 123);
        assertNotNull(activity);
    }

    @Test
    public void titleTest() {
        Activity activity = new Activity("abc", "abc", "abc", 123, "abc", 123);
        assertEquals("abc", activity.title);
    }

    @Test
    public void consumptionTest() {
        Activity activity = new Activity("abc", "abc", "abc", 123, "abc", 123);
        assertEquals(123, activity.consumption);
    }

    @Test
    public void getTitleTest() {
        Activity activity = new Activity("abc", "abc", "abc", 123, "abc", 123);
        assertEquals("abc", activity.getTitle());
    }

    @Test
    public void getConsumptionTest() {
        Activity activity = new Activity("abc", "abc", "abc", 123, "abc", 123);
        assertEquals(123, activity.getConsumption());
    }

    @Test
    public void equalsHashCode() {
        Activity a = new Activity("abc", "abc", "abc", 123, "abc", 123);
        Activity b = new Activity("abc", "abc", "abc", 123, "abc", 123);
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void notEqualsHashCode() {
        Activity b = new Activity("abc", "abc", "abc", 123, "abc", 123);
        Activity a = new Activity("adefbc", "abc", "abc", 123, "abc", 123);
        assertNotEquals(a, b);
        assertNotEquals(a.hashCode(), b.hashCode());
    }


}
