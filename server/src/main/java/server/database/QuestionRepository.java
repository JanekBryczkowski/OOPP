/*
  In this interface we connect to the QuestionRepository. This repository stores Activities in the database along with
  their attributes. To create the table we have an SQL query that also sets up the columns to coincide with the attributes
  from the parsed Json file we obtained from the Activity Bank.

  We also have an SQL query to get a random activity from the database. Depending on the randomised Integer created
  on the client side, we call getThreeRandom() once, twice or three times. This is used to construct a Question with
  a different number of activities each time and for it to be constructed randomly.

  The DROP TABLE SQL query is used to create the database table.
 */
package server.database;

import commons.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Activity, Long> {

    /**
     * Query for getting one random activity from the database.
     *
     * @return - returns one random activity from the database.
     */
    @Query(value = "SELECT * FROM Activity ORDER BY RAND() LIMIT 1", nativeQuery = true)
    List<Activity> getThreeRandom();

    /**
     * Query for deleting the activity table.
     */
    @Query(value = "DROP TABLE ACTIVITY", nativeQuery = true)
    void dropTable();

    /**
     * Query for creating the activity table.
     */
    @Query(value = "CREATE TABLE ACTIVITY (id varchar(255)), image_path varchar(255), title varchar(255), consumption_in_wh int, source varchar(255), consumption int", nativeQuery = true)
    void createTable();
}

