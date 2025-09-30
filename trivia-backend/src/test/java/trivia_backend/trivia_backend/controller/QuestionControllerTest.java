package trivia_backend.trivia_backend.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import trivia_backend.trivia_backend.dto.CheckAnswerRequest;
import trivia_backend.trivia_backend.dto.CheckAnswerResponse;
import trivia_backend.trivia_backend.model.Answer;
import trivia_backend.trivia_backend.model.AnswerResult;
import trivia_backend.trivia_backend.model.Question;
import trivia_backend.trivia_backend.service.QuestionService;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class QuestionControllerTest {

    @Mock
    private QuestionService service;

    @InjectMocks
    private QuestionController controller;

    @Test
    void getQuestion_shouldReturnQuestions() {
        //Arrange
        int amount = 2;
        int category = 9;
        String difficulty = "easy";
        String sessionId = "test-session";

        Question question1 = new Question(UUID.randomUUID(), "Test Question1", List.of("Option1", "Option2", "Option3", "Option4"));
        Question question2 = new Question(UUID.randomUUID(), "Test Question2", List.of("Option1", "Option2", "Option3", "Option4"));

        List<Question> questionList = new ArrayList<>();
        questionList.add(question1);
        questionList.add(question2);

        when(service.getQuestions(amount, category, difficulty, sessionId)).thenReturn(questionList);

        //Act
        ResponseEntity<List<Question>> response = controller.getQuestions(amount, category, difficulty, sessionId);

        //Assert
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void checkAnswers_shouldReturnCheckedAnswers() {
        //Arrange
        Answer answer1 = new Answer(UUID.randomUUID(), "Test Answer1");
        Answer answer2 = new Answer(UUID.randomUUID(), "Test Answer2");
        List<Answer> answers = new ArrayList<>();
        answers.add(answer1);
        answers.add(answer2);

        CheckAnswerRequest request = new CheckAnswerRequest(answers);
        AnswerResult result1 = new AnswerResult(answer1.getQuestionId(), "Test Question1","Correct Answer1", "Correct Answer1", true);
        AnswerResult result2 = new AnswerResult(answer2.getQuestionId(), "Test Question2", "Wrong Answer2", "Correct Answer2", false);
        CheckAnswerResponse response = new CheckAnswerResponse(1, 2, List.of(result1, result2));

        when(service.checkAnswers(request)).thenReturn(response);

        //Act
        ResponseEntity<CheckAnswerResponse> controllerResponse = controller.checkAnswers(request);

        //Assert
          assertTrue(controllerResponse.getStatusCode().is2xxSuccessful());
          assertNotNull(controllerResponse.getBody());
          assertEquals(1, controllerResponse.getBody().getScore());
          assertEquals(2, controllerResponse.getBody().getTotal());
          assertEquals(2, controllerResponse.getBody().getResults().size());
    }
}
