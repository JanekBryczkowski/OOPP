package server.api;

import commons.Question;
import server.database.QuestionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    private final QuestionRepository repo;

    public QuestionController(QuestionRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Question> getById(@PathVariable("id") long id) {
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(repo.getById(id));
    }

    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    @PostMapping(path = { "", "/" })
    public ResponseEntity<Question> add(@RequestBody Question question) {

        if (question==null || isNullOrEmpty(question.title)) {
            return ResponseEntity.badRequest().build();
        }

        Question saved = repo.save(question);
        return ResponseEntity.ok(saved);
    }
}
