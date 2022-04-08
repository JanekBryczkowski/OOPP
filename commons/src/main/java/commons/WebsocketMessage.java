package commons;

import java.util.List;

public class WebsocketMessage {

    /**
     * The typOfMessage could have the following values:
     * QUESTION     - Whenever a question gets send
     * EMOJIONE     - Whenever someone has clicked on emoji one
     * EMOJITWO     - Whenever someone has clicked on emoji two
     * EMOJITHREE   - Whenever someone has clicked on emoji three
     * LEADERBOARD  - Whenever it is time to show the leaderboard
     * JOKERTHREE   - Whenever someone has pressed joker three
     * JOKERUSED    - Whenever another player uses a joker
     */
    public String typeOfMessage;
    public Question question;
    public String emojiUsername;
    public List<User> userList;
    public int jokerUsed;

    /**
     * Empty constructor for the web socket message.
     */
    public WebsocketMessage() {

    }

    /**
     * Constructor for the web socket message.
     *
     * @param typeOfMessage - string representing the type of the web socket message.
     */
    public WebsocketMessage(String typeOfMessage) {
        this.typeOfMessage = typeOfMessage;
    }

    /**
     * Setter of the question.
     *
     * @param question - question to be set to the object.
     */
    public void setQuestion(Question question) {
        this.question = question;
    }

    /**
     * Getter of the question.
     * @return - returns a question.
     */
    public Question getQuestion() {
        return question;
    }

    /**
     * Getter of the emoji username.
     * @return - string emoji username.
     */
    public String getEmojiUsername() {
        return emojiUsername;
    }

    /**
     * Getter of the user list.
     * @return - returns a user list.
     */
    public List<User> getUserList() {
        return userList;
    }

    /**
     * Setter of the emoji username.
     *
     * @param emojiUsername - string indicating which emoji has been used.
     */
    public void setEmojiUsername(String emojiUsername) {
        this.emojiUsername = emojiUsername;
    }

    /**
     * Setter for the list of users.
     *
     * @param userList - user list that needs to be set.
     */
    public void setUserList(List<User> userList) {
        this.userList = userList;
    }
}
