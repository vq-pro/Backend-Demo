package quebec.virtualite.backend.services.utils;

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
public class CustomExceptionHandlerResolver
{
    @ExceptionHandler({
        ConstraintViolationException.class,
        HttpRequestMethodNotSupportedException.class,
        MethodArgumentNotValidException.class
    })
    public ResponseEntity<String> customExceptionHandler(Exception e)
    {
        return new ResponseEntity<>(BAD_REQUEST);
    }
}
