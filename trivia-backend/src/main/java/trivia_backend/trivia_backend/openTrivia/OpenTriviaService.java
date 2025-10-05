package trivia_backend.trivia_backend.openTrivia;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import trivia_backend.trivia_backend.exception.ExternalApiNoResponseException;
import trivia_backend.trivia_backend.exception.NotEnoughQuestionException;
import trivia_backend.trivia_backend.openTrivia.dto.OpenTriviaResponse;
import trivia_backend.trivia_backend.openTrivia.dto.TokenResponse;
import trivia_backend.trivia_backend.openTrivia.repository.TriviaTokenRepository;
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

        OpenTriviaResponse response;
        try {
            response = restTemplate.getForObject(url, OpenTriviaResponse.class);
        } catch (RestClientException ex) {
            throw new RuntimeException("Failed to fetch questions from Open Trivia API", ex);
        }
        if (response == null) {
            throw new ExternalApiNoResponseException();
        }

        // Handle different response codes from the Open Trivia API
        // Code 0: Success Returned results successfully.
        // Code 1: No Results Could not return results. The API doesn't have enough questions for your query. (Ex. Asking for 50 Questions in a Category that only has 20.)
        // Code 2: Invalid Parameter Contains an invalid parameter. Arguments passed in aren't valid. (Ex. Amount = Five)
        // Code 3: Token Not Found Session Token does not exist.
        // Code 4: Token Empty Session Token has returned all possible questions for the specified query. Resetting the Token is necessary.
        return switch (response.getResponseCode()) {
            case 0 -> response;
            case 1 -> throw new NotEnoughQuestionException();
            case 2 -> throw new IllegalArgumentException("Invalid parameter(s) provided");
            case 3 -> {
                deleteToken(sessionId);
                createNewToken(sessionId);
                yield fetchQuestions(amount, category, difficulty, sessionId);
            }
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
            TokenResponse response;
            try{
                response = restTemplate.getForObject(url, TokenResponse.class);
            } catch (RestClientException ex){
                throw new RuntimeException("Failed to create new token from Open Trivia API", ex);
            }
            if(response == null) {
                throw new ExternalApiNoResponseException();
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
        if (response == null) {
            throw new ExternalApiNoResponseException();
        }
        if (response.getResponseCode() != 0) {
            throw new RuntimeException("Failed to reset token: " + response.getResponseCode());
        }
        String responseToken = response.getToken();
        tokenRepository.updateToken(responseToken, sessionId);

    }

    public void deleteToken(String sessionId) {
        tokenRepository.deleteToken(sessionId);
    }
}
