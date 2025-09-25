package trivia_backend.trivia_backend.repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryAnswerRepository implements AnswerRepository{

    private final Map<String, String> answers = new ConcurrentHashMap<>();

    @Override
    public void save(String id, String correctAnswer) {
        answers.put(id, correctAnswer);
    }

    @Override
    public String getCorrectAnswer(String id) {
        return answers.get(id);
    }
}

