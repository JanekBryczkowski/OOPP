package commons;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

public class Message {

    public Lobby lobby;

    public Question question;

    /**
     * Constructor for the message object.
     *
     * @param lobby - lobby of the message.
     * @param question - question of the message.
     */
    public Message(Lobby lobby, Question question) {
        this.lobby = lobby;
        this.question = question;
    }

    /**
     * Equals method checking if the provided object is equal to the message object.
     *
     * @param obj - object to be checked.
     * @return - returns true if the objects are equal to each other.
     */
    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    /**
     * Method generating the hashcode for the message object.
     *
     * @return - returns the integer representing the generated hashcode.
     */
    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    /**
     * Method generating the string representation of the object message.
     *
     * @return - returns a string representation of the message object.
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, MULTI_LINE_STYLE);
    }
}
