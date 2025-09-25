package trivia_backend.trivia_backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import trivia_backend.trivia_backend.model.Question;
import trivia_backend.trivia_backend.service.QuestionService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class QuestionController {

    private final QuestionService service;

    public QuestionController(QuestionService service) {
        this.service = service;
    }


    @GetMapping("/questions")
    public List<Question> getQuestions() {
        return service.fetchQuestions();
    }
}
