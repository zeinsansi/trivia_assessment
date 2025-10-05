package trivia_backend.trivia_backend.openTrivia.dto;


public class TokenResponse {
    int responseCode;
    String token;
    String responseMessage;

    public TokenResponse(int responseCode, String newToken) {
        this.responseCode = responseCode;
        this.token = newToken;
    }

    public String getToken() { return token;}
    public int getResponseCode() { return responseCode; }
    public String getResponseMessage() { return responseMessage; }
}
