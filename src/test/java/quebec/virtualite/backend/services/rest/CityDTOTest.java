package quebec.virtualite.backend.services.rest;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static quebec.virtualite.backend.TestConstants.BAD_CITY_DTO;
import static quebec.virtualite.backend.TestConstants.CITY_DTO;
import static quebec.virtualite.backend.services.utils.TestUtils.assertInvalid;
import static quebec.virtualite.backend.services.utils.TestUtils.assertValid;

@ExtendWith(MockitoExtension.class)
@Tag("UnitTest")
class CityDTOTest
{
    @Test
    void validate()
    {
        assertValid(CITY_DTO);

        assertInvalid(CITY_DTO.withProvince(null));
        assertInvalid(CITY_DTO.withProvince(""));

        assertInvalid(CITY_DTO.withName(null));
        assertInvalid(CITY_DTO.withName(""));

        assertInvalid(BAD_CITY_DTO);
    }
}
