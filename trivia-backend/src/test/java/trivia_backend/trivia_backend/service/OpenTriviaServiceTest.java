package trivia_backend.trivia_backend.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;
import trivia_backend.trivia_backend.openTrivia.OpenTriviaQuestion;
import trivia_backend.trivia_backend.openTrivia.OpenTriviaService;
import trivia_backend.trivia_backend.openTrivia.dto.OpenTriviaResponse;
import trivia_backend.trivia_backend.openTrivia.dto.TokenResponse;
import trivia_backend.trivia_backend.openTrivia.repository.TriviaTokenRepository;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OpenTriviaServiceTest {

    @Mock
    private TriviaTokenRepository tokenRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private OpenTriviaService openTriviaService;

    @Test
    void fetchQuestions_WhenGivenExistingSessionId_ShouldReturnQuestions() {
       //Arrange
         int amount = 2;
         Integer category = 9;
         String difficulty = "easy";
         String sessionId = "test-session";
         String token= "test-token";

         when(tokenRepository.getToken(sessionId)).thenReturn(token);

        List<OpenTriviaQuestion> results = List.of(
                new OpenTriviaQuestion("Any Category", "Multi", "easy", "Test Question1", "Correct Answer1", List.of("Incorrect1", "Incorrect2", "Incorrect3")),
                new OpenTriviaQuestion("Any Category", "Multi", "easy", "Test Question2", "Correct Answer2", List.of("Incorrect1", "Incorrect2", "Incorrect3"))
        );
        OpenTriviaResponse apiResponse = new OpenTriviaResponse(0, results);

        when(restTemplate.getForObject(anyString(), eq(OpenTriviaResponse.class))).thenReturn(apiResponse);
         //Act
        OpenTriviaResponse response = openTriviaService.fetchQuestions(amount, category, difficulty, sessionId);
        //Assert
        assertEquals(2, response.getQuestions().size());
        assertEquals(0, response.getResponseCode());
        verify(tokenRepository, times(1)).getToken(sessionId);
    }

    @Test
    void fetchQuestions_WhenGivenNonExistingSessionId_ShouldCreateNewTokenAndReturnQuestions() {
        //Arrange
        int amount = 2;
        Integer category = 9;
        String difficulty = "easy";
        String sessionId = "new-session";
        String newToken= "new-token";

        when(tokenRepository.getToken(sessionId)).thenReturn(null);

        TokenResponse tokenResponse = new TokenResponse(0, newToken);

        when(restTemplate.getForObject(anyString(), eq(TokenResponse.class))).thenReturn(tokenResponse);
        List<OpenTriviaQuestion> results = List.of(
                new OpenTriviaQuestion("Any Category", "Multi", "easy", "Test Question1", "Correct Answer1", List.of("Incorrect1", "Incorrect2", "Incorrect3")),
                new OpenTriviaQuestion("Any Category", "Multi", "easy", "Test Question2", "Correct Answer2", List.of("Incorrect1", "Incorrect2", "Incorrect3"))
        );

        OpenTriviaResponse apiResponse = new OpenTriviaResponse(0, results);

        when(restTemplate.getForObject(anyString(), eq(OpenTriviaResponse.class))).thenReturn(apiResponse);
        //Act
        OpenTriviaResponse response = openTriviaService.fetchQuestions(amount, category, difficulty, sessionId);
        //Assert
        assertEquals(2, response.getQuestions().size());
        assertEquals(0, response.getResponseCode());
        verify(tokenRepository, times(1)).getToken(sessionId);
        verify(tokenRepository, times(1)).save(newToken, sessionId);
    }

    @Test
    void fetchQuestions_WhenGivenExhaustedToken_ShouldResetTokenAndReturnQuestions() {
        //Arrange
        int amount = 2;
        Integer category = 9;
        String difficulty = "easy";
        String sessionId = "exhausted-session";
        String exhaustedToken= "exhausted-token";
        String newToken= "new-token";

        when(tokenRepository.getToken(sessionId)).thenReturn(exhaustedToken);

        List<OpenTriviaQuestion> results = List.of(
                new OpenTriviaQuestion("Any Category", "Multi", "easy", "Test Question1", "Correct Answer1", List.of("Incorrect1", "Incorrect2", "Incorrect3")),
                new OpenTriviaQuestion("Any Category", "Multi", "easy", "Test Question2", "Correct Answer2", List.of("Incorrect1", "Incorrect2", "Incorrect3"))
        );

        OpenTriviaResponse exhaustedResponse = new OpenTriviaResponse(4, List.of());
        OpenTriviaResponse apiResponse = new OpenTriviaResponse(0, results);

        when(restTemplate.getForObject(anyString(), eq(OpenTriviaResponse.class)))
                .thenReturn(exhaustedResponse)
                .thenReturn(apiResponse);

        TokenResponse resetResponse = new TokenResponse(0, newToken);
        when(restTemplate.getForObject(anyString(), eq(TokenResponse.class))).thenReturn(resetResponse);
        //Act
        OpenTriviaResponse response = openTriviaService.fetchQuestions(amount, category, difficulty, sessionId);
        //Assert
        assertEquals(2, response.getQuestions().size());
        assertEquals(0, response.getResponseCode());
        verify(tokenRepository, times(1)).updateToken(newToken,sessionId);
    }

    @Test
    void fetchQuestions_WhenGivenInvalidParameters_ShouldThrowIllegalArgumentException() {
        //Arrange
        int amount = 2;
        Integer category = 9999;
        String difficulty = "easy";
        String sessionId = "test-session";
        String token= "test-token";

        when(tokenRepository.getToken(sessionId)).thenReturn(token);

        OpenTriviaResponse invalidParamResponse = new OpenTriviaResponse(2, List.of());

        when(restTemplate.getForObject(anyString(), eq(OpenTriviaResponse.class))).thenReturn(invalidParamResponse);
        //Act & Assert
        assertThrows(IllegalArgumentException.class , () -> openTriviaService.fetchQuestions(amount, category, difficulty, sessionId));
    }
}
