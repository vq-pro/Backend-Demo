package quebec.virtualite.backend.services.utils;

import org.springframework.validation.BindException;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

import javax.validation.ValidatorFactory;

import static javax.validation.Validation.buildDefaultValidatorFactory;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.validation.ValidationUtils.invokeValidator;

public class TestUtils
{
    private TestUtils()
    {
    }

    public static <T> void validateDTO(T dto, int expectedErrors)
    {
        try (ValidatorFactory validatorFactory = buildDefaultValidatorFactory())
        {
            // Given
            BindException errors = new BindException(dto, dto.getClass().getSimpleName());
            SpringValidatorAdapter validator = new SpringValidatorAdapter(validatorFactory.getValidator());

            // When
            invokeValidator(validator, dto, errors);

            // Then
            assertThat(errors.getErrorCount()).isEqualTo(expectedErrors);
        }
    }
}
