package server.database;

import commons.Scores;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserScoreRepository extends JpaRepository<Scores, Long> {

//    @Query(value = "SELECT * FROM Scores ORDER BY DESC", nativeQuery = true)
//    List<Scores>;

}
