package game.alias.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestController
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
        @ExceptionHandler(Exception.class)
        public ResponseEntity<ApiErrorResponse> handResponse(Exception ex, HttpServletRequest request) {
                log.error("Caught exception", ex);
                ApiErrorResponse error = ApiErrorResponse.builder().status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                .message("An unexpected error occured:" + ex.getMessage())
                                .build();
                return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);

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
}
