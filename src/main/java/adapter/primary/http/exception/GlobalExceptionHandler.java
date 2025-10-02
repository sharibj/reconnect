package adapter.primary.http.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex) {
        String message = ex.getMessage();
        HttpStatus status;
        
        // Conflict cases (409)
        if (message.contains("already exists") || 
            message.contains("Contact found") || 
            message.contains("Interaction found")) {
            status = HttpStatus.CONFLICT;
        }
        // Not Found cases (404)
        else if (message.contains("not found") || 
                 message.contains("doesn't exist") || 
                 message.contains("does not exist")) {
            status = HttpStatus.NOT_FOUND;
        }
        // Bad Request cases (400)
        else {
            status = HttpStatus.BAD_REQUEST;
        }
        
        ErrorResponse error = new ErrorResponse(
            status.value(),
            message
        );
        return new ResponseEntity<>(error, status);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        ErrorResponse error = new ErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "An unexpected error occurred"
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
} 