package server.database;

import commons.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserScoreRepository extends JpaRepository<Score, Long> {

    @Query(value = "SELECT * FROM Score ORDER BY SCORE DESC LIMIT 3", nativeQuery = true)
    List<Score> getTopThree();

}
