package quebec.virtualite.backend.services.rest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static quebec.virtualite.backend.TestConstants.BAD_WHEEL_DTO;
import static quebec.virtualite.backend.TestConstants.WHEEL_DTO;
import static quebec.virtualite.backend.services.utils.TestUtils.assertInvalid;
import static quebec.virtualite.backend.services.utils.TestUtils.assertValid;

@ExtendWith(MockitoExtension.class)
class WheelDTOTest
{
    @Test
    void validate()
    {
        assertValid(WHEEL_DTO);

        assertInvalid(WHEEL_DTO.withBrand(null));
        assertInvalid(WHEEL_DTO.withBrand(""));

        assertInvalid(WHEEL_DTO.withName(null));
        assertInvalid(WHEEL_DTO.withName(""));

        assertInvalid(BAD_WHEEL_DTO);
    }
}
