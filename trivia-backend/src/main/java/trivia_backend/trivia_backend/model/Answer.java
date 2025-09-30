package trivia_backend.trivia_backend.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class Answer {
    @NotNull(message = "Question ID cannot be blank")
    private UUID questionId;

    @NotBlank(message = "Answer cannot be blank")
    private String answer;

    public Answer(UUID questionId, String answer) {
        this.questionId = questionId;
        this.answer = answer;
    }

    public UUID getQuestionId() { return questionId; }
    public String getAnswer() { return answer; }
}
