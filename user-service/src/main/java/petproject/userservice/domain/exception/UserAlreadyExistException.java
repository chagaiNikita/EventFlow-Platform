package petproject.userservice.domain.exception;

public class UserAlreadyExistException extends RuntimeException {
    public UserAlreadyExistException() {
        super("User already exists");
    }
}
