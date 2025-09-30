package trivia_backend.trivia_backend.repository;

import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryAnswerRepository implements AnswerRepository{

    private final Map<UUID, String> answers = new ConcurrentHashMap<>();
    private final Map<UUID, String> questions = new ConcurrentHashMap<>();

    @Override
    public void save(UUID id, String correctAnswer, String questionText) {
        answers.put(id, correctAnswer);
        questions.put(id, questionText);
    }

    @Override
    public String getAnswer(UUID id) {
        return answers.get(id);
    }

    @Override
    public String getQuestion(UUID id) {
        return questions.get(id);
    }

    @Override
    public void clear() {
        answers.clear();
        questions.clear();
    }
}

