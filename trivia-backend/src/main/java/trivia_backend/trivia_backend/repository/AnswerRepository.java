package trivia_backend.trivia_backend.repository;

import java.util.UUID;

public interface AnswerRepository {
    void save(UUID id, String correctAnswer, String questionText);
    String getAnswer(UUID id);
    String getQuestion(UUID id);
    void clear();
}
