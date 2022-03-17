package server.api;

import commons.Scores;
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

    @GetMapping("/")
    public List<Scores> getScores(){
        return repository.findAll();
    }

    @GetMapping("/restart")
    public static void restartServer() {
        exit(0);
    }

    @PostMapping(path = {"/" , " "})
    public ResponseEntity<Scores> add(@RequestBody Scores scores) {

        if (scores == null) {
            return ResponseEntity.badRequest().build();
        }

        Scores saved = repository.save(scores);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public void deleteQuestion(@PathVariable("id") long id) {
        repository.deleteById(id);
    }
}
