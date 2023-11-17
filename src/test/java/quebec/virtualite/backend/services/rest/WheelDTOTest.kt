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
import quebec.virtualite.backend.services.utils.TestUtils.validateDTO

@RunWith(MockitoJUnitRunner::class)
class WheelDTOTest
{
    private val NO_ERRORS = 0
    private val ONE_ERROR = 1
    private val TWO_ERRORS = 2

    private val dto = WheelDTO(BRAND, NAME)

    @Test
    fun validate()
    {
        validateDTO(dto, NO_ERRORS)
        validateDTO(WheelDTO(EMPTY_BRAND, EMPTY_NAME), TWO_ERRORS)
    }

    @Test
    fun validateBrand()
    {
        validateDTO(dto.copy(brand = EMPTY_BRAND), ONE_ERROR)
        validateDTO(dto.copy(brand = NULL_BRAND), ONE_ERROR)
    }

    @Test
    fun validateName()
    {
        validateDTO(dto.copy(name = EMPTY_NAME), ONE_ERROR)
        validateDTO(dto.copy(name = NULL_NAME), ONE_ERROR)
    }
}
