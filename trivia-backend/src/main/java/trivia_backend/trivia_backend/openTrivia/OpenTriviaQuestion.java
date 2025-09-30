package trivia_backend.trivia_backend.openTrivia;

import com.fasterxml.jackson.annotation.JsonProperty;
import trivia_backend.trivia_backend.model.Question;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class OpenTriviaQuestion {
    @JsonProperty("category")
    private String category;
    @JsonProperty("type")
    private String type;
    @JsonProperty("difficulty")
    private String difficulty;
    @JsonProperty("question")
    private String question;
    @JsonProperty("correct_answer")
    private String correct_answer;
    @JsonProperty("incorrect_answers")
    private List<String> incorrect_answers;

    public OpenTriviaQuestion(String category, String type, String difficulty, String question, String correct_answer, List<String> incorrect_answers) {
        this.category = category;
        this.type = type;
        this.difficulty = difficulty;
        this.question = question;
        this.correct_answer = correct_answer;
        this.incorrect_answers = incorrect_answers;
    }

    public Question toQuestion(OpenTriviaQuestion question){
        List<String> options = new ArrayList<>(question.getIncorrectAnswers());
        options.add(question.getCorrectAnswer());
        Collections.shuffle(options);

        return new Question(
                UUID.randomUUID(),
                question.getQuestion(),
                options
        );
    }

    public String getCategory() { return category; }
    public String getType() { return type; }
    public String getDifficulty() { return difficulty; }
    public String getQuestion() { return question; }
    public String getCorrectAnswer() { return correct_answer; }
    public List<String> getIncorrectAnswers() { return incorrect_answers; }
}
