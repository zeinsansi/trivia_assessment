package trivia_backend.trivia_backend.exception;

public class ExternalApiNoResponseException extends RuntimeException{
    public ExternalApiNoResponseException() {
        super("External API did not respond.");
    }
}
