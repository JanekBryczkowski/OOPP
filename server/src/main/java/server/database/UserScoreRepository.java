package server.database;

import commons.Activity;
import commons.Scores;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserScoreRepository extends JpaRepository<Activity, Long> {

    @Query(value = "SELECT * FROM Scores ORDER BY DESC", nativeQuery = true)
    List<Scores> getThreeRandom();

}
