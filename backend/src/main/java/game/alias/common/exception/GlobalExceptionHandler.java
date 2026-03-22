package game.alias.common.exception;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
@Slf4j
public class GlobalExceptionHandler {
        @ExceptionHandler(Exception.class)
        public ResponseEntity<ApiErrorResponse> handResponse(Exception ex, HttpServletRequest request) {
                log.error("Unexpected exception at {} ", request.getRequestURI(), ex);

                ApiErrorResponse error = ApiErrorResponse.builder().status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .message("An unexpected error occured")
                        .build();

                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);

        }

        @ExceptionHandler({IllegalArgumentException.class})
        public ResponseEntity<ApiErrorResponse> handleBadRequest(Exception ex, HttpServletRequest request) {
                ApiErrorResponse error = ApiErrorResponse.builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .message(ex.getMessage())
                        .build();

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }

        @ExceptionHandler(NoHandlerFoundException.class)
        public ResponseEntity<ApiErrorResponse> handle404(HttpServletRequest request, NoHandlerFoundException ex) {
                ApiErrorResponse error = ApiErrorResponse.builder()
                        .status(HttpStatus.NOT_FOUND.value())
                        .message("Resource not found")
                        .build();

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ApiErrorResponse> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
                var fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                        .map(e -> ApiErrorResponse.FieldError.builder()
                                .field(e.getField()).message(e.getDefaultMessage()).build())
                        .toList();

                ApiErrorResponse error = ApiErrorResponse.builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .message("Validation failed")
                        .errors(fieldErrors)
                        .build();

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
}
