package quebec.virtualite.backend.services.utils

import org.assertj.core.api.Assertions.assertThat
import org.springframework.validation.BindException
import org.springframework.validation.ValidationUtils.invokeValidator
import org.springframework.validation.beanvalidation.SpringValidatorAdapter
import javax.validation.Validation.buildDefaultValidatorFactory

open class AbstractDTOTest
{
    internal fun validate(dto: Any, expectedErrors: Int)
    {
        buildDefaultValidatorFactory().use { validatorFactory ->

            // Given
            val errors = BindException(dto, dto.javaClass.simpleName)
            val validator = SpringValidatorAdapter(validatorFactory.validator)

            // When
            invokeValidator(validator, dto, errors)

            // Then
            assertThat(errors.errorCount).isEqualTo(expectedErrors)
        }
    }
}

