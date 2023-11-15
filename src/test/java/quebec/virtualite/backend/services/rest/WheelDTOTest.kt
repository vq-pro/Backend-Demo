package quebec.virtualite.backend.services.rest

import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import quebec.virtualite.backend.TestConstants.BRAND
import quebec.virtualite.backend.TestConstants.EMPTY_BRAND
import quebec.virtualite.backend.TestConstants.EMPTY_NAME
import quebec.virtualite.backend.TestConstants.NAME
import quebec.virtualite.backend.TestConstants.NULL_BRAND
import quebec.virtualite.backend.TestConstants.NULL_NAME
import quebec.virtualite.backend.services.utils.AbstractDTOTest

@RunWith(MockitoJUnitRunner::class)
class WheelDTOTest : AbstractDTOTest()
{
    private val NO_ERRORS = 0
    private val ONE_ERROR = 1
    private val TWO_ERRORS = 2

    @Test
    fun validate()
    {
        validate(BRAND, NAME, NO_ERRORS)

        validate(EMPTY_BRAND, NAME, ONE_ERROR)
        validate(NULL_BRAND, NAME, ONE_ERROR)

        validate(BRAND, EMPTY_NAME, ONE_ERROR)
        validate(BRAND, NULL_NAME, ONE_ERROR)

        validate(EMPTY_BRAND, EMPTY_NAME, TWO_ERRORS)
    }

    private fun validate(brand: String?, name: String?, expectedErrors: Int)
    {
        validate(
            WheelDTO(brand, name),
            expectedErrors
        )
    }
}
