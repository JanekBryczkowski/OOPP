package server.api;

import commons.Score;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.UserScoreRepository;
import java.util.List;

import static java.lang.System.*;


@RestController
@RequestMapping("/api/scores")
public class ScoreController {

    private final UserScoreRepository repository;

    public ScoreController(UserScoreRepository repository){
        this.repository = repository;
    }

    /**
     * Getting all the score from the database
     *
     * @return  A list with all the scores from the database
     */
    @GetMapping
    public List<Score> getScores() {
        return  repository.findAll();
    }

    /**
     * Getting the top three scores from the database
     *
     * @return  A list with the top three scores from the database
     */
    @GetMapping("/top")
    public List<Score> getTopScores(){
        return repository.getTopThree();
    }

    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    /**
     * A mapping for restarting the server
     */
    @GetMapping("/restart")
    public static void restartServer() {
        exit(0);
    }

    /**
     * A mapping for adding a score to the database
     *
     * @param score The score that needs to be added to the database
     * @return      The score that is added to the database
     */
    @PostMapping(path = {"/", " "})
    public ResponseEntity<Score> add(@RequestBody Score score) {

        if (score == null || isNullOrEmpty(score.username)) {
            return ResponseEntity.badRequest().build();
        }

        Score saved = repository.save(score);
        return ResponseEntity.ok(saved);
    }

    /**
     * Mapping for deleting a score from the database
     *
     * @param id id of Score that needs to be deleted
     */
    @DeleteMapping("/{id}")
    public void deleteScore(@PathVariable("id") long id) {
        repository.deleteById(id);
    }
}
