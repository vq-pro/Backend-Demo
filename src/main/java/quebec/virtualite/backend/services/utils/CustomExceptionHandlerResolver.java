package quebec.virtualite.backend.services.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Component
@ControllerAdvice
@Slf4j
public class CustomExceptionHandlerResolver
{
    @ExceptionHandler({
        ConstraintViolationException.class,
        HttpRequestMethodNotSupportedException.class,
        MethodArgumentNotValidException.class
    })
    public ResponseEntity<String> customExceptionHandler(Exception e)
    {
        log.warn(e.getMessage());
        return new ResponseEntity<>(BAD_REQUEST);
    }
}
