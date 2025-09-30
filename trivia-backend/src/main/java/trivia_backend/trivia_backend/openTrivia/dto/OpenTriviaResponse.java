package trivia_backend.trivia_backend.openTrivia;

import java.util.List;

public class OpenTriviaResponse {
    private int responseCode;
    private List<OpenTriviaQuestion> results;

    public int getResponseCode() {
        return responseCode;
    }

    public List<OpenTriviaQuestion> getQuestions() {
        return results;
    }
}
