package trivia_backend.trivia_backend.dto.request;

import jakarta.validation.constraints.NotEmpty;
import trivia_backend.trivia_backend.model.Answer;

import java.util.List;

public class CheckAnswerRequest {
    @NotEmpty(message = "Answers cannot be empty")
    private List<Answer> answers;

    public CheckAnswerRequest() {}
    public CheckAnswerRequest(List<Answer> answers) { this.answers = answers; }


    public List<Answer> getAnswers() { return answers; }
}
