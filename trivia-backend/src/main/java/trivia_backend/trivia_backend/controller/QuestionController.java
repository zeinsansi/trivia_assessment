package trivia_backend.trivia_backend.controller;

import jakarta.validation.constraints.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import trivia_backend.trivia_backend.dto.CheckAnswerRequest;
import trivia_backend.trivia_backend.dto.CheckAnswerResponse;
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

    @GetMapping("")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Trivia API is running");
    }

    @GetMapping("/questions")
    public ResponseEntity<List<Question>> getQuestions(
            @RequestParam
            @Min(value = 1, message = "Amount must be at least 1")
            @Max(value = 50, message = "Amount cannot exceed 50")
            int amount,

            @RequestParam(required = false)
            Integer category,

            @RequestParam
            @NotBlank(message = "Difficulty cannot be blank")
            @Pattern(regexp = "easy|medium|hard", message = "Difficulty must be easy, medium, or hard")
            String difficulty,

            @RequestHeader("sessionId")
            @NotBlank
            String sessionId
    ) {
        List<Question> questions = service.getQuestions(amount, category, difficulty, sessionId);
        return ResponseEntity.ok(questions);
    }

    @PostMapping("/checkanswers")
    public ResponseEntity<CheckAnswerResponse>  checkAnswers(@RequestBody CheckAnswerRequest request) {
        CheckAnswerResponse answerResponse = service.checkAnswers(request);
        return ResponseEntity.ok(answerResponse);
    }
}
