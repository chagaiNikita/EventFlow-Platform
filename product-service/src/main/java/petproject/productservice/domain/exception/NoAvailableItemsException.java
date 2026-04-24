package petproject.productservice.domain.exception;

public class NoAvailableItemsException extends RuntimeException {
    public NoAvailableItemsException() {
        super("No enough items available");
    }
}
