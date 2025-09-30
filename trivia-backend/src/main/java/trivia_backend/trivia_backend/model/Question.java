package trivia_backend.trivia_backend.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public class Question {
    @NotNull(message = "Question ID cannot be null")
    private UUID id;

    @NotBlank(message = "Question text cannot be blank")
    private String question;

    @NotEmpty(message = "Options cannot be empty")
    private List<String> options;

    public Question(UUID id, String question, List<String> options) {
        this.id = id;
        this.question = question;
        this.options = options;
    }

    public UUID getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }

    public List<String> getOptions() {
        return options;
    }
}