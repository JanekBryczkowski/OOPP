package server.database;

import commons.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Activity, Long>{

    @Query(value = "SELECT * FROM Activity ORDER BY RAND() LIMIT 1", nativeQuery = true)
    List<Activity> getThreeRandom();

}

