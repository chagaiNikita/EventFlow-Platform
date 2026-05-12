package petproject.apigateway.application.exception;

public record ErrorResponse(int status, String message, String path) {}