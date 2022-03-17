package server.database;

import commons.Score;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserScoreRepository extends JpaRepository<Score, Long> {

//    @Query(value = "SELECT * FROM Scores ORDER BY DESC", nativeQuery = true)
//    List<Score> getThreeRandom();

}
