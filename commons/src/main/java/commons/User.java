package commons;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    public String username;

    public int score;

    public int lobbyNumber;

    /**
     * Empty constructor for the user object.
     */
    @SuppressWarnings("unused")
    public User() {

    }

    /**
     * Constructor for the user object.
     *
     * @param username - username of the user.
     * @param score - score of the user.
     * @param lobbyNumber - lobby number of the user.
     */
    public User(String username, int score, int lobbyNumber) {
        this.username = username;
        this.score = score;
        this.lobbyNumber = lobbyNumber;
    }

    /**
     * Getter for the username.
     *
     * @return - returns a string username.
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Getter for the score.
     *
     * @return - returns int score.
     */
    public int getScore() {
        return this.score;
    }

    /**
     * Getter for the lobby number.
     *
     * @return - returns int lobby number.
     */
    public int getLobbyNumber() {
        return this.lobbyNumber;
    }

    /**
     * Setter for the score.
     *
     * @param score - score to be set.
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Equals method checking if the provided object is equal to the user object.
     *
     * @param obj - object to be checked.
     * @return - returns true if the objects are equal to each other.
     */
    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    /**
     * Method generating the hashcode for the user object.
     *
     * @return - returns the integer representing the generated hashcode.
     */
    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    /**
     * Method generating the string representation of the object user.
     *
     * @return - returns a string representation of the user object.
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, MULTI_LINE_STYLE);
    }
}