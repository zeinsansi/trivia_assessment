package trivia_backend.trivia_backend.exception;

public class NotEnoughQuestionException extends RuntimeException{
    public NotEnoughQuestionException() {
        super("Not enough questions available for the requested category and difficulty.");
    }
}
