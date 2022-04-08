package commons;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

public class Question {

    public List<Activity> activityList;

    public int correctAnswer;

    /**
     * Constructor for the question object.
     */
    public Question() {
        this.activityList = new ArrayList<>();
    }

    /**
     * Method responsible for setting the correct answer of the given question.
     */
    public void setCorrectAnswer() {
        if (this.activityList.size() == 1) {
            correctAnswer = this.activityList.get(0).consumption_in_wh;
        } else if (this.activityList.size() == 2) {
            correctAnswer = findRatio();
        } else if (this.activityList.size() == 3) {
            correctAnswer = findLargest(this.activityList);
        }
    }

    /**
     * Equals method checking if the provided object is equal to the question object.
     *
     * @param obj - object to be checked.
     * @return - returns true if the objects are equal to each other.
     */
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    /**
     * Method generating the hashcode for the question object.
     *
     * @return - returns the integer representing the generated hashcode.
     */
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    /**
     * Method generating the string representation of the object question.
     *
     * @return - returns a string representation of the question object.
     */
    public String toString() {
        return ToStringBuilder.reflectionToString(this, MULTI_LINE_STYLE);
    }

    /**
     * Method for finding the largest value in the list of the activities.
     *
     * @param list - provided list of activities to find the largest value in.
     * @return - returns the largest value found in the list.
     */
    private int findLargest(List<Activity> list) {
        int index = -1;
        int maxValue = Integer.MIN_VALUE;
        for (int i = 0; i < list.size(); i++) {
            if (maxValue < list.get(i).consumption_in_wh) {
                maxValue = list.get(i).consumption_in_wh;
                index = i;
            }
        }

        return index + 1;
    }

    /**
     * Method responsible for finding the ratio.
     *
     * @return - returns the ratio.
     */
    private int findRatio() {
        return (int) (Math.random() * 3 + 1);
    }
}