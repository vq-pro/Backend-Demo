package quebec.virtualite.backend.services.rest.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import quebec.virtualite.backend.services.domain.entities.CityAlreadyExistsException;

import javax.validation.ConstraintViolationException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;

@Component
@ControllerAdvice
@Slf4j
public class RestExceptionHandler
{
    @ExceptionHandler({
        ConstraintViolationException.class,
        HttpRequestMethodNotSupportedException.class,
        MethodArgumentNotValidException.class
    })
    public ResponseEntity<String> exceptionHandlerValidation(Exception e)
    {
        log.warn(e.getMessage());
        return new ResponseEntity<>(BAD_REQUEST);
    }

    @ExceptionHandler(CityAlreadyExistsException.class)
    protected ResponseEntity<String> exceptionHandlerDuplicate()
    {
        return new ResponseEntity<>(CONFLICT);
    }
}
