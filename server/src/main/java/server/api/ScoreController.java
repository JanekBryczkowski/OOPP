package server.api;

import commons.Score;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.UserScoreRepository;

import java.util.List;


@RestController
@RequestMapping("/api/scores")
public class ScoreController {

    private final UserScoreRepository repository;

    public ScoreController(UserScoreRepository repository){
        this.repository = repository;
    }

    @GetMapping("/")
    public List<Score> getScores(){
        return repository.findAll();
    }

    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    @PostMapping(path = {"/" , " "})
    public ResponseEntity<Score> add(@RequestBody Score score) {

        if (score == null || isNullOrEmpty(score.username)) {
            return ResponseEntity.badRequest().build();
        }

        Score saved = repository.save(score);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public void deleteQuestion(@PathVariable("id") long id) {
        repository.deleteById(id);
    }
}
