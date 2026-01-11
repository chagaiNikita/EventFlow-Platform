package petproject.authservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import petproject.authservice.exception.ErrorResponseBody;
import petproject.authservice.service.ErrorService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ErrorServiceImpl implements ErrorService {

    @Override
    public ErrorResponseBody getErrorResponseBody(Exception e) {
        log.warn("Get error {}", e.getMessage());
        return ErrorResponseBody.builder()
                .title(e.getMessage())
                .response(Map.of("errors", List.of(e.getMessage())))
                .build();
    }

    @Override
    public ErrorResponseBody makeResponse(BindingResult bindingResult) {
        Map<String, List<String>> reasons = new HashMap<>();
        bindingResult.getFieldErrors().stream()
                .filter(err -> err.getDefaultMessage() != null)
                .forEach(err -> {
                    List<String> errors = new ArrayList<>();
                    errors.add(err.getDefaultMessage());
                    if (!reasons.containsKey(err.getField())) {
                        reasons.put(err.getField(), errors);
                    }
                });
        return ErrorResponseBody.builder()
                .title("Validation Failed")
                .response(reasons)
                .build();
    }

}
