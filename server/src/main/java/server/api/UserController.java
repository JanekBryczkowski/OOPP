package server.api;

import commons.User;
import org.springframework.web.bind.annotation.*;
import server.mainGame;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final mainGame main;

    public UserController(mainGame main) {
        this.main = main;
    }


        @PostMapping(path = { "", "/" })
        public User postUser(@RequestBody User user) {
                return main.addUser(user);
        }

        @GetMapping
        public ArrayList<User> getUser() {
            return main.getUsers();
        }


//    @GetMapping("/")
//    public List<Question> getQuestions() {
//        return repo.findAll();
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<Question> getById(@PathVariable("id") long id) {
//        if (id < 0 || !repo.existsById(id)) {
//            return ResponseEntity.badRequest().build();
//        }
//        return ResponseEntity.ok(repo.getById(id));
//    }
//
//    private static boolean isNullOrEmpty(String s) {
//        return s == null || s.isEmpty();
//    }
//
//    @PostMapping(path = { "", "/" })
//    public ResponseEntity<Question> add(@RequestBody Question question) {
//
//        if (question==null || isNullOrEmpty(question.title)) {
//            return ResponseEntity.badRequest().build();
//        }
//
//        Question saved = repo.save(question);
//        return ResponseEntity.ok(saved);
//    }
//
//    @DeleteMapping("/{id}")
//    void deleteQuestion(@PathVariable long id) {
//        repo.deleteById(id);
//    }
}
