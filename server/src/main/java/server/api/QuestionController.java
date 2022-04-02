package server.api;

import commons.Activity;
import commons.Question;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import server.database.QuestionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.Main;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    private final QuestionRepository repo;
    private SimpMessagingTemplate msgs;

    /**
     * Constructor for QuestionController.
     *
     * @param repo The Activity database
     * @param msgs The web socket message
     */
    public QuestionController(Main mainGame, QuestionRepository repo, SimpMessagingTemplate msgs) {
        this.repo = repo;
        this.msgs = msgs;
    }

    /**
     * This function stores a list of type Activity inside the database.
     * This function gets called at the start of running
     * the server to store every activity from the json file.
     *
     * @param activities A list of activities to store in the database.
     * @return An iterable list of all activities in the database.
     */
    public Iterable<Activity> save(List<Activity> activities) {
        return repo.saveAll(activities);
    }

    /**
     * This functions gets called to see all the activities
     * that are present in the database.
     *
     * @return A list of all activities in the database.
     */
    @GetMapping("/")
    public List<Activity> getAllActivities() {
        return repo.findAll();
    }

    /**
     * This functions gets called whenever a client needs a question
     * in a solo player game.
     *
     * @return A random question with 1,2 or 3 activities
     */
    @GetMapping("/getQuestion")
    public Question getActivities() {
        Question question = new Question();
        List<Activity> currentList = new ArrayList<>();
        int counter = (int) (Math.random() * 3 + 1);
        if (counter == 2) {
            List<Activity> random1 = repo.getThreeRandom();
            int firstConsumption = random1.get(0).consumption;
            currentList.addAll(random1);
            List<Activity> random2 = repo.getThreeRandom();
            int secondConsumption = random2.get(0).consumption;
            do {
                if (firstConsumption  > 10 * secondConsumption
                        || secondConsumption  > 10 * firstConsumption) {
                    currentList.addAll(random2);
                    break;
                } else {
                    random2 = repo.getThreeRandom();
                    secondConsumption = random2.get(0).consumption;
                }
            } while (firstConsumption  < 10 * secondConsumption
                    && secondConsumption < firstConsumption * 10);
        } else {
            while (counter > 0) {
                List<Activity> random = repo.getThreeRandom();
                if (!currentList.contains(random.get(0))) {
                    currentList.addAll(random);
                    counter--;
                }
            }
        }
        question.activityList.addAll(currentList);
        return question;
    }

    /**
     * This function is called whenever the user needs a specific activity.
     * The function does not get called by the client but can be used
     * for debugging through the localhost.
     *
     * @param id The id of the question.
     * @return The requested question.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Activity> getById(@PathVariable("id") long id) {
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(repo.getById(id));
    }

    /**
     * Checks if the String input as parameter is null or empty.
     *
     * @param s String to be checked.
     * @return boolean indicating the condition of s
     */
    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    /**
     * Posts an Activity to the database. Checks if this Activity object actually
     * contains a body.
     *
     * @param activity Activity to be saved in the repository
     * @return Entity Activity
     */
    @PostMapping(path = {"", "/"})
    public ResponseEntity<Activity> add(@RequestBody Activity activity) {

        if (activity == null || isNullOrEmpty(activity.title)) {
            return ResponseEntity.badRequest().build();
        }

        Activity saved = repo.save(activity);
        return ResponseEntity.ok(saved);
    }

    /**
     * Delete mapping can be used to remove an Activity from the repository.
     *
     * @param id Is the unique id representing that specific Activity
     */
    @DeleteMapping("/{id}")
    public void deleteQuestion(@PathVariable("id") long id) {
        repo.deleteById(id);
    }

}
