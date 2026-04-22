package kg.bee.productservice.domain.exception;

import java.util.NoSuchElementException;

public class CategoryAlreadyExistException extends NoSuchElementException {
    public CategoryAlreadyExistException() {
        super("Category already exists");
    }
}
