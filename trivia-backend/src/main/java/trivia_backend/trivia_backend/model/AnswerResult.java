package trivia_backend.trivia_backend.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class AnswerResult {
    @NotNull(message = "Question ID cannot be null")
    private UUID questionId;

    @NotBlank(message = "Question text cannot be blank")
    private String question;

    @NotBlank(message = "Your answer cannot be blank")
    private String yourAnswer;

    @NotBlank(message = "Correct answer cannot be blank")
    private String correctAnswer;

    private boolean isCorrect;

    public AnswerResult(UUID questionId, String question, String yourAnswer, String correctAnswer, boolean isCorrect) {
        this.questionId = questionId;
        this.question = question;
        this.yourAnswer = yourAnswer;
        this.correctAnswer = correctAnswer;
        this.isCorrect = isCorrect;
    }

    public UUID getQuestionId() { return questionId; }
    public String getQuestion() { return question; }
    public String getYourAnswer() { return yourAnswer; }
    public String getCorrectAnswer() { return correctAnswer; }
    public boolean isCorrect() { return isCorrect; }
}
