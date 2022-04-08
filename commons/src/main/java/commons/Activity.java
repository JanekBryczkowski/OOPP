package commons;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

import javax.persistence.*;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Entity
public class Activity {

    @Id
    public String id;

    public String image_path;

    public String title;

    public int consumption_in_wh;

    @Column(length = 10000000)
    public String source;

    public int consumption;

    /**
     * Empty constructor for the activity object.
     */
    @SuppressWarnings("unused")
    public Activity() {

    }

    /**
     * Constructor for the activity object.
     *
     * @param id - id of the activity.
     * @param image_path - image path for the activity.
     * @param title - title of the activity.
     * @param consumption_in_wh - energy consumption of the activity in watt hours.
     * @param source - internet source of the activity.
     * @param consumption - consumption of the activity.
     */
    public Activity(String id, String image_path, String title, int consumption_in_wh, String source, int consumption) {
        this.id = id;
        this.image_path = image_path;
        this.title = title;
        this.consumption_in_wh = consumption_in_wh;
        this.source = source;
        this.consumption = consumption;
    }

    /**
     * Getter of the id.
     *
     * @return - string id.
     */
    public String getId() {
        return this.id;
    }

    /**
     * Getter of the title.
     *
     * @return - string title.
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Getter of the consumption in watt hours.
     *
     * @return - int consumption in watt hours.
     */
    public int getConsumption() {
        return this.consumption_in_wh;
    }

    /**
     * Setter for setting the title of the activity.
     *
     * @param title - provided title for the activity.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Setter for the consumption in watt hours.
     *
     * @param consumption_in_wh - integer of the consumption to be set.
     */
    //Important notation: We need to get rid of the consumption field, but I added here in order to
    //avoid the creation of new errors
    public void setConsumption_in_wh(int consumption_in_wh) {
        this.consumption_in_wh = consumption_in_wh;
        this.consumption = consumption_in_wh;
    }

    /**
     * Equals method checking if the object is equal to the activity object.
     *
     * @param obj - object to be checked.
     * @return - returns true if they're equal.
     */
    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    /**
     * Hashcode of the activity object.
     *
     * @return - returns a hashcode of the activity object.
     */
    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    /**
     * To string method of the activity object.
     *
     * @return - returns a string representation of the activity object.
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, MULTI_LINE_STYLE);
    }
}