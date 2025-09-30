package trivia_backend.trivia_backend.dto.response;

import trivia_backend.trivia_backend.model.AnswerResult;

import java.util.List;

public class CheckAnswerResponse {
    private int score;
    private int total;
    private List<AnswerResult> results;

    public CheckAnswerResponse(int score, int total, List<AnswerResult> results) {
        this.score = score;
        this.total = total;
        this.results = results;
    }

    public int getScore() { return score; }
    public int getTotal() { return total; }
    public List<AnswerResult> getResults() { return results; }
}
