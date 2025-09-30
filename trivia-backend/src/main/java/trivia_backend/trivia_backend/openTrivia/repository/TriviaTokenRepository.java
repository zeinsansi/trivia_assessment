package trivia_backend.trivia_backend.openTrivia;

public interface TriviaTokenRepository {
    void save(String token, String sessionId);
    String getToken(String sessionId);
    void updateToken(String token, String sessionId);
    void clear();
}
