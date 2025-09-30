package trivia_backend.trivia_backend.openTrivia.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import trivia_backend.trivia_backend.openTrivia.OpenTriviaQuestion;

import java.util.List;

@Data
public class OpenTriviaResponse {
    @JsonProperty("response_code")
    private int responseCode;
    @JsonProperty("results")
    private List<OpenTriviaQuestion> results;


    public OpenTriviaResponse(int responseCode, List<OpenTriviaQuestion> results) {
        this.responseCode = responseCode;
        this.results = results;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public List<OpenTriviaQuestion> getQuestions() {
        return results;
    }
}
