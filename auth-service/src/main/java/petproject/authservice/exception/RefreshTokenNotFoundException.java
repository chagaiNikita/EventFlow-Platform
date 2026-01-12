package petproject.authservice.exception;

import java.util.NoSuchElementException;

public class RefreshTokenNotFoundException extends NoSuchElementException {
    public RefreshTokenNotFoundException(String message) {
        super(message);
    }

    public RefreshTokenNotFoundException() {
        super("Refresh token not found");
    }
}
