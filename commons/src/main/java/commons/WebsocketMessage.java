package commons;

public class WebsocketMessage {

    /**
     * The typOfMessage could have the following values:
     *      QUESTION    Whenever a question gets send
     *      EMOJIONE    Whenever someone has clicked on emoji one
     *      EMOJITOW    Whenever someone has clicked on emoji two
     *      EMOJITHREE  Whenever someone has clicked on emoji three
     *      LEADERBOARD Whenever it is time to show the leaderboard
     */
    public String typeOfMessage;
    public Question question;
    public String emojiUsername;

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

}
