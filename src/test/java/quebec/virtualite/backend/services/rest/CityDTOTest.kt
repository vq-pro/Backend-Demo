package quebec.virtualite.backend.services.rest

import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import quebec.virtualite.backend.TestConstants.BAD_CITY_DTO
import quebec.virtualite.backend.TestConstants.CITY_DTO
import quebec.virtualite.backend.services.utils.TestUtils.assertInvalid
import quebec.virtualite.backend.services.utils.TestUtils.assertValid

@RunWith(MockitoJUnitRunner::class)
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
