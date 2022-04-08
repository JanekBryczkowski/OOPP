package commons;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WebsocketMessageTest {

    @Test
    void emptyConstructorTest() {
        WebsocketMessage websocketMessage = new WebsocketMessage();
        assertNotNull(websocketMessage);
    }

    @Test
    void constructorTest() {
        WebsocketMessage websocketMessage = new WebsocketMessage("type");
        assertNotNull(websocketMessage);
    }


    @Test
    void setQuestion() {
        WebsocketMessage websocketMessage = new WebsocketMessage();
        Question a = new Question();
        websocketMessage.setQuestion(a);
        assertEquals(a, websocketMessage.getQuestion());
    }

    @Test
    void setEmojiUsername() {
        WebsocketMessage websocketMessage = new WebsocketMessage();
        String a = "";
        websocketMessage.setEmojiUsername(a);
        assertEquals(a, websocketMessage.getEmojiUsername());
    }

    @Test
    void setUserList() {
        WebsocketMessage websocketMessage = new WebsocketMessage();
        List<User> a = new ArrayList<>();
        websocketMessage.setUserList(a);
        assertEquals(a, websocketMessage.getUserList());
    }
}