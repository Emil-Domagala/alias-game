package game.alias.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@RestController
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
        @ExceptionHandler(Exception.class)
        public ResponseEntity<ApiErrorResponse> handResponse(Exception ex, HttpServletRequest request) {
                log.error("Caught exception", ex);
                ApiErrorResponse error = ApiErrorResponse.builder().status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                .message("An unexpected error occured:" + ex.getMessage()).path(request.getRequestURI())
                                .build();
                return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);

        }
}
