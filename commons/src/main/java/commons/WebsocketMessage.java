package commons;

import java.util.List;

public class WebsocketMessage {

    /**
     * The typOfMessage could have the following values:
     *      QUESTION    Whenever a question gets send
     *      EMOJIONE    Whenever someone has clicked on emoji one
     *      EMOJITWO    Whenever someone has clicked on emoji two
     *      EMOJITHREE  Whenever someone has clicked on emoji three
     *      LEADERBOARD Whenever it is time to show the leaderboard
     *      JOKERTHREE  Whenever someone has pressed joker three
     *      JOKERUSED   Whenever another player uses a joker
     */
    public String typeOfMessage;
    public Question question;
    public String emojiUsername;
    public List<User> userList;
    public int jokerUsed;

    public WebsocketMessage() {

    }

    public WebsocketMessage(String typeOfMessage) {
        this.typeOfMessage = typeOfMessage;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public void setEmojiUsername(String emojiUsername) {
        this.emojiUsername = emojiUsername;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

}
