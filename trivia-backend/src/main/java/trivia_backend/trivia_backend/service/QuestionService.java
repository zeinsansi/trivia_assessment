package trivia_backend.trivia_backend.service;

import org.springframework.stereotype.Service;
import trivia_backend.trivia_backend.dto.CheckAnswerRequest;
import trivia_backend.trivia_backend.dto.CheckAnswerResponse;
import trivia_backend.trivia_backend.model.*;
import trivia_backend.trivia_backend.openTrivia.OpenTriviaQuestion;
import trivia_backend.trivia_backend.openTrivia.dto.OpenTriviaResponse;
import trivia_backend.trivia_backend.openTrivia.OpenTriviaService;
import trivia_backend.trivia_backend.repository.AnswerRepository;

import java.util.*;

@Service
public class QuestionService {

    private final AnswerRepository answerRepository;
    private final OpenTriviaService openTriviaService;



    public QuestionService(AnswerRepository answerRepository, OpenTriviaService openTriviaService) {
        this.answerRepository = answerRepository;
        this.openTriviaService = openTriviaService;

    }

    public List<Question> getQuestions(int amount, Integer category, String difficulty, String sessionId) {
        try {
            // Fetch questions from Open Trivia API
            OpenTriviaResponse apiResponse = openTriviaService.fetchQuestions(amount, category , difficulty, sessionId);
            List<OpenTriviaQuestion> apiQuestions = apiResponse.getQuestions();

            List<Question> questions = new ArrayList<>();
            answerRepository.clear();

            for (OpenTriviaQuestion item : apiQuestions) {
                Question question = item.toQuestion(item);
                answerRepository.save(question.getId(), item.getCorrectAnswer(), question.getQuestion());
                questions.add(question);
            }
            return questions;
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch questions");
        }
    }

    public CheckAnswerResponse checkAnswers(CheckAnswerRequest request) {
        try{
            int score = 0;
            List<AnswerResult> results = new ArrayList<>();

            for (Answer answer : request.getAnswers()) {
                String correct = answerRepository.getAnswer(answer.getQuestionId());
                String questionText = answerRepository.getQuestion(answer.getQuestionId());
                boolean isCorrect = correct.equals(answer.getAnswer());
                if (isCorrect) score++;

                results.add(new AnswerResult(
                        answer.getQuestionId(),
                        questionText,
                        answer.getAnswer(),
                        correct,
                        isCorrect
                ));
            }

            return new CheckAnswerResponse(score, request.getAnswers().size(), results);
        } catch (Exception e) {
            throw new RuntimeException("Failed to check answers");
        }
    }
}
