package trivia_backend.trivia_backend.model;

import java.util.List;

public class Question {
    private String id;
    private String question;
    private List<String> options;

    public Question(String id, String question, List<String> options) {
        this.id = id;
        this.question = question;
        this.options = options;
    }

    public String getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }

    public List<String> getOptions() {
        return options;
    }
}