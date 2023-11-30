package quebec.virtualite.backend.services.rest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static quebec.virtualite.backend.TestConstants.WHEEL_DTO;
import static quebec.virtualite.backend.services.utils.TestUtils.validateDTO;

@RunWith(MockitoJUnitRunner.class)
public class WheelDTOTest
{
    @Test
    public void validate()
    {
        validateDTO(0, WHEEL_DTO);

        validateDTO(1, WHEEL_DTO.withBrand(null));
        validateDTO(1, WHEEL_DTO.withBrand(""));

        validateDTO(1, WHEEL_DTO.withName(null));
        validateDTO(1, WHEEL_DTO.withName(""));

        validateDTO(2, new WheelDTO("", ""));
    }
}
