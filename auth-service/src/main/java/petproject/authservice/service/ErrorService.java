package petproject.authservice.service;

import org.springframework.validation.BindingResult;
import petproject.authservice.exception.ErrorResponseBody;

public interface ErrorService {
    ErrorResponseBody getErrorResponseBody(Exception e);

    ErrorResponseBody makeResponse(BindingResult bindingResult);
}
