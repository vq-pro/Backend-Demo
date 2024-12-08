package quebec.virtualite.backend.services.utils;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ConstraintViolation;
import javax.validation.ValidatorFactory;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.Collections.addAll;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static javax.validation.Validation.buildDefaultValidatorFactory;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.validation.ValidationUtils.invokeValidator;

public class TestUtils
{
    private TestUtils()
    {
    }

    public static <CTRL> void assertInvalid(CTRL controller, String methodName, Object param)
    {
        assertInvalid(controller, methodName, listOfNullable(param));
    }

    public static <CTRL> void assertInvalid(CTRL controller, String methodName, Object param1,
        Object param2)
    {
        assertInvalid(controller, methodName, listOfNullable(param1, param2));
    }

    public static <CTRL> void assertInvalid(CTRL controller, String methodName, Object param1,
        Object param2, Object param3)
    {
        assertInvalid(controller, methodName, listOfNullable(param1, param2, param3));
    }

    public static <DTO> void assertInvalid(DTO dto)
    {
        assertThat(validateDTO(dto))
            .withFailMessage("Validation should have failed (but didn't)")
            .isGreaterThanOrEqualTo(1);
    }

    public static void assertStatus(Throwable exception, HttpStatus expectedStatus)
    {
        assertThat(exception)
            .isInstanceOf(ResponseStatusException.class)
            .hasFieldOrPropertyWithValue("status", expectedStatus);
    }

    public static <CTRL> void assertValid(CTRL controller, String methodName, Object param)
    {
        assertValid(controller, methodName, listOfNullable(param));
    }

    public static <CTRL> void assertValid(CTRL controller, String methodName, Object param1,
        Object param2)
    {
        assertValid(controller, methodName, listOfNullable(param1, param2));
    }

    public static <CTRL> void assertValid(CTRL controller, String methodName, Object param1,
        Object param2, Object param3)
    {
        assertValid(controller, methodName, listOfNullable(param1, param2, param3));
    }

    public static <DTO> void assertValid(DTO dto)
    {
        assertThat(validateDTO(dto))
            .withFailMessage("Validation failed")
            .isEqualTo(0);
    }

    public static <CTRL> String validateController(
        CTRL controller,
        String methodName,
        List<Object> parameterValues)
    {
        try (ValidatorFactory factory = buildDefaultValidatorFactory())
        {
            Method method = findMethod(controller, methodName);

            if (searchForNullRequiredRequestBody(method, parameterValues).isPresent())
                return "Can't have a null for a required RequestBody";

            try
            {
                Set<ConstraintViolation<CTRL>> errors = factory
                    .getValidator()
                    .forExecutables()
                    .validateParameters(controller, method, parameterValues.toArray());

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

    public static <DTO> int validateDTO(DTO dto)
    {
        try (ValidatorFactory validatorFactory = buildDefaultValidatorFactory())
        {
            BindException errors = new BindException(dto, dto.getClass().getSimpleName());
            SpringValidatorAdapter validator = new SpringValidatorAdapter(validatorFactory.getValidator());

            invokeValidator(validator, dto, errors);
            return errors.getErrorCount();
        }
    }

    private static <CTRL> void assertInvalid(
        CTRL controller, String methodName, List<Object> params)
    {
        assertThat(validateController(controller, methodName, params))
            .withFailMessage("Expecting an error (but didn't get any)")
            .isNotBlank();
    }

    private static <CTRL> void assertValid(CTRL controller, String methodName,
        List<Object> params)
    {
        String message = validateController(controller, methodName, params);
        assertThat(message)
            .withFailMessage(message)
            .isBlank();
    }

    private static <CLS> Method findMethod(CLS type, String methodName)
    {
        for (Method method : type.getClass().getMethods())
        {
            if (method.getName().equals(methodName))
                return method;
        }

        throw new AssertionError("Method " + methodName + " not found in class " + type
            .getClass().getSimpleName());
    }

    private static boolean isRequiredRequestBody(Method method, int noParameter)
    {
        Parameter[] methodParameters = method.getParameters();
        RequestBody requestBody = methodParameters[noParameter]
            .getAnnotation(RequestBody.class);

        return requestBody != null && requestBody.required();
    }

    @SafeVarargs
    private static <T> List<T> listOfNullable(T... items)
    {
        ArrayList<T> list = new ArrayList<>();
        addAll(list, items);

        return list;
    }

    private static Optional<Integer> searchForNullRequiredRequestBody(Method method,
        List<Object> parameterValues)
    {
        for (int i = 0; i < method.getParameterCount(); i++)
        {
            if (isRequiredRequestBody(method, i)
                && parameterValues.get(i) == null)
            {
                return of(i);
            }
        }

        return empty();
    }
}
