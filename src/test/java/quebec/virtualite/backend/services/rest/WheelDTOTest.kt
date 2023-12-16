package quebec.virtualite.backend.services.rest

import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import quebec.virtualite.backend.TestConstants.BAD_WHEEL_DTO
import quebec.virtualite.backend.TestConstants.WHEEL_DTO
import quebec.virtualite.backend.services.utils.TestUtils.assertInvalid
import quebec.virtualite.backend.services.utils.TestUtils.assertValid

@RunWith(MockitoJUnitRunner::class)
class WheelDTOTest
{
    @Test
    fun validate()
    {
        assertValid(WHEEL_DTO)

        assertInvalid(WHEEL_DTO.copy(brand = null))
        assertInvalid(WHEEL_DTO.copy(brand = ""))

        assertInvalid(WHEEL_DTO.copy(name = null))
        assertInvalid(WHEEL_DTO.copy(name = ""))

        assertInvalid(BAD_WHEEL_DTO)
    }
}
