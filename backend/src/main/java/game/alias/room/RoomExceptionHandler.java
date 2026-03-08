package game.alias.room;

import game.alias.common.exception.ApiErrorResponse;
import game.alias.room.domains.RoomException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice(basePackages = "game.alias.room")
@Order(1)
@Slf4j
public class RoomExceptionHandler {

    @ExceptionHandler(RoomException.class)
    public ResponseEntity<ApiErrorResponse> handleRoomException(RoomException ex, HttpServletRequest request) {
        log.warn("Room exception at {}: {}", request.getRequestURI(), ex.getMessage());

        ApiErrorResponse error = ApiErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleRoomNotFound(EntityNotFoundException ex, HttpServletRequest request) {
        log.warn("Entity not found at {}: {}", request.getRequestURI(), ex.getMessage());

        ApiErrorResponse error = ApiErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}