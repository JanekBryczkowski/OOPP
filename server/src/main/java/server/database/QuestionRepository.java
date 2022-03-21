package server.database;

import commons.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Activity, Long>{

    @Query(value = "SELECT * FROM Activity ORDER BY RAND() LIMIT 1", nativeQuery = true)
    List<Activity> getThreeRandom();

    @Query(value = "DROP TABLE ACTIVITY", nativeQuery = true)
    void dropTable();

    @Query(value = "CREATE TABLE ACTIVITY (id varchar(255)), image_path varchar(255), title varchar(255), consumption_in_wh int, source varchar(255), consumption int", nativeQuery = true)
    void createTable();

}

