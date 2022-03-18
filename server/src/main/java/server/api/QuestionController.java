package server.api;

import commons.Activity;
import commons.Question;
import server.database.QuestionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    private final QuestionRepository repo;

    public QuestionController(QuestionRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/")
    public List<Activity> getQuestions() {
        return repo.findAll();
    }

    @GetMapping("/getRandom")
    public Activity getRandomQuestion() {
        List<Activity> activityList = repo.findAll();
        int numberOfQuestions = activityList.size();
        int randomNumber = (int) (Math.random() * numberOfQuestions);
        return activityList.get(randomNumber);
    }

    @GetMapping("/getQuestion")
    public Question getActivities() {
        Question question = new Question();
        List<Activity> currentList = new ArrayList<>();
        int counter = (int) (Math.random() * 3 + 1);
        while (counter > 0) {
            List<Activity> random = repo.getThreeRandom();
            if (!currentList.contains(random.get(0))) {
                currentList.addAll(random);
                counter--;
            }
        }
        question.activityList.addAll(currentList);
        return question;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Activity> getById(@PathVariable("id") long id) {
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(repo.getById(id));
    }

    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    @PostMapping(path = {"", "/"})
    public ResponseEntity<Activity> add(@RequestBody Activity activity) {

        if (activity == null || isNullOrEmpty(activity.title)) {
            return ResponseEntity.badRequest().build();
        }

        Activity saved = repo.save(activity);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public void deleteQuestion(@PathVariable("id") long id) {
        repo.deleteById(id);
    }
}
