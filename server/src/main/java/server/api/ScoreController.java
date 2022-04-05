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

    /**
     * Constructor for ScoreController.
     *
     * @param repository This is the database for the Scores
     */
    public ScoreController(UserScoreRepository repository){
        this.repository = repository;
    }

    /**
     * Get mapping for the Scores.
     *
     * @return A List with all the Scores from the Score repository
     */
    @GetMapping
    public List<Score> getScores() {
        return  repository.findAll();
    }

    /**
     * Connects to SQL native query in the Score repository to get the top 3 scores.
     *
     * @return A List of the top 3 Scores in the database
     */
    @GetMapping("/top")
    public List<Score> getTopScores(){
        return repository.getTopThree();
    }

    /**
     * Checks if the String is null or empty.
     *
     * @param s Is the String to check
     * @return A boolean indicating whether the conditions are true for s
     */
    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    /**
     * Mapping to restart the server.
     */
    @GetMapping("/restart")
    public static void restartServer() {
        exit(0);
    }

    /**
     * Post mapping used to add a Score to the Score repository.
     *
     * @param score Is the Score that needs to be added to the database
     * @return ResponseEntity Score is the type being stored
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
     * Method for deleting the score from the database.
     * @param id - id of the score.
     */
    @DeleteMapping("/{id}")
    public void deleteScore(@PathVariable("id") long id) {
        repository.deleteById(id);
    }
}
