/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package commons;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

public class Question {

//    public long id;

    public List<Activity> activityList;

    public int correctAnswer;

    public Question() {
        this.activityList = new ArrayList<>();
        // for object mappers
    }

//    public Question(String title, int consumption) {
//        this.title = title;
//        this.consumption = consumption;
//    }

    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    public void setCorrectAnswer() {
        correctAnswer = 0;
        if(activityList.get(1).consumption > activityList.get(correctAnswer).consumption)
            this.correctAnswer = 1;
        if(activityList.get(2).consumption > activityList.get(correctAnswer).consumption)
            this.correctAnswer = 2;
    }

    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this, MULTI_LINE_STYLE);
    }
}