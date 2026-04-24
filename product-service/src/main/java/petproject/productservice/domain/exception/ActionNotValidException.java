package petproject.productservice.domain.exception;

public class ActionNotValidException extends RuntimeException {
    public ActionNotValidException() {
        super("Action is not valid");
    }
}
