package trivia_backend.trivia_backend.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import trivia_backend.trivia_backend.dto.CheckAnswerRequest;
import trivia_backend.trivia_backend.dto.CheckAnswerResponse;
import trivia_backend.trivia_backend.model.Answer;
import trivia_backend.trivia_backend.model.Question;
import trivia_backend.trivia_backend.openTrivia.OpenTriviaQuestion;
import trivia_backend.trivia_backend.openTrivia.OpenTriviaService;
import trivia_backend.trivia_backend.openTrivia.dto.OpenTriviaResponse;
import trivia_backend.trivia_backend.repository.AnswerRepository;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class QuestionServiceTest {

    @Mock
    private AnswerRepository answerRepository;

    @Mock
    private OpenTriviaService openTriviaService;

    @InjectMocks
    private QuestionService questionService;

    @Test
    void getQuestion_shouldReturnQuestions() {
        //Arrange
        int amount = 2;
        Integer category = 9;
        String difficulty = "easy";
        String sessionId = "test-session";
        List<OpenTriviaQuestion> results = List.of(
                new OpenTriviaQuestion("Any Category", "Multi", "easy", "Test Question1", "Correct Answer1", List.of("Incorrect1", "Incorrect2", "Incorrect3")),
                new OpenTriviaQuestion("Any Category", "Multi", "easy", "Test Question2", "Correct Answer2", List.of("Incorrect1", "Incorrect2", "Incorrect3"))
        );
        OpenTriviaResponse apiResponse = new OpenTriviaResponse(0, results);

        when(openTriviaService.fetchQuestions(amount, category, difficulty, sessionId)).thenReturn(apiResponse);
        //Act
        List<Question> questions = questionService.getQuestions(amount, category, difficulty, sessionId);
        //Assert
        assertEquals(2, questions.size());
        verify(answerRepository, times(2)).save(any(), any(), any());
    }

    @Test
    void checkAnswers_shouldReturnCheckedAnswers() {
        //Arrange
        UUID questionId1 = UUID.randomUUID();
        String correctAnswer1 = "Correct Answer1";
        UUID questionId2 = UUID.randomUUID();
        String correctAnswer2 = "Correct Answer2";

        CheckAnswerRequest request = new CheckAnswerRequest(List.of(
                new Answer(questionId1, "Correct Answer1"),
                new Answer(questionId2, "Wrong Answer2")
        ));

        when(answerRepository.getAnswer(questionId1)).thenReturn(correctAnswer1);
        when(answerRepository.getAnswer(questionId2)).thenReturn(correctAnswer2);

        //Act
        CheckAnswerResponse response = questionService.checkAnswers(request);

        //Assert
        assertEquals(1, response.getScore());
        assertEquals(2, response.getTotal());
        assertEquals(2, response.getResults().size());
    }
}
