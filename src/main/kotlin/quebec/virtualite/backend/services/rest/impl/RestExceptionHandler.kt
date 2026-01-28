package quebec.virtualite.backend.services.rest.impl

import jakarta.validation.ConstraintViolationException
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.CONFLICT
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import quebec.virtualite.backend.services.domain.CityAlreadyExistsException

@Component
@ControllerAdvice
class RestExceptionHandler
{
    @ExceptionHandler(CityAlreadyExistsException::class)
    internal fun exceptionHandlerDuplicate(e: Exception?): ResponseEntity<String>
    {
        return ResponseEntity(CONFLICT)
    }

    @ExceptionHandler(
        ConstraintViolationException::class,
        HttpRequestMethodNotSupportedException::class,
        MethodArgumentNotValidException::class,
    )
    fun exceptionHandlerValidation(e: Exception): ResponseEntity<String>
    {
        return ResponseEntity(BAD_REQUEST)
    }
}