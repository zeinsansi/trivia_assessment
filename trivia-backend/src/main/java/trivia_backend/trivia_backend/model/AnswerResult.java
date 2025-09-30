package trivia_backend.trivia_backend.model;

public class ResultDetail {
    private String id;
    private String question;
    private String yourAnswer;
    private String correctAnswer;
    private boolean isCorrect;

    public ResultDetail(String id, String question, String yourAnswer, String correctAnswer, boolean isCorrect) {
        this.id = id;
        this.question = question;
        this.yourAnswer = yourAnswer;
        this.correctAnswer = correctAnswer;
        this.isCorrect = isCorrect;
    }

    public String getId() { return id; }
    public String getQuestion() { return question; }
    public String getYourAnswer() { return yourAnswer; }
    public String getCorrectAnswer() { return correctAnswer; }
    public boolean isCorrect() { return isCorrect; }
}
