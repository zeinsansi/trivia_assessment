package trivia_backend.trivia_backend.openTrivia;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import trivia_backend.trivia_backend.openTrivia.dto.OpenTriviaResponse;
import trivia_backend.trivia_backend.openTrivia.dto.TokenResponse;
import trivia_backend.trivia_backend.openTrivia.repository.TriviaTokenRepository;

import java.util.Map;
import java.util.Optional;

@Service
public class OpenTriviaService {
    private final RestTemplate restTemplate;
    private final TriviaTokenRepository tokenRepository;

    public OpenTriviaService(RestTemplate restTemplate, TriviaTokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
        this.restTemplate = restTemplate;
    }

    // Fetches trivia questions from the Open Trivia Database API.
    public OpenTriviaResponse fetchQuestions(int amount, Integer category, String difficulty, String sessionId) {
        // Make sure a valid session token exists
        String token = getOrCreateSession(sessionId);
        String url = UriComponentsBuilder.fromUriString("https://opentdb.com/api.php")
                .queryParam("amount", amount)
                .queryParam("difficulty", difficulty)
                .queryParam("type", "multiple")
                .queryParam("token", token)
                .queryParamIfPresent("category", Optional.ofNullable(category))
                .toUriString();
        OpenTriviaResponse response = restTemplate.getForObject(url, OpenTriviaResponse.class);
        if (response == null) {
            throw new RuntimeException("Failed to fetch questions: no response from API");
        }
        // Handle different response codes from the Open Trivia API
        return switch (response.getResponseCode()) {
            case 0 -> response;
            case 1 -> throw new RuntimeException("No results available for the specified query.");
            case 4 -> {
                resetToken(sessionId);
                yield fetchQuestions(amount, category, difficulty, sessionId);
            }
            default -> throw new RuntimeException("Failed to fetch questions: " + response.getResponseCode());
        };
    }

    public String getOrCreateSession(String sessionId) {
        String token = getToken(sessionId);
        if(token != null) {
            return token;
        }
        return createNewToken(sessionId);
    }

    public String createNewToken(String sessionId) {
            String url = "https://opentdb.com/api_token.php?command=request";
            TokenResponse response = restTemplate.getForObject(url, TokenResponse.class);
            if(response == null || response.getResponseCode() != 0) {
                throw new RuntimeException("Invalid response from token API");
            }
            String token = response.getToken();
            tokenRepository.save(token, sessionId);
            return token;
    }

    public String getToken(String sessionId) {
        return tokenRepository.getToken(sessionId);
    }

    // Resets the session token to avoid repetition of questions.
    public void resetToken(String sessionId) {
        String token = tokenRepository.getToken(sessionId);
        if (token == null) {
            throw new IllegalArgumentException("No token found for session: " + sessionId);
        }
        String url = "https://opentdb.com/api_token.php?command=reset&token=" + token;
        TokenResponse response = restTemplate.getForObject(url, TokenResponse.class);
        if (response == null || response.getResponseCode() != 0) {
            throw new RuntimeException("Failed to reset token");
        }
        String responseToken = response.getToken();
        tokenRepository.updateToken(responseToken, sessionId);

    }
}
