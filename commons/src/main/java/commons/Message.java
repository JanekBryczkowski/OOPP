package commons;

import commons.Question;

public class Message {

    public Lobby lobby;
    public Question question;

    public Message(Lobby lobby, Question question) {
        this.lobby = lobby;
        this.question = question;
    }

}
