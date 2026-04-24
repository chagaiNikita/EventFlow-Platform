package petproject.productservice.domain.exception;

import java.util.NoSuchElementException;

public class ProductNotFoundException extends NoSuchElementException {
    public ProductNotFoundException() {
        super("Product not found");
    }
}
