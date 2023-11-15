package quebec.virtualite.backend.services.utils;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public abstract class AbstractRestServer
{
    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<String> exceptionHandler(Exception e)
    {
        return new ResponseEntity<>(BAD_REQUEST);
    }
}
