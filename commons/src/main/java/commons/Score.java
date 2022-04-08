package commons;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

@Entity
public class Score {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    public String username;

    public int score;

    /**
     * Empty constructor for the Score object.
     */
    public Score() {

    }

    /**
     * Constructor for the Score object.
     *
     * @param username - username of the user who has a score.
     * @param score - score of the user.
     */
    public Score(String username, int score) {
        this.username = username;
        this.score = score;
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
     * Equals method checking if the provided object is equal to the score object.
     *
     * @param obj - object to be checked.
     * @return - returns true if the objects are equal to each other.
     */
    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    /**
     * Method generating the hashcode for the score object.
     *
     * @return - returns the integer representing the generated hashcode.
     */
    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    /**
     * Method generating the string representation of the object score.
     *
     * @return - returns a string representation of the score object.
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, MULTI_LINE_STYLE);
    }
}
