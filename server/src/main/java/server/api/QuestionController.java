package server.api;

import commons.Activity;
import commons.Question;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import server.database.QuestionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.Main;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    private final QuestionRepository repo;
    private SimpMessagingTemplate msgs;

    public QuestionController(Main mainGame, QuestionRepository repo, SimpMessagingTemplate msgs) {
        this.repo = repo;
        this.msgs = msgs;
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
        question.activityList = repo.getThreeRandom();
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

    @PostMapping(path = { "", "/" })
    public ResponseEntity<Activity> add(@RequestBody Activity activity) {

        if (activity ==null || isNullOrEmpty(activity.title)) {
            return ResponseEntity.badRequest().build();
        }

        Activity saved = repo.save(activity);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public void deleteQuestion(@PathVariable("id") long id) {
        repo.deleteById(id);
    }

    @MessageMapping("/question")
    @SendTo("/topic/question")
    public Question sendQuestion(Question question) {
        System.out.println("SENDING QUESTION " + question.toString());
        return question;
    }

}
