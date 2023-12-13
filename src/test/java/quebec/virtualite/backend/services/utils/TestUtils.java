package quebec.virtualite.backend.services.utils;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ConstraintViolation;
import javax.validation.ValidatorFactory;
import javax.validation.executable.ExecutableValidator;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Set;

import static javax.validation.Validation.buildDefaultValidatorFactory;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.validation.ValidationUtils.invokeValidator;
import static quebec.virtualite.utils.CollectionUtils.list;

public class TestUtils
{
    private TestUtils()
    {
    }

    public static <T> void assertInvalid(T dto)
    {
        assertThat(validateDTO(dto)).isGreaterThanOrEqualTo(1);
    }

    public static <T> void assertInvalid(T tested, String methodName, Object param)
    {
        assertInvalid(tested, methodName, list(param));
    }

    public static <T> void assertInvalid(T tested, String methodName, Object param1,
        Object param2)
    {
        assertInvalid(tested, methodName, list(param1, param2));
    }

    public static <T> void assertInvalid(T tested, String methodName, Object param1,
        Object param2, Object param3)
    {
        assertInvalid(tested, methodName, list(param1, param2, param3));
    }

    public static void assertStatus(Throwable exception, HttpStatus expectedStatus)
    {
        assertThat(exception)
            .isInstanceOf(ResponseStatusException.class)
            .hasFieldOrPropertyWithValue("status", expectedStatus);
    }

    public static <T> void assertValid(T dto)
    {
        assertThat(validateDTO(dto)).isEqualTo(0);
    }

    public static <T> void assertValid(T tested, String methodName, Object param)
    {
        assertValid(tested, methodName, list(param));
    }

    public static <T> void assertValid(T tested, String methodName, Object param1,
        Object param2)
    {
        assertValid(tested, methodName, list(param1, param2));
    }

    public static <T> void assertValid(T tested, String methodName, Object param1,
        Object param2, Object param3)
    {
        assertValid(tested, methodName, list(param1, param2, param3));
    }

    public static <T> String validateController(
        T tested,
        String methodName,
        List<Object> parameterValues)
    {
        try (ValidatorFactory factory = buildDefaultValidatorFactory())
        {
            ExecutableValidator executableValidator = factory
                .getValidator()
                .forExecutables();

            Method method = findMethod(tested, methodName);
            for (int i = 0; i < method.getParameterCount(); i++)
            {
                if (isRequestBody(method, i)
                    && parameterValues.get(i) == null)
                {
                    return "Param " + i + " - Can't have a null for a required RequestBody";
                }
            }

            try
            {
                Set<ConstraintViolation<T>> errors =
                    executableValidator
                        .validateParameters(tested, method, parameterValues.toArray());

                return errors.isEmpty()
                       ? ""
                       : "Validation errors: " + errors;
            }
            catch (Exception e)
            {
                return "Invalid argument type: " + e.getMessage();
            }
        }
    }

    public static <T> int validateDTO(T dto)
    {
        try (ValidatorFactory validatorFactory = buildDefaultValidatorFactory())
        {
            BindException errors = new BindException(dto, dto.getClass().getSimpleName());
            SpringValidatorAdapter validator = new SpringValidatorAdapter(validatorFactory.getValidator());

            invokeValidator(validator, dto, errors);
            return errors.getErrorCount();
        }
    }

    private static <T> void assertInvalid(T tested, String methodName, List<Object> params)
    {
        String message = validateController(tested, methodName, params);
        assertThat(message)
            .withFailMessage("Expecting an error (but didn't get any)")
            .isNotBlank();
    }

    private static <T> void assertValid(T tested, String methodName, List<Object> params)
    {
        String message = validateController(tested, methodName, params);
        assertThat(message)
            .withFailMessage(message)
            .isBlank();
    }

    private static <T> Method findMethod(T tested, String methodName)
    {
        for (Method method : tested.getClass().getMethods())
        {
            if (method.getName().equals(methodName))
                return method;
        }

        throw new AssertionError("Method " + methodName + " not found in class " + tested
            .getClass().getSimpleName());
    }

    private static boolean isRequestBody(Method method, int noParameter)
    {
        Parameter[] methodParameters = method.getParameters();
        RequestBody requestBody = methodParameters[noParameter]
            .getAnnotation(RequestBody.class);

        return requestBody != null && requestBody.required();
    }
}
