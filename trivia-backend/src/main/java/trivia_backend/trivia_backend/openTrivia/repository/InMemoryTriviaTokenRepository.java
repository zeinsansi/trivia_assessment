package trivia_backend.trivia_backend.openTrivia.repository;

import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryTriviaTokenRepository implements TriviaTokenRepository {

    private final Map<String, String> tokens = new ConcurrentHashMap<>();

    @Override
    public void save(String token, String sessionId) {
        tokens.put(sessionId, token);
    }

    @Override
    public String getToken(String sessionId) {
        return tokens.get(sessionId);
    }

    @Override
    public void updateToken(String token, String sessionId) {
        tokens.put(sessionId, token);
    }

    @Override
    public void clear() {

    }
}
