package trivia_backend.trivia_backend.repository;

public interface AnswerRepository {
    void save(String id, String correctAnswer);
    String getCorrectAnswer(String id);
}
