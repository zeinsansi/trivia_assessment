package trivia_backend.trivia_backend.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import trivia_backend.trivia_backend.model.Question;
import trivia_backend.trivia_backend.repository.AnswerRepository;

import java.util.*;

@Service
public class QuestionService {

    private final AnswerRepository answerRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private static final String apiUrl = "https://opentdb.com/api.php?amount=10&type=multiple";

    public QuestionService(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }

    public List<Question> fetchQuestions() {
        Map response = restTemplate.getForObject(apiUrl, Map.class);
        List<Map<String, Object>> results = (List<Map<String, Object>>) response.get("results");

        List<Question> questions = new ArrayList<>();

        for (Map<String, Object> questionData : results) {
            String id = UUID.randomUUID().toString();
            String questionText = (String) questionData.get("question");
            String correct = (String) questionData.get("correct_answer");

            List<String> options = new ArrayList<>((List<String>) questionData.get("incorrect_answers"));
            options.add(correct);
            Collections.shuffle(options);

            answerRepository.save(id, correct);

            questions.add(new Question(id, questionText, options));
        }
        return questions;
    }
}
