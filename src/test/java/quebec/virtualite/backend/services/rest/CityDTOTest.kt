package quebec.virtualite.backend.services.rest

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import quebec.virtualite.backend.TestConstants.BAD_CITY_DTO
import quebec.virtualite.backend.TestConstants.CITY_DTO
import quebec.virtualite.backend.services.utils.TestUtils.assertInvalid
import quebec.virtualite.backend.services.utils.TestUtils.assertValid

@ExtendWith(MockitoExtension::class)
class CityDTOTest
{
    @Test
    fun validate()
    {
        assertValid(CITY_DTO)

        assertInvalid(CITY_DTO.copy(name = null))
        assertInvalid(CITY_DTO.copy(name = ""))

        assertInvalid(CITY_DTO.copy(province = null))
        assertInvalid(CITY_DTO.copy(province = ""))

        assertInvalid(BAD_CITY_DTO)
    }
}
