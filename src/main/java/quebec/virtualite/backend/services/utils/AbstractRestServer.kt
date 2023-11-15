package quebec.virtualite.backend.services.utils

import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import javax.validation.ConstraintViolationException

abstract class AbstractRestServer
{
    @ExceptionHandler(ConstraintViolationException::class)
    internal fun validationExceptionHandler(e: Exception?): ResponseEntity<String>
    {
        return ResponseEntity(BAD_REQUEST)
    }
}
