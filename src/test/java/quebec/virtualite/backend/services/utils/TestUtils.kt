package quebec.virtualite.backend.services.utils

import org.assertj.core.api.Assertions.assertThat
import org.springframework.http.HttpStatus
import org.springframework.validation.BindException
import org.springframework.validation.ValidationUtils.invokeValidator
import org.springframework.validation.beanvalidation.SpringValidatorAdapter
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.server.ResponseStatusException
import java.lang.reflect.Method
import javax.validation.ConstraintViolation
import javax.validation.Validation.buildDefaultValidatorFactory

object TestUtils
{
    fun assertInvalid(
        controller: Any,
        methodName: String,
        param: Any?
    )
    {
        assertInvalid(controller, methodName, listOf(param))
    }

    fun assertInvalid(
        controller: Any,
        methodName: String,
        param1: Any?,
        param2: Any?
    )
    {
        assertInvalid(controller, methodName, listOf(param1, param2))
    }

    fun assertInvalid(
        controller: Any,
        methodName: String,
        param1: Any?,
        param2: Any?,
        param3: Any?
    )
    {
        assertInvalid(controller, methodName, listOf(param1, param2, param3))
    }

    fun assertInvalid(dto: Any)
    {
        assertThat(validateDTO(dto))
            .withFailMessage("Validation should have failed (but didn't)")
            .isGreaterThanOrEqualTo(1)
    }

    fun assertStatus(exception: Throwable, expectedStatus: HttpStatus)
    {
        assertThat(exception)
            .isInstanceOf(ResponseStatusException::class.java)
            .hasFieldOrPropertyWithValue("status", expectedStatus)
    }

    fun assertValid(
        controller: Any,
        methodName: String,
        param: Any?
    )
    {
        assertValid(controller, methodName, listOf(param))
    }

    fun assertValid(
        controller: Any,
        methodName: String,
        param1: Any?,
        param2: Any?
    )
    {
        assertValid(controller, methodName, listOf(param1, param2))
    }

    fun assertValid(
        controller: Any,
        methodName: String,
        param1: Any?,
        param2: Any?,
        param3: Any?
    )
    {
        assertValid(controller, methodName, listOf(param1, param2, param3))
    }

    fun assertValid(dto: Any)
    {
        assertThat(validateDTO(dto))
            .withFailMessage("Validation failed")
            .isEqualTo(0)
    }

    fun validateController(
        controller: Any,
        methodName: String,
        parameterValues: List<Any?>
    ): String
    {
        buildDefaultValidatorFactory().use { factory ->

            val method: Method = findMethod(controller, methodName)

            if (searchForNullRequiredRequestBody(method, parameterValues) != null)
                return "Can't have a null for a required RequestBody"

            try
            {
                val errors: Set<ConstraintViolation<Any>> = factory
                    .validator
                    .forExecutables()
                    .validateParameters(controller, method, parameterValues.toTypedArray())

                return if (errors.isEmpty()) "" else "Validation errors: $errors"

            } catch (e: Exception)
            {
                return "Invalid argument type: " + e.message
            }
        }
    }

    fun validateDTO(dto: Any): Int
    {
        buildDefaultValidatorFactory().use { validatorFactory ->

            val errors = BindException(dto, dto.javaClass.simpleName)
            val validator = SpringValidatorAdapter(validatorFactory.validator)

            invokeValidator(validator, dto, errors)
            return errors.errorCount
        }
    }

    private fun assertInvalid(
        controller: Any,
        methodName: String,
        params: List<Any?>
    )
    {
        assertThat(validateController(controller, methodName, params))
            .withFailMessage("Expecting an error (but didn't get any)")
            .isNotBlank()
    }

    private fun assertValid(
        controller: Any,
        methodName: String,
        params: List<Any?>
    )
    {
        val message: String = validateController(controller, methodName, params)
        assertThat(message)
            .withFailMessage(message)
            .isBlank()
    }

    private fun findMethod(type: Any, methodName: String): Method
    {
        for (method in type.javaClass.getMethods())
        {
            if (method.name.equals(methodName))
                return method
        }

        throw AssertionError("Method $methodName not found in class ${type.javaClass.simpleName}")
    }

    private fun isRequiredRequestBody(method: Method, noParameter: Int): Boolean
    {
        val methodParameters = method.parameters
        val requestBody: RequestBody? = methodParameters[noParameter]
            .getAnnotation(RequestBody::class.java)

        return requestBody != null && requestBody.required
    }

    private fun searchForNullRequiredRequestBody(
        method: Method,
        parameterValues: List<Any?>
    ): Int?
    {
        for (i in 0..method.parameterCount - 1)
        {
            if (isRequiredRequestBody(method, i)
                && parameterValues[i] == null
            )
            {
                return i
            }
        }

        return null
    }
}
