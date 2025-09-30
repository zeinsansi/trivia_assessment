package trivia_backend.trivia_backend.openTrivia;

import java.util.List;

public class OpenTriviaQuestion {
    private String category;
    private String type;
    private String difficulty;
    private String question;
    private String correct_answer;
    private List<String> incorrect_answers;

    public String getCategory() { return category; }
    public String getType() { return type; }
    public String getDifficulty() { return difficulty; }
    public String getQuestion() { return question; }
    public String getCorrectAnswer() { return correct_answer; }
    public List<String> getIncorrectAnswers() { return incorrect_answers; }
}
