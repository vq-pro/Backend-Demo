package quebec.virtualite.backend.services.utils

import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import javax.validation.ConstraintViolationException

@Component
@ControllerAdvice
class CustomExceptionHandlerResolver
{
    @ExceptionHandler(
        ConstraintViolationException::class,
        HttpRequestMethodNotSupportedException::class,
        MethodArgumentNotValidException::class,
    )
    fun customExceptionHandler(e: Exception): ResponseEntity<String>
    {
        return ResponseEntity(BAD_REQUEST)
    }
}