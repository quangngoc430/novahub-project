package vn.novahub.helpdesk.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import vn.novahub.helpdesk.model.ApiError;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.HashMap;

@ControllerAdvice
public class TokenExceptionHandler {

    private static final String MESSAGE = "message";

    @ExceptionHandler(value = TokenNotFoundException.class)
    public ResponseEntity<ApiError> handleTokenNotFoundException(HttpServletRequest request, Exception exception) {
        ApiError apiError = new ApiError();

        apiError.setTimestamp(Instant.now());
        apiError.setStatus(HttpStatus.NOT_FOUND.value());
        HashMap<String, String> errors = new HashMap<>();
        errors.put(MESSAGE, "Token not found");
        apiError.setErrors(errors);
        apiError.setMessage(exception.getMessage());
        apiError.setPath(request.getRequestURI());

        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = TokenIsExpiredException.class)
    public ResponseEntity<ApiError> handleTokenIsExpiredException(HttpServletRequest request, Exception exception) {
        ApiError apiError = new ApiError();

        apiError.setTimestamp(Instant.now());
        apiError.setStatus(HttpStatus.BAD_REQUEST.value());
        HashMap<String, String> errors = new HashMap<>();
        errors.put(MESSAGE, "Token is expired");
        apiError.setErrors(errors);
        apiError.setMessage(exception.getMessage());
        apiError.setPath(request.getRequestURI());

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

}
