package server.database;

import commons.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserScoreRepository extends JpaRepository<Score, Long> {

    /**
     * Query for getting top three scores from the database.
     *
     * @return - returns top three scores from the database.
     */
    @Query(value = "SELECT * FROM Score ORDER BY SCORE DESC LIMIT 3", nativeQuery = true)
    List<Score> getTopThree();
}
