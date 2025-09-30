package trivia_backend.trivia_backend.openTrivia.dto;

import lombok.Data;

@Data
public class TokenResponse {
    int responseCode;
    String token;
    String responseMessage;

    public int getResponseCode() {
        return responseCode;
    }
    public String getToken() {
        return token;
    }
    public String getResponseMessage() {
        return responseMessage;
    }
}
